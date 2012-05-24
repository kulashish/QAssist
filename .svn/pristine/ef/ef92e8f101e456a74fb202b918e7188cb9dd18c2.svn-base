package com.aneedo.indexing;

public interface IndexingConstants {
	
	// ****************** FIELDS ******************************
	// page title, chop off disambiguation text
	public static final String PAGE_TITLE = "page_title";
	// Store the disambiguation text separately
	public static final String PAGE_TITLE_DISAMB = "title_disamb";
	// Id
	public static final String PAGE_ID = "page_id";
	// page type
	public static final String PAGE_TYPES = "page_types";
	public static final String NO_OF_INLINKS = "inlink_count";
	public static final String NO_OF_OUTLINKS = "outlink_count";
	public static final String NE_TYPE = "NE_type";
	
	// section
	public static final String SECTION_HEADING = "section_heading";
	public static final String SECTION_LINK_TEXT = "link_title";
	public static final String LINK_LEFT_CONTEXT = "link_left_context";
	public static final String LINK_RIGHT_CONTEXT = "link_right_context";
	public static final String IMAGE_TEXT = "image_text";
	public static final String NUM_OF_SECTION = "num_of_sections";
	
	
	public static final String[] TITLE_STOP_WORDS = new String[] 
			{"of", "by", "in" , "on ","based","from", "the", "about","list","comparision"};
	
	
	//********************** SEMANTIC CLASSES ******************************
	// nouns in the page abstract
	public static final String SEM_CLASS_SYNOPSIS = "synopsis";
	
	// Contains all verbs and adjectives of abstract
	public static final String SYNOPSIS_VB_ADJ = "synopsis_vbadj";

	// redirects, synonyms, info box: *name, abstract:also known as, also called,
	public static final String SEM_CLASS_SYNONYM = "synonym";
	public static final String SEM_CLASS_SYNONYM_WORDNET = "synonym_wordnet";
	
	// disambiguation
	public static final String SEM_CLASS_HOMONYM = "homonym";
	
	// make produce
	public static final String SEM_CLASS_MAKEPRODUCE = "make_produce";

	// info box: products
	// INfo box :
	public static final String SEM_CLASS_MERONYM = "meronym";
	
	/* info box : Type,Industry  abstract: is/was a separated by comma,  is/was kind of
	 * Categories : consider a match words more than 50% of page title, abstract is a
	 * Ignore category with number, 
	 *  Stem the category, if it is the page title ignore
	 *  If the number of sub categories more than 50 ignore the category, otherwise I will hit the administrative category or 
	 *  list out administrative category
	 */
	public static final String SEM_CLASS_HYPERNYM = "hypernym";
	
	/* Ending with sub categories/pages  of selected category/page title
	 */
	public static final String SEM_CLASS_HYPONYM = "hyponym";
	
	// Info box: Known for or occupation or Profession or role
	public static final String SEM_CLASS_ROLE = "role";
	
	// pages under selected categories
	public static final String SEM_CLASS_SIBLING = "sibling";
	
	// association class break into sub classes (internal link text, related (see also), external link text)
	public static final String SEM_CLASS_ASSOCIATION_INLINK = "association_inlink";

	// Association class inlink
	public static final String SEM_CLASS_ASSOCIATION_INLINK_PARENT = "association_inlink_parent";
	
	// see also
	public static final String SEM_CLASS_ASSOCIATION_RELATED = "association_related";
	
	//Starting with subcategories/pages of selected category/page title
	public static final String SEM_CLASS_ASSOCIATION_HIERARCHY = "association_hierarchy";

	//Starting with subcategories/pages of selected category/page title
	public static final String SEM_CLASS_COOCCURRENCE = "cooccurrence";

	//Starting with subcategories/pages of selected category/page title
	public static final String SEM_CLASS_ASSOCIATION_RELATED_HIERARCHY = "association_related_hierarchy";

	// external link text match pagetitle, type of the page title reference or note or further reading,reference,external link
	public static final String SEM_CLASS_REFERENCE = "reference";
	
	// Frequent Class (section wise frequent words occurring)
	public static final String SEM_CLASS_FREQUENT = "frequent";
	
	// Frequent Class (section wise frequent words occurring)
	public static final String SEM_CLASS_PRODUCT = "product";
	
	public static final String SEM_CLASS_CAUSE_EFFECT = "cause_effect";
	public static final String SEM_CLASS_PROFIT_BENEFIT = "profit_benefit";
	
	
	public static final String NE_TYPE_PERSON = "person";
	public static final String NE_TYPE_LOCATION = "location";
	public static final String NE_TYPE_ACADEMIA = "academia";
	public static final String NE_TYPE_COMPANY = "company";
	public static final String NE_TYPE_MUSIC = "music";
	public static final String NE_TYPE_MOVIE = "movie";
			
	/********** For searching *********/

	// section words
	public static final int INT_SECTIONS = 0;
	
	public static final int INT_SEM_CLASS_SYNOPSIS = 1;

	// redirects, synonyms, info box: *name, abstract:also known as, also called,
	public static final int INT_SEM_CLASS_SYNONYM = 2;

	public static final int INT_SEM_CLASS_HYPERNYM = 3;
	
	/* Ending with sub categories/pages  of selected category/page title
	 */
	public static final int INT_SEM_CLASS_HYPONYM = 4;
	
	
	// pages under selected categories
	public static final int INT_SEM_CLASS_SIBLING = 5;
	
	// association class break into sub classes (internal link text, related (see also), external link text)
	public static final int INT_SEM_CLASS_ASSOCIATION_INLINK = 6;
	
	// see also
	public static final int INT_SEM_CLASS_ASSOCIATION_RELATED = 7;
	
	//Starting with subcategories/pages of selected category/page title
	public static final int INT_SEM_CLASS_ASSOCIATION_HIERARCHY = 8;
	
	// external link text match pagetitle, type of the page title reference or note or further reading,reference,external link
	public static final int INT_SEM_CLASS_REFERENCE = 9;
	
	// Frequent Class (section wise frequent words occurring)
	public static final int INT_SEM_CLASS_FREQUENT = 10;
	
	// info box: products
	public static final int INT_SEM_CLASS_MERONYM = 11;

	// Info box: Known for or occupation or Profession or role
	public static final int INT_SEM_CLASS_ROLE = 14;

	// make produce
	public static final int INT_SEM_CLASS_MAKEPRODUCE = 15;

	// product Class words
	public static final int INT_SEM_CLASS_PRODUCT = 16;

	// disambiguation
	public static final int INT_SEM_CLASS_HOMONYM = 17;
	
	public static final int INT_SEM_CLASS_SYNONYM_WORDNET = 18;
	
	
	public static final int NUM_OF_SEM_CLASSES = 19;
	
	public static final int NUM_OF_SEM_CLASSES_FOR_COMPARISON = 12;

}
