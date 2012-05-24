package com.aneedo.indexing.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aneedo.indexing.IndexingConstants;

public class WikiPageBean {
	String pageTitle="";
	String titleDisambText="";
	String NEType="";
	
	List<String> pageTypes = new ArrayList<String>();
	int pageId=0;
	List<WikiSection> sections = new ArrayList<WikiSection>();
	//String abstractText;
	String abstractVbAdj;
	int noOfInlinks;
	int noOfOutlinks;
	
	
	
	

	public Integer getNoOfInlinks() {
		return noOfInlinks;
	}

	public void setNoOfInlinks(int noOfInlinks) {
		this.noOfInlinks = noOfInlinks;
	}

	public Integer getNoOfOutlinks() {
		return noOfOutlinks;
	}

	public void setNoOfOutlinks(int noOfOutlinks) {
		this.noOfOutlinks = noOfOutlinks;
	}

	public String getAbstractVbAdj() {
		return abstractVbAdj;
	}

	public void setAbstractVbAdj(String abstractVbAdj) {
		this.abstractVbAdj = abstractVbAdj;
	}
	Map<String, SemanticClass> semClassMap = new HashMap<String, SemanticClass>();
	
	public WikiPageBean() {
		semClassMap.put(IndexingConstants.SEM_CLASS_REFERENCE, new SemanticClass());
		semClassMap.put(IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK, new SemanticClass());
		semClassMap.put(IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED, new SemanticClass());
		semClassMap.put(IndexingConstants.SEM_CLASS_ASSOCIATION_HIERARCHY, new SemanticClass());
		semClassMap.put(IndexingConstants.SEM_CLASS_FREQUENT, new SemanticClass());
		semClassMap.put(IndexingConstants.SEM_CLASS_HOMONYM, new SemanticClass());
		semClassMap.put(IndexingConstants.SEM_CLASS_HYPERNYM, new SemanticClass());
		semClassMap.put(IndexingConstants.SEM_CLASS_HYPONYM, new SemanticClass());
		semClassMap.put(IndexingConstants.SEM_CLASS_MERONYM, new SemanticClass());
		semClassMap.put(IndexingConstants.SEM_CLASS_ROLE, new SemanticClass());
		semClassMap.put(IndexingConstants.SEM_CLASS_SIBLING, new SemanticClass());
		semClassMap.put(IndexingConstants.SEM_CLASS_SYNONYM, new SemanticClass());
		semClassMap.put(IndexingConstants.SEM_CLASS_SYNOPSIS, new SemanticClass());
		semClassMap.put(IndexingConstants.SEM_CLASS_SYNONYM_WORDNET, new SemanticClass());
		semClassMap.put(IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK_PARENT, new SemanticClass());
		semClassMap.put(IndexingConstants.SEM_CLASS_COOCCURRENCE, new SemanticClass());
		semClassMap.put(IndexingConstants.SEM_CLASS_MAKEPRODUCE, new SemanticClass());
	}
	
	public void clearAll() {
		pageTitle = null;
		titleDisambText = null;
		pageId = 0;
		NEType = "";
		sections.clear();
		//abstractText = null;
		pageTypes.clear();
		abstractVbAdj = null;
		noOfInlinks =0;
		noOfOutlinks=0;
		clearSemClasses();
	}
	
	public void clearSemClasses() {
		semClassMap.get(IndexingConstants.SEM_CLASS_REFERENCE).clear();
		semClassMap.get(IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK).clear();
		semClassMap.get(IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED).clear();
		semClassMap.get(IndexingConstants.SEM_CLASS_ASSOCIATION_HIERARCHY).clear();
		semClassMap.get(IndexingConstants.SEM_CLASS_FREQUENT).clear();
		semClassMap.get(IndexingConstants.SEM_CLASS_HOMONYM).clear();
		semClassMap.get(IndexingConstants.SEM_CLASS_HYPERNYM).clear();
		semClassMap.get(IndexingConstants.SEM_CLASS_HYPONYM).clear();
		semClassMap.get(IndexingConstants.SEM_CLASS_MERONYM).clear();
		semClassMap.get(IndexingConstants.SEM_CLASS_ROLE).clear();
		semClassMap.get(IndexingConstants.SEM_CLASS_SIBLING).clear();
		semClassMap.get(IndexingConstants.SEM_CLASS_SYNONYM).clear();
		semClassMap.get(IndexingConstants.SEM_CLASS_SYNOPSIS).clear();
		semClassMap.get(IndexingConstants.SEM_CLASS_SYNONYM_WORDNET).clear();
		semClassMap.get(IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK_PARENT).clear();
		semClassMap.get(IndexingConstants.SEM_CLASS_COOCCURRENCE).clear();
		semClassMap.get(IndexingConstants.SEM_CLASS_MAKEPRODUCE).clear();
	}
	
	public List<String> getPageTypes() {
		return pageTypes;
	}

	public void setPageType(List<String> pageTypes) {
		this.pageTypes = pageTypes;
	}

	public String getTitleDisambText() {
		return titleDisambText;
	}

	public void setTitleDisambText(String titleDisambText) {
		this.titleDisambText = titleDisambText;
	}

	public Map<String, SemanticClass> getSemClassMap() {
		return semClassMap;
	}

	public void setSemClassMap(Map<String, SemanticClass> semClassMap) {
		this.semClassMap = semClassMap;
	}

	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	
	public Integer getPageId() {
		return pageId;
	}
	public void setPageId(int pageId) {
		this.pageId = pageId;
	}
//	public String getAbstractText() {
//		return abstractText;
//	}
//	public void setAbstractText(String abstractText) {
//		this.abstractText = abstractText;
//	}
	public List<WikiSection> getSections() {
		return sections;
	}
	public void setSections(List<WikiSection> sections) {
		this.sections = sections;
	}
	public String getNEType() {
		return NEType;
	}
	public void setNEType(String nEType) {
		NEType = nEType;
	}
	
}
