package com.example.example;

class Model {
	
	private int id;
	private String name;
	private boolean finished;
	
	
	Model(int id, String name, boolean finished){
		this.id			= id;
		this.name 		= name;
		this.finished 	= finished;
	}
	
	//----------------------(get)------------------------------------------------------
	String getName(){
		return this.name;
	}
	
	boolean getValue(){
		return this.finished;
	}
	
	int getId(){
		return this.id;
	}
	
	//----------------------(set)------------------------------------------------------
}
