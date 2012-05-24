package com.aneedo.search.bean;

import java.util.ArrayList;
import java.util.List;

public class SemClassFeedback {
	Integer queryId;
	String rawQuery;
	String userId;
	List<QueryFacetDetail> queryFacet = new ArrayList<QueryFacetDetail>();
	public Integer getQueryId() {
		return queryId;
	}
	public void setQueryId(Integer queryId) {
		this.queryId = queryId;
	}
	public String getRawQuery() {
		return rawQuery;
	}
	public void setRawQuery(String rawQuery) {
		this.rawQuery = rawQuery;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<QueryFacetDetail> getQueryFacet() {
		return queryFacet;
	}
	public void setQueryFacet(List<QueryFacetDetail> queryFacet) {
		this.queryFacet = queryFacet;
	}
}
