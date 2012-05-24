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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aneedo.indexing.bean.AssociationOverlap;
import com.aneedo.indexing.bean.SemanticClass;
import com.aneedo.indexing.bean.WikiPageBean;
import com.aneedo.indexing.bean.WikiSection;
import com.aneedo.jwplext.JwplPage;
import com.aneedo.jwplext.JwplSQLConstants;
import com.aneedo.jwplext.dao.AssociationDao;
import com.aneedo.jwplext.dao.PageDao;
import com.aneedo.jwplext.dao.tablecreation.DBConnectionFactory;
import com.aneedo.search.util.TagText;
import com.aneedo.util.ExtractionUtil;
import com.aneedo.util.InterestingWordRecognizer;
import com.aneedo.util.PorterStemmer;
import com.aneedo.util.StopwordRecognizer;

import de.tudarmstadt.ukp.wikipedia.api.Title;
import de.tudarmstadt.ukp.wikipedia.parser.Link;
import de.tudarmstadt.ukp.wikipedia.parser.Section;
import de.tudarmstadt.ukp.wikipedia.parser.SectionContainer;

public class Test {
	private PreparedStatement pageCategoryStmt = null;
	private PreparedStatement linkOverlapStmt = null;
	private PreparedStatement linkCatOverlapStmt = null;
	private PreparedStatement pageRedirectStmt = null;
	private String path = "/data2/indexing/semantic"; 
	private final Pattern templatePattern = Pattern.compile("TEMPLATE\\[([^\\]]+)]");
	private final Pattern quotePattern = Pattern.compile("\\[\\[([^\\]\\]]+)]]");
	private final Pattern tagPattern1 = Pattern.compile("(?i)(<ref.*?>)(.+?)(</ref>)");
	private final Pattern tagPattern2 = Pattern.compile("(?i)(<small.*?>)(.+?)(</small>)");
	private final Pattern nonWordpattern = Pattern.compile("^[a-zA-Z-']");
	private final String JUNK_WORDS ="infobox template logo png file image size color text name imagesize " +
			"birth_date birth_place birth_name jpg px caption thumb mm left right retrieved <br>";
	
	
	private JwplPage getPage(int pageId, BufferedWriter errorWriter,Connection conn) {
		JwplPage page = null;
		try {
			
			page = PageDao.getInstance().getJwplPageDetails(pageId,
					pageCategoryStmt,conn, errorWriter);
			// page = wiki.getPage(pageId);
			//System.out.println("Page at getPage : " + page);
		} catch (Exception e) {
			try {
				errorWriter.write("Accessing page with id : " + pageId
						+ " failed : " + e.getMessage() + " " + e.getCause()
						+ "\n");
				e.printStackTrace();
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}
		return page;
	}
	
	public static void main(String[] args) throws Exception {
		Connection conn = DBConnectionFactory.getInstance("Wikipedia","narayan",null).getConnectionOfDatabase();
		BufferedWriter errorWriter=null;
		Test test = new Test();
		test.pageCategoryStmt = conn
		.prepareStatement(JwplSQLConstants.SELECT_PAGE_CATEGORY_DETAIL);
		
//		
//		test.linkOverlapStmt = conn
//		.prepareStatement(JwplSQLConstants.SELECT_LINK_OVERLAP_COUNT);
//		
//		test.linkCatOverlapStmt = conn
//		.prepareStatement(JwplSQLConstants.SELECT_LINK_CATEGORY_OVERLAP);
		errorWriter = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("ErrorFileNew.log"), "UTF8"));
		int pageId = 1449;//198584; //974
		
		WikiPageBean wikiPageBean = new WikiPageBean();
		wikiPageBean.setPageId(pageId);
		JwplPage page = test.getPage(pageId, errorWriter,conn);
		if(page == null) return;
		
		Title title = page.getTitle();
		String wikiTitle = title.getWikiStyleTitle();
		//System.out.println("Wiki title :" + wikiTitle);
		int disTextStart = wikiTitle.indexOf("(");
		//System.out.println("Wiki title position :" + disTextStart);
		String plainTitle = title.getPlainTitle();
		//System.out.println("Plain title : " + plainTitle);
		if (disTextStart >= 0 && wikiTitle.indexOf(")") > wikiTitle.length()-2) {
			wikiTitle = wikiTitle.substring(disTextStart + 1)
					.replace(")", "").replaceAll("_", " ").trim();
			//System.out.println("Wiki title :" + wikiTitle);
			plainTitle = plainTitle.substring(0,
					plainTitle.indexOf(wikiTitle)).replace("(", "").trim();
		} else {
			wikiTitle = null;
		}
		System.out.println("Plain title after removing disamb : "
			+ plainTitle);
		wikiPageBean.setPageTitle(plainTitle);
		PorterStemmer stemmer = new PorterStemmer();
		test.pageRedirectStmt = conn.prepareStatement(JwplSQLConstants.SELECT_REDIRECT);
		
		Set<String> Synonyms = test.setSynonyms(page, wikiPageBean,""
				+ " " + "", errorWriter, stemmer);
		
		List<Section> sectionList = page.getParsedPage().getSections();
		
		//System.out.println(page.getParsedPage().getText());
		
