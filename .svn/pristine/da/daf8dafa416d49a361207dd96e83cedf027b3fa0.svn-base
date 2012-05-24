package com.aneedo.training;

import gnu.trove.TIntObjectHashMap;
import gnu.trove.TObjectIntHashMap;
import gnu.trove.map.TObjectCharMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aneedo.jwplext.dao.tablecreation.DBConnectionFactory;
import com.aneedo.search.bean.InterpretationFeedbackBean;

public class RelevanceFeedbackDao {
	
	private static RelevanceFeedbackDao dao = null;
	
	private final String FEEDBACK_QUERY = "INSERT INTO feedback_queries(queryId,query, user) VALUES(?,?,?)";
	
	private final String INSERT_INTERPRETATION_FEEDBACK = "INSERT INTO interpretation_feedback(queryId, interpretation, rank) VALUES(?,?,?)" ;
	
	private final String INSERT_TITLE_FEEDBACK = "INSERT INTO title_feedback(queryid, title, relevance) VALUES (?,?,?)";
	
	private final String SELECT_MAX_QUERY = "select max(queryId) as max_id from feedback_queries";
	
	private final String SELECT_QUERY = "select queryId, query from feedback_queries";
	
	public static final String SELECT_QUERY_FEEDBACK = "select interpretation, rank from interpretation_feedback where queryid = ? and rank <= 10";
	
	//private Statement stmt = null;
	
	public String password = "bet123";
		
	public static void storeFeedback(InterpretationFeedbackBean bean){
		
		RelevanceFeedbackDao dao =  new RelevanceFeedbackDao();
		int maxId=0;
		try{
			Connection conn = DBConnectionFactory.getInstance(dao.password).getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs= stmt.executeQuery(dao.SELECT_MAX_QUERY);
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
		RelevanceFeedbackDao.storeFeedback(bean);
		
	}
	
	/* .... For LR training ..... */
	public TIntObjectHashMap<String> getQueries(Connection conn)  {
		 TIntObjectHashMap<String> queryMap = new TIntObjectHashMap<String>();
		try{
			PreparedStatement stmt = conn.prepareStatement(SELECT_QUERY);
		ResultSet rs= stmt.executeQuery();
		if (rs != null) {
			while(rs.next()) {
				queryMap.put(rs.getInt("queryId"),
						rs.getString("query"));
			}
		}
		if(rs != null) rs.close();
		if(stmt != null) stmt.close();
		return queryMap;
		} catch (Exception e) {
			e.printStackTrace();
			return queryMap;
		}
	}
	
	public TObjectIntHashMap<String> getRelevanceFeedback(Connection conn, PreparedStatement stmt, int queryId) {
		TObjectIntHashMap<String> interpretRelevanceMap = new TObjectIntHashMap<String>();
		try{
			stmt.setInt(1, queryId);
		ResultSet rs= stmt.executeQuery();
		
		if (rs != null) {
			while(rs.next()) {
				interpretRelevanceMap.put(rs.getString("interpretation"),
						rs.getInt("rank")-1);
			}
		}
		if(rs != null) rs.close();
		return interpretRelevanceMap;
		} catch (Exception e) {
			e.printStackTrace();
			return interpretRelevanceMap;
		}
	}
	

}
