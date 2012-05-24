package com.aneedo.indexing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.aneedo.jwplext.dao.tablecreation.DBConnectionFactory;

public class WikiFSIndexWriterMain {
	private final ExecutorService execSvc = Executors.newCachedThreadPool();
	private static final int MAX_THREADS = 1;
	
	// From which rowId should start
	private int offset = 0;

	// How many rows to pick
	private static final int LIMIT = 25;
	private int MAX_RECORD = 500;//600000;//offset + 2*LIMIT;//3678909;
	
	private List<Integer> pageIdList = new ArrayList<Integer>();
	private Integer pageId = 0;
	
	private PreparedStatement pstmt = null;
	private Connection con = null;
	private String dbName = null;
	private String password = null;
	private String url = null;
	
	public static void main(String[] args) throws Exception {
		String startTime = new Date(System.currentTimeMillis()).toString();

		System.out.println("Indexing ...."+startTime);
		
		WikiFSIndexWriterMain wikiFSWriterMain = new WikiFSIndexWriterMain();
		IndexWriterHelper[] ramThreads = new IndexWriterHelper[MAX_THREADS];
		
		System.out.println("Enter <start record> <end record> <system name(mlir,narayan,lakshmi1...>");
		if(args.length < 3) {
			System.out.println("Not entered all parameters....");
			System.out.println("Arg[0] " + args[0]);
			System.out.println("Arg[1] " + args[1]);
			System.out.println("Arg[2] " + args[2]);
			if( args.length > 3) System.out.println("Arg[3] " + args[3]);
			return;
		}
		try {
			wikiFSWriterMain.offset = Integer.parseInt(args[0]);
		} catch (Exception e) {
			System.out.println("start record not integer....");
			System.out.println("Arg[0] " + args[0]);
			System.out.println("Arg[1] " + args[1]);
			System.out.println("Arg[2] " + args[2]);
			if( args.length > 3) System.out.println("Arg[3] " + args[3]);
			return;
		}
		
		try {
			wikiFSWriterMain.MAX_RECORD = Integer.parseInt(args[1]);
		} catch (Exception e) {
			System.out.println("end record not integer....");
			System.out.println("Arg[0] " + args[0]);
			System.out.println("Arg[1] " + args[1]);
			System.out.println("Arg[2] " + args[2]);
			if( args.length > 3) System.out.println("Arg[3] " + args[3]);
			return;
		}
		
		// based on system set db details, indexing and log
		String dbName = "";
		String password="";
		String indexingPath = "";
		String tmpUrl = "localhost";

		System.out.println("Arg[0] " + args[0]);
		System.out.println("Arg[1] " + args[1]);
		System.out.println("Arg[2] " + args[2]);
		
		if("mlir".equals(args[2])) {
			dbName = "wikipedia";
			password = "aneedo";
			indexingPath = "/mnt/bag1/querysystem/indexing/semanticNew";
		} else if("narayan".equals(args[2])) {
			dbName = "Wikipedia";
			password = "narayan";
			indexingPath = "/data2/indexing/semantic";
		} else if("lakshmi1".equals(args[2])) {
			dbName = "wikipedia";
			password = "lakshmi1";
			indexingPath = "/data1/indexing/semantic";
		} else if("lakshmi2".equals(args[2])) {
			dbName = "wikipedia";
			password = "lakshmi2";
			indexingPath = "/data1/indexing/semantic";
		} else if("lakshmi3".equals(args[2])) {
			dbName = "wikipedia";
			password = "lakshmi";
			indexingPath = "/data1/indexing/semantic";
		} else if("lakshmi4".equals(args[2])) {
			dbName = "wikipedia";
			password = "lakshmi";
			indexingPath = "/data1/indexing/semantic";
		} else if("lakshmi5".equals(args[2])) {
			dbName = "wikipedia";
			password = "lakshmi";
			tmpUrl = "0.0.0.0";
			indexingPath = "/data1/indexing/semantic";
		} else if("bet".equals(args[2])) {
			dbName = "wikipediaNew";
			password = "bet123";
			indexingPath = "/home/kedhar/indexing/semantic";
		} else if(args[2].indexOf("129") >= 0 ) {
			System.out.println("Running on non server with db : " + args[2]);
			if(args.length > 3) {
				indexingPath = args[3].trim();
			} else {
				System.out.println("Indexing path missing....");
				System.out.println("Arg[0] " + args[0]);
				System.out.println("Arg[1] " + args[1]);
				System.out.println("Arg[2] " + args[2]);
				if( args.length > 3) System.out.println("Arg[3] " + args[3]);
				return;
			}
				tmpUrl = args[2].trim();
				
				if("10.129.5.199".equals(tmpUrl)) {
					dbName = "wikipediaNew";
					password = "bet123";
					
				} else if("10.129.1.101".equals(tmpUrl)) {
					dbName = "Wikipedia";
					password = "narayan";
					
				} else if("10.129.1.102".equals(tmpUrl)) {
					dbName = "wikipedia";
					password = "lakshmi1";
					
				} else if("10.129.1.103".equals(tmpUrl)) {
					dbName = "wikipedia";
					password = "lakshmi2";
					
				} else if("10.129.1.104".equals(tmpUrl)) {
					dbName = "wikipedia";
					password = "lakshmi";
					
				} else if("10.129.1.105".equals(tmpUrl)) {
					dbName = "wikipedia";
					password = "lakshmi";
					
				} else if("10.129.1.106".equals(tmpUrl)) {
					dbName = "wikipedia";
					password = "lakshmi";
					
				}  else if("10.129.1.91".equals(tmpUrl)) {
					dbName = "wikipedia";
					password = "aneedo";
					
				} else {
					System.out.println("Invalid IP address of database server....");
					System.out.println("Arg[0] " + args[0]);
					System.out.println("Arg[1] " + args[1]);
					System.out.println("Arg[2] " + args[2]);
					if( args.length > 3) System.out.println("Arg[3] " + args[3]);
					return;
				}
			
		} else {
			System.out.println("Arg[0] " + args[0]);
			System.out.println("Arg[1] " + args[1]);
			System.out.println("Arg[2] " + args[2]);
			if( args.length > 3) System.out.println("Arg[3] " + args[3]);
			System.out.println("System does not exists....");
			return;
		}
		
		

		wikiFSWriterMain.dbName = dbName;
		wikiFSWriterMain.password = password;
		wikiFSWriterMain.url = tmpUrl;
		
        DBConnectionFactory connFactory = DBConnectionFactory.getInstance(dbName,password,tmpUrl);
        try {
        	wikiFSWriterMain.con = connFactory.getConnectionOfDatabase();
        	wikiFSWriterMain.pstmt = wikiFSWriterMain.con.prepareStatement("select pageId from Page LIMIT "+LIMIT+" offset ?");
        } catch (Exception e) {
			e.printStackTrace();
		}
		
		for(int i=0;i<ramThreads.length;i++) {
			ramThreads[i] = new IndexWriterHelper(wikiFSWriterMain.offset,i+1,indexingPath,dbName,password,tmpUrl);
		}
		
		int count = wikiFSWriterMain.offset;
		while(count <= wikiFSWriterMain.MAX_RECORD ) {
			for(int i=0;i<ramThreads.length;i++) {
				if(!ramThreads[i].getStatus()) {
					wikiFSWriterMain.nextPageIdForProcessing();
					System.out.println("Running Count : " + count++ + " Page Id : " + wikiFSWriterMain.pageId);
					ramThreads[i].setPageId(wikiFSWriterMain.pageId);
					ramThreads[i].run();
					//wikiFSWriterMain.execSvc.execute( ramThreads[i]);
				}
			}
			//count = count + wikiFSWriterMain.LIMIT;
			//System.out.println("Completed till " + count);
		}
		
		// close File indexWriter if done
		boolean isDone = false;
		while(!isDone) {
			for(int i=0;i<ramThreads.length;i++) {
				if(ramThreads[i].getStatus()) {
					isDone = false;
					break;
				}
				
				isDone = true;
			}
		}
		
		if(isDone) {
			for(int i=0;i<ramThreads.length;i++) {
				ramThreads[i].closeResources(); 
			}
		}
		System.out.println("indexing done... started at " + startTime +" end time : "+new Date(System.currentTimeMillis()));
		if(wikiFSWriterMain.con != null)
			wikiFSWriterMain.con.close();
	}
	
