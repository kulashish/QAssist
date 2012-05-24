package com.aneedo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aneedo.search.util.TagText;

public class InterestingWordRecognizer {
//	private IDictionary dictionary;
//	private static final List<POS> ALLOWABLE_PARTS_OF_SPEECH = Arrays
//			.asList(new POS[] { POS.NOUN, POS.VERB, POS.ADJECTIVE, POS.ADVERB });

	private static InterestingWordRecognizer intWordRecog = null;
	private final Pattern nonWordpattern = Pattern.compile("^[a-zA-Z-']");

	private final Pattern replacepattern = Pattern.compile("[()\",.0-9]");
	private final Pattern templatePattern = Pattern.compile("TEMPLATE\\[([^\\]]+)]");
	private final Pattern quotePattern = Pattern.compile("\\[\\[([^\\]\\]]+)]]");
	private final Pattern tagPattern1 = Pattern.compile("(?i)(<ref.*?>)(.+?)(</ref>)");

	private String path = null;
	//TagText tagText = TagText.getInstance();
	
	public final String JUNK_WORDS_PAGE ="infobox template logo png file image size color text name imagesize " +
			"birth_date birth_place birth_name jpg px caption thumb mm left right retrieved";

	public InterestingWordRecognizer(String path) {
		this.path = path;
//		try {
//			this.dictionary = new Dictionary(new URL("file", null,
//					"/home/ambha/aneedo/wordnet4/dict"));
//			// DataIntegrationConstants.WORDNET_LOCATION));
//			dictionary.open();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	 public static InterestingWordRecognizer getInstance(String path) {
		 if(intWordRecog == null)
			 intWordRecog = new InterestingWordRecognizer(path);
	 return intWordRecog;
	 }

	public String[] getInterestingWords(String text) {
		
		Matcher matcher = templatePattern.matcher(text);
		text = matcher.replaceAll("");
		//System.out.println("Text tem: " + text);
		matcher = quotePattern.matcher(text);
		text = matcher.replaceAll("");
		//System.out.println("Text img : " + text);
		matcher = tagPattern1.matcher(text);
		text = matcher.replaceAll("");
		
		matcher = replacepattern.matcher(text);
		text = matcher.replaceAll(" ");
		
		// System.out.println(text);
		//String[] tokens = text.split(" ");
		TagText tagText = TagText.getInstance(path);
		String taggedText = tagText.tagSentence(text);
		//System.out.println(taggedText);
		String[] taggedWords = taggedText.split(" ");
		//System.out.println("Split length" + taggedWords.length);
		StringBuilder outputNNTokens = new StringBuilder();
		StringBuilder outputVBTokens = new StringBuilder();

		StringBuilder phraseTokens = null;
		boolean isPrevNN = false;
		// Matcher matcher = null;

		for(int i=0;i<taggedWords.length;i++){
			final String[] splits = taggedWords[i].split("/");
			if(splits.length <= 1) continue;
			final String token = splits[0].toLowerCase().trim();
			final String posTag = splits[1].toLowerCase();
			//System.out.println(token + " "+posTag);
			if(!(posTag.indexOf("nn") >= 0
					||posTag.indexOf("vb") >= 0
					||posTag.indexOf("jj") >= 0)) {
				isPrevNN = false;
				continue;
			}
		
			//System.out.println(token + " "+posTag);
			
			// don't add junk words
			matcher = nonWordpattern.matcher(token);
			if (matcher.matches() || token.length() <=2) {
				isPrevNN = false;
				continue;
			}
			
			if(ExtractionUtil.getInstance(path).isMonth(token)
					|| StopwordRecognizer.getInstance().isStopWord(token)) {
				isPrevNN = false;
				continue;
			}
			
			if(JUNK_WORDS_PAGE.indexOf(token) >= 0
					||token.indexOf("-") >= 0
					||token.indexOf("_") >= 0
					||token.indexOf("^") >= 0
					||token.indexOf("=") >= 0
					||token.indexOf("==") >= 0
					||token.indexOf("<") >= 0
					|| token.endsWith("px")) {
				isPrevNN = false;
				continue;
			}

			// add only NN phrases, VB, adj etc
			if (posTag.indexOf("nn") >= 0) {
				if (isPrevNN) {
					phraseTokens.append(token + " ");
				} else {
					if(phraseTokens != null)
						outputNNTokens.append(phraseTokens + " | ");
					phraseTokens = new StringBuilder();
					phraseTokens.append(token + " ");
				}
				isPrevNN = true;
				continue;
			}
			if (posTag.indexOf("vb") >= 0) {
				outputVBTokens.append(token + " ");
				isPrevNN = false;
				continue;
			}
			if (posTag.indexOf("jj") >= 0) {
				outputVBTokens.append(token + " ");
			}
			isPrevNN = false;
		}

		String[] nounAndVerbs = new String[2];
		nounAndVerbs[0] = outputNNTokens.toString();
		nounAndVerbs[1] = outputVBTokens.toString();
		return nounAndVerbs;
	}

