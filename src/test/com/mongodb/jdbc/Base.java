// Base.java

package com.mongodb.jdbc;

import org.testng.annotations.Test;

import com.mongodb.*;

@Test
public abstract class Base {
    
    final Mongo _mongo;
    DB _db;

    @Test
    public Base(){
        try {
            _mongo = new Mongo();
            _db = _mongo.getDB( "jdbctest" );
        }
        catch ( Exception e ){
            throw new RuntimeException( e );
        }
    }

    
}

