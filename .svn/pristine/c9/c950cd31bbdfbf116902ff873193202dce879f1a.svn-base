package com.aneedo.jwplext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JwplUtil {
	private static	final    Pattern pattern = Pattern.compile(Pattern.quote("articles_")
			+"|"+Pattern.quote("_article")
			+"|"+Pattern.quote("article_")
			+"|"+Pattern.quote(" by ")
			+"|"+Pattern.quote(" in ")
			+"|"+Pattern.quote(" on ")
			+"|"+Pattern.quote(" based ")
			+"|"+Pattern.quote(" from ")
			+"|"+Pattern.quote(" about ")
			+"|"+Pattern.quote(" history ")
			+"|"+Pattern.quote(" wikipedia ")
			+"|"+Pattern.quote(":")
			+"|"+Pattern.quote(" people "), Pattern.CASE_INSENSITIVE);
	
	private  static	final    Pattern numpattern = Pattern.compile("[0-9]"); 
	
	public static String getText(String text) {
		 // Normalize strings read from the DB to use "\n" for all line breaks.
       StringBuilder sb = new StringBuilder(text);
       int t = 0;
       boolean seenLineBreak = false;
       char breakQue = ' ';
       for (int s = 0; s < sb.length(); s++) {
               char c = sb.charAt(s);
               boolean isLineBreak = c == '\n' || c == '\r';
               if (isLineBreak) {
                       if (seenLineBreak && !(c == breakQue)) {
                               // This is a Windows or Mac line ending. Ignoring the second char
                               seenLineBreak = false;
                               continue;
                       }
                       else {
                               // Linebreak character that we cannot ignore
                               seenLineBreak = true;
                               breakQue = c;
                       }
               }
               else {
                       // Reset linebreak state
                       seenLineBreak = false;
               }

               // Character needs to be copied
               sb.setCharAt(t, isLineBreak ? '\n' : c);
               t++;
       }
       sb.setLength(t);

       return sb.toString();
}

	public static void main(String[] args) {
	 //System.out.println(isValidCatName("One 3232D Laptop Child‎"));
	}
	
	public static boolean isValidCatName(String catName) {
		final String temp = catName.replace("(", "").replace(")", "").replaceAll("_", " ");
		String[] splits = catName.split(" ");

		// No category length more than 5
		if (splits.length > 5) {
			return false;
		}

		Matcher matcher = pattern.matcher(catName);
		while (matcher.find()) {
		    	return false;
		    }

		for(int i=0;i<splits.length;i++) {
			 matcher = numpattern.matcher(splits[i]);
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

}
