package com.aneedo.jwplext.dao.tablecreation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


 public class DBConnectionHadoop
 {
	 private static DBConnectionHadoop dbConn = null;
	 private static final String url = "jdbc:mysql://localhost:3306/";
	 private static final String db = "wikipedia";
	 private static final String driver = "com.mysql.jdbc.Driver";
	 private static final String user = "root";
	 private String pass = "lakshmi1";

	 private DBConnectionHadoop() throws ClassNotFoundException  {
	        Class.forName(driver);

	 }
	
	 public static DBConnectionHadoop getInstance(String password) throws ClassNotFoundException {
		 	if(dbConn == null) {
				dbConn = new DBConnectionHadoop();
				dbConn.pass = password;
			}
		 	
		 return dbConn;
     }
	 
	 public Connection getConnection() throws SQLException {
		 synchronized (this) {
		        Connection conn = DriverManager.getConnection(url + db, user, pass);
				 return conn;
		}
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
}
