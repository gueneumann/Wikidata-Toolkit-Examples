package gntests;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.bliki.wiki.dump.IArticleFilter;
import info.bliki.wiki.dump.Siteinfo;
import info.bliki.wiki.dump.WikiArticle;
import info.bliki.wiki.dump.WikiXMLParser;
import info.bliki.wiki.filter.PlainTextConverter;
import info.bliki.wiki.model.WikiModel;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.xml.sax.SAXException;

public class Wikipedia2Txt {

	// Dumpfile
	public static File dumpFile ;
	// GN: added on March, 2016
	public static BufferedWriter outStream = null;
	static int sentCnt = 0;
	static int pageCnt = 0;
	static int sentMod = 100000;
	static int pageMod = 10000;

	/*
	 * From: https://bitbucket.org/axelclk/info.bliki.wiki/wiki/MediaWikiDumpSupport
	 */
	static class DemoArticleFilter implements IArticleFilter {

		public void process(WikiArticle page, Siteinfo siteinfo) {

			System.out.println("----------------------------------------");
			System.out.println(page.getTitle());
			System.out.println("----------------------------------------");
			System.out.println(page.getText());
		}
	}

	static class GNArticleFilter implements IArticleFilter {

		public void process(WikiArticle page, Siteinfo siteinfo) {

			if (page.isMain()){
				System.out.println("---------------- IS MAIN ------------------------");
				System.out.println(page.getTitle());
				System.out.println(page.getText());
				System.out.println("----------------------------------------");		
			}
			else {
				System.out.println("---------------- IS OTHER ------------------------");
				System.out.println(page.getTitle());
				System.out.println("----------------------------------------");		
			}
		}
	}

	/*
	 * http://trulymadlywordly.blogspot.de/2011/03/creating-text-corpus-from-wikipedia.html
	 */
	/**
	 * Print title an content of all the wiki pages in the dump.
	 * 
	 */

	// The version from the original method:

	// "[A-Z][\\p{L}\\w\\p{Blank},\\\"\\';\\[\\]\\(\\)-]+[\\.!]"
	// Its structure:
	// [A-Z] -> Starts with capital character 
	// \p{L} for Unicode letters, \p{N} for Unicode digits -> from http://www.regular-expressions.info/unicode.html#prop
	// [\\p{L}\\w\\p{Blank},\\\"\\';\\[\\]\\(\\)-] -> ?
	// [\\.!] -> ends with . or !

	// why the Pattern.CANON_EQ
	// If you are using Java, you can pass the CANON_EQ flag as the second parameter to Pattern.compile(). 
	// This tells the Java regex engine to consider canonically equivalent characters as identical. 
	// The regex à encoded as U+00E0 matches à encoded as U+0061 U+0300, and vice versa. 
	// None of the other regex engines currently support canonical equivalence while matching.

	// TODO
	// THis is the problematic case because it is too restricted, does not work for Hindi Russia ?
	// Hindi: p{IsDevanagari} danda bzw. double danda als Satzende: \u0964 \0965 BUT not ? or !
	// "[\\p{IsDevanagari}][\\p{InDevanagari}\\p{L}\\w\\p{Blank},\\\"\\';\\[\\]\\(\\)-]+[\u0964\u0965]"


	static class TextArticleFilter implements IArticleFilter {
		String dumpFileLanguagePrefix = Wikipedia2Txt.dumpFile.getName().substring(0, 2);

		String languagePatternString = this.getLanguageSentenceRegPattern(dumpFileLanguagePrefix);


		final Pattern regex = Pattern.compile(languagePatternString,Pattern.CANON_EQ);

		// Convert to plain text
		WikiModel wikiModel = new WikiModel("${image}", "${title}");

		public String getLanguageSentenceRegPattern(String langID){

			String patternString = "";
			switch (langID) {
			case "hi" : patternString = 
					"[\\p{IsDevanagari}][\\p{InDevanagari}\\p{L}\\w\\p{Blank},\\\"\\';\\[\\]\\(\\)-]+[\u0964\u0965]";
			break;
			case "ko" : patternString = 
					"[\\p{IsHangul}][\\p{L}\\w\\p{Blank},\\\"\\';\\[\\]\\(\\)-]+[\\.!]";
			break;
			case "ru" : patternString = 
					"[\\p{IsCyrillic}][\\p{L}\\w\\p{Blank},\\\"\\';\\[\\]\\(\\)-]+[\\.!]";
			break;
			case "bg" : patternString = 
					"[\\p{IsCyrillic}][\\p{L}\\w\\p{Blank},\\\"\\';\\[\\]\\(\\)-]+[\\.!]";
			break;
			default : patternString =
					"[A-Z][\\p{L}\\w\\p{Blank},\\\"\\';\\[\\]\\(\\)-]+[\\.!]";
			break;

			}
			return patternString;
		}

