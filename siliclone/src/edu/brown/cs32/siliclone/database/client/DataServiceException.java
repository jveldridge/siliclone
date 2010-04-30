package edu.brown.cs32.siliclone.database.client;

/**
 * A simple exception that is essentially the same as java.lang.exception
 *  explicitly acts as throwable, serializable wrapper from some error message.
 */
public class DataServiceException extends Exception {
	private String message;
	
	@SuppressWarnings("unused")
	private DataServiceException(){}
	
	public DataServiceException(String message){
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
}
