package com.aneedo.search.ranking.rgcd;

import java.util.ArrayList;
import java.util.List;

import org.ejml.data.DenseMatrix64F;
import org.ejml.simple.SimpleMatrix;

public class GcdSolverMaxEntropy extends GcdSolver
{
	@Override
	public void train(List<TrainingInstance> examples) throws Exception
	{
		ArrayList<DenseMatrix64F> allQueryLambdas = new ArrayList<DenseMatrix64F>(examples.size());
		
		for(TrainingInstance inst : examples)
		{
			MaxEntropyTrainer maxEnt = new MaxEntropyTrainer();
			SimpleMatrix[] featureMatrix = computeFeatureMatrix(inst);
			maxEnt.train(featureMatrix, inst.getOptimumTeleport());
			allQueryLambdas.add(maxEnt.getLambdas());
		}
		
		consolidateAllQueryLambdas(allQueryLambdas);
	}

	private void consolidateAllQueryLambdas(ArrayList<DenseMatrix64F> allQueryLambdas) 
	{
		lambdas = new DenseMatrix64F(allQueryLambdas.get(0).numRows, 1);
		
		// Add all lambdas
		for(int n = 0; n < allQueryLambdas.size(); n++)
		{
			for(int r = 0; r < lambdas.numRows; r++)
			{
				lambdas.set(r, 0, lambdas.get(r, 0) + allQueryLambdas.get(n).get(r, 0));
			}
		}
		
		// Normalize
		for(int r = 0; r < lambdas.numRows; r++)
		{
			lambdas.set(r, 0, lambdas.get(r, 0) / allQueryLambdas.size());
		}
	}
}
