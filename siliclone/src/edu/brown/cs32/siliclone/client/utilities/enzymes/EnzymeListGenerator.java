package edu.brown.cs32.siliclone.client.utilities.enzymes;

import java.util.LinkedHashMap;

public final class EnzymeListGenerator {
	
	private EnzymeListGenerator() {}
	
	public final static LinkedHashMap<String, String> pcrEnzymeList() {
		
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String,String>();
		valueMap.put("Taq", "Taq");
		valueMap.put("Pfu", "Pfu");
		
		return valueMap;
	}

}
