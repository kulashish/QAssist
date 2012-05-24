package com.aneedo.search;

import gnu.trove.TIntObjectHashMap;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.SetBasedFieldSelector;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.util.Version;

import com.aneedo.indexing.IndexingConstants;
import com.aneedo.search.bean.CompatibleEntitySet;
import com.aneedo.search.bean.Interpretation;
import com.aneedo.search.bean.QueryDetailBean;
import com.aneedo.search.bean.SemEntityBean;
import com.aneedo.search.bean.SemInterpretation;
import com.aneedo.search.helper.EntitySetProcessor;
import com.aneedo.search.helper.SemanticSearchHelper;
import com.aneedo.search.util.CompatibleSetFinder;
import com.aneedo.search.util.QueryProcessingUtil;
import com.aneedo.search.util.SemClassConstants;
import com.aneedo.search.util.SemClassSerachConstants;
import com.aneedo.search.util.SemanticSearchUtil;

public class SemanticIndexSearcher {

	private SetBasedFieldSelector resultSetBasedField = null;
	private static final ExecutorService execSvc = Executors
			.newCachedThreadPool();
	CompatibleSetFinder finder = new CompatibleSetFinder();
	// private SetBasedFieldSelector entitySetBasedField = null;

	private static SemanticIndexSearcher instnace = null;
	private IndexSearcher wikiIndSearcher = null;

