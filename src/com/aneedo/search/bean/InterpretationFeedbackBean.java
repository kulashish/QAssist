package com.aneedo.search.bean;

import java.util.HashMap;
import java.util.Map;

public class InterpretationFeedbackBean {

	String query;
	String userId;
	
	Map<String,String>  InterpretationRank=new HashMap<String, String>();
	Map<String,String>  TitleRel =new HashMap<String, String>();
	
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Map<String, String> getInterpretationRank() {
		return InterpretationRank;
	}
	public void setInterpretationRank(Map<String, String> interpretationRank) {
		InterpretationRank = interpretationRank;
	}
	public Map<String, String> getTitleRel() {
		return TitleRel;
	}
	public void setTitleRel(Map<String, String> titleRel) {
		TitleRel = titleRel;
	}
 }
