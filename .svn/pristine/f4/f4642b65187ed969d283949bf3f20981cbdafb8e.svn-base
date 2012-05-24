package com.aneedo.search.ranking.features.edge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.ejml.simple.SimpleMatrix;

import com.aneedo.search.SemClassStore;
import com.aneedo.search.bean.EntityWordBean;
import com.aneedo.search.bean.SemEntityBean;
import com.aneedo.search.bean.SemInterpretation;
import com.aneedo.search.ranking.features.FeatureUtil;
import com.aneedo.util.StopwordRecognizer;

public abstract class EdgeFeature
{
	protected Set<SemEntityBean> store;

	protected SimpleMatrix edgeScore = null;
	protected List<SemInterpretation> interpreatations;

	public EdgeFeature(List<SemInterpretation> interpretations, Set<SemEntityBean> store)
	{
		this.interpreatations = interpretations;
		this.store = store;
		int n = interpretations.size();
		this.edgeScore = new SimpleMatrix(n, n);
		buildEdgeScoreMatrix();
	}

	public SimpleMatrix getEdgeScoreMatrix()
	{
		return edgeScore;
	}

	protected List<SemInterpretation> getAllInterpretations()
	{
		return interpreatations;
	}

	protected int indexOfInterpretation(String interpretation)
	{
		int index = 0;
		for (SemInterpretation p : getAllInterpretations())
		{
			if (p.getInterpretation().equalsIgnoreCase(interpretation))
				return index;

			index++;
		}

		return -1;
	}

	protected ArrayList<SemEntityBean> computeMaxEntitiesForInterpretation(
			SemInterpretation interpretation)
	{
		ArrayList<SemEntityBean> entities = new ArrayList<SemEntityBean>();
		
		String intrprtn = interpretation.getInterpretation();
		ArrayList<String> possibleEntityNames = FeatureUtil.generatePossibleEntityNames(intrprtn);
		int maxLenEntityAdded = 0;
		for(String name : possibleEntityNames)
		{
			int len = name.split(" ").length;
			if(len < maxLenEntityAdded)
				continue;
			
			SemEntityBean entity = null;
			for(SemEntityBean  e :store)
			{
				if(e.getTitle().equalsIgnoreCase(name))
				{
					entity = e;
					break;
				}
			}
			
			if(entity == null) // No entity by this title
				continue;
			
			entities.add(entity);
			
			maxLenEntityAdded = len;
		}
		
		return entities;
	}

	protected List<SemEntityBean> getContributingEntities(SemInterpretation interpretation)
	{
		return FeatureUtil.getContributingEntities(interpretation, store);
	}
	
	protected int countCommonWordsBetween(SemEntityBean entityOne,
			SemEntityBean entityTwo)
	{
		int count = 0;
		Collection<EntityWordBean> wordsOne = entityOne.getEntDetailBeanMap().values();
		Collection<EntityWordBean> wordsTwo = entityTwo.getEntDetailBeanMap().values();
		for(EntityWordBean wordOne : wordsOne)
		{
			if(StopwordRecognizer.getInstance().isStopWord(wordOne.getWord()))
				continue;
			
			// Check if the wordOne in present in wordsTwo
			for(EntityWordBean wordTwo : wordsTwo)
			{
				if(StopwordRecognizer.getInstance().isStopWord(wordTwo.getWord()))
					continue;
				
				if(wordOne.getWord().equalsIgnoreCase(wordTwo.getWord()))
				{
					count++;
				}
			}
		}
		return count;
	}

	public double getEdgeScore(String fromInterpretation,
			String toInterpretation)
	{
		return edgeScore.get(indexOfInterpretation(fromInterpretation), indexOfInterpretation(toInterpretation));
	}

	public abstract void buildEdgeScoreMatrix();
}
