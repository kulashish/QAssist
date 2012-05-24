package com.aneedo.training;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.aneedo.jwplext.dao.DBConnection;

public class ExtractDisambInterpretations {
	public static String select_disamb_pages = "SELECT id,name FROM Page WHERE isDisambiguation=1 LIMIT 10";
	public static String select_inlinks_query = "SELECT d.pageId,d.inlinkId,p.name,p.text FROM disambInlinkCount AS d, Page AS p WHERE d.inlinkId=p.id AND d.pageId=?";
	public static String store_interpretations_query = "INSERT INTO disambInterprets values(?,?,?)";
	public static String select_outlinks = "SELECT o.id,o.outLinks,p.name FROM page_outlinks AS o, Page AS p WHERE o.outLinks=p.id AND o.id=? LIMIT 5";
	
	public static void storeInterpretation(PreparedStatement pstmt, int id, String interpretation, int rank) throws Exception{
		pstmt.setInt(1, id);
		pstmt.setString(2, interpretation);
		pstmt.setInt(3, rank);
		pstmt.executeUpdate();
	}
	
	private static void extractRankedInterpretations(PreparedStatement pstmt, int id, String name, PreparedStatement store_pstmt, PreparedStatement outlink_pstmt) throws Exception {
		pstmt.setInt(1, id);
		ResultSet res = pstmt.executeQuery();
		if(res!=null){
			while(res.next()){
				int page_id = res.getInt(1);
				int interpretation_id = res.getInt(2);
				String interpretation = res.getString(3);
				String text = res.getString(4);
				System.out.println(name + " ; " + interpretation);
				//storeInterpretation(store_pstmt, page_id, interpretation, 1);
				storeRelated(id, name, text, store_pstmt);
				storeOutlikns(id, interpretation_id, name, outlink_pstmt, store_pstmt);
			}
		}
	}
	
	private static void storeRelated(int id, String name, String text,	PreparedStatement store_pstmt) throws Exception {
		String related_section_head = "==See also==";
		if(text.indexOf(related_section_head)!=-1){
			String[] sections = text.split("==");
			int i=0;
			while(!sections[i].equals("See also")) i++;
			String related_text = sections[i+1];
			System.out.println(related_text);
			String[] related_page_strings = related_text.split("*");
			for(int j=0;j<related_page_strings.length;j++){
				String curr = related_page_strings[j];
				if(curr.matches(".*[[.*]].*")){
					String page_name = curr.split("[[")[1].split("]]")[0].split("|")[0];
					//storeInterpretation(store_pstmt, id, page_name, 2);
				}
			}
			
			
		}
	}

	private static void storeOutlikns(int id, int interpretation_id, String name, PreparedStatement outlink_pstmt, PreparedStatement store_pstmt) throws Exception {
		outlink_pstmt.setInt(1, interpretation_id);
		ResultSet res = outlink_pstmt.executeQuery();
		if(res!=null){
			while(res.next()){
				int page_id = res.getInt(1);
				//int outlink_id = res.getInt(2);
				String interpretation = res.getString(3);
				System.out.println(name + " ; " + interpretation);
				//storeInterpretation(store_pstmt, id, interpretation, 3);
			}
		}
	}

	public static void main(String[] args) throws Exception{
		DBConnection db_conn = DBConnection.getInstance();
		Connection conn = db_conn.getConnection();
		
		PreparedStatement get_disamb_pages_stmt = conn.prepareStatement(select_disamb_pages);
		PreparedStatement get_inlinks_stmt = conn.prepareStatement(select_inlinks_query);
		PreparedStatement store_interprets_stmt = conn.prepareStatement(store_interpretations_query);
		PreparedStatement get_outlinks_stmt = conn.prepareStatement(select_outlinks);
		
		ResultSet res = get_disamb_pages_stmt.executeQuery();
		if(res!=null){
			while(res.next()){
				int page_id = res.getInt(1);
				String page_name = res.getString(2);
				extractRankedInterpretations(get_inlinks_stmt, page_id, page_name, store_interprets_stmt, get_outlinks_stmt);
			}
		}
		
		get_inlinks_stmt.close();
		store_interprets_stmt.close();
		conn.close();
	}

}
