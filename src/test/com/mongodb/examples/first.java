package com.mongodb.examples;
// first.java

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

@Test
public class first {
	private static Logger logger =LoggerFactory.getLogger(blog.class);
	
	
    static void print( String name , ResultSet res )
        throws SQLException {
    	logger.info( name );
        while ( res.next() ){
        	logger.info( "\t" + res.getString( "name" ) + "\t" + res.getInt( "age" ) + "\t" + res.getObject(0) );
        }
    }

    public static void main()
        throws SQLException , ClassNotFoundException {
        
        Class.forName( "com.mongodb.jdbc.MongoDriver" );
        
        Connection c = DriverManager.getConnection( "mongodb://localhost/exampledb" );

        Statement stmt = c.createStatement();
        
        // drop old table
        stmt.executeUpdate( "drop table people" );

        // insert some data
        stmt.executeUpdate( "insert into people ( name , age ) values ( 'eliot' , 30 )" );
        stmt.executeUpdate( "insert into people ( name , age ) values ( 'sara' , 2 )" );
        stmt.executeUpdate( "insert into people ( name , age ) values ( 'jaime' , 28 )" );
        

        // print
        print( "not sorted" , stmt.executeQuery( "select name , age from people " ) );
        print( "sorted by age" , stmt.executeQuery( "select name , age from people order by age " ) );
        print( "sorted by age desc" , stmt.executeQuery( "select name , age from people order by age desc " ) );

        // update
        stmt.executeUpdate( "update people set age=32 where name='jaime'" );
        print( "sorted by age desc" , stmt.executeQuery( "select name , age from people order by age desc " ) );

    }

} 
