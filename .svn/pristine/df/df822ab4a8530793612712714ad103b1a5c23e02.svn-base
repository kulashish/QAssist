package com.aneedo.search.ranking.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ejml.data.DenseMatrix64F;
import org.ejml.simple.SimpleMatrix;
import org.hibernate.impl.SessionImpl;

import com.aneedo.search.SemClassStore;
import com.aneedo.search.SemanticIndexSearcher;
import com.aneedo.search.bean.SemEntityBean;
import com.aneedo.search.bean.SemInterpretation;
import com.aneedo.search.ranking.features.FeatureUtil;
import com.aneedo.search.ranking.rgcd.GcdSolver;
import com.aneedo.search.ranking.rgcd.GcdSolverLeastSquares;
import com.aneedo.search.ranking.rgcd.GreedyTeleportSelector;
import com.aneedo.search.ranking.rgcd.GreedyTeleportSelector.TeleportSelectionResult;
import com.aneedo.search.ranking.rgcd.QITrainingInstance;

public class InterpretationGenerator
{
	private SimpleMatrix wVector;
	private DenseMatrix64F lambdaVector;
	
	public InterpretationGenerator() throws Exception
	{
		loadWs();
		loadLambdas();
	}
	
	private void loadWs()
	{
		// TODO Auto-generated method stub
		
	}

	private void loadLambdas() throws Exception
	{
		lambdaVector = FeatureUtil.load("./lambdas.dat");
	}

	public List<String> generateInterpretations(String query) throws Exception
	{
		SemanticIndexSearcher processor = SemanticIndexSearcher.getInstance();
		SemClassStore results = processor.getResults(query, null);

		List<SemInterpretation> semInterList = results.getSemInterpretationList();
		
		LRTrainer lr = new LRTrainer();
		SimpleMatrix b = null;//lr.predict(semInterList);
		
		GreedyTeleportSelector teleportSelector = new GreedyTeleportSelector();
		SimpleMatrix teleportProfile = new SimpleMatrix(semInterList.size(), 1);
		teleportProfile.set(1);
		GcdSolver solver = new GcdSolverLeastSquares();
		Set<SemEntityBean> store = new HashSet<SemEntityBean>();
		FeatureUtil.ensureInterpretationEntitiesInStore(semInterList, store);
		solver.setLambdas(lambdaVector);
		QITrainingInstance example = new QITrainingInstance(1, null, semInterList, null, b, store);
		TeleportSelectionResult r = teleportSelector.selectTeleport(teleportProfile, b, solver.buildFinalFeatureMatrix(example));
		ArrayList<String> rankedInterpretations = new ArrayList<String>();
		for(int i = 0; i < r.teleport.numRows(); i++)
		{
			String interpretation = semInterList.get(i).getInterpretation();
			System.out.println(interpretation);
			rankedInterpretations.add(interpretation);
		}
		
		return rankedInterpretations;
	}
	
	public static void main(String[] args) throws Exception
	{
		String query = "Natural Gas";
		InterpretationGenerator gen = new InterpretationGenerator();
		for(String intrptn : gen.generateInterpretations(query))
			System.out.println(intrptn);
	}
}
