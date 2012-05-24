package com.aneedo.training;

public class FeatureBean {
	double[] features;
	double label;
	double prevLabel;
	String interpretation;
//	double score;
//	
//	public double getScore() {
//		return score;
//	}
//	public void setScore(double score) {
//		this.score = score;
//	}
	public String getInterpretation() {
		return interpretation;
	}
	public void setInterpretation(String interpretation) {
		this.interpretation = interpretation;
	}
	public double[] getFeatures() {
		return features;
	}
	public void setFeatures(double[] features) {
		this.features = features;
	}
	public double getLabel() {
		return label;
	}
	public void setLabel(double label) {
		this.label = label;
	}
	public double getPrevLabel() {
		return prevLabel;
	}
	public void setPrevLabel(double prevLabel) {
		this.prevLabel = prevLabel;
	}
	
	
}