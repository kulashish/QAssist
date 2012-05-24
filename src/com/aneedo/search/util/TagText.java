package com.aneedo.search.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;


public class TagText{

	private static TagText tagText = null;
	public MaxentTagger tagger;

	public static TagText getInstance(String path){
		if(tagText == null) {
			tagText = new TagText(path);
		}
		return tagText;
	}

	private TagText(String path){
		try{
			this.tagger= new MaxentTagger(path+"/stanford-models/english-left3words-distsim.tagger");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public String getReference(String sen){

		String tagged = null;
		synchronized (this) {
			tagged = tagger.tagString(sen);
		}

		String[] splits= tagged.replace(" ","/").split("/");
		String[] tags = new String[splits.length/2];
		String[] words = new String[splits.length/2];
	//	System.out.println(splits.length);
		for(int i=0,j=0;i<splits.length;i=i+2,j++){
			words[j]=splits[i];
			tags[j]=splits[i+1]+j;
		}
		StringBuilder posTagSent=new StringBuilder();
		for(String tag:tags)
			posTagSent.append(tag+" ");
				//System.out.println("pdjfdf:"+posTagSent);
				//only JJ * <Pagetitle> (VB | JJ)? NN+ words
				Pattern p= Pattern.compile("(JJ[0-9] )+(NN[0-9]+ )+((VB|JJ|VBD)[0-9]+ )*(NN[0-9])");
				Matcher m= p.matcher(posTagSent.toString().trim());
				int start=0,end=0;

				if(m.find()){
					start=m.start();
					end=m.end();
				}

				String match= posTagSent.substring(start, end);
				//System.out.println(match);
				String[] indices=match.replaceAll(" ","").split("[a-zA-Z]");
				int startIndex=-1,endIndex=-1;
				for(String s:indices){
					if(!s.equals("")&&startIndex==-1)startIndex=Integer.parseInt(s);
					if(!s.equals(""))endIndex=Integer.parseInt(s);
				}
				StringBuilder sb=new StringBuilder("");
				if(startIndex!=-1)
					for(int k=startIndex;k<=endIndex;k++) sb.append(words[k]+" ");
				//System.out.println(sb.toString().trim());
				return sb.toString().trim();

	}

	public String[] getTagsArray(String sen){

		String tagged = null;
		synchronized (this) {
			tagged = tagger.tagString(sen);
		}

		String[] splits= tagged.replace(" ","/").split("/");
		String[] tags = new String[splits.length/2];
		for(int i=0,j=0;i<splits.length;i=i+2,j++)
			tags[j]=splits[i+1]+j;
		return tags;
	}

	public String tagSentence(String sen){
		String tagged = null;
		synchronized (this) {
			tagged = tagger.tagString(sen);
		}
//		String[] splits= tagged.replace(" ","/").split("/");
//		String[] tags = new String[splits.length/2];
//		String[] words = new String[splits.length/2];
//		//System.out.println(splits.length);
//		for(int i=0,j=0;i<splits.length;j++){
//			words[j]=splits[i++];
//			tags[j]=splits[i++]+j;
//		}
//		StringBuilder posTagSent=new StringBuilder();
//		for(String tag:tags)
//			posTagSent.append(tag+" ");
//				//System.out.println(posTagSent);
//				//only JJ * <Pagetitle> (VB | JJ)? NN+ words
//				Pattern p= Pattern.compile("JJ");//*(NN[0-9]+ )");//+((VB|JJ|VBD)[0-9]+ )*(NN[0-9])");
//				Matcher m= p.matcher(posTagSent);
//				//System.out.println(m.matches());
//				if(m.matches()){
//					//System.out.println(m.start());
//					//System.out.println(m.end());
//				}
				return tagged;
	}

	//public static void main(String[] args) {
	//System.out.println("here:"+TagText.getInstance().getReference("Cameras are used for shooting and taking photographs"));//An intelligent man suggested movie"));
	//}
	public static void main(String[] args) {
		
		//System.out.println("here:"+TagText.getInstance().getReference("An intelligent man collected movie"));
		//TagText.getInstance().findReference("reynolds pencil", new String[]{"reynolds ball point pen","i have a nice reynolds blue pen"});
		System.out.println(TagText.getInstance("/data2/indexing/semantic").tagSentence("Augusta Ada King, Countess of Lovelace (10 December 1815 – 27 November 1852), " +
				"born Augusta Ada Byron, was an English writer chiefly known for her work on Charles Babbage's early " +
				"mechanical general-purpose computer, the analytical engine."));

	}

	public void findReference(String title, String[] links){

		String titleWords[] = title.split(" ");
		for(String link: links){
			String tagged;
			if(link.replaceAll("_"," ").indexOf(title)!=-1){
				//System.out.println(link.replaceAll("_"," ").indexOf(title));
				synchronized(this){
					tagged=this.tagger.tagString(link);
				}

			//	System.out.println("Tagged:"+tagged);
				String[] splits= tagged.replace(" ","/").split("/");
				String[] tags = new String[splits.length/2];
				String[] words = new String[splits.length/2];
				for(int i=0,j=0;i<splits.length;i=i+2,j++){
					words[j]=splits[i];
					tags[j]=splits[i+1];
				}

				int spaces=0;
				StringBuilder interpretation=new StringBuilder();
				for(int i=0,start=link.replaceAll("_"," ").indexOf(title);i<start;i++){
					if(link.charAt(i)==' ') spaces++;
				}
				for(int i=spaces-1;i>0;i--)
				{
					if(tags[i].equals("JJ")){
						interpretation.append(tags[i]+" ");
					}
				}
				interpretation.append(title+" ");
				for(int i=spaces+titleWords.length;i<tags.length;i++)
				{
					if(tags[i].equals("NN")||tags[i].equals("NNS")||tags[i].equals("JJ")){
						interpretation.append(words[i]+" ");

					}
					else break;
				}
			//	System.out.println("part-1");
				//System.out.println("Interpretation:"+interpretation);

			}

			else{
				boolean isTitleWordinLink = false;
				String titleWord="";
				String stopWords = "of by in on based from about";
				for(String word: titleWords){
					if(stopWords.indexOf(word)!=-1) continue;
					if(link.indexOf(word)!=-1){
						isTitleWordinLink = true;
						titleWord=word;
						break;
					}
				}
				if(isTitleWordinLink){
					synchronized(this){
						tagged=this.tagger.tagString(link);
					}

					//System.out.println("Tagged:"+tagged);
					String[] splits= tagged.replace(" ","/").split("/");
					String[] tags = new String[splits.length/2];
					String[] words = new String[splits.length/2];
					for(int i=0,j=0;i<splits.length;i=i+2,j++){
						words[j]=splits[i];
						tags[j]=splits[i+1];
					}

					int spaces=0;
					StringBuilder interpretation=new StringBuilder();
					for(int i=0,start=link.replaceAll("_"," ").indexOf(titleWord);i<start;i++){
						if(link.charAt(i)==' ') spaces++;
					}
					for(int i=spaces-1;i>0;i--)
					{
						if(tags[i].equals("JJ")){
							interpretation.append(tags[i]+" ");
						}
					}
				
					interpretation.append(titleWord+" ");
					for(int i=spaces+1;i<tags.length;i++)
					{
						if(title.indexOf(words[i])!=-1) {
							interpretation.append(words[i]+" ");
							continue;
						}
						if(tags[i].equals("NN")||tags[i].equals("NNS")||tags[i].equals("JJ")){
							interpretation.append(words[i]+" ");

						}
						else break;
					}
					
					//System.out.println("Interpretation:"+interpretation);

				}
				
			}

		}
	}

}
