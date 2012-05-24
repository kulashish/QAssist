package com.aneedo.jwplext.dao;

import java.io.BufferedWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import com.aneedo.jwplext.JwplCategory;
import com.aneedo.jwplext.JwplPage;
import com.aneedo.jwplext.JwplSQLConstants;
import com.aneedo.jwplext.JwplUtil;

public class PageDao {
	
	
	private static PageDao pageDao = new PageDao();


	public static PageDao getInstance() {
		return pageDao;
	}

	public JwplPage getJwplPageDetails(int pageId, PreparedStatement pstmt,Connection conn, 
			BufferedWriter errorWriter) {
		ResultSet result = null;
		JwplPage jwplPage = null;
		Set<JwplCategory> jwplCatSet = new HashSet<JwplCategory>();
		try {
			pstmt.setInt(1, pageId);
			result = pstmt.executeQuery();
			boolean firstTime  = true;
			if(result != null) {
				JwplCategory jwplCat = null;
				
				while(result.next()) {
					if(firstTime) {
//						System.out.println("PageId : "+result.getInt("pid"));
//						System.out.println("pageId : "+result.getInt("ppageid"));
//						System.out.println("page name : "+result.getString("pname"));
//						System.out.println("disamnb : "+result.getBoolean("isDisambiguation"));
						jwplPage = new JwplPage(result.getInt("pid"), result.getInt("ppageid"), 
								result.getString("pname"), result.getBoolean("isDisambiguation"));
						jwplPage.setParsedPage(JwplUtil.getText(result.getString("text")));
						firstTime = false;
					}
					// prune the category here itself stop word based, number, :, junk words
					jwplCat = new JwplCategory(result.getString("cname"));
					if(JwplUtil.isValidCatName(jwplCat.getTitle().getPlainTitle())) {
						jwplCat.setId(result.getInt("cid"));
						jwplCat.setPageId(result.getInt("cpageid"));
						jwplCatSet.add(jwplCat);
					}
					
				}
				
			}
			
			if(result != null) result.close();
				if(jwplPage == null) {
					//Connection conn = DBConnection.getInstance().getConnection();
					PreparedStatement pageStmt = conn.prepareStatement(JwplSQLConstants.SELECT_PAGE);
					pageStmt.setInt(1, pageId);
					result = pageStmt.executeQuery();
					if(result != null) {
						while(result.next()) {
							jwplPage = new JwplPage(result.getInt("pid"), result.getInt("ppageid"), 
									result.getString("pname"), result.getBoolean("isDisambiguation"));
							jwplPage.setParsedPage(JwplUtil.getText(result.getString("text")));
						}
						result.close();
					}
					
					pageStmt.close();
					//conn.close();
				}
				if(jwplPage != null)
					jwplPage.setJwplCategories(jwplCatSet);
			
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
		return jwplPage;
	}
	
	public int getNoOfInLinks(int pageId, PreparedStatement pstmt, BufferedWriter errorWriter){
		ResultSet result = null;
		JwplPage jwplPage = null;
		int rowCount=0;
		try {
			pstmt.setInt(1, pageId);
			result = pstmt.executeQuery();
			result.next();
			rowCount = result.getInt(1);
		    }
		catch(Exception e){
			try {
				errorWriter.write("Accessing page with id : " + pageId
						+ " failed : " + e.getMessage() + " " + e.getCause()
						+ "\n");
				e.printStackTrace();
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}
		return rowCount;
	}
	
	public int getNoOfOutLinks(int pageId, PreparedStatement pstmt, BufferedWriter errorWriter){
		ResultSet result = null;
		JwplPage jwplPage = null;
		int rowCount=0;
		try {
			pstmt.setInt(1, pageId);
			result = pstmt.executeQuery();
			result.next();
			rowCount = result.getInt(1);
		    }
		catch(Exception e){
			try {
				errorWriter.write("Accessing page with id : " + pageId
						+ " failed : " + e.getMessage() + " " + e.getCause()
						+ "\n");
				e.printStackTrace();
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}
		return rowCount;
	}

	
	
}
