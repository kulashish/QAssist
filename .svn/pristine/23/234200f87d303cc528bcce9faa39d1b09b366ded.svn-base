package com.aneedo.search.util;

import gnu.trove.TIntObjectHashMap;
import gnu.trove.TObjectDoubleHashMap;
import gnu.trove.TObjectIntHashMap;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.aneedo.indexing.IndexingConstants;
import com.aneedo.search.bean.EntityPair;
import com.aneedo.search.bean.EntityWordBean;
import com.aneedo.search.bean.FeatureData;
import com.aneedo.search.bean.QueryDetailBean;
import com.aneedo.search.bean.SemEntityBean;
import com.aneedo.search.bean.SemInterpretation;
import com.aneedo.util.ExtractionUtil;
import com.aneedo.util.PorterStemmer;

public class SemanticSearchUtil {

	private static final SemanticSearchUtil util = new SemanticSearchUtil();

	private SemanticSearchUtil() {

	}

	public static SemanticSearchUtil getInstance() {
		return util;
	}
	

	public double getSoftMatchSum(double[] softmatch) {
		double result = 0;
		for (int i = 0; i < softmatch.length; i++) {
			result = softmatch[i] + result;
		}
		return result;
	}
	
	public int getNNVBADJCount(Map<String,String> wordPOSTags) {
		Iterator<String> posTagItr = wordPOSTags.values().iterator();
		int count = 0;
		while(posTagItr.hasNext()) {
			final String posTag = posTagItr.next();
			if(posTag.indexOf(SemClassConstants.POS_NN) >=0 
					|| posTag.indexOf(SemClassConstants.POS_VB) >=0
					|| posTag.indexOf(SemClassConstants.POS_ADJ) >=0) {
				count++;
			}
		}
		return count;
	}

	public boolean computeSimilarity(String[] query, String[] input,
			double[] softMatch, boolean[] hardMatch, int[] titlePosMatch,
			List<String> stemList) {
		// double[] softMatch = new double[query.length];
		boolean atLeastMatch = false;
		Arrays.fill(softMatch, 0);
		// int[] inputMatch = new int[input.length];
		if (titlePosMatch != null) {
			Arrays.fill(titlePosMatch, -1);
		}
		if (hardMatch != null) {
			// hardMatch = new boolean[query.length];
			Arrays.fill(hardMatch, false);
		}
		for (int i = 0; i < query.length; i++) {
			final int qWordLen = query[i].length();
			final String subWord = query[i].substring(0, (int) Math
					.floor(qWordLen / 2));
			final int subWordLen = subWord.length();
			double max = 0.0;
			String stemQuery = null;
			if (stemList != null)
				stemQuery = stemList.get(i);
			for (int j = 0; j < input.length; j++) {
				if (input[j].equals(query[i])
						|| input[j].startsWith(query[i])
						|| (stemQuery != null && input[j].startsWith(stemQuery))) {
					softMatch[i] = 1.0;
					if (titlePosMatch != null)
						titlePosMatch[j] = i;
					if (hardMatch != null && !hardMatch[i])
						hardMatch[i] = true;
				} else if (input[j].startsWith(subWord)) {
					// max = 0.0;
					final int inputLen = input[j].length();
					int k = subWordLen;
					for (; k < qWordLen && k < inputLen; k++) {
						if (query[i].charAt(k) == input[j].charAt(k)) {

						} else {
							break;
						}
					}
					max = (double) k / qWordLen;
					if (max > softMatch[i]) {
						softMatch[i] = max;
						if (titlePosMatch != null)
							titlePosMatch[j] = i;
						atLeastMatch = true;
						if (hardMatch != null && !hardMatch[i])
							hardMatch[i] = true;

						// word occurring again in input
					} else if (titlePosMatch != null && titlePosMatch[j] < 0) {
						titlePosMatch[j] = i;
					}

				}
			}
		}
		return atLeastMatch;
	}

