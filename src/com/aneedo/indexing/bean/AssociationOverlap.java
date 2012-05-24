package com.aneedo.indexing.bean;

public class AssociationOverlap {
	
	Integer linkId;
	int linkOverlapCount;
	String linkParentOverlap;
	String linkCatOverlapId;
	
	public String getLinkCatOverlapId() {
		return linkCatOverlapId;
	}
	public void setLinkCatOverlapId(String linkCatOverlapId) {
		this.linkCatOverlapId = linkCatOverlapId;
	}
	public Integer getLinkId() {
		return linkId;
	}
	public void setLinkId(Integer linkId) {
		this.linkId = linkId;
	}
	public Integer getLinkOverlapCount() {
		return linkOverlapCount;
	}
	public void setLinkOverlapCount(Integer linkOverlapCount) {
		this.linkOverlapCount = linkOverlapCount;
	}
	public String getLinkParentOverlap() {
		return linkParentOverlap;
	}
	public void setLinkParentOverlap(String linkCatOverlap) {
		this.linkParentOverlap = linkCatOverlap;
	}
}
