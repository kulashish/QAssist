package com.aneedo.search.diversity;


public class Item {
	private String id;
	private String title;
	private String description;
	private static int numOfFeatures=10;//number of features
	private Double features[];//feature vector
	private static Double featureWeights[]={1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0};//weight for each feature
	private Double aggScore=0.0;
	
	public Item()
	{//default constructor
		features=new Double[numOfFeatures];
	}
	
	public void setId(String id)
	{
		this.id=id;
	}
	
	public void setTitle(String title)
	{
		this.title=title;
	}
	
	public void setFeatures(Double featureValues[])
	{
		if(featureValues==null)return;
		if(featureValues.length<numOfFeatures)
			for(int j=featureValues.length;j<numOfFeatures;++j)features[j]=0.0;
		for(int i=0;i<featureValues.length;++i)
			features[i]=featureValues[i];
	}
	
	private Double getHammingDistance(String s1,String s2)
	{
		Double result=0.0;
		for(int i=0;i<Math.min(s1.length(), s2.length());++i)
		{
			if(s1.charAt(i)==s2.charAt(i))result++;
		}
		return result/(result+Math.max(s1.length(), s2.length()));
	}
	
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
	
	//returns the similarity between item-i and itself.
	public Double getSimilarity(Item i)
	{
		return getJaccard(this.title,i.title);
	}
	
	
	public void setAggScore(Double score)
	{
		aggScore=score;
	}
	
	//returns the aggregated feature value
	public Double getAggScore()
	{
		/*Double result=0.0;
		Double temp=0.0;
		for(int i=0;i<numOfFeatures;++i)
		{
			result+=features[i]*featureWeights[i];
			temp+=featureWeights[i];
		}
		return result/temp;*/
		return aggScore;
	}
	
	public static void main(String args[])
	{
		Item test=new Item();
		System.out.println(test.getJaccard("iphone", "iphone 4G"));
		
	}
}
