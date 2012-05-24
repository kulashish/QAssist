package com.aneedo.search.dao;

	import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


	 public class ResultDBConnection
	 {
		 private DataSource ds = null;
		 
		 private static ResultDBConnection dbConn = null;
		 
		 public static final String JAVA_ENV = "java:/comp/env";
			
		public static final String DATASOURCE_NAME = "semclass";
		 
		 private ResultDBConnection() throws NamingException {
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup(JAVA_ENV);
			ds = (DataSource)envContext.lookup(DATASOURCE_NAME);
		 }
		
		 public static ResultDBConnection getInstance()  {
			 try {	
			 	if(dbConn == null) {
					dbConn = new ResultDBConnection();
				}
			 	} catch(NamingException exp){
					 exp.printStackTrace();
					 //throw new Exception(exp);
			 }
			 return dbConn;
	     }
		 
		 public Connection getConnection() throws SQLException {
			 Connection conn = ds.getConnection();
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
	}


