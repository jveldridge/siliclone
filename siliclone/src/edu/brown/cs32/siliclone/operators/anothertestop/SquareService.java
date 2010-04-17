package edu.brown.cs32.siliclone.operators.anothertestop;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("square")
public interface SquareService extends RemoteService {
	Integer square(Integer in) throws IllegalArgumentException;
}
