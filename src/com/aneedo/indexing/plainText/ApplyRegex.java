package com.aneedo.indexing.plainText;

import jregex.Matcher;
import jregex.Pattern;
public class ApplyRegex {
	public static void getHypernyms(String input) {
		String pattern1="({class1}((\\w+ )*?|( \\w+)*,* ))(like|such as)(:)*({inst11}(( \\w+)*,)*)({inst12}( \\w+)+?)( (and|or)({inst13}( \\w+))| etc.|\\.|.) ";
		//String pattern1 = "({class1}(\\w+ )*)(like|such as)({inst11}(( \\w+)*,)*)({inst12}( \\w+)*) (and({inst13}( \\w+))|etc.) ";
		String pattern2 = "({inst21}(\\w+ )*)and other ({class2}(?:\\w+ )*)(like|such as)({inst22}(?:( \\w+)*,)*)({inst23}( \\w+)*) and({inst24} (\\w+)*)";
		String hypernymRegex = "("+pattern1 + ")|(" + pattern2 +")";
		Pattern p=new Pattern(hypernymRegex);
		Matcher matcher = p.matcher(input);
		while (matcher.find()) {
			
			StringBuilder instances1=new StringBuilder();
			StringBuilder instances2=new StringBuilder();
			if(matcher.group("class1")!=null&& !matcher.group("class1").equals("")){
				System.out.println("HyperClass1:"+matcher.group("class1"));
				if(matcher.group("inst11")!=null&& !matcher.group("inst11").equals(""))
				{
					System.out.println("inst11:"+matcher.group("inst11"));
					instances1.append(matcher.group("inst11")+"|");
				}
				if(matcher.group("inst12")!=null&& !matcher.group("inst12").equals(""))
				{
					System.out.println("inst12:"+matcher.group("inst12"));
					instances1.append(matcher.group("inst12")+"|");
				}

				if(matcher.group("inst13")!=null&& !matcher.group("inst13").equals(""))
				{
					System.out.println("inst13:"+matcher.group("inst13"));
					instances1.append(matcher.group("inst13")+"|");
				}
			}

			if(matcher.group("class2")!=null&& !matcher.group("class2").equals(""))
			{
				System.out.println("HyperClass2:"+matcher.group("class2"));
				if(matcher.group("inst21")!=null&& !matcher.group("inst21").equals(""))
				{
					System.out.println("inst21:"+matcher.group("inst21"));
					instances2.append(matcher.group("inst21")+"|");
				}
				if(matcher.group("inst22")!=null&& !matcher.group("inst22").equals("")){
					instances2.append(matcher.group("inst22")+"|");
					System.out.println("inst22:"+matcher.group("inst22"));
				}

				if(matcher.group("inst23")!=null&& !matcher.group("inst23").equals(""))
				{
					System.out.println("inst23:"+matcher.group("inst23"));
					instances2.append(matcher.group("inst23")+"|");
				}
				if(matcher.group("inst24")!=null&& !matcher.group("inst24").equals(""))
				{
					System.out.println("inst24:"+matcher.group("inst24"));
					instances2.append(matcher.group("inst24"));
				}
			}
			System.out.println(instances1.toString());
			System.out.println(instances2.toString());
		}

	}
	public static void getMeronyms(String input){
		String pattern3 = "({Meroclass3}(\\w+ )*) includes({inst31}(( \\w+)*,)*)({inst32}( \\w+)*) and( (other|different|another) (kinds|types) of({class3}( \\w+)*)|({inst33}( \\w+)))";
		String meronymRegex = pattern3;
		Pattern p=new Pattern(meronymRegex);
		Matcher matcher = p.matcher(input);
		while (matcher.find()) {
			StringBuilder instances1=new StringBuilder();
			
			if(matcher.group("class3")!=null){
				System.out.println("Meroclass:"+matcher.group("class3"));
				if(matcher.group("inst31")!=null){
					System.out.println("inst31:"+matcher.group("inst31"));
					instances1.append(matcher.group("inst31")+"|");
				}
				if(matcher.group("inst32")!=null){
					System.out.println("inst32:"+matcher.group("inst32"));
					instances1.append(matcher.group("inst32")+"|");
				}
				if(matcher.group("inst33")!=null){
					instances1.append(matcher.group("inst33")+"|");
					System.out.println("inst33:"+matcher.group("inst33"));
				}
					
			}
			System.out.println("instanceset:"+instances1);
		}
	}
	
	public static void getSynonyms(String input){
		
		String pattern1="({key1}(\\w+ )+?)(is|,)( also)* (called|known|reffered to)( as)*?({inst11}(( \\w+)*,)*)({inst12}( \\w+)*)";
		String synonymRegex=pattern1;
		Pattern p=new Pattern(synonymRegex);
		Matcher matcher = p.matcher(input);
		while (matcher.find()) {
			StringBuilder instances1=new StringBuilder();
				if(matcher.group("key1")!=null&& !matcher.group("key1").equals(""))
					System.out.println("Synonymkey1:"+matcher.group("key1"));
				if(matcher.group("inst11")!=null&& !matcher.group("inst11").equals(""))
				{
					System.out.println("inst11:"+matcher.group("inst11"));
					instances1.append(matcher.group("inst11")+"|");
				}
				if(matcher.group("inst12")!=null&& !matcher.group("inst12").equals(""))
				{
					System.out.println("inst12:"+matcher.group("inst12"));
					instances1.append(matcher.group("inst12")+"|");
				}
				System.out.println(instances1.toString());
			}
	}
	public static void main(String[] args) {
		//		String pattern1="({class1}((\\w+ )*?|( \\w+)*,* ))(like|such as)(:)*?({inst11}(( \\w+)*,)*)({inst12}( \\w+)*?)( and({inst13}( \\w+))| etc.|.)* ";
		//		Pattern p=new Pattern(pattern1);
		//		String input="The risk of infection with other tropical diseases such as protozoal diarrhea, hepatitis A , typhoid fever , malaria , yellow fever , schistosomiasis and Lassa fever is described by the CIA as very high";
		//		input=input.replaceAll(",", ", ").replaceAll(",  ", ", ").replaceAll(" ,",",").replaceAll("  "," ");
		//		System.out.println(input);
		//		Matcher m=p.matcher(input);
		//		while(m.find()){
		//			if(m.group("class1")!=null&& !m.group("class1").equals("")){
		//				System.out.println("HyperClass:"+m.group("class1"));
		//				if(m.group("inst11")!=null&& !m.group("inst11").equals(""))
		//				System.out.println("inst11:"+m.group("inst11"));
		//				if(m.group("inst12")!=null&& !m.group("inst12").equals(""))
		//				System.out.println("inst12:"+m.group("inst12"));
		//				if(m.group("inst13")!=null&& !m.group("inst13").equals(""))
		//				System.out.println("inst13:"+m.group("inst13"));}
		//			}

		//String input=" The risk of infection with other tropical diseases such as protozoal diarrhea, hepatitis A , typhoid fever , malaria , yellow fever , schistosomiasis and Lassa fever is described by the CIA as very high. Liberia has no special bank secrecy laws like other tax havens , but this is not a deterrent as few Liberian offshore corporations actually handle their banking transactions within Liberia’s territory borders.";
		String input="India is called as hindustan";
		//		String input="Asial has several countries like Indai,pakistan,sri lanka. In addition there are some more countries like nepal, bhutan who also are in asia.";
		getSynonyms(input);

	}

}
