package com.aneedo.search.diversity;

import java.util.ArrayList;

import com.aneedo.search.bean.SemInterpretation;




/*
 * Given a list of items(could be documents etc.,), MMR orders them in such a way that diversity is seen in the ordered items.
 * getDiverseSet() is the function which does this for us.
 */
public class MMR {
	
	public static Double alpha=0.3;
	public static Double minMMR=alpha-1;
	
	public Double getJaccard(String s1,String s2)
	{
		if(s1==null || s2==null)return 0.0;
		String s1Tokens[]=s1.split(" ");
		String s2Tokens[]=s2.split(" ");
		if(s1Tokens.length==0 || s2Tokens.length==0)return 0.0;
		int intersection=0;
		for(int i=0;i<s1Tokens.length;++i)
		{
			for(int j=0;j<s2Tokens.length;++j)
			{
				if(s1Tokens[i].toLowerCase().equals(s2Tokens[j].toLowerCase()))intersection++;
			}
		}
		return (1.0*intersection)/(s1Tokens.length+s2Tokens.length-intersection);
	}
	
	private Double getSimilarity(SemInterpretation a,SemInterpretation b)
	{
		return getJaccard(a.getInterpretation(),b.getInterpretation());
		
	}
	
	//This is a helper function called by getDiverseSet(). It returns the item which has maximal marginal relevance.
	public int getMMRItem(ArrayList<SemInterpretation> relevantItems,ArrayList<SemInterpretation> currResult)
	{
		if(relevantItems.size()<=0)return -1;
		int MMRId=-1;
		Double marginalRelevanceScore=Double.MIN_VALUE;
		for(int i=0;i<relevantItems.size();++i)
		{
			Double redundancyScore=0.0;
			if(currResult.size()==0)redundancyScore=0.0;
			else 
			{
				for(int j=0;j<currResult.size();++j)
				{
					Double temp=getSimilarity(relevantItems.get(i),currResult.get(j));
					if(redundancyScore<temp)redundancyScore=temp;
				}
			}
			Double currMRScore=alpha*relevantItems.get(i).getAggScore()-(1-alpha)*redundancyScore;
			if(currMRScore>marginalRelevanceScore)
			{
				marginalRelevanceScore=currMRScore;
				MMRId=i;
			}
		}
		if(marginalRelevanceScore<minMMR)return -1;
		return MMRId;
	}
	
	
	//given a list of relevant items, this function returns a diverse set of items in the given list.
	public ArrayList<SemInterpretation> getDiverseSet(ArrayList<SemInterpretation> relevantItems)
	{
		if(relevantItems==null)return null;
		ArrayList<SemInterpretation> results=new ArrayList<SemInterpretation>();
		Boolean stop=false;
		while(!stop)
		{
			int id=getMMRItem(relevantItems,results);//get the item which has maximal marginal relevance.
			if(id==-1)stop=true;
			else
			{
				SemInterpretation temp=relevantItems.get(id);
				relevantItems.remove(id);
				results.add(temp);
			}
		}
		return results;
	}
	
	public static void main(String[] args)
	{
		
	}
}

