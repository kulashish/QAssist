package com.aneedo.indexing;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

public class WikiIndexFSMerger {
	
	public static void main(String[] args) {
		Date start = new Date();
		try {

			String path;
			
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter the merged index path:");
			path = br.readLine();
			
			Analyzer analyzer = new SimpleAnalyzer(Version.LUCENE_30);
			File file = new File(path);

			Directory directory = new SimpleFSDirectory(file);

			IndexWriter iwriter = new IndexWriter(directory, analyzer, 
					new IndexWriter.MaxFieldLength(Integer.MAX_VALUE));
			iwriter.setMergeFactor(10000);
			iwriter.setMaxMergeDocs(Integer.MAX_VALUE);
			iwriter.setRAMBufferSizeMB(100);
			
			String index_path;
			Directory index;
			
			while(true){
				System.out.println("Enter the index Directory or 'exit' to quit");
				index_path = br.readLine();
				if(index_path.equals("exit")) break;
				
				
				File folder = new File(index_path);
				String filepath = "";
				File[] listOfFiles = folder.listFiles();
				for (int i = 0; i < listOfFiles.length; i++) {
					if (listOfFiles[i].isDirectory()) {
						filepath = index_path + listOfFiles[i].getName();
						try{
							index = new SimpleFSDirectory(new File(filepath));
							System.out.println("Merging " + filepath + " optimize");
							iwriter.addIndexes(index);
							System.out.println("Merging " + filepath + " done");
						}
						catch(Exception e){
							System.out.println("Index creation/merge failed for directiory " + filepath);
							e.printStackTrace();
						}
					}
				}
			}
			
			System.out.print("Optimizing index...");
	        try {
	        	iwriter.optimize();
	        	System.out.println("Optimzation successful ...");
	        } catch (Exception e) {
	        	System.out.println("Optimzation failed ...");
				e.printStackTrace();
			}
	        try {
	        	iwriter.close();
	        	System.out.println("Close successful ...");
	        } catch (Exception e) {
	        	System.out.println("Close failed ...");
			}
	        System.out.println("done all merging");

	        Date end = new Date();
	        System.out.println("Total indexing time with optimize: "+((end.getTime() - start.getTime()) / 1000));

		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}
