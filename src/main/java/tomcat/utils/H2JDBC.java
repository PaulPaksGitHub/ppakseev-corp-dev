package tomcat.utils;

import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.SQLException; 
import java.sql.Statement;

public class H2JDBC { 
   private static H2JDBCInstance instance = new H2JDBCInstance();

   public static H2JDBCInstance getInstance(){
     return H2JDBC.instance;
   }
}