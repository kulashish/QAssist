package com.aneedo.jwplext.dao;

import java.io.BufferedWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.aneedo.jwplext.JwplPage;
import com.aneedo.jwplext.JwplSQLConstants;
import com.aneedo.jwplext.JwplUtil;

public class PageDisambDao {
	private static PageDisambDao pageDao = new PageDisambDao();


	public static PageDisambDao getInstance() {
		return pageDao;
	}
	
	public JwplPage getDisambPage(String title, PreparedStatement pstmt, BufferedWriter errorWriter) {
			ResultSet result = null;
			JwplPage jwplPage = null;
			try {
				final String temp = title + "_(disambiguation)";
				pstmt.setString(1, title);
				pstmt.setString(2, temp);
				result = pstmt.executeQuery();
				if(result != null) {
					jwplPage = new JwplPage();
					while(result.next()) {
						//System.out.println(result.getString("name") +" Title"+ title);
						if(result.getString("name").equals(title)
								|| result.getString("name").equals(temp)) {
							jwplPage.setParsedPage(JwplUtil.getText(result.getString("text")));
							//System.out.println(JwplUtil.getText(result.getString("text")));
							jwplPage.setName(title);
							break;
						}
						continue;
					}
				}
			} catch (Exception e) {
				try {
					errorWriter.write("Accessing disamb with title : " + title
							+ " failed : " + e.getMessage() + " " + e.getCause()
							+ "\n");
					e.printStackTrace();
				} catch (Exception exp) {
					exp.printStackTrace();
				}
			}
			return jwplPage;
	}
	
	public static void main(String[] args) {
		try {
		String title = "Zamość";
		Connection conn = DBConnection.getInstance().getConnection();
		PreparedStatement pageDisambStmt = conn.prepareStatement(JwplSQLConstants.SELECT_DISAMB);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
