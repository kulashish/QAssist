package com.aneedo.util;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aneedo.indexing.WikiPageExtractor;

public class ExtractionUtil {
	private static ExtractionUtil util = null;
	private SynonymMap synonymMap = null;
	private final Pattern numPattern = Pattern.compile("[0-9]");
	private final Pattern pattern = Pattern.compile(Pattern.quote(" is a ")
			+"|"+Pattern.quote(" was a ")
			+"|"+Pattern.quote(" was an ")
			+"|"+Pattern.quote(" is an ")
			+"|"+Pattern.quote(" was the ")
			+"|"+Pattern.quote(" is the ")
			+"|"+Pattern.quote(" is a line of ")
			+"|"+Pattern.quote(" like ")
			+"|"+Pattern.quote(" such as "), Pattern.CASE_INSENSITIVE);
	
	private final Pattern MONTH_PATTERN = Pattern.compile(Pattern.quote("january")
			+"|"+Pattern.quote("february")
			+"|"+Pattern.quote("march")
			+"|"+Pattern.quote("april")
			+"|"+Pattern.quote("may")
			+"|"+Pattern.quote("june")
			+"|"+Pattern.quote("july")
			+"|"+Pattern.quote("august")
			+"|"+Pattern.quote("september")
			+"|"+Pattern.quote("october")
			+"|"+Pattern.quote("november")
			+"|"+Pattern.quote("retrieved")
			+"|"+Pattern.quote("december"), Pattern.CASE_INSENSITIVE);
	
	private final Pattern wordpattern = Pattern.compile("[^a-zA-Z0-9.',]");
	
	private final Pattern catNumPattern = Pattern.compile("[0-9]");
	
	private final Pattern synPattern = Pattern.compile(Pattern.quote("also known as")
			+"|"+Pattern.quote(" also called a ")
			+"|"+Pattern.quote(" also called an "), Pattern.CASE_INSENSITIVE);
	

	public static ExtractionUtil getInstance(String path) {
		if(util == null) {
			util = new ExtractionUtil(path);
		}
		return util;
	}
	
	private final StringBuilder infoBoxKeys = new StringBuilder(); 
	//PorterStemmer stemmer = new PorterStemmer();

