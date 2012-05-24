package com.aneedo.indexing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

public class WikiFSIndexWriter {

	private final String INDEX_PATH = "/data2/qassist/indexing/semantic/WikiIndexing";
	private final String LOG_FILE = "/data2/qassist/indexing/semantic/log/WikiIndexingLog.log";

	private IndexWriter iwriter = null;

	private static WikiFSIndexWriter instance = null;

	BufferedWriter errorWriter = null;

	public WikiFSIndexWriter() throws Exception {

		Analyzer analyzer = new SimpleAnalyzer(Version.LUCENE_30);
		File file = new File(INDEX_PATH);
		Directory directory = new SimpleFSDirectory(file);
		iwriter = new IndexWriter(directory, analyzer, true,
				new IndexWriter.MaxFieldLength(Integer.MAX_VALUE));
		iwriter.setMergeFactor(10000);
		iwriter.setMaxMergeDocs(Integer.MAX_VALUE);
		iwriter.setRAMBufferSizeMB(100);
		errorWriter = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(LOG_FILE), "UTF8"));
	}

	public static WikiFSIndexWriter getInstance() throws Exception {
		if (instance == null) {
			instance = new WikiFSIndexWriter();
		}
		return instance;
	}
	

	public boolean mergeIndexes(Directory directory) {
		try {
			iwriter.addIndexes(directory);
			return true;
		} catch (Exception e) {
			try {
				errorWriter.write("Merge falied" + e.getMessage() + " "
						+ e.getCause().toString() + "\n");
			} catch (Exception exp) {
				exp.printStackTrace();
			}
			return false;
		}
	}
	
	public boolean addDocument(Document document) {
		try {
			iwriter.addDocument(document);
			return true;
		} catch (Exception e) {
			try {
				errorWriter.write("Merge falied" + e.getMessage() + " "
						+ e.getCause().toString() + "\n");
			} catch (Exception exp) {
				exp.printStackTrace();
			}
			return false;
		}
	}

	public void closeResources() {
//		try {
//			iwriter.optimize();
//
//		} catch (Exception e) {
//			try {
//				errorWriter.write("Optimization of FS writer failed : " + e.getMessage()
//						+ " " + e.getCause().toString() + "\n");
//			} catch (Exception exp) {
//				exp.printStackTrace();
//			}
//		}
		try {
			iwriter.close();
			System.out.println("File indexer closed....");
		} catch (Exception e) {
			try {
				errorWriter.write("Closing of FS Writer failed : " + e.getMessage()
						+ " " + e.getCause().toString() + "\n");
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}
		try {
			errorWriter.flush();
			errorWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
