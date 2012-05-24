package com.aneedo.search;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.SetBasedFieldSelector;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.util.Version;

import com.aneedo.indexing.IndexingConstants;
import com.aneedo.search.bean.QueryDetailBean;
import com.aneedo.search.bean.WikiEntity;
import com.aneedo.search.util.QueryProcessingUtil;

public class SemanticIndexSearcher_old {

	private static final String WIKI_PATH = "/mnt/bag1/querysystem/indexing/semantic/WikiIndexing";
	private SetBasedFieldSelector resultSetBasedField = null;
	//private SetBasedFieldSelector entitySetBasedField = null;

	private static SemanticIndexSearcher_old instnace = null;
	private IndexSearcher wikiIndSearcher = null;
	private final String[] phraseFields= new String[] {
			IndexingConstants.PAGE_TITLE + IndexingConstants.PAGE_TITLE_DISAMB,
			IndexingConstants.SECTION_HEADING,
			IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED,
			IndexingConstants.SEM_CLASS_SYNOPSIS,
			IndexingConstants.SEM_CLASS_HYPONYM,
			IndexingConstants.SEM_CLASS_ASSOCIATION_HIERARCHY,
			IndexingConstants.SEM_CLASS_FREQUENT,
			IndexingConstants.SEM_CLASS_REFERENCE};
	
	private final Map<String, Float> semanticBoosts = new HashMap<String, Float>();
	
	private final BooleanClause.Occur[] semanticFlags = new BooleanClause.Occur[] {
			BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD,
			BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD,
			BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD,
			BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD,
			BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD,
			BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD,
			BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD,
			BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD,
			BooleanClause.Occur.SHOULD };

	private final Map<String, Float> entityBoosts = new HashMap<String, Float>();
	private final BooleanClause.Occur[] entityFlags = new BooleanClause.Occur[] {
			BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD,
			BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD,
			BooleanClause.Occur.SHOULD};

	/*
	 * What fields to be searched
	 */
	private final String[] semanticViewFields = {
			// Captured in entity view
	        IndexingConstants.PAGE_TITLE, IndexingConstants.PAGE_TITLE_DISAMB,
			IndexingConstants.SECTION_HEADING,
			IndexingConstants.SEM_CLASS_SYNOPSIS,
			IndexingConstants.SEM_CLASS_HYPERNYM,
			IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED,
			IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK,
			IndexingConstants.SEM_CLASS_ASSOCIATION_HIERARCHY,
			IndexingConstants.SEM_CLASS_FREQUENT,
			IndexingConstants.SEM_CLASS_HOMONYM,
			IndexingConstants.SEM_CLASS_SYNONYM,
			IndexingConstants.SEM_CLASS_SYNONYM_WORDNET,
			IndexingConstants.SEM_CLASS_MAKEPRODUCE,
			IndexingConstants.SEM_CLASS_MERONYM,
			IndexingConstants.SEM_CLASS_REFERENCE,
			IndexingConstants.SEM_CLASS_ROLE,
			IndexingConstants.SEM_CLASS_SIBLING };
	
	private final String[] entityViewFields = {
			IndexingConstants.SEM_CLASS_SYNONYM,
			IndexingConstants.SEM_CLASS_SYNONYM_WORDNET,
			IndexingConstants.PAGE_TITLE, IndexingConstants.PAGE_TITLE_DISAMB,
			IndexingConstants.SEM_CLASS_HOMONYM
	};

