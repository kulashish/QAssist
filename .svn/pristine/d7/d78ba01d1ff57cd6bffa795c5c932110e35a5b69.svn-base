package com.aneedo.jwplext.dao.tablecreation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


 public class DBConnectionFactory
 {
	 private static DBConnectionFactory dbConn = null;
	 private String url = "jdbc:mysql://10.129.5.199:3306/";
	 private String db = "wikipediaNew";
	 private static final String driver = "com.mysql.jdbc.Driver";
	 private static final String user = "root";
	 private String pass = "bet123";

	 private DBConnectionFactory() throws ClassNotFoundException  {
	        Class.forName(driver);

	 }
	
	 public static DBConnectionFactory getInstance(String password) throws ClassNotFoundException {
		 	if(dbConn == null) {
				dbConn = new DBConnectionFactory();
				dbConn.pass = password;
			}
		 	
		 return dbConn;
     }
	 
	 public static DBConnectionFactory getInstance(String dbName,String password, String url) throws ClassNotFoundException {
		 if(dbConn == null) {
				dbConn = new DBConnectionFactory();
				 dbConn.pass = password;
				 dbConn.db = dbName;
				 dbConn.url = "jdbc:mysql://"+url+":3306/";
			 	System.out.println("DB url : " + dbConn.url);
		 }

		 return dbConn;
  }
	 
	 public Connection getConnection() throws SQLException {
		 synchronized (this) {
		        Connection conn = DriverManager.getConnection(url + db, user, pass);
		        //System.out.println("DB url : " + url);
				 return conn;
		}
	 }
	 
	 public Connection getConnectionOfDatabase() throws SQLException {
		// System.out.println("Pass" + pass + "db name :" + this.db);
		        Connection conn = DriverManager.getConnection(url + this.db, user, this.pass);
				 return conn;
	 }

	 
	 public void close( Connection connection ) throws Exception {
		 try {
			 if( connection != null && !connection.isClosed() ) {
				 connection.close();
			 }
		 } catch(SQLException exp) {
			 
			 exp.printStackTrace();
			 throw new Exception(exp);
		 }
	 }
	 
	 public void close( Statement statement ) throws Exception {
		 try {
			 if( statement != null) {
				 statement.close();
		     }
			 } catch(SQLException exp) {
				 exp.printStackTrace();
				 throw new Exception(exp);
			 }
	 }
	 
	 public static void main(String[] args) {
		
	}
}
