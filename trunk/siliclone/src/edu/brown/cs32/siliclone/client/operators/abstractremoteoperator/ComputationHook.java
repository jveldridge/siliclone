package edu.brown.cs32.siliclone.client.operators.abstractremoteoperator;

import java.io.Serializable;
import java.util.Random;

public class ComputationHook implements Serializable {
	
	private int hash;
	private int hash2;

	
	public ComputationHook(){
		hash = super.hashCode();
		hash2 = new Random().nextInt(Integer.MAX_VALUE);
	}
	
	public int hashCode(){
		return hash;
	}
	
	public boolean equals(Object o){
		if(o instanceof ComputationHook){
			ComputationHook ch = (ComputationHook)o;
			return (ch.hash==this.hash&&ch.hash2==this.hash2);
		}else{
			return false;
		}
		
	}
	

}