		 PreparedStatement selectLinkCatOverlapStmt=null;
		PreparedStatement selectOutlinksStmt = null;
		PreparedStatement selectAssOverlapStmt  = null;
		selectOutlinksStmt = conn
				.prepareStatement(JwplSQLConstants.SELECT_OUTLINKS);
		selectLinkCatOverlapStmt = conn
				.prepareStatement(JwplSQLConstants.SELECT_ASS_PARENT_OVERLAP);
		selectAssOverlapStmt = conn
				.prepareStatement(JwplSQLConstants.SELECT_ASS_OVERLAP_QUERY);
	
		
		AssociationDao dao = AssociationDao.getInstance();
		StringBuilder assBuilder = new StringBuilder();
		Map<String,AssociationOverlap> overlapMap = dao.getAssociationOverlap(198584, new PreparedStatement[] {selectOutlinksStmt,selectAssOverlapStmt,	
				selectLinkCatOverlapStmt}, errorWriter, assBuilder);
		String titleToCompare = test.getAllTitleRelatedWords(wikiPageBean.getPageTitle(), stemmer,
				wikiPageBean.getSemClassMap().get(IndexingConstants.SEM_CLASS_SYNONYM).getWords().toString());
		for (int j = 0, size = sectionList.size(); j < size; j++) {
			test.setSectionDetails(sectionList.get(j), wikiPageBean,overlapMap,titleToCompare,
					errorWriter,stemmer);
		}
		
//		System.out.println("Ass : "+wikiPageBean.getSemClassMap().get(IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK).getWords());
//		System.out.println("Parent : " + wikiPageBean.getSemClassMap().get(IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK_PARENT).getWords());
		System.out.println(" Cooccur : " + wikiPageBean.getSemClassMap().get(IndexingConstants.SEM_CLASS_COOCCURRENCE).getWords());
		System.out.println("Frequent : " +wikiPageBean.getSemClassMap().get(IndexingConstants.SEM_CLASS_FREQUENT).getWords());
		
//		for(int i=0;i<wikiPageBean.getSections().size();i++) {
//			
//			WikiSection section = wikiPageBean.getSections().get(i);
//			System.out.println("name : "+section.getSectionName());
//			if(section.getImageText() != null)
//				System.out.println("Img : " +section.getImageText());
//			
//		}
		
//		for(int i=0;i<sectionList.size();i++) {
//			List<Link> linkList = sectionList.get(i).getLinks(Link.type.EXTERNAL);
//			for(int j=0;j<linkList.size();j++) {
//				System.out.println("Target " + linkList.get(j).getTarget());
//				System.out.println("ToString " + linkList.get(j).toString());
//				System.out.println("Text " + linkList.get(j).getText());
//			}
//		}
		
