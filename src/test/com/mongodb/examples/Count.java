package com.mongodb.examples;

import java.net.UnknownHostException;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class Count {
	@BeforeTest
	protected void before(){
		
	}
//	@Test
//	public void testCount() {
//		MongoClient mongo;
//		try {
//			mongo = new MongoClient("localhost", 27017);
//		}
//		catch (UnknownHostException e) {
//			return;
//		}
//		DB db = mongo.getDB("demo");
//		DBCollection  order=db.getCollection("demo_order");
//		
//		BasicDBObject groups=new BasicDBObject();
//		groups.put("_id", null);
//		groups.put("count", new BasicDBObject("$sum",1));
//		BasicDBObject group =new BasicDBObject("$group",groups);
//		System.out.println(group.toString());
//		
//		AggregationOutput output =order.aggregate(group);
//		
//		System.out.println( output.getCommandResult().toString() );	
//	}
	
	@Test
	public void test1() {
		MongoClient mongo;
		try {
			mongo = new MongoClient("localhost", 27017);
		}
		catch (UnknownHostException e) {
			return;
		}
		DB db = mongo.getDB("demo");
		DBCollection  order=db.getCollection("weixin_order");
		
		BasicDBObject obj =new BasicDBObject();
		obj.append("$regex", "/*g/*");

		BasicDBObject query  =new BasicDBObject();
		query.put("num", obj);
		
	
		DBCursor dbCursor = order.find(query);
		int count=0;
		while (dbCursor.hasNext()) {
			System.out.println(dbCursor.next());
			count++;
		}
		System.out.println(count);
		
	}
	
}
