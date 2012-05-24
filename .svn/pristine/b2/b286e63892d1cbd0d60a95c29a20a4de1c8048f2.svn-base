package com.aneedo.search.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.search.BooleanClause;

import com.aneedo.indexing.IndexingConstants;

public interface SemClassSerachConstants {
	public static final int RESULT_ENTITY = 1;
	public static final int RESULT_SEM_CLASS = 2;
	public static final int RESULT_FUZZY = 3;
	 
	
	public final String[] phraseFields= new String[] {
//			IndexingConstants.PAGE_TITLE + IndexingConstants.PAGE_TITLE_DISAMB,
			IndexingConstants.SECTION_HEADING,
			IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED,
			IndexingConstants.SEM_CLASS_SYNOPSIS,
			IndexingConstants.SEM_CLASS_HYPONYM,
			IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED_HIERARCHY,
			IndexingConstants.SEM_CLASS_FREQUENT,
			IndexingConstants.SEM_CLASS_REFERENCE};
	
	public final Map<String, Float> semanticBoosts = new HashMap<String, Float>();
	
	public final BooleanClause.Occur[] semanticFlags = new BooleanClause.Occur[] {
//			BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD,
			BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD,
			BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD,
			BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD,
			BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD,
			BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD,
			BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD,
			BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD,
			BooleanClause.Occur.SHOULD };

	/*
	 * What fields to be searched
	 */
	public final String[] semanticViewFields = {
			// Captured in entity view
	        //IndexingConstants.PAGE_TITLE, IndexingConstants.PAGE_TITLE_DISAMB,
			IndexingConstants.SECTION_HEADING,
			IndexingConstants.SEM_CLASS_SYNOPSIS,
			IndexingConstants.SEM_CLASS_HYPERNYM,
			IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED,
			IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK,
			IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED_HIERARCHY,
			IndexingConstants.SEM_CLASS_FREQUENT,
			IndexingConstants.SEM_CLASS_HOMONYM,
			IndexingConstants.SEM_CLASS_SYNONYM,
			IndexingConstants.SEM_CLASS_SYNONYM_WORDNET,
			IndexingConstants.SEM_CLASS_MAKEPRODUCE,
			IndexingConstants.SEM_CLASS_MERONYM,
			IndexingConstants.SEM_CLASS_REFERENCE,
			IndexingConstants.SEM_CLASS_ROLE,
			IndexingConstants.SEM_CLASS_SIBLING };
	
	public final Map<String, Float> fuzzyBoosts = new HashMap<String, Float>();
	
	public final Map<String, Float> entityBoosts = new HashMap<String, Float>();
	
	public final BooleanClause.Occur[] fuzzyFlags = new BooleanClause.Occur[] {
//			BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD,
//			BooleanClause.Occur.SHOULD, //BooleanClause.Occur.SHOULD,
			BooleanClause.Occur.SHOULD};
	
	public final BooleanClause.Occur[] entityFlags = new BooleanClause.Occur[] {
			BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD,
			BooleanClause.Occur.SHOULD, //BooleanClause.Occur.SHOULD,
			BooleanClause.Occur.SHOULD};

	public final String[] fuzzyViewFields = {
			IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK
			//IndexingConstants.SEM_CLASS_HYPERNYM
	};
	
	public final String[] entityViewFields = {
			IndexingConstants.SEM_CLASS_SYNONYM,
			IndexingConstants.SEM_CLASS_SYNONYM_WORDNET,
			IndexingConstants.PAGE_TITLE, IndexingConstants.PAGE_TITLE_DISAMB,
	};
	
	/*********** Output constants **********/
	//For boost factors, behind every field name there will be one extra "-boost" appended.
	// Also for entity fields append "entity_" to remove ambiguity across views.
	
	public static final String TRUE_POS="truePos";
	public static final String TOTAL_POS="totalPos";
	public static final String TRUE_STEMS="trueStems";
	public static final String TOTAL_STEMS="totalStems";
	public static final String TRUE_PHRASES="truePhrases";
	public static final String TOTAL_PHRASES="totalPhrases";
	public static final String NUM_OF_FUZZY_RECORDS="Fuzzy";
	public static final String NUM_OF_SEMCLASS_RECORDS="SemClass";
	public static final String NUM_OF_ENTITY_RECORDS="Entity";
	public static final String LAMBDA="lambda";
	
	// TODO to add co-occurrence once extracted
	Integer[] NON_ENT_CAT_SEM_CLASSES = new Integer[] {IndexingConstants.INT_SEM_CLASS_FREQUENT,IndexingConstants.INT_SEM_CLASS_SYNOPSIS};
}
