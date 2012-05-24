package com.aneedo.search.dao;

import java.util.HashMap;
import java.util.Map;

public class Sample {
	public static void main(String[] args) {
		String s="entity_title_disamb-boost:1.0: synonym:true: make_produce-boost:1.0: section_heading:true: association_related:true: homonym-boost:1.0: lambda:0.7: association_related-boost:1.0: association_related_hierarchy:true: role:true: association_related_hierarchy-boost:1.0: Fuzzy:5: homonym:true: frequent-boost:1.0: entity_page_title-boost:1.0: trueStems:0: totalPos:1: synonym_wordnet:true: truePos:0: POS_1:NN: synopsis-boost:1.0: POS_2:NN: SemClass:30: entity_synonym:true: fuzzy_association_inlink-boost:1.0: POS_0:NNS: query_input:ladies apparel gaga: Entity:20: section_heading-boost:1.0: entity_synonym_wordnet:true: truePhrases:0: role-boost:1.0: totalStems:1: entity_page_title:true: synopsis:true: stems:ladies apparel gaga: entity_title_disamb:true: fuzzy_association_inlink:true: sibling-boost:1.0: entity_synonym-boost:1.0: association_inlink:true: hypernym-boost:1.0: entity_synonym_wordnet-boost:1.0: synonym-boost:1.0: frequent:true: reference:true: meronym-boost:1.0: totalPhrases:1: sibling:true: synonym_wordnet-boost:1.0: hypernym:true: phrases:ladies apparel gaga: association_inlink-boost:1.0: reference-boost:1.0: user:1: make_produce:true: meronym:true";
		String ss[]=s.split(":");
		System.out.println(ss.length);
		Map<String,String> fun=new HashMap<String, String>();
		for(int i=0;i<ss.length;i=i+2)
			fun.put(ss[i], ss[i+1]);
		SemanticClassDao scd=SemanticClassDao.getInstance();
		scd.storeResults(fun);
	}

}
