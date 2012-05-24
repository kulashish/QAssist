package com.aneedo.indexing;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import com.aneedo.indexing.bean.SemanticClass;
import com.aneedo.indexing.bean.WikiLink;
import com.aneedo.indexing.bean.WikiPageBean;
import com.aneedo.indexing.bean.WikiSection;

public class RAMDirWriterThread extends Thread {
	private IndexWriter ramWriter = null;
	RAMDirectory ramDir = new RAMDirectory();
	boolean isInProgress = false;
	
	WikiPageExtractor extractor = null;
	WikiPageBean pageBean = new WikiPageBean();
	BufferedWriter errorWriter = null;
	Integer pageId = null;

	public void setPageId(Integer pageId) {
		this.pageId = pageId;
	}

	Document doc = new Document();

	Field titleField = new Field(IndexingConstants.PAGE_TITLE, "",
			Field.Store.YES, Field.Index.ANALYZED);
	Field idField = new Field(IndexingConstants.PAGE_ID,"",
			Field.Store.YES, Field.Index.NOT_ANALYZED);
	Field titleDismbField = new Field(IndexingConstants.PAGE_TITLE_DISAMB, "",
			Field.Store.YES, Field.Index.NOT_ANALYZED);
	Field abstractField = new Field(IndexingConstants.SEM_CLASS_SYNOPSIS, "",
			Field.Store.YES, Field.Index.ANALYZED);
	// Field noOfSections = new Field(IndexingConstants.NUM_OF_SECTION, "",
	// Field.Store.YES, Field.Index.NOT_ANALYZED);

	/*
	 * TODO Section wise to store you have to differentiate link, text etc
	 * corresponding to section
	 */
	Field secHeadingField = new Field(IndexingConstants.SECTION_HEADING, "",
			Field.Store.YES, Field.Index.ANALYZED);
	Field linkTextField = new Field(IndexingConstants.SECTION_LINK_TEXT, "",
			Field.Store.YES, Field.Index.ANALYZED);
	Field linkContextField = new Field(IndexingConstants.LINK_LEFT_CONTEXT,
			"", Field.Store.YES, Field.Index.ANALYZED,
			Field.TermVector.WITH_POSITIONS_OFFSETS);

	Field classSynonymField = new Field(IndexingConstants.SEM_CLASS_SYNONYM,
			"", Field.Store.YES, Field.Index.ANALYZED);
	Field classSynonymWordnetField = new Field(IndexingConstants.SEM_CLASS_SYNONYM_WORDNET,
			"", Field.Store.YES, Field.Index.ANALYZED);
	Field classHomonymField = new Field(IndexingConstants.SEM_CLASS_HOMONYM,
			"", Field.Store.YES, Field.Index.ANALYZED);
	Field classAsctLinkField = new Field(
			IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK, "",
			Field.Store.YES, Field.Index.ANALYZED);
	Field classAsctExLinkField = new Field(
			IndexingConstants.SEM_CLASS_REFERENCE, "",
			Field.Store.YES, Field.Index.ANALYZED);
	Field classRelatedField = new Field(
			IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED, "",
			Field.Store.YES, Field.Index.ANALYZED);
	Field classAsctRelatedHrchyField = new Field(
			IndexingConstants.SEM_CLASS_ASSOCIATION_HIERARCHY, "",
			Field.Store.YES, Field.Index.ANALYZED);
	Field classFrequentField = new Field(IndexingConstants.SEM_CLASS_FREQUENT,
			"", Field.Store.YES, Field.Index.ANALYZED);
	Field classHyperField = new Field(IndexingConstants.SEM_CLASS_HYPERNYM, "",
			Field.Store.YES, Field.Index.ANALYZED);
	Field classHypoField = new Field(IndexingConstants.SEM_CLASS_HYPONYM, "",
			Field.Store.YES, Field.Index.ANALYZED);
	Field classRoleField = new Field(IndexingConstants.SEM_CLASS_ROLE, "",
			Field.Store.YES, Field.Index.ANALYZED);
	Field classMeronymField = new Field(IndexingConstants.SEM_CLASS_MERONYM,
			"", Field.Store.YES, Field.Index.ANALYZED);
	Field classSiblingField = new Field(IndexingConstants.SEM_CLASS_SIBLING,
			"", Field.Store.YES, Field.Index.ANALYZED);

