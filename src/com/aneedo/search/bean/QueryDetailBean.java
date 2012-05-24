package com.aneedo.search.bean;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class QueryDetailBean {
	Map<String,String> postags;
	Map<String,Set<String>>  synonymMap;
	List<String> stemList;
	String strStem = null;
	String strPhrase = null;
	Set<String> phraseList;
	String rawQuery;
	String queryBuilder = null;
	String entityQuery;
	String fuzzyQuery;
	String semClassQuery;
	Integer numOfEntityRecords;
	Integer numOfFuzzyRecords;
	Integer numOfSemRecords;
	String[] querySplits = null;
	String[] halfQuerySplits = null;
	double queryIDCG;
	String nounWordStemStr = null;
	int NNSize;
	
	
	public int getNNSize() {
		return NNSize;
	}

	public void setNNSize(int nNSize) {
		NNSize = nNSize;
	}

	public String getNounWordStemStr() {
		return nounWordStemStr;
	}

	public void setNounWordStemStr(String nounWordStemStr) {
		this.nounWordStemStr = nounWordStemStr;
	}

	public double getQueryIDCG() {
		return queryIDCG;
	}

	public void setQueryIDCG(double queryIDCG) {
		this.queryIDCG = queryIDCG;
	}

	public String[] getQuerySplits() {
		return querySplits;
	}

	public void setQuerySplits(String[] querySplits) {
		this.querySplits = querySplits;
	}

	public String[] getHalfQuerySplits() {
		return halfQuerySplits;
	}

	public void setHalfQuerySplits(String[] halfQuerySplits) {
		this.halfQuerySplits = halfQuerySplits;
	}

	public Integer getNumOfEntityRecords() {
		return numOfEntityRecords;
	}

	public void setNumOfEntityRecords(Integer numOfEntityRecords) {
		this.numOfEntityRecords = numOfEntityRecords;
	}

	public Integer getNumOfFuzzyRecords() {
		return numOfFuzzyRecords;
	}

	public void setNumOfFuzzyRecords(Integer numOfFuzzyRecords) {
		this.numOfFuzzyRecords = numOfFuzzyRecords;
	}

	public Integer getNumOfSemRecords() {
		return numOfSemRecords;
	}

	public void setNumOfSemRecords(Integer numOfSemRecords) {
		this.numOfSemRecords = numOfSemRecords;
	}

	public String getEntityQuery() {
		return entityQuery;
	}

	public void setEntityQuery(String entityQuery) {
		this.entityQuery = entityQuery;
	}

	public String getFuzzyQuery() {
		return fuzzyQuery;
	}

	public void setFuzzyQuery(String fuzzyQuery) {
		this.fuzzyQuery = fuzzyQuery;
	}

	public String getSemClassQuery() {
		return semClassQuery;
	}

	public void setSemClassQuery(String semClassQuery) {
		this.semClassQuery = semClassQuery;
	}

	
	public String getStrStem() {
		return strStem;
	}
	public void setStrStem(String strStem) {
		this.strStem = strStem;
	}
	public String getStrPhrase() {
		return strPhrase;
	}
	public void setStrPhrase(String strPhrase) {
		this.strPhrase = strPhrase;
	}
	public Set<String> getPhraseList() {
		return phraseList;
	}
	public void setPhraseList(Set<String> phraseList) {
		this.phraseList = phraseList;
	}
	public Map<String, String> getPostags() {
		return postags;
	}
	public void setPostags(Map<String, String> postags) {
		this.postags = postags;
	}

	public Map<String, Set<String>> getSynonymMap() {
		return synonymMap;
	}
	public void setSynonymMap(Map<String, Set<String>> synonymMap) {
		this.synonymMap = synonymMap;
	}
	
	
	public List<String> getStemList() {
		return stemList;
	}

	public void setStemList(List<String> stemList) {
		this.stemList = stemList;
	}

	public String getRawQuery() {
		return rawQuery;
	}
	public void setRawQuery(String rawQuery) {
		this.rawQuery = rawQuery;
	}
	public String getQueryBuilder() {
		return queryBuilder;
	}
	public void setQueryBuilder(String queryBuilder) {
		this.queryBuilder = queryBuilder;
	}


}
