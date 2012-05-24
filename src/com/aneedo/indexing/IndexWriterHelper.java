package com.aneedo.indexing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import com.aneedo.indexing.bean.SemanticClass;
import com.aneedo.indexing.bean.WikiPageBean;
import com.aneedo.indexing.bean.WikiSection;
import com.aneedo.search.util.SentimentRepos;

public class IndexWriterHelper {
	// private IndexWriter ramWriter = null;
	// RAMDirectory ramDir = new RAMDirectory();
	boolean isInProgress = false;

	WikiPageExtractor extractor = null;
	WikiPageBean pageBean = new WikiPageBean();
	BufferedWriter errorWriter = null;
	Integer pageId = null;
	IndexWriter fsIndexWriter = null;

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
	Field abstractVbAdjField = new Field(IndexingConstants.SYNOPSIS_VB_ADJ, "",
			Field.Store.YES, Field.Index.ANALYZED);
	Field pageTypesField = new Field(IndexingConstants.PAGE_TYPES, "",
			Field.Store.YES, Field.Index.ANALYZED);
	

	// Field noOfSections = new Field(IndexingConstants.NUM_OF_SECTION, "",
	// Field.Store.YES, Field.Index.NOT_ANALYZED);

	/*
	 * TODO Section wise to store you have to differentiate link, text etc
	 * corresponding to section
	 */
	Field secHeadingField = new Field(IndexingConstants.SECTION_HEADING, "",
			Field.Store.YES, Field.Index.ANALYZED);
	Field imgTextField = new Field(IndexingConstants.IMAGE_TEXT, "",
		Field.Store.YES, Field.Index.ANALYZED);
//	Field linkTextField = new Field(IndexingConstants.SECTION_LINK_TEXT, "",
//			Field.Store.YES, Field.Index.ANALYZED);
//	Field linkLeftContextField = new Field(IndexingConstants.LINK_LEFT_CONTEXT,
//			"", Field.Store.YES, Field.Index.ANALYZED,
//			Field.TermVector.WITH_POSITIONS_OFFSETS);
//	Field linkRightContextField = new Field(IndexingConstants.LINK_RIGHT_CONTEXT,
//			"", Field.Store.YES, Field.Index.ANALYZED,
//			Field.TermVector.WITH_POSITIONS_OFFSETS);

	Field classSynonymField = new Field(IndexingConstants.SEM_CLASS_SYNONYM,
			"", Field.Store.YES, Field.Index.ANALYZED);
	Field classSynonymWordnetField = new Field(
			IndexingConstants.SEM_CLASS_SYNONYM_WORDNET, "", Field.Store.YES,
			Field.Index.ANALYZED);
	Field classHomonymField = new Field(IndexingConstants.SEM_CLASS_HOMONYM,
			"", Field.Store.YES, Field.Index.ANALYZED);
	Field classAsctLinkField = new Field(
			IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK, "",
			Field.Store.YES, Field.Index.ANALYZED);
	Field classReferenceField = new Field(
			IndexingConstants.SEM_CLASS_REFERENCE, "", Field.Store.YES,
			Field.Index.ANALYZED);
	Field classRelatedField = new Field(
			IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED, "",
			Field.Store.YES, Field.Index.ANALYZED);
	Field classCooccurrenceField = new Field(
			IndexingConstants.SEM_CLASS_COOCCURRENCE, "",
			Field.Store.YES, Field.Index.ANALYZED);
	Field classAsctLinkParentField = new Field(
			IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK_PARENT, "",
			Field.Store.YES, Field.Index.ANALYZED);

