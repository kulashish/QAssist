package com.aneedo.search.util;

public interface SemClassConstants {
	public static final String PROPERTY_FILE = "SemClassConfig.properties";
	// public static final String WIKI_PATH = "/data2/indexing/WikiIndexing";
	public static final String WIKI_PATH = "/data2/indexing/semantic/EntireRuns/CompleteIndex";
	// public static final String WIKI_PATH =
	// "/mnt/bag1/querysystem/indexing/semantic/WikiIndexing";
	public static final String SYNONYM_PARSER_PATH = "/data2/indexing/";
	// public static final String SYNONYM_PARSER_PATH = "/home/ashish/QAssist/";
	public static final String PARSER_FILE = "englishPCFG.ser.gz";
	public static final String SYNONYM_FILE = "/home/kedar/workspace/QAssist";
	// public static final String SYNONYM_FILE = "/home/ashish/QAssist";
	public static final int RESULT_ENTITY = 1;
	public static final int RESULT_SEM_CLASS = 2;
	public static final int RESULT_FUZZY = 3;

	// Entity set type
	public static final int ENT_SET_TYPE_EXACT_MATCH = 0;
	public static final int ENT_SET_TYPE_PAIRWISE_MATCH_BOTH = 1;
	public static final int ENT_SET_TYPE_PAIRWISE_MATCH_ONE_LEFT = 2;
	public static final int ENT_SET_TYPE_PAIRWISE_MATCH_ONE_RIGHT = 3;
	public static final int ENT_SET_TYPE_PAIRWISE_MATCH_NO_EVIDENCE = 4;
	public static final int ENT_SET_TYPE_NO_MATCH = 5;

	public static final int INTER_ENTITY = 1;
	public static final int INTER_CATEGORY = 2;
	public static final int INTER_OTHER = 3;

	// POS Tag
	public static final String POS_ADJ = "jj";
	public static final String POS_VB = "vb";
	public static final String POS_NN = "nn";
	public static final String POS_ADV = "rb";

}
