package com.aneedo.jwplext;

public interface JwplSQLConstants {
	
	// Page and page category selection 
	public static final String SELECT_PAGE_CATEGORY_DETAIL = "select p.id as pid, p.pageId as ppageId, p.text as text, " +
			"p.name as pname,p.isDisambiguation as isDisambiguation, c.id as cid, c.pageId as cpageId, c.name as cname from Page p, " +
			"page_categories pc, Category c where p.id = pc.id and c.pageId = pc.pages and p.pageId = ?";
	public static final String SELECT_PLAIN_PAGE = "select id,  pageId, name, text from Page where pageId = ?";
	public static final String SELECT_PAGE = "select id as pid,  pageId as ppageId, name as pname, text, isDisambiguation from Page where pageId = ?";
	public static final String SELECT_COUNT_INLINKS="select count(inLinks) from page_inlinks where id = ?";
	public static final String SELECT_COUNT_OUTLINKS="select count(outLinks) from page_outlinks where id = ?";
	public static final String SELECT_GET_INLINK_ID="select pageId from DisambiguationPage where inlink=?";
	public static final String SELECT_DISAMB_PAGEID= "select pageId,title from DisambiguationPage where outlink = ?";
	public static final String SELECT_DISAMB_DATA = "select inlinkId,name from disambInlinkCount d,Page p where p.pageId = d.inlinkId " +
			"and d.pageId = ? order by count DESC";
	public final String SELECT_OUTLINKS = "select outLinks from page_outlinks where id = ?";
	public final String SELECT_ASS_PARENT_OVERLAP = "select distinct pc2.id as pageId, c.pageId as catId, c.name as name from page_categories pc1, "
			+ "page_categories pc2, Category c where c.pageId = pc1.pages and c.pageId = pc2.pages and pc1.id = ? and pc2.id = ?";
	public final String SELECT_ASS_OVERLAP_QUERY = "select distinct o1.id as pageId1, o2.id as pageId2, "
			+ "p1.name as name1, p2.name as name2, count(*) as overlap from page_outlinks o1, page_outlinks o2, Page p1, Page p2 where o1.outLinks = o2.outLinks "
			+ "and p1.id = o1.id and p2.id = o2.id and o1.id = ? and o2.id = ? group by o1.id";

	public static final String STORE_ANCESTORS = "insert into CategoryPath values(?,?)";
	
	public static final String SELECT_CATEGORY_PATH="select cp.ancestors as ancestors, c.name as category_name from CategoryCleaned c, CategoryPath cp where c.pageId = cp.id and LOWER(c.name) = LOWER(?)";
//	public static final String SELECT_PAGE_CATEGORY = "select pages page_categories where id = ?";
//	//pageId = pages of page_categories
//	public static final String SELECT_CATEGORY_DETAIL = "select id, pageId,name from Category where pageId = ?";

	// redirect id = Page.id
	public static final String  SELECT_REDIRECT = "select redirects from page_redirects where id = ?";

	// redirect id = Page.id
	public static final String  SELECT_LINK_OVERLAP_COUNT = "select link2, name2, overlap from outlink_overlap where link1 = ?";

	// redirect id = Page.id
	public static final String  SELECT_LINK_CATEGORY_OVERLAP = "select category_ids, category_names, pageId2 from page_category_overlap " +
			"where pageId1 = ? order by pageId2";

	
	// internal links 
	public static final String  SELECT_INLINKS = "select p2.name as pname from page_outlinks po, Page p2, Page p1 where " +
			"po.id = p1.id and p2.pageId = po.outLinks and p1.pageId = ?";
	//disambiguation page
	public static final String  SELECT_DISAMB = "select name, text from DisamPage where name = ? or ?";
	// sub category and pages selection for selected category
	public static final String SELECT_PAGES_OF_CATEGORY = "select p.name as name, p.pageId as subpageId, c1.pageId as pageId from category_pages cp, " +
			"Category c1, Page p where cp.id =  c1.id and p.pageId = cp.pages and c1.pageId in (";
	public static final String SELECT_SUB_CATEGORY = "select c2.name as name, c2.pageId as subpageId, c1.pageId as pageId from category_outlinks co, " +
			"Category c1, Category c2 where co.id =  c1.id and c2.pageId = co.outLinks and c1.pageId in (";
	
	public static final String SELECT_PARENT_CATEGORY = "select c2.name as name, c1.pageId as pageId from category_inlinks co, " +
	"Category c1, Category c2 where co.id =  c1.id and c2.pageId = co.outLinks and c1.pageId in (";
	
	// TODO Related : make a combination of Parent and sub category
	
	// Category name
	public static final String SELECT_CATID_GIVEN_NAME = "select pageId from CategoryCleaned where name = ?";
	public static final String SELECT_CATEGORY = "select id,  pageId, name from Category where pageId = ?";
	public static final String SELECT_OUTLINKS_GIVEN_CATNAME = "select c1.outlinks as outlinks from category_outlinks c1, CategoryCleaned cc where cc.pageId = c1.id and cc.name = ? ";
    public static final String SELECT_SUB_CATEGORY_ID = "select outlinks from category_outlinks where id = ?";
    public static final String COUNT_COMMON_PAGES = "select count(*) as count from category_pages c1, category_pages c2 where c1.id = ? and c2.id = ? and c1.pages = c2.pages";
}
