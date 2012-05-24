package com.aneedo.jwplext.dao;

import gnu.trove.TIntObjectHashMap;

import java.io.BufferedWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.aneedo.indexing.bean.AssociationOverlap;

public class AssociationDaoOld {
	
	private static AssociationDao pageDao = new AssociationDao();


	public static AssociationDao getInstance() {
		return pageDao;
	}
	
	public Map<String,AssociationOverlap> getAssociationOverlap(int pageId, PreparedStatement[] pstmt, BufferedWriter errorWriter) {
		ResultSet result = null;
		Map<String,AssociationOverlap> assOverlapMap = new HashMap<String, AssociationOverlap>();
		try {
			pstmt[0].setInt(1, pageId);
			result = pstmt[0].executeQuery();
			TIntObjectHashMap<String> idNameMap = new TIntObjectHashMap<String>();
			
			if(result != null) {
				
				
				while(result.next()) {
					final AssociationOverlap assOverlap = new AssociationOverlap();
					final Integer assLink = result.getInt("link2");
					final String assLinkTitle = result.getString("name2");
					
					assOverlap.setLinkId(assLink);
					assOverlap.setLinkOverlapCount(result.getInt("overlap"));
					assOverlapMap.put(assLinkTitle, assOverlap);
					idNameMap.put(assLink,assLinkTitle);
				}
				result.close();
			}
			pstmt[1].setInt(1, pageId);
			result = pstmt[1].executeQuery();
			if(result != null) {
				int preId = -1;
				StringBuilder builder = null;
				while(result.next()) {
					//category_id, category_name, pageId2
					final int assLink = result.getInt("pageId2");
					if(preId != assLink) {
						if(builder == null) {
							builder = new StringBuilder();
							preId = assLink;
						}
						if(preId != assLink) {
							assOverlapMap.get(idNameMap.get(preId)).setLinkParentOverlap(builder.toString());
							builder = new StringBuilder(); 
							builder.append(result.getString("category_name") +" , ");
						} else {
							builder.append(result.getString("category_name") + " , ");
						}
						preId = assLink;
					}
					
				}
			}
			
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
		return assOverlapMap;
		
	}

}
