package com.aneedo.training;

import java.util.List;

import org.ejml.simple.SimpleMatrix;

import com.aneedo.search.bean.SemInterpretation;

public class RankedInterpretation {
	List<SemInterpretation> interpretations;
	SimpleMatrix scores;
	SimpleMatrix teleports;
	public List<SemInterpretation> getInterpretations() {
		return interpretations;
	}
	public void setInterpretations(List<SemInterpretation> interpretations) {
		this.interpretations = interpretations;
	}
	public SimpleMatrix getScores() {
		return scores;
	}
	public void setScores(SimpleMatrix scores) {
		this.scores = scores;
	}
	public SimpleMatrix getTeleports() {
		return teleports;
	}
	public void setTeleports(SimpleMatrix teleports) {
		this.teleports = teleports;
	}
	
	
	
}