		if(conn != null) conn.close();
	}
	
	private Set setSynonyms(JwplPage page, WikiPageBean wikiPageBean,
			String text, BufferedWriter errorWriter, PorterStemmer stemmer) {
		// redirects
		Set<String> resultList = new HashSet<String>();
		Set<String> redirects = page
				.getRedirects(pageRedirectStmt, errorWriter);
		SemanticClass semClass = wikiPageBean.getSemClassMap().get(
				IndexingConstants.SEM_CLASS_SYNONYM);
		SemanticClass semClassWNet = wikiPageBean.getSemClassMap().get(
								IndexingConstants.SEM_CLASS_SYNONYM_WORDNET);
		Iterator<String> redirectsIterator = redirects.iterator();
		while (redirectsIterator.hasNext()) {
			final String redirect = redirectsIterator.next().replaceAll("_", " ").toLowerCase();
			if(redirect.trim().length() >= 2) {
				if(resultList.add(redirect)) {
					semClass.getWords().append(redirect + " | ");
				}
			}
		}

		// extract from first paragraph
		if(text.equals("")) return resultList;
		int typePos = ExtractionUtil.getInstance(path)
				.getPosAfterPatternSynonym(text);
		//System.out.println("Synonym pos " + typePos);
		if (typePos >= 0) {
			String syn = getIdentifiedWord(text, typePos, stemmer);
			if (syn != null)
				semClassWNet.getWords().append(syn + " | ");
		}

		//		semClass = wikiPageBean.getSemClassMap().get(
		//				IndexingConstants.SEM_CLASS_SYNONYM_WORDNET);
		//		// synonyms
		//		StringBuilder builder = new StringBuilder();
		//		final Set<String> temp = ExtractionUtil.getInstance().getSynonyms(
		//				wikiPageBean.getPageTitle(), builder);
		//		if(temp != null) {
		//			resultList.addAll(temp);
		//			semClass.getWords().append(builder+ " | ");
		//		}
		return resultList;
	}
	
	private String getIdentifiedWord(String firstPara, int startingPos, PorterStemmer stemmer) {

		// hypernym or synonym exists for a page it will be mentioned in first sentence
		// TODO add this when the data is clean 
		//		if(firstPara.indexOf(".") < startingPos) {
		//			return null;
		//		}

		firstPara = firstPara.substring(startingPos);
		String[] splits = firstPara.split(" ");
		StopwordRecognizer stopWordRecog = StopwordRecognizer.getInstance();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < splits.length; i++) {
			if(splits[i].endsWith(",")) {
				splits[i] = splits[i].replace(",", "");
				builder.append(splits[i] + " ");
				break;
			}
			// stop word or full stop or verb
			if (stopWordRecog.isStopWordForType(splits[i]) 
					|| splits[i].indexOf(".") >= 0
					|| intWordRecog.isVerb(splits[i], stemmer)) {
				break;
			}
			builder.append(splits[i] + " ");
		}
		if (builder.length() > 2) {
			return builder.toString();
		}
		return null;
	}
	private InterestingWordRecognizer intWordRecog = new InterestingWordRecognizer(null);
	
	private void setPageType(WikiPageBean wikiPageBean, String firstPara, PorterStemmer stemmer) {
		int typePos = ExtractionUtil.getInstance(path).getPosAfterPattern(
				firstPara);
		//System.out.println("start of Hypernym " + typePos);
		if (typePos >= 0) {
			List<String> pageType = getIdentifiedWord(firstPara, typePos, stemmer, null);
			//System.out.println("Type of page : " + pageType);
			if (pageType != null)
				wikiPageBean.getPageTypes().addAll(pageType);
		}
	}



	private List<String> getIdentifiedWord(String firstPara, int startingPos, 
			PorterStemmer stemmer, List<Link> firstSecLinkList) {

		// hypernym or synonym exists for a page it will be mentioned in first sentence

		firstPara = firstPara.substring(startingPos);
		String[] splits = firstPara.split(" ");
		StopwordRecognizer stopWordRecog = StopwordRecognizer.getInstance();
		List<String> pageTypeList = new ArrayList<String>();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < splits.length; i++) {
			
			// associated with more than one type
			if(splits[i].endsWith(",")) {
				splits[i] = splits[i].replace(",", "");
				pageTypeList.add(builder.toString()+" " + splits[i]);
				builder = new StringBuilder();
			}
			
			if(splits[i].equals("and")) {
				continue;
			}

			// stop word or full stop or verb
			if (stopWordRecog.isStopWordForType(splits[i]) 
					|| splits[i].indexOf(".") >= 0
					|| intWordRecog.isVerb(splits[i], stemmer)) {
				break;
			}
			builder.append(splits[i] + " ");
		}
		pageTypeList.add(builder.toString());
		
		int size = pageTypeList.size();
		List<String> pageTypeNewList = new ArrayList<String>();
		if (size > 0) {
			
			for(int i=0;i<size;i++) {
				
				final String pageType = pageTypeList.get(i);
				
				for(int j=0,linkSize=firstSecLinkList.size();j<linkSize;j++) {
					Link link = firstSecLinkList.get(j);
					try {
					final String plainTitle = new Title(link.getTarget()).getPlainTitle().replaceAll("-", " ").replace("(", "").replace(")", "").replaceAll("#", " ").trim();
					
					if(pageType.equals(plainTitle) 
							|| pageType.startsWith(plainTitle)
							|| pageType.endsWith(plainTitle)
							|| plainTitle.startsWith(pageType)
							|| plainTitle.endsWith(pageType)
							)  {
						pageTypeNewList.add(plainTitle);
						break;
					} else {
						pageTypeNewList.add(pageType);
						break;
					}
					} catch (Exception e) {
						pageTypeNewList.add(pageType);
					}
				}
				
			}
			return pageTypeNewList;
		}
		return null;
	}
	
	private void setSectionDetails(Section section, WikiPageBean wikiPageBean,
			Map<String,AssociationOverlap> assMap, String titleWordsToCompare, 
			BufferedWriter errorWriter, PorterStemmer stemmer) {
		WikiSection wikiSection = new WikiSection();
		final Map<String, SemanticClass> semClassMap = wikiPageBean
				.getSemClassMap();


		// Abstract does not have any heading
		String secTitle = section.getTitle();
		if(secTitle == null) {
			wikiSection.setSectionName("abstract");
		} else {
			secTitle = secTitle.toLowerCase();
			// don't do it for reference or see also
			if (((secTitle.indexOf("see") >= 0)
					&& (secTitle.indexOf("also")) >= 0)
					||(secTitle.indexOf("reference") >= 0)
					|| ((secTitle.indexOf("external") >= 0) && (secTitle
							.indexOf("link") >= 0))
							|| (secTitle.indexOf("note") >= 0)
							|| ((secTitle.indexOf("further") >= 0) && (secTitle
									.indexOf("reading") >= 0))) {
				return;
			}

			if(section instanceof SectionContainer){
				SectionContainer sectioncontainer = (SectionContainer) section;
				List<Section> subsections = sectioncontainer.getSubSections();
				Iterator<Section> subsectionsIter=subsections.iterator();
				while(subsectionsIter.hasNext()){
					String subSecTitle = subsectionsIter.next().getTitle();
					if(subSecTitle != null && !"".equals(subSecTitle.trim())) {
						secTitle = secTitle +" / " + subSecTitle;
					}
				}
			} 
			wikiSection.setSectionName(secTitle);
		}

		final SemanticClass semClassAssLink = semClassMap.get(IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK);
		semClassAssLink.getWords().append("/ " + wikiSection.getSectionName() +" / ");
		final SemanticClass semClassAssLinkParent = semClassMap.get(IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK_PARENT);
		semClassAssLinkParent.getWords().append("/ " + wikiSection.getSectionName() +" / ");
		final SemanticClass semClassCooccurrence = semClassMap.get(IndexingConstants.SEM_CLASS_COOCCURRENCE);
		semClassCooccurrence.getWords().append("/ " + wikiSection.getSectionName() +" / ");
		final SemanticClass semClassFrequent = semClassMap.get(IndexingConstants.SEM_CLASS_FREQUENT);
		semClassFrequent.getWords().append("/ " + wikiSection.getSectionName() +" / ");

		// Set association and parent
		List<Link> links = section.getLinks(Link.type.INTERNAL);
		String imgTextSection ="";
		if (links != null) {
			//WikiLink wikiLink = null;
			for (int i = 0, size = links.size(); i < size; i++) {
				final Link link = links.get(i);
				if (semClassAssLink != null) {
					// Link text is very noisy, don't store

					try {
						// Points to images
						final String target = link.getTarget();
						if(target.indexOf("File:") >= 0 || target.indexOf("Image:") >= 0) {
							String imgText = link.getText();
							imgText = imgText.substring(imgText.lastIndexOf('|')+1, imgText.length());
							//System.out.println("Imge text " + imgText);
							if(imgText != null && !"".equals(imgText.trim())) {
								imgTextSection = imgTextSection + imgText +" | ";
							}
							//TODO add even link present there
							continue;
						}
						try {
							final String assLinkTitle = new Title(target).getPlainTitle().replaceAll("-", " ").replace("(", "").replace(")", "");
							final String tempLinkTitle = assLinkTitle.replaceAll("#", " ").trim();
							final String tempToCompare = tempLinkTitle.toLowerCase();
							AssociationOverlap assOverlap = assMap.get(tempToCompare);
							if(assOverlap != null) {
								semClassAssLink.getWords().append(tempLinkTitle + " |" +assOverlap.getLinkId()+"/"+ assOverlap.getLinkOverlapCount() +"| ");
								if(assOverlap.getLinkParentOverlap() == null) 
									semClassAssLinkParent.getWords().append("NA|0| ");
								else 
									semClassAssLinkParent.getWords().append(assOverlap.getLinkParentOverlap() +assOverlap.getLinkCatOverlapId()+" ");
							} else {
								String temp = tempToCompare.replaceAll(" ", "");
								 assOverlap = assMap.get(temp);
								if(assOverlap != null) {
									semClassAssLink.getWords().append(tempLinkTitle + " |" +assOverlap.getLinkId()+"/"+ assOverlap.getLinkOverlapCount() +"| ");
									if(assOverlap.getLinkParentOverlap() == null) 
										semClassAssLinkParent.getWords().append("NA|0| ");
									else 
										semClassAssLinkParent.getWords().append(assOverlap.getLinkParentOverlap() +assOverlap.getLinkCatOverlapId()+" ");
								} else {
									 temp = ExtractionUtil.getInstance(path).getRootFormStrings(tempToCompare, stemmer);
									 assOverlap = assMap.get(temp);
									if(assOverlap != null) {
										semClassAssLink.getWords().append(tempLinkTitle + " |" +assOverlap.getLinkId()+"/"+ assOverlap.getLinkOverlapCount() +"| ");
										if(assOverlap.getLinkParentOverlap() == null) 
											semClassAssLinkParent.getWords().append("NA|0| ");
										else 
											semClassAssLinkParent.getWords().append(assOverlap.getLinkParentOverlap() +assOverlap.getLinkCatOverlapId()+" ");
									} else {
									temp = assLinkTitle.substring(0, assLinkTitle.indexOf("#")).toLowerCase().trim();
									assOverlap = assMap.get(temp);
									if(assOverlap != null) {
										semClassAssLink.getWords().append(temp + " |" +assOverlap.getLinkId()+"/"+ assOverlap.getLinkOverlapCount() +"| ");
										if(assOverlap.getLinkParentOverlap() == null) 
											semClassAssLinkParent.getWords().append("NA|0| ");
										else 
											semClassAssLinkParent.getWords().append(assOverlap.getLinkParentOverlap() +assOverlap.getLinkCatOverlapId()+" ");
									} else {
											semClassAssLink.getWords().append(tempLinkTitle + " |NA/0| ");
									}
								}
								try {
									//errorWriter.write("warning : Hyperlink page name mismatch in DB and parser for "+ wikiPageBean.getPageId()+" link " +assLinkTitle+"\n");
								} catch (Exception exp) {
									// ignore
								}
							}
							//System.out.println("link title " + new Title(target).getPlainTitle());
						} 
							} catch (Exception e) {

							final String assLinkTitle = target.replaceAll("_", " ").replaceAll("-", " ").replace("(", "").replace(")", "").replace("#", " ").trim();
							AssociationOverlap assOverlap = assMap.get(assLinkTitle);
							if(assOverlap != null) {
								semClassAssLink.getWords().append(assLinkTitle + " |" +assOverlap.getLinkId()+"/"+ assOverlap.getLinkOverlapCount() +"| ");
								semClassAssLinkParent.getWords().append(assOverlap.getLinkParentOverlap() +assOverlap.getLinkCatOverlapId()+" ");
								if(assOverlap.getLinkParentOverlap() == null) 
									semClassAssLinkParent.getWords().append("NA|0| ");
								else 
									semClassAssLinkParent.getWords().append(assOverlap.getLinkParentOverlap() +assOverlap.getLinkCatOverlapId()+" ");
							}
							else {
								semClassAssLink.getWords().append(assLinkTitle + " |NA/0| ");
								try {
									//errorWriter.write("warning : Hyperlink page name mismatch in DB and parser for "+ wikiPageBean.getPageId()+" link " +assLinkTitle+"\n");
								} catch (Exception exp) {
									// ignore
								}
							}
						}
					} catch (Exception e) {
						try {
							errorWriter.write("Accessing internal link id : "
									+ wikiPageBean.getPageId() + " failed : "
									+ e.getMessage() + " " + e.getCause()
									+ "\n");
							e.printStackTrace();
						} catch (Exception exp) {
							exp.printStackTrace();
						}
					}

				}
			}
		}
		if(!imgTextSection.equals("")) {
			Matcher matcher = templatePattern.matcher(imgTextSection);
			imgTextSection = matcher.replaceAll("").trim();
			if(!imgTextSection.equals("")) {
				wikiSection.setImageText("/ " + wikiSection.getSectionName() +" / "+imgTextSection +" ");
			}
		}
		wikiPageBean.getSections().add(wikiSection);

		// set frequent phrases and co-occurrence
		String sectionText = section.getText();
		Matcher matcher = templatePattern.matcher(sectionText);
		sectionText = matcher.replaceAll("");
		//System.out.println("Text tem: " + sectionText);
		matcher = quotePattern.matcher(sectionText);
		sectionText = matcher.replaceAll("");
		//System.out.println("Text img : " + sectionText);
		matcher = tagPattern1.matcher(sectionText);
		sectionText = matcher.replaceAll("");
		matcher = tagPattern2.matcher(sectionText);
		sectionText = matcher.replaceAll("").replaceAll("_", " ").replaceAll(",", "").replaceAll(":", "");
		StringBuilder secCooccurrence = new StringBuilder();
		//System.out.println("Text cleaned : " + sectionText);
		
		String[] lines=sectionText.split("\\.");
		
		final List<String> phrases=new ArrayList<String>();
		
		for(int j=0;j<lines.length;j++){

			String[] words=lines[j].split(" ");
			final Map<String, Boolean> wordIsJunkMap = new HashMap<String, Boolean>();
			
			// Collect all undesired words
			for(int i=0;i<words.length;i++){

				matcher = nonWordpattern.matcher(words[i]);
				if (matcher.matches() || words[i].length() <=2) {
					wordIsJunkMap.put(words[i].trim(), true);
					if(words[i].indexOf("|")>=0) {
						final String[] pipSplits = words[i].split("\\|");
						for(int k=0;k<pipSplits.length;k++) {
							wordIsJunkMap.put(pipSplits[k].trim(), true);
						}
					}
					continue;
				}
				
				if(ExtractionUtil.getInstance(path).isMonth(words[i])
						|| StopwordRecognizer.getInstance().isStopWord(words[i])
						|| JUNK_WORDS.indexOf(words[i]) >= 0
						|| words[i].trim().indexOf("px") >= 0) {
					wordIsJunkMap.put(words[i].trim(), true);
					if(words[i].indexOf("|")>=0) {
						final String[] pipSplits = words[i].split("\\|");
						for(int k=0;k<pipSplits.length;k++) {
							wordIsJunkMap.put(pipSplits[k].trim(), true);
						}
					}
					continue;
				}
				wordIsJunkMap.put(words[i], false);
			}
			words = null;
			String[] taggedWords =TagText.getInstance(this.path).tagSentence(lines[j]).split(" ");

			List<String> wordList = new ArrayList<String>();
			List<String> posList = new ArrayList<String>();
			for(int i=0;i<taggedWords.length;i++){
				final String[] splits = taggedWords[i].split("/");
				if(splits.length <= 1) continue;
				wordList.add(splits[0].toLowerCase().trim());
				posList.add(splits[1]);
			}

			taggedWords = null;

			for(int i=0,size=wordList.size();i<size;i++){

				final String currentWord = wordList.get(i);
				final String currentPOS = posList.get(i);
				
				if(wordIsJunkMap.get(currentWord) == null || wordIsJunkMap.get(currentWord)) {
					continue;
				}

				// Add all words which might be frequent
				if((currentPOS.indexOf("VB") >= 0 ||currentPOS.indexOf("JJ") >= 0)
						&& wordIsJunkMap.get(currentWord) != null 
						&& !wordIsJunkMap.get(currentWord)){
					String phrase=currentWord;
					int h=i+1;
					if(h<size && posList.get(h).indexOf("NN")>=0 
							&& wordIsJunkMap.get(wordList.get(h)) != null 
							&& !wordIsJunkMap.get(wordList.get(h))){
						//phrases.add(phrase);
						phrase=phrase+" "+wordList.get(h);
						phrases.add(phrase);
						while(++h<size && posList.get(h).indexOf("NN")>=0
								&& wordIsJunkMap.get(wordList.get(h)) != null 
								&& !wordIsJunkMap.get(wordList.get(h))){
							phrase=phrase+" "+wordList.get(h);
							phrases.add(phrase);
						}
					}
				}
				else if(currentPOS.indexOf("NN") >= 0
						&& wordIsJunkMap.get(currentWord) != null 
						&& !wordIsJunkMap.get(currentWord)){
					String phrase=currentWord;
					int h=i;
					phrases.add(phrase);
					while(++h<size &&  posList.get(h).indexOf("NN")>=0
							&& wordIsJunkMap.get(wordList.get(h)) != null 
							&& !wordIsJunkMap.get(wordList.get(h))){
						phrase=phrase+" "+wordList.get(h);
						phrases.add(phrase);
					}
				}
				
				
//				// Co-occurrence starts here
				if(titleWordsToCompare.indexOf(currentWord) >= 0){
					String cooccurence = null;
					if(i!=0 && posList.get(i-1).indexOf("JJ")>=0
							&& wordIsJunkMap.get(wordList.get(i-1)) != null && !wordIsJunkMap.get(wordList.get(i-1))){
							cooccurence = wordList.get(i-1) +" "+currentWord;
							int h=i-2;
							while(h>=0 && posList.get(h).indexOf("JJ")>=0
									&& wordIsJunkMap.get(wordList.get(h)) != null 
									&& !wordIsJunkMap.get(wordList.get(h))){
								cooccurence= wordList.get(h) +" "+ cooccurence;
								--h;
							} 
					} else if(i!=0 && posList.get(i-1).indexOf("NN")>=0
							&& wordIsJunkMap.get(wordList.get(i-1)) != null && !wordIsJunkMap.get(wordList.get(i-1))){
							cooccurence = wordList.get(i-1) +" "+currentWord;
							int h=i-2;
							while(h>=0 && (posList.get(h).indexOf("JJ")>=0
									|| posList.get(h).indexOf("NN")>=0)
									&& wordIsJunkMap.get(wordList.get(h)) != null && !wordIsJunkMap.get(wordList.get(h))){
								cooccurence= wordList.get(h) +" "+ cooccurence;
								--h;
							}
					}

					if((i+1)<=size-1 && posList.get(i+1).indexOf("NN")>=0
							&& wordIsJunkMap.get(wordList.get(i+1)) != null && !wordIsJunkMap.get(wordList.get(i+1))){
						if(cooccurence != null) {
							cooccurence = cooccurence+" "+wordList.get(i+1);
						} else {
							cooccurence = currentWord +" "+wordList.get(i+1);
						}
						int h=i+1;
						while(++h<size && posList.get(h).indexOf("NN")>=0
								&& wordIsJunkMap.get(wordList.get(h)) != null && !wordIsJunkMap.get(wordList.get(h))){
							cooccurence=cooccurence+" "+wordList.get(h);
						}
					}
					if(cooccurence != null) {
						if(secCooccurrence.indexOf(cooccurence) < 0) {
							secCooccurrence.append(cooccurence +" | ");
						}
					}
				}
			}
		}
		
		// Add co-occurrence
		if(secCooccurrence.length() > 2) {
			semClassCooccurrence.getWords().append(secCooccurrence);
		}

//		for(int t=0;t<phrases.size();t++) {
//			System.out.print(phrases.get(t) +" | ");
//		}
//		System.out.println("******");
		
		// Add phrase
		final List<String> tempPhraseList = new ArrayList<String>();
		for(int m=0,phraseSize=phrases.size();m<phraseSize;m++){
			final String phrase = phrases.get(m);
			int cnt=Collections.frequency(phrases, phrase);
			final List<Integer> toRemoveList = new ArrayList<Integer>();
			boolean canAdd = true;
			if(cnt>=2) {
				for(int n=0,tempSize=tempPhraseList.size();n<tempSize;n++){
					final String temPhrase = tempPhraseList.get(n);
					// Dont add substring
					//if(temPhrase.length() != phrase.length()) {
					if(phrase.indexOf(temPhrase) >=0 && !phrase.equals(temPhrase)) {
						toRemoveList.add(n);
					}
					if((canAdd && temPhrase.indexOf(phrase) >=0) || titleWordsToCompare.indexOf(phrase) >= 0) {
						canAdd = false;
						//break;
					}
				//}
				}
				if(toRemoveList.size()>0) {
					for(int n=0,toRmSize=toRemoveList.size();n<toRmSize;n++){
						tempPhraseList.remove(toRemoveList.get(n));
					}
				} 
				if(canAdd)
					tempPhraseList.add(phrase);
			}
		}

		// Add long phrases as well
		//System.out.println("Sec Name : " + wikiSection.getSectionName());
		Set<String> tempSet = new HashSet<String>();
		tempSet.addAll(phrases);
		List<String> toRemove = new ArrayList<String>();
		Set<String> tempSet2 = new HashSet<String>();
		tempSet2.addAll(tempSet);
		Iterator<String> temSetItr = tempSet.iterator();
		while(temSetItr.hasNext()) {
			final String toAdd1 = temSetItr.next();
			
			final Iterator<String> temSet2Itr = tempSet2.iterator();
			while(temSet2Itr.hasNext()) {
				final String toAdd2 = temSet2Itr.next();
				if(toAdd2.indexOf(toAdd1) >= 0 && !toAdd2.equals(toAdd1)) {
					toRemove.add(toAdd1);
				}
			}
		}
		tempSet2 = null;
		temSetItr = toRemove.iterator();
		while(temSetItr.hasNext()) {
			tempSet.remove(temSetItr.next());
		}
		toRemove = null;
		
		StringBuilder builder = new StringBuilder();
		for(int m=0,phraseSize=tempPhraseList.size();m<phraseSize;m++){
			final String frePhrase = tempPhraseList.get(m);
			semClassFrequent.getWords().append(frePhrase+" |"+Collections.frequency(phrases,
					frePhrase)+"| ");
			
			final Iterator<String> temItr = tempSet.iterator();
			while(temItr.hasNext()) {
				final String toAdd = temItr.next();
				if(!tempPhraseList.contains(toAdd)
						&& toAdd.indexOf(frePhrase) >=0
						&& toAdd.split(" ").length > 1) {
					
					if(builder.indexOf(toAdd) < 0) {
						//System.out.println("New phrases : " +toAdd);
					builder.append(toAdd.trim()+" |"+Collections.frequency(phrases,
							toAdd)+"| ");
					}
				}
			}
			
		}
		
		semClassFrequent.getWords().append(builder);
	}

	private String getAllTitleRelatedWords(String title, PorterStemmer stemmer, String synonym) {
		
		String[] titleRelatedWords = title.toLowerCase().trim().split(" ");
		String toCompare = "";
		boolean canAdd = true;
		for(int j=0;j<titleRelatedWords.length;j++) {
			canAdd = true;
			for(int i=0;i<IndexingConstants.TITLE_STOP_WORDS.length;i++) {
				if(titleRelatedWords[j].indexOf(IndexingConstants.TITLE_STOP_WORDS[i]) >= 0) {
					canAdd = false;
					break;
				}
			}
			if(canAdd)
				toCompare = toCompare+" " + titleRelatedWords[j];
		}
		toCompare = toCompare +" " + ExtractionUtil.getInstance(path).getRootFormStrings(toCompare, stemmer);
		
		if(synonym.length() >= 2) {
//			String[] splits = synonym.split(" \\| ");
//			for(int i=0;i<splits.length;i++) {
//				System.out.println(splits[i]);
//			}
			toCompare = toCompare +" "+ synonym.replaceAll(" \\| ", " ");
			for(int i=0;i<IndexingConstants.TITLE_STOP_WORDS.length;i++) {
				if(toCompare.indexOf(IndexingConstants.TITLE_STOP_WORDS[i]) >= 0) {
					toCompare = toCompare.replaceAll(IndexingConstants.TITLE_STOP_WORDS[i], "");
				}
			}
		}
		return toCompare;
	}

	
}
//
///*
// 	private String setExternal(Section section, WikiPageBean wikiPageBean,
//			BufferedWriter errorWriter) {
//		List<Link> links = section.getLinks(Link.type.EXTERNAL);
//		StringBuilder externString = new StringBuilder();
//		TagText tt= TagText.getInstance();
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
//					tagged=tt.tagSentence(link.toString());	
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
//							interpretation.append(tags[k]+" ");
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
//	}
// 
//*/
///*
// Target http://www.yourdictionary.com/notebook
//ToString LI_TYPE: EXTERNAL
//LI_TARGET: "http://www.yourdictionary.com/notebook"
//LI_TEXT: ""A small, lightweight laptop computer in full notebook computer"."
//LI_POSITION: "(19, 84)"
//LI_PARAMETERS: 0
//Text "A small, lightweight laptop computer in full notebook computer".
//Target http://dictionary.reference.com/browse/notebook+?o=100074
//ToString LI_TYPE: EXTERNAL
//LI_TARGET: "http://dictionary.reference.com/browse/notebook+?o=100074"
//LI_TEXT: ""A small, lightweight laptop computer." Retrieved 16 November 2010."
//LI_POSITION: "(112, 179)"
//LI_PARAMETERS: 0
//Text "A small, lightweight laptop computer." Retrieved 16 November 2010.
//Target http://www.thefreedictionary.com/notebook
//ToString LI_TYPE: EXTERNAL
//LI_TARGET: "http://www.thefreedictionary.com/notebook"
//LI_TEXT: ""2. A light, portable computer that is generally thinner than a laptop." Retrieved 16 November 2010."
//LI_POSITION: "(218, 318)"
//LI_PARAMETERS: 0
//Text "2. A light, portable computer that is generally thinner than a laptop." Retrieved 16 November 2010.
//Target http://www.laptopsguru.com/how-to-buy-a-laptop/
//ToString LI_TYPE: EXTERNAL
//LI_TARGET: "http://www.laptopsguru.com/how-to-buy-a-laptop/"
//LI_TEXT: ""How to Buy Laptop Computers". Retrieved 16 November 2010."
//LI_POSITION: "(318, 376)"
//LI_PARAMETERS: 0
//Text "How to Buy Laptop Computers". Retrieved 16 November 2010.
//Target http://www.webopedia.com/TERM/l/laptop_computer.html
//ToString LI_TYPE: EXTERNAL
//LI_TARGET: "http://www.webopedia.com/TERM/l/laptop_computer.html"
//LI_TEXT: "What is a laptop computer"
//LI_POSITION: "(376, 401)"
//LI_PARAMETERS: 0
//Text What is a laptop computer
//Target http://knowledge.wharton.upenn.edu/article.cfm?articleid=2107
//ToString LI_TYPE: EXTERNAL
//LI_TARGET: "http://knowledge.wharton.upenn.edu/article.cfm?articleid=2107"
//LI_TEXT: "The Net Impact of Netbooks? It Depends on Who Uses Them for What"
//LI_POSITION: "(142, 206)"
//LI_PARAMETERS: 0
//Text The Net Impact of Netbooks? It Depends on Who Uses Them for What
//Target http://www.elitezoom.com/umid-netbook-only-48-display.html
//ToString LI_TYPE: EXTERNAL
//LI_TARGET: "http://www.elitezoom.com/umid-netbook-only-48-display.html"
//LI_TEXT: "UMID Netbook Only 4.8″"
//LI_POSITION: "(664, 686)"
//LI_PARAMETERS: 0
//Text UMID Netbook Only 4.8″
//Target http://apcmag.com/scoop_we_review_the_inspiron_mini_12__dells_supersized_yet_superslim_12_inch_netbook.htm
//ToString LI_TYPE: EXTERNAL
//LI_TARGET: "http://apcmag.com/scoop_we_review_the_inspiron_mini_12__dells_supersized_yet_superslim_12_inch_netbook.htm"
//LI_TEXT: "Worlds First  review of Inspiron Mini 12: Dell’s super-slim netbook!"
//LI_POSITION: "(698, 766)"
//LI_PARAMETERS: 0
//Text Worlds First  review of Inspiron Mini 12: Dell’s super-slim netbook!
//Target http://lenovoblogs.com/insidethebox/?p=154
//ToString LI_TYPE: EXTERNAL
//LI_TARGET: "http://lenovoblogs.com/insidethebox/?p=154"
//LI_TEXT: "Inside the Box » Switchable Graphics"
//LI_POSITION: "(323, 359)"
//LI_PARAMETERS: 0
//Text Inside the Box » Switchable Graphics
//Target http://www.engadget.com/2009/07/14/rock-delivers-bd-core-i7-equipped-xtreme-790-and-xtreme-840-ga/
//ToString LI_TYPE: EXTERNAL
//LI_TARGET: "http://www.engadget.com/2009/07/14/rock-delivers-bd-core-i7-equipped-xtreme-790-and-xtreme-840-ga/"
//LI_TEXT: "Rock delivers BD / Core i7-equipped Xtreme 790 and Xtreme 840 gaming laptops - Engadget"
//LI_POSITION: "(90, 177)"
//LI_PARAMETERS: 0
//Text Rock delivers BD / Core i7-equipped Xtreme 790 and Xtreme 840 gaming laptops - Engadget
//Target http://en.wikipedia.org/wiki/Docking_station#Computer_stands|stands
//ToString LI_TYPE: EXTERNAL
//LI_TARGET: "http://en.wikipedia.org/wiki/Docking_station#Computer_stands|stands"
//LI_TEXT: "[ ]"
//LI_POSITION: "(310, 313)"
//LI_PARAMETERS: 0
//Text [ ]
//Target http://www.philly.com/philly/education/20100727_A_lawyer_in_the_Lower_Merion_webcam_case_wants_to_be_paid_now.html
//ToString LI_TYPE: EXTERNAL
//LI_TARGET: "http://www.philly.com/philly/education/20100727_A_lawyer_in_the_Lower_Merion_webcam_case_wants_to_be_paid_now.html"
//LI_TEXT: ""A lawyer in the Lower Merion webcam case wants to be paid now""
//LI_POSITION: "(848, 911)"
//LI_PARAMETERS: 0
//Text "A lawyer in the Lower Merion webcam case wants to be paid now"
//Target http://www.eetimes.com/news/latest/showArticle.jhtml;jsessionid=YN1M2POYEMEN0QSNDLSCKHA?articleID=212701251
//ToString LI_TYPE: EXTERNAL
//LI_TARGET: "http://www.eetimes.com/news/latest/showArticle.jhtml;jsessionid=YN1M2POYEMEN0QSNDLSCKHA?articleID=212701251"
//LI_TEXT: "Analysis: Did Intel underestimate netbook success?"
//LI_POSITION: "(978, 1028)"
//LI_PARAMETERS: 0
//Text Analysis: Did Intel underestimate netbook success?
//Target http://www.isuppli.com/NewsDetail.aspx?ID=19823
//ToString LI_TYPE: EXTERNAL
//LI_TARGET: "http://www.isuppli.com/NewsDetail.aspx?ID=19823"
//LI_TEXT: "Notebook PC Shipments Exceed Desktops for First Time in Q3"
//LI_POSITION: "(1199, 1257)"
//LI_PARAMETERS: 0
//Text Notebook PC Shipments Exceed Desktops for First Time in Q3
//Target http://www.eweek.com/c/a/Windows/Netbooks-Are-Destroying-the-Laptop-Market-and-Microsoft-Needs-to-Act-Now-863307/
//ToString LI_TYPE: EXTERNAL
//LI_TARGET: "http://www.eweek.com/c/a/Windows/Netbooks-Are-Destroying-the-Laptop-Market-and-Microsoft-Needs-to-Act-Now-863307/"
//LI_TEXT: "Netbooks Are Destroying the Laptop Market and Microsoft Needs to Act Now"
//LI_POSITION: "(363, 435)"
//LI_PARAMETERS: 0
//Text Netbooks Are Destroying the Laptop Market and Microsoft Needs to Act Now
//Target http://bestlaptops2010.us
//ToString LI_TYPE: EXTERNAL
//LI_TARGET: "http://bestlaptops2010.us"
//LI_TEXT: "Best Laptops 2010"
//LI_POSITION: "(0, 17)"
//LI_PARAMETERS: 0
//Text Best Laptops 2010
// */
