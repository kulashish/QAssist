package com.aneedo.search.bean;

import java.util.Arrays;

public class EntityPair {
	SemEntityBean semEntityL;
	SemEntityBean semEntityR;
	double titlePairwise;
	double intraPairScore;
	double[] overlapCount =new double[3];
	boolean isOverlapExist;
	
	
	
	public boolean isOverlapExist() {
		return isOverlapExist;
	}
	public void setOverlapExist(boolean isOverlapExist) {
		this.isOverlapExist = isOverlapExist;
	}
	public double getTitlePairwise() {
		return titlePairwise;
	}
	public void setTitlePairwise(double titlePairwise) {
		this.titlePairwise = titlePairwise;
	}
	
	public double[] getOverlapCount() {
		return overlapCount;
	}
	public void setOverlapCount(double[] overlapCount) {
		this.overlapCount = overlapCount;
	}
	public double getIntraPairScore() {
		return intraPairScore;
	}
	public void setIntraPairScore(double intraPairScore) {
		this.intraPairScore = intraPairScore;
	}
	
	public SemEntityBean getSemEntityL() {
		return semEntityL;
	}
	public void setSemEntityL(SemEntityBean semEntityL) {
		this.semEntityL = semEntityL;
	}
	public SemEntityBean getSemEntityR() {
		return semEntityR;
	}
	public void setSemEntityR(SemEntityBean semEntityR) {
		this.semEntityR = semEntityR;
	}
	public void clear() {
		this.titlePairwise = 0.0;
		this.intraPairScore = 0.0;
		Arrays.fill(this.overlapCount, 0);
	}
}
