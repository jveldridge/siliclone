package edu.brown.cs32.siliclone.server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/*****************************************************
 Amino Acid Preference Toolkit in Java
 Pathogen Project
 Department of Computer Science and Engineering
 University of South Carolina
 Columbia, SC 29208
 Contact Email: rose@cse.sc.edu
*****************************************************/

//TODO add validation
public class FastaParser {

    int length;
    String header;

    public FastaParser() {
		length = 0;
		header = null;
    }
    
    public String parseFastaFile(InputStream inputStream) {
    	BufferedReader bufferedReader;
		String line;
		StringBuffer stringBuffer = new StringBuffer("");
		
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			while ((line = bufferedReader.readLine()) != null) {
				if (!line.startsWith(">")) {
					line = line.trim();
					stringBuffer.append(line);
				}
				else if (line.startsWith(">")) {
					header = line;
				}
			}
	
			length = stringBuffer.length();
			bufferedReader.close();
		}
		catch (Exception exception) {
			System.out.println(exception);
			exception.printStackTrace();
		}	
	
		return stringBuffer.toString();
    }

    public int getLength() {
    	return length;
    }
    
    public String getHeader() {
    	return header;
    }
    
}