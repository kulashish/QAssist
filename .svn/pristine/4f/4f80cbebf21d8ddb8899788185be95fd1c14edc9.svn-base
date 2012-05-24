package com.aneedo.search.ranking.rgcd;

import gnu.trove.list.array.TIntArrayList;

import org.ejml.simple.SimpleMatrix;

public class GreedyTeleportSelector 
{
	/**
	 * 
	 * @param teleportProfile 
	 * 		a matrix
	 * @param relevence 
	 * 		b matrix
	 * @param ppv 
	 * 		M matrix
	 * @return
	 */
	public TeleportSelectionResult selectTeleport(
			SimpleMatrix teleportProfile,
			SimpleMatrix relevence,
			SimpleMatrix ppv)
	{
		int K = teleportProfile.numRows();
		TeleportSelectionResult res = new TeleportSelectionResult();
		TIntArrayList teleportColumns = new TIntArrayList(K);
		
		for(int k = 0; k < K; k++)
		{
			// Check which is the best column to add
			int bestCol = -1;
			double leastDivergence = Double.POSITIVE_INFINITY;
			for(int i = 0; i < ppv.numCols(); i++)
			{
				if(teleportColumns.contains(i))
					continue;
				
				teleportColumns.add(i);
				
				SimpleMatrix phi = computeSumAMRstart(teleportProfile, relevence, ppv, teleportColumns);
				res.teleportDistribution = phi;
				
				// KL Divergence
				double div = computeKLDivergence(relevence, phi);
				
				if(div < leastDivergence)
				{
					leastDivergence = div;
					bestCol = i;
				}
				
				teleportColumns.remove(i);
			}
			
			teleportColumns.add(bestCol);
		}
		
		res.teleport = new SimpleMatrix(relevence.numRows(), 1);
		for(int r = 0; r < res.teleport.numRows(); r++)
			if(teleportColumns.contains(r))
				res.teleport.set(r, 0, 1);
			else
				res.teleport.set(r, 0, 0);
		
		res.normalizeTeleportDistribution();
		
		return res;
	}

	/**
	 * Computes SUM (aMr*)
	 */
	private SimpleMatrix computeSumAMRstart(SimpleMatrix teleportProfile,
			SimpleMatrix relevence, SimpleMatrix ppv,
			TIntArrayList teleportColumns) 
	{
		SimpleMatrix phi = new SimpleMatrix(ppv.numRows(), 1);
		double normalizer = 1;
		for(int i = 0; i < teleportColumns.size(); i++)
		{
			double a = teleportProfile.get(i, 0);
			int colPos = teleportColumns.get(i);
			SimpleMatrix column = ppv.extractMatrix(0, ppv.numRows(), colPos, colPos+1);
			column = column.scale(a);
			phi = phi.plus(column);
			normalizer += a;
		}
		phi = phi.scale(normalizer);
		
		return phi;
	}

	private double computeKLDivergence(SimpleMatrix a, SimpleMatrix b) 
	{
		double divergence = 0;
		int rows = Math.min(a.numRows(), b.numRows());
		for(int i = 0; i < rows; i++)
		{
			if(b.get(i, 0) > 0 && a.get(i, 0) / b.get(i, 0) > 0)
			divergence += a.get(i, 0) * Math.log(a.get(i, 0) / b.get(i, 0));
		}
		return divergence;
	}
	
	public class TeleportSelectionResult
	{
		/**
		 * The teleport vector. It contains 1's and 0's only
		 */
		public SimpleMatrix teleport;
		
		/**
		 * The teleport vector's distribution. It contains the probability distribution observed after selecting K columns from M matrix.
		 */
		public SimpleMatrix teleportDistribution;
		
		private void normalizeTeleportDistribution()
		{
			double sum = 0;
			for(int r = 0; r < teleportDistribution.numRows(); r++)
				sum += round8(teleportDistribution.get(r, 0));
			
			for(int r = 0; r < teleportDistribution.numRows(); r++)
				teleportDistribution.set(r, 0, round8(teleportDistribution.get(r, 0)) / sum);
		}

		private double round8(double val)
		{
			double d = Math.pow(10, 8);
			return Math.round(val * d) / d;
		}
		
		public void print()
		{
			System.out.println("Teleport:");
			for(int r = 0; r < teleport.numRows(); r++)
			    System.out.println(teleport.get(r, 0));
			
			System.out.println("Teleport Distribution:");
			for(int r = 0; r < teleportDistribution.numRows(); r++)
			    System.out.println(teleportDistribution.get(r, 0));
		}
	}
}
