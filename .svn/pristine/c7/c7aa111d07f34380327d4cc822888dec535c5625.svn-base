package com.aneedo.indexing.plainText;

import java.io.BufferedWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.aneedo.jwplext.JwplUtil;

public class PlainPageDao {


	private static PlainPageDao pageDao = new PlainPageDao();


	public static PlainPageDao getInstance() {
		return pageDao;
	}

	public JwplPlainPage getJwplPlainPageDetails(int pageId, PreparedStatement pstmt, BufferedWriter errorWriter) {
		ResultSet result = null;
		JwplPlainPage jwplPlainPage = null;
		try {
			pstmt.setInt(1, pageId);
			result = pstmt.executeQuery();
			boolean firstTime  = true;
			if(result != null) {
				while(result.next()) {
					if(firstTime) {
						jwplPlainPage = new JwplPlainPage(result.getInt("id"), result.getInt("pageId"), 
								result.getString("name"));
						jwplPlainPage.setPageText(JwplUtil.getText(result.getString("text").
									replaceAll("\n","").replaceAll("\\{\\{.+?\\}\\}","").replaceAll("\\}\\}", "").
									replaceAll("'''", "").replaceAll("<ref.+?</ref>","").replaceAll("[\\n\\t\\\\\"/]*","").
									replace('|', ',').replaceAll("( )+", " ").replaceAll("\\[\\[[a-z-]+:.+?\\]\\]","").
									replaceAll("\\[\\[Image:.+?\\]\\]","").replaceAll("\\[\\[File:.+?\\]\\]","").replaceAll("\\[\\[", "").
									replaceAll("\\]\\]"," ").replaceAll("\\'\\'","").replaceAll("<!--.+?-->","")));
						firstTime = false;
//						System.out.println("In PlainPage:"+JwplUtil.getText(result.getString("text").
//								replaceAll("\n","").replaceAll("\\{\\{.+?\\}\\}","").replaceAll("\\}\\}", "").
//								replaceAll("'''", "").replaceAll("<ref.+?</ref>","").replaceAll("[\\n\\t\\\\\"/]*","").
//								replace('|', ',').replaceAll("( )+", " ").replaceAll("\\[\\[[a-z-]+:.+?\\]\\]","").
//								replaceAll("\\[\\[Image:.+?\\]\\]","").replaceAll("\\[\\[File:.+?\\]\\]","").replaceAll("\\[\\[", "").
//								replaceAll("\\]\\]"," ").replaceAll("\\'\\'","").replaceAll("<!--.+?-->","")));
					}

				}
			}
		}
			catch (Exception e) {
				try {
					errorWriter.write("Accessing page with id : " + pageId
							+ " failed : " + e.getMessage() + " " + e.getCause()
							+ "\n");
				} catch (Exception exp) {
					exp.printStackTrace();
				}
			}
			return jwplPlainPage;
		}




	}
