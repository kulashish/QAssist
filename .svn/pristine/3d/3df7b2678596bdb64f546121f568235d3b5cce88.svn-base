package com.aneedo.search.ranking.rgcd;

import java.util.Arrays;

import org.ejml.data.DenseMatrix64F;
import org.ejml.simple.SimpleMatrix;

import riso.numerical.LBFGS;
import riso.numerical.LBFGS.ExceptionWithIflag;

public class KLDivergenceTrainer
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
	 * @throws ExceptionWithIflag 
	 * 
	 */
	public void train(SimpleMatrix[][] featureMatrix, SimpleMatrix[] optimumTeleport, SimpleMatrix[] relevance) throws ExceptionWithIflag
	{
		int numOfFeatures = featureMatrix[0].length;
				
		double[] lambdaVector = new double[numOfFeatures];
		Arrays.fill(lambdaVector, 1.0/numOfFeatures);
		
		int[] iprint = new int[2];
		iprint[0] = 1;
        iprint[1] = 3;
		
        int[] iflag = new int[1];
        iflag[0] = 0;  
        
        // Caclculate Mr* for every query
        SimpleMatrix[][] p = computeMr(featureMatrix, optimumTeleport);
        
        double[] diag = new double[numOfFeatures];
        
		int itrCount = 0;
		while(itrCount++ <= 30)
		{
			LBFGS.lbfgs(
					numOfFeatures, 
					7, 
					lambdaVector, 
					evalulateObjective(lambdaVector, p, relevance), 
					evaluateGradient(lambdaVector, p, relevance), 
					false, 
					diag, 
					iprint, 
					1, 
					0, 
					iflag);
		}
		
		lambdas = new DenseMatrix64F(lambdaVector.length, 1);
		for(int r = 0; r < lambdaVector.length; r++)
			lambdas.set(r, 0, lambdaVector[r]);
		
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
	
	private SimpleMatrix[][] computeMr(SimpleMatrix[][] mq,
			SimpleMatrix[] rq)
	{
		int numQueries = mq.length;
		int numOfFeatures = mq[0].length;
		SimpleMatrix[][] p = new SimpleMatrix[numQueries][numOfFeatures];
		
		for(int q = 0; q < numQueries; q++)
		{
			for(int f = 0; f < numOfFeatures; f++)
			{
				p[q][f] = mq[q][f].mult(rq[q]);
			}			
		}
		return p;
	}

	/**
	 * 
	 * @param lambdaVector 
	 * @param p  The Mr* matrix of all queries
	 * @param b  The b matrix of all queries
	 * @return
	 */
	private double evalulateObjective(double[] lambdaVector, SimpleMatrix[][] p, SimpleMatrix[] b)
	{
		int numQueries = p.length;
		int numOfFeatures = lambdaVector.length;
		int numOfInterpretations = p[0][0].numRows(); // also equals to b[0].numRows()
		
		double value = 0;
		
		for(int q = 0; q < numQueries; q++)
		{
			for(int i = 0; i < numOfInterpretations; i++)
			{
				double bqi = b[q].get(i, 0);
				double sumLambdaPqi = 0;
				for(int j = 0; j < numOfFeatures; j++)
					sumLambdaPqi += p[q][j].get(i, 0) * lambdaVector[j];
				 
				if(sumLambdaPqi != 0 && bqi / sumLambdaPqi > 0) // to ensure +ve number for log. This seems to be incorrect to me!!
					value += bqi * Math.log(bqi / sumLambdaPqi);
			}
		}
		
		return value;
	}

	private double[] evaluateGradient(double[] lambdaVector, SimpleMatrix[][] p, SimpleMatrix[] b)
	{
		int numOfFeatures = lambdaVector.length;
		
		double[] gradient = new double[numOfFeatures];
		
		for(int j = 0; j < numOfFeatures; j++)
		{
			gradient[j] = evaluatePartialDerivative(j, lambdaVector, p, b);
		}
		
		return gradient;
	}

	private double evaluatePartialDerivative(int lambdaIndex, double[] lambdaVector,
			SimpleMatrix[][] p, SimpleMatrix[] b)
	{
		int numQueries = p.length;
		int numOfFeatures = lambdaVector.length;
		int numOfInterpretations = p[0][0].numRows(); // also equals to b[0].numRows()
		
		double value = 0;
		
		for(int q = 0; q < numQueries; q++)
		{
			for(int i = 0; i < numOfInterpretations; i++)
			{
				double bqi = b[q].get(i, 0);
				double pqi = p[q][lambdaIndex].get(i, 0);
				double sumLambdaPqi = 0;
				for(int j = 0; j < numOfFeatures; j++)
					sumLambdaPqi += p[q][j].get(i, 0) * lambdaVector[j];
				 
				value += bqi * pqi / sumLambdaPqi;
			}
		}
		
		return -1.0 * value;
	}
}
