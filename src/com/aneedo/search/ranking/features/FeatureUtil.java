package com.aneedo.search.ranking.features;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.ejml.data.DenseMatrix64F;

import com.aneedo.search.SemClassStore;
import com.aneedo.search.SemanticIndexSearcher;
import com.aneedo.search.bean.Interpretation;
import com.aneedo.search.bean.QueryDetailBean;
import com.aneedo.search.bean.SemEntityBean;
import com.aneedo.search.bean.SemInterpretation;

public class FeatureUtil
{
	public static double jaccard(String[] vectorA, String[] vectorB)
	{
		// (A intersection B) / (A Union B)
		int numOfCommonWords = 0;
		
		for(String strA : vectorA)
		{
			for(String strB : vectorB)
			{
				if(strA.equalsIgnoreCase(strB))
					numOfCommonWords++;
			}
		}
		
		return 1.0 * numOfCommonWords / (vectorA.length + vectorB.length  + Double.MIN_VALUE);
	}
	
	public static double jaccard(Set<String> setA, Set<String> setB)
	{
		// (A intersection B) / (A Union B)
		int numOfCommonWords = 0;
		
		for(String strA : setA)
		{
			for(String strB : setB)
			{
				if(strA.equalsIgnoreCase(strB))
					numOfCommonWords++;
			}
		}
		
		return 1.0 * numOfCommonWords / (setA.size() + setB.size() + Double.MIN_VALUE);
	}

	/**
	 * Ensures all possible entities that can lead to the interpretations are in the store.
	 * @param interpretations
	 * @throws Exception 
	 */
	public static void ensureInterpretationEntitiesInStore(List<SemInterpretation> interpretations, Set<SemEntityBean> store) throws Exception
	{
		List<String> entities = new ArrayList<String>(interpretations.size() * 3);
		
		for (int i = 0; i < interpretations.size(); i++)
		{
			for(String entityName : generatePossibleEntityNames(interpretations.get(i).getInterpretation()))
				if(!store.contains(entityName))			
				    entities.add(entityName);
		}
		
		ensureEntitiesInStore(entities, store);
	}
	
	public static void ensureEntitiesInStore(List<String> entityNames, Set<SemEntityBean> store) throws Exception
	{	
		SemClassStore s = new SemClassStore();
		SemanticIndexSearcher.getInstance().storeEntitiesMathchingQuery(entityNames, s);
		for(int k : s.getSemEntityBeanMap().keys())
			store.add(s.getSemEntityBeanMap().get(k));
	}
	
	public static ArrayList<String> generatePossibleEntityNames(String interpretation)
	{
		ArrayList<String> names = new ArrayList<String>();
		// Start trimming words from the ends
		String[] parts = interpretation.split(" ");
		for(int i = 0; i < parts.length; i++)
		{
			String name = "";
			// Join first n parts
			for(int j = 0; j < parts.length - i; j++)
			{
				name += parts[j] + " ";
			}
			name = name.trim();
			names.add(name);
			
			// Join last n parts
			name = "";
			for(int j = i; j < parts.length; j++)
			{
				name += parts[j] + " ";
			}
			name = name.trim();
			names.add(name);
		}
		
		// Remove duplicate names
		for(int i = 0; i < names.size(); i++)
		{
			for(int j = i + 1; j < names.size(); j++)
			{
				if(names.get(j).equalsIgnoreCase(names.get(i)))
					names.remove(j);
			}
		}
		
		return names;
	}
	
	public static SemEntityBean getEntityById(int id, Set<SemEntityBean> store)
	{
		for(SemEntityBean entity : store)
		{
			if(entity.getPageId() == id)
				return entity;
		}
		
		return null;
	}

	public static List<Integer> getContributingEntityIds(SemInterpretation interpretation)
	{
		List<Integer> entityIds = new ArrayList<Integer>();
		String entityIdStr = interpretation.getOverlapSemMembersDtls().split("//")[1];
		for(String id : entityIdStr.split("/"))
		{
			entityIds.add(Integer.parseInt(id));
		}
		return entityIds;
	}
	
	public static List<SemEntityBean> getContributingEntities(SemInterpretation interpretation, Set<SemEntityBean> store)
	{
		List<SemEntityBean> entities = new ArrayList<SemEntityBean>();
		for(int id : getContributingEntityIds(interpretation))
		{
			SemEntityBean entity = getEntityById(id, store);
			if(entity != null)
				entities.add(entity);
		}
		return entities;
	}
	
	public static void persist(String fileName, DenseMatrix64F matrix) throws Exception
	{
		System.out.println("Storing Lambdas in : " + new File(fileName).getAbsolutePath());
		ObjectOutput ObjOut = new ObjectOutputStream(new FileOutputStream(fileName)); 
		ObjOut.writeObject(matrix);
		ObjOut.close();
	}
	
	public static DenseMatrix64F load(String fileName) throws Exception
	{
		System.out.println("Loading Lambdas from : " + new File(fileName).getAbsolutePath());
		ObjectInputStream obj = new ObjectInputStream(new FileInputStream(fileName));
		DenseMatrix64F matrix = (DenseMatrix64F) obj.readObject();
		obj.close();
		return matrix;
	}
}
