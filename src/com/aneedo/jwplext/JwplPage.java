package com.aneedo.jwplext;

import java.io.BufferedWriter;
import java.sql.PreparedStatement;
import java.util.Set;

import com.aneedo.jwplext.dao.DisambPageInLinkDao;
import com.aneedo.jwplext.dao.PageDisambDao;
import com.aneedo.jwplext.dao.PageRedirectDao;

import de.tudarmstadt.ukp.wikipedia.api.Title;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiTitleParsingException;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;

/**
 * Overriding this to data from DB directly and avoid connection pooling issue
 * of Hibernate
 * 
 * @author
 * 
 */
public class JwplPage {
	private int pageId;
	private int id;
	private ParsedPage parsedPage;
	private Boolean isDisambiguated;
	private String name;
	private Set<JwplCategory> jwplCategories;
	private int noOfInLinks;
	private int noOfOutLinks;
	

	public int getNoOfInLinks() {
		return noOfInLinks;
	}

	public void setNoOfInLinks(int noOfInLinks) {
		this.noOfInLinks = noOfInLinks;
	}

	public int getNoOfOutLinks() {
		return noOfOutLinks;
	}

	public void setNoOfOutLinks(int noOfOutLinks) {
		this.noOfOutLinks = noOfOutLinks;
	}

	public Title getTitle() throws WikiTitleParsingException {
		if(name == null) return null;
		return new Title(name);
	}

	public JwplPage() {

	}

	public JwplPage(int id, int pageId, String name, boolean isDisambiguated) {
		this.pageId = pageId;
		this.name = name;
		this.isDisambiguated = isDisambiguated;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public Set<String> getRedirects(PreparedStatement pstmt,
			BufferedWriter errorWriter) {
		return PageRedirectDao.getInstance()
				.getRedirect(id, pstmt, errorWriter);
	}
	
	public Set<String> getPageInLinks(PreparedStatement pstmt,
			BufferedWriter errorWriter) {
		return DisambPageInLinkDao.getInstance()
				.getDisambPageInLinks(id, pstmt, errorWriter);
	}
	public boolean isDisambiguation() {
		return isDisambiguated;
	}

	public void setJwplCategories(Set<JwplCategory> jwplCategory) {
		this.jwplCategories = jwplCategory;
	}

	public Set<JwplCategory> getJwplCategories() {
		return jwplCategories;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPageId() {
		return pageId;
	}

	public void setPageId(int pageId) {
		this.pageId = pageId;
	}

	public void setParsedPage(String text) {
		this.parsedPage = JwplMediaWikiParserFactory.getInstance().getParser()
				.parse(text);
	}

	public ParsedPage getParsedPage() {
		return parsedPage;
	}

	public JwplPage getDisamgPage(String pageName, PreparedStatement pstmt,
			BufferedWriter errorWriter) {
		String title = pageName.replace("(", "").trim();
		//System.out.println("Title to disamb " + pageName+" :" + title);
		return PageDisambDao.getInstance().getDisambPage(title, pstmt,
				errorWriter);
	}

}
