package com.aneedo.search.bean;

import gnu.trove.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.aneedo.indexing.IndexingConstants;


public class SemEntityBean {
	double[] softTitleQueryMatch;
	int[] matchedPositions;
	Integer pageId;
	String title;
	String disamb;
	boolean isDisam;
	double[] allSemClsQueryMatch;
	boolean isAllMatched;
	double queryDotProduct;
	int titleWordId;

	boolean isEntity;
	boolean isSemClass;
	boolean isFuzzy;

	//TODO toRemove
	String[] queryMatchedSemClass;
	String[] unmatchedSemClass;
	String[] titleMatchedSemClass;
	// TODO toRemove end

	TIntObjectHashMap<List<String>> wordSemClassMap = null;
	Map<String,EntityWordBean> entDetailBeanMap = null;
	
	FeatureData featureData;
	
	
	
//	String[] semClassForPartialMatch = new String[2];
//	
//	
	
	
//	public String[] getSemClassForPartialMatch() {
//		return semClassForPartialMatch;
//	}
//	public void setSemClassForPartialMatch(String[] semMemForPartialMatch) {
//		this.semClassForPartialMatch = semMemForPartialMatch;
//	}
	
	public int getTitleWordId() {
		return titleWordId;
	}
	public void setTitleWordId(int titleWordId) {
		this.titleWordId = titleWordId;
	}
	public boolean isAllMatched() {
		return isAllMatched;
	}
	public double getQueryDotProduct() {
		return queryDotProduct;
	}
	public void setQueryDotProduct(double queryDotProduct) {
		this.queryDotProduct = queryDotProduct;
	}
	public void setAllMatched(boolean isAllMatched) {
		this.isAllMatched = isAllMatched;
	}

	// For each semantic class store only for those 
	List<double[]>[] semClassQueryMatch = new ArrayList[IndexingConstants.NUM_OF_SEM_CLASSES];
	// ends remove
	
	public TIntObjectHashMap<List<String>> getWordSemClassMap() {
		return wordSemClassMap;
	}
	
	public void setWordSemClassMap(TIntObjectHashMap<List<String>> wordSemClassMap) {
		this.wordSemClassMap = wordSemClassMap;
	}
	
	public Map<String,EntityWordBean> getEntDetailBeanMap() {
		return entDetailBeanMap;
	}
	
	public void setEntDetailBeanMap(Map<String,EntityWordBean> entDetailBeanList) {
		this.entDetailBeanMap = entDetailBeanList;
	}
	
	public double[] getAllSemClsQueryMatch() {
		return allSemClsQueryMatch;
	}

	public void setAllSemClsQueryMatch(double[] allSemClsQueryMatch) {
		this.allSemClsQueryMatch = allSemClsQueryMatch;
	}
	
	public FeatureData getFeatureData() {
		return featureData;
	}

	public void setFeatureData(FeatureData featureData) {
		this.featureData = featureData;
	}
	
	public String[] getTitleMatchedSemClass() {
		return titleMatchedSemClass;
	}

	public void setTitleMatchedSemClass(String[] titlematchedSemClass) {
		this.titleMatchedSemClass = titlematchedSemClass;
	}

	public SemEntityBean() {
//		for(int i=0;i<IndexingConstants.NUM_OF_SEM_CLASSES;i++) {
//			sem
//		}
	}
	
	public int[] getMatchedPositions() {
		return matchedPositions;
	}

	public void setMatchedPositions(int[] matchedPositions) {
		this.matchedPositions = matchedPositions;
	}

	public String[] getQueryMatchedSemClass() {
		return queryMatchedSemClass;
	}
	public void setQueryMatchedSemClass(String[] matchedSemClass) {
		this.queryMatchedSemClass = matchedSemClass;
	}
	public String[] getUnmatchedSemClass() {
		return unmatchedSemClass;
	}
	public void setUnmatchedSemClass(String[] unmatchedSemClass) {
		this.unmatchedSemClass = unmatchedSemClass;
	}
	
	public List<double[]>[] getSemClassQueryMatch() {
		return semClassQueryMatch;
	}
	public void setSemClassQueryMatch(List<double[]>[] semClassQueryMatch) {
		this.semClassQueryMatch = semClassQueryMatch;
	}
	public boolean isEntity() {
		return isEntity;
	}
	public void setEntity(boolean isEntity) {
		this.isEntity = isEntity;
	}
	public boolean isSemClass() {
		return isSemClass;
	}
	public void setSemClass(boolean isSemClass) {
		this.isSemClass = isSemClass;
	}
	public boolean isFuzzy() {
		return isFuzzy;
	}
	public void setFuzzy(boolean isFuzzy) {
		this.isFuzzy = isFuzzy;
	}
	public double[] getSoftTitleQueryMatch() {
		return softTitleQueryMatch;
	}
	public void setSoftTitleQueryMatch(double[] softTitleQueryMatch) {
		this.softTitleQueryMatch = softTitleQueryMatch;
	}
	public Integer getPageId() {
		return pageId;
	}
	public void setPageId(Integer pageId) {
		this.pageId = pageId;
	}
	public String getTitle() {
//		try {
//		return new Title(title).getPlainTitle();
//		} catch (Exception e) {
//			return "";
//		}
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDisamb() {
		return disamb;
	}
	public void setDisamb(String disamb) {
		this.disamb = disamb;
	}
	public boolean isDisam() {
		return isDisam;
	}
	public void setDisam(boolean isDisam) {
		this.isDisam = isDisam;
	}
	
	public boolean isWordExist(String word) {
		for(int i=0;i<queryMatchedSemClass.length-2;i++) {
			
			if(queryMatchedSemClass[i] != null && queryMatchedSemClass[i].indexOf(word) >= 0) {
			
				return true;
			}
		}
		for(int i=0;i<unmatchedSemClass.length-1;i++) {
			
			if(unmatchedSemClass[i] != null && unmatchedSemClass[i].indexOf(word) >= 0) {
			
				return true;
			}
		}
		return false;
	}
	
	public String[] getAllSemClassMembers() {
		String[] allSemClass = new String[queryMatchedSemClass.length+unmatchedSemClass.length];
		//allSemClass.
		return allSemClass;
	}
//	public List<String> getSections() {
//		return sections;
//	}
//	public void setSections(List<String> sections) {
//		this.sections = sections;
//	}
}
