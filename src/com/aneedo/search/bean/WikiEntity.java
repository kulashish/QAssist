package com.aneedo.search.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WikiEntity {
	Integer pageId;
	String title;
	String disamb;
	boolean isDisam;
	List<String> sections = new ArrayList<String>();
	Map<Integer, String> semClassMap = new HashMap<Integer, String>();

	
	public Integer getPageId() {
		return pageId;
	}
	public void setPageId(Integer pageId) {
		this.pageId = pageId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDisamb() {
		return disamb;
	}
	public void setDisamb(String disamb) {
		this.disamb = disamb;
	}
	public boolean isDisam() {
		return isDisam;
	}
	public void setDisam(boolean isDisam) {
		this.isDisam = isDisam;
	}
	public List<String> getSections() {
		return sections;
	}
	public void setSections(List<String> sections) {
		this.sections = sections;
	}
	public Map<Integer, String> getSemClassMap() {
		return semClassMap;
	}
	public void setSemClassMap(Map<Integer, String> semClassMap) {
		this.semClassMap = semClassMap;
	}
}
