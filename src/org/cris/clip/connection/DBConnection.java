package org.cris.clip.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import oracle.jdbc.driver.OracleDriver;

public class DBConnection {

	private static DBConnection _INSTANCE =null;
	
	/**
     * It makes sure only single instance of DBConnection instance
     * 
     * @return DBConnection connection Instance
     */
    public static DBConnection getInstance() {
        if (_INSTANCE == null) {
            synchronized (DBConnection.class) {
                if (_INSTANCE == null) {
                    _INSTANCE = new DBConnection();
                } // endif
            } // endsyncblock
        }
        return _INSTANCE;
    }

    /**
     * Private contructor restricts the instance creation through new key word
     */
    private DBConnection() {
    }

    /**
     * get a DataBase Connection from the DBConnection Instance
     * @return java.sql.Connection
     * @throws Exception
     */
    public Connection getDBConnection() throws Exception{
    	Connection con=null;
    	DataSource datasource= null;
    	InitialContext ctx = null;
    	
    	try {
    		 ctx = new InitialContext();    		  
    		  datasource = (javax.sql.DataSource)ctx.lookup("java:/clip"); 	
    		 // datasource = (javax.sql.DataSource)ctx.lookup("java:/clipoff");
			 con = datasource.getConnection();		   
			 DriverManager.registerDriver(new OracleDriver());
    	}
    	catch(Exception e){
    		e.printStackTrace();
    		throw e;
    	}
    	return con;
    }
    
    public void closeConnection(Connection con){
    	try{
    		if(con!=null)
    			con.close();
    	}catch(SQLException e){
    		e.printStackTrace();
    	}
    }

}