	int count = 0;

	public RAMDirWriterThread(int threadId) throws Exception {
		setName(threadId + "");
		String LOG_FILE = "/home/ambha/aneedo/indexing/semantic/log/RAMIndexWriterLog"
				+ threadId + ".log";

		errorWriter = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(LOG_FILE), "UTF8"));

		Analyzer analyzer = new SimpleAnalyzer(Version.LUCENE_30);

		ramWriter = new IndexWriter(ramDir, analyzer, true,
				new IndexWriter.MaxFieldLength(Integer.MAX_VALUE));
		
		
	}

	@Override
	public void run() {
		isInProgress = true;
		count++;
		try {
			// init page details from JWPL
			// TODO rename SEM_CLASS_ASSOCIATION_HIERARCHY instead of SEM_CLASS_ASSOCIATION_RELATED_HIERARCHY in all places
			pageBean.setPageId(pageId);
			extractor.initWikiPageDetails(pageBean,errorWriter);
			try {
			// Set all fields to lucene and add docs
			
			// set feilds
			titleField.setValue(pageBean.getPageTitle());
			idField.setValue(pageBean.getPageId().toString());
			//abstractField.setValue(pageBean.getAbstractText());
			titleDismbField.setValue(pageBean.getTitleDisambText());

			//set sections
			List<WikiSection> wikiSections = pageBean.getSections();
			WikiSection section = null;
			List<WikiLink> wikiLinks = null;
			WikiLink wikiLink = null;
			for (int i = 0, size = wikiSections.size(); i < size; i++) {
				section = wikiSections.get(i);
				secHeadingField.setValue(section.getSectionName());
//				wikiLinks = section.getInternalLinks();
//
//				if (wikiLinks != null) {
//					for (int j = 0, linkSize = wikiLinks.size(); j < linkSize; j++) {
//						wikiLink = wikiLinks.get(j);
//						linkTextField.setValue(wikiLink.getLinkText());
//						linkContextField.setValue(wikiLink.getLeftContext());
//					}
//				}

			}

			// set sem classes
			Map<String, SemanticClass> semClassMap = pageBean.getSemClassMap();
			classSynonymField.setValue(semClassMap.get(
					IndexingConstants.SEM_CLASS_SYNONYM).getWords().toString());
			classSynonymWordnetField.setValue(semClassMap.get(
					IndexingConstants.SEM_CLASS_SYNONYM_WORDNET).getWords().toString());
			classHomonymField.setValue(semClassMap.get(
					IndexingConstants.SEM_CLASS_HOMONYM).getWords().toString());
			classAsctLinkField.setValue(semClassMap.get(
					IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK).getWords()
					.toString());
			classAsctExLinkField.setValue(semClassMap.get(
					IndexingConstants.SEM_CLASS_REFERENCE).getWords()
					.toString());
			classRelatedField.setValue(semClassMap.get(
					IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED).getWords()
					.toString());
			classAsctRelatedHrchyField.setValue(semClassMap.get(
					IndexingConstants.SEM_CLASS_ASSOCIATION_HIERARCHY)
					.getWords().toString());
			classFrequentField
					.setValue(semClassMap.get(
							IndexingConstants.SEM_CLASS_FREQUENT).getWords()
							.toString());
			classHyperField
					.setValue(semClassMap.get(
							IndexingConstants.SEM_CLASS_HYPERNYM).getWords()
							.toString());
			classHypoField.setValue(semClassMap.get(
					IndexingConstants.SEM_CLASS_HYPONYM).getWords().toString());
			classRoleField.setValue(semClassMap.get(
					IndexingConstants.SEM_CLASS_ROLE).getWords().toString());
			classMeronymField.setValue(semClassMap.get(
					IndexingConstants.SEM_CLASS_MERONYM).getWords().toString());
			classSiblingField.setValue(semClassMap.get(
					IndexingConstants.SEM_CLASS_SIBLING).getWords().toString());
			// Add fields
			doc.add(titleField);
			doc.add(idField);
			doc.add(abstractField);
			doc.add(titleDismbField);

			doc.add(secHeadingField);
			doc.add(linkTextField);
			doc.add(linkContextField);
			
			doc.add(classSynonymField);
			doc.add(classSynonymWordnetField);
			doc.add(classAsctLinkField);
			doc.add(classAsctExLinkField);
			doc.add(classHomonymField);
			doc.add(classRelatedField);
			doc.add(classAsctRelatedHrchyField);
			doc.add(classFrequentField);
			doc.add(classHyperField);
			doc.add(classHypoField);
			doc.add(classRoleField);
			doc.add(classMeronymField);
			doc.add(classSiblingField);
			
			ramWriter.addDocument(doc);
			doc = new Document();

			// clean for the next run
			pageBean.clearAll();

		} catch (CorruptIndexException e1) {
			try {
				errorWriter.write("Writing of page with id " + pageId
						+ " failed : " + e1.getMessage() + " "
						+ e1.getCause().toString() + "\n");
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		} catch (IOException e1) {
			try {
				errorWriter.write("Writing of page with id " + pageId
						+ " failed : " + e1.getMessage() + " "
						+ e1.getCause().toString() + "\n");
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}

		if (count == 10) {
			try {
				
				ramWriter.optimize();
			} catch (Exception e) {
				try {
					errorWriter
							.write("RAM Directory flush failed end at page id : "
									+ pageId
									+ e.getMessage()
									+ " "
									+ e.getCause().toString() + "\n");
				} catch (Exception exp) {
					exp.printStackTrace();
				}
			}
			try {
				ramWriter.commit();
				WikiFSIndexWriter.getInstance().mergeIndexes(ramDir);
			} catch (Exception e) {
				try {
					errorWriter
							.write("RAM Directory flush failed end at page id : "
									+ pageId
									+ e.getMessage()
									+ " "
									+ e.getCause().toString() + "\n");
				} catch (Exception exp) {
					exp.printStackTrace();
				}
			}

				// clean for next 10
			try {
				ramWriter.deleteAll();
				ramWriter.commit();
			} catch (Exception e) {
				try {
					errorWriter
							.write("RAM Directory flush failed end at page id : "
									+ pageId
									+ e.getMessage()
									+ " "
									+ e.getCause().toString() + "\n");
				} catch (Exception exp) {
					exp.printStackTrace();
				}
			}
				count = 0;
				//extractor.closeConnection(errorWriter, pageId);
				//extractor.setNewConnection(errorWriter, pageId);
				
//				// close the old connection and create new, keeping connection for a long is not recommended
//				try {
//				Connection con = getNewConnection(errorWriter);
//				closeConnection();
//				conn = con;
//				} catch (Exception e) {
//					try {
//						errorWriter
//								.write("connection opening falied at page id : "
//										+ pageId
//										+ e.getMessage()
//										+ " "
//										+ e.getCause().toString() + "\n");
//					} catch (Exception exp) {
//						exp.printStackTrace();
//					}
//				}

			
		}
		isInProgress = false;
	} catch (Exception e) {
		try {
			errorWriter
					.write("JWPS initialization falied at page id : "
							+ pageId
							+ e.getMessage()
							+ " "
							+ e.getCause().toString() + "\n");
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		System.out.println("Problem in JWPS initialization ....");
		System.exit(0);
	}
	}

	public boolean getStatus() {
		return isInProgress;
	}
	
	public void closeLogFiles() {
		try {
			errorWriter.flush();
			errorWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	private Connection getNewConnection(BufferedWriter errorWriter) throws Exception {
//		
//		return DBConnection.getInstance().getConnection();
//		
//	
//	}
	
//	private void closeConnection() {
//		try {
//		DBConnection.getInstance().close(conn);
//		conn = null;
//		} catch (Exception e) {
//			try {
//				errorWriter
//						.write("connection closing falied at page id : "
//								+ pageId
//								+ e.getMessage()
//								+ " "
//								+ e.getCause().toString() + "\n");
//			} catch (Exception exp) {
//				exp.printStackTrace();
//			}
//		}
//	}

}
