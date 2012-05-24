package com.aneedo.jwplext;

import de.tudarmstadt.ukp.wikipedia.api.Title;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiTitleParsingException;

public class JwplCategory {
	private String name;
	private int id;
	private int pageId;
	
	public JwplCategory(int id, int pageId,String name) {
		this.id = id;
		this.pageId = pageId;
		this.name = name;
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPageId() {
		return pageId;
	}

	public void setPageId(int pageId) {
		this.pageId = pageId;
	}

	public JwplCategory(String name) {
		this.name = name;
	}

	public Title getTitle() throws WikiTitleParsingException {
		return new Title(name);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