	Field classAsctHrchyField = new Field(
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
	Field classMakeProduceField = new Field(IndexingConstants.SEM_CLASS_MAKEPRODUCE,
			"", Field.Store.YES, Field.Index.ANALYZED);
	Field classSiblingField = new Field(IndexingConstants.SEM_CLASS_SIBLING,
			"", Field.Store.YES, Field.Index.ANALYZED);
	Field classCauseEffectField = new Field(IndexingConstants.SEM_CLASS_CAUSE_EFFECT,
			"", Field.Store.YES, Field.Index.ANALYZED);
	Field classProfitBenefitField = new Field(IndexingConstants.SEM_CLASS_PROFIT_BENEFIT,
			"", Field.Store.YES, Field.Index.ANALYZED);
	
	Field noOfInlinks = new Field(IndexingConstants.NO_OF_INLINKS,
			"", Field.Store.YES, Field.Index.NO);
	Field noOfOutlinks = new Field(IndexingConstants.NO_OF_OUTLINKS,
			"", Field.Store.YES, Field.Index.NO);
	Field NEType = new Field(IndexingConstants.NE_TYPE,
			"", Field.Store.YES, Field.Index.NO);
	
	SentimentRepos sentRepos = null;
	

	int count = 0;

	public IndexWriterHelper(int offset, int threadId, String indexingPath,String dbName,String password,String url) throws Exception {
		// setName(threadId + "");
		String LOG_FILE = indexingPath+"/log/Indexing"+offset+ threadId + ".log";

		errorWriter = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(LOG_FILE), "UTF8"));
		
		Analyzer analyzer = new SimpleAnalyzer(Version.LUCENE_30);
		File file = new File(indexingPath+"/WikiIndexing"+offset+threadId);
		Directory directory = new SimpleFSDirectory(file);
		IndexWriter iwriter = new IndexWriter(directory, analyzer, true,
				new IndexWriter.MaxFieldLength(Integer.MAX_VALUE));
		iwriter.setMergeFactor(10000);
		iwriter.setMaxMergeDocs(Integer.MAX_VALUE);
		iwriter.setRAMBufferSizeMB(100);
		
		this.fsIndexWriter = iwriter;
		this.extractor = new WikiPageExtractor(dbName,password,errorWriter,indexingPath,url);
		sentRepos = SentimentRepos.getInstance(indexingPath);

