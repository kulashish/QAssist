package com.aneedo.jwplext.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


 public class DBConnection
 {
	 private static DBConnection dbConn = null;
	 private static final String url = "jdbc:mysql://localhost:3306/";
	 private static final String db = "Wikipedia";
	 private static final String driver = "com.mysql.jdbc.Driver";
	 private static final String user = "root";
	 private static final String pass = "narayan";

	 private DBConnection() throws ClassNotFoundException  {
	        Class.forName(driver);

	 }
	
	 public static DBConnection getInstance() throws ClassNotFoundException {
		 	if(dbConn == null) {
				dbConn = new DBConnection();
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
