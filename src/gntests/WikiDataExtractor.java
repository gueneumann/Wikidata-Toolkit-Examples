package gntests;

/**
 * See also /GNT/src/main/java/corpus/WikiPediaConllReader.java
 * Can also used code from examples/
 * Actually same as: /wdtk_examples/src/examples/JsonSerializationProcessor.java
 * 
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

import com.fasterxml.jackson.databind.ObjectMapper;


public class WikiDataExtractor {

	public static BufferedReader getBufferedReaderForCompressedFile(String fileIn) throws FileNotFoundException, CompressorException {
		FileInputStream fin = new FileInputStream(fileIn);
		BufferedInputStream bis = new BufferedInputStream(fin);
		CompressorInputStream input = new CompressorStreamFactory().createCompressorInputStream(bis);
		BufferedReader br2 = new BufferedReader(new InputStreamReader(input));
		return br2;
	}
	
	public static BufferedWriter getBufferedWriterForTextFile(String fileOut) throws FileNotFoundException, CompressorException, UnsupportedEncodingException {
		FileOutputStream fout = new FileOutputStream(fileOut);
		BufferedOutputStream bout = new BufferedOutputStream(fout);
		CompressorOutputStream out = new CompressorStreamFactory().createCompressorOutputStream("bzip2", bout);
		BufferedWriter br2 = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
		return br2;
	}

	public void extract() throws IOException, CompressorException {
		// temporally store on local machine, then faster processing
		String archiveName = "/Volumes/data2/WikiData/latest-all.json.bz2";
		BufferedReader reader = WikiDataExtractor.getBufferedReaderForCompressedFile(archiveName);
		String line = "";
		int lineCnt = 0;
		int mod = 50000;

		ObjectMapper mapper = new ObjectMapper();

		while ((line = reader.readLine()) != null) {
			if (!(line.equals("[") || line.equals("]"))){
				Map<String,Object> jsonObject = mapper.readValue(line, Map.class); 
				if ((lineCnt % mod) == 0) {
					System.out.println(jsonObject.get("id"));
					System.out.println(line);
				}
			}
			lineCnt++;
		}
		reader.close();
	}
	
	public void testJsonSerializer(){
	}

	public static void main(String[] args) throws IOException, CompressorException{
		WikiDataExtractor wdextractor = new WikiDataExtractor();

		wdextractor.extract();
	}

}