	public ExtractionUtil(String path) {
		try {
			synonymMap = new SynonymMap(new FileInputStream(
					path+"/wn_s.pl"));
			infoBoxKeys.append("occupations ");
			infoBoxKeys.append("roles ");
			infoBoxKeys.append("professions ");
			infoBoxKeys.append("industry ");
			infoBoxKeys.append("types ");
			infoBoxKeys.append("products ");
			infoBoxKeys.append("names ");
			infoBoxKeys.append("known for ");
			infoBoxKeys.append("develop ");
			infoBoxKeys.append("manufact ");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getInfoBoxKeys() {
		return infoBoxKeys.toString();
	}

	public Set<String> getSynonyms(String word,StringBuilder builder) {
		if (word == null) {
			return null;
		}

		final Set<String> resultList = new HashSet<String>();
		String[] splits = word.split(" ");

		//StringBuilder builder = new StringBuilder("");
		for (int i = 0; i < splits.length; i++) {
		//	System.out.println("i:"+i+" "+"ss"+splits[i]);
			String[] synonyms = synonymMap.getSynonyms(splits[i]);
			if (synonyms != null) {
			//	System.out.println(synonyms.length);
				for (int k = 0; k < synonyms.length; k++) {
					builder.append(" " + synonyms[k] + " | ");
					//System.out.println("syn:"+synonyms[k]);
					if(synonyms[k].trim().length() > 2) {
					resultList.add(synonyms[k]);
					}
				}
			}
		}
		if (builder.length() <= 1) {
			return null;
		}

		return resultList;
	}
	
	public Set<String> getWordsfromBuilder(StringBuilder builder, String splitStr){
		if(splitStr==null) return null;
		final Set<String> resultWords= new HashSet<String>();
		String[] splits=builder.toString().split(splitStr);
		resultWords.addAll(Arrays.asList(splits));
		return resultWords;
	}
	
	public static void main(String[] args) {
		//QueryProcessingUtil util = QueryProcessingUtil.getInstance();
		////System.out.println(util.canIncludeCategory("Personal222computing"));
//		List matches = new ArrayList();
//		Pattern p = Pattern.compile("([0-9]{3})-([0-9]{3})-([0-9]{4})");
//		String text = "Albert Pujols\t111-456-7890\nDarth Vader\t222-123-4567\nCarrot Top\t333-123-4444\n";
//		//List matches = RegexUtil.getMatches(p, text);
//		Matcher m = p.matcher(text);
//		int index = 0;
//		while(m.find(index)) {
//		//matches.add(m.group(1));
//			System.out.println("1: " +m.group(1));
//			System.out.println("3: " + m.group(3));
//			System.out.println("2: " + m.group(2));
//			//System.out.println(m.group(1));
//		index = m.end();
//		}
//		ExtractionUtil util = ExtractionUtil.getInstance();
//		Set<String> resultList = util.getSynonyms("river", new StringBuilder());
//		Iterator<String> iter= resultList.iterator();
//		while(iter.hasNext())
//			System.out.println(iter.next());
//		//System.out.println(util.canIncludeCategory("1998 books"));
//		StringBuilder builder = new StringBuilder();
//		util.getSynonyms("booking", builder);
//		System.out.println(builder.toString());
		// System.out.println(util.getRootForm("building", new PorterStemmer()));
		//util.getSynonyms("prepaid", )
		String[] sections = {"History","Oscar statuette","Nomination","Ceremony","Awards ceremonies","Venues","Academy Awards of Merit","Special Academy Awards",
				"Criticisms","Associated events","See also","Notes","References","External links"};
		for(int i=0;i<sections.length;i++)
		System.out.println(WikiPageExtractor.getNEString(sections[i]));
	}
	
	public int getPosAfterPattern(String text) {
		// Clean junk character so that pattern matching does not fail
		Matcher matcher = wordpattern.matcher(text);
		text = matcher.replaceAll(" ");
		//System.out.println("Text after matching : " + text);
		int pos = -1;
	    matcher = pattern.matcher(text);
	    while (matcher.find()) {
	     return matcher.end();
	    }
		return pos;
	}
	
	public int getPosAfterPatternSynonym(String text) {
		// Clean junk character so that pattern matching does not fail
		Matcher matcher = wordpattern.matcher(text);
		text = matcher.replaceAll(" ");
		
		int pos = -1;
	    matcher = synPattern.matcher(text);
	    while (matcher.find()) {
	     return matcher.end();
	    }
		return pos;
	}
	
	public boolean isJaccardSimHigh(List<String> toCompareList, String input,PorterStemmer stemmer) {
		
		// If any of the string (pagetile, synonym, type) is having similarity more than .5
		for(int i=0,size=toCompareList.size();i<size;i++) {
			if(getJaccardSim(toCompareList.get(i), input, stemmer) > 0.499) {
				return true;
			}
		}
		
		return isAllMatch(toCompareList, input, stemmer);
	}
	
	public boolean isAllMatch(List<String> toCompareList, String input,PorterStemmer stemmer) {
		// The combination of pagetile, synonym, type is having similarity 1
		StringBuilder builder = new StringBuilder();
		for(int i=0,size=toCompareList.size();i<size;i++) {
			builder.append(toCompareList.get(i) + " ");
		}
		
		if(getJaccardSim(builder.toString(), input, stemmer) >= .90) {
			return true;
		}
		return false;
	}
	
	public boolean isJaccardHigh(String toCompare, String input,PorterStemmer stemmer) {
		toCompare = toCompare.replaceAll(" \\| ", " ").replaceAll("/", " ");
		Matcher matcher = numPattern.matcher(toCompare);
		toCompare = matcher.replaceAll(" ");
		if(getJaccardSim(toCompare, input, stemmer) >= .50) {
			return true;
		}
		return false;
	}
	
	public double getJaccardSim(String toCompare,  String input,PorterStemmer stemmer) {
        // Remove disambiguation stuff ()
		toCompare = toCompare.replace("(", " ").replace(")", " ");
		input = input.replace("(", " ").replace(")", " ");
		
		// If both are one and same
		if(toCompare.replaceAll(" ", "").equalsIgnoreCase(input.replaceAll(" ", ""))) {
			return 0;
		}
		
		// Get stemmed first
		//System.out.println("ToCompare : " + toCompare + " Input : " + input);
		Set<String> toCompareStem = getRootFormStringSet(toCompare, stemmer);
		Set<String> inputStem = getRootFormStringSet(input, stemmer);
		
		
		// category, page title should be matched and normalised
		Iterator<String> itr = toCompareStem.iterator();
		int count = 0;
		while(itr.hasNext()) {
			if(!inputStem.add(itr.next())) {
				count++;
			}
		}
		
		if(count != 0) {
			//System.out.println("Value :" + (double) count/toCompareStem.size());
			return ((double) count/toCompareStem.size());
		}
		
		return 0;
	}
	
	public boolean canIncludeCategory(String catName) {
		
		// dont' include numbered category
		String[] splits = catName.split(" ");
		
		for(int i=0;i<splits.length;i++) {
		final Matcher matcher = catNumPattern.matcher(splits[i]); 
		while (matcher.find()) {
			
			try {
			Integer.parseInt(splits[i]);
			return false;
			} catch (Exception e) {
				// Just Ignore
			}
		}
		}
		return true;
	}
	
	
	
	public String replaceMonth(String text) {
		Matcher matcher = MONTH_PATTERN.matcher(text);
		return matcher.replaceAll(" ");
	}
	
	public boolean isMonth(String text) {
		Matcher matcher = MONTH_PATTERN.matcher(text);
		return matcher.matches();
	}
	
	public Set<String> getRootFormStringSet(String str, PorterStemmer stemmer) {
		 String[] splits = str.split(" ");
		   Set<String> stemSet = new HashSet<String>();
		   for(int j=0;j<splits.length;j++) {
			   stemSet.add(getRootForm(splits[j].trim().toLowerCase(), stemmer));
		   }
		   return stemSet;
	}
	
	public Set<String> getRootFormStringSet(String[] splits, PorterStemmer stemmer, StringBuilder builder) {
		 //String[] splits = str.split(" ");
		   Set<String> stemSet = new HashSet<String>();
		   for(int j=0;j<splits.length;j++) {
			   final String stem = getRootForm(splits[j].trim().toLowerCase(), stemmer);
			   if(stemSet.add(stem)) {
				   builder.append(stem + " ");
			   }
		   }
		   return stemSet;
	}
	
	public List<String> getRootFormStringList(String[] splits, PorterStemmer stemmer, StringBuilder builder) {
		
		List<String> stemList = new ArrayList<String>();
		   for(int j=0;j<splits.length;j++) {
			   final String stem = getRootForm(splits[j], stemmer);
			   if(!stemList.contains(stem)) {
				   builder.append(stem + " ");
			   }
			   stemList.add(stem);
		   }
		   return stemList;
	}
	
	public String getRootFormString(List<String> splits, PorterStemmer stemmer) {
		StringBuilder builder = new StringBuilder();
		   for(int j=0,size=splits.size();j<size;j++) {
			   final String stem = getRootForm(splits.get(j), stemmer);
			   if(builder.indexOf(stem) < 0) {
				   builder.append(stem + " ");
			   }
		   }
		   return builder.toString().trim();
	}

	
	public Set<String> getRootFormStringSet(String str, PorterStemmer stemmer, StringBuilder builder) {
		 String[] splits = str.split(" ");
		 return getRootFormStringSet(splits, stemmer, builder);
		 
	}
	
	public List<String> getRootFormStringList(String str, PorterStemmer stemmer, StringBuilder builder) {
		 String[] splits = str.split(" ");
		 return getRootFormStringList(splits, stemmer, builder);
	}
	
	
	public String getRootFormStrings(String str, PorterStemmer stemmer) {
		 String[] splits = str.split(" ");
		   StringBuilder stems = new StringBuilder();
		   for(int j=0;j<splits.length;j++) {
			   stems.append(getRootForm(splits[j].trim().toLowerCase(), stemmer) +" ");
		   }
		   return stems.toString().trim();
	}
	
	public String getRootForm(String inputStr, PorterStemmer stemmer ) {
		String copy = inputStr.trim();
		inputStr = stemmer.stemString(inputStr);

		// input is singular and stem is proper
		if(inputStr.equals(copy)) {
			return copy;
		}
		
		// if ends with ful, ing take 
		if(copy.endsWith("ing") || copy.endsWith("ful")) {
			String predictRoot = copy.substring(0,copy.length()-3);
			if(inputStr.equals(predictRoot)) {
				return inputStr;
			}
		} else 
		
		// if ends with ment, ness, able
		if(copy.endsWith("ment") || copy.endsWith("ness") 
				|| copy.endsWith("able")) {
			String predictRoot = copy.substring(0,copy.length()-4);
			if(inputStr.equals(predictRoot)) {
				return inputStr;
			}
		} else 
		
		// if ends with ment, ness, able
		if(copy.endsWith("less")) {
			String predictRoot = copy.substring(0,copy.length()-4);
			return predictRoot;
		}
		
		String stem = inputStr;
		
		// TODO fix for plural ending with ies
		if (inputStr.endsWith("s") || inputStr.endsWith("x")
				|| inputStr.endsWith("o") || inputStr.endsWith("ss")
				|| inputStr.endsWith("ch") || inputStr.endsWith("sh")
				|| inputStr.endsWith("or")) {
			inputStr = inputStr + "es";
		} else if (inputStr.endsWith("is")) {
			inputStr = inputStr.substring(0, inputStr.length() - 2) + "es";
		} else if (inputStr.endsWith("y")) {
			inputStr = inputStr.substring(0, inputStr.length() - 1) + "ies";
		} else if (inputStr.endsWith("ey")) {
			inputStr = inputStr.substring(0, inputStr.length() - 2) + "ies";
		} else {
			inputStr = inputStr + "s";
		}
		
		// If i get the original plural then doing proper stemming
		if(copy.equals(inputStr)) {
			return stem;
		}
		
		return copy;
	}
	
	public String getPlural(String inputStr, PorterStemmer stemmer) {
		 
			inputStr = getRootForm(inputStr, stemmer);
			
			if (inputStr.endsWith("s") || inputStr.endsWith("x")
					|| inputStr.endsWith("o") || inputStr.endsWith("ss")
					|| inputStr.endsWith("ch") || inputStr.endsWith("sh")
					|| inputStr.endsWith("or")) {
				inputStr = inputStr + "es";
			} else if (inputStr.endsWith("is")) {
				inputStr = inputStr.substring(0, inputStr.length() - 2) + "es";
			} else if (inputStr.endsWith("y")) {
				inputStr = inputStr.substring(0, inputStr.length() - 1) + "ies";
			} else if (inputStr.endsWith("ey")) {
				inputStr = inputStr.substring(0, inputStr.length() - 2) + "ies";
			} else {
				inputStr = inputStr + "s";
			}
			//////System.out.println("After Plural form : " + inputStr);
			return inputStr.toLowerCase();
		
	}
}
