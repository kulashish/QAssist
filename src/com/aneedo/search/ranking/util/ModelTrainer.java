package com.aneedo.search.ranking.util;

import gnu.trove.TIntObjectHashMap;
import gnu.trove.TObjectDoubleHashMap;
import gnu.trove.TObjectIntHashMap;

import java.util.ArrayList;
import java.util.Set;

import org.ejml.data.DenseMatrix64F;
import org.ejml.simple.SimpleMatrix;

import com.aneedo.search.bean.SemEntityBean;
import com.aneedo.search.bean.SemInterpretation;
import com.aneedo.search.ranking.features.FeatureUtil;
import com.aneedo.search.ranking.rgcd.GcdSolver;
import com.aneedo.search.ranking.rgcd.GcdSolverLeastSquares;
import com.aneedo.search.ranking.rgcd.GreedyTeleportSelector;
import com.aneedo.search.ranking.rgcd.QITrainingInstance;
import com.aneedo.search.ranking.rgcd.TrainingInstance;
import com.aneedo.search.ranking.rgcd.GreedyTeleportSelector.TeleportSelectionResult;
import com.aneedo.training.LogisticOutput;
import com.aneedo.training.RankedInterpretation;

public class ModelTrainer
{
	private static final int TELEPORT_SIZE = 5;
	private static final int MAX_ITERATIONS = 50;
	private static final double EPSILON = 0.001;
	
	private int iterationCount = 0;
	
	public void trainModel() throws Exception
	{
		 LRTrainer lrTrainer = new LRTrainer();
		LogisticOutput result = lrTrainer.trainFromGroundTruth();
		Set<SemEntityBean> store = result.getSemEntBeanSet();
		TIntObjectHashMap<RankedInterpretation> allQryRankedIntrptn = result.getRankedInterQueryMap();
			
		DenseMatrix64F prevLambdas = null;
		while(true)
		{
			System.out.println("Iteration# " + iterationCount++);
			
			ArrayList<TrainingInstance> examples = new ArrayList<TrainingInstance>(allQryRankedIntrptn.size());
			for(int qryId : allQryRankedIntrptn.keys())
			{
				RankedInterpretation qryInterpretation = allQryRankedIntrptn.get(qryId);
				FeatureUtil.ensureInterpretationEntitiesInStore(qryInterpretation.getInterpretations(), store);
				examples.add(new QITrainingInstance(qryId, null, qryInterpretation.getInterpretations(), qryInterpretation.getTeleports(), qryInterpretation.getScores(), store));
			}
			
			// Call GCD component
			GcdSolver solver = new GcdSolverLeastSquares();			
			solver.train(examples);
			
			solver.printLambdas();
			
			if(hasConverged(prevLambdas, solver.getLambdas()))
			{
				printRankedInterpretations(allQryRankedIntrptn, examples, solver);
				FeatureUtil.persist("./lambdas.dat", solver.getLambdas());
				break;
			}
			
			prevLambdas = solver.getLambdas();
			
			TIntObjectHashMap<TObjectDoubleHashMap<String>> queryMap = new TIntObjectHashMap<TObjectDoubleHashMap<String>>();
			for(TrainingInstance example : examples)
			{
				RankedInterpretation qryInterpretation = allQryRankedIntrptn.get(example.getQryId());
				TeleportSelectionResult r = computeR(allQryRankedIntrptn, example, solver);
                r.print();
                
				TObjectDoubleHashMap<String> qryDetails = new TObjectDoubleHashMap<String>();
				for(int i = 0; i < r.teleport.numRows(); i++)
				{
					String interpretation = qryInterpretation.getInterpretations().get(i).getInterpretation();
					qryDetails.put(interpretation, r.teleportDistribution.get(i));
				}				
				queryMap.put(example.getQryId(), qryDetails);
			}
			
			// Call least square
			result = lrTrainer.trainInIteration(queryMap);
		}
	}

	private void printRankedInterpretations(
			TIntObjectHashMap<RankedInterpretation> allQryRankedIntrptn,
			ArrayList<TrainingInstance> examples, GcdSolver solver)
	{
		for(TrainingInstance example : examples)
		{
			System.out.println("------------------------------");
			TeleportSelectionResult r = computeR(allQryRankedIntrptn, example, solver);
			RankedInterpretation qryInterpretation = allQryRankedIntrptn.get(example.getQryId());
			
			for(int i = 0; i < r.teleport.numRows(); i++)
				System.out.println(Math.round(r.teleport.get(i, 0)));
			
			for(int i = 0; i < r.teleport.numRows(); i++)
			{
				if(Math.round(r.teleport.get(i, 0)) == 0)
				    continue;
				
				String interpretation = qryInterpretation.getInterpretations().get(i).getInterpretation();
				System.out.println(interpretation);
			}
			System.out.println("------------------------------");
		}
	}

	private TeleportSelectionResult computeR(TIntObjectHashMap<RankedInterpretation> allQryRankedIntrptn, TrainingInstance example, GcdSolver solver)
	{
		RankedInterpretation qryInterpretation = allQryRankedIntrptn.get(example.getQryId());
		GreedyTeleportSelector teleportSelector = new GreedyTeleportSelector();
		int k = (int) qryInterpretation.getTeleports().elementSum();
		SimpleMatrix teleportProfile = new SimpleMatrix(k, 1);
		teleportProfile.set(1);
		return teleportSelector.selectTeleport(teleportProfile, qryInterpretation.getScores(), solver.buildFinalFeatureMatrix(example));
	}

	private boolean hasConverged(DenseMatrix64F prevLambdas, DenseMatrix64F currLambdas)
	{
		if(prevLambdas == null)
			return false;
		
		if(iterationCount > MAX_ITERATIONS)
			return true;
		
		for(int r = 0; r < currLambdas.getNumRows(); r++)
			if(Math.abs(currLambdas.get(r, 0) - prevLambdas.get(r, 0)) > EPSILON)
				return false;
		
		return true;
	}
	
	public static void main(String[] args) throws Exception 
	{
		ModelTrainer trainer = new ModelTrainer();
		trainer.trainModel();
	}
}
