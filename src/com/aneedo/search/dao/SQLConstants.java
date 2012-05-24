package com.aneedo.search.dao;

public interface SQLConstants {

	// query log
	public static final String INSERT_QUERY = "insert into query(rawQuery, userId, creationTime) values(?,?,?)";
	public static final String SELECT_MAX_QUERY_ID = "select max(queryId) max_id from querydetails";
	public static final String INSERT_QUERY_FACET = "insert into query_facet_detail(queryId, facet, isRelevant) values(?,?,?)";
	
	
	public static final String INSERT_QUERY_DETAILS="insert into querydetails(user, query, time) values(?,?,?)";
	
	public static final String INSERT_QUERY_DEPENDENT="insert into queryDependentParam(queryID, TruePOS, TotalPOS, TrueStem," +
			"TotalStem, TruePhrase, TotalPhrase) values(?,?,?,?,?,?,?)";
	public static final String INSERT_QUERY_INDEPENDENT="insert into searchFields(queryID,section_heading,synopsis, hypernym, " +
			"association_related, association_inlink, association_related_hierarchy, frequent, homonym," +
			"synonym, synonym_wordnet, make_produce, meronym, reference, role, sibling, " +
			"fuzzy_association_inlink, entity_synonym, entity_synonym_wordnet, entity_page_title," +
			"entity_title_disamb) values(?,?,?,?,?," +
										"?,?,?,?,?," +
										"?,?,?,?,?," +
										"?,?,?,?,?,?)";
	public static final String INSERT_FIELD_BOOSTS="insert into FieldBoosts(queryID, section_heading_Boost, synopsis_Boost, hypernym_Boost, " +
	"association_related_Boost, association__Boostinlink_Boost, association_related_hierarchy_Boost, frequent_Boost, homonym_Boost," +
	"synonym_Boost, synonym_wordnet_Boost, make_produce_Boost, meronym_Boost, reference_Boost, role_Boost, sibling_Boost, " +
	"fuzzy_association_inlink_Boost, entity_synonym_Boost, entity_synonym_wordnet_Boost, entity_page_title_Boost" +
	"entity_title_disamb_Boost) values(?,?,?,?,?," +
									  "?,?,?,?,?," +
									  "?,?,?,?,?," +
									  "?,?,?,?,?,?)";
}
