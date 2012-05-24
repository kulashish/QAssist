package com.aneedo.search.ranking.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.aneedo.jwplext.JwplSQLConstants;
import com.aneedo.jwplext.dao.tablecreation.DBConnectionFactory;

import de.tudarmstadt.ukp.wikipedia.api.Title;


public class FolksonomyMeasures {

	private static FolksonomyMeasures instance = null; 

	private static final String DB_NAME = "wikipediaNew";
	private static final String DB_PASSWORD = "bet123";
	//private static final String DB_IP = "10.129.5.199";
	private static final String DB_IP = "10.129.5.199";

	public FolksonomyMeasures() {

	}

	public static FolksonomyMeasures getInstance() {
		if(instance == null) {
			instance = new FolksonomyMeasures();
		}
		return instance;
	}

	public Map<String,String[]> getRootPath(String catName){
		Map<String,String[]> catAncestors = null;
		//		 String[] nameSplits = catName.split(" ");
		//		 StringBuilder nameBuilder = new StringBuilder();
		catName = Character.toUpperCase(catName.charAt(0)) + catName.substring(1, catName.length());
		//		 if(nameSplits.length > 1) {
		//			 nameBuilder.append("("+nameSplits[0]+")");
		//			 for(int i=1;i<nameSplits.length;i++) {
		//				 nameBuilder.append("[_-]?[(]?("+nameSplits[i]+")[)]?");
		//			 }
		//		 } else {
		//			 nameBuilder.append(nameSplits[0]);
		//		 }
		try{
			DBConnectionFactory connFactory = DBConnectionFactory.getInstance(DB_NAME,DB_PASSWORD,DB_IP);
			Connection con = connFactory.getConnectionOfDatabase();
			PreparedStatement pstmt = con.prepareStatement(JwplSQLConstants.SELECT_CATEGORY_PATH);
			//System.out.println("Name with reg expression : " +nameBuilder.toString());
			pstmt.setString(1, catName);
			ResultSet result = pstmt.executeQuery();
			String dbCatName = null;

			if(result != null) {
				catAncestors = new HashMap<String, String[]>();
				while (result.next()) {

					try {
						dbCatName = new Title(result.getString("category_name")).getPlainTitle();
					} catch (Exception e) {
						dbCatName = result.getString("category_name").replaceAll("_", " ").replace("(", "").replace(")", "");
					}
					// 
					String[] ancestors = null;
					if(result.getString("ancestors") != null) {
						String[] catSplits = result.getString("ancestors").split(" ");
						ancestors = new String[catSplits.length];
						for(int i=0; i<catSplits.length; i++) {
							final String[] ancestor = catSplits[i].split("\\|");
							if(ancestor.length >1) {
								try {
									ancestors[i] = new Title(ancestor[1]).getPlainTitle();
								} catch (Exception e) {
									ancestors[i] = ancestor[1].replaceAll("_", " ").replace("(", "").replace(")", "");
								}
							}
						}
					}

					catAncestors.put(dbCatName, ancestors);
				}
			}
			if(result != null) result.close();
			if(pstmt != null) pstmt.close();
			if(con != null) con.close();
		}
		catch(Exception e){                
			e.printStackTrace();
		}
		return catAncestors;
	}

