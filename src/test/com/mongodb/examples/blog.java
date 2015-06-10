package com.mongodb.examples;
// blog.java

import java.sql.*;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;


import com.mongodb.jdbc.*;

@Test
public class blog {
	private static Logger logger =LoggerFactory.getLogger(blog.class);
	
    static void print( String name , ResultSet res )
        throws SQLException {
        logger.info( name );
        while ( res.next() ){
        	logger.info( "\t" + res.getInt( "num" ) + "\t" + res.getString( "title" ) + "\t" + res.getObject( "tags" ) );
        }
    }

    public static void main( )
        throws SQLException , ClassNotFoundException {
        
        Class.forName( "com.mongodb.jdbc.MongoDriver" );
        
        Connection c = DriverManager.getConnection( "mongodb://localhost/exampledb" );
        MongoConnection mc = (MongoConnection)c;

        Statement st = c.createStatement();
        st.executeUpdate( "drop table blogposts" );

        PreparedStatement ps = c.prepareStatement( "insert into blogposts ( title , tags , num ) values ( ? , ? , ? )" );
        ps.setString( 1 , "first post!" );
        ps.setObject( 2 , new String[]{ "fun" , "eliot" } );
        ps.setInt( 3 , 1 );
        ps.executeUpdate();

        ps.setString( 1 , "wow - this is cool" );
        ps.setObject( 2 , new String[]{ "eliot" , "bar" } );
        ps.setInt( 3 , 2 );
        ps.executeUpdate();
        ps.close();


        logger.info(mc.getCollection( "blogposts" ).find().toString() );
        
//        print( "num should be 1 " , st.executeQuery( "select * from blogposts where tags='fun'" ) );
//        print( "num should be 2 " , st.executeQuery( "select * from blogposts where tags='bar'" ) );
        print( "num should be 0 " , st.executeQuery( "select * from blogposts where num<0" ) );
        print( "num should be 2 " , st.executeQuery( "select * from blogposts where num>=1" ) );
        print( "num should be 0 " , st.executeQuery( "select * from blogposts where num>0 and num<0" ) );
        print( "num should be 1 " , st.executeQuery( "select * from blogposts where tags='fun' and num=1" ) );
        print( "num should be 1 " , st.executeQuery( "select * from blogposts where tags='fun' or num>0" ) );
        print( "num should be 2 " , st.executeQuery( "select * from blogposts where num>0 and num<0 or num<100" ) );
        print( "num should be 2 " , st.executeQuery( "select title, tags  from blogposts where num>0" ) );
        st.executeUpdate( "delete blogposts where num>0 and num<0 or num<100" ) ;
        print( "num should be 2 " , st.executeQuery( "select title tags  from blogposts where num>0" ) );
        // TODO indexing

    }

} 
