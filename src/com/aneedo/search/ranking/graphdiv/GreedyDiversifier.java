package com.aneedo.search.ranking.graphdiv;

import org.ejml.simple.SimpleMatrix;

import gnu.trove.list.array.TIntArrayList;

public class GreedyDiversifier
{
	public static double gamma = 0.5;

	public TIntArrayList selectTeleport(SimpleMatrix nodeScores,
			SimpleMatrix edgeScores, int numTeleport)
	{
		TIntArrayList teleportNodes = new TIntArrayList(numTeleport);

		// Select the node which has maximum score.
		teleportNodes.add(selectMaxScoreNode(nodeScores));

		// Select next node such that objective is maximized
		// Objective:
		for (int k = 0; k < numTeleport; k++)
		{
			teleportNodes.add(selectMaximizingNode(nodeScores, edgeScores,
					teleportNodes));
		}

		return teleportNodes;
	}

	private int selectMaxScoreNode(SimpleMatrix nodeScores)
	{
		double maxScore = Double.NEGATIVE_INFINITY;
		int maxNode = -1;
		for (int i = 0; i < nodeScores.numRows(); i++)
		{
			if(nodeScores.get(i, 0) > maxScore)
			{
				maxNode = i;
				maxScore = nodeScores.get(i, 0);
			}
		}
		return maxNode;
	}

	private int selectMaximizingNode(SimpleMatrix nodeScores,
			SimpleMatrix edgeScores, TIntArrayList selectedNodes)
	{
		int bestNode = -1;
		double bestScore = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < nodeScores.numRows(); i++)
		{
			if (selectedNodes.contains(i))
				continue;

			selectedNodes.add(i);
			double score = evaluateScore(selectedNodes, nodeScores, edgeScores);
			selectedNodes.remove(i);

			if (score > bestScore)
			{
				bestScore = score;
				bestNode = i;
			}
		}

		return bestNode;
	}

	private double evaluateScore(TIntArrayList nodes, SimpleMatrix nodeScores,
			SimpleMatrix edgeScores)
	{
		double score = 0;

		for (int i = 0; i < nodes.size(); i++)
		{
			score += gamma * nodeScores.get(i, 0);

			for (int j = 0; j < nodes.size(); j++)
			{
				score -= (1.0 - gamma) * edgeScores.get(i, j);
			}
		}
		
		return score;
	}

}