	public static ArrayList<Integer> getCommonDescendantCount(String catName1, String catName2){
		ArrayList<Integer> count = new ArrayList<Integer>();
		ArrayList<Integer> descendants1 = getDescendants(catName1);
		System.out.println("desc1 size:"+descendants1.size());
		ArrayList<Integer> descendants2 = getDescendants(catName2);
		System.out.println("desc2 size:"+descendants2.size());
		try{
			DBConnectionFactory connFactory = DBConnectionFactory.getInstance(DB_NAME,DB_PASSWORD,DB_IP);
			Connection con = connFactory.getConnectionOfDatabase();
			Iterator<Integer> set_iterator = descendants1.iterator();
			for(Iterator<Integer> i = descendants1.iterator();i.hasNext();){
				int catId = i.next();
				for(Iterator<Integer> j = descendants2.iterator(); j.hasNext();){
					PreparedStatement pstmt = con.prepareStatement(JwplSQLConstants.COUNT_COMMON_PAGES);
					pstmt.setInt(1, catId);
					int catId2 = j.next();
					pstmt.setInt(2, catId2);
//					System.out.println(catId + " vs " + catId2 );
					ResultSet rs = pstmt.executeQuery();
					if(rs != null){
						rs.first();
						count.add(rs.getInt(1));
					}
					if(rs!= null) rs.close();
					if(pstmt!=null) pstmt.close();
				}
			}
			if(con!= null) con.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

		return count;
	}


	public static ArrayList<Integer> getDescendants(String catName){
		/// Here it is only till two levels
		ArrayList<Integer> descendants = new ArrayList<Integer>();

		try{
			int catId=-1;
			DBConnectionFactory connFactory = DBConnectionFactory.getInstance(DB_NAME,DB_PASSWORD,DB_IP);
			Connection con = connFactory.getConnectionOfDatabase();
			PreparedStatement pstmt = con.prepareStatement(JwplSQLConstants.SELECT_SUB_CATEGORY_ID);
			PreparedStatement idStmt = con.prepareStatement(JwplSQLConstants.SELECT_CATID_GIVEN_NAME);
			idStmt.setString(1, catName);
			ResultSet idRes = idStmt.executeQuery();
		//	System.out.println(idRes.first());
			if(idRes != null){
				if(idRes.next()){
				catId = idRes.getInt("pageId");
//				System.out.println("current Id:"+catId);
				descendants.add(catId);
				}
			}
			if(idRes!= null) idRes.close();
			if(idStmt!= null) idStmt.close();
			if(catId == -1){
				return null;
			}
			pstmt.setInt(1, catId);
			ResultSet result = pstmt.executeQuery();
			if(result!= null){
				while(result.next()){
					try{
						int child = result.getInt("outlinks");
						descendants.add(child);
//						System.out.println("child:"+child);
						PreparedStatement pstmt2 = con. prepareStatement(JwplSQLConstants.SELECT_SUB_CATEGORY_ID);
						pstmt2.setInt(1, child);
						ResultSet result2 = pstmt2.executeQuery();
						if(result2!= null){
							while(result2.next()){
								int grandChild = result2.getInt("outlinks");
//								System.out.println("GrandChild:"+ grandChild);
								descendants.add(grandChild);
							}
						}

						if(result2 != null)    result2.close();
						if(pstmt2 != null)pstmt2.close();
					}
					catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						System.out.println("Problem while extracting grand children");

					}
				}
			}
			if(result != null) result.close();
			if(pstmt != null) pstmt.close();
			if(con != null) con.close();
		}
		catch (Exception e) {

			e.printStackTrace();
			// TODO: handle exception
		}

		return descendants;
	}

	public static void main(String[] args) {
//		FolksonomyMeasures folkMeasures = FolksonomyMeasures.getInstance();
//		String catName = "Computing by domain";
//		Map<String,String[]> ancestorMap = folkMeasures.getRootPath(catName);
//
//		Iterator<String> keyItr = ancestorMap.keySet().iterator();
//		String key = null;
//		while(keyItr.hasNext()){
//			key = keyItr.next();
//			String[] ancestors = ancestorMap.get(key);
//			System.out.println("*******"+key+"*******");
//			if(ancestors != null) {
//				for( int i=0;i<ancestors.length;i++) {
//					System.out.print(ancestors[i]+",");
//				}
//				System.out.println();
//			}
//		}
		ArrayList l=getCommonDescendantCount("recipients of the padma shri", "indian film actors");
		Iterator i=l.iterator();
		while(i.hasNext()) System.out.println(i.next());

	}

}
