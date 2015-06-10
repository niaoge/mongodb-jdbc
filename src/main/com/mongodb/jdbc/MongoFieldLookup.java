﻿/*
 * Copyright 2013-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
/**
 *@Author: niaoge(Zhengsheng Xia)
 *@Email 78493244@qq.com
 *@Date: 2015-6-11
 */
package com.mongodb.jdbc;

import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.HashMap;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

class MongoFieldLookup  {
	static final String MUST_BE_POSITIVE = "index must be a positive number less or equal the count of returned columns: %s";
	
	final Map<Integer, String> fieldIds;
	final Map<String, Integer> fieldNames;
	final MongoResultSet mrst;
	
	MongoFieldLookup(MongoResultSet mrst) {
		fieldIds = new HashMap<Integer, String>();
		fieldNames = new HashMap<String, Integer>();
		this.mrst = mrst;
	}
	
	void checkIndex(int index) throws SQLException {
		// 1 <= index <= size()
		if (index < 1 || index > fieldIds.size()) {
			init(mrst.curDBObject);
			if (index < 1 || index > fieldIds.size()) {
				throw new SQLSyntaxErrorException(String.format(MUST_BE_POSITIVE, String.valueOf(index)) + " " + fieldIds.size());
			}
		}
	}
	
	//todo 这里需要加快速度
	void init(DBObject o) {
		if (o == null)
			return;
		for (String key : o.keySet())
			get(key);
	}
	
	String get(int i) {
		String s = fieldIds.get(i);
		if (s != null)
			return s;
		init(mrst.curDBObject);
		s = fieldIds.get(i);
		return s;
	}
	
	int get(String s) {
		Integer i = fieldNames.get(s);
		if (i == null) {
			i = fieldNames.size() + 1;
			store(i, s);
		}
		return i;
	}
	
	int getFeidsCount() {
		int count =0;
	    if (mrst.curDBObject instanceof BasicDBObject){
	    	BasicDBObject bsObject =(BasicDBObject)mrst.curDBObject ;
	    	count =bsObject.size();
	    	return count;
	    }
		init(mrst.curDBObject);
		return fieldIds.size();
	}
	
	private void store(Integer i, String s) {
		fieldIds.put(i, s);
		fieldNames.put(s, i);
	}
	
}
