package com.aneedo.indexing;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aneedo.indexing.bean.AssociationOverlap;
import com.aneedo.indexing.bean.SemanticClass;
import com.aneedo.indexing.bean.WikiPageBean;
import com.aneedo.indexing.bean.WikiSection;
import com.aneedo.jwplext.JwplCategory;
import com.aneedo.jwplext.JwplPage;
import com.aneedo.jwplext.JwplSQLConstants;
import com.aneedo.jwplext.dao.AssociationDao;
import com.aneedo.jwplext.dao.DisambPageInLinkDao;
import com.aneedo.jwplext.dao.PageCategoryDao;
import com.aneedo.jwplext.dao.PageDao;
import com.aneedo.jwplext.dao.tablecreation.DBConnectionFactory;
import com.aneedo.search.util.TagText;
import com.aneedo.util.ExtractionUtil;
import com.aneedo.util.InterestingWordRecognizer;
import com.aneedo.util.PorterStemmer;
import com.aneedo.util.StopwordRecognizer;
import com.aneedo.util.WordnetUtils;

import de.tudarmstadt.ukp.wikipedia.api.Title;
import de.tudarmstadt.ukp.wikipedia.parser.Content;
import de.tudarmstadt.ukp.wikipedia.parser.Link;
import de.tudarmstadt.ukp.wikipedia.parser.Section;
import de.tudarmstadt.ukp.wikipedia.parser.SectionContainer;
import de.tudarmstadt.ukp.wikipedia.parser.Template;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.FlushTemplates;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParser;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParserFactory;

