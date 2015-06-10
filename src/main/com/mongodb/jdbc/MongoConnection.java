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
import java.util.*;
import java.util.concurrent.Executor;

import com.mongodb.*;

public class MongoConnection implements Connection {
	
	public MongoConnection(DB db) {
		_db = db;
	}
	
	@Deprecated
	public SQLWarning getWarnings() {
		throw new RuntimeException("should do get last error");
	}
	
	@Deprecated
	public void clearWarnings() {
		throw new RuntimeException("should reset error");
	}
	
	// ---- state -----
	
	public void close() {
		_db = null;
	}
	
	public boolean isClosed() {
		return _db == null;
	}
	
	// --- commit ----
	
	public void commit() {
		// NO-OP
	}
	
	public boolean getAutoCommit() {
		return true;
	}
	
	@Deprecated
	public void rollback() {
		throw new RuntimeException("can't rollback");
	}
	
	@Deprecated
	public void rollback(Savepoint savepoint) {
		throw new RuntimeException("can't rollback");
	}
	
	public void setAutoCommit(boolean autoCommit) {
		if (!autoCommit)
			throw new RuntimeException("autoCommit has to be on");
	}
	
	@Deprecated
	public void releaseSavepoint(Savepoint savepoint) {
		throw new RuntimeException("no savepoints");
	}
	
	@Deprecated
	public Savepoint setSavepoint() {
		throw new RuntimeException("no savepoints");
	}
	
	@Deprecated
	public Savepoint setSavepoint(String name) {
		throw new RuntimeException("no savepoints");
	}
	
	@Deprecated
	public void setTransactionIsolation(int level) {
		throw new RuntimeException("no TransactionIsolation");
	}
	
	// --- create ----
	@Deprecated
	public Array createArrayOf(String typeName, Object[] elements) {
		throw new RuntimeException("no create*");
	}
	
	@Deprecated
	public Struct createStruct(String typeName, Object[] attributes) {
		throw new RuntimeException("no create*");
	}
	
	@Deprecated
	public Blob createBlob() {
		throw new RuntimeException("no create*");
	}
	
	@Deprecated
	public Clob createClob() {
		throw new RuntimeException("no create*");
	}
	
	@Deprecated
	public NClob createNClob() {
		throw new RuntimeException("no create*");
	}
	
	@Deprecated
	public SQLXML createSQLXML() {
		throw new RuntimeException("no create*");
	}
	
	// ------- meta data ----
	
	public String getCatalog() {
		return null;
	}
	
	@Deprecated()
	public void setCatalog(String catalog) {
		throw new RuntimeException("can't set catalog");
	}
	
	public Properties getClientInfo() {
		return _clientInfo;
	}
	
	public String getClientInfo(String name) {
		return (String) _clientInfo.get(name);
	}
	
	public void setClientInfo(String name, String value) {
		_clientInfo.put(name, value);
	}
	
	public void setClientInfo(Properties properties) {
		_clientInfo = properties;
	}
	
	public int getHoldability() {
		return ResultSet.HOLD_CURSORS_OVER_COMMIT;
	}
	
	public void setHoldability(int holdability) {
	}
	
	@Deprecated
	public int getTransactionIsolation() {
		throw new RuntimeException("not dont yet");
	}
	
	@Deprecated
	public DatabaseMetaData getMetaData() {
		throw new RuntimeException("not dont yet");
	}
	
	public boolean isValid(int timeout) {
		return _db != null;
	}
	
	public boolean isReadOnly() {
		return false;
	}
	
	public void setReadOnly(boolean readOnly) {
		if (readOnly)
			throw new RuntimeException("no read only mode");
	}
	
	@Deprecated
	public Map<String, Class<?>> getTypeMap() {
		throw new RuntimeException("not done yet");
	}
	
	@Deprecated
	public void setTypeMap(Map<String, Class<?>> map) {
		throw new RuntimeException("not done yet");
	}
	
	// ---- Statement -----
	
	public Statement createStatement() {
		return createStatement(0, 0, 0);
	}
	
	public Statement createStatement(int resultSetType, int resultSetConcurrency) {
		return createStatement(resultSetType, resultSetConcurrency, 0);
	}
	
	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) {
		return new MongoStatement(this, resultSetType, resultSetConcurrency, resultSetHoldability);
	}
	
	// --- CallableStatement
	
	public CallableStatement prepareCall(String sql) {
		return prepareCall(sql, 0, 0, 0);
	}
	
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) {
		return prepareCall(sql, resultSetType, resultSetConcurrency, 0);
	}
	
	@Deprecated
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) {
		throw new RuntimeException("CallableStatement not supported");
	}
	
	// ---- PreparedStatement 
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return prepareStatement(sql, 0, 0, 0);
	}
	
	@Deprecated
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) {
		throw new RuntimeException("no PreparedStatement yet");
	}
	
	@Deprecated
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) {
		throw new RuntimeException("no PreparedStatement yet");
	}
	
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
			throws SQLException {
		return prepareStatement(sql, resultSetType, resultSetConcurrency, 0);
	}
	
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		return new MongoPreparedStatement(this, resultSetType, resultSetConcurrency, resultSetHoldability, sql);
	}
	
	@Deprecated
	public PreparedStatement prepareStatement(String sql, String[] columnNames) {
		throw new RuntimeException("no PreparedStatement yet");
	}
	
	// ---- random ----
	
	public String nativeSQL(String sql) {
		return sql;
	}
	
	@Deprecated
	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}
	
	public DB getDB() {
		return _db;
	}
	
	public DBCollection getCollection(String name) {
		return _db.getCollection(name);
	}
	
	DB _db;
	Properties _clientInfo;
	public void setSchema(String schema) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public String getSchema() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void abort(Executor executor) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public int getNetworkTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
}