	public boolean computeSimilarity(String[] query, String[] input,
			double[] softMatch, boolean[] hardMatch) {
		boolean atLeastMatch = false;
		if(softMatch != null)
			Arrays.fill(softMatch, 0);
		if (hardMatch != null) {
			Arrays.fill(hardMatch, false);
		}
		for (int i = 0; i < query.length; i++) {
			final int qWordLen = query[i].length();
			final String subWord = query[i].substring(0, (int) Math
					.floor(qWordLen / 2));
			final int subWordLen = subWord.length();
			double max = 0.0;
			for (int j = 0; j < input.length; j++) {
				if (input[j].equals(query[i])
						|| input[j].startsWith(query[i])) {
					softMatch[i] = 1.0;
					if (hardMatch != null && !hardMatch[i])
						hardMatch[i] = true;
				} else if (input[j].startsWith(subWord)) {
					// max = 0.0;
					final int inputLen = input[j].length();
					int k = subWordLen;
					for (; k < qWordLen && k < inputLen; k++) {
						if (query[i].charAt(k) == input[j].charAt(k)) {

						} else {
							break;
						}
					}
					max = (double) k / qWordLen;
					if (max > softMatch[i]) {
						softMatch[i] = max;
						atLeastMatch = true;
						if (hardMatch != null && !hardMatch[i])
							hardMatch[i] = true;
						// word occurring again in input
					}

				}
			}
		}
		return atLeastMatch;
	}

	
	public double computeSimilarity(String[] toMatch, String[] input) {
		//double[] softMatch = new double[toMatch.length];
		double match = 0.0;
		for (int i = 0; i < toMatch.length; i++) {
			final int qWordLen = toMatch[i].length();
			final String subWord = toMatch[i].substring(0, (int) Math
					.floor(qWordLen / 2));
			final int subWordLen = subWord.length();
			double max = 0.0;
			//double current = 0.0;
			for (int j = 0; j < input.length; j++) {
				if (input[j].equals(toMatch[i])
						|| input[j].startsWith(toMatch[i])) {
					max = 1.0;
				} else if (input[j].startsWith(subWord)) {
					// max = 0.0;
					final int inputLen = input[j].length();
					int k = subWordLen;
					for (; k < qWordLen && k < inputLen; k++) {
						if (toMatch[i].charAt(k) == input[j].charAt(k)) {

						} else {
							break;
						}
					}
					final double current = (double) k / qWordLen;
					if (current > max) {
						max = current;
					}
				}
			}

			match = match + max;
		}
		return (double) match / toMatch.length;
	}
	
	public double computeSimilarity(List<String> toMatch, List<String> input) {
		//double[] softMatch = new double[toMatch.length];
		double match = 0.0;
		for (int i = 0, sizei = toMatch.size();i < sizei; i++) {
			final String compare1 = toMatch.get(i);
			final int qWordLen = compare1.length();
			final String subWord = compare1.substring(0, (int) Math
					.floor(qWordLen / 2));
			final int subWordLen = subWord.length();
			double max = 0.0;
			//double current = 0.0;
			for (int j = 0, sizej = input.size(); j < sizej; j++) {
				final String compare2 = input.get(j);
				if (compare2.equals(compare1)
						|| compare2.startsWith(compare1)) {
					max = 1.0;
				} else if (compare2.startsWith(subWord)) {
					// max = 0.0;
					final int inputLen = compare2.length();
					int k = subWordLen;
					for (; k < qWordLen && k < inputLen; k++) {
						if (compare1.charAt(k) == compare2.charAt(k)) {

						} else {
							break;
						}
					}
					final double current = (double) k / qWordLen;
					if (current > max) {
						max = current;
					}
				}
			}

			match = match + max;
		}
		return (double) match / toMatch.size();
	}

	private static void testComputeQuerySimilarity() {
		String[] query = { "mobile", "develop", "application" };
		String[] input = { "applic", "mobi", "development" };
		double[] softMatch = new double[query.length];
		boolean[] hardMatch = new boolean[query.length];

		List<String> stemList = new ArrayList<String>();
		stemList.add("mobile");
		stemList.add("develop");
		stemList.add("apply");

		SemanticSearchUtil.getInstance().computeSimilarity(query, input,
				softMatch, hardMatch, null, stemList);
		System.out.println("****SoftMatch*****");
		for (int i = 0; i < softMatch.length; i++) {
			System.out.print(softMatch[i] + ", ");
		}
		System.out.println("****HardMatch*****");
		for (int i = 0; i < softMatch.length; i++) {
			System.out.print(hardMatch[i] + ", ");
		}

	}

	public double getNDCGSimilarity(double[] queryMatch, int[] input,
			boolean[] anyMatches, double idcg) {

		double dcg = input[0] >= 0 ? queryMatch[input[0]]
				: SemClassParamConstants.NDCG_IRREL_EPS;
		for (int i = 1; i < input.length; i++) {

			// If none of the entity matches the query word do not consider
			if (input[i] >= 0 && anyMatches[input[i]]) {
				double toConsider = SemClassParamConstants.NDCG_IRREL_EPS;
				if (input[i] >= 0) {
					toConsider = queryMatch[input[i]];
				}

				dcg = dcg + toConsider / Math.log(i + 1);
			}
		}
		return dcg / idcg;
	}

	public double getQueryIDCG(int queryLen) {
		double idcg = 1.0;
		for (int i = 1; i < queryLen; i++) {
			idcg = idcg + 1 / Math.log(i + 1);
		}
		return idcg;
	}

	public static void main(String[] args) {
		testComputeQuerySimilarity();
	}

