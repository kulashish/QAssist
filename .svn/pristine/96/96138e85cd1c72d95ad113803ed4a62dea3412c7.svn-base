package com.aneedo.search.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Iterator;

public class SentimentRepos {
	
	public static SentimentRepos repos= null;
	
	//private final static String SENTI_WORDNET_PATH = "/home/kedar/kedhar_workspace/SentiWordnet/Sentiwordnet.txt";
	private HashSet<String> posWords = new HashSet<String>();
	private HashSet<String> negWords = new HashSet<String>();
	private HashSet<String> objWords = new HashSet<String>();
	
	public static SentimentRepos getInstance(String path){
		if(repos == null) {
			repos = new SentimentRepos(path);
		}
		return repos;
	}
	
	private SentimentRepos(String path) {
		populateRepos(path);
	}
	
	public void populateRepos(String path){
		//System.out.println("POPULATING SWN");
		LovinsStemmer ls = new LovinsStemmer();
		try{
		BufferedReader in = new BufferedReader(new FileReader(path+"/SentiWordnet/Sentiwordnet.txt"));
		String line="";
		HashSet<String> present;
		while ((line = in.readLine()) != null) {
			String[] params= line.replaceAll("\t"," ").split(" ");
			// Obtaining the current map
			if(Float.parseFloat(params[2])-Float.parseFloat(params[3])>0.3)
				present=objWords;
			else if(Float.parseFloat(params[2])>0.3)
				present=posWords;
			else if(Float.parseFloat(params[3])>0.3)
				present=negWords;
			else present=objWords;
			
			// Getting the words to be added to respective map
			
			for(int i=4;i<params.length;i++){
				present.add(ls.stem(params[i].split("#")[0]));
			}
			
		}
		}
		
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		//System.out.println("Pos words");
		Iterator<String> iter=negWords.iterator();
//		while(iter.hasNext())
//			System.out.println(iter.next());
//		
//		System.out.println("-----------------neg words---------------------");
//		iter=negWords.iterator();
//		while(iter.hasNext())
//			System.out.println(iter.next());
//		
//		System.out.println("-----------------Obj words---------------------");
//		iter=negWords.iterator();
//		while(iter.hasNext())
//			System.out.println(iter.next());
		
	}
	
	public int getSentiment(String title){
		int i=0;
		LovinsStemmer ls = new LovinsStemmer();
		String[] titleWords= title.split(" ");
		for(String word:titleWords){
			String stem = ls.stem(word);
			if(negWords.contains(stem)){
				//System.out.println("word:"+ stem);
				return -1;
			}
				
			else if(posWords.contains(stem)){
				i=1;
			}
			
		}
		
		return i;
	}
	
	public static void main(String[] args) {
		LovinsStemmer ls= new LovinsStemmer();
		SentimentRepos sr= SentimentRepos.getInstance("/home/kedar/kedhar_workspace/SentiWordnet/Sentiwordnet.txt");
		System.out.println(ls.stem("wanton"));
		System.out.println(sr.getSentiment("I want a bicycle"));
	}
	

}
