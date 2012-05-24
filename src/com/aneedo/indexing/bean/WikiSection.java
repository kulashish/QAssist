package com.aneedo.indexing.bean;


public class WikiSection{
	String sectionName;
//	List<String> subSections = new ArrayList<String>();
//	List<WikiLink> internalLinks =  new ArrayList<WikiLink>();
	String imageText =null;
	
	
	
	public String getImageText() {
		return imageText;
	}
	public void setImageText(String imageText) {
		this.imageText = imageText;
	}
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
//	public List<String> getSubSections() {
//		return subSections;
//	}
//	public void setSubSections(List<String> subSections) {
//		this.subSections = subSections;
//	}
//	public List<WikiLink> getInternalLinks() {
//		return internalLinks;
//	}
//	public void setInternalLinks(List<WikiLink> internalLinks) {
//		this.internalLinks = internalLinks;
//	}
	
}