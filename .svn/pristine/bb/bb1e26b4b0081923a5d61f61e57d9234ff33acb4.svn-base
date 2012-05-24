package com.aneedo.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TermFreqBasedTrimmer {
	public static final TermFreqBasedTrimmer termFreqTrim =new TermFreqBasedTrimmer();

	private final Pattern replacepattern = Pattern.compile("[()\",.0-9]");
	
	//PorterStemmer stemmer = new PorterStemmer();
	private TermFreqBasedTrimmer() {
	
	}
	
	public static TermFreqBasedTrimmer getInstance() {
		return termFreqTrim;
	}
	
	public String getFreqWords(String text, PorterStemmer stemmer, String pageName,InterestingWordRecognizer intWordRecog) {
		if(text != null) {
			// Clean (), "", , etc
			Matcher matcher = replacepattern.matcher(text);
			text = matcher.replaceAll(" ");
			
			// Avoid all months
			text = ExtractionUtil.getInstance(null).replaceMonth(text);
			
			//System.out.println("After removing junk : " + text);
			
			String[] splits = text.split(" ");
			
		 return getFreqWords(Arrays.asList(splits),stemmer,pageName,intWordRecog);
		} 
		return null;
	}

	public String getFreqWords(List<String> textList, PorterStemmer stemmer, String pageName,InterestingWordRecognizer intWordRecog) {
		// Remove stop words
		List<String> intWordList = StopwordRecognizer.getInstance().removeStopWords(textList);
		
		// Get only interesting words
		//intWordList = intWordRecog.getInterestingWords(intWordList);
		
		// stem words and get words more than 2 times
		return stemmedFreqWords(intWordList,stemmer,pageName);
		
	}
	
	private String stemmedFreqWords(List<String> intWordList, PorterStemmer stemmer, String pageName) {
		Set<String> distnictWordSet = new HashSet<String>();
		String rootForm = null;
		StringBuilder builder = new StringBuilder();
		
		Map<String,Integer> wordCountMap = new HashMap<String, Integer>();
		
		String key = null;
		Integer count = null;
		//System.out.println(pageName);
		pageName = pageName.toLowerCase();
		for(int i=0,size=intWordList.size();i<size;i++) {
			key = intWordList.get(i).toLowerCase();
			
			// don;t add entity
			if(key.startsWith(pageName) || pageName.startsWith(key)) {
				continue;
			}
			
			// use original word if stemmer makes mistakes
			rootForm = ExtractionUtil.getInstance(null).getRootForm(key, stemmer);
			
			
				/* More the occurrence can be
				 * part of the interpretation label 
				 */
				count = wordCountMap.get(rootForm);
				if(count == null) {
					wordCountMap.put(rootForm, 1);
				} else {
					wordCountMap.put(rootForm, count+1);
					if(count == 2) {
						distnictWordSet.add(rootForm);
					}
				}
		}
		Iterator<String> itr = distnictWordSet.iterator();
		while(itr.hasNext()) {
			key = itr.next();
			builder.append(key + " | " +wordCountMap.get(key) + " ");
		}
		//System.out.println("After stemming : " + builder.toString());
		return builder.toString();
	}
	
	
	public static void main(String[] args) {
		
	}

}
