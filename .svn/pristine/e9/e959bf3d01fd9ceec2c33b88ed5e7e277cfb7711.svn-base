package com.aneedo.search.bean;

import com.aneedo.indexing.IndexingConstants;


public class EntityWordBean {
	String word;
	Integer wordId;
	
	int totalFreq;
	double[] titleMatch;
	double[] queryMatch;
	boolean[] semClassMatch = new boolean[IndexingConstants.NUM_OF_SEM_CLASSES];
	
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	
	public Integer getWordId() {
		return wordId;
	}
	public void setWordId(Integer wordId) {
		this.wordId = wordId;
	}
	public boolean[] getSemClassMatch() {
		return semClassMatch;
	}
	public void setSemClassMatch(boolean[] semClassMatch) {
		this.semClassMatch = semClassMatch;
	}
	public int getTotalFreq() {
		return totalFreq;
	}
	public void setTotalFreq(int totalFreq) {
		this.totalFreq = totalFreq;
	}
	public double[] getTitleMatch() {
		return titleMatch;
	}
	public void setTitleMatch(double[] titleMatch) {
		this.titleMatch = titleMatch;
	}
	public double[] getQueryMatch() {
		return queryMatch;
	}
	public void setQueryMatch(double[] queryMatch) {
		this.queryMatch = queryMatch;
	}
	
}
