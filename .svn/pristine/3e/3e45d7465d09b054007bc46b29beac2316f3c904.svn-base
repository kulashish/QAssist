package com.aneedo.search.util;


public class Test {
	private static String REGEX = "[a-zA-Z]";

	  private static String INPUT = "one9two4three7four1five";

	  public static void main(String[] argv) throws Exception {
//	    Pattern p = Pattern.compile(REGEX);
//	    String[] items = p.split(INPUT);
//	    for (int i = 0; i < items.length; i++) {
//	    	if("".equals(items[i]))
//	    		System.out.println("Empty");
//	    	else System.out.println(items[i]);
//	    }
		  //Random random = new Random();
		  //int i = random.nextInt(19);
		  // System.out.println(i);
		  digest();
	  }
	  
	  private static void digest() throws Exception {
		  String plaintext = "your text here";
		  String plaintext2 = "your  text here";
//		  MessageDigest m = MessageDigest.getInstance("MD5");
//		  m.reset();
//		  m.update(plaintext.getBytes());
//		  byte[] digest = m.digest();
//		  BigInteger bigInt = new BigInteger(1,digest);
//		  String hashtext = bigInt.toString(16);
//		  while(hashtext.length() < 32 ){
//			  hashtext = "0"+hashtext;
//			}

		  System.out.println(plaintext.hashCode());
		  System.out.println(plaintext2.hashCode());
		  
		  if(plaintext.hashCode() == plaintext2.hashCode()) {
			  System.out.println("equal");
		  } else {
			  System.out.println("not equal");
		  }
		  
		  if(plaintext.equalsIgnoreCase(plaintext2)) {
			  System.out.println("equal");
		  } else {
			  System.out.println("not equal");
		  }
	  }
}
