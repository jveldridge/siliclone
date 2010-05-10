package edu.brown.cs32.siliclone.server.operators.abstractoperator;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.xml.internal.ws.message.saaj.SAAJHeader;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.client.operators.abstractremoteoperator.AbstractRemoteOperatorService;
import edu.brown.cs32.siliclone.client.operators.abstractremoteoperator.BadComputationHookException;
import edu.brown.cs32.siliclone.client.operators.abstractremoteoperator.ComputationHook;
import edu.brown.cs32.siliclone.tasks.SquareTask;
import edu.brown.cs32.siliclone.tasks.client.TaskClient;
import edu.brown.cs32.siliclone.tasks.client.TaskTimedOutException;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public abstract class AbstractRemoteOperatorServiceImpl extends RemoteServiceServlet implements
		AbstractRemoteOperatorService {
	
	protected abstract OperatorComputer getNewOperatorComputer();
	
	private void setResult(ComputationHook ch, Collection<SequenceHook> sh){
		if(
				getThreadLocalRequest().getSession().getAttribute("finishedComputations") ==null){
			getThreadLocalRequest().getSession().setAttribute("finishedComputations",
					new HashMap<ComputationHook, Collection<SequenceHook>>());
		}
		if(((HashMap<ComputationHook, OperatorComputer>)getThreadLocalRequest().getSession().getAttribute("runningOperatorComputers")).containsKey(ch)){
		((HashMap<ComputationHook, Collection<SequenceHook>>)
		getThreadLocalRequest().getSession().getAttribute("finishedComputations")).put(ch,sh);
		}
	}
	
	public ComputationHook startComputation(final Collection<SequenceHook>[] input,
			final Map properties) throws IllegalArgumentException {
		
		final OperatorComputer oc = getNewOperatorComputer();
		final ComputationHook ch = new ComputationHook();
		
		if(this.getThreadLocalRequest().getSession().getAttribute("runningOperatorComputers") ==null){
			this.getThreadLocalRequest().getSession().setAttribute("runningOperatorComputers",
					new HashMap<ComputationHook, OperatorComputer>());
		}
		
		((HashMap<ComputationHook, OperatorComputer>)
		this.getThreadLocalRequest().getSession().getAttribute("runningOperatorComputers")).put(ch, oc);
		
		
		new Thread(new Runnable() {
			private HttpSession session;
			{
			session = getThreadLocalRequest().getSession();
			}
			public void run() {
				Collection<SequenceHook> hooks = oc.computeOutput(input, properties);
				if(
						session.getAttribute("finishedComputations") ==null){
					session.setAttribute("finishedComputations",
							new HashMap<ComputationHook, Collection<SequenceHook>>());
				}
				if(((HashMap<ComputationHook, OperatorComputer>)session.getAttribute("runningOperatorComputers")).containsKey(ch)){
				((HashMap<ComputationHook, Collection<SequenceHook>>)
				session.getAttribute("finishedComputations")).put(ch,hooks);
				}
			}
		}).start();
		
		
		
		
		return ch;
	}
	
	/**
	 */
	public Integer getProgress(ComputationHook hook) throws BadComputationHookException{
		Object computers = this.getThreadLocalRequest().getSession().getAttribute("runningOperatorComputers");
		if (computers==null){
			throw new BadComputationHookException();
		}
		OperatorComputer computer =((HashMap<ComputationHook, OperatorComputer>)computers).get(hook);
		if(computer==null){
			throw new BadComputationHookException();
		}
		return computer.getProgress();
	}


	
/**
 * 
 * @param hook a computationhook
 * @return the result of the computation
 * @throws BadComputationHookException when the computationhook is not registered (e.g. you just called getResult)
 */
	public Collection<SequenceHook> getResult(ComputationHook hook) throws BadComputationHookException{

		Object allresults = this.getThreadLocalRequest().getSession().getAttribute("finishedComputations");
		if (allresults==null){
			throw new BadComputationHookException();
		}
		Collection<SequenceHook> result =((HashMap<ComputationHook, Collection<SequenceHook>>)allresults).remove(hook);
		/*if(result==null){
			throw new BadComputationHookException();
		}*/
		Object computers = this.getThreadLocalRequest().getSession().getAttribute("runningOperatorComputers");
		if (computers==null){
			throw new BadComputationHookException();
		}
		((HashMap<ComputationHook, OperatorComputer>)computers).remove(hook);
		LinkedList<SequenceHook> r = new LinkedList<SequenceHook>();
		r.addAll(result);
		return r;
	}
	
	public void cancelComputation(ComputationHook hook) throws BadComputationHookException{
		
		
		Object computers = this.getThreadLocalRequest().getSession().getAttribute("runningOperatorComputers");
		if (computers==null){
			throw new BadComputationHookException();
		}
		OperatorComputer computer = ((HashMap<ComputationHook, OperatorComputer>)computers).remove(hook);
		if(computer!=null)computer.cancel();
		Object allresults = this.getThreadLocalRequest().getSession().getAttribute("finishedComputations");
		if (allresults==null){
			throw new BadComputationHookException();
		}
		((HashMap<ComputationHook, Collection<SequenceHook>>)allresults).remove(hook);
		
	}
	
	
		



}
