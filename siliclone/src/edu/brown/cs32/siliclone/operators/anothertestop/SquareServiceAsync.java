package edu.brown.cs32.siliclone.operators.anothertestop;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SquareServiceAsync {

	void square(Integer in, AsyncCallback<Integer> callback);

}
