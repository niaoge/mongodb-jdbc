package com.hoo.test;
 
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
 
/**
 * <b>function:</b>实现MongoDB的CRUD操纵
 * @author hoojo
 * @createDate 2011-6-2 下午03:21:23
 * @file MongoDB4CRUDTest.java
 * @package com.hoo.test
 * @project MongoDB
 * @blog http://blog.csdn.net/IBM_hoojo
 * @email hoojo_@126.com
 * @version 1.0
 */
public class MongoDB4CRUDTest {
	private static Logger logger =LoggerFactory.getLogger(MongoDB4CRUDTest.class);
	
    private Mongo mg = null;
    private DB db;
    private DBCollection users;
    
    public void init() {
        try {
            mg = new Mongo();
            //mg = new Mongo("localhost", 27017);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (MongoException e) {
            e.printStackTrace();
        }
        //获取temp DB；假如默认没有创建，mongodb会自动创建
        db = mg.getDB("temp");
        //获取users DBCollection；假如默认没有创建，mongodb会自动创建
        users = db.getCollection("users");
    }
    
    public void destory() {
        if (mg != null)
            mg.close();
        mg = null;
        db = null;
        users = null;
        System.gc();
    }
    
    public void print(Object o) {
        logger.info(o.toString());
    }
}
