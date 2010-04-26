package edu.brown.cs32.siliclone.client.workspace;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.brown.cs32.siliclone.operators.Operator;


public class BasicWorkspace implements Workspace {
	
	private static final long serialVersionUID = 1L;
	
	private HashMap <String, Object> _properties;
	private List<Operator> _operators;
	private String _name;

	public BasicWorkspace(String name) {
		_properties = new HashMap<String, Object>();
		_operators = new ArrayList<Operator>();
		_name = name;
	}

	public void addOperator(Operator o) {
		_operators.add(o);
	}

	public void addProperty(String key, Object value) {
		_properties.put(key, value);
	}

	public List<Operator> getOperators() {
		return _operators;
	}

	public Object getProperty(String key) {
		return _properties.get(key);
	}

	public void removeOperator(Operator o) {
		_operators.remove(o);
	}

	public void removeProperty(String key) {
		_properties.remove(key);
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}


}
