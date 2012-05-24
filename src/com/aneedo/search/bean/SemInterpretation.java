package com.aneedo.search.bean;

import com.aneedo.indexing.IndexingConstants;
import com.aneedo.search.ranking.features.node.NodeFeatures;


public class SemInterpretation {
	char entityCatOther;
	double aggScore;
	double queryMatch;
	double titleMatch;
//	double tfidf;
//	double df;
//	//int semClass;
//	boolean isSectionFollowTemplate;
	String activationPath="";
	String interpretation;
	int[] semClassMatch = new int[IndexingConstants.NUM_OF_SEM_CLASSES_FOR_COMPARISON];

	double intraSetScore;
	double interSetScore;
	String overlapSemMembersDtls;
	//int frequency;
	NodeFeatures nodeFeatures = null;
	
	
	
	
//	public double getDf() {
//		return df;
//	}
//	public void setDf(double df) {
//		this.df = df;
//	}
	public NodeFeatures getNodeFeatures() {
		return nodeFeatures;
	}
	public void setNodeFeatures(NodeFeatures nodeFeatures) {
		this.nodeFeatures = nodeFeatures;
	}
	public double getQueryMatch() {
		return queryMatch;
	}
	public void setQueryMatch(double queryMatch) {
		this.queryMatch = queryMatch;
	}
	public double getTitleMatch() {
		return titleMatch;
	}
	public void setTitleMatch(double titleMatch) {
		this.titleMatch = titleMatch;
	}
//	public double getTfidf() {
//		return tfidf;
//	}
//	public void setTfidf(double tfidf) {
//		this.tfidf = tfidf;
//	}
//	public boolean isSectionFollowTemplate() {
//		return isSectionFollowTemplate;
//	}
//	public void setSectionFollowTemplate(boolean isSectionFollowTemplate) {
//		this.isSectionFollowTemplate = isSectionFollowTemplate;
//	}
	public int[] getSemClassMatch() {
		return semClassMatch;
	}
	public void setSemClassMatch(int[] semClassMatch) {
		this.semClassMatch = semClassMatch;
	}
	
	public double getIntraSetScore() {
		return intraSetScore;
	}
	public void setIntraSetScore(double intraSetScore) {
		this.intraSetScore = intraSetScore;
	}
	public double getInterSetScore() {
		return interSetScore;
	}
	public void setInterSetScore(double interSetScore) {
		this.interSetScore = interSetScore;
	}
	public String getOverlapSemMembersDtls() {
		return overlapSemMembersDtls;
	}
	public void setOverlapSemMembersDtls(String overlapSemMembersDtls) {
		this.overlapSemMembersDtls = overlapSemMembersDtls;
	}
	public void setAggScore(double aggScore) {
		this.aggScore = aggScore;
	}
	public char getEntityCatOther() {
		return entityCatOther;
	}
	public void setEntityCatOther(char entityCatOther) {
		this.entityCatOther = entityCatOther;
	}
	
//	public int getSemClass() {
//		return semClass;
//	}
//	public void setSemClass(int semClass) {
//		this.semClass = semClass;
//	}
	public String getActivationPath() {
		return activationPath;
	}
	public void setActivationPath(String activationPath) {
		this.activationPath = activationPath;
	}
	public Double getAggScore() {
		return aggScore;
	}
	public void setAggScore(Double aggScore) {
		this.aggScore = aggScore;
	}
	public String getInterpretation() {
		return interpretation;
	}
	public void setInterpretation(String interpretation) {
		this.interpretation = interpretation;
	}
}