	private SemanticIndexSearcher_old() {
		File wikifile = new File(WIKI_PATH);
		try {
			semanticBoosts.put(IndexingConstants.PAGE_TITLE, Float.valueOf(1));
			semanticBoosts.put(IndexingConstants.PAGE_TITLE_DISAMB, Float
					.valueOf(1));
			semanticBoosts.put(IndexingConstants.SEM_CLASS_SYNOPSIS, Float
					.valueOf(1));
			semanticBoosts.put(IndexingConstants.SECTION_HEADING, Float.valueOf(1));
			semanticBoosts.put(IndexingConstants.SEM_CLASS_HYPERNYM, Float
					.valueOf(1));
			semanticBoosts.put(IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED,
					Float.valueOf(1));
			semanticBoosts.put(IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK,
					Float.valueOf(1));
			semanticBoosts.put(
					IndexingConstants.SEM_CLASS_ASSOCIATION_HIERARCHY,
					Float.valueOf(1));
			semanticBoosts.put(IndexingConstants.SEM_CLASS_FREQUENT, Float
					.valueOf(1));
			semanticBoosts.put(IndexingConstants.SEM_CLASS_HOMONYM, Float
					.valueOf(1));
			semanticBoosts.put(IndexingConstants.SEM_CLASS_SYNONYM, Float
					.valueOf(1));
			semanticBoosts.put(IndexingConstants.SEM_CLASS_SYNONYM_WORDNET, Float
					.valueOf(1));
			semanticBoosts.put(IndexingConstants.SEM_CLASS_MAKEPRODUCE, Float
					.valueOf(1));
			semanticBoosts.put(IndexingConstants.SEM_CLASS_MERONYM, Float
					.valueOf(1));
			semanticBoosts.put(IndexingConstants.SEM_CLASS_REFERENCE, Float
					.valueOf(1));
			semanticBoosts.put(IndexingConstants.SEM_CLASS_ROLE, Float.valueOf(1));
			semanticBoosts.put(IndexingConstants.SEM_CLASS_SIBLING, Float
					.valueOf(1));

			entityBoosts.put(IndexingConstants.PAGE_TITLE, Float.valueOf(1));
			entityBoosts.put(IndexingConstants.PAGE_TITLE_DISAMB, Float
					.valueOf(1));
			entityBoosts.put(IndexingConstants.SEM_CLASS_SYNONYM, Float
					.valueOf(1));
			entityBoosts.put(IndexingConstants.SEM_CLASS_SYNONYM_WORDNET, Float
					.valueOf(1));
			entityBoosts.put(IndexingConstants.SEM_CLASS_HOMONYM, Float
					.valueOf(1));


			// project what fields to selected for wiki
			Set<String> resultFieldsToSelect = new HashSet<String>();
			resultFieldsToSelect.add(IndexingConstants.PAGE_TITLE);
			resultFieldsToSelect.add(IndexingConstants.PAGE_ID);
			resultFieldsToSelect.add(IndexingConstants.PAGE_TITLE_DISAMB);
			resultFieldsToSelect.add(IndexingConstants.SECTION_HEADING);
			resultFieldsToSelect.add(IndexingConstants.SEM_CLASS_HYPERNYM);
			resultFieldsToSelect
					.add(IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED);
			resultFieldsToSelect
					.add(IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK);
			resultFieldsToSelect
					.add(IndexingConstants.SEM_CLASS_ASSOCIATION_HIERARCHY);
			resultFieldsToSelect.add(IndexingConstants.SEM_CLASS_FREQUENT);
			resultFieldsToSelect.add(IndexingConstants.SEM_CLASS_PRODUCT);
			resultFieldsToSelect.add(IndexingConstants.SEM_CLASS_HOMONYM);
			resultFieldsToSelect.add(IndexingConstants.SEM_CLASS_HYPONYM);
			resultFieldsToSelect.add(IndexingConstants.SEM_CLASS_MAKEPRODUCE);
			resultFieldsToSelect.add(IndexingConstants.SEM_CLASS_MERONYM);
			resultFieldsToSelect.add(IndexingConstants.SEM_CLASS_REFERENCE);
			resultFieldsToSelect.add(IndexingConstants.SEM_CLASS_ROLE);
			resultFieldsToSelect.add(IndexingConstants.SEM_CLASS_SIBLING);
			resultSetBasedField = new SetBasedFieldSelector(resultFieldsToSelect,
					new HashSet<String>());

//			Set<String> entityfieldsToSelect = new HashSet<String>();
//			entityfieldsToSelect.add(IndexingConstants.PAGE_TITLE);
//			entityfieldsToSelect.add(IndexingConstants.PAGE_TITLE_DISAMB);
//			entityfieldsToSelect.add(IndexingConstants.SEM_CLASS_SYNOPSIS);
			//entitySetBasedField = new SetBasedFieldSelector(entityfieldsToSelect, new HashSet<String>());

			MMapDirectory mapDir = new MMapDirectory(wikifile);
			wikiIndSearcher = new IndexSearcher(mapDir, true);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static SemanticIndexSearcher_old getInstance() {
		if (instnace == null) {
			instnace = new SemanticIndexSearcher_old();
		}
		return instnace;
	}
	
	public void preprocessQuery(QueryDetailBean queryDetailBean) {
		QueryProcessingUtil util = QueryProcessingUtil.getInstance();
		util.setPosStemPhrase(queryDetailBean);
		util.setPhrasesStem(queryDetailBean);
	}

	public Set getSemanticClassResults(QueryDetailBean queryDetailBean) {
		MultiFieldQueryParser mulfieldParser = new MultiFieldQueryParser(
				Version.LUCENE_30, semanticViewFields, new SimpleAnalyzer(
						Version.LUCENE_30), semanticBoosts);
		Set<String> results = new HashSet<String>();
		ScoreDoc[] hits = null;
		
		String rawQuery = queryDetailBean.getRawQuery();
		
		Query[] queries = null;
		
		String phraseStr = queryDetailBean.getStrPhrase();

		// Add phrase query
		if(phraseStr.length() > 2) {
			queries = new Query[1+phraseFields.length];
			try {
				for(int i=0;i<phraseFields.length;i++) {
					final QueryParser queryParser = new QueryParser(Version.LUCENE_30,
							phraseFields[i], new SimpleAnalyzer(
									Version.LUCENE_30));
					queries[1+i] = queryParser.parse(phraseStr);
					queries[1+i].setBoost(2.0f);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			queries = new Query[1];
		}
		
		try {
			
			queries[0] = mulfieldParser.parse(Version.LUCENE_30, rawQuery+phraseStr,
					semanticViewFields, semanticFlags, new StandardAnalyzer(
							Version.LUCENE_30));
			queries[0].setBoost(1.0f);

			// More than 1 query
			if(queries.length > 1)
				queries[0].combine(queries);
			
			hits = wikiIndSearcher.search(queries[0], null, 50).scoreDocs;
		} catch (Exception e) {
			e.printStackTrace();
		}

		Field field = null;

		for (int i = 0; i < hits.length; i++) {
			final WikiEntity wikiEntity = new WikiEntity();
			try {
				final Document hitDoc = wikiIndSearcher.doc(hits[i].doc,
						resultSetBasedField);
				field = hitDoc.getField(IndexingConstants.PAGE_TITLE);
				if (field != null) {
					final Field disambField = hitDoc
							.getField(IndexingConstants.PAGE_TITLE_DISAMB);
					if (disambField != null) {
//						System.out.println(field.stringValue() + " "
//								+ disambField.stringValue());
						wikiEntity.setTitle(field.stringValue() + " "
								+ disambField.stringValue());
					} else {
//						System.out.println(field.stringValue());
						wikiEntity.setDisamb(field.stringValue());
						wikiEntity.setDisam(true);
					}
				}


				field = hitDoc.getField(IndexingConstants.SECTION_HEADING);
				if (field != null) {
					if (field.stringValue() != null) {
						String[] splits = field.stringValue().split(" \\| ");
						for (int j = 0; j < splits.length; j++) {
							if(!(splits[j].equals("abstract") || splits[j].equals("history") || splits[j].indexOf("graphy") >=0 ))
								wikiEntity.getSections().add(splits[j]);
						}
					}
				}
				
				field = hitDoc.getField(IndexingConstants.SEM_CLASS_HYPERNYM);
				if (field != null) {
					if (field.stringValue() != null) {
						//System.out.println("Hypernym ....");
						String[] splits = field.stringValue().split(" \\| ");
						for (int j = 0; j < splits.length; j++) {
							if(splits[j].indexOf(rawQuery) >= 0 || rawQuery.indexOf(splits[j]) >= 0) {
//							System.out.println(splits[j]);
							results.add(splits[j].trim());
							}
						}
					}
				}

				field = hitDoc.getField(IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED);
				if (field != null) {
					if (field.stringValue() != null) {
						System.out.println("Related ....");
						String[] splits = field.stringValue().split(" \\| ");
						for (int j = 0; j < splits.length; j++) {
							if(splits[j].indexOf(rawQuery) >= 0 || rawQuery.indexOf(splits[j]) >= 0) {
							System.out.println(splits[j]);
							results.add(splits[j].trim());
							}
						}
					}
				}

				field = hitDoc.getField(IndexingConstants.SEM_CLASS_ASSOCIATION_HIERARCHY);
				if (field != null) {
					if (field.stringValue() != null) {
						System.out.println("Related hierarcy ....");
						String[] splits = field.stringValue().split(" \\| ");
						for (int j = 0; j < splits.length; j++) {
							if(splits[j].indexOf(rawQuery) >= 0 || rawQuery.indexOf(splits[j]) >= 0) {
							System.out.println(splits[j]);
							}
							//results.add(splits[j]);
						}
					}
				}

//				field = hitDoc.getField(IndexingConstants.SEM_CLASS_FREQUENT);
//				if (field != null) {
//					if (field.stringValue() != null) {
//						String[] splits = field.stringValue().split(" \\| ");
//						for (int j = 0; j < splits.length; j++) {
//							System.out.println(splits[j]);
//						}
//					}
//				}

				field = hitDoc.getField(IndexingConstants.SEM_CLASS_HOMONYM);
				if (field != null) {
					if (field.stringValue() != null) {
						System.out.println("Homonym ....");
						String[] splits = field.stringValue().split(" \\| ");
						for (int j = 0; j < splits.length; j++) {
							if(splits[j].indexOf(rawQuery) >= 0 || rawQuery.indexOf(splits[j]) >= 0) {
							System.out.println(splits[j]);
							results.add(splits[j].trim());
							}
						}
					}
				}

				field = hitDoc.getField(IndexingConstants.SEM_CLASS_SIBLING);
				if (field != null) {
					if (field.stringValue() != null) {
						System.out.println("Sibling ....");
						String[] splits = field.stringValue().split(" \\| ");
						for (int j = 0; j < splits.length; j++) {
							if(splits[j].indexOf(rawQuery) >= 0 || rawQuery.indexOf(splits[j]) >= 0) {
							System.out.println(splits[j]);
							results.add(splits[j].trim());
							}
						}
					}
				}

				field = hitDoc.getField(IndexingConstants.SEM_CLASS_REFERENCE);
				if (field != null) {
					if (field.stringValue() != null) {
						String[] splits = field.stringValue().split(" \\| ");
						for (int j = 0; j < splits.length; j++) {
							//System.out.println(splits[j]);
						}
					}
				}

				field = hitDoc.getField(IndexingConstants.SEM_CLASS_MERONYM);
				if (field != null) {
					if (field.stringValue() != null) {
						String[] splits = field.stringValue().split(" \\| ");
						for (int j = 0; j < splits.length; j++) {
							//System.out.println(splits[j]);
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return results;
	}

	public static void main(String[] args) {
		SemanticIndexSearcher_old processor = SemanticIndexSearcher_old.getInstance();
		QueryDetailBean queryDetailBean = new QueryDetailBean();
		queryDetailBean.setRawQuery("laptop");
		processor.preprocessQuery(queryDetailBean);
		Set results = processor.getSemanticClassResults(queryDetailBean);
		
//		processor.setPosTags(queryDetailBean);
//		processor.setPhrasesStem(queryDetailBean);
//		Set<String> phrases = queryDetailBean.getPhraseList();
		Iterator<String> itr = results.iterator();
		while(itr.hasNext()) {
			System.out.println("dsds"+itr.next());
		}
	}
	

}

class TitleFuzzySearchThread2 extends Thread {
	private QueryDetailBean queryDetailBean = null;
	private IndexSearcher indexSearcher=null;
	
	TitleFuzzySearchThread2(QueryDetailBean queryDetailBean, IndexSearcher searcher) {
		this.queryDetailBean = queryDetailBean;
		this.indexSearcher = searcher;
	}
	
	@Override
	public void run() {
		super.run();
		ScoreDoc[] hits = null;
	}
}

// Better way to extract all matching entities
/*private void identifyEntity(QueryDetailBean queryDetailBean) {
	MultiFieldQueryParser mulfieldParser = new MultiFieldQueryParser(
			Version.LUCENE_30, entityViewFields, new SimpleAnalyzer(
					Version.LUCENE_30), entityBoosts);
	ScoreDoc[] hits = null;
	
	
	
	Query[] queries = null;
	
	String phraseStr = queryDetailBean.getStrPhrase();

	// Add phrase query
	if(phraseStr.length() > 2) {
		queries = new Query[1+entityViewFields.length];
		try {
			for(int i=0;i<entityViewFields.length;i++) {
				final QueryParser queryParser = new QueryParser(Version.LUCENE_30,
						entityViewFields[i], new SimpleAnalyzer(
								Version.LUCENE_30));
				queries[1+i] = queryParser.parse(phraseStr);
				queries[1+i].setBoost(2.0f);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	} else {
		queries = new Query[1];
	}
	
	try {
		queries[0] = mulfieldParser.parse(Version.LUCENE_30, queryDetailBean.getRawQuery() +phraseStr,
				entityViewFields, entityFlags, new StandardAnalyzer(
						Version.LUCENE_30));
		queries[0].setBoost(1.0f);

		// More than 1 query
		if(queries.length > 1)
			queries[0].combine(queries);
		
		hits = wikiIndSearcher.search(queries[0], null, 50).scoreDocs;
	} catch (Exception e) {
		e.printStackTrace();
	}

	Field field = null;
	for (int i = 0; i < hits.length; i++) {
		try {
			final Document hitDoc = wikiIndSearcher.doc(hits[i].doc,
					resultSetBasedField);
			field = hitDoc.getField(IndexingConstants.PAGE_TITLE);
			if (field != null) {
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
*/