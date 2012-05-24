package com.aneedo.jwplext.dao;

import java.io.BufferedWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import de.tudarmstadt.ukp.wikipedia.api.Title;


public class DisambPageInLinkDao {
	
	private static DisambPageInLinkDao inLinkDao = new DisambPageInLinkDao();
	
	public static DisambPageInLinkDao getInstance(){
		return inLinkDao;
	}
	
	public Set<String> getDisambPageInLinks(int pageId, PreparedStatement pstmt, BufferedWriter errorWriter) {
		ResultSet result = null;
		Set<String> inLinkSet = null;
		try {
			pstmt.setInt(1, pageId);
			result = pstmt.executeQuery();
			if(result != null) {
				inLinkSet = new HashSet<String>();
				while(result.next()) {
					inLinkSet.add(result.getString("pageId"));
				}
			}
		} catch (Exception e) {
			try {
				errorWriter.write("Accessing redirect with id : " + pageId
						+ " failed : " + e.getMessage() + " " + e.getCause()
						+ "\n");
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}
		return inLinkSet;
	}
	
	
	public String getDisambPageId(int pageId, PreparedStatement pstmt, BufferedWriter errorWriter){
		
		StringBuilder disambId  = null;
		DisambPageInLinkDao dao= new DisambPageInLinkDao();
				
		try{
			pstmt.setInt(1,pageId);
			ResultSet rs= pstmt.executeQuery();
			if(rs != null){
				disambId  = new StringBuilder();
				while(rs.next()){
					try {
					disambId.append(new Title(rs.getString("title")).getPlainTitle()+" /"+rs.getInt("pageId")+"| ");
					} catch (Exception e) {
						try {
							errorWriter.write("Accessing disamb title details : " + pageId
									+ " failed : " + e.getMessage() + " " + e.getCause()
									+ "\n");
						} catch (Exception exp) {
							exp.printStackTrace();
						}
					}
				}
			}			
		}
		catch(Exception e){
			try {
				errorWriter.write("Accessing disamb PageId with id : " + pageId
						+ " failed : " + e.getMessage() + " " + e.getCause()
						+ "\n");
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}
		if(disambId==null || disambId.length() < 3) {
//			try {
//				errorWriter.write("There is no outlink for this disambiguation page : "+pageId+ "\n");
//			} catch (Exception exp) {
//				exp.printStackTrace();
//			}
			return null;
		}
		else {
			//System.out.println("Page id " + pageId +" disamb id "+ disambId);
			return disambId.substring(0,disambId.length()-1);
		}
	}
	
	public String getDisambDetails(int pageId,PreparedStatement pstmt, BufferedWriter errorWriter){
		StringBuilder pageDetails=null;
		
		try{
			pstmt.setInt(1, pageId);
			ResultSet rs= pstmt.executeQuery();
			
			if(rs != null){
				pageDetails  = new StringBuilder();
				if(!rs.next()) return null;
				while(rs.next()){
//					System.out.println("ID:"+rs.getInt("inlinkId"));
					try {
					pageDetails.append(new Title(rs.getString("name")).getPlainTitle()+" /"+rs.getInt("inlinkId")+"| ");
					} catch (Exception e) {
						try {
							errorWriter.write("Accessing disamb page inlink title : " + pageId
									+ " failed : " + e.getMessage() + " " + e.getCause()
									+ "\n");
						} catch (Exception exp) {
							exp.printStackTrace();
						}
					}
				}
			}
			
		}
		catch(Exception e){
			try {
				errorWriter.write("Accessing disamb page details with id : " + pageId
						+ " failed : " + e.getMessage() + " " + e.getCause()
						+ "\n");
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}
		//System.out.println(pageId +" "+ pageDetails);
		if(pageDetails == null || pageDetails.length() < 3) {
				try {
					errorWriter.write("There is no referred disambiguation page : " + pageId+ "\n");
				} catch (Exception exp) {
					exp.printStackTrace();
				}
				return null;
		}
		return pageDetails.substring(0,pageDetails.length()-1);
	}
	
	public static void main(String[] args){
		
	}

}
