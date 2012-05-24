package com.aneedo.training;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.aneedo.jwplext.dao.DBConnection;

public class ExtractDisambInfo {
	public static String select_inlinks_query = "SELECT d.pageId,d.inlinkId,p.name FROM disambInlinkCount AS d, Page AS p WHERE d.inlinkId=p.id AND d.pageId >= 4150000 and d.pageId < 7150000";
	public static String store_interpretations_query = "INSERT INTO disambPages values(?,?)";
	
	public static void storeInterpretation(PreparedStatement pstmt, int id, String interpretation) throws Exception{
		pstmt.setInt(1, id);
		pstmt.setString(2, interpretation);
		pstmt.executeUpdate();
	}
	
	public static void main(String[] args) throws Exception{
		DBConnection db_conn = DBConnection.getInstance();
		Connection conn = db_conn.getConnection();
		
		PreparedStatement get_inlinks_stmt = conn.prepareStatement(select_inlinks_query);
		PreparedStatement store_interprets_stmt = conn.prepareStatement(store_interpretations_query);
		
		int i=0;
		ResultSet res = get_inlinks_stmt.executeQuery();
		if(res!=null){
			while(res.next()){
				i++;
				int page_id = res.getInt(1);
				String interpretation = res.getString(3);
				storeInterpretation(store_interprets_stmt,page_id,interpretation);
				if(i%500==0) System.out.println(i + " " + page_id);
			}
		}
		
		get_inlinks_stmt.close();
		store_interprets_stmt.close();
		conn.close();
	}
	
	
}
