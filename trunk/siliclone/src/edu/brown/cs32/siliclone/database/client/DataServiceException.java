package edu.brown.cs32.siliclone.database.client;

/**
 * A simple exception that is essentially the same as java.lang.exception
 *  explicitly acts as throwable, serializable wrapper from some error message.
 */
public class DataServiceException extends Exception {
	private String message;
	
	@SuppressWarnings("unused")
	private DataServiceException(){} //for RPC serialization only
	
	/**
	 * Constructs an exception with the given message returned by getMessage()
	 * @param message The message to return (not null)
	 */
	public DataServiceException(String message){
		this.message = message;
	}
	
	/**
	 * @return the message set during construction.
	 */
	public String getMessage() {
		return message;
	}
	
}
