package com.aneedo.search.ranking.rgcd;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.ejml.simple.SimpleMatrix;

import com.aneedo.search.SemClassStore;
import com.aneedo.search.bean.SemEntityBean;
import com.aneedo.search.bean.SemInterpretation;
import com.aneedo.search.ranking.features.edge.EdgeFeatures;
import com.aneedo.search.ranking.features.edge.SemClassOverlap;

public class QITrainingInstance extends TrainingInstance
{
	private String query;
	private SimpleMatrix[] featureConductanceMatrices;
	private SimpleMatrix optimumTeleportMatrix;
	private SimpleMatrix relevanceMatrix;
	private int qryId;

	

	/**
	 * 
	 * @param query The query used to generate the interpretations
	 * @param List<SemInterpretation> interpretations
	 * @param teleport The r* vector of interpretations. This will be N x 1 matrix (N rows x 1 col)
	 * @param relevence The relevance vector of interpretations. This will be N x 1 matrix (N row x 1 col)
	 * @param store The sematic store
	 */
	public QITrainingInstance(int qryId, String query, List<SemInterpretation> interpretations, SimpleMatrix teleport, SimpleMatrix relevence, Set<SemEntityBean> store)
	{
		this.qryId = qryId;
		this.query = query;
		this.optimumTeleportMatrix = teleport;
		this.relevanceMatrix = relevence;
		EdgeFeatures edgeFeatures = new EdgeFeatures(interpretations, store);
		ArrayList<SimpleMatrix> featureM = new ArrayList<SimpleMatrix>(32);
		featureM.add(edgeFeatures.interpretationContentOverlap().getEdgeScoreMatrix());
		featureM.add(edgeFeatures.proximity().getEdgeScoreMatrix());
		for(SemClassOverlap semClass : edgeFeatures.semClassBased())
			featureM.add(semClass.getEdgeScoreMatrix());
		this.featureConductanceMatrices = featureM.toArray(new SimpleMatrix[0]);
	}

	@Override
	public int getQryId()
	{
		return qryId;
	}
	
	@Override
	public String getQuery()
	{
		return query;
	}

	@Override
	public SimpleMatrix[] getConductanceMatrixForAllFeatures()
	{
		return featureConductanceMatrices;
	}

	@Override
	public SimpleMatrix getOptimumTeleport()
	{
		return optimumTeleportMatrix;
	}

	@Override
	public SimpleMatrix getRelevence()
	{
		return relevanceMatrix;
	}

}
