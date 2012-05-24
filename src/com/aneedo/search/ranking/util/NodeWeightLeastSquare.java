package com.aneedo.search.ranking.util;

import gnu.trove.TIntObjectHashMap;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import org.ejml.alg.dense.linsol.LinearSolver;
import org.ejml.alg.dense.linsol.LinearSolverFactory;
import org.ejml.data.DenseMatrix64F;
import org.ejml.simple.SimpleMatrix;

import com.aneedo.training.FeatureBean;

public class NodeWeightLeastSquare {
	

	public double[] train(SimpleMatrix[][] featureMatrix, SimpleMatrix[] currentTeleport)
	{
		int n = featureMatrix[0][0].numCols();
		SimpleMatrix qm = new SimpleMatrix(n, n);
		qm.zero();
		
		int[] queryInterSize = new int[featureMatrix.length];
		System.out.println("query size : " + queryInterSize.length);
		
		int totalIntrptns = 0;
		for(int q = 0; q < featureMatrix.length; q++) {
			queryInterSize[q] = featureMatrix[q][0].numRows();
			System.out.println("Inter size" + queryInterSize[q]);
			totalIntrptns  += queryInterSize[q];
		}
		
		DenseMatrix64F b = new DenseMatrix64F(featureMatrix.length * totalIntrptns, 1);
		DenseMatrix64F coeffMatrix = new DenseMatrix64F(featureMatrix.length * totalIntrptns, n);
		
		for (int q = 0; q < featureMatrix.length; q++)
		{

			for(int qi=0;qi<queryInterSize[q];qi++) {
			for (int i = 0; i < n; i++)
			{
				
					coeffMatrix.set(q * queryInterSize[q] +qi, i, featureMatrix[q][0].get(qi, i));
					System.out.println("row : " + q * queryInterSize[q] +qi+ " col : "+i+" val: "+ featureMatrix[q][0].get(qi, i));
				}
			}

			for (int r = 0; r < currentTeleport[q].numRows(); r++)
			{
				System.out.print(currentTeleport[q].get(r, 0)+", ");
				b.set(q * queryInterSize[q] + r, 0, currentTeleport[q].get(r, 0));
			}
			System.out.println();
		}
		
		DenseMatrix64F weights = solve(coeffMatrix, b);
		
		normalize(weights);
		
		double[] featureWeights = new double[weights.getNumRows()];
		
		for(int r = 0; r < weights.getNumRows(); r++) {
			featureWeights[r] = weights.get(r,0);
		}
		
		return featureWeights;
	}
	
	public double[] findLeastSquare(int[] queryIds, TIntObjectHashMap<Map<String, FeatureBean>> queryFeatureBeanMap) {
		String interpretation = null;
		FeatureBean featureBean = null;
		double[] feaValues = null;

		// prepare for least square
		SimpleMatrix[][] featureMatrix = new SimpleMatrix[queryIds.length][1];
		SimpleMatrix[] currentTeleports = new SimpleMatrix[queryIds.length];
		for (int i = 0; i < queryIds.length; i++) {
			final Map<String, FeatureBean> featureLabelMap = queryFeatureBeanMap
					.get(queryIds[i]);
			Iterator<String> interItr = featureLabelMap.keySet().iterator();
			final int interSize = featureLabelMap.size();
			double[][] features = new double[interSize][LRConstants.NO_OF_LR_FEATURE];
			double[][] teleport = new double[interSize][1];
			int id = 0;
			while (interItr.hasNext()) {
				interpretation = interItr.next();
				featureBean = featureLabelMap.get(interpretation);
				feaValues = featureBean.getFeatures();
				features[id] = Arrays.copyOf(feaValues,feaValues.length);
				System.out.println("teleport :" + id +" : "+featureBean.getLabel()+" : "+ Math.log(((double)(1/featureBean.getLabel()))-1));
				teleport[id][0] = -Math.log(((double)(1/featureBean.getLabel()))-1);
				id++;
			}
			featureMatrix[i][0] = new SimpleMatrix(features);
			currentTeleports[i] = new SimpleMatrix(teleport);
		}
		
		// call least square
		return train(featureMatrix, currentTeleports);
	}

	private void normalize(DenseMatrix64F weights)
	{
		// Normalize the lambdas
		double sum = 0;
		for(int i = 0; i < weights.numRows; i++)
		{
			sum += weights.get(i, 0);
		}
		
		for(int i = 0; i < weights.numRows; i++)
		{
			weights.set(i, 0, weights.get(i, 0) / sum);
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

	public static void main(String[] args) {
		SimpleMatrix[][] featureMatrix = new SimpleMatrix[2][1];
		featureMatrix[0][0] = new SimpleMatrix(new double[][] 
		                                                               {
				{ 1, 1 },
				{ 1, 2 },
				{ 1, 3 }
				
			});
		featureMatrix[1][0] = new SimpleMatrix(new double[][] 
                                                            {
				
				{ 1, 4 }
	});
		
		SimpleMatrix[] currentTeleport = new SimpleMatrix[2];
		currentTeleport[0] = new SimpleMatrix(new double[][]
			{
				{ 6 },
				{ 5 },
				{ 7 }
				
			});
		currentTeleport[1] = new SimpleMatrix(new double[][]
		                                       			{
		                                       				
		                                       				{ 10 }
		                                       			});
		
		NodeWeightLeastSquare nwLeastSquare = new NodeWeightLeastSquare();
		double[] weights = nwLeastSquare.train(featureMatrix, currentTeleport);
		for(int r = 0; r < weights.length; r++) {
			//for(int c = 0; c < weights.getNumCols(); c++) {
			System.out.print(weights[r] +", ");
			//}
	}
	}

}
