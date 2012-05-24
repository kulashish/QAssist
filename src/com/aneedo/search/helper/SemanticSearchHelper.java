package com.aneedo.search.helper;

import gnu.trove.TIntObjectHashMap;
import gnu.trove.TIntProcedure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;

import com.aneedo.indexing.IndexingConstants;
import com.aneedo.search.SemClassStore;
import com.aneedo.search.bean.EntityWordBean;
import com.aneedo.search.bean.QueryDetailBean;
import com.aneedo.search.bean.SemEntityBean;
import com.aneedo.search.util.SemanticSearchUtil;


public class SemanticSearchHelper {
	
	private static final SemanticSearchHelper util = new SemanticSearchHelper();

	private SemanticSearchHelper() {

	}

	public static SemanticSearchHelper getInstance() {
		return util;
	}
	
	public void processDocument(Document hitDoc, SemClassStore repos, 
			int resultType, QueryDetailBean queryDetailBean) {

		String[] querySplits = queryDetailBean.getQuerySplits();

		Fieldable field = hitDoc.getFieldable(IndexingConstants.PAGE_ID);
		Integer pageId = null;
		if (field != null) {
			pageId = Integer.parseInt(field.stringValue());

		}
		if (repos.isAlreadyPresent(pageId, resultType)) {
			return;
		}
		final SemEntityBean semEntity = new SemEntityBean();
		semEntity.setPageId(pageId);
		
		repos.setFetchedFrom(semEntity, resultType);

		boolean[] hardMatch = new boolean[querySplits.length];
		
		SemanticSearchUtil searchUtil = SemanticSearchUtil.getInstance();
		double[] allSemClassQueryMatch = new double[querySplits.length];

		field = hitDoc.getField(IndexingConstants.PAGE_TITLE);
		String[] titleSplits = null;
		// TODO require title splits? 
		//List<String> stemList = ExtractionUtil.getInstance().getRootFormStringList(querySplits, stemmer, stemBuilder);
		if (field != null) {
			final Field disambField = hitDoc
					.getField(IndexingConstants.PAGE_TITLE_DISAMB);
			if (disambField != null) {
				semEntity.setTitle(field.stringValue() + " "
						+ disambField.stringValue());

			} else {
				semEntity.setTitle(field.stringValue());
				semEntity.setDisam(true);
			}
			titleSplits = semEntity.getTitle().toLowerCase().split(" ");
			double[] softMatch = new double[querySplits.length];
			int[] matchedPositions = new int[titleSplits.length];
			searchUtil.computeSimilarity(querySplits, titleSplits, softMatch, hardMatch, matchedPositions, 
					queryDetailBean.getStemList());

			semEntity.setMatchedPositions(matchedPositions);
			semEntity.setSoftTitleQueryMatch(softMatch);
			
			// initialize all query sem match to title match
			allSemClassQueryMatch = Arrays.copyOf(softMatch,querySplits.length);
			semEntity.setAllSemClsQueryMatch(allSemClassQueryMatch);
			
		}

		
		String fieldValue = null;
		Map<String,EntityWordBean> entityWordMap = new HashMap<String, EntityWordBean>();
		TIntObjectHashMap<List<String>> wordSemMap = new TIntObjectHashMap<List<String>>();

		field = hitDoc.getField(IndexingConstants.SECTION_HEADING);
		//StringBuilder semMemHashBuilder = new StringBuilder();
		//StringBuilder semMemBuilder = new StringBuilder();
		//final String[] semMembers = new String[2];
		//System.out.println("Page id : " + pageId);
		if (field != null) {
			if (field.stringValue() != null) {
				fieldValue = field.stringValue();
				//System.out.println("Sections : "+ fieldValue );
				String[] splits = fieldValue.split(" \\| ");
				List<String> semMemHashList = new ArrayList<String>();
				for (int j = 0; j < splits.length; j++) {
					if (!(splits[j].equals("abstract")
							|| splits[j].equals("history") || splits[j]
							.indexOf("graphy") >= 0)) {
						//Integer wordHash = splits[j].hashCode();
						
						if(entityWordMap.containsKey(splits[j])) {
							final EntityWordBean wordBean = entityWordMap.get(splits[j]);
							wordBean.setTotalFreq(wordBean.getTotalFreq() + 1);
							semMemHashList.add(wordBean.getWord());
							//semMemBuilder.append(splits[j]+",");
							wordBean.getSemClassMatch()[IndexingConstants.INT_SECTIONS] = true;
							
						} else {

							final String[] semClassSplits = splits[j]
								   									.split(" ");
								
							final double[] softMatch = new double[querySplits.length];
						 final EntityWordBean wordBean = new EntityWordBean();
						wordBean.setWord(splits[j]);
						
							if (searchUtil.computeSimilarity(querySplits, semClassSplits,
									softMatch, null, null, queryDetailBean.getStemList())) {
								wordBean.setQueryMatch(softMatch);
								if(!semEntity.isAllMatched()) 
									semEntity.setAllMatched(updateQueryAllMatchEntity(semEntity.getAllSemClsQueryMatch(), softMatch));
							} 
							final double[] titleMatch = new double[titleSplits.length];
							if (searchUtil.computeSimilarity(titleSplits, semClassSplits,
									titleMatch, null, null, null)) {
								wordBean.setTitleMatch(titleMatch);
							} 	
							wordBean.setTotalFreq(1);
							wordBean.getSemClassMatch()[IndexingConstants.INT_SECTIONS] = true;
							entityWordMap.put(splits[j], wordBean);
							//semMemBuilder.append(splits[j]+",");
							semMemHashList.add(splits[j]);
								
							}
							
					}
			}
				wordSemMap.put(IndexingConstants.INT_SECTIONS, semMemHashList);
		}
		}
		
		field = hitDoc.getField(IndexingConstants.SEM_CLASS_HYPERNYM);
		fillSemClass(IndexingConstants.INT_SEM_CLASS_HYPERNYM, field, 
				querySplits, titleSplits, semEntity, entityWordMap, wordSemMap, searchUtil, queryDetailBean);

		field = hitDoc
				.getField(IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED);
		fillSemClass(IndexingConstants.INT_SEM_CLASS_ASSOCIATION_RELATED, field, 
				querySplits, titleSplits, semEntity, entityWordMap, wordSemMap, searchUtil, queryDetailBean);
		
		field = hitDoc
				.getField(IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK);
		fillSemClass(IndexingConstants.INT_SEM_CLASS_ASSOCIATION_INLINK, field, 
				querySplits, titleSplits, semEntity, entityWordMap, wordSemMap, searchUtil, queryDetailBean);

		field = hitDoc
				.getField(IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED_HIERARCHY);
		fillSemClass(IndexingConstants.INT_SEM_CLASS_ASSOCIATION_HIERARCHY, field, 
				querySplits, titleSplits, semEntity, entityWordMap, wordSemMap, searchUtil, queryDetailBean);
		
		field = hitDoc.getField(IndexingConstants.SEM_CLASS_FREQUENT);
		fillSemClass(IndexingConstants.INT_SEM_CLASS_FREQUENT, field, 
				querySplits, titleSplits, semEntity, entityWordMap, wordSemMap, searchUtil, queryDetailBean);
		//semEntity.getSemClassForPartialMatch()[1] = field.stringValue();

		field = hitDoc.getField(IndexingConstants.SEM_CLASS_HOMONYM);
		fillSemClass(IndexingConstants.INT_SEM_CLASS_HOMONYM, field, 
				querySplits, titleSplits, semEntity, entityWordMap, wordSemMap, searchUtil, queryDetailBean);
		
		field = hitDoc.getField(IndexingConstants.SEM_CLASS_ROLE);
		fillSemClass(IndexingConstants.INT_SEM_CLASS_ROLE, field, 
				querySplits, titleSplits, semEntity, entityWordMap, wordSemMap, searchUtil, queryDetailBean);

		field = hitDoc.getField(IndexingConstants.SEM_CLASS_SIBLING);
		fillSemClass(IndexingConstants.INT_SEM_CLASS_SIBLING, field, 
				querySplits, titleSplits, semEntity, entityWordMap, wordSemMap, searchUtil, queryDetailBean);

		field = hitDoc.getField(IndexingConstants.SEM_CLASS_REFERENCE);
		fillSemClass(IndexingConstants.INT_SEM_CLASS_REFERENCE, field, 
				querySplits, titleSplits, semEntity, entityWordMap, wordSemMap, searchUtil, queryDetailBean);
		
		field = hitDoc.getField(IndexingConstants.SEM_CLASS_SYNONYM);
		fillSemClass(IndexingConstants.INT_SEM_CLASS_SYNONYM, field, 
				querySplits, titleSplits, semEntity, entityWordMap, wordSemMap, searchUtil, queryDetailBean);


		field = hitDoc.getField(IndexingConstants.SEM_CLASS_MERONYM);
		fillSemClass(IndexingConstants.INT_SEM_CLASS_MERONYM, field, 
				querySplits, titleSplits, semEntity, entityWordMap, wordSemMap, searchUtil, queryDetailBean);
		
		field = hitDoc.getField(IndexingConstants.SEM_CLASS_SYNOPSIS);
		fillSemClass(IndexingConstants.INT_SEM_CLASS_SYNOPSIS, field, 
				querySplits, titleSplits, semEntity, entityWordMap, wordSemMap, searchUtil, queryDetailBean);
		//semEntity.getSemClassForPartialMatch()[0] = field.stringValue();
		
		semEntity.setWordSemClassMap(wordSemMap);
		semEntity.setEntDetailBeanMap(entityWordMap);
		
		repos.add(pageId, semEntity, hardMatch, resultType);

	}
	
