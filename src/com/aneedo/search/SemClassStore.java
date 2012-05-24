package com.aneedo.search;

import gnu.trove.TIntObjectHashMap;
import gnu.trove.TIntObjectIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aneedo.search.bean.CompatibleEntitySet;
import com.aneedo.search.bean.QueryDetailBean;
import com.aneedo.search.bean.SemEntityBean;
import com.aneedo.search.bean.SemInterpretation;
import com.aneedo.search.util.SemClassConstants;

public class SemClassStore {
	private TIntObjectHashMap<SemEntityBean> 
		semEntityBeanMap = new TIntObjectHashMap<SemEntityBean>();
	private List<boolean[]> hardQueryMatchVector = null;
	private List<Integer> pageIdList = null;
	
	// Keep tracks whether all query matched at least by one entity
	private boolean[] queryMatch = null;
	List<CompatibleEntitySet> compatibleList = null;
	private List<SemInterpretation> semInterpretationList = null;
	private QueryDetailBean queryDetailBean = null;
	List<Integer> noMatchEntityList = new ArrayList<Integer>();
	int sizeOfLargerSet;
	// TODO semClassResult is null
	
	
	
	public TIntObjectHashMap<SemEntityBean> getSemEntityBeanMap() {
		return semEntityBeanMap;
	}

	public int getSizeOfLargerSet() {
		return sizeOfLargerSet;
	}

	public void setSizeOfLargerSet(int sizeOfLargerSet) {
		this.sizeOfLargerSet = sizeOfLargerSet;
	}

	public List<Integer> getNoMatchEntityList() {
		return noMatchEntityList;
	}

	public void setNoMatchEntityList(List<Integer> noMatchEntityList) {
		this.noMatchEntityList = noMatchEntityList;
	}

	public QueryDetailBean getQueryDetailBean() {
		return queryDetailBean;
	}

	public void setQueryDetailBean(QueryDetailBean queryDetailBean) {
		this.queryDetailBean = queryDetailBean;
	}

	public List<SemInterpretation> getSemInterpretationList() {
		return semInterpretationList;
	}

	public void setSemInterpretationList(
			List<SemInterpretation> semInterpretation) {
		this.semInterpretationList = semInterpretation;
	}

	public SemClassStore() {
		hardQueryMatchVector = new ArrayList<boolean[]>();
		pageIdList = new ArrayList<Integer>();
	}
	
	public List<CompatibleEntitySet> getCompatibleList() {
		return compatibleList;
	}

	public void setCompatibleList(List<CompatibleEntitySet> compatibleList) {
		this.compatibleList = compatibleList;
	}

	public synchronized boolean isAlreadyPresent(Integer pageId, int resultType) {
		//System.out.println("Inside isAlreadyPresent() pageId : " + pageId +"Result Type : "+ resultType);
		if(semEntityBeanMap.containsKey(pageId)) {
			SemEntityBean entityBean = semEntityBeanMap.get(pageId);
			setFetchedFrom(entityBean, resultType);
			return true;
		}
		return false;
	}
	
	public synchronized void add(Integer pageId, SemEntityBean semEntityBean, boolean[] hardMatch, int resultType) {
		//System.out.println("Inside add() pageId : " + pageId +"Result Type : "+ resultType);
		if(!isAlreadyPresent(pageId, resultType)) {
			this.semEntityBeanMap.put(pageId, semEntityBean);
			pageIdList.add(pageId);
			hardQueryMatchVector.add(hardMatch);
		}
	}
	
	public void setFetchedFrom(SemEntityBean entityBean, int resultType){
		switch (resultType) {
		case SemClassConstants.RESULT_ENTITY:
			entityBean.setEntity(true);
			break;
		case SemClassConstants.RESULT_SEM_CLASS:
			entityBean.setSemClass(true);
			break;
		case SemClassConstants.RESULT_FUZZY:
			entityBean.setFuzzy(true);
			break;
		default:
			break;
		}
	}
	
	public SemEntityBean getSemEntity(Integer pageId) {
		return semEntityBeanMap.get(pageId);
	}
	
	
	public List<boolean[]> getHardMatch() {
		return hardQueryMatchVector;
	}
	
	public List<Integer> getPageIds() {
		return pageIdList;
	}
	
	public void clearTempItems() {
		pageIdList = null;
		hardQueryMatchVector = null;
	}

	public boolean[] getQueryMatch() {
		return queryMatch;
	}

	public void setQueryMatch(boolean[] queryMatch) {
		this.queryMatch = queryMatch;
	}

	public SemEntityBean getSemEntityByTitle(String title)
	{
		TIntObjectIterator<SemEntityBean> iterator = semEntityBeanMap.iterator();
		while(iterator.hasNext())
		{
			SemEntityBean entity = iterator.value();
			if(entity.getTitle().equalsIgnoreCase(title))
				return entity;
			iterator.advance();
		}
		return null;
	}
	
	
}
