/*
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

import java.sql.*;



import com.mongodb.*;

public class MongoStatement implements Statement {
	MongoConnection _conn;
	
	final int _type;
	final int _concurrency;
	final int _holdability;
	
	int _fetchSize = 0;
	int _maxRows = 0;
	
	MongoResultSet _last;
	
	MongoStatement(MongoConnection conn, int type, int concurrency, int holdability) {
		_conn = conn;
		_type = type;
		_concurrency = concurrency;
		_holdability = holdability;
		
		//		if (_type != 0)
		//			throw new UnsupportedOperationException("type not supported yet");
		//		if (_concurrency != 0)
		//			throw new UnsupportedOperationException("concurrency not supported yet");
		//		if (_holdability != 0)
		//			throw new UnsupportedOperationException("holdability not supported yet");
		
	}
	
	// ---- reads -----
	ResultSet _DBCursorToResultSet(DBCursor cursor) {
		// TODO
		// handle max rows
		if (_fetchSize > 0)
			cursor.batchSize(_fetchSize);
		if (_maxRows > 0)
			cursor.limit(_maxRows);
		
		_last = new MongoResultSet(cursor,cursor.count());
		
		return _last;
	}
	
	ResultSet _DBCursorToResultSet(BasicDBList cursor) {
		// TODO
		// handle max rows
//		if (_fetchSize > 0)
//			cursor.batchSize(_fetchSize);
//		if (_maxRows > 0)
//			cursor.limit(_maxRows);
		
		_last = new MongoResultSet(cursor.iterator(),cursor.size());
		
		return _last;
	}	
	
	// ---- fetch modifiers ----
	
	public int getFetchSize() {
		return _fetchSize;
	}
	
	public void setFetchSize(int rows) {
		_fetchSize = rows;
	}
	
	public int getMaxRows() {
		return _maxRows;
	}
	
	public void setMaxRows(int max) {
		_maxRows = max;
	}
	
	public int getResultSetConcurrency() {
		return _concurrency;
	}
	
	public int getResultSetHoldability() {
		return _holdability;
	}
	
	public int getResultSetType() {
		return _type;
	}
	
	public ResultSet executeQuery(String sql) throws SQLException {
		DBCursor cursor = new JDBCExecutor(_conn._db, sql).query();
		return _DBCursorToResultSet(cursor);
	}
	
	public void close() {
		_conn = null;
	}
	
	public Connection getConnection() {
		return _conn;
	}
	
	public boolean isClosed() {
		return _conn == null;
	}
	
	public boolean isPoolable() {
		return true;
	}
	
	public int executeUpdate(String sql) throws SQLException {
		return new JDBCExecutor(_conn._db, sql).writeOperation();
	}
	
	public ResultSet getResultSet() {
		return _last;
	}
	
	// --- batch ---
	@Deprecated
	public void addBatch(String sql) {
		throw new UnsupportedOperationException("batch not supported");
	}
	
	@Deprecated
	public void clearBatch() {
		throw new UnsupportedOperationException("batch not supported");
	}
	
	@Deprecated
	public int[] executeBatch() {
		throw new UnsupportedOperationException("batch not supported");
	}
	
	// --- random
	@Deprecated
	public void cancel() {
		throw new RuntimeException("not supported yet - can be");
	}
	
	public void setPoolable(boolean poolable) {
		if (!poolable)
			throw new RuntimeException("why don't you want me to be poolable?");
	}
	
	@Deprecated
	public void clearWarnings() {
		throw new RuntimeException("not supported yet - can be");
	}
	
	// --- writes ----
	@Deprecated
	public boolean execute(String sql) {
		throw new RuntimeException(
				"execute not done,please use 'executeUpdate(String sql)' or 'executeQuery(String sql)");
	}
	
	@Deprecated
	public boolean execute(String sql, int autoGeneratedKeys) {
		throw new RuntimeException(
				"execute not done,please use 'executeUpdate(String sql)' or 'executeQuery(String sql)");
	}
	
	@Deprecated
	public boolean execute(String sql, int[] columnIndexes) {
		throw new RuntimeException(
				"execute not done,please use 'executeUpdate(String sql)' or 'executeQuery(String sql)");
	}
	
	@Deprecated
	public boolean execute(String sql, String[] columnNames) {
		throw new RuntimeException(
				"execute not done,please use 'executeUpdate(String sql)' or 'executeQuery(String sql)");
	}
	
	@Deprecated
	public int executeUpdate(String sql, int autoGeneratedKeys) {
		throw new RuntimeException(
				"executeUpdate not done,please use 'executeUpdate(String sql)' or 'executeQuery(String sql)");
	}
	
	@Deprecated
	public int executeUpdate(String sql, int[] columnIndexes) {
		throw new RuntimeException(
				"executeUpdate not done,please use 'executeUpdate(String sql) or 'executeQuery(String sql)'");
	}
	
	@Deprecated
	public int executeUpdate(String sql, String[] columnNames) {
		throw new RuntimeException(
				"executeUpdate not done,please use 'executeUpdate(String sql)' or 'executeQuery(String sql)'");
	}
	
	@Deprecated
	public int getUpdateCount() {
		throw new RuntimeException("getUpdateCount not done");
	}
	
	@Deprecated
	public ResultSet getGeneratedKeys() {
		throw new RuntimeException("getGeneratedKeys notn done");
	}
	
	@Deprecated
	public int getQueryTimeout() {
		throw new RuntimeException("query timeout not done");
	}
	
	@Deprecated
	public void setQueryTimeout(int seconds) {
		throw new RuntimeException("query timeout not done");
	}
	
	@Deprecated
	public int getFetchDirection() {
		throw new RuntimeException("fetch direction not done yet");
	}
	
	@Deprecated
	public void setFetchDirection(int direction) {
		throw new RuntimeException("fetch direction not done yet");
	}
	
	@Deprecated
	public int getMaxFieldSize() {
		throw new RuntimeException("max field size not supported");
	}
	
	@Deprecated
	public void setMaxFieldSize(int max) {
		throw new RuntimeException("max field size not supported");
	}
	
	@Deprecated
	public boolean getMoreResults() {
		throw new RuntimeException("getMoreResults not supported");
	}
	
	@Deprecated
	public boolean getMoreResults(int current) {
		throw new RuntimeException("getMoreResults not supported");
	}
	
	// ---- more random -----
	@Deprecated
	public SQLWarning getWarnings() {
		//throw new UnsupportedOperationException("warning not supported yet");
		return null;
	}
	
	@Deprecated
	public void setCursorName(String name) {
		throw new UnsupportedOperationException("can't set cursor name");
	}
	
	@Deprecated
	public void setEscapeProcessing(boolean enable) {
		if (!enable)
			throw new RuntimeException("why do you want to turn escape processing off?");
	}
	
	@Deprecated
	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void closeOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
	}

	public boolean isCloseOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	
}
