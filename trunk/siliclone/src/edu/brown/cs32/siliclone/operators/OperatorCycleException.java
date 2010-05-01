package edu.brown.cs32.siliclone.operators;

public class OperatorCycleException extends Exception {
	private String message;
	
	public OperatorCycleException(){
		message = "Cycles not permitted.";
	}
	
	public OperatorCycleException(String message){
		this.message = message;
	}
	
	public String getMessage(){
		return message;
	}
}
