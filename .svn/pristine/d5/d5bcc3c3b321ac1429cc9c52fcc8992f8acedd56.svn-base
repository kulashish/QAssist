package com.aneedo.search.util;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

public class WriteSelectToFile {
	
BufferedWriter fileWriter;
	

	
	public static void main(String[] args) {
		WriteSelectToFile file = new WriteSelectToFile();
	}
	
	public WriteSelectToFile() {
		try {
			//fileWriter. 
			fileWriter = new BufferedWriter(new OutputStreamWriter
                    (new FileOutputStream("/home/ambha/mysqlexport/PageLinks2.txt"),"UTF8"));
			
        	Connection con = null;
            String url = "jdbc:mysql://localhost:3306/";
            String db = "wikipedia";
            String driver = "com.mysql.jdbc.Driver";
            String user = "root";
            String pass = "aneedo";
            Class.forName(driver);
            con = DriverManager.getConnection(url + db, user, pass);
            
            PreparedStatement pstmt = con.prepareStatement("select p1.name as name1, p1.pageId id1, p2.name name2, p2.pageId id2 from Page p1, Page p2, page_outlinks p " +
    		"where p1.id = p.id and p2.pageId = p.outLinks and p.id != p.outlinks and p.id > ? and p.id <= ? order by p1.pageId ");
            //for(int i = 10 ; i<=100; i =i+10) {
            Random random = new Random();
            int MAX = 31899000;
            for(int i = 500000 ; i<=MAX; i =i+10000) {
            pstmt.setInt(1, i-10000);
            pstmt.setInt(2, i);
            ResultSet results = pstmt.executeQuery();
            int current = 0;
            int next = 0;
            if(results != null) {
        		while (results.next()) {
        			try {
        			String name1 = results.getString("name1").replaceAll("_", " ").replaceAll(",", "").replace("(", "").replace(")", "").replaceAll("-", " ");
        			String name2 = results.getString("name2").replaceAll("_", " ").replaceAll(",", "").replace("(", "").replace(")", "").replaceAll("-", " ");
        			current = results.getInt("id1");
        			if(name1.indexOf("disambiguation") < 0 && name2.indexOf("disambiguation") < 0 && current != next)
        				fileWriter.write(name1 +"\t" + current +"\t" + name2 +"\t" + results.getInt("id2") +"\t"+random.nextInt(19)+"\n");
        			else if(name1.indexOf("disambiguation") < 0 && name2.indexOf("disambiguation") < 0 && current == next)
        				fileWriter.write(name2 +"\t" + results.getInt("id2") +"\t"+random.nextInt(19)+"\n");
        			next = current;
        			} catch (Exception e) {
						e.printStackTrace();
					}
    		    }
            }
            }
        		fileWriter.flush();
        		fileWriter.close();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	

}
