package com.aneedo.indexing;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WikiIndexSimulator {
	private static int MAX_THREADS = 1;
	private int MAX_RECORDS = 0;
	private int offset = 0;
	private static final int LIMIT = 25;
	private List<Integer> pageIdList = new ArrayList<Integer>();
	BufferedWriter errorWriter;
	
	public static void main(String[] args) {
		WikiIndexSimulator indexSim = new WikiIndexSimulator();
		try {
		indexSim.errorWriter = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("/home/ganesh_r/MainLog.txt"), "UTF8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		indexSim.offset = Integer.parseInt(args[0]);
		indexSim.MAX_RECORDS = Integer.parseInt(args[1]);
		
		String startTime = new Date(System.currentTimeMillis()).toString();
		IndexSimulatorHelper[] ramThreads = new IndexSimulatorHelper[MAX_THREADS];
		
		String indexingPath = "";
		
		for(int i=0;i<ramThreads.length;i++) {
			try {
			ramThreads[i] = new IndexSimulatorHelper(indexSim.offset,i+1,indexingPath);
			} catch (Exception e) {
				try {
				indexSim.errorWriter.write(e.toString());
				} catch (Exception exp) {
					exp.printStackTrace();
				}
			}
		}
		
		int count=0;
		
		for(int i=0;i<ramThreads.length;i++) {
			if(!ramThreads[i].getStatus()) {
				int page_id = indexSim.getnextPageId();
				ramThreads[i].setPageId(page_id);
				ramThreads[i].run();
				System.out.println("Count : " + count++);
			}
		}
		
	
			for(int i=0;i<ramThreads.length;i++) {
				ramThreads[i].close();
			}
		
		System.out.println("Indexing done ..... Closed all resources ...." + startTime +" end time : "+new Date(System.currentTimeMillis()));
		
	}
	
	public int getnextPageId() {
		int pageId;
		if(pageIdList.size() == 0) {
			pageIdList = getPageIdList(offset);
			//System.out.println("doing till ..." + offset);
			offset = offset + LIMIT;
			
		}
		pageId = pageIdList.get(0);
		pageIdList.remove(0);
		return pageId;
	}
	
	public List<Integer> getPageIdList(int offset) {
		List<Integer> pageIdList = new ArrayList<Integer>();
		for(int i=0; i<MAX_RECORDS; i++){
			pageIdList.add(i);
		}
		return pageIdList;
	}
}
