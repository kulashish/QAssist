package com.aneedo.indexing.plainText;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.SetBasedFieldSelector;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.util.Version;


public class CompleteTextSearcher {
	private static final String WIKI_PATH = "/mnt/bag3/kedhar/aneedo/indexing/wikipage/WikiIndexing2110";
	private SetBasedFieldSelector wikiSetBasedField = null;

	private static CompleteTextSearcher instnace = null;
	private IndexSearcher wikiIndSearcher = null;
	private final Map<String, Float> wikiBoosts = new HashMap<String, Float>();
	private final BooleanClause.Occur[] wikiflags = new BooleanClause.Occur[] {
			BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD };

	/*
	 * What fields to be searched
	 */
	private final String[] wikiFields = {
			TextIndexingConstants.PAGE_TITLE, TextIndexingConstants.PAGE_TEXT
	};

	private CompleteTextSearcher() {
		File wikifile = new File(WIKI_PATH);
		try {
			wikiBoosts.put(TextIndexingConstants.PAGE_TITLE, Float.valueOf(1));
			wikiBoosts.put(TextIndexingConstants.PAGE_TEXT, Float.valueOf(1));

			// project what fields to selected for wiki
			Set<String> wikifieldsToSelect = new HashSet<String>();
			wikifieldsToSelect.add(TextIndexingConstants.PAGE_TITLE);
			wikifieldsToSelect.add(TextIndexingConstants.PAGE_TEXT);
			MMapDirectory mapDir = new MMapDirectory(wikifile);
			wikiIndSearcher = new IndexSearcher(mapDir, true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static CompleteTextSearcher getInstance() {
		if (instnace == null) {
			instnace = new CompleteTextSearcher();
		}
		return instnace;
	}

	public void getResults(String query) {
		MultiFieldQueryParser mulfieldParser = new MultiFieldQueryParser(
				Version.LUCENE_30, wikiFields, new SimpleAnalyzer(
						Version.LUCENE_30), wikiBoosts);
		ScoreDoc[] hits = null;
		try {
			Query mulQuery = mulfieldParser.parse(Version.LUCENE_30, query,
					wikiFields, wikiflags, new StandardAnalyzer(Version.LUCENE_30));
			hits = wikiIndSearcher.search(mulQuery, null, 50).scoreDocs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		try{
			BufferedWriter bw=new BufferedWriter(new FileWriter("/home/ambha/output.txt"));
			for (int i = 0; i < hits.length; i++) {
				//		try {

				final Document hitDoc = wikiIndSearcher.doc(hits[i].doc, wikiSetBasedField);
				//			Field[] fields = hitDoc.getFields(TextIndexingConstants.PAGE_TITLE);
				//			if(fields != null) {
				//			
				//				for(int j=0;j<fields.length;j++) {
				//				System.out.println(fields[j].stringValue());
				//				}
				//			}
				Field[] fields = hitDoc.getFields(TextIndexingConstants.PAGE_TEXT);
				if(fields != null) {
					for(int j=0;j<fields.length;j++) {
						String input=fields[j].stringValue();
						input=input.replaceAll(",", ", ").replaceAll(",  ", ", ").replaceAll(" ,",",").replaceAll("  "," ");
						//ApplyRegex.getHypernyms(input);
						//ApplyRegex.getMeronyms(input);
						ApplyRegex.getSynonyms(input);
						//System.out.println(fields[j].stringValue());
						bw.write(fields[j].stringValue()+"\n\n");
					}

				}
			} 
			bw.flush();
			bw.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		



	}

	public static void main(String[] args) {
		CompleteTextSearcher processor = new CompleteTextSearcher();
		processor.getResults("\"called as\"");
	}
}