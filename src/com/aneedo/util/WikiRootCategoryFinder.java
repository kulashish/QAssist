package com.aneedo.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aneedo.jwplext.JwplSQLConstants;
import com.aneedo.jwplext.dao.DBConnection;

import de.tudarmstadt.ukp.wikipedia.api.CategoryGraph;
import de.tudarmstadt.ukp.wikipedia.api.DatabaseConfiguration;
import de.tudarmstadt.ukp.wikipedia.api.WikiConstants.Language;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;

public class WikiRootCategoryFinder {
	public Wikipedia wiki;
	public CategoryGraphExt cg;
	private static WikiRootCategoryFinder finder = null;

	public WikiRootCategoryFinder(){
		DatabaseConfiguration dbConfig = new DatabaseConfiguration();
		dbConfig.setHost("localhost");
		dbConfig.setDatabase("Wikipedia");
		dbConfig.setUser("root");
		dbConfig.setPassword("narayan");
		dbConfig.setLanguage(Language.english);
		try{
			wiki = new Wikipedia(dbConfig);
			cg= new CategoryGraphExt(wiki);
			cg.createRootPathMap();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public static WikiRootCategoryFinder getInstance(){
		if(finder == null){
			finder = new WikiRootCategoryFinder();
		}
		return finder;
	}
	
	public String getCatName(Integer key, PreparedStatement pstmt) throws Exception{
		pstmt.setInt(1, key);
		ResultSet res = pstmt.executeQuery();
		if(res == null) return null;
		res.next();
		return res.getString(1);
	}

	void getRootPath(){

		try{
			Map<Integer, List<Integer>> map= cg.getRootPathMap();
			Iterator<Integer> map_iterator = map.keySet().iterator();
			
			DBConnection db_conn = DBConnection.getInstance();
			Connection conn = db_conn.getConnection();
			
			String getNameStmt = "SELECT name FROM Category WHERE id=?";
			PreparedStatement nameStmt = conn.prepareStatement(getNameStmt);
			
			String storeAncStmt = JwplSQLConstants.STORE_ANCESTORS;
			PreparedStatement storeStmt = conn.prepareStatement(storeAncStmt);
			
			while(map_iterator.hasNext()){
				Integer key = map_iterator.next();
				//String name = getCatName(key,nameStmt);
				System.out.println("Key:"+key);
				List<Integer> list = map.get(key);
				Iterator<Integer> list_iterator = list.iterator();
				String ancestors = null;
				while(list_iterator.hasNext()){
					Integer cat_id = list_iterator.next();
					String cat_name = getCatName(cat_id, nameStmt);
					System.out.println("value:"+ cat_id + "| " + cat_name);
					if(ancestors==null){
						ancestors = cat_id + "|" + cat_name;
					}else{
						ancestors = ancestors + " " + cat_id + "|" + cat_name;
					}
				}
				System.out.println("ancestors:"+ ancestors);
				storeStmt.setInt(1, key);
				storeStmt.setString(2, ancestors);
				storeStmt.executeUpdate();
			}
			nameStmt.close();
			storeStmt.close();
			conn.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		WikiRootCategoryFinder finder= WikiRootCategoryFinder.getInstance();
		finder.getRootPath();
	}


}

class CategoryGraphExt extends CategoryGraph {
	//Wikipedia wiki = null;
	public CategoryGraphExt(Wikipedia wiki) throws Exception {
		super(wiki);
	}


	public Map<Integer, List<Integer>> getRootPathMap()
			throws WikiApiException {
		return super.getRootPathMap();
	}
}
