package com.aneedo.jwplext;

import de.tudarmstadt.ukp.wikipedia.api.WikiConstants.Language;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParser;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParserFactory;
/**
 * Overriding this because constructor of MediaWikiParserFactory is not 
 * exposed
 * @author
 *
 */
public class JwplMediaWikiParserFactory extends MediaWikiParserFactory {

	public static final Language language = Language.english;
	
	public static final JwplMediaWikiParserFactory factory = new JwplMediaWikiParserFactory();
	
	private JwplMediaWikiParserFactory(){
		super(language);
	}
	
	public static JwplMediaWikiParserFactory getInstance() {
		return factory;
	}
	
	public MediaWikiParser getParser() {
		return createParser();
	}
}
