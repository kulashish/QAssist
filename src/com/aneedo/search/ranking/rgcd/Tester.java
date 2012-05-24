package com.aneedo.search.ranking.rgcd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ejml.simple.SimpleMatrix;

import com.aneedo.search.SemClassStore;
import com.aneedo.search.SemanticIndexSearcher;
import com.aneedo.search.bean.SemEntityBean;
import com.aneedo.search.bean.SemInterpretation;
import com.aneedo.search.ranking.features.FeatureUtil;

public class Tester
{
	public static void main(String[] args) throws Exception
	{
		String query = "Laptop charger";
		SemanticIndexSearcher processor = SemanticIndexSearcher.getInstance();
		SemClassStore results = processor.getResults(query, null);

		List<SemInterpretation> semInterList = results.getSemInterpretationList();

		System.out.println("Interpretations:");
		
		for (int i = 0, size = semInterList.size(); i < size; i++)
		{
			SemInterpretation semInter = semInterList.get(i);
			System.out.println("\"" + semInter.getInterpretation() + "\",");
		}

		Set<SemEntityBean> store = (Set<SemEntityBean>)new HashSet<SemEntityBean>();
		FeatureUtil.ensureInterpretationEntitiesInStore(semInterList, store);
		
		//GcdSolver solver = new GcdSolverLeastSquares();
		GcdSolver solver = new GcdSolverKLDivergnece();
		ArrayList<TrainingInstance> examples = new ArrayList<TrainingInstance>(1);
		SimpleMatrix[] teleportAndRelevance = buildTeleportAndRelevanceMatrices(teleport(semInterList), semInterList);
		examples.add(new QITrainingInstance(1, query, semInterList, teleportAndRelevance[0], teleportAndRelevance[1], store));
		solver.train(examples);
		System.out.println("Lambdas");
		for(int i = 0; i < solver.getLambdas().getNumRows(); i++)
			System.out.println(solver.getLambdas().get(i, 0));
	}

	/**
	 * 
	 * @param teleport
	 * @param interpretations
	 * @return SimpleMatrix[2]; the first element is the teleport vector r* and the second element is the relevance matrix b.
	 */
	public static SimpleMatrix[] buildTeleportAndRelevanceMatrices(List<String> teleport, List<SemInterpretation> interpretations)
	{
		if(teleport.size() != interpretations.size())
			throw new IllegalArgumentException("Size of teleport (r*) and interpretations (b) must be equal");
		
		SimpleMatrix optimumTeleportMatrix = new SimpleMatrix(teleport.size(), 1);
		SimpleMatrix relevanceMatrix = new SimpleMatrix(interpretations.size(), 1);
		
		for(int i = 0; i < teleport.size(); i++)
		{
			double score = 1.0 * i / teleport.size();
			optimumTeleportMatrix.set(i, 0, score);
			
			// Get the index of interpretation
			for(int j = 0; j < interpretations.size(); j++)
			{
				if(interpretations.get(j).getInterpretation().equalsIgnoreCase(teleport.get(i)))
				{
					relevanceMatrix.set(j, 0, score);
				}
			}
		}
		
		return new SimpleMatrix[] {optimumTeleportMatrix, relevanceMatrix};
	}
	private static List<String> teleport(List<SemInterpretation> interpretations)
	{
		String[] teleports = new String[]
		{
			"Apple Battery Charger",
			"Shelby Charger",
			"Dodge Charger L-body",
			"charger",
			"ibm laptops",
			"universal",
			"Battery charger",
			"Dodge Charger",
			"hardware",
			"Treble Charger",
			"battery",
			"Remote Laptop Security",
			"Charger table setting",
			"Convair Model 48 Charger",
			"HMS Charger 1894",
			"compaq laptops",
			"OLPC XO-1",
			"chrysler viper gts-r",
			"chrysler 300m",
			"Laptop cooler",
			"Dodge Charger B-body",
			"hp laptops",
			"Cash For Laptops",
			"Dodge Charger R/T 1999 concept",
			"Deuce and Charger",
			"lenovo laptops",
			"one laptop per child",
		};
		
		//return Arrays.asList(teleports);
		
		ArrayList<String> tp = new ArrayList<String>();
		for (int i = 0, size = interpretations.size(); i < size; i++)
		{
			SemInterpretation semInter = interpretations.get(i);
			tp.add(semInter.getInterpretation());
		}
		Collections.shuffle(tp);
		return tp;
	}
}
