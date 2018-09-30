package edu.self.types;

public enum Type {
	$("",     4, 3),
	//$M("M",  4, 3),
	$m("m",   3, 4),
	$6("6",   3, 5),
	$m6("m6", 4, 5),
	$7("7",   4, 3, 3),
	$m7("m7", 3, 4, 3);
	
	private String name;
	private int[] distances;

	Type(String name, int ... distances){
		this.name = name;
		this.distances = distances;
	}
	
	public String getName(){
		return name;
	}
	
	public int[] getDistances() {
		return distances;
	}
	
	/*
	setType("",   4, 3);
	//setChordType("M",  4, 3);
	setType("m",  3, 4);
	setType("7",  4, 3, 3);
	setType("m7", 3, 4, 3);
	*/
}
