package com.aneedo.indexing.plainText;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
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

public class WikiPageIndexWriter {
	private static final String INDEX_PATH = "/mnt/bag3/kedhar/aneedo/indexing/wikipage/WikiIndexing2110";//"/home/ambha/workspace/check/testIndex";
	private static final String ERROR_FILE = "/mnt/bag3/kedhar/aneedo/indexing/wikipage/log/FSIndexWriterLog2110.log";
	private static final String LOG_FILE = "/mnt/bag3/kedhar/aneedo/indexing/wikipage/log/log2110.log";

	private IndexWriter iwriter = null;
	

	private PreparedStatement pageCategoryStmt = null;

	BufferedWriter errorWriter = null;
	BufferedWriter logWriter = null;
	private int offset = 0;
	private static final int LIMIT = 1000;

	private static final int MAX_RECORD = 3678909;//000;//31898077;// 3678909;

	private List<Integer> pageIdList = new ArrayList<Integer>();
	private Integer pageId = 0;
	private PreparedStatement pstmt = null;

	public static void main(String[] args) throws Exception {
		System.out.println("Indexing ....");
		WikiPageIndexWriter pageWriter = new WikiPageIndexWriter();

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
				Field.Store.YES, true);
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
		pageWriter.iwriter.setRAMBufferSizeMB(1000);

		pageWriter.errorWriter = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(ERROR_FILE), "UTF8"));
		pageWriter.logWriter = new BufferedWriter(new FileWriter(LOG_FILE));

		pageWriter.pstmt = con
				.prepareStatement("select pageId from Page LIMIT " + LIMIT
						+ " offset ?");
		pageWriter.pageCategoryStmt = con
				.prepareStatement(JwplSQLConstants.SELECT_PLAIN_PAGE);

		int count = 0;
		System.out.println("MAX_RECORD:"+ MAX_RECORD);
		while (count < MAX_RECORD) {
			pageWriter.nextPageIdForProcessing();
			pageWriter.logWriter.write(pageWriter.pageId+"\n");
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
				pageWriter.logWriter.write(pageWriter.pageId+"\n");
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
			pageWriter.logWriter.flush();
			pageWriter.logWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("indexing done..");
	}

	public void nextPageIdForProcessing() {
		if (pageIdList.size() == 0) {
			pageIdList = getPageIdList(offset);
			System.out.println("offset:"+offset);
			
			System.out.println("doing till ..." + offset);
			offset = offset + LIMIT;
			// If the last fetch size will be less than LIMIT
//			if (pageIdList.size() < LIMIT) {
//				System.out.println("Inside if");
//				pageIdList.add(MAX_RECORD + 1);
//			}
		}
		pageId = pageIdList.get(0);
		pageIdList.remove(0);
	}

	public List<Integer> getPageIdList(int offset) {
		List<Integer> pageIdList = new ArrayList<Integer>();
		try {
			pstmt.setInt(1, offset);
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