	public WordFreqStore generateDFTFIDFVectors(
			TIntObjectHashMap<SemEntityBean> semEntityBeanMap,
			List<Integer> pageIdList,
			PorterStemmer stemmer) {

		//TObjectDoubleHashMap<String> topKWords = new TObjectDoubleHashMap<String>();
		int wordId = -1;
		TIntObjectHashMap<String> idToWordMap = new TIntObjectHashMap<String>();
		TObjectIntHashMap<String> wordToIdMap = new TObjectIntHashMap<String>();
		TIntObjectHashMap<List<Integer>> tf = new TIntObjectHashMap<List<Integer>>();
		// StopwordRecognizer stopwordRecognizer =
		// StopwordRecognizer.getInstance();
		//System.out.println("Size of sem entity bean map : " + semEntityBeanMap.size());
		
		//TIntObjectIterator<SemEntityBean> semItr = semEntityBeanMap.iterator();

		for(int i=0,size=pageIdList.size();i<size;i++) {
			final SemEntityBean semEntity = semEntityBeanMap.get(pageIdList.get(i));
			//computeTF(semEntity, stemmer);
			Map<String,EntityWordBean> entityTf = semEntity.getEntDetailBeanMap();
			
			final String entityTitle = semEntity.getTitle();
			
			//String[] keys = new String[entityTf.size()];
			final Set<String> entityWordSet = entityTf.keySet();
			final Iterator<String> wordItr = entityWordSet.iterator(); 
			
			// Add title to TF-IDF and DF
			if (wordToIdMap.containsKey(entityTitle)) {
				List<Integer> occurrence = tf.get(wordToIdMap.get(entityTitle));
				occurrence.add(1);
				semEntity.setTitleWordId(wordToIdMap.get(entityTitle));
				tf.put(wordToIdMap.get(entityTitle), occurrence);
			} else {
				wordId++;
				idToWordMap.put(wordId, entityTitle);
				wordToIdMap.put(entityTitle, wordId);
				final List<Integer> occurrence = new ArrayList<Integer>();
				occurrence.add(1);
				semEntity.setTitleWordId(wordId);
				tf.put(wordId, occurrence);
			}
				
			while(wordItr.hasNext()) {
				final String key = wordItr.next();
				if (wordToIdMap.containsKey(key)) {
					List<Integer> occurrence = tf.get(wordToIdMap.get(key));
					
					// If the word is the title then dont add it in a separate entry
					if(key.equals(entityTitle)) {
						final int dTf = occurrence.get(occurrence.size()-1).intValue() + entityTf.get(key).getTotalFreq();
						Integer obj = occurrence.remove(occurrence.size()-1);
						occurrence.add(dTf);
					} else {
					occurrence.add(entityTf.get(key).getTotalFreq());
					}
					entityTf.get(key).setWordId(wordToIdMap.get(key));
					tf.put(wordToIdMap.get(key), occurrence);

				} else {
					wordId++;
					idToWordMap.put(wordId, key);
					wordToIdMap.put(key, wordId);
					final List<Integer> occurrence = new ArrayList<Integer>();
					occurrence.add(entityTf.get(key).getTotalFreq());
					tf.put(wordId, occurrence);
				}
			}

		}

		// sort the result
		WordTFIDF[] tempTfIDF = new WordTFIDF[wordId + 1];
		WordDF[] tempDF = new WordDF[wordId + 1];
		int count = 0;
		for (int i = 0; i <= wordId; i++) {
			tempTfIDF[i] = new WordTFIDF(i);
			tempDF[i] = new WordDF(i);
			final List<Integer> occurence = tf.get(i);
			count = 0;
			
			//if(occurence != null) {
			for (int j = 0, size = occurence.size(); j < size; j++) {
				count = count + occurence.get(j);
			}
			
			tempTfIDF[i].tfidf = (double) count / Math.log(occurence.size()+1);
			tempDF[i].freq =   Math.log(occurence.size() +1);
			//}
		}
		Arrays.sort(tempTfIDF, new TFIDFComparator());
		Arrays.sort(tempDF, new DFComparator());
		
		
		double[] tfIDF = new double[wordId + 1]; 
		double[] DF = new double[wordId + 1];
		TObjectIntHashMap<String> topKTFIDF = new TObjectIntHashMap<String>();
		TObjectIntHashMap<String> topKDF = new TObjectIntHashMap<String>();
		
		for (int i = 0; i < tempTfIDF.length; i++) {
			tfIDF[tempTfIDF[i].wordId] = tempTfIDF[i].tfidf;
			topKTFIDF.put(idToWordMap.get(tempTfIDF[i].wordId), tempTfIDF[i].wordId);
		}
		
		for (int i = 0; i < tempDF.length; i++) {
			DF[tempDF[i].wordId] = tempDF[i].freq;
			topKDF.put(idToWordMap.get(tempDF[i].wordId), tempDF[i].wordId);
		}
		WordFreqStore wordFreqStore = new WordFreqStore(tfIDF,DF,topKTFIDF,topKDF);
		wordFreqStore.setWordToIdMap(wordToIdMap);
		return wordFreqStore;
	}

