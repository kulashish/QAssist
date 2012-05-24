package com.aneedo.indexing;


public class WikiPageExtractorOld {

//	static Wikipedia wiki = null;
//	private static WikiPageExtractorOld wikiPageExtract = null;;
//
//	private WikiPageExtractorOld() throws Exception {
//		DatabaseConfiguration dbConfig = new DatabaseConfiguration();
//		dbConfig.setHost("localhost");
//		dbConfig.setDatabase("wikipedia");
//		dbConfig.setUser("root");
//		dbConfig.setPassword("aneedo");
//		dbConfig.setLanguage(Language.english);
//		try {
//			wiki = new Wikipedia(dbConfig);
//		} catch (WikiInitializationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			throw new Exception("Mysql connection exception");
//		}
//	}
//	
//	private void getCleanText() {
//		MediaWikiParserFactory pf = new MediaWikiParserFactory();
//		pf.setTemplateParserClass( FlushTemplates.class );
//
//		MediaWikiParser parser = pf.createParser();
//		Page page = getPage(198584, null);
////		ParsedPage pp = parser.parse(page.getParsedPage().getSection(1).getText());
////		System.out.println(pp.getText());
//		List<Content> pageContent = page.getParsedPage().getSection(1).getContentList();
//		for(int i=0;i<pageContent.size();i++) {
//			System.out.println(pageContent.get(i).getText());
//			
//		}
//	}
//
//	public static WikiPageExtractorOld getIntance() throws Exception {
//		if(wikiPageExtract == null) {
//			synchronized (WikiPageExtractorOld.class) {
//				wikiPageExtract = new WikiPageExtractorOld();
//			}
//		}
//		return wikiPageExtract;
//	}
//
//	public void initWikiPageDetails(WikiPageBean wikiPageBean,
//			BufferedWriter errorWriter) {
//		int pageId = wikiPageBean.getPageId();
//		Page page = getPage(pageId, errorWriter);
//		if (page != null) {
//
//			// Page title
//			try {
//
//				String wikiTitle = page.getTitle().getWikiStyleTitle();
//				int disTextStart = wikiTitle.indexOf("(");
//				String plainTitle = page.getTitle().getPlainTitle();
//				if (disTextStart >= 0) {
//					wikiTitle = wikiTitle.substring(disTextStart + 1)
//							.replaceAll(")", "").trim();
//					plainTitle = plainTitle.substring(0,
//							plainTitle.indexOf(wikiTitle)).trim();
//				} else {
//					wikiTitle = null;
//				}
//
//				wikiPageBean.setPageTitle(plainTitle);
//
//				// disambiguation text
//				if (wikiTitle != null) {
//					wikiPageBean.setTitleDisambText(wikiTitle);
//				}
//
//			
//
//			PorterStemmer stemmer = new PorterStemmer();
//
//			List<Section> sectionList = page.getParsedPage().getSections();
//			
//			// abstract
//			Section abstractSection = sectionList.get(0);
//			String freqWords = null;
//			String firstPara = abstractSection.getParagraph(1).getText();
//			wikiPageBean.setAbstractText(abstractSection.getParagraph(0)
//					.getText()
//					+ " " + firstPara);
//			freqWords = TermFreqBasedTrimmer.getInstance().getFreqWords(abstractSection.getText(), stemmer,plainTitle);
//			if(freqWords != null && freqWords.length() > 5) {
//				wikiPageBean.getSemClassMap().get(IndexingConstants.SEM_CLASS_FREQUENT).getWords().append(freqWords);
//			}
//
//			// Set type/hypernym of the page from first para
//			setPageType(wikiPageBean, firstPara);
//
//			// set info box details
//			setDetailsFromInfoBox(page, wikiPageBean);
//
//			// Set other sections, from lst three see also starts
//			// TODO sub section wise storage
//			for (int j = 0, size = sectionList.size() - 3; j < size; j++) {
//				setSectionDetails(sectionList.get(j), wikiPageBean,
//						IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK,
//						errorWriter);
//				freqWords = TermFreqBasedTrimmer.getInstance().getFreqWords(sectionList.get(j).getText(), stemmer,plainTitle);
//				if(freqWords != null && freqWords.length() > 5) {
//					wikiPageBean.getSemClassMap().get(IndexingConstants.SEM_CLASS_FREQUENT).getWords().append(freqWords);
//				}
//			}
//			
//			// set synonyms
//			setSynonyms(page, wikiPageBean, abstractSection.getParagraph(0)
//					.getText()
//					+ " " + firstPara);
//
//			// Select category and pages
//			selectCategoriesPages(wikiPageBean,page,errorWriter,stemmer);
//
//			// set see also and  External Links
//			setRelated(wikiPageBean, page, errorWriter);
//			
//			// set homonym
//			setHomonyms(page, wikiPageBean, errorWriter);
//			
//			// Add page type to hypernym
//			for(int j=0,size=wikiPageBean.getPageTypes().size();j<size;j++) {
//				wikiPageBean.getSemClassMap().get(IndexingConstants.SEM_CLASS_HYPERNYM).getWords()
//				.append(wikiPageBean.getPageTypes().get(j));
//			}
//			} catch (Exception e) {
//				try {
//					errorWriter.write("Accessing page title id : " + pageId
//							+ " failed : " + e.getMessage() + " "
//							+ e.getCause() + "\n");
//				} catch (Exception exp) {
//					exp.printStackTrace();
//				}
//			}
//		}
//			
//	}
//
//	private void setRelated(WikiPageBean wikiPageBean, Page page, BufferedWriter errorWriter) {
//		List<Section> sections = page.getParsedPage().getSections();
//		if (sections != null) {
//			int size = sections.size() - 1;
//			String secTitle = null;
//			int index = -1 ; 
//			int exIndex = -1 ;
//			for (int i = size; i >= 0; i--) {
//				secTitle = sections.get(i).getTitle();
//				if (secTitle != null) {
//					secTitle = secTitle.toLowerCase();
//					
//					if ((secTitle.indexOf("see") > 0)
//							&& (secTitle.indexOf("also") > 0)) {
//						index = i;
//						break;
//					} 
//					
//						if ((secTitle.indexOf("reference") > 0)
//								|| ((secTitle.indexOf("external") > 0)
//										&& (secTitle.indexOf("link") > 0))
//										|| (secTitle.indexOf("note") > 0) ) {
//							exIndex = i;
//							continue;
//						} 
//					}
//				}
//			
//			
//			if(index != -1) {
//				
//				setExternalRelated(sections.get(index), wikiPageBean, IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED, 
//						errorWriter, Link.type.INTERNAL);
//			}
//			if(exIndex != -1) {
//				
//				setExternalRelated(sections.get(exIndex), wikiPageBean, IndexingConstants.SEM_CLASS_REFERENCE, 
//						errorWriter, Link.type.EXTERNAL);
//
//			}
//		}
//	}
//	private void selectCategoriesPages(WikiPageBean wikiPageBean, Page page,
//			BufferedWriter errorWriter, PorterStemmer stemmer) {
//		Set<Category> categories = page.getCategories();
//		Iterator<Category> categoriesIterator = categories.iterator();
//		QueryProcessingUtil util = QueryProcessingUtil.getInstance();
//		Category category = null;
//		String catName = null;
//		Map<String, SemanticClass> semClassMap = wikiPageBean.getSemClassMap();
//		String pageTitle = wikiPageBean.getPageTitle();
//		String[] catNameSplits = null;
//		String[] titleSplits = null;
//
//		List<String> toCompareList = new ArrayList<String>();
//		toCompareList.add(pageTitle);
//		toCompareList.addAll(wikiPageBean.getPageTypes());
//		String[] splits = semClassMap.get(IndexingConstants.SEM_CLASS_HYPERNYM)
//				.getWords().toString().split(" ");
//		toCompareList.addAll(Arrays.asList(splits));
//		splits = semClassMap.get(IndexingConstants.SEM_CLASS_ROLE).getWords()
//				.toString().split(" ");
//		toCompareList.addAll(Arrays.asList(splits));
//		splits = semClassMap.get(IndexingConstants.SEM_CLASS_SYNONYM)
//				.getWords().toString().split(" ");
//		toCompareList.addAll(Arrays.asList(splits));
//
//		while (categoriesIterator.hasNext()) {
//			category = categoriesIterator.next();
//			try {
//				catName = category.getTitle().getPlainTitle();
//
//				// If number is there ignore that category
//				if (!util.canIncludeCategory(catName)) {
//					continue;
//				}
//
//				// If both are having the same number of words and catname is
//				// plural of page title
//				// Include its sub catgories
//				catNameSplits = stemmer.stemStringWithSpace(catName).split(" ");
//				titleSplits = stemmer.stemStringWithSpace(pageTitle).split(" ");
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
//						selectSubCategoryPages(category, stemmer,
//								toCompareList, semClassMap, errorWriter);
//						continue;
//					}
//				}
//
//				// Select only those which are having high similarity measure
//				if (util.isJaccardSimHigh(toCompareList, catName, stemmer)) {
//					semClassMap.get(IndexingConstants.SEM_CLASS_HYPERNYM)
//							.getWords().append(catName + " ");
//					toCompareList.add(catName);
//					selectSubCategoryPages(category, stemmer, toCompareList,
//							semClassMap, errorWriter);
//					toCompareList.remove(catName);
//				}
//
//			} catch (Exception e) {
//				try {
//					errorWriter.write("Accessing category details of : "
//							+ wikiPageBean.getPageId() + " failed : "
//							+ e.getMessage() + " " + e.getCause()
//							+ "\n");
//				} catch (Exception exp) {
//					exp.printStackTrace();
//				}
//			}
//
//		}
//
//	}
//
//	private void selectSubCategoryPages(Category category,
//			PorterStemmer stemmer, List<String> toCompareList,
//			Map<String, SemanticClass> semClassMap, BufferedWriter errorWriter) {
//
//		// Add category
//		Set<Category> children = category.getChildren();
//		
//		// most likely hitting undesired category
//		if(children.size() > 50) {
//			return;
//		}
//		Iterator<Category> itr = children.iterator();
//		String pageTitle = toCompareList.get(0);
//		String name = null;
//		String stemName = null;
//		while (itr.hasNext()) {
//			try {
//				name = itr.next().getTitle().getPlainTitle();
//				if (isValidCatName(name,pageTitle)) {
//					stemName = stemmer.stemStringWithSpace(name);
//					if (stemName.startsWith(pageTitle)) {
//						semClassMap
//								.get(
//										IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED_HIERARCHY)
//								.getWords().append(name + " ");
//					} else if (stemName.endsWith(pageTitle)) {
//						semClassMap.get(IndexingConstants.SEM_CLASS_HYPONYM)
//								.getWords().append(name + " ");
//					} else if (QueryProcessingUtil.getInstance().isAllMatch(
//							toCompareList, name, stemmer)) {
//						semClassMap
//								.get(
//										IndexingConstants.SEM_CLASS_SIBLING)
//								.getWords().append(name + " ");
//					}
//				}
//			} catch (Exception e) {
//				try {
//					errorWriter.write("Accessing sub category details of : "
//							+ pageTitle + " failed : " + e.getMessage() + " "
//							+ e.getCause() + "\n");
//				} catch (Exception exp) {
//					exp.printStackTrace();
//				}
//			}
//		}
//
//		// Add pages
//		try {
//			Set<Page> pages = category.getArticles();
//			Iterator<Page> pageItr = pages.iterator();
//			
//			while (pageItr.hasNext()) {
//				try {
//					name = pageItr.next().getTitle().getPlainTitle();
//					if (isValidCatName(name,pageTitle)) {
//						stemName = stemmer.stemStringWithSpace(name);
//						if (stemName.startsWith(pageTitle)) {
//							semClassMap
//									.get(
//											IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED_HIERARCHY)
//									.getWords().append(name + " ");
//						} else if (stemName.endsWith(pageTitle)) {
//							semClassMap
//									.get(IndexingConstants.SEM_CLASS_HYPONYM)
//									.getWords().append(name + " ");
//						} else if (QueryProcessingUtil.getInstance()
//								.isAllMatch(toCompareList, name, stemmer)) {
//							semClassMap
//									.get(
//											IndexingConstants.SEM_CLASS_SIBLING)
//									.getWords().append(name + " ");
//						}
//					}
//				} catch (Exception e) {
//					try {
//						errorWriter.write("Accessing  page details of : "
//								+ pageTitle + " failed : " + e.getMessage()
//								+ " " + e.getCause() + "\n");
//					} catch (Exception exp) {
//						exp.printStackTrace();
//					}
//				}
//			}
//		} catch (Exception e) {
//			try {
//				errorWriter.write("Accessing pages details of : " + pageTitle
//						+ " failed : " + e.getMessage() + " "
//						+ e.getCause() + "\n");
//			} catch (Exception exp) {
//				exp.printStackTrace();
//			}
//		}
//	}
//
//	private boolean isValidCatName(String catName, String pageTitle) {
//
//		if(catName.equals(pageTitle)) {
//			return false;
//		}
//		String[] splits = catName.split(" ");
//
//		// No category length more than 3
//		if (splits.length > 3) {
//			return false;
//		}
//
//		// no stop words in it
//		StopwordRecognizer stRecognizer = StopwordRecognizer.getInstance();
//		if (stRecognizer.isStopWordForType(catName)) {
//			return false;
//		}
//		return true;
//	}
//
//	private void setDetailsFromInfoBox(Page page, WikiPageBean wikiPageBean) {
//		List<Template> templates = page.getParsedPage().getParagraph(0)
//				.getTemplates(1, 3);
//		Template template;
//		String infoBoxKey = QueryProcessingUtil.getInstance().getInfoBoxKeys();
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
//					key = parameter_words[0].trim().toLowerCase();
//					if (infoBoxKey.indexOf(key) >= 0) {
//						if (key.indexOf("name") >= 0) {
//							semClass.get(IndexingConstants.SEM_CLASS_SYNONYM)
//									.getWords().append(
//											parameter_words[1].replace("[", "")
//													.replace("]", "").replace(
//															"<br>", ""));
//						} else if (key.indexOf("product") >= 0) {
//							semClass.get(IndexingConstants.SEM_CLASS_MERONYM)
//									.getWords().append(
//											parameter_words[1].replace("[", "")
//													.replace("]", "").replace(
//															"<br>", ""));
//						} else if (key.indexOf("type") >= 0
//								|| key.indexOf("industry") >= 0) {
//							semClass.get(IndexingConstants.SEM_CLASS_HYPERNYM)
//									.getWords().append(
//											parameter_words[1].replace("[", "")
//													.replace("]", "").replace(
//															"<br>", ""));
//						} else if (key.indexOf("known for") >= 0
//								|| key.indexOf("occupation") >= 0
//								|| key.indexOf("profession") >= 0
//								|| key.indexOf("role") >= 0) {
//							semClass.get(IndexingConstants.SEM_CLASS_ROLE)
//									.getWords().append(
//											parameter_words[1].replace("[", "")
//													.replace("]", "").replace(
//															"<br>", ""));
//
//						} else if (key.indexOf("develop") >= 0
//								|| key.indexOf("manufact") >= 0) {
//							semClass.get(IndexingConstants.SEM_CLASS_MAKEPRODUCE)
//									.getWords().append(
//											parameter_words[1].replace("[", "")
//													.replace("]", "").replace(
//															"<br>", ""));
//						}
//					}
//				}
//			}
//		}
//	}
//
//	private void setHomonyms(Page page, WikiPageBean wikiPageBean,
//			BufferedWriter errorWriter) {
//		if (!page.isDisambiguation()) {
//			try {
//				Page disambPage = wiki.getPage(wikiPageBean.getPageTitle()
//						+ "_(disambiguation)");
//				if (disambPage != null) {
//					List<Link> links = disambPage.getParsedPage().getSection(0)
//							.getLinks(Link.type.INTERNAL);
//					Link link = null;
//					Page linkPage = null;
//					SemanticClass semClass = wikiPageBean.getSemClassMap().get(
//							IndexingConstants.SEM_CLASS_HOMONYM);
//					for (int i = 0, size = links.size(); i < size; i++) {
//						link = links.get(i);
//						try{
//						linkPage = wiki.getPage(link.getTarget());
//						semClass.getWords().append(
//								linkPage.getTitle().getPlainTitle() + " ");
//						}catch (Exception e) {
//							try {
//								errorWriter.write("Accessing disambiguation details of : "
//										+ wikiPageBean.getPageId() + " failed : "
//										+ e.getMessage() + " " + e.getCause()
//										+ "\n");
//							} catch (Exception exp) {
//								exp.printStackTrace();
//							}
//						}
//					}
//				}
//			} catch (Exception e) {
//				try {
//					errorWriter.write("Accessing disambiguation details of : "
//							+ wikiPageBean.getPageId() + " failed : "
//							+ e.getMessage() + " " + e.getCause()
//							+ "\n");
//				} catch (Exception exp) {
//					exp.printStackTrace();
//				}
//			}
//		}
//	}
//
//	private void setSynonyms(Page page, WikiPageBean wikiPageBean, String text) {
//		// redirects
//		Set<String> redirects = page.getRedirects();
//		SemanticClass semClass = wikiPageBean.getSemClassMap().get(
//				IndexingConstants.SEM_CLASS_SYNONYM);
//		Iterator<String> redirectsIterator = redirects.iterator();
//		while (redirectsIterator.hasNext()) {
//			semClass.getWords().append(redirectsIterator.next() + " ");
//		}
//
//		// synonyms
//		semClass.getWords().append(
//				QueryProcessingUtil.getInstance().getSynonyms(
//						wikiPageBean.getPageTitle()
//						, new StringBuilder())+ " ");
//
//		// extract from first paragraph
//		int typePos = QueryProcessingUtil.getInstance()
//				.getPosAfterPatternSynonym(text);
//		if (typePos >= 0) {
//			String syn = getIdentifiedWord(text, typePos);
//			if (syn != null)
//				semClass.getWords().append(syn + " ");
//		}
//
//	}
//
//	private void setPageType(WikiPageBean wikiPageBean, String firstPara) {
//		int typePos = QueryProcessingUtil.getInstance().getPosAfterPattern(
//				firstPara);
//		if (typePos >= 0) {
//			String pageType = getIdentifiedWord(firstPara, typePos);
//			if (pageType != null)
//				wikiPageBean.getPageTypes().add(pageType);
//		}
//	}
//
//	private String getIdentifiedWord(String firstPara, int startingPos) {
//		firstPara = firstPara.substring(startingPos + 1);
//		String[] splits = firstPara.split(" ");
//		StopwordRecognizer stopWordRecog = StopwordRecognizer.getInstance();
//		StringBuilder builder = new StringBuilder();
//		for (int i = 0; i < splits.length; i++) {
//			if (stopWordRecog.isStopWordForType(splits[i])) {
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
//					.getSemClassMap();
//			SemanticClass semClass = semClassMap.get(semanticClassName);
//			for (int i = 0, size = links.size(); i < size; i++) {
//				link = links.get(i);
//				if (semClass != null) {
//					try {
//						semClass.getWords().append(link.getText() + " ");
//					} catch (Exception e) {
//						try {
//							errorWriter.write("Accessing link id : "
//									+ wikiPageBean.getPageId() + " failed : "
//									+ e.getMessage() + " "
//									+ e.getCause() + "\n");
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
//		wikiSection.setSectionName(section.getTitle());
//		List<Link> links = section.getLinks(Link.type.INTERNAL);
//
//		if (links != null) {
//			Link link = null;
//			WikiLink wikiLink = null;
//			Map<String, SemanticClass> semClassMap = wikiPageBean
//					.getSemClassMap();
//			SemanticClass semClass = semClassMap.get(semanticClassName);
//			Page linkPage = null;
//			for (int i = 0, size = links.size(); i < size; i++) {
//				link = links.get(i);
//				wikiLink = new WikiLink(link.getText(), link.getContext(3, 3));
//				wikiSection.getInternalLinks().add(wikiLink);
//
//				if (semClass != null) {
//					try {
//						linkPage = wiki.getPage(link.getTarget());
//						semClass.getWords().append(
//								linkPage.getTitle().getPlainTitle() + " ");
//					} catch (Exception e) {
//						try {
//							errorWriter.write("Accessing link id : "
//									+ wikiPageBean.getPageId() + " failed : "
//									+ e.getMessage() + " "
//									+ e.getCause() + "\n");
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
//	private Page getPage(int pageId, BufferedWriter errorWriter) {
//		Page page = null;
//		try {
//			page = wiki.getPage(pageId);
//		} catch (Exception e) {
//			try {
//				errorWriter.write("Accessing page with id : " + pageId
//						+ " failed : " + e.getMessage() + " "
//						+ e.getCause() + "\n");
//			} catch (Exception exp) {
//				exp.printStackTrace();
//			}
//		}
//		return page;
//	}
//
//
//	public static void main(String[] args) throws Exception {
//		WikiPageExtractorOld wikiPageExtractor = WikiPageExtractorOld.getIntance();
//		wikiPageExtract.getCleanText();
//		/*BufferedWriter errorWriter;
//		try {
//			errorWriter = new BufferedWriter(new OutputStreamWriter(
//					new FileOutputStream("ErrorFile.log"), "UTF8"));
//		
//		WikiPageBean pageBean = new WikiPageBean();
//		pageBean.setPageId(198584);
//		wikiPageExtractor.initWikiPageDetails(pageBean, errorWriter);
//		
//		System.out.println("Title" + pageBean.getPageTitle());
//		System.out.println(pageBean.getPageId());
//		System.out.println(pageBean.getAbstractText());
//		System.out.println(pageBean.getTitleDisambText());
//		
//		List<WikiSection> wikiSections = pageBean.getSections();
//		WikiSection section = null;
//		List<WikiLink> wikiLinks = null;
//		WikiLink wikiLink = null;
//		System.out.println("Section details ***");
//		for (int i = 0, size = wikiSections.size(); i < size; i++) {
//			section = wikiSections.get(i);
//			System.out.println(section.getSectionName());
//			wikiLinks = section.getInternalLinks();
//
//			if (wikiLinks != null) {
//				for (int j = 0, linkSize = wikiLinks.size(); j < linkSize; j++) {
//					wikiLink = wikiLinks.get(j);
//					System.out.println(wikiLink.getLinkText());
//					System.out.println(wikiLink.getLinkText());
//				}
//			}
//
//		}
//		
//		System.out.println("Semantic classes ..........");
//		
//		Map<String, SemanticClass> semClassMap = pageBean.getSemClassMap();
//		System.out.println(semClassMap.get(
//				IndexingConstants.SEM_CLASS_SYNONYM).getWords().toString());
//		System.out.println(semClassMap.get(
//				IndexingConstants.SEM_CLASS_HOMONYM).getWords().toString());
//		System.out.println(semClassMap.get(
//				IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK).getWords()
//				.toString());
//		System.out.println(semClassMap.get(
//				IndexingConstants.SEM_CLASS_ASSOCIATION_EXLINK).getWords()
//				.toString());
//		System.out.println(semClassMap.get(
//				IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED).getWords()
//				.toString());
//		System.out.println(semClassMap.get(
//				IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED_HIERARCHY)
//				.getWords().toString());
//		System.out.println(semClassMap.get(
//						IndexingConstants.SEM_CLASS_FREQUENT).getWords()
//						.toString());
//		System.out.println(semClassMap.get(
//						IndexingConstants.SEM_CLASS_HYPERNYM).getWords()
//						.toString());
//		System.out.println(semClassMap.get(
//				IndexingConstants.SEM_CLASS_HYPONYM).getWords().toString());
//		System.out.println(semClassMap.get(
//				IndexingConstants.SEM_CLASS_ROLE).getWords().toString());
//		System.out.println(semClassMap.get(
//				IndexingConstants.SEM_CLASS_MERONYM).getWords().toString());
//		System.out.println(semClassMap.get(
//				IndexingConstants.SEM_CLASS_SIBLING).getWords().toString());
//
//		
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}*/
//	}

}
