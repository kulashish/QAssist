package com.aneedo.search.ranking.rgcd;

import java.util.List;

import org.ejml.simple.SimpleMatrix;

public class GcdSolverLeastSquares extends GcdSolver
{

	@Override
	public void train(List<TrainingInstance> examples) throws Exception
	{
		SimpleMatrix[][] M = combineAllTrainingInstanceFeatureMatrices(examples);
		SimpleMatrix[] rStar = combineAllTrainingInstanceOptimumTeleports(examples);
		SimpleMatrix[] b = combineAllTrainingInstanceRelevances(examples);
		
		logQueryMatrices(M, rStar, b, examples);
		
		//LeastSqureTrainer lsTrainer = new LeastSqureTrainer();
		LeastSquareTrainerQP lsTrainer = new LeastSquareTrainerQP();
		lsTrainer.train(M, rStar, b);
		
		lambdas = lsTrainer.getLambdas();
	}
}
