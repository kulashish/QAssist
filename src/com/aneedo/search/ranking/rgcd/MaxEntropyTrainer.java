package com.aneedo.search.ranking.rgcd;

import org.ejml.alg.dense.linsol.LinearSolver;
import org.ejml.alg.dense.linsol.LinearSolverFactory;
import org.ejml.data.DenseMatrix64F;
import org.ejml.simple.SimpleMatrix;

public class MaxEntropyTrainer
{
	private DenseMatrix64F lambdas = null;
	
	public DenseMatrix64F getLambdas()
	{
		return lambdas;
	}
	
	/**
	 * 
	 * @param featureMatrix Mf Matrices
	 * @param optimumTeleport r*
	 */
	public void train(SimpleMatrix[] featureMatrix, SimpleMatrix optimumTeleport)
	{
		int numOfFeatures = featureMatrix.length;
		DenseMatrix64F coeffMatrix = new DenseMatrix64F(featureMatrix[0].numRows(), numOfFeatures + 1);
		for(int i = 0; i < numOfFeatures; i++)
		{
			SimpleMatrix mf = featureMatrix[i].mult(optimumTeleport);
			
			// Fill the coefficient matrix
			for(int r = 0; r < mf.numRows(); r++)
			{
				coeffMatrix.set(r, i, mf.get(r, 0));
			}
		}
		
		// Fill the last column as -1
		for(int r = 0; r < coeffMatrix.getNumRows(); r++)
		{
			coeffMatrix.set(r, coeffMatrix.getNumCols() - 1, -1);
		}
		
		lambdas = solve(coeffMatrix);
		
		// Normalize the lambdas
		double sum = 0;
		for(int i = 0; i < lambdas.numRows; i++)
		{
			sum += lambdas.get(i, 0);
		}
		
		for(int i = 0; i < lambdas.numRows; i++)
		{
			lambdas.set(i, 0, lambdas.get(i, 0) / sum);
		}
	}

	private DenseMatrix64F solve(DenseMatrix64F coeffMatrix) 
	{
		LinearSolver<DenseMatrix64F> solver = LinearSolverFactory.leastSquares(coeffMatrix.getNumRows(), coeffMatrix.getNumCols());
		solver.setA(coeffMatrix);
		DenseMatrix64F B = new DenseMatrix64F(coeffMatrix.numRows, 1);
		DenseMatrix64F X = new DenseMatrix64F(coeffMatrix.numCols, 1);
		B.zero();
		X.zero();
		solver.solve(B, X);
		return X;
	}
}
