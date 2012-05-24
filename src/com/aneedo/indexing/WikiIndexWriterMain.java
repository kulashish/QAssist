package com.aneedo.indexing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WikiIndexWriterMain {
	private static final ExecutorService execSvc = Executors.newCachedThreadPool();
	private static final int MAX_THREADS = 1;
	private static int offset = 0;

	private static final int MAX_RECORD = 100;//3678909;
	
	private static List<Integer> pageIdList = new ArrayList<Integer>();
	private static Integer pageId = 0;
	private static final int LIMIT = 20;
	private static PreparedStatement pstmt = null;
	
	public static void main(String[] args) throws Exception {
		//int count = MIN_PAGE_ID;
		//Integer pageId = MIN_PAGE_ID;
		System.out.println("Indexing ....");
		RAMDirWriterThread[] ramThreads = new RAMDirWriterThread[MAX_THREADS];
		
		Connection con = null;
        String url = "jdbc:mysql://localhost:3306/";
        String db = "wikipedia";
        String driver = "com.mysql.jdbc.Driver";
        String user = "root";
        String pass = "aneedo";
        try {
        Class.forName(driver);
        con = DriverManager.getConnection(url + db, user, pass);
        pstmt = con.prepareStatement("select pageId from Page LIMIT "+LIMIT+" offset ?");
        } catch (Exception e) {
			e.printStackTrace();
		}
		
		for(int i=0;i<ramThreads.length;i++) {
			ramThreads[i] = new RAMDirWriterThread(i+1);
		}
		int count = 0;
		while(pageId <= MAX_RECORD ) {
			for(int i=0;i<ramThreads.length;i++) {
				if(!ramThreads[i].getStatus()) {
					nextPageIdForProcessing();
					System.out.println("Count : " + count++);
					ramThreads[i].setPageId(pageId);
					execSvc.execute( ramThreads[i]);
				}
			}
			//count = count + 1;
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
				ramThreads[i].closeLogFiles(); 
			}
			try {
			WikiFSIndexWriter.getInstance().closeResources();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("indexing done..");
	}
	
	public static void nextPageIdForProcessing() {
		if(pageIdList.size() == 0) {
			pageIdList = getPageIdList(offset);
			System.out.println("doing till ..." + offset);
			offset = offset + LIMIT;
			
			// If the last fetch size will be less than LIMIT
			if(pageIdList.size() < LIMIT) {
				pageIdList.add(MAX_RECORD +1 );
			}
		}
		pageId = pageIdList.get(0);
		pageIdList.remove(0);
	}
	
	public static List<Integer> getPageIdList(int offset) {
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
