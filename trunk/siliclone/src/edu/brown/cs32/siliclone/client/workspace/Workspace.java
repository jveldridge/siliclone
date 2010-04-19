package edu.brown.cs32.siliclone.client.workspace;

import java.io.Serializable;
import java.util.List;

import edu.brown.cs32.siliclone.operators.Operator;

public interface Workspace extends Serializable {

	/**
	 * @param o The operator to be added (not null)
	 */
	public void addOperator(Operator o);
	/**
	 * @param o The operator to be removed. (not null)
	 */
	public void removeOperator(Operator o);
	/**
	 * @return A list of all operators in this workspace. not null
	 */
	public List<Operator> getOperators();
	/**
	 * @param key The name of the property to be added. not null
	 * @param Value A serializable object. not null
	 */
	public void addProperty(String key, Object Value);
	/**
	 * @param key The name of the property to be removed. (not null)
	 */
	public void removeProperty(String key);
	/**
	 * @param key The name of the property to be accessed. not null
	 * @return The property with the given name. null if this 
	 * 		 workspace has no property with that name.
	 */
	public Object getProperty(String key);
}
