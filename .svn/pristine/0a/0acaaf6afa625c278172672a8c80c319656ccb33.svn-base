package com.aneedo.search.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.aneedo.search.bean.QueryFacetDetail;
import com.aneedo.search.bean.SemClassFeedback;

public class SemanticClassDao {
	private static SemanticClassDao dao = null;

	ResultDBConnection dbConn = ResultDBConnection.getInstance();

	private SemanticClassDao() {
		// TODO Auto-generated constructor stub
	}

	public static SemanticClassDao getInstance() {
		if(dao == null) {
			dao = new SemanticClassDao();
		}
		return dao;
	}
	            
	public void storeResults(Map<String,String> resultMap){ 
			try{
			Connection conn = dbConn.getConnection();
			PreparedStatement pstmt = conn
			.prepareStatement(SQLConstants.INSERT_QUERY_DETAILS);
			pstmt.setString(1,resultMap.get("user").trim());
			pstmt.setString(2,resultMap.get("query_input").trim());
			pstmt.setDate(3,new Date(System.currentTimeMillis()));

			pstmt.executeUpdate();
			pstmt.close();

			pstmt = conn.prepareStatement(SQLConstants.SELECT_MAX_QUERY_ID);
			int maxId=0;
			ResultSet result = pstmt.executeQuery();
			if (result != null) {
				if (result.next()) {
					maxId=result.getInt("max_id");
				}
			}
			result.close();
			pstmt.close();
			
			pstmt = conn.prepareStatement(SQLConstants.INSERT_QUERY_DEPENDENT);
			pstmt.setInt(1, maxId);
			pstmt.setInt(2, Integer.parseInt(resultMap.get("truePos").trim()));
			pstmt.setInt(3, Integer.parseInt(resultMap.get("totalPos").trim()));
			pstmt.setInt(4, Integer.parseInt(resultMap.get("trueStems").trim()));
			pstmt.setInt(5, Integer.parseInt(resultMap.get("totalStems").trim()));
			pstmt.setInt(6, Integer.parseInt(resultMap.get("truePhrases").trim()));
			pstmt.setInt(7, Integer.parseInt(resultMap.get("totalPhrases").trim()));
			
			pstmt.executeUpdate();
			pstmt.close();
			
			pstmt = conn.prepareStatement(SQLConstants.INSERT_QUERY_INDEPENDENT);
			
			pstmt.setInt(1, maxId);
			pstmt.setBoolean(2,Boolean.parseBoolean(resultMap.get("section_heading").trim()));
			pstmt.setBoolean(3,Boolean.parseBoolean(resultMap.get("synopsis").trim()));
			pstmt.setBoolean(4,Boolean.parseBoolean(resultMap.get("hypernym").trim()));
			pstmt.setBoolean(5,Boolean.parseBoolean(resultMap.get("association_related").trim()));
			pstmt.setBoolean(6,Boolean.parseBoolean(resultMap.get("association_inlink").trim()));
			pstmt.setBoolean(7,Boolean.parseBoolean(resultMap.get("association_related_hierarchy").trim()));
			pstmt.setBoolean(8,Boolean.parseBoolean(resultMap.get("frequent").trim()));
			pstmt.setBoolean(9,Boolean.parseBoolean(resultMap.get("homonym").trim()));
			pstmt.setBoolean(10,Boolean.parseBoolean(resultMap.get("synonym").trim()));
			pstmt.setBoolean(11,Boolean.parseBoolean(resultMap.get("synonym_wordnet").trim()));
			pstmt.setBoolean(12,Boolean.parseBoolean(resultMap.get("make_produce").trim()));
			pstmt.setBoolean(13,Boolean.parseBoolean(resultMap.get("meronym").trim()));
			pstmt.setBoolean(14,Boolean.parseBoolean(resultMap.get("reference").trim()));
			pstmt.setBoolean(15,Boolean.parseBoolean(resultMap.get("role").trim()));
			pstmt.setBoolean(16,Boolean.parseBoolean(resultMap.get("sibling").trim()));
			pstmt.setBoolean(17,Boolean.parseBoolean(resultMap.get("fuzzy_association_inlink").trim()));
			pstmt.setBoolean(18,Boolean.parseBoolean(resultMap.get("entity_synonym").trim()));
			pstmt.setBoolean(19,Boolean.parseBoolean(resultMap.get("entity_synonym_wordnet").trim()));
			pstmt.setBoolean(20,Boolean.parseBoolean(resultMap.get("entity_page_title").trim()));
			pstmt.setBoolean(21,Boolean.parseBoolean(resultMap.get("entity_title_disamb").trim()));
			
			System.out.println(pstmt.toString());
			pstmt.executeUpdate();
			pstmt.close();
			
			
			pstmt = conn.prepareStatement(SQLConstants.INSERT_FIELD_BOOSTS);
			pstmt.setInt(1, maxId);
			pstmt.setFloat(2,Float.parseFloat(resultMap.get("section_heading-boost").trim()));
			pstmt.setFloat(3,Float.parseFloat(resultMap.get("synopsis-boost").trim()));
			pstmt.setFloat(4,Float.parseFloat(resultMap.get("hypernym-boost").trim()));
			pstmt.setFloat(5,Float.parseFloat(resultMap.get("association_related-boost").trim()));
			pstmt.setFloat(6,Float.parseFloat(resultMap.get("association_inlink-boost").trim()));
			pstmt.setFloat(7,Float.parseFloat(resultMap.get("association_related_hierarchy-boost").trim()));
			pstmt.setFloat(8,Float.parseFloat(resultMap.get("frequent-boost").trim()));
			pstmt.setFloat(9,Float.parseFloat(resultMap.get("homonym-boost").trim()));
			pstmt.setFloat(10,Float.parseFloat(resultMap.get("synonym-boost").trim()));
			pstmt.setFloat(11,Float.parseFloat(resultMap.get("synonym_wordnet-boost").trim()));
			pstmt.setFloat(12,Float.parseFloat(resultMap.get("make_produce-boost").trim()));
			pstmt.setFloat(13,Float.parseFloat(resultMap.get("meronym-boost").trim()));
			pstmt.setFloat(14,Float.parseFloat(resultMap.get("reference-boost").trim()));
			pstmt.setFloat(15,Float.parseFloat(resultMap.get("role-boost").trim()));
			pstmt.setFloat(16,Float.parseFloat(resultMap.get("sibling-boost").trim()));
			pstmt.setFloat(17,Float.parseFloat(resultMap.get("fuzzy_association_inlink-boost").trim()));
			pstmt.setFloat(18,Float.parseFloat(resultMap.get("entity_synonym-boost").trim()));
			pstmt.setFloat(19,Float.parseFloat(resultMap.get("entity_synonym_wordnet-boost").trim()));
			pstmt.setFloat(20,Float.parseFloat(resultMap.get("entity_page_title-boost").trim()));
			pstmt.setFloat(21,Float.parseFloat(resultMap.get("entity_title_disamb-boost").trim()));
			           
			pstmt.executeUpdate();
			pstmt.close();
			}
			catch(Exception e){
				e.printStackTrace();
				System.out.println("Sem Class error : " + e.getMessage());
			}		
	}
	public void storeQueryResults(SemClassFeedback bean) {
		try {
			Connection conn = dbConn.getConnection();
			storeQuery(bean, conn);
			storeFacetDetail(bean, conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void storeQuery(SemClassFeedback bean, Connection conn) throws Exception {
		PreparedStatement pstmt = conn
		.prepareStatement(SQLConstants.INSERT_QUERY);
		pstmt.setString(1, bean.getRawQuery());

		pstmt.setInt(2, Integer.valueOf(bean.getUserId()));
		pstmt.setDate(3, new Date(System.currentTimeMillis()));

		pstmt.executeUpdate();

		pstmt.close();
		pstmt = conn.prepareStatement(SQLConstants.SELECT_MAX_QUERY_ID);

		ResultSet result = pstmt.executeQuery();
		if (result != null) {
			if (result.next()) {
				bean.setQueryId(result.getInt("max_id"));
			}
		}
		result.close();
		pstmt.close();
	}

	private void storeFacetDetail(SemClassFeedback bean, Connection conn) throws Exception {
		PreparedStatement pstmt = conn
		.prepareStatement(SQLConstants.INSERT_QUERY_FACET);
		List<QueryFacetDetail> facetList = bean.getQueryFacet();

		for(int i=0,size=facetList.size();i<size;i++) {
			final QueryFacetDetail facet = facetList.get(i);
			pstmt.setInt(1, bean.getQueryId());
			pstmt.setString(2, facet.getFacet());
			pstmt.setBoolean(3, facet.IsRelevant());
			pstmt.addBatch();
		}
		pstmt.executeBatch();
		pstmt.close();
	}

}