	private SemanticIndexSearcher() {
		File wikifile = new File(SemClassConstants.WIKI_PATH);
		try {

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
					.add(IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED_HIERARCHY);
			resultFieldsToSelect.add(IndexingConstants.SEM_CLASS_FREQUENT);
			resultFieldsToSelect.add(IndexingConstants.SEM_CLASS_PRODUCT);
			resultFieldsToSelect.add(IndexingConstants.SEM_CLASS_HOMONYM);
			resultFieldsToSelect.add(IndexingConstants.SEM_CLASS_HYPONYM);
			resultFieldsToSelect.add(IndexingConstants.SEM_CLASS_MAKEPRODUCE);
			resultFieldsToSelect.add(IndexingConstants.SEM_CLASS_MERONYM);
			resultFieldsToSelect.add(IndexingConstants.SEM_CLASS_REFERENCE);
			resultFieldsToSelect.add(IndexingConstants.SEM_CLASS_ROLE);
			resultFieldsToSelect.add(IndexingConstants.SEM_CLASS_SIBLING);
			resultSetBasedField = new SetBasedFieldSelector(
					resultFieldsToSelect, new HashSet<String>());

			// Set<String> entityfieldsToSelect = new HashSet<String>();
			// entityfieldsToSelect.add(IndexingConstants.PAGE_TITLE);
			// entityfieldsToSelect.add(IndexingConstants.PAGE_TITLE_DISAMB);
			// entityfieldsToSelect.add(IndexingConstants.SEM_CLASS_SYNOPSIS);
			// entitySetBasedField = new
			// SetBasedFieldSelector(entityfieldsToSelect, new
			// HashSet<String>());

			MMapDirectory mapDir = new MMapDirectory(wikifile);
			wikiIndSearcher = new IndexSearcher(mapDir, true);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static SemanticIndexSearcher getInstance() {
		if (instnace == null) {
			instnace = new SemanticIndexSearcher();
		}
		return instnace;
	}

	public void preprocessQuery(QueryDetailBean queryDetailBean) {
		QueryProcessingUtil util = QueryProcessingUtil.getInstance();
		util.setPosStemPhrase(queryDetailBean);
		// util.setPhrasesStem(queryDetailBean);
	}

	public void storeEntitiesMatchingQuery(String fieldName, String entity,
			SemClassStore store) throws Exception {

		String qry = String.format(fieldName + ":%s", entity);
		System.out.println("QUERY : " + qry);

		QueryParser parser = new QueryParser(Version.LUCENE_30, "page_title",
				new StandardAnalyzer(Version.LUCENE_30));
		Query query = parser.parse(qry);

		// Search for the query
		TopDocs hits = wikiIndSearcher.search(query, 999);
		System.out.println("Got results : " + hits.totalHits);
//		int count = 0;
		for (ScoreDoc hitDoc : hits.scoreDocs) {
//			if (++count % 500 == 0)
//				System.out.println("[storeEntitiesMathchingQuery] Processed "
//						+ count + "/" + hits.scoreDocs.length);

			Document doc = wikiIndSearcher.doc(hitDoc.doc, resultSetBasedField);
			QueryDetailBean queryDetailBean = new QueryDetailBean();
			String q = doc.get("page_title").toLowerCase();
			queryDetailBean.setRawQuery(q);
			queryDetailBean.setQuerySplits(q.split(" "));
			SemanticSearchHelper.getInstance().processDocument(doc, store,
					SemClassSerachConstants.RESULT_ENTITY, queryDetailBean);
		}
	}

	public void storeEntitiesMathchingQuery(List<String> entityNames,
			SemClassStore store) throws Exception {
		/*
		 * QueryDetailBean queryDetailBean = new QueryDetailBean();
		 * queryDetailBean.setRawQuery(qry.toLowerCase());
		 * queryDetailBean.setStrPhrase(queryDetailBean.getRawQuery());
		 * 
		 * EntitySearchThread entityThread = new
		 * EntitySearchThread(wikiIndSearcher, resultSetBasedField,
		 * queryDetailBean, store, new HashMap<String, String>());
		 * entityThread.run();
		 */

		if (entityNames.size() == 0)
			return;

		String qry = String.format("page_title:\"%s*\"", entityNames.get(0)
				.toLowerCase());
		for (int i = 1; i < entityNames.size(); i++) {
			qry += String.format(" OR page_title:\"%s*\"", entityNames.get(i)
					.toLowerCase());
		}

		QueryParser parser = new QueryParser(Version.LUCENE_30, "page_title",
				new StandardAnalyzer(Version.LUCENE_30));
		Query query = parser.parse(qry);
		/*
		 * BooleanQuery query = new BooleanQuery(); PhraseQuery pq = new
		 * PhraseQuery(); for(String entity : entityNames) pq.add(new
		 * Term("page_title", entity));
		 */

		// Search for the query
		TopDocs hits = wikiIndSearcher.search(query, 999);
		int count = 0;
		for (ScoreDoc hitDoc : hits.scoreDocs) {
			if (++count % 500 == 0)
				System.out.println("[storeEntitiesMathchingQuery] Processed "
						+ count + "/" + hits.scoreDocs.length);

			Document doc = wikiIndSearcher.doc(hitDoc.doc, resultSetBasedField);
			QueryDetailBean queryDetailBean = new QueryDetailBean();
			String q = doc.get("page_title").toLowerCase();
			for (String entity : entityNames)
				if (!entity.equalsIgnoreCase(q))
					continue;
			queryDetailBean.setRawQuery(q);
			queryDetailBean.setQuerySplits(q.split(" "));
			SemanticSearchHelper.getInstance().processDocument(doc, store,
					SemClassSerachConstants.RESULT_ENTITY, queryDetailBean);
		}
	}

	public static void main(String[] args) {
		SemanticIndexSearcher processor = SemanticIndexSearcher.getInstance();
		SemClassStore results = processor.getResults("amitabh bachchan", null);

		// phrase
		QueryDetailBean bean = results.getQueryDetailBean();
		Set<String> phraseSet = bean.getPhraseList();
		Iterator<String> pitr = phraseSet.iterator();

		// while(pitr.hasNext()) {
		// System.out.println("Phrase : " + pitr.next());
		// }

		// System.out.println("Phrase " + bean.getStrPhrase());

		// System.out.println("Stem " + bean.getStrStem());

		bean.getStemList();

		List<Integer> pageIdList = results.getPageIds();

		Iterator<Integer> itr = pageIdList.iterator();
		// while(itr.hasNext()) {
		// System.out.println("Entity Id : "+itr.next());
		// }

		TIntObjectHashMap<SemEntityBean> semEntityMap = results
				.getSemEntityBeanMap();
		// System.out.println("Sem Entity Map : " +semEntityMap.size());

		List<CompatibleEntitySet> compatEntitySet = results.getCompatibleList();
		// System.out.println("Compat size : " + compatEntitySet.size());

		for (int i = 0, size = compatEntitySet.size(); i < size; i++) {
			final CompatibleEntitySet compatEntities = compatEntitySet.get(i);
			// final Set<Integer> compatEntityIds =
			// compatEntities.getPageIdList();
			// final Iterator<Integer> comIterator = compatEntityIds.iterator();
			// while(comIterator.hasNext()) {
			// final SemEntityBean semEntityBean =
			// results.getSemEntity(comIterator.next());
			// //System.out.print(semEntityBean.getTitle() +", " );
			// }
			// System.out.println();
			// System.out.println("Entity Set Score : " +
			// compatEntities.getEntitySetScore());
		}

		List<SemInterpretation> semInterList = results
				.getSemInterpretationList();

		for (int i = 0, size = semInterList.size(); i < size; i++) {
			final SemInterpretation semInter = semInterList.get(i);
			System.out.println("Interpretation : "
					+ semInter.getInterpretation());
			// System.out.println("Activity Flow : " +
			// semInter.getActivationPath());
			// System.out.println("Agg Score : " + semInter.getAggScore());
		}

		// processor.setPosTags(queryDetailBean);
		// processor.setPhrasesStem(queryDetailBean);
		// Set<String> phrases = queryDetailBean.getPhraseList();
		// Iterator<String> itr = results.iterator();
		// while(itr.hasNext()) {
		// System.out.println("dsds"+itr.next());
		// }
	}

	public SemClassStore getResults(QueryDetailBean queryDetailBean,
			Map<String, String> keyValueMap) {
		preprocessQuery(queryDetailBean);
		SemClassStore semClassStore = new SemClassStore();

		if (keyValueMap == null) {
			keyValueMap = new HashMap<String, String>();
		}

		boolean[] matchQuery = new boolean[queryDetailBean.getQuerySplits().length];
		Arrays.fill(matchQuery, false);
		semClassStore.setQueryMatch(matchQuery);

		semClassStore.setQueryDetailBean(queryDetailBean);
		SemClassThread semThread = new SemClassThread(wikiIndSearcher,
				resultSetBasedField, queryDetailBean, semClassStore,
				keyValueMap);
		semThread.start();
		EntityFuzzySearchThread fuzzyThread = new EntityFuzzySearchThread(
				wikiIndSearcher, resultSetBasedField, queryDetailBean,
				semClassStore, keyValueMap);
		fuzzyThread.start();
		EntitySearchThread entityThread = new EntitySearchThread(
				wikiIndSearcher, resultSetBasedField, queryDetailBean,
				semClassStore, keyValueMap);
		entityThread.start();

		// wait all to complete
		// int count =0;
		while (semThread.isInProgress() || entityThread.isInProgress()
				|| fuzzyThread.isInProgress()) {
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// count++;
		}
		// System.out.println("Retried entities");

		// TIntObjectHashMap<List<String>> wordSemTestMap =
		// semEntity.getWordSemClassMap();
		// int[] keys = wordSemTestMap.keys();
		// for(int k=0;k<keys.length;k++) {
		// List<String> memberList = wordSemMap.get(k);
		// System.out.println("After filling : "+SemanticSearchUtil.getInstance().getSemClassName(k));
		// if(memberList != null) {
		// for(int m=0;m<memberList.size();m++) {
		// System.out.print(memberList.get(m)+",");
		// }
		//
		// }
		// }

		// System.out.println();
		// Get all compatible sets
		finder.findCompatibleSets(semClassStore);

		// Process entity sets and entities
		EntitySetProcessor.getInstance().processEntitySet(semClassStore);

		// Filter and rank interpretation
		/*
		 * List<SemInterpretation>
		 * semInterpretations=semClassStore.getSemInterpretationList();
		 * Iterator<SemInterpretation> it=semInterpretations.iterator();
		 * ArrayList<SemInterpretation> interpretationList=new
		 * ArrayList<SemInterpretation>(); while(it.hasNext()) {
		 * SemInterpretation currIntrpr=it.next();
		 * interpretationList.add(currIntrpr); System.out.println(); } MMR
		 * mmrObject=new MMR(); ArrayList<SemInterpretation>
		 * results=mmrObject.getDiverseSet(interpretationList);
		 * semClassStore.setSemInterpretationList(results);
		 */
		return semClassStore;
	}

	public SemClassStore getResults(String rawQuery,
			Map<String, String> keyValueMap) {
		QueryDetailBean queryDetailBean = new QueryDetailBean();
		queryDetailBean.setRawQuery(rawQuery.toLowerCase());
		return getResults(queryDetailBean, keyValueMap);
	}

	/**
	 * Exposed for production
	 * 
	 * @param rawQuery
	 * @return
	 */
	public List<Interpretation> getResults(String rawQuery) {
		QueryDetailBean queryDetailBean = new QueryDetailBean();
		queryDetailBean.setRawQuery(rawQuery.toLowerCase());
		List<SemInterpretation> semInterList = getResults(queryDetailBean, null)
				.getSemInterpretationList();
		List<Interpretation> interList = new ArrayList<Interpretation>();

		for (int i = 0, size = semInterList.size(); i < size; i++) {
			final Interpretation inter = new Interpretation();
			inter.setInterpretId(i + 1);
			inter.setInterpretation(semInterList.get(i).getInterpretation());
			interList.add(inter);
		}
		return interList;
	}
}

class SemClassThread extends Thread implements SemClassSerachConstants {

	private QueryDetailBean queryDetailBean = null;

	private IndexSearcher indexSearcher;
	private SetBasedFieldSelector resultSetBasedField;
	private boolean inProgress = true;
	MultiFieldQueryParser mulfieldParser = null;
	QueryParser[] queryParser = new QueryParser[phraseFields.length];
	SemClassStore repos = null;
	Map<String, String> paramMap = null;

	// TODO To remove
	String[] paramFields = null;
	BooleanClause.Occur[] paramFlags = null;

	// ends here

	public SemClassThread(IndexSearcher searcher,
			SetBasedFieldSelector setBasedFieldSelector,
			QueryDetailBean queryDetailBean, SemClassStore repos,
			Map<String, String> paramMap) {
		this.resultSetBasedField = setBasedFieldSelector;
		this.indexSearcher = searcher;
		this.queryDetailBean = queryDetailBean;
		this.repos = repos;
		this.paramMap = paramMap;

		// TODO to remove
		Map<String, Float> paramSemBoosts = new HashMap<String, Float>();
		List<String> paramSearchFields = new ArrayList<String>();
		for (int i = 0; i < semanticViewFields.length; i++) {
			if (paramMap.get(semanticViewFields[i]) != null) {
				paramSearchFields.add(semanticViewFields[i]);
			}
		}

		int selFieldSize = paramSearchFields.size();

		if (selFieldSize == 0) {
			paramFields = semanticViewFields;
			paramFlags = semanticFlags;
		} else {
			paramFields = new String[selFieldSize];
			paramFlags = new BooleanClause.Occur[selFieldSize];
			paramFields = paramSearchFields.toArray(paramFields);
		}
		float paramBoost = 1;
		// Ends here

		for (int i = 0; i < paramFields.length; i++) {
			final String strParamBoost = paramMap
					.get(paramFields[i] + "-boost");
			paramBoost = 1;
			paramFlags[i] = BooleanClause.Occur.SHOULD;
			if (strParamBoost != null && !"".equals(strParamBoost.trim())) {
				try {
					paramBoost = Float.parseFloat(strParamBoost);
				} catch (Exception e) {
					paramBoost = 1;
				}

			}
			paramSemBoosts.put(paramFields[i], Float.valueOf(paramBoost));
		}

		mulfieldParser = new MultiFieldQueryParser(Version.LUCENE_30,
				paramFields, new SimpleAnalyzer(Version.LUCENE_30),
				paramSemBoosts);

		this.queryParser = new QueryParser[phraseFields.length];
		for (int i = 0; i < phraseFields.length; i++) {
			queryParser[i] = new QueryParser(Version.LUCENE_30,
					phraseFields[i], new SimpleAnalyzer(Version.LUCENE_30));
		}

	}

	public boolean isInProgress() {
		return inProgress;
	}

	@Override
	public void run() {
		inProgress = true;

		ScoreDoc[] hits = null;

		// String[] querySplits = rawQuery.split(" ");
		//
		// String[] querySoftMatchSplits = new String[querySplits.length];
		//
		// for(int i=0;i<querySplits.length;i++) {
		// querySoftMatchSplits[i] = querySplits[i].substring(0, (int) Math
		// .floor(querySplits[i].length() / 2));
		// }

		Query[] queries = null;

		// To remove later TODO
		String param = paramMap.get("stems");
		if (param != null && !"".equals(param.trim())) {
			queryDetailBean.setStrStem(param);
		}

		param = paramMap.get("phrases");
		if (param != null && !"".equals(param.trim())) {
			queryDetailBean.setStrPhrase(param);
		}
		// Ends here To remove

		String phraseStr = queryDetailBean.getStrPhrase();

		// Add phrase query
		if (phraseStr.length() > 2) {

			queries = new Query[1 + phraseFields.length];
			try {
				for (int i = 0; i < phraseFields.length; i++) {
					queries[1 + i] = queryParser[i].parse(phraseStr);
					queries[1 + i].setBoost(2.0f);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			queries = new Query[1];
		}

		// Consider only nouns and phrases in semantic view

		try {

			queries[0] = mulfieldParser.parse(Version.LUCENE_30,
					queryDetailBean.getNounWordStemStr() + " " + phraseStr,
					paramFields, paramFlags, new StandardAnalyzer(
							Version.LUCENE_30));
			queries[0].setBoost(1.0f);

			// More than 1 query
			if (queries.length > 1)
				queries[0].combine(queries);

			// TODO to remove
			int noOfRecords = 150;
			String strNoOfRecords = paramMap.get("SemClass");
			if (strNoOfRecords != null && !"".equals(strNoOfRecords.trim())) {
				try {
					noOfRecords = Integer.parseInt(strNoOfRecords);
				} catch (Exception e) {
					noOfRecords = 150;
				}
			}
			// Ends here

			hits = indexSearcher.search(queries[0], null, noOfRecords).scoreDocs;
			queryDetailBean.setSemClassQuery(queries[0].toString());
			queryDetailBean.setNumOfSemRecords(30);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < hits.length; i++) {
			try {
				final Document hitDoc = indexSearcher.doc(hits[i].doc,
						resultSetBasedField);
				SemanticSearchHelper.getInstance().processDocument(hitDoc,
						repos, RESULT_SEM_CLASS, queryDetailBean);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		inProgress = false;
	}

}

class EntityFuzzySearchThread extends Thread implements SemClassSerachConstants {
	private QueryDetailBean queryDetailBean = null;
	private IndexSearcher indexSearcher = null;
	private SetBasedFieldSelector resultSetBasedField = null;
	private SemClassStore repos = null;
	Set<String> results = new HashSet<String>();
	boolean inProgress = true;
	private MultiFieldQueryParser mulfieldParser;
	private QueryParser[] queryParser;
	Map<String, String> paramMap = null;

	// TODO to remove
	private String[] paramFields;
	private Occur[] paramFlags;

	EntityFuzzySearchThread(IndexSearcher searcher,
			SetBasedFieldSelector setBasedFieldSelector,
			QueryDetailBean queryDetailBean, SemClassStore repos,
			Map<String, String> paramMap) {
		this.queryDetailBean = queryDetailBean;
		this.indexSearcher = searcher;
		this.resultSetBasedField = setBasedFieldSelector;
		this.repos = repos;
		this.paramMap = paramMap;
		// for(int i=0; i<fuzzyViewFields.length;i++) {
		// fuzzyBoosts.put(fuzzyViewFields[i], Float
		// .valueOf(1));
		// }
		// mulfieldParser = new MultiFieldQueryParser(
		// Version.LUCENE_30, fuzzyViewFields, new SimpleAnalyzer(
		// Version.LUCENE_30), fuzzyBoosts);

		// TODO to remove
		Map<String, Float> paramSemBoosts = new HashMap<String, Float>();
		List<String> paramSearchFields = new ArrayList<String>();
		for (int i = 0; i < fuzzyViewFields.length; i++) {
			if (paramMap.get("fuzzy_" + fuzzyViewFields[i]) != null) {
				paramSearchFields.add(fuzzyViewFields[i]);
			}
		}

		int selFieldSize = paramSearchFields.size();

		if (selFieldSize == 0) {
			paramFields = fuzzyViewFields;
			paramFlags = fuzzyFlags;
		} else {
			paramFields = new String[selFieldSize];
			paramFlags = new BooleanClause.Occur[selFieldSize];
			paramFields = paramSearchFields.toArray(paramFields);
		}
		float paramBoost = 1;
		// Ends here

		for (int i = 0; i < paramFields.length; i++) {
			final String strParamBoost = paramMap.get("fuzzy_" + paramFields[i]
					+ "-boost");
			paramBoost = 1;
			paramFlags[i] = BooleanClause.Occur.SHOULD;
			if (strParamBoost != null && !"".equals(strParamBoost.trim())) {
				try {
					paramBoost = Float.parseFloat(strParamBoost);
				} catch (Exception e) {
					paramBoost = 1;
				}

			}
			paramSemBoosts.put(paramFields[i], Float.valueOf(paramBoost));
		}

		mulfieldParser = new MultiFieldQueryParser(Version.LUCENE_30,
				paramFields, new SimpleAnalyzer(Version.LUCENE_30),
				paramSemBoosts);

		this.queryParser = new QueryParser[paramFields.length];
		for (int i = 0; i < paramFields.length; i++) {
			queryParser[i] = new QueryParser(Version.LUCENE_30, paramFields[i],
					new SimpleAnalyzer(Version.LUCENE_30));
		}

	}

	@Override
	public void run() {
		inProgress = true;

		ScoreDoc[] hits = null;

		String rawQuery = queryDetailBean.getRawQuery();
		// String[] querySplits = queryDetailBean.getQuerySplits();

		// StringBuilder fuzzyQuery = new StringBuilder();
		//
		// for(int i=0;i<querySplits.length;i++) {
		// int prefixEnd = (querySplits[i].length()/2)+1;
		// fuzzyQuery.append(querySplits[i].substring(0, prefixEnd) + " ");
		// for(;prefixEnd<querySplits[i].length();prefixEnd++) {
		// fuzzyQuery.append(querySplits[i].substring(0, prefixEnd) + " ");
		// }
		// }

		// To remove later TODO
		String param = paramMap.get("stems");
		if (param != null && !"".equals(param.trim())) {
			queryDetailBean.setStrStem(param);
		}

		param = paramMap.get("phrases");
		if (param != null && !"".equals(param.trim())) {
			queryDetailBean.setStrPhrase(param);
		}
		// Ends here To remove

		Query[] queries = null;
		String phraseStr = queryDetailBean.getStrPhrase();

		// Add phrase query
		if (phraseStr.length() > 2) {
			queries = new Query[1 + paramFields.length];
			try {
				for (int i = 0; i < paramFields.length; i++) {
					queries[1 + i] = queryParser[i].parse(phraseStr);
					queries[1 + i].setBoost(2.0f);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			queries = new Query[1];
		}

		try {

			queries[0] = mulfieldParser.parse(Version.LUCENE_30, rawQuery + " "
					+ phraseStr + " " + queryDetailBean.getStrStem(),
					paramFields, paramFlags, new StandardAnalyzer(
							Version.LUCENE_30));
			queries[0].setBoost(1.0f);

			// More than 1 query
			if (queries.length > 1)
				queries[0].combine(queries);

			// TODO to remove
			int noOfRecords = 5;
			String strNoOfRecords = paramMap.get("Fuzzy");
			if (strNoOfRecords != null && !"".equals(strNoOfRecords.trim())) {
				try {
					noOfRecords = Integer.parseInt(strNoOfRecords);
				} catch (Exception e) {
					noOfRecords = 5;
				}
			}
			// Ends here

			hits = indexSearcher.search(queries[0], null, noOfRecords).scoreDocs;
			queryDetailBean.setFuzzyQuery(queries[0].toString());
			queryDetailBean.setNumOfFuzzyRecords(5);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < hits.length; i++) {
			try {
				final Document hitDoc = indexSearcher.doc(hits[i].doc,
						resultSetBasedField);
				SemanticSearchHelper.getInstance().processDocument(hitDoc,
						repos, RESULT_FUZZY, queryDetailBean);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		inProgress = false;
	}

	public boolean isInProgress() {
		return inProgress;
	}

}

class EntitySearchThread extends Thread implements SemClassSerachConstants {
	private QueryDetailBean queryDetailBean = null;
	private IndexSearcher indexSearcher = null;
	private SetBasedFieldSelector resultSetBasedField = null;
	private SemClassStore repos = null;
	private boolean inProgress = true;
	private MultiFieldQueryParser mulfieldParser;
	private QueryParser[] queryParser;
	Map<String, String> paramMap = null;

	// TODO to remove
	private String[] paramFields;
	private Occur[] paramFlags;

	public EntitySearchThread(IndexSearcher searcher,
			SetBasedFieldSelector setBasedFieldSelector,
			QueryDetailBean queryDetailBean, SemClassStore repos,
			Map<String, String> paramMap) {
		this.queryDetailBean = queryDetailBean;
		this.indexSearcher = searcher;
		this.resultSetBasedField = setBasedFieldSelector;
		this.repos = repos;
		this.paramMap = paramMap;
		// for(int i=0; i<entityViewFields.length;i++) {
		// entityBoosts.put(entityViewFields[i], Float
		// .valueOf(1));
		// }
		// mulfieldParser = new MultiFieldQueryParser(
		// Version.LUCENE_30, entityViewFields, new SimpleAnalyzer(
		// Version.LUCENE_30), entityBoosts);

		// TODO to remove
		Map<String, Float> paramSemBoosts = new HashMap<String, Float>();
		List<String> paramSearchFields = new ArrayList<String>();
		for (int i = 0; i < entityViewFields.length; i++) {
			if (paramMap.get("entity_" + entityViewFields[i]) != null) {
				paramSearchFields.add(entityViewFields[i]);
			}
		}

		int selFieldSize = paramSearchFields.size();

		if (selFieldSize == 0) {
			paramFields = entityViewFields;
			paramFlags = entityFlags;
		} else {
			paramFields = new String[selFieldSize];
			paramFlags = new BooleanClause.Occur[selFieldSize];
			paramFields = paramSearchFields.toArray(paramFields);
		}
		float paramBoost = 1;
		// Ends here

		for (int i = 0; i < paramFields.length; i++) {
			final String strParamBoost = paramMap.get("entity_"
					+ paramFields[i] + "-boost");
			paramBoost = 1;
			paramFlags[i] = BooleanClause.Occur.SHOULD;
			if (strParamBoost != null && !"".equals(strParamBoost.trim())) {
				try {
					paramBoost = Float.parseFloat(strParamBoost);
				} catch (Exception e) {
					paramBoost = 1;
				}

			}
			paramSemBoosts.put(paramFields[i], Float.valueOf(paramBoost));
		}

		mulfieldParser = new MultiFieldQueryParser(Version.LUCENE_30,
				paramFields, new SimpleAnalyzer(Version.LUCENE_30),
				paramSemBoosts);

		this.queryParser = new QueryParser[paramFields.length];
		for (int i = 0; i < paramFields.length; i++) {
			queryParser[i] = new QueryParser(Version.LUCENE_30, paramFields[i],
					new SimpleAnalyzer(Version.LUCENE_30));
		}

	}

	@Override
	public void run() {
		inProgress = true;

		ScoreDoc[] hits = null;

		String rawQuery = queryDetailBean.getRawQuery();
		// String[] querySplits = rawQuery.split(" ");
		//
		// String[] querySoftMatchSplits = new String[querySplits.length];
		//
		// for(int i=0;i<querySplits.length;i++) {
		// querySoftMatchSplits[i] = querySplits[i].substring(0, (int) Math
		// .floor(querySplits[i].length() / 2));
		// }

		Query[] queries = null;

		// To remove later TODO
		String param = paramMap.get("stems");
		if (param != null && !"".equals(param.trim())) {
			queryDetailBean.setStrStem(param);
		}

		param = paramMap.get("phrases");
		if (param != null && !"".equals(param.trim())) {
			queryDetailBean.setStrPhrase(param);
		}
		// Ends here To remove

		String phraseStr = queryDetailBean.getStrPhrase();

		// Add phrase query
		if (phraseStr.length() > 2) {
			queries = new Query[1 + paramFields.length];
			try {
				for (int i = 0; i < paramFields.length; i++) {
					queries[1 + i] = queryParser[i].parse(phraseStr);
					queries[1 + i].setBoost(2.0f);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			queries = new Query[1];
		}

		try {

			queries[0] = mulfieldParser.parse(Version.LUCENE_30, rawQuery + " "
					+ phraseStr + " " + queryDetailBean.getStrStem(),
					paramFields, paramFlags, new StandardAnalyzer(
							Version.LUCENE_30));
			queries[0].setBoost(1.0f);

			// More than 1 query
			if (queries.length > 1)
				queries[0].combine(queries);

			// TODO to remove
			int noOfRecords = 50;
			String strNoOfRecords = paramMap.get("Entity");
			if (strNoOfRecords != null && !"".equals(strNoOfRecords.trim())) {
				try {
					noOfRecords = Integer.parseInt(strNoOfRecords);
				} catch (Exception e) {
					noOfRecords = 50;
				}
			}
			// Ends here

			hits = indexSearcher.search(queries[0], null, noOfRecords).scoreDocs;
			queryDetailBean.setEntityQuery(queries[0].toString());
			queryDetailBean.setNumOfEntityRecords(20);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < hits.length; i++) {
			try {
				final Document hitDoc = indexSearcher.doc(hits[i].doc,
						resultSetBasedField);
				SemanticSearchHelper.getInstance().processDocument(hitDoc,
						repos, RESULT_ENTITY, queryDetailBean);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		inProgress = false;
	}

	public boolean isInProgress() {
		return inProgress;
	}
}

// TODO Better way to extract all matching entities
/*
 * private void identifyEntity(QueryDetailBean queryDetailBean) {
 * MultiFieldQueryParser mulfieldParser = new MultiFieldQueryParser(
 * Version.LUCENE_30, entityViewFields, new SimpleAnalyzer( Version.LUCENE_30),
 * entityBoosts); ScoreDoc[] hits = null;
 * 
 * 
 * 
 * Query[] queries = null;
 * 
 * String phraseStr = queryDetailBean.getStrPhrase();
 * 
 * // Add phrase query if(phraseStr.length() > 2) { queries = new
 * Query[1+entityViewFields.length]; try { for(int
 * i=0;i<entityViewFields.length;i++) { final QueryParser queryParser = new
 * QueryParser(Version.LUCENE_30, entityViewFields[i], new SimpleAnalyzer(
 * Version.LUCENE_30)); queries[1+i] = queryParser.parse(phraseStr);
 * queries[1+i].setBoost(2.0f); } } catch (Exception e) { e.printStackTrace(); }
 * } else { queries = new Query[1]; }
 * 
 * try { queries[0] = mulfieldParser.parse(Version.LUCENE_30,
 * queryDetailBean.getRawQuery() +phraseStr, entityViewFields, entityFlags, new
 * StandardAnalyzer( Version.LUCENE_30)); queries[0].setBoost(1.0f);
 * 
 * // More than 1 query if(queries.length > 1) queries[0].combine(queries);
 * 
 * hits = wikiIndSearcher.search(queries[0], null, 50).scoreDocs; } catch
 * (Exception e) { e.printStackTrace(); }
 * 
 * Field field = null; for (int i = 0; i < hits.length; i++) { try { final
 * Document hitDoc = wikiIndSearcher.doc(hits[i].doc, resultSetBasedField);
 * field = hitDoc.getField(IndexingConstants.PAGE_TITLE); if (field != null) {
 * 
 * } } catch (Exception e) { e.printStackTrace(); } }
 * 
 * }
 */