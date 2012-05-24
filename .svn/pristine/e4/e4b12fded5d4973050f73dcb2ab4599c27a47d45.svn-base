package com.aneedo.util;

import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;

public class WordnetUtils {

	private static WordnetUtils util= null;
	private IDictionary dictionary;
	public WordnetUtils(String path){
		try {
			this.dictionary = new Dictionary(new URL("file", null,path+"/wordnet/dict"));
			//DataIntegrationConstants.WORDNET_LOCATION));
			dictionary.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static WordnetUtils getInstance(String path){
		if(util == null)
			util= new WordnetUtils(path);
		return util;
	}

	public void populateMeronyms(ISynset synset, Set<String> resultSet){
		List < ISynsetID > meronyms = synset.getRelatedSynsets (Pointer . MERONYM_PART) ;
		if(meronyms.size()>0){
			List <IWord > words ;
			Iterator<ISynsetID> meroIter = meronyms.iterator();
			while(meroIter.hasNext()){
				ISynsetID sid= meroIter.next();
				
				words = dictionary . getSynset ( sid ) . getWords () ;
				for ( Iterator < IWord > i = words . iterator () ; i . hasNext () ;) {
					String lemma=i.next().getLemma();
					//builder.append( lemma.replaceAll("_", " ")+ " | ");
					resultSet.add(lemma.replaceAll("_", " "));
				}

			}
		}
	}


	public Set<String> getMeronyms (String word) {
		// get the synset
		word=word.replaceAll(" ", "_");
		IIndexWord idxWord = dictionary.getIndexWord(word,POS.NOUN) ;
		if(idxWord ==  null) return null;
		List<IWordID> iWordIDList=idxWord.getWordIDs();
		Set<String> resultSet= new HashSet<String>();
		for(int j=0;j<iWordIDList.size();j++){
			IWordID wordID = idxWord .getWordIDs().get (j) ; // 1 st meaning
			IWord iword = dictionary . getWord ( wordID ) ;
			ISynset synset = iword.getSynset();
			// get the meronyms
			List < ISynsetID > meronyms = synset.getRelatedSynsets (Pointer . MERONYM_PART) ;

			
			// If meronyms are not present for a synset, go to its hypernyms and collect meronyms from them
			if(meronyms.size()<=0){
				List<ISynsetID> hyper = synset.getRelatedSynsets(Pointer . HYPERNYM);
				Iterator<ISynsetID> hyperIter = hyper.iterator();
				while(hyperIter.hasNext())
				{
					populateMeronyms(dictionary.getSynset(hyperIter.next()), resultSet);
				}

			}

			else{
				populateMeronyms(synset, resultSet);

			}
		}
		return resultSet;
	}

	public static void main(String[] args) {
		Set<String> resultSet=new WordnetUtils("/data2/indexing/semantic/").getMeronyms("laptop");

		Iterator<String> itrResult = resultSet.iterator();

		while(itrResult.hasNext()) {
			System.out.println(itrResult.next());
		}

	}

}
