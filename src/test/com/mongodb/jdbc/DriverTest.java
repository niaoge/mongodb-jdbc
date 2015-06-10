// DriverTest.java

package com.mongodb.jdbc;

import java.sql.*;
import static org.testng.AssertJUnit.*;
import org.testng.annotations.Test;

import com.mongodb.jdbc.MongoDriver;

@Test
public class DriverTest  {
    @Test
    public void test1()
        throws Exception {

        Connection c = null;
        try {
            c = DriverManager.getConnection( "mongodb://localhost/test" );
        }
        catch ( Exception e ){}

        assertNull( c );

        MongoDriver.install();
        c = DriverManager.getConnection( "mongodb://localhost/test" );
    } 

}
