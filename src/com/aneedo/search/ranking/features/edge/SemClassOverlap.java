package com.aneedo.search.ranking.features.edge;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.aneedo.indexing.IndexingConstants;
import com.aneedo.search.bean.SemEntityBean;
import com.aneedo.search.bean.SemInterpretation;
import com.aneedo.search.ranking.features.FeatureUtil;

public class SemClassOverlap extends EdgeFeature
{

	private int semClassId;

	public SemClassOverlap(int semClassId, List<SemInterpretation> interpretations, Set<SemEntityBean> store)
	{
		super(interpretations, store);
		this.semClassId = semClassId;
	}

	@Override
	public void buildEdgeScoreMatrix()
	{
		List<SemInterpretation> interpretations = getAllInterpretations();
		for(int r = 0; r < interpretations.size(); r++)
		{
			SemInterpretation from = interpretations.get(r);
			for(int c = 0; c < interpretations.size(); c++)
			{
				SemInterpretation to = interpretations.get(c);
				double score = calculateEdgeScore(from, to);
				edgeScore.set(r, c, score);
			}
		}
	}

	private double calculateEdgeScore(SemInterpretation from, SemInterpretation to)
	{
		List<SemEntityBean> fromEntities = FeatureUtil.getContributingEntities(from, store);
		List<SemEntityBean> toEntities = FeatureUtil.getContributingEntities(to, store);
		Set<String> fromCategories = new HashSet<String>();
		Set<String> toCategories = new HashSet<String>();
		
		for(SemEntityBean e : fromEntities)
			 fromCategories.addAll(e.getWordSemClassMap().get(semClassId));
				
		for(SemEntityBean e : toEntities)
			 toCategories.addAll(e.getWordSemClassMap().get(semClassId));
				
		return FeatureUtil.jaccard(fromCategories, toCategories);
	}


}
