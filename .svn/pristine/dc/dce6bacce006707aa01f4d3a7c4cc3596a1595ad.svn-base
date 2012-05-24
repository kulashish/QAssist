package com.aneedo.search.util;

import java.util.ArrayList;

import de.tudarmstadt.ukp.wikipedia.api.Page;

public class PurposeSearcher {

	public static ArrayList<String> findTelic(String text, Page page){
		String[] sentences= text.toLowerCase().split(".");
		String pageTitle="";
		try{
			pageTitle= page.getTitle().getPlainTitle().toLowerCase().replaceAll("_", " ");
		}
		catch(Exception e){

		}
		String s="";
		ArrayList<String> purposeList= new ArrayList<String>();
		for(String sent:sentences){
			if(sent.indexOf(pageTitle)>0){
				boolean b=false;
				if(sent.indexOf("used for")>0 ) s="used for";
				else if(sent.indexOf("intended to")>0) s="intended to";
				else if(sent.indexOf("suitable for")>0) s="suitable for";
				else if(sent.indexOf("made for")>0) s="made for";
				else b= true;
				if(b==false) continue;
				String[] tags= null;//TagText.getInstance().getTagsArray(sent);
				String[] words=sent.split(" ");
				String ss[] = sent.split("s");
				int start=ss[0].split(" ").length+1;
				// starting to check the presence of noun and adj in the second half of the split 
				if(ss[0].indexOf(pageTitle)>=-0){
					StringBuilder purpose=new StringBuilder();
					boolean gotNoun=false;
					for(int i=start;i<tags.length;i++){
						if(tags[i].toLowerCase().equals("jj")){
							purpose.append(words[i]+" ");
						}
						else if(tags[i].toLowerCase().equals("nn")||tags[i].toLowerCase().equals("nns")){
							purpose.append(words[i]+" ");
							gotNoun=true;
						}
						else if(gotNoun==true) break;
					}
					if(!purpose.equals("")) purposeList.add(purpose.toString()); 

				}
			}
		}

		if(purposeList.size()>0) return purposeList;
		else return null;
	}

}
