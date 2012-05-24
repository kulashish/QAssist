package com.aneedo.search.ranking.features.edge;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.graph.EdgeSetFactory;

import com.aneedo.search.SemClassStore;
import com.aneedo.search.SemanticIndexSearcher;
import com.aneedo.search.bean.EntityWordBean;
import com.aneedo.search.bean.SemEntityBean;
import com.aneedo.search.bean.SemInterpretation;
import com.aneedo.search.ranking.features.FeatureUtil;

public class InterpretationContentOverlap extends EdgeFeature
{

	public InterpretationContentOverlap(List<SemInterpretation> interpretations, Set<SemEntityBean> store)
	{
		super(interpretations, store);
	}

	private double calculateEdgeScore(SemInterpretation fromInterpretation,
			SemInterpretation toInterpretation)
	{
		// Suffix match -> more similar. Then check the prefix entity contents
		// Prefix match -> less similar
		
		String[] fromParts = fromInterpretation.getInterpretation().split(" ");
		String[] toParts = toInterpretation.getInterpretation().split(" ");
				
		// Jaccard similarity between titles
		double titleScore = FeatureUtil.jaccard(fromParts, toParts);
		
		// Find the maximum length entities that can be compared
		ArrayList<SemEntityBean> maxEntitiesFromIntptn = computeMaxEntitiesForInterpretation(fromInterpretation);
		ArrayList<SemEntityBean> maxEntitiesToIntptn = computeMaxEntitiesForInterpretation(toInterpretation);
		
		// Match the entities with maximum overlap
		int wordCount = 0;
		for(SemEntityBean fromIntrEntity : maxEntitiesFromIntptn)
		{
			for(SemEntityBean toIntrEntity : maxEntitiesToIntptn)
			{
				// Check the amount of overlap in the contents of the entities.
				wordCount = countCommonWordsBetween(fromIntrEntity, toIntrEntity);
			}
		}
			
		return titleScore + wordCount;
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
}