		// ramWriter = new IndexWriter(ramDir, analyzer, true,
		// new IndexWriter.MaxFieldLength(Integer.MAX_VALUE));

	}

	public void run() {
		isInProgress = true;
		
		//count++;
		try {
			// init page details from JWPL
			pageBean.setPageId(pageId);
			extractor.initWikiPageDetails(pageBean, errorWriter);
			try {
				// Set all fields to lucene and add docs

				// set feilds
				if (pageBean.getPageTitle() != null
						&& !"".equals(pageBean.getPageTitle())) {
					titleField.setValue(pageBean.getPageTitle());
					//System.out.println("Page title : " + pageBean.getPageTitle());
					doc.add(titleField);
				}
				if (pageBean.getPageId() != 0) {
					idField.setValue(pageBean.getPageId().toString());
					//System.out.println();
					doc.add(idField);
				}

				if (pageBean.getNoOfInlinks() > 0) {
					noOfInlinks.setValue(pageBean.getNoOfInlinks().toString());
					doc.add(noOfInlinks);
				}

				if (pageBean.getNoOfOutlinks() > 0) {
					noOfOutlinks.setValue(pageBean.getNoOfOutlinks().toString());
					doc.add(noOfOutlinks);
				}
				
				if (pageBean.getNEType() != null && !"".equals(pageBean.getNEType().trim())) {
					NEType.setValue(pageBean.getNEType());
					doc.add(NEType);
				}

//				if (pageBean.getPageId() != 0) {
//					idField.setValue(pageBean.getPageId().toString());
//					//System.out.println();
//					doc.add(idField);
//				}
				//System.out.println("Abstract VB/ADJ " + pageBean.getAbstractVbAdj());
				if (pageBean.getAbstractVbAdj() != null
						&& !"".equals(pageBean.getAbstractVbAdj())) {
					abstractVbAdjField.setValue(pageBean.getAbstractVbAdj());
					doc.add(abstractVbAdjField);
				}
				if (pageBean.getTitleDisambText() != null
						&& !"".equals(pageBean.getTitleDisambText())) {
					titleDismbField.setValue(pageBean.getTitleDisambText());
					doc.add(titleDismbField);
				}
				
				// Add page type
				StringBuilder pageType = new StringBuilder();
				for (int j = 0, size = pageBean.getPageTypes().size(); j < size; j++) {
					pageType.append(pageBean.getPageTypes().get(j)+" | ");
				}
				
				if(pageType.length() > 0) {
					pageTypesField.setValue(pageType.toString());
					doc.add(pageTypesField);
				}

				// set sections
				List<WikiSection> wikiSections = pageBean.getSections();
				WikiSection section = null;
				
				final StringBuilder sectionBuilder = new StringBuilder();
				final StringBuilder imgBuilder = new StringBuilder();
				final StringBuilder causeEffectBuilder = new StringBuilder();
				final StringBuilder profitBenefitBuilder = new StringBuilder();

				for (int i = 0, size = wikiSections.size(); i < size; i++) {
					section = wikiSections.get(i);
					final String secName = section.getSectionName(); 
					if (secName != null
							&& !"".equals(secName)) {
						sectionBuilder.append(secName +" | ");
						if(section.getImageText() !=null )
							imgBuilder.append(section.getImageText());
						final int sentScore = sentRepos.getSentiment(secName);
						if(sentScore < 0) {
							causeEffectBuilder.append(secName +" | ");
						} else if(sentScore > 0) {
							profitBenefitBuilder.append(secName+" | ");
						}
					}
				}

				if(sectionBuilder.length()>5) {
				secHeadingField.setValue(sectionBuilder.toString());
				doc.add(secHeadingField);
				}
				
				if(imgBuilder.length()>4) {
					imgTextField.setValue(imgBuilder.toString());
					doc.add(imgTextField);
				}

				if(causeEffectBuilder.length()>0) {
				classCauseEffectField.setValue(causeEffectBuilder.toString());
				doc.add(classCauseEffectField);
				}

				if(profitBenefitBuilder.length()>0) {
				classProfitBenefitField.setValue(profitBenefitBuilder.toString());
				doc.add(classProfitBenefitField);
				}

				// set sem classes
				Map<String, SemanticClass> semClassMap = pageBean
						.getSemClassMap();
//				System.out.println("Abstract field : " + semClassMap.get(
//							IndexingConstants.SEM_CLASS_SYNOPSIS).getWords()
//							.toString());
				if (semClassMap.get(IndexingConstants.SEM_CLASS_SYNOPSIS)
						.getWords().length() > 2) {
					abstractField.setValue(semClassMap.get(
							IndexingConstants.SEM_CLASS_SYNOPSIS).getWords()
							.toString());
					doc.add(abstractField);
				}

				
				if (semClassMap.get(IndexingConstants.SEM_CLASS_SYNONYM)
						.getWords().length() > 2) {
					classSynonymField.setValue(semClassMap.get(
							IndexingConstants.SEM_CLASS_SYNONYM).getWords()
							.toString());
					doc.add(classSynonymField);
				}

				if (semClassMap.get(IndexingConstants.SEM_CLASS_SYNONYM_WORDNET)
						.getWords().length() > 2) {
					classSynonymWordnetField.setValue(semClassMap.get(
							IndexingConstants.SEM_CLASS_SYNONYM_WORDNET)
							.getWords().toString());
					doc.add(classSynonymWordnetField);
				}

				if (semClassMap.get(IndexingConstants.SEM_CLASS_HOMONYM)
						.getWords().length() > 4) {
					classHomonymField.setValue(semClassMap.get(
							IndexingConstants.SEM_CLASS_HOMONYM).getWords()
							.toString());
					doc.add(classHomonymField);
				}
				if (semClassMap.get(IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK)
						.getWords().length() > 2) {
					classAsctLinkField.setValue(semClassMap.get(
							IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK)
							.getWords().toString());
					doc.add(classAsctLinkField);
				}
				
				if (semClassMap.get(IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK_PARENT)
						.getWords().length() > 2) {
					classAsctLinkParentField.setValue(semClassMap.get(
							IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK_PARENT)
							.getWords().toString());
					doc.add(classAsctLinkParentField);
				}
				
				if (semClassMap.get(IndexingConstants.SEM_CLASS_REFERENCE)
						.getWords().length() > 2) {
					classReferenceField.setValue(semClassMap.get(
							IndexingConstants.SEM_CLASS_REFERENCE).getWords()
							.toString());
					doc.add(classReferenceField);
				}
				if (semClassMap.get(IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED)
						.getWords().length() > 2) {
					classRelatedField.setValue(semClassMap.get(
							IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED)
							.getWords().toString());
					doc.add(classRelatedField);
				}
				if (semClassMap.get(IndexingConstants.SEM_CLASS_COOCCURRENCE)
						.getWords().length() > 2) {
					classCooccurrenceField.setValue(semClassMap.get(
							IndexingConstants.SEM_CLASS_COOCCURRENCE)
							.getWords().toString());
					doc.add(classCooccurrenceField);
				}
				if (semClassMap.get(IndexingConstants.SEM_CLASS_ASSOCIATION_HIERARCHY)
						.getWords().length() > 2) {
					classAsctHrchyField
							.setValue(semClassMap
									.get(
											IndexingConstants.SEM_CLASS_ASSOCIATION_HIERARCHY)
									.getWords().toString());
					doc.add(classAsctHrchyField);
				}
				if (semClassMap.get(IndexingConstants.SEM_CLASS_FREQUENT)
						.getWords().length() > 2) {
					classFrequentField.setValue(semClassMap.get(
							IndexingConstants.SEM_CLASS_FREQUENT).getWords()
							.toString());
					doc.add(classFrequentField);
				}
				if (semClassMap.get(IndexingConstants.SEM_CLASS_HYPERNYM)
						.getWords().length() > 2) {
					classHyperField.setValue(semClassMap.get(
							IndexingConstants.SEM_CLASS_HYPERNYM).getWords()
							.toString());
					doc.add(classHyperField);
				}
				if (semClassMap.get(IndexingConstants.SEM_CLASS_HYPONYM)
						.getWords().length() > 2) {
					classHypoField.setValue(semClassMap.get(
							IndexingConstants.SEM_CLASS_HYPONYM).getWords()
							.toString());
					doc.add(classHypoField);
				}
				if (semClassMap.get(IndexingConstants.SEM_CLASS_ROLE)
						.getWords().length() > 2) {
					classRoleField.setValue(semClassMap.get(
							IndexingConstants.SEM_CLASS_ROLE).getWords()
							.toString());
					doc.add(classRoleField);
				}
				if (semClassMap.get(IndexingConstants.SEM_CLASS_MERONYM)
						.getWords().length() > 2) {
					classMeronymField.setValue(semClassMap.get(
							IndexingConstants.SEM_CLASS_MERONYM).getWords()
							.toString());
					doc.add(classMeronymField);
				}
				
				if (semClassMap.get(IndexingConstants.SEM_CLASS_MAKEPRODUCE)
						.getWords().length() > 2) {
					classMakeProduceField.setValue(semClassMap.get(
							IndexingConstants.SEM_CLASS_MAKEPRODUCE).getWords()
							.toString());
					doc.add(classMakeProduceField);
				}
				
				
				if (semClassMap.get(IndexingConstants.SEM_CLASS_SIBLING)
						.getWords().length() > 2) {
					classSiblingField.setValue(semClassMap.get(
							IndexingConstants.SEM_CLASS_SIBLING).getWords()
							.toString());
					doc.add(classSiblingField);
				}


				fsIndexWriter.addDocument(doc);
				doc = new Document();
				// clean for the next run
				pageBean.clearAll();
				count++;
				
				if(count == 100) {
				extractor.setNewConnection(errorWriter);
				}

			} catch (Exception e1) {
				try {
					errorWriter.write("Writing of page with id " + pageId
							+ " failed : " + e1.getMessage() + " "
							+ e1.getCause() + "\n");
				} catch (Exception exp) {
					exp.printStackTrace();
				}
			}

			isInProgress = false;
		} catch (Exception e) {
			try {
				errorWriter.write("JWPS initialization falied at page id : "
						+ pageId + e.getMessage() + " "
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

	public void closeResources() {
		System.out.println("Closing connection and statements...");
		extractor.closeDB(errorWriter);
		try {
			fsIndexWriter.optimize();

	} catch (Exception e) {
		try {
			errorWriter.write("Optimization of FS writer failed : " + e.getMessage()
					+ " " + e.getCause().toString() + "\n");
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
		try {
			fsIndexWriter.close();
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
