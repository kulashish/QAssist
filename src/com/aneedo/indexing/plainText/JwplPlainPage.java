package com.aneedo.indexing.plainText;



public class JwplPlainPage {
	private int pageId;
	private int id;
	private String pageTitle;
	private String pageText;
	
	public void setPageTitle(String title){
		this.pageTitle=title;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public JwplPlainPage() {
	}

	public JwplPlainPage(int id, int pageId, String title) {
		this.pageId = pageId;
		this.pageTitle = title;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getPageText() {
		return pageText;
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

	public void setPageText(String text){
		this.pageText=text;
	}
}
