package com.aneedo.indexing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import com.aneedo.search.util.SentimentRepos;

public class IndexSimulatorHelper {

	Document doc;
	IndexWriter index_writer;
	BufferedWriter errorWriter;
	Integer pageId;
	SentimentRepos sentRepos;
	int t_id;
	
	boolean isInProgress = false;
	
	public void setPageId(Integer pageid) {
		pageId = pageid;
	}

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
	Field secHeadingField = new Field(IndexingConstants.SECTION_HEADING, "",
			Field.Store.YES, Field.Index.ANALYZED);
	Field imgTextField = new Field(IndexingConstants.IMAGE_TEXT, "",
			Field.Store.YES, Field.Index.ANALYZED);
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

	public IndexSimulatorHelper(int thread_id, int offset, String indexing_path) throws Exception{
		doc = new Document();

		String LOG_FILE = indexing_path+"/log/Indexing"+offset+ thread_id + ".log";
		errorWriter = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(LOG_FILE), "UTF8"));

		Analyzer analyzer = new SimpleAnalyzer(Version.LUCENE_30);
		File file = new File(indexing_path+"/WikiIndexing"+offset+thread_id);
		Directory directory = new SimpleFSDirectory(file);
		IndexWriter iwriter = new IndexWriter(directory, analyzer, true,
				new IndexWriter.MaxFieldLength(Integer.MAX_VALUE));
		iwriter.setMergeFactor(10000);
		iwriter.setMaxMergeDocs(Integer.MAX_VALUE);
		iwriter.setRAMBufferSizeMB(100);

		index_writer = iwriter;
		sentRepos = SentimentRepos.getInstance(indexing_path);
		
		t_id = thread_id;
	}

	public void run(){
		isInProgress = true;

		try {
			// Set all fields to lucene and add docs

			// set feilds
			titleField.setValue("title"+t_id);
			doc.add(titleField);
			
			idField.setValue("page id "+t_id);
			doc.add(idField);

			String inlinks = "5";
			noOfInlinks.setValue(inlinks + t_id);
			doc.add(noOfInlinks);

			String outlinks = "3";
			noOfOutlinks.setValue(outlinks + t_id);
			doc.add(noOfOutlinks);

			NEType.setValue("NE" + t_id);
			doc.add(NEType);

			abstractVbAdjField.setValue("VB/ADJ" + t_id);
			doc.add(abstractVbAdjField);

			titleDismbField.setValue("Disamb Text" + t_id);
			doc.add(titleDismbField);

			// Add page type
			StringBuilder pageType = new StringBuilder();
			for (int j = 0; j < 10; j++) {
				pageType.append(j+" | ");
			}

			if(pageType.length() > 0) {
				pageTypesField.setValue(pageType.toString() + t_id);
				doc.add(pageTypesField);
			}

			// set sections
			/*List<WikiSection> wikiSections = pageBean.getSections();
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
			}*/

			//if(sectionBuilder.length()>5) {
			secHeadingField.setValue("sec tion head" + t_id);
			doc.add(secHeadingField);
			//}

			//if(imgBuilder.length()>4) {
			imgTextField.setValue("imgage text" + t_id);
			doc.add(imgTextField);
			//}

			//if(causeEffectBuilder.length()>0) {
			classCauseEffectField.setValue("cause effect" + t_id);
			doc.add(classCauseEffectField);
			//}

			//if(profitBenefitBuilder.length()>0) {
			classProfitBenefitField.setValue("benefit" + t_id);
			doc.add(classProfitBenefitField);
			//}

			// set sem classes
			//Map<String, SemanticClass> semClassMap = pageBean.getSemClassMap();
			//				System.out.println("Abstract field : " + semClassMap.get(
			//							IndexingConstants.SEM_CLASS_SYNOPSIS).getWords()
			//							.toString());
			//if (semClassMap.get(IndexingConstants.SEM_CLASS_SYNOPSIS)
			//		.getWords().length() > 2) {
			abstractField.setValue("abstract field" + t_id);
			doc.add(abstractField);
			//}


			//if (semClassMap.get(IndexingConstants.SEM_CLASS_SYNONYM)
			//	.getWords().length() > 2) {
			classSynonymField.setValue("sun field" + t_id);
			doc.add(classSynonymField);
			//}

			//if (semClassMap.get(IndexingConstants.SEM_CLASS_SYNONYM_WORDNET)
			//		.getWords().length() > 2) {
				classSynonymWordnetField.setValue("syn wordnet" + t_id);
				doc.add(classSynonymWordnetField);
			//}

			//if (semClassMap.get(IndexingConstants.SEM_CLASS_HOMONYM)
			//		.getWords().length() > 4) {
				classHomonymField.setValue("homo field" + t_id);
				doc.add(classHomonymField);
			//}
			//if (semClassMap.get(IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK)
			//		.getWords().length() > 2) {
				classAsctLinkField.setValue("asct link" + t_id);
				doc.add(classAsctLinkField);
			//}

			//if (semClassMap.get(IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK_PARENT)
			//		.getWords().length() > 2) {
				classAsctLinkParentField.setValue("asct parent" + t_id);
				doc.add(classAsctLinkParentField);
			//}

			//if (semClassMap.get(IndexingConstants.SEM_CLASS_REFERENCE)
			//		.getWords().length() > 2) {
				classReferenceField.setValue("ref field" + t_id);
				doc.add(classReferenceField);
			//}
			//if (semClassMap.get(IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED)
			//		.getWords().length() > 2) {
				classRelatedField.setValue("rel field" + t_id);
				doc.add(classRelatedField);
			//}
			//if (semClassMap.get(IndexingConstants.SEM_CLASS_COOCCURRENCE)
			//		.getWords().length() > 2) {
				classCooccurrenceField.setValue("cooccur" + t_id);
				doc.add(classCooccurrenceField);
			//}
			//if (semClassMap.get(IndexingConstants.SEM_CLASS_ASSOCIATION_HIERARCHY)
				//	.getWords().length() > 2) {
				classAsctHrchyField.setValue("asct hrchy" + t_id);
				doc.add(classAsctHrchyField);
			//}
			//if (semClassMap.get(IndexingConstants.SEM_CLASS_FREQUENT)
			//		.getWords().length() > 2) {
				classFrequentField.setValue("freq field" + t_id);
				doc.add(classFrequentField);
			//}
			//if (semClassMap.get(IndexingConstants.SEM_CLASS_HYPERNYM)
			//		.getWords().length() > 2) {
				classHyperField.setValue("hyper field" + t_id);
				doc.add(classHyperField);
			//}
			//if (semClassMap.get(IndexingConstants.SEM_CLASS_HYPONYM)
			//		.getWords().length() > 2) {
				classHypoField.setValue("hypo field" + t_id);
				doc.add(classHypoField);
			//}
			//if (semClassMap.get(IndexingConstants.SEM_CLASS_ROLE)
			//		.getWords().length() > 2) {
				classRoleField.setValue("role field" + t_id);
				doc.add(classRoleField);
			//}
			//if (semClassMap.get(IndexingConstants.SEM_CLASS_MERONYM)
			//		.getWords().length() > 2) {
				classMeronymField.setValue("mero field" + t_id);
				doc.add(classMeronymField);
			//}

			//if (semClassMap.get(IndexingConstants.SEM_CLASS_MAKEPRODUCE)
			//		.getWords().length() > 2) {
				classMakeProduceField.setValue("prod field" + t_id);
				doc.add(classMakeProduceField);
			//}


			//if (semClassMap.get(IndexingConstants.SEM_CLASS_SIBLING)
			//		.getWords().length() > 2) {
				classSiblingField.setValue("sibling" + t_id);
				doc.add(classSiblingField);
			//}


			index_writer.addDocument(doc);
			doc = new Document();
			// clean for the next run
			//pageBean.clearAll();
			isInProgress = false;
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			index_writer.close();
			System.out.println("File indexer closed....");
		} catch (Exception e) {
			try {
				errorWriter.write("Closing of FS Writer failed : " + e.getMessage()
						+ " " + e.getCause().toString() + "\n");
				errorWriter.write(e.toString());
				e.printStackTrace();
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
	public boolean getStatus() {
		return isInProgress;
	}
}
