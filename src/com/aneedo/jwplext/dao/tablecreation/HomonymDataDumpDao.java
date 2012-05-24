package com.aneedo.jwplext.dao.tablecreation;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HomonymDataDumpDao {

	private final String SELECT_OUTLINKS_IN_DISAMBPAGE="select outlink from DisambiguationPage where pageId = ?";

	private final String INSERT_OUTLINKS = "INSERT INTO disambInlinkCount (pageId, inlinkId , count) VALUES(?, ?, ? )";

	private  int offset = 0;

	// How many rows to pick
	private  final int LIMIT = 25;

	private  int MAX_RECORD = 3678909;

	private  PreparedStatement pstmt = null;

	private  Statement stmt=null;

	private  BufferedWriter errorWriter = null;

	private  String LOG_FILE =  "/data1/qassist/wikidata/log/HomonymDataDump";
//		"/mnt/bag1/querysystem/indexing/datadump/log/HomonymDataDump";

	private String password = "aneedo";
	public static void main(String[] args) {
		HomonymDataDumpDao dao= new HomonymDataDumpDao();
			
		try {
			System.out.println("Enter offset max-record db-password");
			if(args.length < 2) {
				System.out.println("Not entered all parameters....");
				return;
			}
			dao.offset = Integer.parseInt(args[0]);
			dao.MAX_RECORD = Integer.parseInt(args[1]);
			dao.LOG_FILE = dao.LOG_FILE +dao.offset+".log";
			dao.errorWriter = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(dao.LOG_FILE), "UTF8"));
			if(args.length > 2) {
				dao.password = args[2];
			}
			Connection conn = DBConnectionFactory.getInstance(dao.password).getConnection();

			dao.pstmt = conn.prepareStatement("select pageId from DisamPage" +
					" LIMIT "+dao.LIMIT+" offset ?");

			PreparedStatement outlinkStmt = conn.prepareStatement(dao.SELECT_OUTLINKS_IN_DISAMBPAGE);
			PreparedStatement insertStmt = conn.prepareStatement(dao.INSERT_OUTLINKS);

			dao.stmt=conn.createStatement();
			ResultSet result=null;
			int pageId=0;
			while(dao.offset + dao.LIMIT<= dao.MAX_RECORD){
				final List<Integer> pageIdList =  dao.getPageIdList(dao.offset);
				for(int i = 0,size=pageIdList.size();i<size;i++) {
					 pageId = pageIdList.get(i);

					outlinkStmt.setInt(1, pageId);
					System.out.println("i:"+i+"  pageId:"+pageId);
					StringBuilder outLinkIds= new StringBuilder();
					String outLinksString="";
					try{
						result = outlinkStmt.executeQuery();
						if(result!=null){
							while(result.next()){
								outLinkIds.append(result.getInt("outlink")+",");
							}
							result.close();
							System.out.println("outLinkIds:"+outLinkIds.toString());
							if(outLinkIds.toString().equals("")) continue;
							outLinksString=outLinkIds.substring(0, outLinkIds.length()-2);
							//System.out.println("outLinksString:"+outLinksString);
						}
					}
					catch(Exception e){
						try{
							dao.errorWriter.write("outLinkStmt failed" + e.getMessage() + 
									" " + e.getCause()+ "\n");
						}
						catch(Exception exp){
							exp.printStackTrace();
						}

					}

					ResultSet rs= dao.stmt.executeQuery("select id,count(inLinks) from page_inlinks where id in (" + outLinksString + ") group by id;");
					if(rs!=null){
						while(rs.next()){
							insertStmt.setInt(1,pageId);
							insertStmt.setInt(2,rs.getInt("id"));
							insertStmt.setInt(3,rs.getInt("count(inLinks)"));
							//System.out.println("Row:"+ pageId+" " + rs.getInt("id")+ " " +rs.getInt("count(inLinks)") );

							insertStmt.addBatch();
						}
						rs.close();
					}
					
					try{
						insertStmt.executeBatch();
					}
					catch(Exception e){
						try{
							dao.errorWriter.write("insertStmt failed" +
									pageId + " " + e.getMessage() + "\n" + e.getCause());
						}
						catch(Exception exp){
							exp.printStackTrace();
						}
					}
				}
				
				dao.offset = dao.offset + dao.LIMIT;
				System.out.println("Completed "+dao.offset );//+ ", To go " + (dao.MAX_RECORD-dao.offset));
				try{
					insertStmt.close();
					outlinkStmt.close();
					
					conn.close();
					conn = DBConnectionFactory.getInstance(dao.password).getConnection();
					
					dao.pstmt=conn.prepareStatement("select pageId from DisamPage" +
							" LIMIT "+dao.LIMIT+" offset ?");
					dao.stmt=conn.createStatement();
					outlinkStmt = conn.prepareStatement(dao.SELECT_OUTLINKS_IN_DISAMBPAGE);
					insertStmt = conn.prepareStatement(dao.INSERT_OUTLINKS);
					
				}
				catch(Exception e){
					try {
						dao.errorWriter.write("connection closed falied after" +dao.offset + e.getMessage() + " "
								+ e.getCause() + "\n");
					} catch (Exception exp) {
						exp.printStackTrace();
					}
					
				}
			}
		}
		catch(Exception e){
			try {
				
				dao.errorWriter.write("initalization falied" + e.getMessage() + " "
						+ e.getCause() + "\n");
			} catch (Exception exp) {
				
				exp.printStackTrace();
			}

		}
		try{
			dao.errorWriter.flush();
			dao.errorWriter.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	private  List<Integer> getPageIdList(int offset) {
		List<Integer> pageIdList = new ArrayList<Integer>();
		try {
			pstmt.setInt(1, offset);
			ResultSet result = pstmt.executeQuery();
			if(result != null) {
				while(result.next()) {
					pageIdList.add(result.getInt("pageId"));
				}
			}
			result.close();
		} catch (Exception e) {
			try {
				errorWriter.write("fetching page id falied" + e.getMessage() + " "
						+ e.getCause() + "\n");
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}
		System.out.println("returning "+pageIdList.size()+ " page ids.");
		return pageIdList;
	}
}
