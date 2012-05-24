package com.aneedo.search.ranking.rgcd;

import java.util.List;

import org.ejml.data.DenseMatrix64F;
import org.ejml.simple.SimpleMatrix;

public abstract class GcdSolver
{
	public double alpha = 0.3;
	
	protected DenseMatrix64F lambdas = null;
	
	public DenseMatrix64F getLambdas()
	{
		return lambdas;
	}
	
	public DenseMatrix64F setLambdas(DenseMatrix64F lambdas)
	{
		return this.lambdas = lambdas;
	}
	
	protected SimpleMatrix[] computeFeatureMatrix(TrainingInstance inst)
	{
		SimpleMatrix[] featureMatrix = new SimpleMatrix[inst.getConductanceMatrixForAllFeatures().length];
		int i = 0;
		for(SimpleMatrix c : inst.getConductanceMatrixForAllFeatures())
		{
			// Mf = (1 - alpha) * inv(I - alpha * Cf)
			featureMatrix[i] = c.negative().scale(alpha); 
			featureMatrix[i] = featureMatrix[i].plus(SimpleMatrix.identity(c.numCols()));
			featureMatrix[i] = featureMatrix[i].invert();
			featureMatrix[i] = featureMatrix[i].scale(1 - alpha);
			i++;
		}
		return featureMatrix;
	}

	/**
	 * Builds an array of M matrices of the all the queries for all the features
	 * @return 
	 */
	protected SimpleMatrix[][] combineAllTrainingInstanceFeatureMatrices(List<TrainingInstance> examples)
	{
		SimpleMatrix[][] featureMatrices = new SimpleMatrix[examples.size()][];
		for(int i = 0; i < examples.size(); i++)
		{
			featureMatrices[i] = computeFeatureMatrix(examples.get(i));
		}
		return featureMatrices;
	}
	
	/**
	 * Builds an array of r* matrices of the all the queries
	 * @return 
	 */
	protected SimpleMatrix[] combineAllTrainingInstanceOptimumTeleports(List<TrainingInstance> examples)
	{
		SimpleMatrix[] rMatrices = new SimpleMatrix[examples.size()];
		for(int i = 0; i < examples.size(); i++)
		{
			rMatrices[i] = examples.get(i).getOptimumTeleport();
		}
		return rMatrices;
	}
	
	/**
	 * Builds an array of b matrices of the all the queries
	 * @return 
	 */
	protected SimpleMatrix[] combineAllTrainingInstanceRelevances(List<TrainingInstance> examples)
	{
		SimpleMatrix[] bMatrices = new SimpleMatrix[examples.size()];
		for(int i = 0; i < examples.size(); i++)
		{
			bMatrices[i] = examples.get(i).getRelevence();
		}
		return bMatrices;
	}
	
	public SimpleMatrix buildFinalFeatureMatrix(TrainingInstance example)
	{
		SimpleMatrix[] featureMatrices = computeFeatureMatrix(example);
		SimpleMatrix m = new SimpleMatrix(featureMatrices[0].numRows(), featureMatrices[0].numCols());;
		m.zero();
		for(int f = 0; f < featureMatrices.length; f++)
		{
			m = m.plus(lambdas.get(f, 0), featureMatrices[f]);
		}
		return m;
	}
	
	public abstract void train(List<TrainingInstance> examples) throws Exception;

	public void printLambdas()
	{
		if(lambdas.getNumRows() <= 0)
			System.out.println("lambdas = ()");
		
		String s = "" + lambdas.get(0, 0);
		for(int r = 1; r < lambdas.getNumRows(); r++)
			s += ", " + lambdas.get(r, 0);
		
		System.out.println("lambdas = (" + s + ")");
	}
	
	public void logQueryMatrices(SimpleMatrix[][] m, SimpleMatrix[] rStar,
			SimpleMatrix[] b, List<TrainingInstance> examples)
	{
		for(int q = 0; q < examples.size(); q++)
		{
			System.out.println("Qry ID: " + examples.get(q).getQryId());
			System.out.println("----------------");
			System.out.println("r*");
			System.out.println("----");
			System.out.println(rStar[q].toString());
			System.out.println("b");
			System.out.println("----");
			System.out.println(b[q].toString());
			System.out.println("Features");
			System.out.println("----------");
			for(int f = 0; f < m[q].length; f++)
			{
				System.out.println("Feature-" + (f+1));
				System.out.println("-------------");
			    System.out.println(m[q][f].toString());
			}
		}
	}
}