	private boolean updateQueryAllMatchEntity(double[] allSemQueryMatch, double[] update) {
		boolean allMatched = true;
		for(int i=0;i<allSemQueryMatch.length;i++) {
			if(allSemQueryMatch[i] < update [i]) {
				allSemQueryMatch[i] = update [i];
			}
			if(!allMatched && allSemQueryMatch[i] < 1.0) {
				allMatched = false;
			}
			
		}
		return allMatched;
	}
	
	private void fillSemClass(int semClass, Fieldable field, String[] querySplits, 
			String[] titleSplits, 
			SemEntityBean semEntity, Map<String,EntityWordBean> entityWordMap, 
			TIntObjectHashMap<List<String>> wordSemMap, 
			SemanticSearchUtil searchUtil, QueryDetailBean queryDetailBean) {
		String fieldValue = null;
		if (field != null) {
			if (field.stringValue() != null) {
				fieldValue = field.stringValue();
				//System.out.println(SemanticSearchUtil.getInstance().getSemClassName(semClass) +" : "+fieldValue);
				String[] splits = fieldValue.split(" \\| ");
				List<String> semMemHashList = new ArrayList<String>();
				for (int j = 0; j < splits.length; j++) {
						
						int increment = 1;
						if(IndexingConstants.INT_SEM_CLASS_FREQUENT == semClass && splits[j].length() > 0) {
							//System.out.println(fieldValue);
							if(j < splits.length-1) {
								//System.out.println("split"+ j + splits[j+1].trim());
							final int pos = splits[j+1].trim().indexOf(" ");
							try {
								//System.out.println("J : " + j);
								if(pos > 0)
									increment = Integer.parseInt(splits[j+1].substring(0, pos).trim());
								else if(j+1 == splits.length - 1 )
									increment = Integer.parseInt(splits[j+1].trim());
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							splits[j+1] = splits[j+1].substring(pos+1);
							} else {
								splits[j] = null;
							}
						}
						if(splits[j] != null) {
						final String[] semClassSplits = splits[j].split(" ");
						
						final double[] softMatch = new double[querySplits.length];
						//Integer wordHash = splits[j].hashCode();
						
						if(entityWordMap.containsKey(splits[j])) {
							final EntityWordBean wordBean = entityWordMap.get(splits[j]);
							wordBean.setTotalFreq(wordBean.getTotalFreq() + increment);
							semMemHashList.add(wordBean.getWord());
							//semMemBuilder.append(splits[j]+",");
							wordBean.getSemClassMatch()[semClass] = true;
							
						} else {

						final EntityWordBean wordBean = new EntityWordBean();
						wordBean.setWord(splits[j]);
						
							if (searchUtil.computeSimilarity(querySplits, semClassSplits,
									softMatch, null, null, queryDetailBean.getStemList())) {
								wordBean.setQueryMatch(softMatch);
								if(!semEntity.isAllMatched())
									semEntity.setAllMatched(updateQueryAllMatchEntity(semEntity.getAllSemClsQueryMatch(), softMatch));
							} 
							final double[] titleMatch = new double[titleSplits.length];
							if (searchUtil.computeSimilarity(titleSplits, semClassSplits,
									titleMatch, null, null, null)) {
								wordBean.setTitleMatch(titleMatch);
							} 	
							wordBean.setTotalFreq(increment);
							wordBean.getSemClassMatch()[semClass] = true;
							entityWordMap.put(splits[j], wordBean);
							//semMemBuilder.append(splits[j]+",");
							semMemHashList.add(splits[j]);
								
							}
						}
							
			}
				wordSemMap.put(semClass, semMemHashList);
		}
		}
	}
}

class TIntProcedureImpl implements TIntProcedure {
	
	public boolean execute(int arg0) {
		// TODO Auto-generated method stub
		return true;
	}
}
