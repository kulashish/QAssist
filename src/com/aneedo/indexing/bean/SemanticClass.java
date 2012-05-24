package com.aneedo.indexing.bean;

public class SemanticClass {
	String name;
	StringBuilder words = new StringBuilder();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public StringBuilder getWords() {
		return words;
	}
	public void setWords(StringBuilder words) {
		this.words = words;
	}
	
	public void clear() {
		words.delete(0, words.length());
		name = null;
	}

}
