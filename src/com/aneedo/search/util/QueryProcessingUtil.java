package com.aneedo.search.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aneedo.search.bean.QueryDetailBean;
import com.aneedo.util.ExtractionUtil;
import com.aneedo.util.PorterStemmer;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;

public class QueryProcessingUtil {

	private static final String STANDFORD_PATH = SemClassConstants.SYNONYM_PARSER_PATH + SemClassConstants.PARSER_FILE;
	
	final TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
	
	private static	final Pattern PHRASE_PATTERN_NN_PREP = Pattern.compile("((nn|nnp|nns)[0-9])((in|to)[0-9])(dt[0-9])?(jj[0-9])*((nn|nnp|nns)[0-9])+");
	private final Pattern PHRASE_PATTERN_ADJ_NN = Pattern.compile("(jj[0-9])*((nn|nnp|nns)[0-9])+"); //((nn|nnp|nns)[0-9])((nn|nnp|nns)[0-9])?(cc[0-9])?
	private static final Pattern CHAR_PATTERN = Pattern.compile("[a-zA-Z]"); 
	

	final LexicalizedParser lp = new LexicalizedParser(STANDFORD_PATH);
	final Pattern singQuotePattern = Pattern.compile("\'[\\w\\W]*?\'");
	final Pattern doubQuotePattern = Pattern.compile("\"[\\w\\W]*?\"");
	
	public static QueryProcessingUtil util =null;
	
	private QueryProcessingUtil() {
		
	}
	
	public static QueryProcessingUtil getInstance() {
		if(util == null) {
			util = new QueryProcessingUtil();
		}
		return util;
	}
	