public class WikiPageExtractorThread {
//
//	private PreparedStatement pageCategoryStmt = null;
//	private PreparedStatement linkOverlapStmt = null;
//	private PreparedStatement linkCatOverlapStmt = null;
//	Connection conn = null;
//
//	private PreparedStatement pageRedirectStmt = null;
//	private PreparedStatement disambPagePageIdStmt = null;
//	private PreparedStatement disambPageDataStmt = null;
//	private PreparedStatement pageDisambStmt = null;
//	private PreparedStatement selectLinkCatOverlapStmt=null;
//	private PreparedStatement inLinkStmt=null;
//	private PreparedStatement outLinkStmt=null;
//	private PreparedStatement inLinkIdStmt=null;
//	private PreparedStatement selectOutlinksStmt = null;
//	private PreparedStatement selectAssOverlapStmt  = null;
//	private InterestingWordRecognizer intWordRecog = InterestingWordRecognizer.getInstance(null);
//
//	// TODO plug in connection pooling
//	public WikiPageExtractorThread(String dbName,String password,BufferedWriter errorWriter) {
//		try {
//			if (conn == null) {
//				conn = DBConnectionFactory.getInstance(dbName,password).getConnectionOfDatabase();
//				initStatements();
//			}
//		} catch (Exception e) {
//			try {
//				errorWriter.write("Initializing connection failed : " + e.getMessage() + " " + e.getCause()
//						+ "\n");
//			} catch (Exception exp) {
//				exp.printStackTrace();
//			}
//		}
//	}
//
//	public void closeDB(BufferedWriter errorWriter) {
//		closeStatements(errorWriter);
//		if (conn != null) {
//			try {
//				conn.close();
//			} catch (Exception e) {
//				try {
//					errorWriter.write("Closing connection failed : " + e.getMessage() + " " + e.getCause()
//							+ "\n");
//					e.printStackTrace();
//				} catch (Exception exp) {
//					exp.printStackTrace();
//				}
//			}
//		}
//
//	}
//	private void getCleanText() throws Exception {
//		MediaWikiParserFactory pf = new MediaWikiParserFactory();
//		pf.setTemplateParserClass(FlushTemplates.class);
//
//		MediaWikiParser parser = pf.createParser();
//
//		JwplPage page = getPage(198584, null);
//		// ParsedPage pp =
//		// parser.parse(page.getParsedPage().getSection(1).getText());
//		// System.out.println(pp.getText());
//		List<Content> pageContent = page.getParsedPage().getSection(1)
//				.getContentList();
//		for (int i = 0; i < pageContent.size(); i++) {
//			//System.out.println(pageContent.get(i).getText());
//
//		}
//	}
//
//	//	public void setNewConnection(BufferedWriter errorWriter, int pageId)
//	//			throws Exception {
//	//		Connection con = DBConnection.getInstance().getConnection();
//	//		closeConnection(errorWriter, pageId);
//	//		conn = con;
//	//		initStatements();
//	//	}
//
//	//	public void closeConnection(BufferedWriter errorWriter, int pageId) {
//	//		try {
//	//			closeStatements(errorWriter, pageId);
//	//			DBConnection.getInstance().close(conn);
//	//		} catch (Exception e) {
//	//			conn = null;
//	//			try {
//	//				errorWriter.write("connection closing falied at page id : "
//	//						+ pageId + e.getMessage() + " "
//	//						+ e.getCause().toString() + "\n");
//	//				e.printStackTrace();
//	//			} catch (Exception exp) {
//	//				exp.printStackTrace();
//	//			}
//	//		}
//	//	}
//
//	private void initStatements() throws Exception {
//		pageCategoryStmt = conn
//				.prepareStatement(JwplSQLConstants.SELECT_PAGE_CATEGORY_DETAIL);
//		inLinkStmt=conn.prepareStatement(JwplSQLConstants.SELECT_COUNT_INLINKS);
//		outLinkStmt=conn.prepareStatement(JwplSQLConstants.SELECT_COUNT_OUTLINKS);
//		linkOverlapStmt = conn
//				.prepareStatement(JwplSQLConstants.SELECT_LINK_OVERLAP_COUNT);
//
//		linkCatOverlapStmt = conn
//				.prepareStatement(JwplSQLConstants.SELECT_LINK_CATEGORY_OVERLAP);
//		inLinkIdStmt=conn.prepareStatement(JwplSQLConstants.SELECT_GET_INLINK_ID);
//		selectOutlinksStmt = conn
//				.prepareStatement(JwplSQLConstants.SELECT_OUTLINKS);
//		selectLinkCatOverlapStmt = conn
//				.prepareStatement(JwplSQLConstants.SELECT_ASS_PARENT_OVERLAP);
//		selectAssOverlapStmt = conn
//				.prepareStatement(JwplSQLConstants.SELECT_ASS_OVERLAP_QUERY);
//		pageRedirectStmt = conn.prepareStatement(JwplSQLConstants.SELECT_REDIRECT);
//		pageDisambStmt = conn.prepareStatement(JwplSQLConstants.SELECT_DISAMB);
//		disambPagePageIdStmt = conn.prepareStatement(JwplSQLConstants.SELECT_DISAMB_PAGEID);
//		disambPageDataStmt = conn.prepareStatement(JwplSQLConstants.SELECT_DISAMB_DATA);
//		// childCategoryStmt = conn
//		// .prepareStatement(JwplConstants.SELECT_SUB_CATEGORY);
//		// articleOfCategoryStmt = conn
//		// .prepareStatement(JwplConstants.SELECT_PAGES_OF_CATEGORY);
//	}
//
//	private void closeStatements(BufferedWriter errorWriter) {
//
//		try {
//			pageCategoryStmt.close();
//			// childCategoryStmt.close();
//			// articleOfCategoryStmt.close();
//			inLinkStmt.close();
//			outLinkStmt.close();
//			pageRedirectStmt.close();
//			pageDisambStmt.close();
//			inLinkIdStmt.close();
//			selectOutlinksStmt.close();
//			selectLinkCatOverlapStmt.close();
//			selectAssOverlapStmt.close();
//			linkOverlapStmt.close();
//			linkCatOverlapStmt.close();
//			disambPageDataStmt.close();
//			disambPagePageIdStmt.close();
//
//		} catch (Exception e) {
//			pageCategoryStmt = null;
//			// childCategoryStmt = null;
//			// articleOfCategoryStmt = null;
//			selectOutlinksStmt = null;
//			selectLinkCatOverlapStmt = null;
//			selectAssOverlapStmt = null;
//			inLinkStmt=null;
//			outLinkStmt=null;
//			pageRedirectStmt = null;
//			pageDisambStmt = null;
//			inLinkIdStmt = null;
//			linkOverlapStmt = null;
//			linkCatOverlapStmt = null;
//			disambPageDataStmt = null;
//			disambPagePageIdStmt = null;
//			try {
//				errorWriter.write("Closing statements " 
//						+ " failed : " + e.getMessage() + " " + e.getCause()
//						+ "\n");
//				e.printStackTrace();
//			} catch (Exception exp) {
//				exp.printStackTrace();
//			}
//		}
//	}
//
//	public void initWikiPageDetails(WikiPageBean wikiPageBean,
//			BufferedWriter errorWriter) {
//		int pageId = wikiPageBean.getPageId();
//		JwplPage page = getPage(pageId, errorWriter);
//		if (page != null) {
//
//			// Page title
//			try {
//				Title title = page.getTitle();
//				String wikiTitle = title.getWikiStyleTitle();
//				//System.out.println("Wiki title :" + wikiTitle);
//				int disTextStart = wikiTitle.indexOf("(");
//				//System.out.println("Wiki title position :" + disTextStart);
//				String plainTitle = title.getPlainTitle();
//				//System.out.println("Plain title : " + plainTitle);
//				if (disTextStart >= 0 && wikiTitle.indexOf(")") > wikiTitle.length()-2) {
//					wikiTitle = wikiTitle.substring(disTextStart + 1)
//							.replace(")", "").replaceAll("_", " ").trim();
//					//System.out.println("Wiki title :" + wikiTitle);
//					plainTitle = plainTitle.substring(0,
//							plainTitle.indexOf(wikiTitle)).replace("(", "").trim();
//				} else {
//					wikiTitle = null;
//				}
//				//System.out.println("Plain title after removing disamb : "
//				//	+ plainTitle);
//				wikiPageBean.setPageTitle(plainTitle);
//
//				// disambiguation text
//				if (wikiTitle != null) {
//					wikiPageBean.setTitleDisambText(wikiTitle);
//					//System.out.println("title disamb : " + wikiTitle);
//				}
//
//				PorterStemmer stemmer = new PorterStemmer();
//
//				// If disambiguation page treat separately
//				setHomonyms(page, wikiPageBean, errorWriter);
//				if(page.isDisambiguation()) {
//					return;
//				}
//
//				List<Section> sectionList = page.getParsedPage().getSections();
//				//System.out.println("Size of the section size : "
//				//		+ sectionList.size());
//
//				// abstract, frequent words, links in abstract carry a lot
//				// information
//				Section abstractSection = sectionList.get(0);
//				//				String freqWords = null;
//				String secondPara = "";
//				if(abstractSection.getParagraph(1) != null){
//					secondPara = abstractSection.getParagraph(1).getText();
//				}
//				//System.out.println("second para : " + secondPara);
//				String[] abstractContent = intWordRecog.getInterestingWords(abstractSection.getText());
//				//				wikiPageBean.setAbstractText(abstractContent[0]);
//				wikiPageBean.setAbstractVbAdj(abstractContent[1]);
//				wikiPageBean.getSemClassMap().
//				get(IndexingConstants.SEM_CLASS_SYNOPSIS).getWords().append(abstractContent[0]);
//
//				Map<String,AssociationOverlap> assMap = AssociationDao.getInstance().
//						getAssociationOverlap(pageId, new PreparedStatement[] {selectOutlinksStmt,selectAssOverlapStmt,	
//								selectLinkCatOverlapStmt}, errorWriter, null);
//
//				// Set type/hypernym of the page from first para
//				String firstPara ="";
//				if(abstractSection.getParagraph(0) != null)
//					firstPara = abstractSection.getParagraph(0).getText();
//
//				setPageType(wikiPageBean, firstPara+secondPara, stemmer, abstractSection.getLinks(Link.type.INTERNAL));
//
//				// set synonyms
//				Set<String> Synonyms = setSynonyms(page, wikiPageBean,firstPara
//						+ " " + secondPara, errorWriter, stemmer);
//				String titleCompare = getAllTitleRelatedWords(wikiPageBean.getPageTitle());
//
//				//setSectionDetails(abstractSection, wikiPageBean, assMap, titleCompare, errorWriter);
//
//				//System.out.println("Page Abstract : " + wikiPageBean.getAbstractText());
//				//				freqWords = TermFreqBasedTrimmer.getInstance().getFreqWords(
//				//						abstractSection.getText(), stemmer, plainTitle,intWordRecog);
//				//				//System.out.println("Frequent words : " + freqWords);
//				//				if (freqWords != null && freqWords.length() > 5) {
//				//					wikiPageBean.getSemClassMap().get(
//				//							IndexingConstants.SEM_CLASS_FREQUENT).getWords()
//				//							.append(freqWords);
//				//				}
//
//				// set info box details
//				setDetailsFromInfoBox(page, wikiPageBean);
//
//
//				// Set other sections, from last three see also starts
//				// There are pages without see also, reference etc, then
//				// last three sections will be blindly ignored
//				for (int j = 0, size = sectionList.size(); j < size; j++) {
//					setSectionDetails(sectionList.get(j), wikiPageBean,assMap,titleCompare,
//							errorWriter);
//
//					//					freqWords = TermFreqBasedTrimmer.getInstance()
//					//							.getFreqWords(sectionList.get(j).getText(),
//					//									stemmer, plainTitle,intWordRecog);
//					//					if (freqWords != null && freqWords.length() > 5) {
//					//						wikiPageBean.getSemClassMap().get(
//					//								IndexingConstants.SEM_CLASS_FREQUENT)
//					//								.getWords().append(freqWords);
//					//					}
//				}
//
//				setRelatedExternal(wikiPageBean, sectionList, assMap, errorWriter);
//				setNEType(wikiPageBean,sectionList, page, errorWriter);
//				// set homonym
//				//setHomonyms(page, wikiPageBean, errorWriter);
//				setWordnetSynonymWords(wikiPageBean, stemmer);
//				getNoOfInLinks(pageId, wikiPageBean, errorWriter);
//				getNoOfOutLinks(pageId, wikiPageBean, errorWriter);
//
//				//System.out.println("Before Category extraction ...");
//				// Select category and pages
//				setCategoriesPages(wikiPageBean, page, errorWriter, stemmer, Synonyms);
//
//				//System.out.println("Before Related extraction ...");
//				// set see also and External Links
//
//			} catch (Exception e) {
//				try {
//					errorWriter.write("Accessing page title id : " + pageId
//							+ " failed : " + e.getMessage() + " "
//							+ e.getCause() + "\n");
//					e.printStackTrace();
//				} catch (Exception exp) {
//					exp.printStackTrace();
//				}
//			}
//		}
//
//	}
//
//	private void setNEType(WikiPageBean wikiPageBean, List<Section> sections,  JwplPage page,
//			BufferedWriter errorWriter){
//
//
//		List<WikiSection> wikiSections=wikiPageBean.getSections();
//		Iterator<WikiSection> wikiSecIter = wikiSections.iterator();
//		while(wikiSecIter.hasNext()){
//			WikiSection wikiSec=wikiSecIter.next();
//			String secName = wikiSec.getSectionName();
//			String nameWords[] = secName.split(" / ");
//			for(int i=0;i<nameWords.length;i++){
//				String NEType=getNEString(nameWords[i]);
//				if(NEType==null) continue;
//				else {
//					wikiPageBean.setNEType(NEType);
//					return;
//				}
//			}
//
//		}
//
//		// Looking for keywords in section names used in the case of person NE Type
//
//		//		//	int index = -1;
//		//			for(int i=0;i<sections.size();i++){
//		//				Section curSection=sections.get(i);
//		//				
//		//				
//		//				if(curSection instanceof SectionContainer){
//		//					SectionContainer sectioncontainer = (SectionContainer) curSection;
//		//					List<Section> subsections = sectioncontainer.getSubSections();
//		//					Iterator<Section> subsectionsIter=subsections.iterator();
//		//					while(subsectionsIter.hasNext()){
//		//						Section curSubSection = subsectionsIter.next();
//		//						String subSecTitle = curSubSection.getTitle();
//		//						String NEType=getNEString(subSecTitle);
//		//						if(NEType==null) continue;
//		//						else {
//		//							wikiPageBean.setNEType(NEType);
//		//							return;
//		//						}
//		//					}
//		//				}
//		//				secTitle= curSection.getTitle();
//		//				String NEType=getNEString(secTitle);
//		//				if(NEType!=null)
//		//				{
//		//					wikiPageBean.setNEType(NEType);
//		//					return;
//		//				}
//		//			}
//		// Looking for the matching words in category names for all other NE types.
//		Set<JwplCategory> categories = page.getJwplCategories();
//		Iterator<JwplCategory> catIterator = categories.iterator();
//		try{
//			while(catIterator.hasNext())
//			{
//				JwplCategory jwplCat= catIterator.next();
//				String catName=jwplCat.getTitle().getPlainTitle();
//				String NEType = getNEStringUsingCategories(catName);
//				if(NEType==null) continue;
//				else {
//					wikiPageBean.setNEType(NEType);
//					return;
//				}
//			}
//		}
//		catch(Exception e){
//			wikiPageBean.setNEType("");
//			try {
//				errorWriter.write("setNEType : Accessing category details of : "
//						+ wikiPageBean.getPageId() + " failed : "
//						+ e.getMessage() + " " + e.getCause() + "\n");
//				//e.printStackTrace();
//			} catch (Exception exp) {
//				exp.printStackTrace();
//			}
//		}
//
//	}
//
//	public static String getNEStringUsingCategories(String keyWord){
//		String[] companyWords={"company","manufacturer"};
//		Set<String> companyKeyWords= new HashSet<String>(Arrays.asList(companyWords));
//		Iterator<String> companyIter= companyKeyWords.iterator();
//		while(companyIter.hasNext())
//		{
//			if(keyWord.indexOf((String)companyIter.next())>=0)
//				return IndexingConstants.NE_TYPE_COMPANY;
//		}
//
//		String[] academiaWords={"university","college","institute","organization"};
//		Set<String> academiaKeyWords= new HashSet<String>(Arrays.asList(academiaWords));
//
//		Iterator<String> academiaIter= academiaKeyWords.iterator();
//		while(academiaIter.hasNext())
//		{
//			if(keyWord.indexOf((String)academiaIter.next())>=0)
//				return IndexingConstants.NE_TYPE_ACADEMIA;
//		}
//
//		String[] locationWords={"city","county","capital","district"};		
//		Set<String> locationKeyWords = new HashSet<String>(Arrays.asList(locationWords));
//
//		Iterator<String> locationIter= locationKeyWords.iterator();
//		while(locationIter.hasNext())
//		{
//			if(keyWord.indexOf((String)locationIter.next())>=0)
//				return IndexingConstants.NE_TYPE_LOCATION;
//		}
//
//		String[] musicWords={"band","song","album"};		
//		Set<String> musicKeyWords = new HashSet<String>(Arrays.asList(musicWords));
//
//		Iterator<String> musicIter= musicKeyWords.iterator();
//		while(locationIter.hasNext())
//		{
//			if(keyWord.indexOf((String)musicIter.next())>=0)
//				return IndexingConstants.NE_TYPE_MUSIC;
//		}
//
//		String[] movieWords={"film","movie"};		
//		Set<String> movieKeyWords = new HashSet<String>(Arrays.asList(movieWords));
//
//		Iterator<String> movieIter= movieKeyWords.iterator();
//		while(locationIter.hasNext())
//		{
//			if(keyWord.indexOf((String)movieIter.next())>=0)
//				return IndexingConstants.NE_TYPE_MOVIE;
//		}
//		return null;
//	}
//
//	public static String getNEString(String keyWord){
//		if(keyWord == null) {
//			return keyWord;
//		}
//		String[] personWords={"early life","career","biography","personal"};
//		Set<String> personKeyWords=new HashSet<String>(Arrays.asList(personWords));
//		// System.out.println("Keyword : " + keyWord);
//		Iterator<String> personIter= personKeyWords.iterator();
//		while(personIter.hasNext())
//		{
//			if(keyWord.indexOf((String)personIter.next())>=0)
//				return IndexingConstants.NE_TYPE_PERSON;
//		}
//		return null;
//	}
//
//	private void setRelatedExternal(WikiPageBean wikiPageBean, List<Section> sections,
//			Map<String,AssociationOverlap> assMap, BufferedWriter errorWriter) {
//		// List<Section> sections = page.getParsedPage().getSections();
//		if (sections != null) {
//
//			String secTitle = null;
//			int index = -1;
//			List<Integer> exIndex = new ArrayList<Integer>();
//
//			// Need to check the section name because there many sections
//			// without see also
//			// reference sections
//			for (int i = sections.size() - 1; i >= 0; i--) {
//				secTitle = sections.get(i).getTitle();
//				if (secTitle != null) {
//					secTitle = secTitle.toLowerCase();
//					//System.out.println("Section Title : " + secTitle);
//
//					if ((secTitle.indexOf("see") >= 0)
//							&& (secTitle.indexOf("also") >= 0)) {
//						index = i;
//						break;
//					}
//
//					if ((secTitle.indexOf("reference") >= 0)
//							|| ((secTitle.indexOf("external") >= 0) && (secTitle
//									.indexOf("link") >= 0))
//									|| (secTitle.indexOf("note") >= 0)
//									|| ((secTitle.indexOf("further") >= 0) && (secTitle
//											.indexOf("reading") >= 0))) {
//						exIndex.add(i);
//						continue;
//					}
//				}
//			}
//
//			if (index != -1) {
//
//				setRelated(sections.get(index), wikiPageBean, assMap,
//						errorWriter);
//			}
//
//			// There are multiple external link sections
//			for (int i = 0, size = exIndex.size(); i < size; i++) {
//
//				setExternal(sections.get(exIndex.get(i)), wikiPageBean,
//						errorWriter);
//
//			}
//		}
//	}
//
//	private void setCategoriesPages(WikiPageBean wikiPageBean, JwplPage page,
//			BufferedWriter errorWriter, PorterStemmer stemmer,Set<String> synonyms) {
//		Set<JwplCategory> categories = page.getJwplCategories();
//		Map<Integer, String> categoryMap = new HashMap<Integer, String>();
//		StringBuilder filteredCats = new StringBuilder();
//		Iterator<JwplCategory> categoriesIterator = categories.iterator();
//		ExtractionUtil util = ExtractionUtil.getInstance();
//		JwplCategory category = null;
//		String catName = null;
//		Map<String, SemanticClass> semClassMap = wikiPageBean.getSemClassMap();
//		String pageTitle = wikiPageBean.getPageTitle();
//		if(wikiPageBean.getTitleDisambText() != null) {
//			pageTitle = pageTitle + " " + wikiPageBean.getTitleDisambText();
//		}
//		String[] catNameSplits = null;
//		String[] titleSplits = null;
//
//		List<String> toCompareList = new ArrayList<String>();
//		toCompareList.add(pageTitle);
//		toCompareList.addAll(wikiPageBean.getPageTypes());
//		String[] splits = semClassMap.get(IndexingConstants.SEM_CLASS_ROLE).getWords()
//				.toString().split(" ");
//		toCompareList.addAll(Arrays.asList(splits));
//		//		splits = semClassMap.get(IndexingConstants.SEM_CLASS_SYNONYM)
//		//				.getWords().toString().split(" ");
//		//		toCompareList.addAll(Arrays.asList(splits));
//		//		splits = semClassMap.get(IndexingConstants.SEM_CLASS_SYNONYM_WORDNET)
//		//		.getWords().toString().split(" ");
//		//		toCompareList.addAll(Arrays.asList(splits));
//		toCompareList.addAll(synonyms);
//
//		String tocompareAss = semClassMap.get(
//				IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK)
//				.getWords().toString()+" "+semClassMap.get(
//						IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED)
//						.getWords().toString();
//
//
//		while (categoriesIterator.hasNext()) {
//			category = categoriesIterator.next();
//			try {
//				catName = category.getTitle().getPlainTitle().replaceAll("_", " ")
//						.replace("(", "").replace(")", "");
//
//				//System.out.println("Cat Name : " + catName);
//				// If number is there ignore that category
//				if (!util.canIncludeCategory(catName)) {
//					continue;
//				}
//				//System.out.println("after can include Cat Name : " + catName);
//				// If both are having the same number of words and catname is
//				// plural of page title
//				// Include its sub categories
//				catNameSplits = ExtractionUtil.getInstance().getRootFormStrings(catName, stemmer).split(" ");
//				titleSplits = ExtractionUtil.getInstance().getRootFormStrings(pageTitle, stemmer).split(" ");
//
//				if (catNameSplits.length == titleSplits.length) {
//					boolean flag = true;
//					for (int i = 0; i < catNameSplits.length; i++) {
//
//						if (!catNameSplits[i].equals(titleSplits[i])) {
//							flag = false;
//							break;
//						}
//					}
//					if (flag) {
//						//System.out.println("Cat Name : " + catName +" " + category.getPageId());
//						categoryMap.put(category.getPageId(), catName.replaceAll("_", " ").replace("(", "").replace(")", ""));
//						filteredCats.append(category.getPageId() + ",");
//						continue;
//					}
//				}
//
//				// Select only those which are having high similarity measure
//				if (util.isJaccardSimHigh(toCompareList, catName, stemmer)
//						|| tocompareAss.indexOf(catName) >= 0 
//						|| util.isJaccardHigh(tocompareAss, catName, stemmer)) {
//					//System.out.println("Cat Name : " + catName+" " + category.getPageId());
//					semClassMap.get(IndexingConstants.SEM_CLASS_HYPERNYM)
//					.getWords().append(catName + " | ");
//					categoryMap.put(category.getPageId(), catName.replaceAll("_", " ").replace("(", "").replace(")", ""));
//
//					filteredCats.append(category.getPageId() + ",");
//				} 
//
//			} catch (Exception e) {
//				try {
//					errorWriter.write("setCategoriesPages : Accessing category details of : "
//							+ wikiPageBean.getPageId() + " failed : "
//							+ e.getMessage() + " " + e.getCause() + "\n");
//					e.printStackTrace();
//				} catch (Exception exp) {
//					exp.printStackTrace();
//				}
//			}
//
//		}
//		splits = semClassMap.get(IndexingConstants.SEM_CLASS_HYPERNYM)
//				.getWords().toString().split(" | ");
//		toCompareList.addAll(Arrays.asList(splits));
//		if(filteredCats.length() > 0) {
//			setSubCategoryPages(categoryMap, stemmer, toCompareList, semClassMap,
//					errorWriter, filteredCats.substring(0,
//							filteredCats.length() - 1), tocompareAss);
//		}
//		page.setJwplCategories(null);
//
//	}
//
//	private void setSubCategoryPages(Map<Integer, String> categoryMap,
//			PorterStemmer stemmer, List<String> toCompareList,
//			Map<String, SemanticClass> semClassMap, BufferedWriter errorWriter,
//			String catIds, String tocompareAss) {
//
//		// Add category
//		PageCategoryDao dao = PageCategoryDao.getInstance();
//		StringBuilder[] filteredData = dao.getFilteredSubCatArticles(categoryMap,
//				catIds, conn, errorWriter, stemmer, toCompareList, tocompareAss);
//		try {
//			if(filteredData != null) {
//				semClassMap.get(
//						IndexingConstants.SEM_CLASS_ASSOCIATION_HIERARCHY)
//						.getWords().append(filteredData[0]);
//				semClassMap.get(IndexingConstants.SEM_CLASS_HYPONYM).getWords()
//				.append(filteredData[1]);
//				semClassMap.get(IndexingConstants.SEM_CLASS_SIBLING).getWords()
//				.append(filteredData[2]);
//			}
//		} catch (Exception e) {
//			try {
//				errorWriter.write("SetSubCategoryPages : Accessing pages details of : "
//						+ toCompareList.get(0) + " failed : " + e.getMessage()
//						+ " " + e.getCause() + "\n");
//				e.printStackTrace();
//			} catch (Exception exp) {
//				exp.printStackTrace();
//			}
//		}
//	}
//
//	private void setDetailsFromInfoBox(JwplPage page, WikiPageBean wikiPageBean) {
//		if(page.getParsedPage().getParagraph(0) == null) 
//			return;
//		List<Template> templates = page.getParsedPage().getParagraph(0)
//				.getTemplates(1, 3);
//		Template template;
//		String infoBoxKey = ExtractionUtil.getInstance().getInfoBoxKeys();
//		String key = null;
//		for (int j = 0, size = templates.size(); j < size; j++) {
//			template = templates.get(j);
//			if (j == 2) {
//				List<String> parameters = template.getParameters();
//
//				Map<String, SemanticClass> semClass = wikiPageBean
//						.getSemClassMap();
//				for (int k = 0, keySize = parameters.size(); k < keySize; k++) {
//
//					String parameter = parameters.get(k);
//					if (parameter.indexOf("=") < 0)
//						continue;
//					String[] parameter_words = parameter.split("=");
//					if(parameter_words.length < 2)
//						continue;
//					key = parameter_words[0].trim().toLowerCase();
//					//System.out.println("Info box key : " + key + "Parameter : " + parameter);
//					if (infoBoxKey.indexOf(key) >= 0 && parameter_words.length > 1) {
//						if (key.indexOf("name") >= 0) {
//							semClass.get(IndexingConstants.SEM_CLASS_SYNONYM)
//							.getWords().append(
//									parameter_words[1].replace("[", "")
//									.replace("]", "").replace(
//											"<br>", "") +" | ");
//						} else if (key.indexOf("product") >= 0
//								|| key.indexOf("industry") >= 0) {
//							semClass.get(IndexingConstants.SEM_CLASS_MAKEPRODUCE)
//							.getWords().append(
//									parameter_words[1].replace("[", "")
//									.replace("]", "").replace(
//											"<br>", "")+" | ");
//						} else if (key.indexOf("type") >= 0) {
//							semClass.get(IndexingConstants.SEM_CLASS_HYPERNYM)
//							.getWords().append(
//									parameter_words[1].replace("[", "")
//									.replace("]", "").replace(
//											"<br>", "")+" | ");
//						} else if (key.indexOf("known for") >= 0
//								|| key.indexOf("occupation") >= 0
//								|| key.indexOf("profession") >= 0
//								|| key.indexOf("role") >= 0
//								|| key.indexOf("founder") >= 0
//								|| key.indexOf("people") >= 0) {
//							semClass.get(IndexingConstants.SEM_CLASS_ROLE)
//							.getWords().append(
//									parameter_words[1].replace("[", "")
//									.replace("]", "").replace(
//											"<br>", "")+" | ");
//
//						} else if (key.indexOf("develop") >= 0
//								|| key.indexOf("manufact") >= 0) {
//							semClass.get(
//									IndexingConstants.SEM_CLASS_MAKEPRODUCE)
//									.getWords().append(
//											parameter_words[1].replace("[", "")
//											.replace("]", "").replace(
//													"<br>", "")+" | ");
//						}
//					}
//				}
//			}
//		}
//	}
//
//	private void setHomonyms(JwplPage page, WikiPageBean wikiPageBean,
//			BufferedWriter errorWriter) {
//		SemanticClass semClass = wikiPageBean.getSemClassMap().get(
//				IndexingConstants.SEM_CLASS_HOMONYM);
//		if (page.isDisambiguation()) {
//			final String disambDtls = DisambPageInLinkDao.getInstance().getDisambDetails(wikiPageBean.getPageId(), 
//					disambPageDataStmt, errorWriter);
//			if(disambDtls != null)
//				semClass.getWords().append(disambDtls);
//		} else {
//
//			final String disambDtls = DisambPageInLinkDao.getInstance().getDisambPageId(wikiPageBean.getPageId(), 
//					disambPagePageIdStmt, errorWriter);
//			if(disambDtls != null)
//				semClass.getWords().append(disambDtls);
//		}
//	}
//
//	private Set setSynonyms(JwplPage page, WikiPageBean wikiPageBean,
//			String text, BufferedWriter errorWriter, PorterStemmer stemmer) {
//		// redirects
//		Set<String> resultList = new HashSet<String>();
//		Set<String> redirects = page
//				.getRedirects(pageRedirectStmt, errorWriter);
//		SemanticClass semClass = wikiPageBean.getSemClassMap().get(
//				IndexingConstants.SEM_CLASS_SYNONYM);
//		Iterator<String> redirectsIterator = redirects.iterator();
//		while (redirectsIterator.hasNext()) {
//			final String redirect = redirectsIterator.next().replaceAll("_", " ");
//			semClass.getWords().append(redirect + " | ");
//			if(redirect.trim().length() >= 2) {
//				resultList.add(redirect);
//			}
//		}
//
//		// extract from first paragraph
//		int typePos = ExtractionUtil.getInstance()
//				.getPosAfterPatternSynonym(text);
//		//System.out.println("Synonym pos " + typePos);
//		if (typePos >= 0) {
//			String syn = getIdentifiedWord(text, typePos, stemmer);
//			if (syn != null)
//				semClass.getWords().append(syn + " | ");
//		}
//
//		//		semClass = wikiPageBean.getSemClassMap().get(
//		//				IndexingConstants.SEM_CLASS_SYNONYM_WORDNET);
//		//		// synonyms
//		//		StringBuilder builder = new StringBuilder();
//		//		final Set<String> temp = ExtractionUtil.getInstance().getSynonyms(
//		//				wikiPageBean.getPageTitle(), builder);
//		//		if(temp != null) {
//		//			resultList.addAll(temp);
//		//			semClass.getWords().append(builder+ " | ");
//		//		}
//		return resultList;
//	}
//
//	private void setWordnetSynonymWords(WikiPageBean wikiPageBean, PorterStemmer stemmer){
//		SemanticClass semClass = wikiPageBean.getSemClassMap().get(
//				IndexingConstants.SEM_CLASS_SYNONYM_WORDNET);
//		//appending synopsis class words
//		StringBuilder sb=wikiPageBean.getSemClassMap().get(
//				IndexingConstants.SEM_CLASS_SYNOPSIS).getWords();
//		sb.append(wikiPageBean.getSemClassMap().get(
//				IndexingConstants.SEM_CLASS_FREQUENT).getWords());
//		sb.append(wikiPageBean.getSemClassMap().get(
//				IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK).getWords());
//		sb.append(wikiPageBean.getSemClassMap().get(
//				IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED).getWords());
//		// TODO append section name
//		//		sb.append(wikiPageBean.getSemClassMap().get(
//		//				IndexingConstants.SECTION_HEADING).getWords());
//
//		// words in synonyms
//		StringBuilder builder = new StringBuilder();
//		ExtractionUtil extUtil= ExtractionUtil.getInstance();
//		final Set<String> temp = extUtil.getSynonyms(
//				wikiPageBean.getPageTitle(), builder);
//
//		if(temp != null) {
//			Iterator<String> tempIter= temp.iterator();
//			while(tempIter.hasNext())
//			{
//				String synWord=tempIter.next();
//				if(sb.indexOf(extUtil.getRootForm(synWord,stemmer)) >= 0){
//					semClass.getWords().append(synWord + " | ");
//				}
//
//			}
//		}
//	}
//
//	private void setWordnetMeronymWords(WikiPageBean wikiPageBean, PorterStemmer stemmer, List<Section> sections,  JwplPage page){
//		SemanticClass semClass = wikiPageBean.getSemClassMap().get(
//				IndexingConstants.SEM_CLASS_MERONYM);
//		//appending synopsis class words
//		StringBuilder sb=wikiPageBean.getSemClassMap().get(
//				IndexingConstants.SEM_CLASS_SYNOPSIS).getWords();
//		sb.append(wikiPageBean.getSemClassMap().get(
//				IndexingConstants.SEM_CLASS_FREQUENT).getWords());
//		sb.append(wikiPageBean.getSemClassMap().get(
//				IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK).getWords());
//		sb.append(wikiPageBean.getSemClassMap().get(
//				IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED).getWords());
//		sb.append(wikiPageBean.getSemClassMap().get(
//				IndexingConstants.SECTION_HEADING).getWords());
//
//		// words in synonyms
//		//StringBuilder builder = new StringBuilder();
//		WordnetUtils wordnetUtil= WordnetUtils.getInstance("/data2/indexing/semantic/");
//		ExtractionUtil extUtil= ExtractionUtil.getInstance();
//		final Set<String> temp = wordnetUtil.getMeronyms(
//				wikiPageBean.getPageTitle());
//
//		if(temp != null) {
//			Iterator<String> tempIter= temp.iterator();
//			while(tempIter.hasNext())
//			{
//				String synWord=tempIter.next();
//				if(sb.indexOf(extUtil.getRootForm(synWord,stemmer)) >= 0){
//					semClass.getWords().append(synWord + " | ");
//				}
//
//			}
//		}
//
//		if(sections!=null){
//			List<WikiSection> wikiSections=wikiPageBean.getSections();
//			Iterator<WikiSection> wikiSecIter = wikiSections.iterator();
//			while(wikiSecIter.hasNext()){
//				WikiSection wikiSec=wikiSecIter.next();
//				String secName = wikiSec.getSectionName();
//				if(secName.indexOf("components") > 0 || secName.indexOf("parts") > 0
//						|| secName.indexOf("modules") > 0){
//					if(secName.indexOf("/")>=0){
//						semClass.getWords().append(secName.replaceFirst("/", "||").split("||")[1].replaceAll("/", "|"));
//					}
//				}
//			}
//		}
//
//		//		if(sections !=null){
//		//			String secTitle = null;
//		//			int index = -1;
//		//			for(int i=0;i<sections.size();i++){
//		//				Section curSection=sections.get(i);
//		//				secTitle= curSection.getTitle();
//		//				if(secTitle.indexOf("components") > 0 || secTitle.indexOf("parts") > 0
//		//						|| secTitle.indexOf("modules") > 0)
//		//					if(curSection instanceof SectionContainer){
//		//						SectionContainer sectioncontainer = (SectionContainer) curSection;
//		//						List<Section> subsections = sectioncontainer.getSubSections();
//		//						Iterator<Section> subsectionsIter=subsections.iterator();
//		//						while(subsectionsIter.hasNext()){
//		//							Section curSubSection = subsectionsIter.next();
//		//							String subSecTitle = curSubSection.getTitle();
//		//							semClass.getWords().append(subSecTitle + " | ");
//		//						}
//		//					}
//		//			}
//		//		}
//	}
//
//	private void setPageType(WikiPageBean wikiPageBean, String firstPara, PorterStemmer stemmer,
//			List<Link> firstSecLinkList) {
//		int typePos = ExtractionUtil.getInstance().getPosAfterPattern(
//				firstPara);
//		//System.out.println("start of Hypernym " + typePos);
//		if (typePos >= 0) {
//			List<String> pageType = getIdentifiedWord(firstPara, typePos, stemmer, firstSecLinkList);
//			//System.out.println("Type of page : " + pageType);
//			if (pageType != null)
//				wikiPageBean.getPageTypes().addAll(pageType);
//		}
//	}
//
//
//
//	private String getIdentifiedWord(String firstPara, int startingPos, PorterStemmer stemmer) {
//
//		// hypernym or synonym exists for a page it will be mentioned in first sentence
//		// TODO add this when the data is clean 
//		//		if(firstPara.indexOf(".") < startingPos) {
//		//			return null;
//		//		}
//
//		firstPara = firstPara.substring(startingPos);
//		String[] splits = firstPara.split(" ");
//		StopwordRecognizer stopWordRecog = StopwordRecognizer.getInstance();
//		StringBuilder builder = new StringBuilder();
//		for (int i = 0; i < splits.length; i++) {
//			if(splits[i].endsWith(",")) {
//				splits[i] = splits[i].replace(",", "");
//				builder.append(splits[i] + " ");
//				break;
//			}
//			// stop word or full stop or verb
//			if (stopWordRecog.isStopWordForType(splits[i]) 
//					|| splits[i].indexOf(".") >= 0
//					|| intWordRecog.isVerb(splits[i], stemmer)) {
//				break;
//			}
//			builder.append(splits[i] + " ");
//		}
//		if (builder.length() > 2) {
//			return builder.toString();
//		}
//		return null;
//	}
//
//	private List<String> getIdentifiedWord(String firstPara, int startingPos, 
//			PorterStemmer stemmer, List<Link> firstSecLinkList) {
//
//		// hypernym or synonym exists for a page it will be mentioned in first sentence
//
//		firstPara = firstPara.substring(startingPos);
//		String[] splits = firstPara.split(" ");
//		StopwordRecognizer stopWordRecog = StopwordRecognizer.getInstance();
//		List<String> pageTypeList = new ArrayList<String>();
//		StringBuilder builder = new StringBuilder();
//		for (int i = 0; i < splits.length; i++) {
//
//			// associated with more than one type
//			if(splits[i].endsWith(",")) {
//				splits[i] = splits[i].replace(",", "");
//				pageTypeList.add(builder.toString()+" " + splits[i]);
//				builder = new StringBuilder();
//			}
//
//			if(splits[i].equals("and")) {
//				continue;
//			}
//
//			// stop word or full stop or verb
//			if (stopWordRecog.isStopWordForType(splits[i]) 
//					|| splits[i].indexOf(".") >= 0
//					|| intWordRecog.isVerb(splits[i], stemmer)) {
//				break;
//			}
//			builder.append(splits[i] + " ");
//		}
//		pageTypeList.add(builder.toString());
//
//		int size = pageTypeList.size();
//		List<String> pageTypeNewList = new ArrayList<String>();
//		if (size > 0) {
//
//			for(int i=0;i<size;i++) {
//
//				final String pageType = pageTypeList.get(i);
//
//				for(int j=0,linkSize=firstSecLinkList.size();j<linkSize;j++) {
//					Link link = firstSecLinkList.get(j);
//					try {
//						final String plainTitle = new Title(link.getTarget()).getPlainTitle();
//
//						if(pageType.equals(plainTitle) 
//								|| pageType.startsWith(plainTitle)
//								|| pageType.endsWith(plainTitle)
//								|| plainTitle.startsWith(pageType)
//								|| plainTitle.endsWith(pageType)
//								)  {
//							pageTypeNewList.add(plainTitle);
//							break;
//						} else {
//							pageTypeNewList.add(pageType);
//							break;
//						}
//					} catch (Exception e) {
//						pageTypeNewList.add(pageType);
//					}
//				}
//
//			}
//			return pageTypeNewList;
//		}
//		return null;
//	}
//
//
//	private void setRelated(Section section, WikiPageBean wikiPageBean,
//			Map<String,AssociationOverlap> assMap, BufferedWriter errorWriter) {
//		List<Link> links = section.getLinks(Link.type.INTERNAL);
//
//		if (links != null) {
//			Link link = null;
//			Map<String, SemanticClass> semClassMap = wikiPageBean
//					.getSemClassMap();
//			SemanticClass semClass = semClassMap.get(IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED);
//			SemanticClass semClassParent = semClassMap.get(IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK_PARENT);
//			semClassParent.getWords().append("/ " + "related" +" / ");
//			for (int i = 0, size = links.size(); i < size; i++) {
//				link = links.get(i);
//				if (semClass != null) {
//					try {
//						final String target = link.getTarget();
//						try {
//							final String assLinkTitle = new Title(target).getPlainTitle();
//							AssociationOverlap assOverlap = assMap.get(assLinkTitle);
//							if(assOverlap != null) {
//								semClass.getWords().append(assLinkTitle + " |" +assOverlap.getLinkId()+"/"+ assOverlap.getLinkOverlapCount() +" ");
//								if(assOverlap.getLinkParentOverlap() == null) 
//									semClass.getWords().append("NA | ");
//								else 
//									semClass.getWords().append(assOverlap.getLinkParentOverlap() +" |"+assOverlap.getLinkCatOverlapId()+" ");
//							} else {
//								semClass.getWords().append(assLinkTitle + " |NA/0 ");
//								try {
//									//errorWriter.write("warning : Hyperlink page name mismatch in DB and parser for "+ wikiPageBean.getPageId()+" link " +assLinkTitle+"\n");
//								} catch (Exception exp) {
//									// ignore
//								}
//							}
//
//							//System.out.println("link title " + new Title(target).getPlainTitle());
//						} catch (Exception e) {
//
//							final String assLinkTitle = target.replaceAll("_", " ").replaceAll("#", " ");
//							if(assLinkTitle != null) {
//								AssociationOverlap assOverlap = assMap.get(assLinkTitle);
//								if(assOverlap != null) {
//									semClass.getWords().append(assLinkTitle + " |" +assOverlap.getLinkId()+"/"+ assOverlap.getLinkOverlapCount() +" ");
//									if(assOverlap.getLinkParentOverlap() == null) 
//										semClass.getWords().append("NA | ");
//									else 
//										semClass.getWords().append(assOverlap.getLinkParentOverlap() +" |"+assOverlap.getLinkCatOverlapId()+" ");
//								}
//								else {
//									semClass.getWords().append(assLinkTitle + " |NA/0 ");
//									try {
//										//errorWriter.write("warning : Hyperlink page name mismatch in DB and parser for "+ wikiPageBean.getPageId()+" link " +assLinkTitle+"\n");
//									} catch (Exception exp) {
//										// ignore
//									}
//								}
//							}
//						}
//
//					} catch (Exception e) {
//						try {
//							errorWriter.write("Accessing link id : "
//									+ wikiPageBean.getPageId() + " failed : "
//									+ e.getMessage() + " " + e.getCause()
//									+ "\n");
//							e.printStackTrace();
//						} catch (Exception exp) {
//							exp.printStackTrace();
//						}
//					}
//
//				}
//			}
//		}
//	}
//
//	private String setExternal(Section section, WikiPageBean wikiPageBean,
//			BufferedWriter errorWriter) {
//		List<Link> links = section.getLinks(Link.type.EXTERNAL);
//		StringBuilder externString = new StringBuilder();
//		TagText tt= TagText.getInstance(null);
//		if (links != null) {
//			Link link = null;
//			Map<String, SemanticClass> semClassMap = wikiPageBean
//					.getSemClassMap();
//			SemanticClass semClass = semClassMap.get(IndexingConstants.SEM_CLASS_REFERENCE);
//			String title=wikiPageBean.getPageTitle().replaceAll("_"," ");
//			String titleWords[] = title.split(" ");
//			for (int i = 0, size = links.size(); i < size; i++) {
//				link = links.get(i);
//				String tagged;
//				if(link.toString().replaceAll("_"," ").indexOf(title)!=-1){
//					//System.out.println(link.toString().replaceAll("_"," ").indexOf(title));
//
//					tagged=tt.tagSentence(link.getText());	
//					String[] splits = tagged.split(" ");
//					
//					String[] tags= new String[splits.length/2], words = new String[splits.length/2];
//					
//					for(int m=0;m<splits.length;m++) {
//					String[] wordPOS = splits[m].split("/");
//					for(int k=0,j=0;k<splits.length;k=k+2,j++){
//						if(wordPOS.length <= 1) {
//							words[j]="NN";
//							tags[j]="KK";
//						} else {
//						words[j] = wordPOS[0];
//						tags[j]= wordPOS[1];
//						}
//					}
//					}
//
//					int spaces=0;
//					StringBuilder interpretation=new StringBuilder();
//					for(int k=0,start=link.toString().replaceAll("_"," ").indexOf(title);k<start;k++){
//						if(link.toString().charAt(k)==' ') spaces++;
//					}
//					for(int k=spaces-1;k>0;k--)
//					{
//						if(tags[k].equals("JJ")){
//							interpretation.append(words[k]+" ");
//						}
//						else break;
//					}
//					interpretation.append(title+" ");
//					for(int k=spaces+titleWords.length;k<tags.length;k++)
//					{
//						if(tags[k].equals("NN")||tags[k].equals("NNS")||tags[k].equals("JJ")){
//							interpretation.append(words[k]+" ");
//
//						}
//						else break;
//					}
//
//					//System.out.println("Interpretation:"+interpretation);
//					externString.append(interpretation.toString().trim()+"|");
//
//				}
//
//				else{
//					boolean isTitleWordinLink = false;
//					String titleWord="";
//					String stopWords = "of by in on based the from about";
//					for(String word: titleWords){
//						if(stopWords.indexOf(word)!=-1) continue;
//						if(link.toString().indexOf(word)!=-1){
//							isTitleWordinLink = true;
//							titleWord=word;
//							break;
//						}
//					}
//					if(isTitleWordinLink){
//						synchronized(tt){
//							tagged=tt.tagger.tagString(link.toString());
//						}
//
//						//System.out.println("Tagged:"+tagged);
//						String[] splits= tagged.replace(" ","/").split("/");
//						if(splits.length <=1) continue;
//						String[] tags = new String[splits.length/2];
//						String[] words = new String[splits.length/2];
//						for(int k=0,j=0;k<splits.length;k=k+2,j++){
//							words[j]=splits[k];
//							tags[j]=splits[k+1];
//						}
//
//						int spaces=0;
//						StringBuilder interpretation=new StringBuilder();
//						for(int k=0,start=link.toString().replaceAll("_"," ").indexOf(titleWord);k<start;k++){
//							if(link.toString().charAt(k)==' ') spaces++;
//						}
//						for(int k=spaces-1;k>0;k--)
//						{
//							if(tags[k].equals("JJ")){
//								interpretation.append(tags[k]+" ");
//							}
//							else break;
//						}
//
//						interpretation.append(titleWord+" ");
//						for(int k=spaces+1;k<tags.length;k++)
//						{
//							if(title.indexOf(words[k])!=-1) {
//								interpretation.append(words[k]+" ");
//								continue;
//							}
//							if(tags[k].indexOf("NN")>=0||tags[k].equals("JJ")){
//								interpretation.append(words[k]+" ");
//
//							}
//							else break;
//						}
//
//						//System.out.println("Interpretation:"+interpretation);
//						externString.append(interpretation.toString().trim()+"|");
//					}
//				}			
//				if (semClass != null) {
//					try {
//						semClass.getWords().append(link.getText() + " | ");
//					} catch (Exception e) {
//						try {
//							errorWriter.write("Accessing link id : "
//									+ wikiPageBean.getPageId() + " failed : "
//									+ e.getMessage() + " " + e.getCause()
//									+ "\n");
//							e.printStackTrace();
//						} catch (Exception exp) {
//							exp.printStackTrace();
//						}
//					}
//
//				}
//			}
//		}
//		return externString.toString();
//
//	}
//
//	private String getAllTitleRelatedWords(String title) {
//		String[] titleRelatedWords = title.split(" ");
//		String toCompare = "";
//		for(int j=0;j<titleRelatedWords.length;j++) {
//		for(int i=0;i<IndexingConstants.TITLE_STOP_WORDS.length;i++) {
//			if(titleRelatedWords[j].indexOf(IndexingConstants.TITLE_STOP_WORDS[i]) < 0) {
//				toCompare = toCompare+" " + titleRelatedWords[j].replaceAll(IndexingConstants.TITLE_STOP_WORDS[i], " ");
//			}
//		}
//		}
//		return toCompare;
//	}
//
//	private void setSectionDetails(Section section, WikiPageBean wikiPageBean,
//			Map<String,AssociationOverlap> assMap, String titleWordsToCompare, 
//			BufferedWriter errorWriter) {
//		WikiSection wikiSection = new WikiSection();
//		final Map<String, SemanticClass> semClassMap = wikiPageBean
//				.getSemClassMap();
//
//
//		// Abstract does not have any heading
//		String secTitle = section.getTitle();
//		if(section.getTitle() == null) {
//			wikiSection.setSectionName("abstract");
//		} else {
//			secTitle = secTitle.toLowerCase();
//			// don't do it for reference or see also
//			if (((secTitle.indexOf("see") >= 0)
//					&& (secTitle.indexOf("also")) >= 0)
//					||(secTitle.indexOf("reference") >= 0)
//					|| ((secTitle.indexOf("external") >= 0) && (secTitle
//							.indexOf("link") >= 0))
//							|| (secTitle.indexOf("note") >= 0)
//							|| ((secTitle.indexOf("further") >= 0) && (secTitle
//									.indexOf("reading") >= 0))) {
//				return;
//			}
//
//			if(section instanceof SectionContainer){
//				SectionContainer sectioncontainer = (SectionContainer) section;
//				List<Section> subsections = sectioncontainer.getSubSections();
//				Iterator<Section> subsectionsIter=subsections.iterator();
//				while(subsectionsIter.hasNext()){
//					String subSecTitle = subsectionsIter.next().getTitle();
//					if(subSecTitle != null && !"".equals(subSecTitle.trim())) {
//						secTitle = secTitle +" / " + subSecTitle;
//					}
//				}
//			} 
//			wikiSection.setSectionName(secTitle);
//		}
//
//		final SemanticClass semClassAssLink = semClassMap.get(IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK);
//		semClassAssLink.getWords().append("/ " + wikiSection.getSectionName() +" / ");
//		final SemanticClass semClassAssLinkParent = semClassMap.get(IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK_PARENT);
//		semClassAssLinkParent.getWords().append("/ " + wikiSection.getSectionName() +" / ");
//		final SemanticClass semClassCooccurrence = semClassMap.get(IndexingConstants.SEM_CLASS_COOCCURRENCE);
//		semClassCooccurrence.getWords().append("/ " + wikiSection.getSectionName() +" / ");
//		final SemanticClass semClassFrequent = semClassMap.get(IndexingConstants.SEM_CLASS_FREQUENT);
//		semClassFrequent.getWords().append("/ " + wikiSection.getSectionName() +" / ");
//
//		// Set association and parent
//		List<Link> links = section.getLinks(Link.type.INTERNAL);
//		String imgTextSection =null;
//		if (links != null) {
//			//WikiLink wikiLink = null;
//			for (int i = 0, size = links.size(); i < size; i++) {
//				final Link link = links.get(i);
//				if (semClassAssLink != null) {
//					// Link text is very noisy, don't store
//
//					try {
//						// Points to images
//						final String target = link.getTarget();
//						if(target.indexOf("File:") >= 0 || target.indexOf("Image:") >= 0) {
//							String imgText = link.getText();
//							imgText = imgText.substring(imgText.lastIndexOf('|')+1, imgText.length());
//							//System.out.println("Imge text " + imgText);
//							if(imgText != null && !"".equals(imgText.trim())) {
//								imgTextSection = imgTextSection + imgText +" | ";
//							}
//							//TODO add even link present there
//							continue;
//						}
//						try {
//							final String assLinkTitle = new Title(target).getPlainTitle();
//							AssociationOverlap assOverlap = assMap.get(assLinkTitle);
//							if(assOverlap != null) {
//								semClassAssLink.getWords().append(assLinkTitle + " |" +assOverlap.getLinkId()+"/"+ assOverlap.getLinkOverlapCount() +" ");
//								if(assOverlap.getLinkParentOverlap() == null) 
//									semClassAssLinkParent.getWords().append("NA | ");
//								else 
//									semClassAssLinkParent.getWords().append(assOverlap.getLinkParentOverlap() +" |"+assOverlap.getLinkCatOverlapId()+" ");
//							} else {
//								semClassAssLink.getWords().append(assLinkTitle + " |NA/0 ");
//								try {
//									//errorWriter.write("warning : Hyperlink page name mismatch in DB and parser for "+ wikiPageBean.getPageId()+" link " +assLinkTitle+"\n");
//								} catch (Exception exp) {
//									// ignore
//								}
//							}
//							//System.out.println("link title " + new Title(target).getPlainTitle());
//						} catch (Exception e) {
//
//							final String assLinkTitle = target.replaceAll("_", " ").replaceAll("#", " ");
//							AssociationOverlap assOverlap = assMap.get(assLinkTitle);
//							if(assOverlap != null) {
//								semClassAssLink.getWords().append(assLinkTitle + " |" +assOverlap.getLinkId()+"/"+ assOverlap.getLinkOverlapCount() +" ");
//								semClassAssLinkParent.getWords().append(assOverlap.getLinkParentOverlap() +" |"+assOverlap.getLinkCatOverlapId()+" ");
//								if(assOverlap.getLinkParentOverlap() == null) 
//									semClassAssLinkParent.getWords().append("NA | ");
//								else 
//									semClassAssLinkParent.getWords().append(assOverlap.getLinkParentOverlap() +" |"+assOverlap.getLinkCatOverlapId()+" ");
//							}
//							else {
//								semClassAssLink.getWords().append(assLinkTitle + " |NA/0 ");
//								try {
//									//errorWriter.write("warning : Hyperlink page name mismatch in DB and parser for "+ wikiPageBean.getPageId()+" link " +assLinkTitle+"\n");
//								} catch (Exception exp) {
//									// ignore
//								}
//							}
//						}
//					} catch (Exception e) {
//						try {
//							errorWriter.write("Accessing internal link id : "
//									+ wikiPageBean.getPageId() + " failed : "
//									+ e.getMessage() + " " + e.getCause()
//									+ "\n");
//							e.printStackTrace();
//						} catch (Exception exp) {
//							exp.printStackTrace();
//						}
//					}
//
//				}
//			}
//		}
//		if(imgTextSection != null) {
//			wikiSection.setImageText("/ " + wikiSection.getSectionName() +" / "+imgTextSection +" ");
//		}
//		wikiPageBean.getSections().add(wikiSection);
//
//		// set frequent phrases and co-occurrence
//		String[] lines=section.getText().split("\\.");
//		final List<String> phrases=new ArrayList<String>();
//		for(int j=0;j<lines.length;j++){
//
//			//String[] words=lines[j].split(" ");
//			final String[] taggedWords =TagText.getInstance(null).tagSentence(lines[j].replaceAll("[^a-zA-Z ]+"," ")).split(" ");
//
//
//			//demoAPI(lp,lines[j]);
//
//			for(int i=0;i<taggedWords.length;i++){
//				//System.out.println("tagged i in cooccur : " + taggedWords[i] + i+", "+ taggedWords.length);
//				String[] splits = taggedWords[i].split("/");
//				if(splits.length <= 1) continue;
//				final String currentWord = splits[0].toLowerCase().trim();
//				final String currentPOS = splits[1];
//
//				if(currentPOS.indexOf("VB") >= 0 ||currentPOS.indexOf("JJ") >= 0
//						||currentPOS.indexOf("NN") >= 0){
//					String phrase=currentWord;
//					int h=i;
//					phrases.add(phrase);
//					while(++h<taggedWords.length && taggedWords[h].split("/")[1].indexOf("NN") >=0){
//						phrase=phrase+" "+taggedWords[h].split("/")[0].trim();
//						phrases.add(phrase);
//					}
//				}
//
//				// Co-occurrence starts here
//				if(titleWordsToCompare.indexOf(currentWord) >= 0){
//					String cooccurence = null;
//					if(i!=0&&(taggedWords[i-1].split("/")[1].indexOf("JJ")>=0
//							||taggedWords[i-1].split("/")[1].indexOf("NN") >=0 )){
//						cooccurence = taggedWords[i-1].split("/")[0]+" "
//								+currentWord;
//						int h=i-1;
//						while(h>=1 && taggedWords[--h].split("/")[1].indexOf("JJ")>=0){
//							cooccurence= taggedWords[h].split("/")[0]+" "+ cooccurence;
//						}
//						if(taggedWords[i-1].split("/")[1].indexOf("NN") >=0) {
//							h=i-1;
//							while(h>=1 && taggedWords[--h].split("/")[1].indexOf("NN")>=0){
//								cooccurence=taggedWords[h].split("/")[0]+" "+cooccurence;
//							}
//						}
//					}
//					if((i+1)<=taggedWords.length-1 && taggedWords[i+1].split("/")[1].indexOf("NN")>=0){
//						if(cooccurence != null) {
//							cooccurence = cooccurence+" "+taggedWords[i+1].split("/")[0];
//						} else {
//							cooccurence = currentWord +" "+taggedWords[i+1].split("/")[0];
//						}
//						int h=i+1;
//						//System.out.println(sfix);
//						while(++h<taggedWords.length && taggedWords[h].split("/")[1].indexOf("NN")>=0){
//							cooccurence=cooccurence+" "+taggedWords[h].split("/")[0];
//						}
//					}
//					if(cooccurence != null) {
//						semClassCooccurrence.getWords().append(cooccurence +" | ");
//					}
//				}
//
//			}
//		}
//
//		// Add phrase
//		final List<String> tempPhraseList = new ArrayList<String>();
//		for(int m=0,phraseSize=phrases.size();m<phraseSize;m++){
//			final String phrase = phrases.get(m);
//			int cnt=Collections.frequency(phrases, phrase);
//			final List<Integer> toRemoveList = new ArrayList<Integer>();
//			boolean canAdd = true;
//			if(cnt>=2) {
//				for(int n=0,tempSize=tempPhraseList.size();n<tempSize;n++){
//					final String temPhrase = tempPhraseList.get(n);
//					// Dont add substring
//					if(temPhrase.length() != phrase.length()) {
//						if(phrase.indexOf(temPhrase) >=0) {
//							toRemoveList.add(n);
//						}
//						if(!canAdd && temPhrase.indexOf(phrase) >=0) {
//							canAdd = false;
//						}
//					}
//				}
//				if(toRemoveList.size()>0) {
//					for(int n=0,toRmSize=toRemoveList.size();n<toRmSize;n++){
//						tempPhraseList.remove(toRemoveList.get(n));
//					}
//				} else {
//					if(canAdd)
//						tempPhraseList.add(phrase);
//				}
//			}
//		}
//
//		for(int m=0,phraseSize=tempPhraseList.size();m<phraseSize;m++){
//			semClassFrequent.getWords().append(tempPhraseList.get(m)+" |"+Collections.frequency(phrases,
//					tempPhraseList.get(m))+" ");
//		}
//	}
//
//	private JwplPage getPage(int pageId, BufferedWriter errorWriter) {
//		JwplPage page = null;
//		try {
//			page = PageDao.getInstance().getJwplPageDetails(pageId,
//					pageCategoryStmt, conn, errorWriter);
//			// page = wiki.getPage(pageId);
//			//System.out.println("Page at getPage : " + page);
//		} catch (Exception e) {
//			try {
//				errorWriter.write("Accessing page with id : " + pageId
//						+ " failed : " + e.getMessage() + " " + e.getCause()
//						+ "\n");
//				e.printStackTrace();
//			} catch (Exception exp) {
//				exp.printStackTrace();
//			}
//		}
//		return page;
//	}
//
//	private void getNoOfInLinks(int pageId,WikiPageBean wikiPageBean, BufferedWriter errorWriter){
//		wikiPageBean.setNoOfInlinks(PageDao.getInstance().getNoOfInLinks(pageId, inLinkStmt, errorWriter));
//	}
//
//	private void getNoOfOutLinks(int pageId,WikiPageBean wikiPageBean,BufferedWriter errorWriter){
//		wikiPageBean.setNoOfOutlinks(PageDao.getInstance().getNoOfInLinks(pageId, outLinkStmt, errorWriter));
//	}
//
//
//
//	/*public static void main(String[] args) throws Exception {
//		WikiPageExtractor wikiPageExtract = new WikiPageExtractor();
//		// wikiPageExtract.getCleanText();
//		// Page page = wikiPageExtract.getPage("Car_(disambiguation)");
//		// System.out.println(page.getPageId());
//
//		BufferedWriter errorWriter;
//		try {
//			errorWriter = new BufferedWriter(new OutputStreamWriter(
//					new FileOutputStream("E1908150rrorFile.log"), "UTF8"));
//
//			WikiPageBean pageBean = new WikiPageBean();
//			pageBean.setPageId(1908150); //198584
//			wikiPageExtract.initWikiPageDetails(pageBean, errorWriter);
//			//System.out.println(wikiPageExtract.getNoOfInLinks(198584, errorWriter));
//			//System.out.println(wikiPageExtract.getNoOfOutLinks(198584, errorWriter));
//			//			System.out.println("Title " + pageBean.getPageTitle());
//			//			System.out.println(pageBean.getPageId());
//			//			System.out.println(pageBean.getAbstractText());
//			//			System.out.println(pageBean.getTitleDisambText());
//			//
//			//			List<WikiSection> wikiSections = pageBean.getSections();
//			//			WikiSection section = null;
//			//			List<WikiLink> wikiLinks = null;
//			//			WikiLink wikiLink = null;
//			//			System.out.println("Section details ");
//			//			for (int i = 0, size = wikiSections.size(); i < size; i++) {
//			//				section = wikiSections.get(i);
//			//				System.out.println(section.getSectionName());
//			//				wikiLinks = section.getInternalLinks();
//			//
//			//				if (wikiLinks != null) {
//			//					for (int j = 0, linkSize = wikiLinks.size(); j < linkSize; j++) {
//			//						wikiLink = wikiLinks.get(j);
//			//						//System.out.println(wikiLink.getLinkText());
//			////						System.out.println("L : " +wikiLink.getLeftContext());
//			////						System.out.println("R : " +wikiLink.getRightContext());
//			//					}
//			//				}
//			//
//			//			}
//			//
//			//			System.out.println("Semantic classes ..........");
//			//
//			//			Map<String, SemanticClass> semClassMap = pageBean.getSemClassMap();
//			//			System.out.println(IndexingConstants.SEM_CLASS_SYNONYM+" "+semClassMap.get(
//			//					IndexingConstants.SEM_CLASS_SYNONYM).getWords().toString());
//			//			
//			//			System.out.println(IndexingConstants.SEM_CLASS_SYNONYM_WORDNET+" "+semClassMap.get(
//			//					IndexingConstants.SEM_CLASS_SYNONYM_WORDNET).getWords().toString());
//			//			
//			//			System.out.println(IndexingConstants.SEM_CLASS_HOMONYM+" "+semClassMap.get(
//			//					IndexingConstants.SEM_CLASS_HOMONYM).getWords().toString());
//			//			System.out.println(IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK+" "+semClassMap.get(
//			//					IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK).getWords()
//			//					.toString());
//			//			System.out.println(IndexingConstants.SEM_CLASS_REFERENCE+" "+semClassMap.get(
//			//					IndexingConstants.SEM_CLASS_REFERENCE).getWords()
//			//					.toString());
//			//			System.out.println(IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED+" "+semClassMap.get(
//			//					IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED).getWords()
//			//					.toString());
//			//			System.out.println(IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED_HIERARCHY+" "+semClassMap.get(
//			//					IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED_HIERARCHY)
//			//					.getWords().toString());
//			//			System.out
//			//					.println(IndexingConstants.SEM_CLASS_FREQUENT+" "+semClassMap.get(
//			//							IndexingConstants.SEM_CLASS_FREQUENT).getWords()
//			//							.toString());
//			//			System.out
//			//					.println(IndexingConstants.SEM_CLASS_HYPERNYM+" "+semClassMap.get(
//			//							IndexingConstants.SEM_CLASS_HYPERNYM).getWords()
//			//							.toString());
//			//			System.out.println(IndexingConstants.SEM_CLASS_HYPONYM+" "+semClassMap.get(
//			//					IndexingConstants.SEM_CLASS_HYPONYM).getWords().toString());
//			//			System.out.println(IndexingConstants.SEM_CLASS_ROLE+" "
//			//					+semClassMap.get(IndexingConstants.SEM_CLASS_ROLE).getWords()
//			//					.toString());
//			//			System.out.println(IndexingConstants.SEM_CLASS_MERONYM+" "+semClassMap.get(
//			//					IndexingConstants.SEM_CLASS_MERONYM).getWords().toString());
//			//			System.out.println(IndexingConstants.SEM_CLASS_SIBLING+" "+semClassMap.get(
//			//					IndexingConstants.SEM_CLASS_SIBLING).getWords().toString());
//
//			wikiPageExtract.closeConnection();
//
//			errorWriter.flush();
//			errorWriter.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}*/
//
//	public static void main(String[] args) {
//		BufferedWriter errorWriter=null;
//		WikiPageExtractorThread wikiPageExtract = new WikiPageExtractorThread("Wikipedia","narayan",errorWriter);
//
//		try{
//			wikiPageExtract.getCleanText();
//			errorWriter = new BufferedWriter(new OutputStreamWriter(
//					new FileOutputStream("ErrorFile.log"), "UTF8"));
//			WikiPageBean pageBean = new WikiPageBean();
//			JwplPage page = wikiPageExtract.getPage( 7458401,errorWriter);
//			//System.out.println("page title:"+page.getTitle());
//			//System.out.println("From here:"+page.getParsedPage().getText()+"\n Till Here");
//			List<Section> wikiSections = page.getParsedPage().getSections();
//			Section section = null;
//			List<Link> wikiLinks = null;
//			Link wikiLink = null;
//			//						System.out.println("Section details"+wikiSections.size());
//			for (int i = 0, size = wikiSections.size(); i < size; i++) {
//				section = wikiSections.get(i);
//				//System.out.println("SecTitle:"+section.getTitle());
//				wikiLinks = section.getLinks();
//
//				if (wikiLinks != null) {
//					for (int j = 0, linkSize = wikiLinks.size(); j < linkSize; j++) {
//						wikiLink = wikiLinks.get(j);
//						//System.out.println(wikiLink.getText());
//						//						System.out.println("L : " +wikiLink.getLeftContext());
//						//						System.out.println("R : " +wikiLink.getRightContext());
//					}
//				}
//			}
//		}
//		catch (Exception e) {
//			// TODO: handle exception
//		}
//		// 
//		// System.out.println(page.getPageId());
//
//	}
//
}
