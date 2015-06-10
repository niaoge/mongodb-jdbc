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

import java.math.*;
import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.net.*;

import com.mongodb.*;

public class MongoPreparedStatement extends MongoStatement implements PreparedStatement {
	final String _sql;
	final JDBCExecutor _exec;
	List<Object> _params = new ArrayList<Object>();
	
	MongoPreparedStatement(MongoConnection conn, int type, int concurrency, int holdability, String sql) throws MongoSQLException {
		super(conn, type, concurrency, holdability);
		_sql = sql;
		_exec = new JDBCExecutor(conn._db, sql);
	}
	
	public ResultSet executeQuery() {
		// throw new RuntimeException( "executeQuery not done" );
		_exec.setParams(_params);
		Object result = null;
		try {
			result = _exec.query();
		}
		catch (MongoSQLException e) {
			e.printStackTrace();
			//throw new UnsupportedOperationException("Mismatched types: " + e.getLocalizedMessage());
		}
		
		if (result instanceof DBCursor) {
			DBCursor cursor = (DBCursor) result;
			return _DBCursorToResultSet(cursor);
		}
		
		BasicDBList  cursor=(BasicDBList)result;
		return _DBCursorToResultSet(cursor);
	}
	
	DBCursor _executeQueryCursor() {
		// throw new RuntimeException( "executeQuery not done" );
		_exec.setParams(_params);
		DBCursor cursor = null;
		
		try {
			cursor = _exec.query();
		}
		catch (MongoSQLException e) {
			e.printStackTrace();
		}
		return cursor;
		
	}
	
	public int executeUpdate() throws MongoSQLException {
		_exec.setParams(_params);
		return _exec.writeOperation();
	}
	
	void _set(int idx, Object o) {
		while (_params.size() <= idx)
			_params.add(null);
		_params.set(idx, o);
	}
	
	public void setInt(int idx, int x) {
		_set(idx, x);
	}
	
	public void setLong(int idx, long x) {
		_set(idx, x);
	}
	
	public void setObject(int idx, Object x) {
		_set(idx, x);
	}
	
	public void setShort(int idx, short x) {
		_set(idx, x);
	}
	
	public void setString(int idx, String x) {
		_set(idx, x);
	}
	
	public void setTime(int idx, Time x) {
		_set(idx, x);
	}
	
	public void setTime(int idx, Time x, Calendar cal) {
		_set(idx, cal);
	}
	
	public void setTimestamp(int idx, Timestamp x) {
		_set(idx, x);
	}
	
	@Deprecated
	public void addBatch() {
		throw new UnsupportedOperationException("batch stuff not supported");
	}
	
	// --- metadata ---
	@Deprecated
	public ResultSetMetaData getMetaData() {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public ParameterMetaData getParameterMetaData() {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void clearParameters() {
		throw new UnsupportedOperationException();
	}
	
	// ----- actually do
	@Deprecated
	public boolean execute() {
		throw new RuntimeException("execute not done");
	}
	
	// ---- setters -----
	@Deprecated
	public void setArray(int idx, Array x) {
		_setnotdone();
	}
	
	@Deprecated
	public void setAsciiStream(int idx, InputStream x) {
		_setnotdone();
	}
	
	@Deprecated
	public void setAsciiStream(int idx, InputStream x, int length) {
		_setnotdone();
	}
	
	@Deprecated
	public void setAsciiStream(int idx, InputStream x, long length) {
		_setnotdone();
	}
	
	@Deprecated
	public void setBigDecimal(int idx, BigDecimal x) {
		_setnotdone();
	}
	
	@Deprecated
	public void setBinaryStream(int idx, InputStream x) {
		_setnotdone();
	}
	
	@Deprecated
	public void setBinaryStream(int idx, InputStream x, int length) {
		_setnotdone();
	}
	
	@Deprecated
	public void setBinaryStream(int idx, InputStream x, long length) {
		_setnotdone();
	}
	
	@Deprecated
	public void setBlob(int idx, Blob x) {
		_setnotdone();
	}
	
	@Deprecated
	public void setBlob(int idx, InputStream inputStream) {
		_setnotdone();
	}
	
	@Deprecated
	public void setBlob(int idx, InputStream inputStream, long length) {
		_setnotdone();
	}
	
	@Deprecated
	public void setBoolean(int idx, boolean x) {
		_setnotdone();
	}
	
	@Deprecated
	public void setByte(int idx, byte x) {
		_setnotdone();
	}
	
	@Deprecated
	public void setBytes(int idx, byte[] x) {
		_setnotdone();
	}
	
	@Deprecated
	public void setCharacterStream(int idx, Reader reader) {
		_setnotdone();
	}
	
	@Deprecated
	public void setCharacterStream(int idx, Reader reader, int length) {
		_setnotdone();
	}
	
	@Deprecated
	public void setCharacterStream(int idx, Reader reader, long length) {
		_setnotdone();
	}
	
	@Deprecated
	public void setClob(int idx, Clob x) {
		_setnotdone();
	}
	
	@Deprecated
	public void setClob(int idx, Reader reader) {
		_setnotdone();
	}
	
	@Deprecated
	public void setClob(int idx, Reader reader, long length) {
		_setnotdone();
	}
	
	@Deprecated
	public void setDate(int idx, Date x) {
		_setnotdone();
	}
	
	@Deprecated
	public void setDate(int idx, Date x, Calendar cal) {
		_setnotdone();
	}
	
	@Deprecated
	public void setDouble(int idx, double x) {
		_setnotdone();
	}
	
	@Deprecated
	public void setFloat(int idx, float x) {
		_setnotdone();
	}
	
	@Deprecated
	public void setNCharacterStream(int idx, Reader value) {
		_setnotdone();
	}
	
	@Deprecated
	public void setNCharacterStream(int idx, Reader value, long length) {
		_setnotdone();
	}
	
	@Deprecated
	public void setNClob(int idx, NClob value) {
		_setnotdone();
	}
	
	@Deprecated
	public void setNClob(int idx, Reader reader) {
		_setnotdone();
	}
	
	@Deprecated
	public void setNClob(int idx, Reader reader, long length) {
		_setnotdone();
	}
	
	@Deprecated
	public void setNString(int idx, String value) {
		_setnotdone();
	}
	
	public void setNull(int idx, int sqlType) {
		_set(idx, null);
	}
	
	@Deprecated
	public void setNull(int idx, int sqlType, String typeName) {
		_setnotdone();
	}
	
	@Deprecated
	public void setObject(int idx, Object x, int targetSqlType) {
		_setnotdone();
	}
	
	@Deprecated
	public void setObject(int idx, Object x, int targetSqlType, int scaleOrLength) {
		_setnotdone();
	}
	
	@Deprecated
	public void setRef(int idx, Ref x) {
		_setnotdone();
	}
	
	@Deprecated
	public void setRowId(int idx, RowId x) {
		_setnotdone();
	}
	
	@Deprecated
	public void setSQLXML(int idx, SQLXML xmlObject) {
		_setnotdone();
	}
	
	@Deprecated
	public void setTimestamp(int idx, Timestamp x, Calendar cal) {
		_setnotdone();
	}
	
	@Deprecated
	public void setUnicodeStream(int idx, InputStream x, int length) {
		_setnotdone();
	}
	
	@Deprecated
	public void setURL(int idx, URL x) {
		_setnotdone();
	}
	
	@Deprecated
	void _setnotdone() {
		throw new UnsupportedOperationException("setter not done");
	}
	
	@Override
	public void closeOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	
}
