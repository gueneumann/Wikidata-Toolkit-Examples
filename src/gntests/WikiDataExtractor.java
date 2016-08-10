package gntests;

/**
 * See also /GNT/src/main/java/corpus/WikiPediaConllReader.java
 * Can also used code from examples/
 * Actually same as: /wdtk_examples/src/examples/JsonSerializationProcessor.java
 * 
 */

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
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

	public void extract() throws IOException, CompressorException {
		String archiveName = "/Volumes/data2/WikiData/latest-all.json.bz2";
		BufferedReader reader = WikiDataExtractor.getBufferedReaderForCompressedFile(archiveName);
		String line = "";
		int lineCnt = 0;
		int mod = 10000;

		ObjectMapper mapper = new ObjectMapper();

		while ((line = reader.readLine()) != null) {
			if (!line.equals("[")){
				Map<String,Object> jsonObject = mapper.readValue(line, Map.class); 
				if ((lineCnt % mod) == 0) {
					System.out.println(lineCnt);
					System.out.println(jsonObject.get("id"));
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