		public void process(WikiArticle page, Siteinfo siteinfo) throws IOException {


			if (page != null && page.getText() != null && !page.getText().startsWith("#REDIRECT ")){

				PrintStream out = null;

				try {
					out = new PrintStream(System.out, true, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Zap headings ==some text== or ===some text===

				// <ref>{{Cite web|url=http://tmh.floonet.net/articles/falseprinciple.html |title="The False Principle of our Education" by Max Stirner |publisher=Tmh.floonet.net |date= |accessdate=2010-09-20}}</ref>
				// <ref>Christopher Gray, ''Leaving the Twentieth Century'', p. 88.</ref>
				// <ref>Sochen, June. 1972. ''The New Woman: Feminism in Greenwich Village 1910Ð1920.'' New York: Quadrangle.</ref>

				// String refexp = "[A-Za-z0-9+\\s\\{\\}:_=''|\\.\\w#\"\\(\\)\\[\\]/,?&%Ð-]+";



				String wikiText = page.getText().
						replaceAll("[=]+[A-Za-z+\\s-]+[=]+", " ").
						replaceAll("\\{\\{[A-Za-z0-9+\\s-]+\\}\\}"," ").
						replaceAll("(?m)<ref>.+</ref>"," ").
						replaceAll("(?m)<ref name=\"[A-Za-z0-9\\s-]+\">.+</ref>"," ").
						replaceAll("<ref>"," <ref>");



				// GN: added on March, 2016
				pageCnt++;
				if ((pageCnt % pageMod) == 0) System.out.println("Pages: " + pageCnt);

				// Remove text inside {{ }}
				String plainStr = wikiModel.render(new PlainTextConverter(), wikiText).
						replaceAll("\\{\\{[A-Za-z+\\s-]+\\}\\}"," ");

				// It has all text! also for Hindi
				//System.out.println(plainStr);

				Matcher regexMatcher = regex.matcher(plainStr);
				while (regexMatcher.find())
				{
					// Get sentences with 6 or more words
					String sentence = regexMatcher.group();

					if (matchSpaces(sentence, 5)) {

						System.out.println(sentence);


						outStream.write(sentence);
						outStream.newLine();
						// GN: added on March, 2016
						sentCnt++;
						if ((sentCnt % sentMod) == 0) System.out.println(" ... Sents: " + sentCnt);
					}
				}

			}
		}

		private boolean matchSpaces(String sentence, int matches) {

			int c =0;
			for (int i=0; i< sentence.length(); i++) {
				if (sentence.charAt(i) == ' ') c++;
				if (c == matches) return true;
			}
			return false;
		}

	}

	public static BufferedWriter getBufferedWriterForTextFile(String fileOut) throws FileNotFoundException, CompressorException, UnsupportedEncodingException {
		FileOutputStream fout = new FileOutputStream(fileOut);
		BufferedOutputStream bout = new BufferedOutputStream(fout);
		CompressorOutputStream out = new CompressorStreamFactory().createCompressorOutputStream("bzip2", bout);
		BufferedWriter br2 = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
		return br2;
	}

	/**
	 * @param args
	 * @throws SAXException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws CompressorException 
	 * @throws ParserConfigurationException 
	 */

	/*
	 * Arg1: dumpfile
	 * Arg2: output.txt
	 */
	public static void main(String[] args) throws IOException, SAXException, CompressorException {

		Wikipedia2Txt.dumpFile = new File(args[0]);

		// Directly compress extracted text file
		Wikipedia2Txt.outStream = Wikipedia2Txt.getBufferedWriterForTextFile(args[1]);

		// Do not compress - I am using this for testing, because at least for frwiki I have termination problems
		// Wikipedia2Txt.outStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), "utf-8"));

		WikiXMLParser wxp = new WikiXMLParser(Wikipedia2Txt.dumpFile, new TextArticleFilter());

		wxp.parse();

		outStream.close();

	}

}
