package edu.brown.cs32.siliclone.client.workspace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.brown.cs32.siliclone.operators.Operator;

@SuppressWarnings("serial")
public class BasicWorkspace implements Workspace {
	
	private HashMap <String, Object> properties;
	private List<Operator> operators;
	private String name;
	private boolean hasBeenSavedBefore;
	
	@SuppressWarnings("unused")
	private BasicWorkspace() {
		//constructor to permit GWT serialization
	}

	public BasicWorkspace(String name) {
		this.properties = new HashMap<String, Object>();
		this.operators = new ArrayList<Operator>();
		this.name = name;
		this.hasBeenSavedBefore = true;
	}

	public void addOperator(Operator o) {
		operators.add(o);
	}

	public void addProperty(String key, Object value) {
		properties.put(key, value);
	}

	public List<Operator> getOperators() {
		return operators;
	}

	public Object getProperty(String key) {
		return properties.get(key);
	}

	public void removeOperator(Operator o) {
		operators.remove(o);
	}

	public void removeProperty(String key) {
		properties.remove(key);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getHasBeenSavedBefore() {
		return hasBeenSavedBefore;
	}

	public void setHasBeenSavedBefore(boolean savedBefore) {
		this.hasBeenSavedBefore = savedBefore;
	}


}