	// Initialize all common sem class members
	private void initFeatureData(SemEntityBean semEntityBean) {

		FeatureData featureData = new FeatureData();
		featureData
				.setSemClassesForCommon(new HashSet[IndexingConstants.NUM_OF_SEM_CLASSES]);
		featureData.getSemClassesForCommon()[IndexingConstants.INT_SECTIONS] = new HashSet<String>();
		featureData.getSemClassesForCommon()[IndexingConstants.INT_SEM_CLASS_ASSOCIATION_INLINK] = new HashSet<String>();
		featureData.getSemClassesForCommon()[IndexingConstants.INT_SEM_CLASS_ASSOCIATION_RELATED] = new HashSet<String>();
		featureData.getSemClassesForCommon()[IndexingConstants.INT_SEM_CLASS_ASSOCIATION_HIERARCHY] = new HashSet<String>();
		featureData.getSemClassesForCommon()[IndexingConstants.INT_SEM_CLASS_SYNOPSIS] = new HashSet<String>();
		featureData.getSemClassesForCommon()[IndexingConstants.INT_SEM_CLASS_FREQUENT] = new HashSet<String>();
		Set<String> frequentAsso = new HashSet<String>();
		featureData.setFrequentAsso(frequentAsso);
		featureData.setWordToIdMap(new TObjectIntHashMap<String>());
		semEntityBean.setFeatureData(featureData);
	}

	private void computeTF(SemEntityBean semEntityBean, PorterStemmer stemmer) {
		initFeatureData(semEntityBean);

		String[] semArray = semEntityBean.getQueryMatchedSemClass();
		setFrequency(semArray, semEntityBean, stemmer);

		semArray = semEntityBean.getUnmatchedSemClass();
		setFrequency(semArray, semEntityBean, stemmer);
	}

	private void setFrequency(String[] semArray, SemEntityBean semEntityBean,
			PorterStemmer stemmer) {
		FeatureData featureData = semEntityBean.getFeatureData();
		Set<String> frequentAsso = featureData.getFrequentAsso();
		TObjectIntHashMap<String> wordToIdMap = featureData.getWordToIdMap();

		for (int i = 0; i < semArray.length; i++) {
			if (semArray[i] != null) {
				final String splits[] = semArray[i].split(" \\| ");
				for (int j = 0; j < splits.length; j++) {
					final String rootWord = ExtractionUtil.getInstance(SemClassConstants.SYNONYM_FILE)
							.getRootForm(splits[i].trim(), stemmer);

					switch (i) {
					case IndexingConstants.INT_SECTIONS:
						featureData.getSemClassesForCommon()[IndexingConstants.INT_SECTIONS]
								.add(rootWord);
						break;
					case IndexingConstants.INT_SEM_CLASS_ASSOCIATION_INLINK:
						if (!featureData.getSemClassesForCommon()[IndexingConstants.INT_SEM_CLASS_ASSOCIATION_INLINK]
								.add(rootWord)) {
							frequentAsso.add(rootWord);
						}
						if (featureData.getSemClassesForCommon()[IndexingConstants.INT_SEM_CLASS_ASSOCIATION_RELATED]
								.contains(rootWord)) {
							frequentAsso.add(rootWord);
						}
						break;
					case IndexingConstants.INT_SEM_CLASS_ASSOCIATION_RELATED:
						featureData.getSemClassesForCommon()[IndexingConstants.INT_SEM_CLASS_ASSOCIATION_RELATED]
								.add(rootWord);
						if (featureData.getSemClassesForCommon()[IndexingConstants.INT_SEM_CLASS_ASSOCIATION_INLINK]
								.contains(rootWord)) {
							frequentAsso.add(rootWord);
						}
						break;
					case IndexingConstants.INT_SEM_CLASS_SYNOPSIS:
						featureData.getSemClassesForCommon()[IndexingConstants.INT_SEM_CLASS_SYNOPSIS]
								.add(rootWord);
						break;
					case IndexingConstants.INT_SEM_CLASS_FREQUENT:
						featureData.getSemClassesForCommon()[IndexingConstants.INT_SEM_CLASS_FREQUENT]
								.add(rootWord);
						break;
					default:
						break;
					}

					// if(!stopwordRecognizer.isStopWord(rootWord)) {
					if (wordToIdMap.containsKey(rootWord)) {
						int count = wordToIdMap.get(rootWord);
						wordToIdMap.put(rootWord, count++);
					} else {
						wordToIdMap.put(rootWord, 1);
					}
					// }
				}

			}
		}

	}

