package edu.brown.cs32.siliclone.client.dna.features;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class BasicFeature implements Feature {
	
	private int startPos, endPos;
	private String type;
	private IsSerializable value;

	public int getStartPosition() {
		return startPos;
	}
	
	public int getEndPosition() {
		return endPos;
	}

	public String getType() {
		return type;
	}

	public IsSerializable getValue() {
		return value;
	}

}