	public void nextPageIdForProcessing() {
		if(this.pageIdList.size() == 0) {
			try {
			this.pageIdList = getPageIdList(offset);
			} catch (Exception e) {
		        try {
					Connection conn = null;
			        DBConnectionFactory connFactory = DBConnectionFactory.getInstance(dbName,password,url);
		        	conn = connFactory.getConnectionOfDatabase();
		        	
		        	try {
		        		if(pstmt != null) {
		        			pstmt.close();
		        		}
		        	} catch (Exception exp) {
						pstmt = null;
					}
		        	
		        	try {
		        		if(con != null) {
		        			con.close();
		        		}
		        	} catch (Exception exp) {
						con = null;
					}
		        	
		        	con = conn;
		        	pstmt = con.prepareStatement("select pageId from Page LIMIT "+LIMIT+" offset ?");
		        	this.pageIdList = getPageIdList(offset);
		        } catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			//System.out.println("doing till ..." + offset);
			offset = offset + LIMIT;
			
		}
		int count = 0;
		while(pageIdList.size() == 0 && count++ < 5) {
			
			 try {
					Connection conn = null;
			        DBConnectionFactory connFactory = DBConnectionFactory.getInstance(dbName,password,url);
		        	conn = connFactory.getConnectionOfDatabase();
		        	
		        	try {
		        		if(pstmt != null) {
		        			pstmt.close();
		        		}
		        	} catch (Exception exp) {
						pstmt = null;
					}
		        	
		        	try {
		        		if(con != null) {
		        			con.close();
		        		}
		        	} catch (Exception exp) {
						con = null;
					}
		        	
		        	con = conn;
		        	pstmt = con.prepareStatement("select pageId from Page LIMIT "+LIMIT+" offset ?");
		        	this.pageIdList = getPageIdList(offset);
		        	offset = offset + LIMIT;
		        } catch (Exception ex) {
					ex.printStackTrace();
				}
		}
		
		if(pageIdList.size() != 0) {
		pageId = pageIdList.get(0);
		pageIdList.remove(0);
		}
		
	}
	
	public List<Integer> getPageIdList(int offset) {
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
			e.printStackTrace();
		}
		return pageIdList;
	}

}
