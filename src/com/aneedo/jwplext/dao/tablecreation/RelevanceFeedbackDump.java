package com.aneedo.jwplext.dao.tablecreation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.aneedo.search.bean.InterpretationFeedbackBean;

public class RelevanceFeedbackDump {
	
	private static RelevanceFeedbackDump dao = null;
	
	private final String FEEDBACK_QUERY = "INSERT INTO feedback_queries(queryId,query, user) VALUES(?,?,?)";
	
	private final String INSERT_TITLE_FEEDBACK = "INSERT INTO interpretation_feedback(queryId, interpretation, rank) VALUES(?,?,?)" ;
	
	private final String INSERT_INTERPRETATION_FEEDBACK = "INSERT INTO title_feedback(queryid, title, relevance) VALUES (?,?,?)";
	
	private final String SELECT_MAX_QUERY = "select max(queryId) as max_id from feedback_queries";
	
	private Statement stmt = null;
	
	private String password = "narayan";
		
	public static void storeFeedback(InterpretationFeedbackBean bean){
		
		RelevanceFeedbackDump dao =  new RelevanceFeedbackDump();
		int maxId=0;
		try{
			Connection conn = DBConnectionFactory.getInstance(dao.password).getConnection();
			dao.stmt = conn.createStatement();
			ResultSet rs=dao.stmt.executeQuery(dao.SELECT_MAX_QUERY);
			if (rs != null) {
				if (rs.next()) {
					maxId=rs.getInt("max_id");
				}
			}
			rs.close();
			
			PreparedStatement queryStmt = conn.prepareStatement(dao.FEEDBACK_QUERY);
			queryStmt.setInt(1, maxId+1);	
			queryStmt.setString(2, bean.getQuery());
			queryStmt.setInt(3, Integer.parseInt(bean.getUserId()));
			queryStmt.executeUpdate();
			queryStmt.close();
			
			
			PreparedStatement titleStmt=conn.prepareStatement(dao.INSERT_TITLE_FEEDBACK);
			Map<String,String> titleMap=bean.getTitleRel();
			Iterator<String> iter=titleMap.keySet().iterator();
			while(iter.hasNext()){
				String title=iter.next();
				titleStmt.setInt(1,maxId+1);
				titleStmt.setString(2,title.replaceAll("_", " "));
				titleStmt.setInt(3, Integer.parseInt(titleMap.get(title)));
				
				titleStmt.addBatch();
			}
			
			try{
				titleStmt.executeBatch();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			titleStmt.close();
			
			PreparedStatement interpretStmt=conn.prepareStatement(dao.INSERT_INTERPRETATION_FEEDBACK);
			Map<String,String> interMap=bean.getInterpretationRank();
			Iterator<String> iterator=interMap.keySet().iterator();
			while(iterator.hasNext()){
				String title=iterator.next();
				interpretStmt.setInt(1,maxId+1);
				interpretStmt.setString(2,title.replaceAll("_", " "));
				interpretStmt.setInt(3, Integer.parseInt(interMap.get(title)));				
				interpretStmt.addBatch();
			}
			
			try{
				interpretStmt.executeBatch();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			titleStmt.close();
			conn.close();
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		
		InterpretationFeedbackBean bean=new InterpretationFeedbackBean();
		bean.setQuery("camlin");
		bean.setUserId("1");
		bean.setTitleRel(new HashMap<String, String>());
		bean.setInterpretationRank(new HashMap<String, String>());
		RelevanceFeedbackDump.storeFeedback(bean);
		
	}
	

}