	public void setPosStemPhrase(QueryDetailBean queryDetailBean) {
		Map<String, String> posMap = new LinkedHashMap<String, String>();
		StringBuilder builder = new StringBuilder();
		List<String> wordList = new ArrayList<String>();
		
		String rawQuery = queryDetailBean.getRawQuery();
		String[] querySplits = rawQuery.split(" ");
		

		try {
		
		List<CoreLabel> rawWords = tokenizerFactory.getTokenizer(
				new StringReader(rawQuery)).tokenize();
		
		List<Integer> nnPositionList = new ArrayList<Integer>();
		
		ArrayList<TaggedWord> sentence1 = lp.apply(rawWords).taggedYield();
		
			for (int i = 0; i < sentence1.size(); i++) {
				final TaggedWord taggedWord = sentence1.get(i);
				
				final String pos = taggedWord.tag().toLowerCase();
				final String word = taggedWord.word();
				posMap.put(word, pos);
				
				// Fix for NLP parser, POS tags generated are case sensitive 
				if(SemClassConstants.POS_ADJ.equals(pos)) {

					final String titleCase = Character.toUpperCase(word.charAt(0)) +word.substring(1);

					rawWords = tokenizerFactory.getTokenizer(
							new StringReader(rawQuery.replaceAll(word, titleCase))).tokenize();

					final ArrayList<TaggedWord> sentence2 = lp.apply(rawWords).taggedYield();
					
					if(sentence2.get(i).tag().toLowerCase().startsWith(SemClassConstants.POS_NN)) {
						posMap.put(word, SemClassConstants.POS_NN);
						nnPositionList.add(i);
					}
				} else if(pos.startsWith(SemClassConstants.POS_NN)) {
					nnPositionList.add(i);
				}
				// Fix ends here
				
				builder.append(pos+i);
				wordList.add(word);
				//System.out.println("Word : " + word.word() +"Tag : " + tags.get(0).toLowerCase()+i);
			}
			//System.out.println(new Date(System.currentTimeMillis()));

			// Set stems
			StringBuilder stemBuilder = new StringBuilder();
			PorterStemmer stemmer = new PorterStemmer();

			List<String> stemList = ExtractionUtil.getInstance(SemClassConstants.SYNONYM_FILE).getRootFormStringList(querySplits, stemmer, stemBuilder);
			//System.out.println(querySplits.length +" Stem size :" + stemList.size());
			queryDetailBean.setStemList(stemList);
			queryDetailBean.setStrStem(stemBuilder.toString());

			StringBuilder nnBuilder = new StringBuilder();
			int size=nnPositionList.size();
			queryDetailBean.setNNSize(size);
			for(int i=0;i<size;i++) {
				final Integer nnPos =nnPositionList.get(i); 
				nnBuilder.append(querySplits[nnPos] + " " + stemList.get(nnPos) +" ");
			}
			queryDetailBean.setNounWordStemStr(nnBuilder.toString().trim());
			
			
		} catch (Exception e) {
		}
		
		queryDetailBean.setPostags(posMap);
		queryDetailBean.setQuerySplits(querySplits);

		
		// For calculating NDCG of entity title and query
		queryDetailBean.setQueryIDCG(SemanticSearchUtil.getInstance().getQueryIDCG(querySplits.length));
		
		String[] querySoftMatchSplits = new String[querySplits.length];
		
		for(int i=0;i<querySplits.length;i++) {
			querySoftMatchSplits[i] = querySplits[i].substring(0, (int) Math
					.floor(querySplits[i].length() / 2));
		}
		queryDetailBean.setHalfQuerySplits(querySoftMatchSplits);
		
		StringBuilder compareStr = new StringBuilder();
		Set<String> phraseList = new HashSet<String>();

		Matcher matcher = PHRASE_PATTERN_NN_PREP.matcher(builder.toString());
		
		while (matcher.find()) {
			final String strPhrase =  matcher.group();
			final StringBuilder phraseBuilder = new StringBuilder();
			final String[] strPositions = CHAR_PATTERN.split(strPhrase);
			for(int i=0;i<strPositions.length;i++) {
				if(!"".equals(strPositions[i])) {
					try{
					phraseBuilder.append(wordList.get(
							Integer.parseInt(strPositions[i])) +" ");
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
			}
			final String phrase = phraseBuilder.toString().trim();
			if(compareStr.indexOf(phrase) < 0) {
				phraseList.add(phrase);
				compareStr.append("\""+phrase +"\" ");
			}
		}
		
		matcher = PHRASE_PATTERN_ADJ_NN.matcher(builder.toString());
		
		while (matcher.find()) {
			final String strPhrase =  matcher.group();
			final StringBuilder phraseBuilder = new StringBuilder();
			final String[] strPositions = CHAR_PATTERN.split(strPhrase);
			for(int i=0;i<strPositions.length;i++) {
				if(!"".equals(strPositions[i])) {
					try{
					phraseBuilder.append(wordList.get(
							Integer.parseInt(strPositions[i])) +" ");
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
			}
			final String phrase = phraseBuilder.toString().trim();
			if(compareStr.indexOf(phrase) < 0) {
				phraseList.add(phrase);
				compareStr.append("\""+phrase +"\" ");
			}
		}
		
		
		

		matcher = singQuotePattern.matcher(rawQuery);
		while (matcher.find()) {
			System.out.println("Matched String : " + rawQuery.substring(matcher.start()+1, matcher.end()-1));
			String subStr = rawQuery.substring(matcher.start()+1, matcher.end()-1);
			if(compareStr.indexOf(subStr) < 0) {
			phraseList.add(subStr);
			compareStr.append("\""+subStr +"\" ");
			}
		} 
		matcher = doubQuotePattern.matcher(rawQuery);
		while (matcher.find()) {
			System.out.println("Matched String : " + rawQuery.substring(matcher.start()+1, matcher.end()-1));
			String subStr = rawQuery.substring(matcher.start()+1, matcher.end()-1);
			if(compareStr.indexOf(subStr) < 0) {
			phraseList.add(subStr);
			compareStr.append("\""+subStr +"\" ");
			}
		}

		queryDetailBean.setPhraseList(phraseList);
		queryDetailBean.setStrPhrase(compareStr.toString());
		//System.out.println(builder.toString());

	}
	
	public static void main(String[] args) {
		QueryDetailBean queryDetailBean = new QueryDetailBean();
		queryDetailBean.setRawQuery("List of laptop brands and manufacturers".toLowerCase());
		System.out.println(new Date(System.currentTimeMillis()));
		QueryProcessingUtil.getInstance().setPosStemPhrase(queryDetailBean);
		System.out.println(new Date(System.currentTimeMillis()));
		System.out.println(queryDetailBean.getStrPhrase());
	}
	public void setPhrasesStem(QueryDetailBean queryDetailBean) {
		// identify phrases
		String rawQuery = queryDetailBean.getRawQuery().toLowerCase();
		
		String[] splits = rawQuery.split(" ");
		
		queryDetailBean.setRawQuery(rawQuery);
		StringBuilder builder = new StringBuilder();
		PorterStemmer stemmer = new PorterStemmer();

		queryDetailBean.setStemList(ExtractionUtil.getInstance(SemClassConstants.SYNONYM_FILE).getRootFormStringList(splits, stemmer, builder));
		queryDetailBean.setStrStem(builder.toString());
		builder = new StringBuilder();

		Set<String> phraseList = new HashSet<String>();

		Matcher matcher = singQuotePattern.matcher(rawQuery);
		while (matcher.find()) {
			System.out.println("Matched String : " + rawQuery.substring(matcher.start()+1, matcher.end()-1));
			String subStr = rawQuery.substring(matcher.start()+1, matcher.end()-1);
			phraseList.add(subStr);
			builder.append("\""+subStr +"\" ");
		} 
		matcher = doubQuotePattern.matcher(rawQuery);
		while (matcher.find()) {
			System.out.println("Matched String : " + rawQuery.substring(matcher.start()+1, matcher.end()-1));
			String subStr = rawQuery.substring(matcher.start()+1, matcher.end()-1);
			phraseList.add(subStr);
			builder.append("\""+subStr +"\" ");
		}

		
		

		boolean isStartPOS = true;
		Map<String, String> posMap = queryDetailBean.getPostags();

		for (int i = 0; i < splits.length; i++) {
			if (splits[i].indexOf("-") >= 0) {
				phraseList.add(splits[i]);
				builder.append("\""+splits[i] +"\" ");
				String repStr = splits[i].replaceAll("-", " ");
				phraseList.add(repStr);
				builder.append("\""+repStr +"\" ");
			}
		}

		int numOfWords = posMap.size();
		StringBuilder phrase = new StringBuilder();
		if (numOfWords >= 1) {
			Iterator<String> itr = posMap.keySet().iterator();
			String posTag = null;
			String key = null;
			while (itr.hasNext()) {
				key = itr.next();
				posTag = posMap.get(key);
				// System.out.println("Key " + key + "Pos tag : " + posTag);
				if (posTag != null
						&& !("need".equalsIgnoreCase(key.trim())
								|| "want".equalsIgnoreCase(key.trim()) || "i"
								.equalsIgnoreCase(key.trim()))) {
					posTag = posTag.toLowerCase();
					if (posTag.toLowerCase().startsWith("nn") && isStartPOS) {
						phrase.append(key);
						isStartPOS = false;
					} else if (posTag.toLowerCase().startsWith("nn")
							&& !isStartPOS) {
						phrase.append(" " + key);
						isStartPOS = false;
					} else {
						if(phrase.length() > 0) {
							System.out.println("Pharase POS :" + phrase.toString());
							phraseList.add(phrase.toString());
							builder.append("\""+phrase.toString()+"\" ");
						}
						isStartPOS = true;
						phrase = new StringBuilder();
					}
				}
			}

		}

		queryDetailBean.setPhraseList(phraseList);
		queryDetailBean.setStrPhrase(builder.toString());
	}

}
