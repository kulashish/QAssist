package com.aneedo.search.ranking.features.node;

import gnu.trove.TIntDoubleHashMap;

import java.util.Arrays;

public class NodeFeatures {
	
	public static final int TFIDF = 0;
	// Set based on number of entities involved in the query 
	//1/no of entities involved in query *  evidence
	// evidence, if single entity query : title (0.5)
	// else section template, overlapping (high association overlap count), title words in co-occur or reference
	public static final int GRANULARITY_EVIDENCE_SCORE = 1;
	public static final int NO_OF_SEM_CLASS_MATCH = 2;
	public static final int DF = 3;
	public static final int INTER_SET_SCORE = 4;
	public static final int INTRA_SET_SCORE = 5;
	
	
	public static final int NO_OF_NODE_FEATURE = 6;
	
	private boolean[] allFeatures = new boolean[NO_OF_NODE_FEATURE];
	
	TIntDoubleHashMap featureValueMap = null;

	public void setFeatureValueMap(TIntDoubleHashMap featureValueMap) {
		this.featureValueMap = featureValueMap;
	}
	
	public void setFeature(int feature, double value) {
		this.featureValueMap.put(feature, value);
	}
	
	public double getFeature(int feature) {
		return featureValueMap.get(feature);
	}

	public TIntDoubleHashMap getNodeFeatureValueMap() {
		
		TIntDoubleHashMap featureToUseValues = new TIntDoubleHashMap();
		
		for(int i=0;i<allFeatures.length;i++) {
			if(allFeatures[i]) {
				featureToUseValues.put(i, featureValueMap.get(i));
			}
		}
		return featureToUseValues;
	}

	public void setNodeFeaturesToUse(int[] nodeFeaturesToUse) {
		Arrays.fill(allFeatures, false);
		for(int i=0;i<nodeFeaturesToUse.length;i++) {
			this.allFeatures[nodeFeaturesToUse[i]] = true;
		}
	}

	public NodeFeatures() {
		Arrays.fill(allFeatures, true);
	}
}
