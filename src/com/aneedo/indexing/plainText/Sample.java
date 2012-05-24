package com.aneedo.indexing.plainText;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import com.aneedo.jwplext.JwplSQLConstants;

public class Sample {
	private static final String INDEX_PATH = "/home/ambha/workspace/check/testIndex";//"/mnt/bag3/kedhar/aneedo/indexing/wikipage/CheckWikiIndexing";
	private static final String LOG_FILE = "/home/ambha/workspace/check/log/check.log";//"/mnt/bag3/kedhar/aneedo/indexing/wikipage/log/FSCheckIndexWriterLog.log";

	private IndexWriter iwriter = null;

	private PreparedStatement pageCategoryStmt = null;
	private int temp=0;

	BufferedWriter errorWriter = null;
	private int current = 0;
	private static int offset = 100;

	private static final int MAX_RECORD = 225+offset;//000;//31898077;// 3678909;

	private List<Integer> pageIdList = new ArrayList<Integer>();
	private Integer pageId = 0;
	private PreparedStatement pstmt = null;

	public static void main(String[] args) throws Exception {
		System.out.println("Indexing ....");
		Sample pageWriter = new Sample();

		Connection con = null;
		String url = "jdbc:mysql://localhost:3306/";
		String db = "wikipedia";
		String driver = "com.mysql.jdbc.Driver";
		String user = "root";
		String pass = "aneedo";
		Document doc = new Document();

		Field titleField = new Field(TextIndexingConstants.PAGE_TITLE, "",
				Field.Store.YES, Field.Index.ANALYZED);
		NumericField idField = new NumericField(TextIndexingConstants.PAGE_ID,
				Field.Store.YES, false);
		Field textField = new Field(TextIndexingConstants.PAGE_TEXT, "",
				Field.Store.YES, Field.Index.ANALYZED);

		Class.forName(driver);
		con = DriverManager.getConnection(url + db, user, pass);
		Analyzer analyzer = new SimpleAnalyzer(Version.LUCENE_30);
		File file = new File(INDEX_PATH);
		Directory directory = new SimpleFSDirectory(file);

		pageWriter.iwriter = new IndexWriter(directory, analyzer, true,
				new IndexWriter.MaxFieldLength(Integer.MAX_VALUE));
		pageWriter.iwriter.setMergeFactor(10000);
		pageWriter.iwriter.setMaxMergeDocs(Integer.MAX_VALUE);
		pageWriter.iwriter.setRAMBufferSizeMB(100);

		pageWriter.errorWriter = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(LOG_FILE), "UTF8"));

		pageWriter.pstmt = con
				.prepareStatement("select pageId from Page LIMIT ? offset ?");
		pageWriter.pageCategoryStmt = con
				.prepareStatement(JwplSQLConstants.SELECT_PLAIN_PAGE);

		int count = 0;
		System.out.println("MAX_RECORD:"+ MAX_RECORD);
		while ((pageWriter.current + offset) <= MAX_RECORD) {
			System.out.println("offset+Limit:"+(pageWriter.current + offset));

			pageWriter.nextPageIdForProcessing();
			JwplPlainPage page = null;
			try {
				page = PlainPageDao.getInstance().getJwplPlainPageDetails(
						pageWriter.pageId, pageWriter.pageCategoryStmt,
						pageWriter.errorWriter);
				
				titleField.setValue(page.getPageTitle());
				doc.add(titleField);
				idField.setIntValue(page.getPageId());
				System.out.println("Id:"+page.getPageId());
				doc.add(idField);
				textField.setValue(page.getPageText());
				doc.add(textField);
				
				pageWriter.iwriter.addDocument(doc);
				doc = new Document();
				
				System.out.println("Indexing document count : " + ++count);
			} catch (Exception e) {
				try {
					pageWriter.errorWriter.write("Accessing page with id : "
							+ pageWriter.pageId + " failed : " + e.getMessage()
							+ " " + e.getCause() + "\n");
				} catch (Exception exp) {
					exp.printStackTrace();
				}
			}

		}

		try {
			pageWriter.iwriter.optimize();

		} catch (Exception e) {
			try {
				pageWriter.errorWriter
						.write("Optimization of FS writer failed : "
								+ e.getMessage() + " "
								+ e.getCause().toString() + "\n");
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}
		try {
			pageWriter.iwriter.close();
			System.out.println("File indexer closed....");
		} catch (Exception e) {
			try {
				pageWriter.errorWriter
						.write("Closing of FS Writer failed : "
								+ e.getMessage() + " "
								+ e.getCause().toString() + "\n");
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}
		try {
			pageWriter.errorWriter.flush();
			pageWriter.errorWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("indexing done..");
	}

	public void nextPageIdForProcessing() {
		System.out.println("List size:"+pageIdList.size());
		if (pageIdList.size() == 0) {
			pageIdList = getPageIdList(current);
			System.out.println("size:"+pageIdList.size());
			current = current + pageIdList.size();
			System.out.println("doing till ..." + current);	
						
		}
		pageId = pageIdList.get(0);
		pageIdList.remove(0);
	}

	public List<Integer> getPageIdList(int current) {
		List<Integer> pageIdList = new ArrayList<Integer>();
		try {
			offset=Math.min(offset,MAX_RECORD-current);
			pstmt.setInt(1, offset);
			pstmt.setInt(2, current);
			System.out.println("Getting from "+current+ "to" + (current+offset));
			ResultSet result = pstmt.executeQuery();
			if (result != null) {
				while (result.next()) {
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
