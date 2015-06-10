// MongoResultSetTest.java

package com.mongodb.jdbc;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.*;
import com.mongodb.jdbc.MongoResultSet;

import static org.testng.AssertJUnit.*;
import org.testng.annotations.Test;

public class MongoResultSetTest extends Base {

//    @Test
//    public void testbasic1(){
//        DBCollection c = _db.getCollection( "rs.basic1" );
//        c.drop();
//
//        c.insert( BasicDBObjectBuilder.start( "x" , 1 ).add( "y" , "foo" ).get() );
//        c.insert( BasicDBObjectBuilder.start( "x" , 2 ).add( "y" , "bar" ).get() );
        
//        MongoResultSet res = new MongoResultSet( c.find().sort( new BasicDBObject( "x" , 1 ) ) );
//        assertTrue( res.next() );
//        assertEquals( 1 , res.getInt("x" ) );
//        assertEquals( "foo" , res.getString("y" ) );
//        assertTrue( res.next() );
//        assertEquals( 2 , res.getInt("x" ) );
//        assertEquals( "bar" , res.getString("y" ) );
//        assertFalse( res.next() );
//    }

//    @Test
//    public void testorder1(){
//        DBCollection c = _db.getCollection( "rs.basic1" );
//        c.drop();
//
//        c.insert( BasicDBObjectBuilder.start( "x" , 1 ).add( "y" , "foo" ).get() );
//        c.insert( BasicDBObjectBuilder.start( "x" , 2 ).add( "y" , "bar" ).get() );
//        
//        MongoResultSet res = new MongoResultSet( c.find( new BasicDBObject() , BasicDBObjectBuilder.start("x",1).add("y",1).get() ).sort( new BasicDBObject( "x" , 1 ) ) );
//        assertTrue( res.next() );
//        assertEquals( 1 , res.getInt(1) );
//        assertEquals( "foo" , res.getString(2) );
//        assertTrue( res.next() );
//        assertEquals( 2 , res.getInt(1) );
//        assertEquals( "bar" , res.getString(2) );
//        assertFalse( res.next() );
//
//        res = new MongoResultSet( c.find( new BasicDBObject() , BasicDBObjectBuilder.start("y",1).add("x",1).get() ).sort( new BasicDBObject( "x" , 1 ) ) );
//        assertTrue( res.next() );
//        assertEquals( 1 , res.getInt(2) );
//        assertEquals( "foo" , res.getString(1) );
//        assertTrue( res.next() );
//        assertEquals( 2 , res.getInt(2) );
//        assertEquals( "bar" , res.getString(1) );
//        assertFalse( res.next() );
//    }

    
    @Test
    public void testorder1(){
    	 _db  =_mongo.getDB("demo");
        DBCollection c = _db.getCollection( "weixin_user" );
        
       DBObject count =new BasicDBObject();
       count.put("count", new BasicDBObject("$sum",1));
       count.put("_id", null);
       
       DBObject group =new BasicDBObject();
       group.put("$group", count);
       
       
       DBObject match =new BasicDBObject();
       DBObject regex =new BasicDBObject();
       regex.put("$regex", "/*a/*");
       
       match.put("$match", new BasicDBObject("username",regex));
       
       
       List<DBObject> list= new ArrayList<>();
       list.add(group);
       list.add(match);
       System.out.println(list);
       
       AggregationOutput output = c.aggregate(list);
       System.out.println(output);
    }
    
}
