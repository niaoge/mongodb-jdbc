// MongoConnectionTest.java

package com.mongodb.jdbc;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;

public class MongoJdbcTest extends Base {
	
	MongoConnection _conn;
	
	public MongoJdbcTest() {
		super();
		_conn = new MongoConnection(_db);
	}
	
//	@Test
//	public void testBasic1() throws SQLException {
//		
//		String name = "conn.basic1";
//		DBCollection coll = _db.getCollection(name);
//		coll.drop();
//		
//		coll.insert(BasicDBObjectBuilder.start("x", 1).add("y", "foo").get());
//		coll.insert(BasicDBObjectBuilder.start("x", 2).add("y", "bar").get());
//		
//		MongoStatement stmt = (MongoStatement) _conn.createStatement();
//		
//		ResultSet res = stmt.executeQuery("select * from " + name + " order by x");
//		assertTrue(res.next());
//		assertEquals(1, res.getInt("x"));
//		assertEquals("foo", res.getString("y"));
//		assertTrue(res.next());
//		assertEquals(2, res.getInt("x"));
//		assertEquals("bar", res.getString("y"));
//		assertFalse(res.next());
//		res.close();
//		
//		res = stmt.executeQuery("select * from " + name + " order by x DESC");
//		assertTrue(res.next());
//		assertEquals(2, res.getInt("x"));
//		assertEquals("bar", res.getString("y"));
//		assertTrue(res.next());
//		assertEquals(1, res.getInt("x"));
//		assertEquals("foo", res.getString("y"));
//		assertFalse(res.next());
//		res.close();
//		stmt.close();
//	}
//	
//	@Test
//	public void testBasic2() throws SQLException {
//		
//		String name = "conn.basic2";
//		DBCollection coll = _db.getCollection(name);
//		coll.drop();
//		MongoStatement stmt = (MongoStatement) _conn.createStatement();
//		
//		stmt.executeUpdate("insert into " + name + " ( x , y ) values ( 1 , 'foo' )");
//		stmt.executeUpdate("insert into " + name + " ( y , x ) values ( 'bar' , 2 )");
//		
//		ResultSet res = stmt.executeQuery("select * from " + name + " order by x");
//		assertTrue(res.next());
//		assertEquals(1, res.getInt("x"));
//		assertEquals("foo", res.getString("y"));
//		assertTrue(res.next());
//		assertEquals(2, res.getInt("x"));
//		assertEquals("bar", res.getString("y"));
//		assertFalse(res.next());
//		res.close();
//		
//		stmt.executeUpdate("update " + name + " set x=3 where y='foo' ");
//		res = stmt.executeQuery("select * from " + name + " order by x");
//		assertTrue(res.next());
//		assertEquals(2, res.getInt("x"));
//		assertEquals("bar", res.getString("y"));
//		assertTrue(res.next());
//		assertEquals(3, res.getInt("x"));
//		assertEquals("foo", res.getString("y"));
//		assertFalse(res.next());
//		res.close();
//		
//		stmt.close();
//		
//	}
//	
//	@Test
//	public void testBasic3() throws SQLException {
//		
//		String name = "connbasic3";
//		
//		MongoStatement stmt = (MongoStatement) _conn.createStatement();
//		
//		stmt.executeUpdate("drop table " + name);
//		
//		stmt.executeUpdate("insert into " + name + " ( x , y ) values ( 1 , 'foo' )");
//		stmt.executeUpdate("insert into " + name + " ( y , x ) values ( 'bar' , 2 )");
//		
//		ResultSet res = stmt.executeQuery("select * from " + name + " order by x");
//		assertTrue(res.next());
//		assertEquals(1, res.getInt("x"));
//		assertEquals("foo", res.getString("y"));
//		assertTrue(res.next());
//		assertEquals(2, res.getInt("x"));
//		assertEquals("bar", res.getString("y"));
//		assertFalse(res.next());
//		res.close();
//		
//		stmt.executeUpdate("update " + name + " set x=3 where y='foo' ");
//		res = stmt.executeQuery("select * from " + name + " order by x");
//		assertTrue(res.next());
//		assertEquals(2, res.getInt("x"));
//		assertEquals("bar", res.getString("y"));
//		assertTrue(res.next());
//		assertEquals(3, res.getInt("x"));
//		assertEquals("foo", res.getString("y"));
//		assertFalse(res.next());
//		res.close();
//		
//		stmt.close();
//		
//	}
//	
//	@Test
//	public void testBasic4() throws SQLException {
//		
//		String name = "connbasic3";
//		
//		MongoStatement stmt = (MongoStatement) _conn.createStatement();
//		
//		stmt.executeUpdate("drop table " + name);
//		
//		stmt.executeUpdate("insert into " + name + " ( x , y ) values ( 1 , 'foo' )");
//		stmt.executeUpdate("insert into " + name + " ( y , x ) values ( 'bar' , 2 )");
//		
//		ResultSet res = stmt.executeQuery("select * from " + name + " order by x");
//		assertTrue(res.next());
//		assertEquals(1, res.getInt("x"));
//		assertEquals("foo", res.getString("y"));
//		assertTrue(res.next());
//		assertEquals(2, res.getInt("x"));
//		assertEquals("bar", res.getString("y"));
//		assertFalse(res.next());
//		res.close();
//		
//		String sql = null;
//		PreparedStatement ps = null;
//		
//		sql = "update " + name + " set x=? where y=? ";
//		ps = _conn.prepareStatement(sql);
//		ps.setInt(1, 3);
//		ps.setString(2, "foo");
//		ps.executeUpdate();
//		
//		res = stmt.executeQuery("select * from " + name + " order by x");
//		assertTrue(res.next());
//		assertEquals(2, res.getInt("x"));
//		assertEquals("bar", res.getString("y"));
//		assertTrue(res.next());
//		assertEquals(3, res.getInt("x"));
//		assertEquals("foo", res.getString("y"));
//		assertFalse(res.next());
//		res.close();
//		
//		stmt.close();
//		
//	}
//	
//	@Test
//	public void testEmbed1() throws SQLException {
//		
//		String name = "connembed1";
//		DBCollection coll = _db.getCollection(name);
//		
//		MongoStatement stmt = (MongoStatement) _conn.createStatement();
//		stmt.executeUpdate("drop table " + name);
//		coll.insert(BasicDBObjectBuilder.start("x", 1).add("y", new BasicDBObject("z", 2)).get());
//		coll.insert(BasicDBObjectBuilder.start("x", 11).add("y", new BasicDBObject("z", 12)).get());
//		
//		String query;
//		MongoPreparedStatement ps;
//		ResultSet res;
//		
//		// query="select * from " + name + " order by x";
//		// ps =_conn.prepareStatement(query);
//		// res = ps.executeQuery();
//		// assertTrue( res.next() );
//		// assertEquals( 1 , res.getInt("x" ) );
//		// assertEquals( 2 , ((Map)(res.getObject( "y" ))).get( "z" ) );
//		// assertTrue( res.next() );
//		// res.close();
//		//
//		// query="select * from " + name +
//		// " where y.z=12 and y.z=13 and y.z>? order by x" ;
//		// ps=_conn.prepareStatement(query);
//		// ps.setInt(1, 10);
//		// res=ps.executeQuery();
//		// res.close();
//		// query= "select * from " + name +
//		// " where (y.z=8 or y.z=12)  order by x" ;
//		query = "select * from " + name + " where (y.z=8 or y.z=12) or y.z=13 and y.z<1 order by x";
//		ps = (MongoPreparedStatement) _conn.prepareStatement(query);
//		// ps.setInt(1, 10);
//		DBCursor dbCursor = ps._executeQueryCursor();
//		while (dbCursor.hasNext()) {
//			logger.info(dbCursor.next().toString());
//		}
//		// res = ps.executeQuery();
//		// res.close();
//		
//		// query= "select * from " + name + " where y.z=12 order by x" ;
//		// ps=_conn.prepareStatement(query);
//		// res = ps.executeQuery();
//		//
//		// assertTrue( res.next() );
//		// assertEquals( 11 , res.getInt("x" ) );
//		// assertEquals( 12 , ((Map)(res.getObject( "y" ))).get( "z" ) );
//		// assertFalse( res.next() );
//		// res.close();
//	}
//	
//	@Test
//	public void testPrepared1() throws SQLException {
//		
//		final String name = "connprepare1";
//		
//		MongoStatement stmt = (MongoStatement) _conn.createStatement();
//		
//		stmt.executeUpdate("drop table " + name);
//		
//		PreparedStatement ps = _conn.prepareStatement("insert into " + name + " ( x , y ) values ( ? , ? )");
//		ps.setInt(1, 1);
//		ps.setString(2, "foo");
//		ps.executeUpdate();
//		ps.setInt(1, 2);
//		ps.setString(2, "bar");
//		ps.executeUpdate();
//		
//		ResultSet res = stmt.executeQuery("select * from " + name + " order by x");
//		assertTrue(res.next());
//		assertEquals(1, res.getInt("x"));
//		assertEquals("foo", res.getString("y"));
//		assertTrue(res.next());
//		assertEquals(2, res.getInt("x"));
//		assertEquals("bar", res.getString("y"));
//		assertFalse(res.next());
//		res.close();
//		
//		stmt.executeUpdate("update " + name + " set x=3 where y='foo' ");
//		res = stmt.executeQuery("select * from " + name + " order by x");
//		assertTrue(res.next());
//		assertEquals(2, res.getInt("x"));
//		assertEquals("bar", res.getString("y"));
//		assertTrue(res.next());
//		assertEquals(3, res.getInt("x"));
//		assertEquals("foo", res.getString("y"));
//		assertFalse(res.next());
//		res.close();
//		stmt.close();
//	}
//	
//	@Test
//	public void testPreparedlimtskip() throws SQLException {
//		_conn.close();
//		
//		_db = _mongo.getDB("webcloud");
//		_conn = new MongoConnection(_db);
//		
//		//String sql = "select * from [order] limit ?,?";
//		//String sql = "select * from [order] limit ?";
//		String sql = "select * from [order] offset ?";
//		
//		MongoPreparedStatement pstmt = (MongoPreparedStatement) _conn.prepareStatement(sql);
//		pstmt.setLong(1, 4);
//		//pstmt.setLong(2, 5);
//		
//		MongoResultSet rst = (MongoResultSet) pstmt.executeQuery();
//		while (rst.next()) {
//			ResultSetMetaData meta = rst.getMetaData();
//			int n = meta.getColumnCount();
//			Map<String, Object> map = new HashMap<String, Object>();
//			for (int i = 1; i <= n; i++) {
//				String fieldName = meta.getColumnName(i);
//				map.put(fieldName, rst.getObject(fieldName));
//			}
//			logger.info(JSON.toJSONString(map));
//		}
//		
//		rst.close();
//		pstmt.close();
//	}
	
//	@Test
//	public void testCount()  throws SQLException{
//		_conn.close();
//		
//		_db = _mongo.getDB("demo");
//		_conn = new MongoConnection(_db);
//		
//		//String sql = "select username as no from [demo_order] ";
//		//String sql = "select count(*) as tatal from [demo_order]";
//		String sql = "select count(*) as total,sum(pay)  as tatalPayment from [demo_order] where 1=1 group by username,email   having tatalPayment>=0 order by username";
//		
//		MongoPreparedStatement pstmt = (MongoPreparedStatement) _conn.prepareStatement(sql);
//		
//		MongoResultSet rst = (MongoResultSet) pstmt.executeQuery();
//		while (rst.next()) {
//			ResultSetMetaData meta = rst.getMetaData();
//			int n = meta.getColumnCount();
//			Map<String, Object> map = new HashMap<String, Object>();
//			for (int i = 1; i <= n; i++) {
//				String fieldName = meta.getColumnName(i);
//				map.put(fieldName, rst.getObject(fieldName));
//			}
//			logger.info(JSON.toJSONString(map));
//		}
//		
//		rst.close();
//		pstmt.close();	
//	}
//	
//	@Test
//	public void testxxx()   throws SQLException{
//		_conn.close();
//		
//		_db = _mongo.getDB("yxbpro_db");
//		_conn = new MongoConnection(_db);
//		
//		//String sql = "select username as no from [demo_order] ";
//		//String sql = "select count(*) as tatal from [demo_order]";
//		String sql = "select * from yxbpro_task";
//		//String sql = "select * from yxbpro_task where _id='53152a75d8342b9aeb1ef0e8'";
//		
//		MongoPreparedStatement pstmt = (MongoPreparedStatement) _conn.prepareStatement(sql);
//		
//		MongoResultSet rst = (MongoResultSet) pstmt.executeQuery();
//		while (rst.next()) {
//			ResultSetMetaData meta = rst.getMetaData();
//			int n = meta.getColumnCount();
//			System.out.println("n:"+n);
//			Map<String, Object> map = new HashMap<String, Object>();
//			for (int i = 1; i <= n; i++) {
//				String fieldName = meta.getColumnName(i);
//				map.put(fieldName, rst.getObject(fieldName));
//			}
//			System.out.println(JSON.toJSONString(map));
//		}
//		
//		rst.close();
//		pstmt.close();	
//	}
//	
	
//	@Test
//	public void testxxx()   throws SQLException{
//		_conn.close();
//		
//		_db = _mongo.getDB("myDemo");
//		_conn = new MongoConnection(_db);
//		
//		String sql ;
//		MongoPreparedStatement pstmt;
//		MongoResultSet rst ;
//		Map<String, Object> map=null;
//		
////		sql=" delete from user";
////		pstmt= (MongoPreparedStatement) _conn.prepareStatement(sql);
////		pstmt.executeUpdate();
////	
////		sql =" insert into user(id,name) values(:userid,:name)";
////		pstmt = (MongoPreparedStatement) _conn.prepareStatement(sql);
////		pstmt.setInt(1, 0);
////		pstmt.setString(2, "张三");
////		pstmt.executeUpdate();	
////		
////		pstmt.setInt(1, 1);
////		pstmt.setString(2, "李四");
////		pstmt.executeUpdate();				
////		
////		sql=" delete from blog";
////		pstmt= (MongoPreparedStatement) _conn.prepareStatement(sql);
////		pstmt.executeUpdate();
////		
////		sql ="select * from user";
////		pstmt = (MongoPreparedStatement) _conn.prepareStatement(sql);
////		rst = (MongoResultSet) pstmt.executeQuery();
////
////		while (rst.next()) {
////			ResultSetMetaData meta = rst.getMetaData();
////			int n = meta.getColumnCount();
////			map = new HashMap<String, Object>();
////			for (int i = 1; i <= n; i++) {
////				String fieldName = meta.getColumnName(i);
////				map.put(fieldName, rst.getObject(fieldName));
////			}
////			System.out.println(JSON.toJSONString(map));
////		}		
////		
//		sql ="insert into blog(id, title,user) values (:id,:title,:user)";
//		pstmt = (MongoPreparedStatement) _conn.prepareStatement(sql);
//		pstmt.setLong(1, 1);
//		pstmt.setString(2, "关于mongodb 1");
//		List<Object> listValue =new ArrayList<>();
//	    Map<String, Object> m=new HashMap<>();
//	    m.put("name", "张三");
//	    listValue.add(m);
//	    m=new HashMap<>();
//	    m.put("name", "李四");
//	    m.put("lname", "小李四");
//	    listValue.add(m);
//	    
//	    m=new HashMap<>();
//	    m.put("name", "李四");
//	    m.put("lname", "小李四");
//	    listValue.add(m);
//	    
//	    m=new HashMap<>();
//	    m.put("name", "李四");
//	    m.put("lname", "小李四");
//	    listValue.add(m);
//	    
//	    m=new HashMap<>();
//	    m.put("name", "李四");
//	    m.put("lname", "小李四");
//	    listValue.add(m);
//	    
//	    m=new HashMap<>();
//	    m.put("name", "李四");
//	    m.put("lname", "小李四");
//	    listValue.add(m);
//		pstmt.setObject(3,listValue);
//		
//		pstmt.executeUpdate();	
//		
//		sql ="select  title, user.name as userName from blog where user.name='张三'";
//		pstmt = (MongoPreparedStatement) _conn.prepareStatement(sql);
//		rst = (MongoResultSet) pstmt.executeQuery();
//		while (rst.next()) {
//			ResultSetMetaData meta = rst.getMetaData();
//			int n = meta.getColumnCount();
//			map = new HashMap<String, Object>();
//			for (int i = 1; i <= n; i++) {
//				String fieldName = meta.getColumnName(i);
//				Object rsObject=rst.getObject(fieldName);
//				//logger.info("fieldName:"+rsObject.getClass());
//				map.put(fieldName, rsObject);
//			}
//			System.out.println(JSON.toJSONString(map));
//		}		
//				
//		rst.close();
//		pstmt.close();	
//	}
	
	
//	@Test
//	public void testxxx()   throws SQLException{
//		_conn.close();
//		
//		_db = _mongo.getDB("demo");
//		_conn = new MongoConnection(_db);
//		
//		//String sql = "select username as no from [demo_order] ";
//		//String sql = "select count(*) as tatal from [demo_order]";
//		String sql = "select count(*) from weixin_user  ";
//		//String sql = "select * from yxbpro_task where _id='53152a75d8342b9aeb1ef0e8'";
//		
//		MongoPreparedStatement pstmt = (MongoPreparedStatement) _conn.prepareStatement(sql);
//		
//		MongoResultSet rst = (MongoResultSet) pstmt.executeQuery();
//		while (rst.next()) {
//			ResultSetMetaData meta = rst.getMetaData();
//			int n = meta.getColumnCount();
//			System.out.println("n:"+n);
//			Map<String, Object> map = new HashMap<String, Object>();
//			for (int i = 1; i <= n; i++) {
//				String fieldName = meta.getColumnName(i);
//				map.put(fieldName, rst.getObject(fieldName));
//			}
//			System.out.println(JSON.toJSONString(map));
//		}
//		
//		rst.close();
//		pstmt.close();	
//	}
	
	@Test
	public void testxxx()   throws SQLException{
		_conn.close();
		
		_db = _mongo.getDB("demo");
		_conn = new MongoConnection(_db);
		
		//String sql = "select username as no from [demo_order] ";
		//String sql = "select count(*) as tatal from [demo_order]";
		String sql = "select count(*) from weixin_user  ";
		//String sql = "select * from yxbpro_task where ";
		
		MongoPreparedStatement pstmt = (MongoPreparedStatement) _conn.prepareStatement(sql);
		
		MongoResultSet rst = (MongoResultSet) pstmt.executeQuery();
		while (rst.next()) {
			ResultSetMetaData meta = rst.getMetaData();
			int n = meta.getColumnCount();
			System.out.println("n:"+n);
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 1; i <= n; i++) {
				String fieldName = meta.getColumnName(i);
				map.put(fieldName, rst.getObject(fieldName));
			}
			System.out.println(JSON.toJSONString(map));
		}
		
		rst.close();
		pstmt.close();	
	}	
	
}
