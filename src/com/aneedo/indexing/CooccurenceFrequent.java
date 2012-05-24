package com.aneedo.indexing;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aneedo.indexing.bean.SemanticClass;
import com.aneedo.indexing.bean.WikiLink;
import com.aneedo.indexing.bean.WikiPageBean;
import com.aneedo.indexing.bean.WikiSection;
import com.aneedo.jwplext.JwplCategory;
import com.aneedo.jwplext.JwplPage;
import com.aneedo.jwplext.JwplSQLConstants;
import com.aneedo.jwplext.dao.DBConnection;
import com.aneedo.jwplext.dao.PageCategoryDao;
import com.aneedo.jwplext.dao.PageDao;
import com.aneedo.search.util.TagText;
import com.aneedo.util.ExtractionUtil;
import com.aneedo.util.InterestingWordRecognizer;
import com.aneedo.util.PorterStemmer;
import com.aneedo.util.StopwordRecognizer;
import com.aneedo.util.TermFreqBasedTrimmer;
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
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.POS;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
public class CooccurenceFrequent {
//	List taggedWords;
//	private PreparedStatement pageCategoryStmt = null;
//	// private PreparedStatement childCategoryStmt = null;
//	// private PreparedStatement articleOfCategoryStmt = null;
//	Connection conn = null;
//	private IDictionary dictionary;
//	private PreparedStatement pageRedirectStmt = null;
//	private PreparedStatement pageDisambStmt = null;
//	private PreparedStatement inLinkStmt=null;
//	private PreparedStatement outLinkStmt=null;
//	private InterestingWordRecognizer intWordRecog = new InterestingWordRecognizer(null);
//	String plainTitle;
//	// TODO plug in connection pooling
//	public CooccurenceFrequent() {
//		try {
//			if (conn == null) {
//				conn = DBConnection.getInstance().getConnection();
//				plainTitle="";
//				initStatements();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	private void closeConnection() {
//		if (conn != null) {
//			try {
//				conn.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
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
//		.getContentList();
//		for (int i = 0; i < pageContent.size(); i++) {
//			//System.out.println(pageContent.get(i).getText());
//
//		}
//	}
//
//	public void setNewConnection(BufferedWriter errorWriter, int pageId)
//	throws Exception {
//		Connection con = DBConnection.getInstance().getConnection();
//		closeConnection(errorWriter, pageId);
//		conn = con;
//		initStatements();
//	}
//
//	public void closeConnection(BufferedWriter errorWriter, int pageId) {
//		try {
//			closeStatements(errorWriter, pageId);
//			DBConnection.getInstance().close(conn);
//		} catch (Exception e) {
//			conn = null;
//			try {
//				errorWriter.write("connection closing falied at page id : "
//						+ pageId + e.getMessage() + " "
//						+ e.getCause().toString() + "\n");
//				e.printStackTrace();
//			} catch (Exception exp) {
//				exp.printStackTrace();
//			}
//		}
//	}
//
//	private void initStatements() throws Exception {
//		pageCategoryStmt = conn
//		.prepareStatement(JwplSQLConstants.SELECT_PAGE_CATEGORY_DETAIL);
//		inLinkStmt=conn.prepareStatement(JwplSQLConstants.SELECT_COUNT_INLINKS);
//		outLinkStmt=conn.prepareStatement(JwplSQLConstants.SELECT_COUNT_OUTLINKS);
//		// childCategoryStmt = conn
//		// .prepareStatement(JwplConstants.SELECT_SUB_CATEGORY);
//		// articleOfCategoryStmt = conn
//		// .prepareStatement(JwplConstants.SELECT_PAGES_OF_CATEGORY);
//		pageRedirectStmt = conn.prepareStatement(JwplSQLConstants.SELECT_REDIRECT);
//		pageDisambStmt = conn.prepareStatement(JwplSQLConstants.SELECT_DISAMB);
//		try {
//			dictionary = new Dictionary(new URL("file", null,"/home/ambha/aneedo/wordnet4/dict"));
//					//DataIntegrationConstants.WORDNET_LOCATION));
//			dictionary.open();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	private void closeStatements(BufferedWriter errorWriter, int pageId) {
//
//		try {
//			pageCategoryStmt.close();
//			// childCategoryStmt.close();
//			// articleOfCategoryStmt.close();
//			inLinkStmt.close();
//			outLinkStmt.close();
//			pageRedirectStmt.close();
//			pageDisambStmt.close();
//		} catch (Exception e) {
//			pageCategoryStmt = null;
//			// childCategoryStmt = null;
//			// articleOfCategoryStmt = null;
//			inLinkStmt=null;
//			outLinkStmt=null;
//			pageRedirectStmt = null;
//			pageDisambStmt = null;
//			try {
//				errorWriter.write("Closing statements failed at id : " + pageId
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
//					.replace(")", "").replaceAll("_", " ").trim();
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
//				List<Section> sectionList = page.getParsedPage().getSections();
//				//System.out.println("Size of the section size : "
//				//		+ sectionList.size());
//
//				// abstract, frequent words, links in abstract carry a lot
//				// information
//				Section abstractSection = sectionList.get(0);
//				String freqWords = null;
//				String secondPara = "";
//				if(abstractSection.getParagraph(1) != null){
//					secondPara = abstractSection.getParagraph(1).getText();
//				}
//				//System.out.println("second para : " + secondPara);
//				String[] abstractContent = intWordRecog.getInterestingWords(abstractSection.getText());
//				//wikiPageBean.setAbstractText(abstractContent[0]);
//				wikiPageBean.setAbstractVbAdj(abstractContent[1]);
//
//				//System.out.println("Page Abstract : " + wikiPageBean.getAbstractText());
//				freqWords = TermFreqBasedTrimmer.getInstance().getFreqWords(
//						abstractSection.getText(), stemmer, plainTitle,intWordRecog);
//				//System.out.println("Frequent words : " + freqWords);
//				if (freqWords != null && freqWords.length() > 5) {
//					wikiPageBean.getSemClassMap().get(
//							IndexingConstants.SEM_CLASS_FREQUENT).getWords()
//							.append(freqWords);
//				}
//
//				// Set type/hypernym of the page from first para
//				String firstPara ="";
//				if(abstractSection.getParagraph(0) != null)
//					firstPara = abstractSection.getParagraph(0).getText();
//
//				setPageType(wikiPageBean, firstPara+secondPara, stemmer);
//
//				// set info box details
//				setDetailsFromInfoBox(page, wikiPageBean);
//
//				// Set other sections, from last three see also starts
//				// TODO sub section wise storage - needs NestedDocument lucene 3.4
//				// TODO either combine see also rest of the section. There are
//				// pages without see also, reference etc, then
//				// last three sections will be blindly ignored
//				for (int j = 0, size = sectionList.size(); j < size; j++) {
//					setSectionDetails(sectionList.get(j), wikiPageBean,
//							IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK,
//							errorWriter);
//					freqWords = TermFreqBasedTrimmer.getInstance()
//					.getFreqWords(sectionList.get(j).getText(),
//							stemmer, plainTitle,intWordRecog);
//					if (freqWords != null && freqWords.length() > 5) {
//						wikiPageBean.getSemClassMap().get(
//								IndexingConstants.SEM_CLASS_FREQUENT)
//								.getWords().append(freqWords);
//					}
//				}
//
//				// set synonyms
//
//				Set<String> Synonyms = setSynonyms(page, wikiPageBean,firstPara
//						+ " " + secondPara, errorWriter, stemmer);
//
//				//System.out.println("Before Category extraction ...");
//				// Select category and pages
//				setCategoriesPages(wikiPageBean, page, errorWriter, stemmer, Synonyms);
//
//				//System.out.println("Before Related extraction ...");
//				// set see also and External Links
//				setRelated(wikiPageBean, sectionList, errorWriter);
//				setNEType(wikiPageBean,sectionList, page, errorWriter);
//				// set homonym
//				setHomonyms(page, wikiPageBean, errorWriter);
//				setWordnetSynonymWords(wikiPageBean, stemmer);
//				// Add page type to hypernym
//				for (int j = 0, size = wikiPageBean.getPageTypes().size(); j < size; j++) {
//					wikiPageBean.getSemClassMap().get(
//							IndexingConstants.SEM_CLASS_HYPERNYM).getWords()
//							.append(wikiPageBean.getPageTypes().get(j));
//				}
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
//		// Looking for keywords in section names used in the case of person NE Type
//		if(sections !=null){
//			String secTitle = null;
//			int index = -1;
//			for(int i=0;i<sections.size();i++){
//				Section curSection=sections.get(i);
//				secTitle= curSection.getTitle();
//
//				if(curSection instanceof SectionContainer){
//					SectionContainer sectioncontainer = (SectionContainer) curSection;
//					List<Section> subsections = sectioncontainer.getSubSections();
//					Iterator<Section> subsectionsIter=subsections.iterator();
//					while(subsectionsIter.hasNext()){
//						Section curSubSection = subsectionsIter.next();
//						String subSecTitle = curSubSection.getTitle();
//						String NEType=getNEString(subSecTitle);
//						if(NEType==null) continue;
//						else {
//							wikiPageBean.setNEType(NEType);
//							return;
//						}
//					}
//				}
//				String NEType=getNEString(secTitle);
//				if(NEType!=null)
//				{
//					wikiPageBean.setNEType(NEType);
//					return;
//				}
//			}
//			// Looking for the matching words in category names for all other NE types.
//			Set<JwplCategory> categories = page.getJwplCategories();
//			Iterator<JwplCategory> catIterator = categories.iterator();
//			try{
//				while(catIterator.hasNext())
//				{
//					JwplCategory jwplCat= catIterator.next();
//					String catName=jwplCat.getTitle().getPlainTitle();
//					String NEType = getNEStringUsingCategories(catName);
//					if(NEType==null) continue;
//					else {
//						wikiPageBean.setNEType(NEType);
//						return;
//					}
//				}
//			}
//			catch(Exception e){
//				wikiPageBean.setNEType("");
//				try {
//					errorWriter.write("setNEType : Accessing category details of : "
//							+ wikiPageBean.getPageId() + " failed : "
//							+ e.getMessage() + " " + e.getCause() + "\n");
//					e.printStackTrace();
//				} catch (Exception exp) {
//					exp.printStackTrace();
//				}
//			}
//		}
//	}
//
//	private String getNEStringUsingCategories(String keyWord){
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
//				return IndexingConstants.NE_TYPE_MUSIC;
//		}
//		return null;
//	}
//
//	private String getNEString(String keyWord){
//		String[] personWords={"early life","career","biography","personal"};
//		Set<String> personKeyWords=new HashSet<String>(Arrays.asList(personWords));
//
//		Iterator<String> personIter= personKeyWords.iterator();
//		while(personIter.hasNext())
//		{
//			if(keyWord.indexOf((String)personIter.next())>=0)
//				return IndexingConstants.NE_TYPE_PERSON;
//		}
//
//
//		return null;
//	}
//
//	private void setRelated(WikiPageBean wikiPageBean, List<Section> sections,
//			BufferedWriter errorWriter) {
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
//				setExternalRelated(sections.get(index), wikiPageBean,
//						IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED,
//						errorWriter, Link.type.INTERNAL);
//			}
//
//			// There are multiple external link sections
//			for (int i = 0, size = exIndex.size(); i < size; i++) {
//
//				setExternalRelated(sections.get(exIndex.get(i)), wikiPageBean,
//						IndexingConstants.SEM_CLASS_REFERENCE, errorWriter,
//						Link.type.EXTERNAL);
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
//		.toString().split(" ");
//		toCompareList.addAll(Arrays.asList(splits));
//		//		splits = semClassMap.get(IndexingConstants.SEM_CLASS_SYNONYM)
//		//				.getWords().toString().split(" ");
//		//		toCompareList.addAll(Arrays.asList(splits));
//		//		splits = semClassMap.get(IndexingConstants.SEM_CLASS_SYNONYM_WORDNET)
//		//		.getWords().toString().split(" ");
//		//		toCompareList.addAll(Arrays.asList(splits));
//		toCompareList.addAll(synonyms);
//
//		while (categoriesIterator.hasNext()) {
//			category = categoriesIterator.next();
//			try {
//				catName = category.getTitle().getPlainTitle().replaceAll("_", " ")
//				.replace("(", "").replace(")", "");
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
//				if (util.isJaccardSimHigh(toCompareList, catName, stemmer)) {
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
//		.getWords().toString().split(" | ");
//		toCompareList.addAll(Arrays.asList(splits));
//		if(filteredCats.length() > 0) {
//			setSubCategoryPages(categoryMap, stemmer, toCompareList, semClassMap,
//					errorWriter, filteredCats.substring(0,
//							filteredCats.length() - 1));
//		}
//		page.setJwplCategories(null);
//
//	}
//
//	private void setSubCategoryPages(Map<Integer, String> categoryMap,
//			PorterStemmer stemmer, List<String> toCompareList,
//			Map<String, SemanticClass> semClassMap, BufferedWriter errorWriter,
//			String catIds) {
//
//		// Add category
//		PageCategoryDao dao = PageCategoryDao.getInstance();
//		StringBuilder[] filteredData = dao.getFilteredSubCatArticles(categoryMap,
//				catIds, conn, errorWriter, stemmer, toCompareList,null);
//		try {
//			if(filteredData != null) {
//				semClassMap.get(
//						IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED_HIERARCHY)
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
//		.getTemplates(1, 3);
//		Template template;
//		String infoBoxKey = ExtractionUtil.getInstance().getInfoBoxKeys();
//		String key = null;
//		for (int j = 0, size = templates.size(); j < size; j++) {
//			template = templates.get(j);
//			if (j == 2) {
//				List<String> parameters = template.getParameters();
//
//				Map<String, SemanticClass> semClass = wikiPageBean
//				.getSemClassMap();
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
//						} else if (key.indexOf("product") >= 0) {
//							semClass.get(IndexingConstants.SEM_CLASS_MERONYM)
//							.getWords().append(
//									parameter_words[1].replace("[", "")
//									.replace("]", "").replace(
//											"<br>", "")+" | ");
//						} else if (key.indexOf("type") >= 0
//								|| key.indexOf("industry") >= 0) {
//							semClass.get(IndexingConstants.SEM_CLASS_HYPERNYM)
//							.getWords().append(
//									parameter_words[1].replace("[", "")
//									.replace("]", "").replace(
//											"<br>", "")+" | ");
//						} else if (key.indexOf("known for") >= 0
//								|| key.indexOf("occupation") >= 0
//								|| key.indexOf("profession") >= 0
//								|| key.indexOf("role") >= 0) {
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
//		if (!page.isDisambiguation()) {
//			try {
//				// TODO For each redirect probably we should be having disamb?
//				// automobile is car. There is no page for car
//				JwplPage disambPage = page.getDisamgPage(wikiPageBean
//						.getPageTitle(),pageDisambStmt, errorWriter);
//				if (disambPage != null && disambPage.getTitle() != null) {
//					// TODO check results
//					List<Link> links = null;
//					List<Section> sections = disambPage.getParsedPage()
//					.getSections();
//
//					Link link = null;
//					// Page linkPage = null;
//					SemanticClass semClass = wikiPageBean.getSemClassMap().get(
//							IndexingConstants.SEM_CLASS_HOMONYM);
//					for (int j = 0, sizej = sections.size(); j < sizej; j++) {
//						links = sections.get(j).getLinks(Link.type.INTERNAL);
//						for (int i = 0, size = links.size(); i < size; i++) {
//							link = links.get(i);
//							try {
//								final String linkTarget = link.getTarget();
//								if(wikiPageBean.getTitleDisambText() != null && linkTarget.indexOf(wikiPageBean.getTitleDisambText()) < 0 )
//									semClass.getWords().append(
//											link.getTarget().replaceAll("_", " ") + " | ");
//							} catch (Exception e) {
//								try {
//									errorWriter
//									.write("Accessing disambiguation details of : "
//											+ wikiPageBean.getPageId()
//											+ " failed : "
//											+ e.getMessage()
//											+ " "
//											+ e.getCause() + "\n");
//									e.printStackTrace();
//								} catch (Exception exp) {
//									exp.printStackTrace();
//								}
//							}
//						}
//					}
//				}
//			} catch (Exception e) {
//				try {
//					errorWriter.write("Accessing disambiguation details of : "
//							+ wikiPageBean.getPageId() + " failed : "
//							+ e.getMessage() + " " + e.getCause() + "\n");
//					e.printStackTrace();
//				} catch (Exception exp) {
//					exp.printStackTrace();
//				}
//			}
//		}
//	}
//
//	private Set setSynonyms(JwplPage page, WikiPageBean wikiPageBean,
//			String text, BufferedWriter errorWriter, PorterStemmer stemmer) {
//		// redirects
//		Set<String> resultList = new HashSet<String>();
//		Set<String> redirects = page
//		.getRedirects(pageRedirectStmt, errorWriter);
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
//		.getPosAfterPatternSynonym(text);
//		//System.out.println("Synonym pos " + typePos);
//		if (typePos >= 0) {
//			String syn = getIdentifiedWord(text, typePos, stemmer);
//			if (syn != null)
//				semClass.getWords().append(syn + " | ");
//		}
//
////		semClass = wikiPageBean.getSemClassMap().get(
////				IndexingConstants.SEM_CLASS_SYNONYM_WORDNET);
////		// synonyms
////		StringBuilder builder = new StringBuilder();
////		final Set<String> temp = ExtractionUtil.getInstance().getSynonyms(
////				wikiPageBean.getPageTitle(), builder);
////		if(temp != null) {
////			resultList.addAll(temp);
////			semClass.getWords().append(builder+ " | ");
////		}
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
//		sb.append(wikiPageBean.getSemClassMap().get(
//				IndexingConstants.SECTION_HEADING).getWords());
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
//	private void setWordnetMeronymWords(WikiPageBean wikiPageBean, PorterStemmer stemmer){
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
//		StringBuilder builder = new StringBuilder();
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
//	}
//
//	private void setPageType(WikiPageBean wikiPageBean, String firstPara, PorterStemmer stemmer) {
//		int typePos = ExtractionUtil.getInstance().getPosAfterPattern(
//				firstPara);
//		//System.out.println("start of Hypernym " + typePos);
//		if (typePos >= 0) {
//			String pageType = getIdentifiedWord(firstPara, typePos, stemmer);
//			//System.out.println("Type of page : " + pageType);
//			if (pageType != null)
//				wikiPageBean.getPageTypes().add(pageType);
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
//	private void setExternalRelated(Section section, WikiPageBean wikiPageBean,
//			String semanticClassName, BufferedWriter errorWriter,
//			Link.type linkType) {
//		List<Link> links = section.getLinks(linkType);
//
//		if (links != null) {
//			Link link = null;
//			Map<String, SemanticClass> semClassMap = wikiPageBean
//			.getSemClassMap();
//			SemanticClass semClass = semClassMap.get(semanticClassName);
//			for (int i = 0, size = links.size(); i < size; i++) {
//				link = links.get(i);
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
//	}
//
//	private void setSectionDetails(Section section, WikiPageBean wikiPageBean,
//			String semanticClassName, BufferedWriter errorWriter) {
//		WikiSection wikiSection = new WikiSection();
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
//			wikiSection.setSectionName(secTitle);
//		}
//		List<Link> links = section.getLinks(Link.type.INTERNAL);
//
//		if (links != null) {
//			Link link = null;
//			WikiLink wikiLink = null;
//			Map<String, SemanticClass> semClassMap = wikiPageBean
//			.getSemClassMap();
//			SemanticClass semClass = semClassMap.get(semanticClassName);
//
//			// TODO this is the synonyms of the linked page. Not this
//			// I need to store against page id somewhere and read later when
//			// extraction of that page
//			// considered
//			SemanticClass synSemClass = semClassMap
//			.get(IndexingConstants.SEM_CLASS_SYNONYM);
//			String text = null;
//			for (int i = 0, size = links.size(); i < size; i++) {
//				link = links.get(i);
//
//				// If link text is different from target then add it to
//				text = link.getText();
//				if (!link.getTarget().equals(text)) {
//					//synSemClass.getWords().append(link.getText() + " ");
//				}
//
//				wikiLink = new WikiLink(text, link.getContext(3, 0),link.getContext(0, 3));
//				//wikiSection.getInternalLinks().add(wikiLink);
//
//				if (semClass != null) {
//					try {
//						// Points to images
//						if(link.getTarget().indexOf("File:") >= 0) {
//							continue;
//						}
//						semClass.getWords().append(link.getTarget().replaceAll("_", " ").replaceAll("#", " ") + " | ");
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
//
//		wikiPageBean.getSections().add(wikiSection);
//	}
//
//	private JwplPage getPage(int pageId, BufferedWriter errorWriter) {
//		JwplPage page = null;
//		try {
//			page = PageDao.getInstance().getJwplPageDetails(pageId,
//					pageCategoryStmt, null,errorWriter);
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
//	private int getNoOfInLinks(int pageId,BufferedWriter errorWriter){
//		return PageDao.getInstance().getNoOfInLinks(pageId, inLinkStmt, errorWriter);
//	}
//
//	private int getNoOfOutLinks(int pageId,BufferedWriter errorWriter){
//		return PageDao.getInstance().getNoOfInLinks(pageId, outLinkStmt, errorWriter);
//	}
//	private synchronized IIndexWord dictionaryLookup(String token, POS posTag) {
//		return dictionary.getIndexWord(token, posTag);
//	}
//	public String wordType(String word, PorterStemmer stemmer) {
//		ExtractionUtil util = ExtractionUtil.getInstance();
//		word = util.getRootForm(word, stemmer);
//		IIndexWord indexWord = null;
//		if(word.endsWith("ed")) {
//			word = word.substring(0,word.length()-2);
//		} else if(word.endsWith("ing")) {
//			word = word.substring(0,word.length()-3);
//			indexWord = dictionaryLookup(word,
//					POS.VERB);
//			if (indexWord != null) return "verb";
//			word = word +"e";
//		}
//		indexWord = dictionaryLookup(word,
//				POS.VERB);
//		if (indexWord != null)
//		return "verb";
//		indexWord = dictionaryLookup(word,
//				POS.NOUN);
//		if (indexWord != null)
//		return "noun";
//		indexWord = dictionaryLookup(word,
//				POS.ADJECTIVE);
//		if (indexWord != null)
//		return "adjective";
//		return "false";
//	}
//	public String demoDP(LexicalizedParser lp, String filename) {
//	    // This option shows loading and sentence-segment and tokenizing
//	    // a file using DocumentPreprocessor
//	    TreebankLanguagePack tlp = new PennTreebankLanguagePack(); 
//	    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
//	    // You could also create a tokenier here (as below) and pass it
//	    // to DocumentPreprocessor
//	    String sent="";
//	    //for (List<HasWord> sentence : new DocumentPreprocessor(filename)) {
//	    Iterator<List<HasWord>> i=new DocumentPreprocessor(filename).iterator();
//	     List<HasWord> sentence=i.next();
//	      Tree parse = lp.apply(sentence);
//	      //parse.pennPrint();
//	      System.out.println();
//	      
//	      GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
//	      Collection tdl = gs.typedDependenciesCCprocessed(true);
//	     
//	      Iterator iter=tdl.iterator();
//	      while(iter.hasNext())sent+=iter.next().toString().replace(" ","");
//	      System.out.println(sent);
//	    //}
//		return sent ;
//	  }
//	public String demoAPI(LexicalizedParser lp,String str) {
////		  String[] sent=str.replace("."," .").replace("?"," ?").replace("!"," !").split(" ");
////	    //String[] sent = { "This", "is", "an", "easy", "sentence", "." };
//	  //  List<CoreLabel> rawWords = new ArrayList<CoreLabel>();
////	    for (String word : sent) {
////	      CoreLabel l = new CoreLabel();
////	      l.setWord(word);
////	      rawWords.add(l);
////	    }
////	    Tree parse = lp.apply(rawWords);
////	    parse.pennPrint();
////	    System.out.println();
//
//	    // This option shows loading and using an explicit tokenizer
//	    //"This is another sentence.";
//	    TokenizerFactory<CoreLabel> tokenizerFactory = 
//	      PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
//	    List<CoreLabel> rawWords2 = 
//	      tokenizerFactory.getTokenizer(new StringReader(str)).tokenize();
//	    	Tree parse = lp.apply(rawWords2);
//
//	    TreebankLanguagePack tlp = new PennTreebankLanguagePack();
//	    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
//	    GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
//	    List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
//	    //System.out.println(tdl);
//	    String out="";
//	    Iterator<TypedDependency> i=tdl.iterator();
//	    while(i.hasNext()){
//	    	out+=i.next().toString();
//	    }
//	    out=out.replace(" ","");
//	    //System.out.println(out);
//	    TreePrint tp = new TreePrint("wordsAndTags");
//	    //tp.printTree(parse);
//	    taggedWords = parse.taggedYield();
//	    return out;
//	//    
//	    }
//	public boolean compareTitle(String title,int index){
//		String[] k=title.split(" ");
//		for(int i=0;i<k.length;i++){
//			String u=k[i].toLowerCase();
//			String v=taggedWords.get(index).toString().toLowerCase();
//			if(!(k[i].toLowerCase().equals(taggedWords.get(index++).toString().toLowerCase().split("/")[0]))){
//				return false;
//			}
//		}
//		return true;
//	}
//	public boolean checkNE(String word){
//		return true;
//	}
//	public ArrayList<String> getTitles(){
//		ArrayList<String> title = new ArrayList<String>();
//		title.add(plainTitle);
//		title.add("computer");
//		return title;
//	}
//	public phraseClass getPhrases(int pageId) throws Exception {
//		// TODO Auto-generated method stub
//		TagText tagText = TagText.getInstance(null);
//		conn = DBConnection.getInstance().getConnection();
//		initStatements();
//		InterestingWordRecognizer intWordRecog = new InterestingWordRecognizer(null);
//		BufferedWriter errorWriter = new BufferedWriter(new OutputStreamWriter(
//				new FileOutputStream("ErrorFile.log"), "UTF8"));
//		WikiPageBean wikiPageBean = new WikiPageBean();
//		ArrayList<String> phrases=new ArrayList<String>();
//		Set<String> phrasecount=new HashSet<String>();
//	    Set<String> prefix=new HashSet<String>();
//		Set<String> suffix= new HashSet<String>();
//		//wikiPageBean.setPageId(1908150); //198584
//		//wikiPageExtract.initWikiPageDetails(pageBean, errorWriter);
//		//wikiPageBean.getPageId();
//		JwplPage page = getPage(pageId, errorWriter);
//		if (page != null) {
//
//			// Page title
//			try {
//				Title title = page.getTitle();
//				String wikiTitle = title.getWikiStyleTitle();
//				//System.out.println("Wiki title :" + wikiTitle);
//				int disTextStart = wikiTitle.indexOf("(");
//				
//				System.out.println("Wiki title position :" + disTextStart);
//				plainTitle = title.getPlainTitle();
//				System.out.println("Plain title : " + plainTitle);
//				if (disTextStart >= 0 && wikiTitle.indexOf(")") > wikiTitle.length()-2) {
//					wikiTitle = wikiTitle.substring(disTextStart + 1)
//					.replace(")", "").replaceAll("_", " ").trim();
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
//				List<Section> sectionList = page.getParsedPage().getSections();
//				//System.out.println("Size of the section size : "
//				//		+ sectionList.size());
//
//				// abstract, frequent words, links in abstract carry a lot
//				// information
//				String path="/home/ambha/workspace/englishPCFG.ser.gz";
//			    LexicalizedParser lp = 
//			      new LexicalizedParser(path);
//			    
//				String pfixfull="",sfixfull="",phrasefull="";
//				for(int k=0;k<sectionList.size();k++){
//					Section abstractSection = sectionList.get(k);
//					String sectiontitle=abstractSection.getTitle();
//					pfixfull="";sfixfull="";phrasefull="";			
//				String freqWords = null;
//				String secondPara = "";
//				
//				if(abstractSection.getText() != null){
//					
//					secondPara = abstractSection.getText();
//					
//					if(sectiontitle==null) sectiontitle="main";
//					String[] lines=secondPara.split("\\.");
//					
//					
//                    ArrayList<String> titles=getTitles();
//                    for(int l=0;l<titles.size();l++){
//                    	int titlelen=titles.get(l).toString().split(" ").length;
//				    for(int j=0;j<lines.length;j++){
//				    	
//				    		//String[] words=lines[j].split(" ");
//				    		taggedWords=Arrays.asList(tagText.tagSentence(lines[j].replaceAll("[^a-zA-Z ]+"," ")).split(" "));
//				    		//demoAPI(lp,lines[j]);
//				    		for(int i=0;i<taggedWords.size();i++){
//				    			if(taggedWords.get(i).toString().split("/")[1].toLowerCase().contains("vb")||taggedWords.get(i).toString().split("/")[1].toLowerCase().contains("nn")){
//				    				String phrase=taggedWords.get(i).toString().split("/")[0];
//				    				int h=i;
//				    				phrases.add(phrase);
//				    				//System.out.println(phrase);
//				    				while(++h<taggedWords.size() && taggedWords.get(h).toString().split("/")[1].toLowerCase().contains("nn")){
//				    					phrase=phrase+" "+taggedWords.get(h).toString().split("/")[0];
//				    					phrases.add(phrase);
//				    					//System.out.println(phrase);
//				    				}
//				    			}
//				    			if(checkNE(titles.get(l).toString())){
//				    				if(titles.get(l).toString().toLowerCase().contains(taggedWords.get(i).toString().split("/")[0].toLowerCase())){
//				    					if(i!=0&&(taggedWords.get(i-1).toString().split("/")[1].toLowerCase().contains("jj")||taggedWords.get(i-1).toString().split("/")[1].toLowerCase().contains("nn"))){
//					    					prefix.add(sectiontitle+"|"+taggedWords.get(i-1).toString().split("/")[0]+" "+taggedWords.get(i).toString().toLowerCase().split("/")[0]);
//					    					String pfix=taggedWords.get(i-1).toString().split("/")[0]+" "+taggedWords.get(i).toString().toLowerCase().split("/")[0];
//					    					int h=i-1;
//					    					pfixfull+=pfix+"|";
//					    					//System.out.println(pfix);
//					    					while(h>=1 && taggedWords.get(--h).toString().split("/")[1].toLowerCase().contains("jj")){
//						    					pfix=taggedWords.get(h).toString().split("/")[0]+" "+pfix;
//						    					pfixfull+=pfix+"|";
//						    					//System.out.println(sectiontitle+"|"+pfix);
//						    				}
//					    					if(taggedWords.get(i-1).toString().split("/")[1].toLowerCase().contains("nn")) {
//					    					h=i-1;
//					    					while(h>=1 && taggedWords.get(--h).toString().split("/")[1].toLowerCase().contains("nn")){
//						    					pfix=taggedWords.get(h).toString().split("/")[0]+" "+pfix;
//						    					pfixfull+=pfix+"|";
//						    					//System.out.println(sectiontitle+"|"+pfix);
//						    				}
//					    					}
//					    				}
//				    					if((i+1)<=taggedWords.size()-1 && taggedWords.get(i+1).toString().split("/")[1].toLowerCase().contains("nn")){
//					    					//suffix.add(sectiontitle+"|"+plainTitle.toLowerCase()+" "+taggedWords.get(i+titlelen).toString().split("/")[0]);
//					    					String sfix=taggedWords.get(i).toString().split("/")[0]+" "+taggedWords.get(i+1).toString().split("/")[0];
//					    					sfixfull+=sfix+"|";
//					    					int h=i+1;
//					    					//System.out.println(sfix);
//					    					while(++h<taggedWords.size() && taggedWords.get(h).toString().split("/")[1].toLowerCase().contains("nn")){
//						    					sfix=sfix+" "+taggedWords.get(h).toString().split("/")[0];
//						    					//System.out.println(sfix);
//						    					sfixfull+=sfix+"|";
//						    				}
//					    				}	
//				    				}
//				    			}
//				    			if(lines[j].toLowerCase().contains(titles.get(l).toString().toLowerCase())){
//				    			if(compareTitle(titles.get(l).toString(),i)){
//				    				if(i!=0&&(taggedWords.get(i-1).toString().split("/")[1].toLowerCase().contains("jj")||taggedWords.get(i-1).toString().split("/")[1].toLowerCase().contains("nn"))){
//				    					prefix.add(sectiontitle+"|"+taggedWords.get(i-1).toString().split("/")[0]+" "+titles.get(l).toString().toLowerCase());
//				    					String pfix=taggedWords.get(i-1).toString().split("/")[0]+" "+titles.get(l).toString().toLowerCase();
//				    					int h=i-1;
//				    					pfixfull+=pfix+"|";
//				    					//System.out.println(pfix);
//				    					while(h>=1 && taggedWords.get(--h).toString().split("/")[1].toLowerCase().contains("jj")){
//					    					pfix=taggedWords.get(h).toString().split("/")[0]+" "+pfix;
//					    					pfixfull+=pfix+"|";
//					    					//System.out.println(sectiontitle+"|"+pfix);
//					    				}
//				    					if(taggedWords.get(i-1).toString().split("/")[1].toLowerCase().contains("nn")) {
//					    					h=i-1;
//					    					while(h>=1 && taggedWords.get(--h).toString().split("/")[1].toLowerCase().contains("nn")){
//						    					pfix=taggedWords.get(h).toString().split("/")[0]+" "+pfix;
//						    					pfixfull+=pfix+"|";
//						    					//System.out.println(sectiontitle+"|"+pfix);
//						    				}
//					    					}
//				    				}
//				    				if((i+titlelen)<=taggedWords.size()-1 && taggedWords.get(i+titlelen).toString().split("/")[1].toLowerCase().contains("nn")){
//				    					//suffix.add(sectiontitle+"|"+plainTitle.toLowerCase()+" "+taggedWords.get(i+titlelen).toString().split("/")[0]);
//				    					String sfix=titles.get(l).toString().toLowerCase()+" "+taggedWords.get(i+titlelen).toString().split("/")[0];
//				    					sfixfull+=sfix+"|";
//				    					int h=i+titlelen;
//				    					//System.out.println(sfix);
//				    					while(++h<taggedWords.size() && taggedWords.get(h).toString().split("/")[1].toLowerCase().contains("nn")){
//					    					sfix=sfix+" "+taggedWords.get(h).toString().split("/")[0];
//					    					//System.out.println(sfix);
//					    					sfixfull+=sfix+"|";
//					    				}
//				    				}				    				
//				    			}
//				    		}				      
//				    	}
//				    }
//				} 
//				}
//				
//				phrasefull="";
//				for(int m=0;m<phrases.size();m++){
//					int cnt=Collections.frequency(phrases, phrases.get(m));
//					if(cnt>=2)
//						phrasefull+=phrases.get(m)+"~"+cnt+"|";
//				}
//				phrases.clear();
//				if(pfixfull!="")
//				prefix.add(sectiontitle+"||"+pfixfull);	
//				if(sfixfull!="")
//				suffix.add(sectiontitle+"||"+sfixfull);
//				if(phrasefull!="")
//				phrasecount.add(sectiontitle+"||"+phrasefull);
//				
//				}
//				
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
//		phraseClass pc=new phraseClass();
//		pc.phrases=phrasecount;
//		pc.prefix=prefix;
//		pc.suffix=suffix;
//		return pc;
//	}
//	class phraseClass{
//		Set<String> prefix;
//		Set<String> suffix;
//		Set<String> phrases;
//		phraseClass(){
//			phrases=new HashSet<String>();
//		    prefix=new HashSet<String>();
//			suffix= new HashSet<String>();
//		}
//	}
//	public static void main(String[] args) throws Exception{
//		CooccurenceFrequent cf=new CooccurenceFrequent();
//		phraseClass pc;
//		pc=cf.getPhrases(2120540);//Pass page id to getPhrases
//		System.out.println(pc.phrases.toString());//Is of the form <section>||<phrase>~<count>
//		System.out.println(pc.prefix.toString());
//		System.out.println(pc.suffix.toString());
//	}
}