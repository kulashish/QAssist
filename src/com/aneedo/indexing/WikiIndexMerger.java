package com.aneedo.indexing;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

public class WikiIndexMerger {
	final static String path = "/mnt/bag1/querysystem/indexing/semantic/WikiIndexing";
	
	
	
	public static void main(String[] args) {
		Date start = new Date();
		 try {
		    	
		    	Analyzer analyzer = new SimpleAnalyzer(Version.LUCENE_30);
				File file = new File(path);

				Directory directory = new SimpleFSDirectory(file);

				IndexWriter iwriter = new IndexWriter(directory, analyzer, true,
						new IndexWriter.MaxFieldLength(Integer.MAX_VALUE));
				iwriter.setMergeFactor(10000);
				iwriter.setMaxMergeDocs(Integer.MAX_VALUE);
		        iwriter.setRAMBufferSizeMB(50);

		        Directory index = new SimpleFSDirectory(new File(path +1));
		        System.out.println("Merging Wiki 1");
		        iwriter.addIndexes(index);
		        System.out.println("Merging Wiki 1 done");
		        
		        index = new SimpleFSDirectory(new File(path + 7));
		        System.out.println("Merging Wiki 7 optimize");
		        iwriter.addIndexes(index);
		        System.out.println("Merging wiki 7 done");
		        
		        index = new SimpleFSDirectory(new File(path + 8));
		        System.out.println("Merging Wiki 8 optimize");
		        iwriter.addIndexes(index);
		        System.out.println("Merging wiki 8 done");
		        
		        index = new SimpleFSDirectory(new File(path + 9));
		        System.out.println("Merging Wiki 9 optimize");
		        iwriter.addIndexes(index);
		        System.out.println("Merging wiki 9 done");
		        
		        index = new SimpleFSDirectory(new File(path + 10));
		        System.out.println("Merging Wiki 10 optimize");
		        iwriter.addIndexes(index);
		        System.out.println("Merging wiki 10 done");
		        
		        index = new SimpleFSDirectory(new File(path + 11));
		        System.out.println("Merging Wiki 11 optimize");
		        iwriter.addIndexes(index);
		        System.out.println("Merging wiki 11 done");
		        
		        index = new SimpleFSDirectory(new File(path + 12));
		        System.out.println("Merging Wiki 12 optimize");
		        iwriter.addIndexes(index);
		        System.out.println("Merging wiki 12 done");
		        
		        index = new SimpleFSDirectory(new File(path + 13));
		        System.out.println("Merging Wiki 13 optimize");
		        iwriter.addIndexes(index);
		        System.out.println("Merging wiki 13 done");

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

		    } catch (IOException e) {
		        e.printStackTrace();
		    }
	}
	 
   

}
