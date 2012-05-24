package com.aneedo.search.ranking.rgcd;

import org.ejml.simple.SimpleMatrix;

public abstract class TrainingInstance
{
	public abstract String getQuery();
	public abstract SimpleMatrix[] getConductanceMatrixForAllFeatures(); // C Matrix 
	public abstract SimpleMatrix getOptimumTeleport(); // r* Matrix
	public abstract SimpleMatrix getRelevence(); // b Matrix
	public abstract int getQryId();
}
