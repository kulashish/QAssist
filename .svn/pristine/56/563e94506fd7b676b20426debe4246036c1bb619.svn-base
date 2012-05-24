package com.aneedo.search.bean;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CompatibleEntitySet {
	
	Set<Integer> pageIdList = null;
	String strPageIds;
//	int entitySetType;
	double entitySetScore;
	double titleMatchScore;
	double featureScore;
	double[] overlapCountList = new double[3];
	double pairwiseTitleMatch;
	double intraPairScore;
	double queryDotProduct;
	double tfIdfScore;
	double dfScore;
	List<String>[] overlapMembers;
	Map<String,SemInterpretation> overlapSemMembers;
	
	public double getQueryDotProduct() {
		return queryDotProduct;
	}
	
	

	public double getTfIdfScore() {
		return tfIdfScore;
	}



	public void setTfIdfScore(double tfIdfScore) {
		this.tfIdfScore = tfIdfScore;
	}

	public double getDfScore() {
		return dfScore;
	}

	public void setDfScore(double dfScore) {
		this.dfScore = dfScore;
	}

	public void setQueryDotProduct(double queryDotProduct) {
		this.queryDotProduct = queryDotProduct;
	}

	// All sem classes are equally important
	Integer overlapCount;
	
	 public List<String>[] getOverlapMembers() {
		return overlapMembers;
	}

	public void setOverlapMembers(List<String>[] overlapMembers) {
		this.overlapMembers = overlapMembers;
	}
	
	

	public double[] getOverlapCountList() {
		return overlapCountList;
	}

	public void setOverlapCountList(double[] overlapCountList) {
		this.overlapCountList = overlapCountList;
	}

	public double getPairwiseTitleMatch() {
		return pairwiseTitleMatch;
	}

	public void setPairwiseTitleMatch(double pairwiseTitleMatch) {
		this.pairwiseTitleMatch = pairwiseTitleMatch;
	}

	public double getIntraPairScore() {
		return intraPairScore;
	}

	public void setIntraPairScore(double intraPairScore) {
		this.intraPairScore = intraPairScore;
	}

	public Map<String, SemInterpretation> getOverlapSemMembers() {
		return overlapSemMembers;
	}

	public void setOverlapSemMembers(Map<String, SemInterpretation> overlapSemMembers) {
		this.overlapSemMembers = overlapSemMembers;
	}

	public Integer getOverlapCount() {
		return overlapCount;
	}

	public void setOverlapCount(Integer overlapCount) {
		this.overlapCount = overlapCount;
	}

	public double getFeatureScore() {
		return featureScore;
	}

	public void setFeatureScore(double featureScore) {
		this.featureScore = featureScore;
	}

	public double getEntitySetScore() {
		return entitySetScore;
	}

	
	public double getTitleMatchScore() {
		return titleMatchScore;
	}

	public void setTitleMatchScore(double titleMatchScore) {
		this.titleMatchScore = titleMatchScore;
	}

	public void setEntitySetScore(double entityScore) {
		this.entitySetScore = entityScore;
	}

//	public int getEntitySetType() {
//		return entitySetType;
//	}
//
//	public void setEntitySetType(int entitySetType) {
//		this.entitySetType = entitySetType;
//	}

	public Set<Integer> getPageIdList() {
		return pageIdList;
	}

	public void setPageIdList(Set<Integer> pageIdList) {
		this.pageIdList = pageIdList;
		initStrPageIds();
	}

	public String getStrPageIds() {
		return strPageIds;
	}
	
	private void initStrPageIds() {
		StringBuilder builder = new StringBuilder();
		final Iterator<Integer> itr = pageIdList.iterator();
		while(itr.hasNext()) {
			builder.append(itr.next()+",");
		}
		//System.out.println(builder.toString());
		this.strPageIds = builder.toString();
	}

	public void setStrPageIds(String strPageIds) {
		this.strPageIds = strPageIds;
	}

	public boolean contains(CompatibleEntitySet w)
	  {
	      int intersectSize = intersect(w).size();
	      return w.size() == intersectSize;
	  }
	 
	  public int size() {
		  return this.pageIdList.size();
	  }

	 public boolean contains(int vertex)
	  {
	      return pageIdList.contains(vertex);
	  }
	  
	public CompatibleEntitySet intersect(CompatibleEntitySet w)
	  {
	      Set compatSet = new HashSet<Integer>();
	      Set<Integer> toAddSet = w.getPageIdList();
	      //int i = 0;
	      final Iterator<Integer> itr = toAddSet.iterator();
			while(itr.hasNext()) {
	     	         final int i = itr.next();
	          if(contains(i))
	              compatSet.add(Integer.valueOf(i));
	      }

	      return new CompatibleEntitySet(compatSet);
	  }
	
	 public CompatibleEntitySet(Set <Integer> pageList) {
		  this.pageIdList = pageList;
		  //TODO remove this, just for testing
		  initStrPageIds();
	}
	 public CompatibleEntitySet(Integer pageId) {
		  Set<Integer> node = new HashSet<Integer>();
		  node.add(pageId);
		  this.pageIdList = node;
	}
	 
	 public boolean contains(String strPageIds) {
		 if(strPageIds.indexOf(this.strPageIds) >= 0) {
			 return true;
		 }
		 return false;
	 }

}
