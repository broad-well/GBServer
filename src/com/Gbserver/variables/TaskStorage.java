package com.Gbserver.variables;

public class TaskStorage {
	private Object toStore;
	
	public TaskStorage(Object toStore){
		this.setStorage(toStore);
	}

	public Object getStorage() {
		return toStore;
	}

	public void setStorage(Object toStore) {
		this.toStore = toStore;
	}
	
}
