package com.aneedo.search.bean;

import gnu.trove.TObjectIntHashMap;

import java.util.Set;

public class FeatureData {
	
	Set<String> frequentAsso =null;
	
	Set<String>[] semClassesForCommon = null;
	
	TObjectIntHashMap<String> wordToIdMap = null;
	
	public Set<String> getFrequentAsso() {
		return frequentAsso;
	}
	public void setFrequentAsso(Set<String> frequentAsso) {
		this.frequentAsso = frequentAsso;
	}
	public Set<String>[] getSemClassesForCommon() {
		return semClassesForCommon;
	}
	public void setSemClassesForCommon(Set<String>[] semClassesForCommon) {
		this.semClassesForCommon = semClassesForCommon;
	}
	public TObjectIntHashMap<String> getWordToIdMap() {
		return wordToIdMap;
	}
	public void setWordToIdMap(TObjectIntHashMap<String> wordToIdMap) {
		this.wordToIdMap = wordToIdMap;
	}
}
