package tomcat.utils;

import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.SQLException; 
import java.sql.Statement;

public class H2JDBCInstance {
  private final String JDBC_DRIVER = "org.h2.Driver";   
  private final String DB_URL = "jdbc:h2:~/test";  
  
  private final String USER = "sa"; 
  private final String PASS = ""; 

  private Connection conn = null;

  public Connection getConnection(){
    return this.conn;
  }

  H2JDBCInstance(){
   // try { 
   //    Class.forName(JDBC_DRIVER);
   //    System.out.println("Connecting to database...");
   //    this.conn = DriverManager.getConnection(DB_URL,USER,PASS);
   // } catch(SQLException se) { 
   //    se.printStackTrace(); 
   // } catch(Exception e) { 
   //    e.printStackTrace(); 
   // }
  }
}