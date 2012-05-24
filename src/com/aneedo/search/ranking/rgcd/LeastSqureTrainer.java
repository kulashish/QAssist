package com.aneedo.search.ranking.rgcd;

import org.ejml.alg.dense.linsol.LinearSolver;
import org.ejml.alg.dense.linsol.LinearSolverFactory;
import org.ejml.data.DenseMatrix64F;
import org.ejml.simple.SimpleMatrix;

public class LeastSqureTrainer
{
	private DenseMatrix64F lambdas = null;
	
	public DenseMatrix64F getLambdas()
	{
		return lambdas;
	}
	
	/**
	 * 
	 * @param featureMatrix Mf Matrices for all queries
	 * @param optimumTeleport r* Matrices for all queries
	 * @param relevance b Matrices for all queries
	 * 
	 */
	public void train(SimpleMatrix[][] featureMatrix, SimpleMatrix[] optimumTeleport, SimpleMatrix[] relevance)
	{
		int numOfFeatures = featureMatrix[0].length;
		int numQueries = featureMatrix.length;
		int numInterpretations = featureMatrix[0][0].numRows();
		DenseMatrix64F b = new DenseMatrix64F(numQueries * numInterpretations, 1);
		DenseMatrix64F coeffMatrix = new DenseMatrix64F(numQueries * numInterpretations, numOfFeatures);
		for(int q = 0; q < numQueries; q++)
		{
			for(int i = 0; i < numOfFeatures; i++)
			{
				SimpleMatrix mf = featureMatrix[q][i].mult(optimumTeleport[q]);
				
				// Fill the coefficient matrix
				for(int r = 0; r < mf.numRows(); r++)
				{
					coeffMatrix.set(q * numInterpretations + r, i, mf.get(r, 0));
				}
			}
			
			for(int r = 0; r < relevance[q].numRows(); r++)
			{
				b.set(q * numInterpretations + r, 0, relevance[q].get(r, 0));
			}
		}
		
		lambdas = solve(coeffMatrix, b);
		
		normaiizeLambdas();
	}

	private void normaiizeLambdas()
	{
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
	
	private DenseMatrix64F solve(DenseMatrix64F coeffMatrix, DenseMatrix64F b) 
	{
		LinearSolver<DenseMatrix64F> solver = LinearSolverFactory.leastSquares(coeffMatrix.getNumRows(), coeffMatrix.getNumCols());
		solver.setA(coeffMatrix);
		DenseMatrix64F x = new DenseMatrix64F(coeffMatrix.numCols, 1);
		x.zero();
		solver.solve(b, x);
		return x;
	}
}
