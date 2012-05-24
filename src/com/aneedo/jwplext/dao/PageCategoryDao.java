package com.aneedo.jwplext.dao;

import java.io.BufferedWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.aneedo.jwplext.JwplSQLConstants;
import com.aneedo.jwplext.JwplUtil;
import com.aneedo.util.ExtractionUtil;
import com.aneedo.util.PorterStemmer;

public class PageCategoryDao {

	private static PageCategoryDao pageDao = new PageCategoryDao();

	public static PageCategoryDao getInstance() {
		return pageDao;
	}

	public StringBuilder[] getFilteredSubCatArticles(Map<Integer, String> catMap,
			String categoryIds, Connection conn, BufferedWriter errorWriter,
			PorterStemmer stemmer, List<String> toCompareList, String assToCompare, String synonymPath) {
		PreparedStatement semClassStmt = null;
		//System.out.println("Category Ids...." + categoryIds);
		String[] stmts = new String[] {
				JwplSQLConstants.SELECT_SUB_CATEGORY + categoryIds + ")",
				JwplSQLConstants.SELECT_PAGES_OF_CATEGORY + categoryIds + ")" };
		ResultSet result = null;
		StringBuilder[] relations = new StringBuilder[] { new StringBuilder(),
				new StringBuilder(), new StringBuilder() };
		String pageTitle = toCompareList.get(0);
		Integer pageId = 0;
		Integer nextPageId = 0;
		String catName = null;
		//System.out.println("Filtering sub category amd pages ... ");
		for (int i = 0; i < stmts.length; i++) {
			try {
				semClassStmt = conn.prepareStatement(stmts[i]);
				result = semClassStmt.executeQuery();
				if (result != null) {
					while (result.next()) {
						nextPageId = result.getInt("pageId");
						if (pageId != nextPageId) {
							catName = catMap.get(pageId);
							if (catName != null) {
								toCompareList.remove(catName);
							}
							catName = catMap.get(nextPageId);
							if (catName != null) {
								toCompareList.add(catName);
							}
							pageId = nextPageId;
						}
						addValid(result.getString("name"), result.getInt("subpageId"),errorWriter,
								stemmer, toCompareList, pageTitle, relations, assToCompare, i, synonymPath);
					}
				}
				result.close();
				semClassStmt.close();
			} catch (Exception e) {
				try {
					errorWriter
							.write("PageCategoryDao : getFilteredSubCatArticles : Accessing sub categories/pages with id : "
									+ categoryIds + " failed : "
									+ e.getMessage() + " " + e.getCause()
									+ "\n");
					e.printStackTrace();
				} catch (Exception exp) {
					exp.printStackTrace();
				}
			}
		}
		
		return relations;
	}

	private void addValid(String name, Integer nameId, BufferedWriter errorWriter,
			PorterStemmer stemmer, List<String> toCompareList,
			String pageTitle, StringBuilder[] relationArr, String assToCompare, int type,String synonymPath) {
		
		try {
			String temp = name.replaceAll("_", " ").replace("(", "").replace(")", "").toLowerCase();
			//System.out.println("Sub cat/page name : " + temp);
			final String titleStem = ExtractionUtil.getInstance(synonymPath).getRootFormStrings(pageTitle, stemmer);
			
			final String plural = ExtractionUtil.getInstance(synonymPath).getPlural(pageTitle, stemmer);
			
			
			if (isValidCatName(temp, pageTitle)) {
				//Set<String> stemNameSet = stemmer.stemStringWithSpaceList(temp);
				//Iterator<String> itr = stemNameSet.iterator();
				//String stemName = null;
				//while (itr.hasNext()) {
					//stemName = itr.next();
				/*
				 * If plural, stem, page title prefix or suffix
				 */
					if (temp.startsWith(titleStem)
							|| titleStem.startsWith(temp)
							|| temp.startsWith(pageTitle)
							|| pageTitle.startsWith(temp)
							|| temp.startsWith(plural)
							|| plural.startsWith(temp)) {
						//System.out.println("Sub cat/page name start : " + temp);
						relationArr[0].append(temp + " |" +nameId+"/"+type+"| ");
					} else if (temp.endsWith(titleStem)
							|| titleStem.endsWith(temp)
							||temp.endsWith(pageTitle)
							|| pageTitle.endsWith(temp)
							||temp.endsWith(plural)
							|| plural.endsWith(temp)) {
						//System.out.println("Sub cat/page name end : " + temp);
						relationArr[1].append(temp + " |" +nameId+"/"+type+"| ");
					} else if (ExtractionUtil.getInstance(synonymPath).isJaccardSimHigh(
							toCompareList, temp, stemmer)) {
						//System.out.println("Sub cat/page name some match : " + temp);
						relationArr[2].append(temp + " |" +nameId+"/"+type+"| ");
					} else {
						if(assToCompare.indexOf(temp) >= 0 || temp.indexOf(assToCompare) >= 0)
							relationArr[2].append(temp + " |" +nameId+"/"+type+"| ");
					}
				//}
			}
		} catch (Exception e) {
			try {
				errorWriter
						.write("PageCategoryDao : addValid : Is valid check of sub category details of : "
								+ pageTitle + " failed : " + e.getMessage()
								+ " " + e.getCause() + "\n");
				e.printStackTrace();
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}
	}

	private boolean isValidCatName(String catName, String pageTitle) {

		if (catName.equalsIgnoreCase(pageTitle)) {
			return false;
		}
		return JwplUtil.isValidCatName(catName);
	}
}
