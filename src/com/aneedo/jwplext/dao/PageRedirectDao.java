package com.aneedo.jwplext.dao;

import java.io.BufferedWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

public class PageRedirectDao {
	private static PageRedirectDao pageDao = new PageRedirectDao();


	public static PageRedirectDao getInstance() {
		return pageDao;
	}
	
	public Set<String>  getRedirect(int pageId, PreparedStatement pstmt, BufferedWriter errorWriter) {
		ResultSet result = null;
		Set<String> reDirectSet = null;
		try {
			pstmt.setInt(1, pageId);
			result = pstmt.executeQuery();
			if(result != null) {
				reDirectSet = new HashSet<String>();
				while(result.next()) {
					reDirectSet.add(result.getString("redirects"));
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
		return reDirectSet;
	}

}