	// TODO Issue : Bias and also not sorted in lucene
	public TObjectDoubleHashMap<String> globalTFIDF() {
		TObjectDoubleHashMap<String> result = new TObjectDoubleHashMap<String>();
		try {
			Directory dir = FSDirectory.open(new File(
					SemClassConstants.WIKI_PATH));
			IndexReader ir = IndexReader.open(dir);
			TermEnum te = ir.terms();
			while (te.next()) {
				final int idf = te.docFreq();
				final TermDocs td = ir.termDocs(te.term());
				int tf = 0;
				while (td.next()) {
					tf = tf + td.freq();
				}
				result.put(te.term().text(), tf * idf);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public Number[][] getTitleMatch(SemEntityBean semEntity1,
			SemEntityBean semEntity2, PorterStemmer stemmer, int[] semClasses) {

		String rootFormLeft = semEntity1.getTitle();
		String rootFormRight = semEntity2.getTitle();

		//int hashLeft = rootFormLeft.hashCode();
		//int hashRight = rootFormRight.hashCode();
		Number[][] toReturn = new Number[2][semClasses.length];

		TIntObjectHashMap<List<String>> semWordMapLeft = semEntity1
				.getWordSemClassMap();
		TIntObjectHashMap<List<String>> semWordMapRight = semEntity2
				.getWordSemClassMap();

		for (int i = 0; i < semClasses.length; i++) {
			//System.out.println("Sem class : " + semClasses[i]);
			
			final List<String> wordHashListL = semWordMapLeft
					.get(semClasses[i]);
			final List<String> wordHashListR = semWordMapRight
					.get(semClasses[i]);
			if(wordHashListL != null && wordHashListR != null) {
			if (wordHashListR.contains(rootFormLeft)) {
				if (wordHashListL.contains(rootFormRight)) {
					toReturn[0][i] = SemClassConstants.ENT_SET_TYPE_PAIRWISE_MATCH_BOTH;
					toReturn[1][i] = 2.0;
				} else {
					toReturn[0][i] = SemClassConstants.ENT_SET_TYPE_PAIRWISE_MATCH_ONE_RIGHT;
					toReturn[1][i] = 1.0;
				}
			} else if (wordHashListL.contains(rootFormRight)) {
				if (wordHashListR.contains(rootFormLeft)) {
					toReturn[0][i] = SemClassConstants.ENT_SET_TYPE_PAIRWISE_MATCH_BOTH;
					toReturn[1][i] = 2.0;
				} else {
					toReturn[0][i] = SemClassConstants.ENT_SET_TYPE_PAIRWISE_MATCH_ONE_LEFT;
					toReturn[1][i] = 1.0;
				}
			} else {
				toReturn[0][i] = SemClassConstants.ENT_SET_TYPE_PAIRWISE_MATCH_NO_EVIDENCE;
				toReturn[1][i] = 0.0;
			}

			// Try partial matches for freq, synopsis and TODO : co-occurrence
			if ((semClasses[i] == IndexingConstants.INT_SEM_CLASS_FREQUENT 
					|| semClasses[i] == IndexingConstants.INT_SEM_CLASS_SYNOPSIS)
					&& toReturn[0][i].intValue() > 1) {
				int semClassIndex = -1;

				switch (semClasses[i]) {
				case IndexingConstants.INT_SEM_CLASS_SYNOPSIS:
					semClassIndex = 0;
					break;
				case IndexingConstants.INT_SEM_CLASS_FREQUENT:
					semClassIndex = 1;
					break;
				}

				String[] semMemberLeft = null;
				String[] semMemberRight = null;

				switch (toReturn[0][i].intValue()) {
				case SemClassConstants.ENT_SET_TYPE_PAIRWISE_MATCH_NO_EVIDENCE:
					semMemberLeft = new String[wordHashListL.size()];
					semMemberLeft = wordHashListL.toArray(semMemberLeft);
					
					semMemberRight = new String[wordHashListR.size()];
					semMemberRight = wordHashListR.toArray(semMemberRight);
					doPartialMatch(semMemberLeft, semMemberRight, rootFormLeft,
							rootFormRight, toReturn, semClassIndex);
					break;
				case SemClassConstants.ENT_SET_TYPE_PAIRWISE_MATCH_ONE_LEFT:
					semMemberLeft = new String[wordHashListL.size()];
					semMemberLeft = wordHashListL.toArray(semMemberLeft);
					doPartialMatch(semMemberLeft, rootFormRight, toReturn,
							semClassIndex);
					break;
				case SemClassConstants.ENT_SET_TYPE_PAIRWISE_MATCH_ONE_RIGHT:
					semMemberRight = new String[wordHashListR.size()];
					semMemberRight = wordHashListR.toArray(semMemberRight);
					doPartialMatch(semMemberRight, rootFormRight, toReturn,
							semClassIndex);
					break;
				}

			}
		} else {
			//System.out.println("Missing sem class " + semClasses[i] +" for " + semEntity1.getTitle() +" , "+ semEntity2.getTitle());
		}
		}
		return toReturn;
	}

	private void doPartialMatch(String[] semMember, String input,
			Number[][] toReturn, int semClass) {
		String[] splits = input.split(" ");
		double sim = computeSimilarity(splits, semMember);
		if (sim > 0.5) {
			toReturn[0][semClass] = SemClassConstants.ENT_SET_TYPE_PAIRWISE_MATCH_BOTH;
			toReturn[1][semClass] = 2.0;
		}
	}

	private void doPartialMatch(String[] semMember1, String[] semMember2,
			String input1, String input2, Number[][] toReturn, int semClass) {
		String[] splits = input1.split(" ");
		double sim1 = computeSimilarity(splits, semMember2);

		splits = input2.split(" ");
		double sim2 = computeSimilarity(splits, semMember1);

		if (sim1 > 0.5) {
			if (sim2 > 0.5) {
				toReturn[0][semClass] = SemClassConstants.ENT_SET_TYPE_PAIRWISE_MATCH_BOTH;
				toReturn[1][semClass] = 2.0;
			} else {
				toReturn[0][semClass] = SemClassConstants.ENT_SET_TYPE_PAIRWISE_MATCH_ONE_LEFT;
				toReturn[1][semClass] = 1.0;
			}
		} else {
			if (sim1 > 0.5) {
				toReturn[0][semClass] = SemClassConstants.ENT_SET_TYPE_PAIRWISE_MATCH_BOTH;
				toReturn[1][semClass] = 2.0;
			} else {
				toReturn[0][semClass] = SemClassConstants.ENT_SET_TYPE_PAIRWISE_MATCH_ONE_RIGHT;
				toReturn[1][semClass] = 1.0;
			}
		}

	}
	
	public double getQueryMatch(SemEntityBean semEntityBean,double[] queryVector) {
		double[] allSemQueryMatch = semEntityBean.getAllSemClsQueryMatch();
		double[] entityQueryPOSVector = new double[allSemQueryMatch.length];
		for(int i=0;i<allSemQueryMatch.length;i++) {
			entityQueryPOSVector[i] = allSemQueryMatch[i] * queryVector[i];
		}
		return getCosSim(queryVector, entityQueryPOSVector);
	}
	
	private double getCosSim(double[] xi, double[] xj) {
		double kernel = 0.0;
		for (int i = 0; i < xi.length; i++) {
			kernel = kernel + (xi[i] * xj[i]);
		}
		return kernel / (getMagnitude(xi) * getMagnitude(xj));
	}

	private double getMagnitude(double[] xi) {
		double kernel = 0.0;
		for (int i = 0; i < xi.length; i++) {
			kernel = kernel + (xi[i] * xi[i]);
		}
		return Math.sqrt(kernel);

	}
	public double[] getQueryPOSVector(QueryDetailBean queryDtlBean) {
		double maxNN = queryDtlBean.getNNSize();
		maxNN = maxNN>1 ? maxNN : maxNN+SemClassParamConstants.QUERY_MATCH_NN_EPS;
		int count = 0;
		
		Collection<String> posMap = queryDtlBean.getPostags().values();
		double[] queryVector = new double[posMap.size()];
		
		Iterator<String> itr = posMap.iterator();
		
		while(itr.hasNext()) {
			String key = itr.next();
			if(key.startsWith(SemClassConstants.POS_NN)) {
				queryVector[count++] = 1;
			} else if(key.startsWith(SemClassConstants.POS_ADJ)) {
				queryVector[count++] = (double) 1/maxNN;
			} else if(key.startsWith(SemClassConstants.POS_VB)) {
				queryVector[count++] = (double) 1/maxNN;
			} else if(key.startsWith(SemClassConstants.POS_ADV)) {
				queryVector[count++] = (double) 1/maxNN * SemClassParamConstants.QUERY_MATCH_VB_EPS;
			} else {
				queryVector[count++] = 0;
			}
		}
		return queryVector;
	}

	public void commonSemMembers(Map<String,SemInterpretation> overlapSemMemberMap,
			Map<String, String> interPairSemMems, int[] semClassLabels,
			EntityPair entityPair, int overlapType) {

		int commCount = 0;
		
		TIntObjectHashMap<List<String>> wordSemMapL = entityPair.getSemEntityL().getWordSemClassMap();
		TIntObjectHashMap<List<String>> wordSemMapR = entityPair.getSemEntityR().getWordSemClassMap();
		
		String entityPairIds = "//" +entityPair.getSemEntityL().getPageId()+"/"+entityPair.getSemEntityR().getPageId()+"//";
		
		for (int i = 0; i < semClassLabels.length; i++) {
			final List<String> semClassMemberListL = wordSemMapL.get(semClassLabels[i]);
			final List<String> semClassMemberListR = wordSemMapR.get(semClassLabels[i]);
			int count = 0;
			if(semClassMemberListL != null && semClassMemberListR != null) {
			for(int j=0,size=semClassMemberListR.size();j<size;j++) {
				final String key = semClassMemberListR.get(j);
				if (semClassMemberListL.contains(key)) {
//					if (!common.contains(key))
//						common.add(key);
					addOrUpdate(overlapSemMemberMap, interPairSemMems, key, 
							entityPairIds, semClassLabels[i]+":"+semClassLabels[i]+"|");
					count++;
				}
			}
			} else {
//				System.out.println("Missing sem class " + semClassLabels[i] +" for " + entityPair.getSemEntityL().getTitle() 
//						+ " , "+ entityPair.getSemEntityR().getTitle());
			}
			commCount = commCount + count;
		}
		if(commCount > 0) {
			entityPair.setOverlapExist(true);
		entityPair.getOverlapCount()[overlapType] = commCount;
		}
	}
	
	public void commonSemMembers(Map<String,SemInterpretation> overlapSemMemberMap,
			Map<String, String> interPairSemMems, int semClassLabel,
			EntityPair entityPair, int overlapType) {

		
		TIntObjectHashMap<List<String>> wordSemMapL = entityPair.getSemEntityL().getWordSemClassMap();
		TIntObjectHashMap<List<String>> wordSemMapR = entityPair.getSemEntityR().getWordSemClassMap();
		
		String entityPairIds = "//" +entityPair.getSemEntityL().getPageId()+"/"+entityPair.getSemEntityR().getPageId()+"//";
		
			final List<String> semClassMemberListL = wordSemMapL.get(semClassLabel);
			final List<String> semClassMemberListR = wordSemMapR.get(semClassLabel);
			int count = 0;
			if(semClassMemberListL != null && semClassMemberListR != null) {
			for(int j=0,size=semClassMemberListR.size();j<size;j++) {
				final String key = semClassMemberListR.get(j);
				if (semClassMemberListL.contains(key)) {
//					if (!common.contains(key))
//						common.add(key);
					addOrUpdate(overlapSemMemberMap, interPairSemMems, key, 
							entityPairIds, semClassLabel+":"+semClassLabel+"|");
					count++;
				}
			}
			} else {
//				System.out.println("Missing sem class " + semClassLabel +" for " + entityPair.getSemEntityL().getTitle() 
//						+ " , "+ entityPair.getSemEntityR().getTitle());
			}
		if(count > 0) {
		entityPair.setOverlapExist(true);
		entityPair.getOverlapCount()[overlapType] = count;
		}
	}
	
	private void addOrUpdate(Map<String,SemInterpretation> overlapSemMemberMap,
			Map<String, String> interPairSemMems,
			String semMember, String entityPair, String semPair) {
		String appendMember = null;
		if(overlapSemMemberMap.containsKey(semMember)) {
			appendMember = overlapSemMemberMap.get(semMember).getOverlapSemMembersDtls();
			if(appendMember.indexOf(entityPair) >= 0) {
				appendMember = appendMember + semPair;
			} else {
				final String tempEntityPair = entityPair.replaceAll("//", "");
				String[] splits = tempEntityPair.split("/");
				//System.out.println("Split size" + splits.length +" "+ splits[0]);
				String interPairIds = interPairSemMems.get(semMember);
				if(interPairIds.indexOf(splits[0]) < 0) {
					interPairIds = interPairIds+"/"+splits[0];
				}
				if(interPairIds.indexOf(splits[1]) < 0) {
					interPairIds = interPairIds+"/"+splits[1];
				}
				interPairSemMems.put(semMember, interPairIds);
				appendMember = appendMember + entityPair + semPair;
			}
			
		} else {
			appendMember = entityPair + semPair +"|";
			interPairSemMems.put(semMember, entityPair.replaceAll("//", ""));
			SemInterpretation semInterpret = new SemInterpretation();
			semInterpret.setInterpretation(semMember);
			overlapSemMemberMap.put(semMember, semInterpret);
		}
		overlapSemMemberMap.get(semMember).setOverlapSemMembersDtls(appendMember);
	}
	
	public String getSemClassName(int semClass) {
		switch (semClass) {
		case IndexingConstants.INT_SECTIONS:
			return IndexingConstants.SECTION_HEADING;
		case IndexingConstants.INT_SEM_CLASS_ASSOCIATION_HIERARCHY:
			return IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED_HIERARCHY;
		case IndexingConstants.INT_SEM_CLASS_ASSOCIATION_INLINK:
			return IndexingConstants.SEM_CLASS_ASSOCIATION_INLINK;
		case IndexingConstants.INT_SEM_CLASS_ASSOCIATION_RELATED:
			return IndexingConstants.SEM_CLASS_ASSOCIATION_RELATED;
		case IndexingConstants.INT_SEM_CLASS_FREQUENT:
			return IndexingConstants.SEM_CLASS_FREQUENT;
		case IndexingConstants.INT_SEM_CLASS_HOMONYM:
			return IndexingConstants.SEM_CLASS_HOMONYM;
		case IndexingConstants.INT_SEM_CLASS_HYPERNYM:
			return IndexingConstants.SEM_CLASS_HYPERNYM;
		case IndexingConstants.INT_SEM_CLASS_HYPONYM:
			return IndexingConstants.SEM_CLASS_HYPONYM;
		case IndexingConstants.INT_SEM_CLASS_MAKEPRODUCE:
			return IndexingConstants.SEM_CLASS_MAKEPRODUCE;
		case IndexingConstants.INT_SEM_CLASS_MERONYM:
			return IndexingConstants.SEM_CLASS_MERONYM;
		case IndexingConstants.INT_SEM_CLASS_REFERENCE:
			return IndexingConstants.SEM_CLASS_REFERENCE;
		case IndexingConstants.INT_SEM_CLASS_PRODUCT:
			return IndexingConstants.SEM_CLASS_PRODUCT;
		case IndexingConstants.INT_SEM_CLASS_ROLE:
			return IndexingConstants.SEM_CLASS_ROLE;
		case IndexingConstants.INT_SEM_CLASS_SYNONYM:
			return IndexingConstants.SEM_CLASS_SYNONYM;
		case IndexingConstants.INT_SEM_CLASS_SIBLING:
			return IndexingConstants.SEM_CLASS_SIBLING;
		case IndexingConstants.INT_SEM_CLASS_SYNONYM_WORDNET:
			return IndexingConstants.SEM_CLASS_SYNONYM_WORDNET;
		case IndexingConstants.INT_SEM_CLASS_SYNOPSIS:
			return IndexingConstants.SEM_CLASS_SYNOPSIS;

		default:
			return "";
		}
	}
	
	public void commonSemMembersDelete(SemEntityBean semEntityL,
			SemEntityBean semEntityR, int semClassLabel,
			List<String> common, EntityPair entityPair) {

		TIntObjectHashMap<List<String>> wordSemMapL = semEntityL.getWordSemClassMap();
		TIntObjectHashMap<List<String>> wordSemMapR = semEntityR.getWordSemClassMap();
			final List<String> semClassMemberListL = wordSemMapL.get(semClassLabel);
			final List<String> semClassMembersListR = wordSemMapR.get(semClassLabel);
//			int jaccards = commonSemMembers(semClassMemberListL, semClassMembersListR,
//					common);
		
	}

	public Integer commonSemMembersDelete(List<String> semClassMemberListL,
			List<String> semClassMembersListR) {
		
		int count = 0;
		for(int i=0,size=semClassMembersListR.size();i<size;i++) {
			final String key = semClassMembersListR.get(i);
			if (semClassMemberListL.contains(key)) {
//				if (!common.contains(key))
//					common.add(key);
				count++;
			}
		}
		return count;
	}
}

class WordTFIDF {
	int wordId;
	double tfidf;

	public WordTFIDF(int wordId) {
		this.wordId = wordId;
	}

}

class WordDF {
	int wordId;
	double freq;

	public WordDF(int wordId) {
		this.wordId = wordId;
	}

}

class DFComparator implements Comparator {
	public int compare(Object obj1, Object obj2) {

		WordDF wordTfidf1 = ((WordDF) obj1);

		WordDF wordTfidf2 = ((WordDF) obj2);

		if (wordTfidf1.freq < wordTfidf2.freq)
			

			return 1;

		else if (wordTfidf1.freq > wordTfidf2.freq)

			return -1;

		else

			return 0;

	}
}


class TFIDFComparator implements Comparator {
	public int compare(Object obj1, Object obj2) {

		WordTFIDF wordTfidf1 = ((WordTFIDF) obj1);

		WordTFIDF wordTfidf2 = ((WordTFIDF) obj2);

		if (wordTfidf1.tfidf < wordTfidf2.tfidf)

			return 1;

		else if (wordTfidf1.tfidf > wordTfidf2.tfidf)

			return -1;

		else

			return 0;

	}
}
