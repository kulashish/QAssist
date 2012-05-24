package com.aneedo.jwplext.dao;

import gnu.trove.TIntObjectHashMap;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aneedo.indexing.bean.AssociationOverlap;
import com.aneedo.jwplext.JwplSQLConstants;
import com.aneedo.jwplext.dao.tablecreation.DBConnectionFactory;

import de.tudarmstadt.ukp.wikipedia.api.Title;

public class AssociationDao {
	
	private static AssociationDao pageDao = new AssociationDao();

	public static AssociationDao getInstance() {
		return pageDao;
	}
	
	public Map<String,AssociationOverlap> getAssociationOverlap(int pageId, PreparedStatement[] pstmt, BufferedWriter errorWriter,
			StringBuilder assBuilder) {
		ResultSet result = null;
		Map<String,AssociationOverlap> assOverlapMap = new HashMap<String, AssociationOverlap>();
		try {
			pstmt[0].setInt(1, pageId);
			result = pstmt[0].executeQuery();
			
			List<Integer> outLinks = new ArrayList<Integer>();
			if(result != null) {
			while (result.next()) {
				outLinks.add(result.getInt("outLinks"));
			}
			result.close();
			}
			 if(outLinks.size() ==0 ) return assOverlapMap;
			 
			TIntObjectHashMap<String> idNameMap = new TIntObjectHashMap<String>();
			
			for (int j = 0, outSize = outLinks.size(); j < outSize; j++) {
				final int outLinkId = outLinks.get(j);
				pstmt[1].setInt(1, pageId);
				pstmt[1].setInt(2, outLinkId);
				result = pstmt[1].executeQuery();
				
				if(result != null) {
					while(result.next()) {
						final AssociationOverlap assOverlap = new AssociationOverlap();
						final Integer assLink = result.getInt("pageId2");
						String assLinkTitle = result.getString("name2");
						
						if(assLinkTitle != null) {
							assLinkTitle = new Title(assLinkTitle).getPlainTitle().replaceAll("-", " ").replace("(", "").replace(")", "").toLowerCase();
						assOverlap.setLinkId(assLink);
						assOverlap.setLinkOverlapCount(result.getInt("overlap"));
						//assBuilder.append(assLinkTitle +" |"+assLink+"/"+assOverlap.getLinkOverlapCount() +" || ");
						assOverlapMap.put(assLinkTitle, assOverlap);
						idNameMap.put(assLink,assLinkTitle);
						}

					}
					result.close();
				}
				
				String[] catOverlap = getParentOverlap(
						pstmt[2], errorWriter,
						pageId, outLinkId);
				
				if (catOverlap != null) {
					AssociationOverlap assOverlap = assOverlapMap.get(idNameMap.get(outLinkId));
					if(assOverlap != null) {
						assOverlap.setLinkCatOverlapId(catOverlap[0]);
						assOverlap.setLinkParentOverlap(catOverlap[1]);
					}
				}
			}
		} catch (Exception e) {
			try {
				errorWriter.write("Accessing association details of with id : " + pageId
						+ " failed : " + e.getMessage() + " " + e.getCause()
						+ "\n");
				e.printStackTrace();
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}
		return assOverlapMap;
		
	}
	
	private String[] getParentOverlap(
			PreparedStatement selectLinkCatOverlapStmt,
			BufferedWriter errorWriter, int pageId, int outlinkId) {
		String[] toReturn = null;

		// as catId, c.name as name, pc2.id as pageId
		// prune articles, wikipedia categories
		try {
			selectLinkCatOverlapStmt.setInt(1, pageId);
			selectLinkCatOverlapStmt.setInt(2, outlinkId);
			final ResultSet result = selectLinkCatOverlapStmt.executeQuery();
			// int prevId = -1;
			if (result != null) {
				StringBuilder catIdBuilder = new StringBuilder();
				StringBuilder catNameBuilder = new StringBuilder();
				while (result.next()) {
					final String catName = result.getString("name")
							.toLowerCase();
					// System.out.println("catName : " + catName);
					if (!(catName.indexOf("article_") >= 0
							|| catName.indexOf("wikipedia_") >= 0
							|| catName.indexOf("_article") >= 0
							|| catName.indexOf("abstract") >= 0
							|| catName.indexOf("all_pages") >= 0
							|| catName.indexOf("articles_") >= 0)) {
						catIdBuilder.append(result.getInt("catId") + "|");
						catNameBuilder.append(new Title(catName).getPlainTitle() + " | ");

					}

				}
				if (catIdBuilder.length() > 3) {
					toReturn = new String[2];
					toReturn[0] = catIdBuilder.toString();
					toReturn[1] = catNameBuilder.toString();
				}
			}
		} catch (Exception e) {
			try {
				errorWriter.write("Fetching parent overlap falied " + pageId
						+ " " + outlinkId + e.getMessage() + " " + e.getCause()
						+ "\n");
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}
		return toReturn;
	}

	public static void main(String[] args) throws Exception {
		BufferedWriter errorWriter=null;
		errorWriter = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("ErrorFileNew.log"), "UTF8"));
		
		 PreparedStatement selectLinkCatOverlapStmt=null;
		PreparedStatement selectOutlinksStmt = null;
		PreparedStatement selectAssOverlapStmt  = null;
		Connection conn = DBConnectionFactory.getInstance("Wikipedia","narayan",null).getConnectionOfDatabase();
		selectOutlinksStmt = conn
				.prepareStatement(JwplSQLConstants.SELECT_OUTLINKS);
		selectLinkCatOverlapStmt = conn
				.prepareStatement(JwplSQLConstants.SELECT_ASS_PARENT_OVERLAP);
		selectAssOverlapStmt = conn
				.prepareStatement(JwplSQLConstants.SELECT_ASS_OVERLAP_QUERY);

		
		AssociationDao dao = AssociationDao.getInstance();
		StringBuilder assBuilder = new StringBuilder();
		Map<String,AssociationOverlap> overlapMap = dao.getAssociationOverlap(198584, new PreparedStatement[] {selectOutlinksStmt,selectAssOverlapStmt,	
				selectLinkCatOverlapStmt}, errorWriter, assBuilder);
		
		
		Iterator<String> keyItr = overlapMap.keySet().iterator();
		
		while(keyItr.hasNext()) {
			String key = keyItr.next();
			String[] splits = key.replaceAll("-", " ").replace("(", "").replace(")", "").split(" ");
			AssociationOverlap overlap = overlapMap.get(key);
			
			System.out.println(key +":" +overlap.getLinkId() +":"+ overlap.getLinkOverlapCount()
					+":"+overlap.getLinkParentOverlap()
					+":"+  overlap.getLinkCatOverlapId());

			int startPos = assBuilder.indexOf(splits[0]);
			if( startPos >= 0) {
				int endPos = assBuilder.indexOf(" || ",startPos);
				
			}
		}
		
	}

}