	/**
	 * Porter stemmer does not take care of past and ing form
	 * 
	 * @param word
	 * @param stemmer
	 * @return
	 */
//	public boolean isVerb(String word, PorterStemmer stemmer) {
//		ExtractionUtil util = ExtractionUtil.getInstance();
//		word = util.getRootForm(word, stemmer);
//		IIndexWord indexWord = null;
//		if (word.endsWith("ed")) {
//			word = word.substring(0, word.length() - 2);
//		} else if (word.endsWith("ing")) {
//			word = word.substring(0, word.length() - 3);
//			indexWord = dictionaryLookup(word, POS.VERB);
//			if (indexWord != null)
//				return true;
//			word = word + "e";
//		}
//		indexWord = dictionaryLookup(word, POS.VERB);
//		if (indexWord == null) {
//			return false;
//		}
//		return true;
//	}

	public boolean isVerb(String word, PorterStemmer stemmer) {
	ExtractionUtil util = ExtractionUtil.getInstance(path);
	word = util.getRootForm(word, stemmer);
	String[] splits = null;
	TagText tagText = TagText.getInstance(path);
	
	if(StopwordRecognizer.getInstance().isStopWord(word)) {
		return false;
	}
	
	
	
	if (word.endsWith("ed")) {
		word = word.substring(0, word.length() - 2);
	} else if (word.endsWith("ing")) {
		word = word.substring(0, word.length() - 3);
		
		splits = tagText.tagSentence(word).split("/");
		if(splits != null && splits.length>1) {
		if (splits[1].indexOf("vb") >= 0)
			return true;
		}
		word = word + "e";
	}
	splits = tagText.tagSentence(word).split("/");
	if(splits != null && splits.length>1) {
	if (splits[1].indexOf("vb") >= 0)
		return true;
	}
	return false;
}

	
//	public List<String> getInterestingWords(List<String> tokens) {
//
//		List<String> outputTokens = new ArrayList<String>();
//		// List<Integer> freqCount = new ArrayList<Integer>();
//		Matcher matcher = null;
//
//		for (String token : tokens) {
//			token = token.trim();
//
//			// don't add junk words
//			matcher = nonWordpattern.matcher(token);
//			if (matcher.matches() || "".equals(token) || "A".equals(token)) {
//				continue;
//			}
//
//			// add only verbs, nouns etc
//			for (POS allowablePartOfSpeech : ALLOWABLE_PARTS_OF_SPEECH) {
//				IIndexWord indexWord = dictionaryLookup(token,
//						allowablePartOfSpeech);
//				if (indexWord != null) {
//					// System.out.print(token + " ");
//					outputTokens.add(token);
//					break;
//				}
//			}
//
//		}
//		// System.out.println("After interesting words ... ");
//		return outputTokens;
//	}

//	private synchronized IIndexWord dictionaryLookup(String token, POS posTag) {
//		return dictionary.getIndexWord(token, posTag);
//	}

	public static void main(String[] args) {
		String wordRecog[] = new InterestingWordRecognizer(null)
				.getInterestingWords("A laptop (notebook)\"A small, lightweight laptop computer in full "
						+ "notebook computer \". Retrieved 16 November 2010.\"A small, lightweight laptop computer.\" "
						+ "Retrieved 16 November 2010. is a personal computer for mobile use.\"2. A light, portable computer that is generally "
						+ "thinner than a laptop.\" Retrieved 16 November 2010.\"How to Buy Laptop Computers\". Retrieved 16 November 2010.What "
						+ "is a laptop computer "
						+ "A laptop integrates most of the typical components of a desktop computer, including a display, a keyboard, a pointing "
						+ "device (a touchpad, also known as a trackpad, and/or a pointing stick) and speakers into a single unit. A laptop is powered "
						+ "by mains electricity "
						+ "via an AC adapter, and can be used away from an outlet using a rechargeable battery. A laptop battery in new condition "
						+ "typically stores enough energy to run the laptop for three to five hours, depending on the computer usage, "
						+ "configuration and power management settings. Template of image size is jpg birth_place"
						+ "When the laptop is plugged into the mains, the battery charges, whether or not the computer is running.");
//		System.out.println(new InterestingWordRecognizer().isVerb("hoping",
//				new PorterStemmer()));
		System.out.println(wordRecog[0]);
		System.out.println("*********");
		System.out.println(wordRecog[1]);
	}

}
