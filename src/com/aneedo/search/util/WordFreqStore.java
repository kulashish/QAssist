package com.aneedo.search.util;

import gnu.trove.TIntObjectHashMap;
import gnu.trove.TObjectIntHashMap;

public class WordFreqStore {
	
	double[] tfIDF;
	double[] DF;
	TObjectIntHashMap<String> topKTFIDF;
	TObjectIntHashMap<String> topKDF;
	TObjectIntHashMap<String> wordToIdMap = null;

	
	public WordFreqStore(double[] tfIDF,
	double[] DF,
	TObjectIntHashMap<String> topKTFIDF,
	TObjectIntHashMap<String> topKDF) {
		this.tfIDF = tfIDF;
		this.DF = DF;
		this.topKTFIDF = topKTFIDF;
		this.topKDF = topKDF;
	}
	
	public TObjectIntHashMap<String> getWordToIdMap() {
		return wordToIdMap;
	}

	public void setWordToIdMap(TObjectIntHashMap<String> wordToIdMap) {
		this.wordToIdMap = wordToIdMap;
	}

	public double[] getTfIDF() {
		return tfIDF;
	}
	public void setTfIDF(double[] tfIDF) {
		this.tfIDF = tfIDF;
	}
	public double[] getDF() {
		return DF;
	}
	public void setDF(double[] DF) {
		this.DF = DF;
	}
	public TObjectIntHashMap<String> getTopKTFIDF() {
		return topKTFIDF;
	}
	public void setTopKTFIDF(TObjectIntHashMap<String> topKTFIDF) {
		this.topKTFIDF = topKTFIDF;
	}
	public TObjectIntHashMap<String> getTopKDF() {
		return topKDF;
	}
	public void setTopKDF(TObjectIntHashMap<String> topKDF) {
		this.topKDF = topKDF;
	}
}
