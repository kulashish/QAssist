package com.aneedo.search.ranking.rgcd;

import java.util.List;

import org.ejml.simple.SimpleMatrix;

public class GcdSolverKLDivergnece extends GcdSolver
{

	@Override
	public void train(List<TrainingInstance> examples) throws Exception
	{
		SimpleMatrix[][] M = combineAllTrainingInstanceFeatureMatrices(examples);
		SimpleMatrix[] rStar = combineAllTrainingInstanceOptimumTeleports(examples);
		SimpleMatrix[] b = combineAllTrainingInstanceRelevances(examples);
		
		KLDivergenceTrainer klTrainer = new KLDivergenceTrainer();
		klTrainer.train(M, rStar, b);
		
		lambdas = klTrainer.getLambdas();
	}

}
