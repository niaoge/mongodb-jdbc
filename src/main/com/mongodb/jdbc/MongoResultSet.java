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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class MongoResultSet  implements ResultSet {
	static final String MUST_BE_POSITIVE = "index must be a positive number less or equal the count of returned columns: %s";
	// members
	final MongoResultSetMetaData meta;
	Iterator<DBObject> cursor = null;
	//final DBCursor cursor;
	
	BasicDBObject curDBObject;
	int row = 0;
	boolean closed = false;
	long size;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	MongoResultSet(Iterator cursor, long size) {
		this.cursor = cursor;
		this.size = size;
		//DBObject wanted = cursor.getKeysWanted();
		//fieldLookup.init(wanted);
		meta = new MongoResultSetMetaData(this);
	}
	
	private Number _getNumber(Object o) {
		Number x = (Number) o;
		if (x == null)
			return 0;
		return x;
	}
	
	int getFieldSize() {
		return curDBObject.size();
	}
	
	void checkIndex(int index) throws SQLException {
		if (index <= getFieldSize())
			return;
		throw new SQLSyntaxErrorException(String.format(MUST_BE_POSITIVE, String.valueOf(index)));
	}
	
	private ArrayList<Entry<String, Object>> entryList = null;
	private ArrayList<String> keyList =null;
	
	
	public boolean next() {
		entryList =null;
		keyList =null;
		boolean result = cursor.hasNext();
		if (result) {
			curDBObject = (BasicDBObject) cursor.next();
			entryList = new ArrayList<>(curDBObject.entrySet());
			
			
//			Iterator<Entry<String, Object>> it = curDBObject.entrySet().iterator();
//			while (it.hasNext()) {
//				Entry<String, Object> entry = it.next();
//				entryList.add(entry);
//			}
			curRow++;
		}
		return result;
	}
	
	Object _ifObjectId(final Object o) {
		if (o == null)
			return null;
		
		if (o instanceof ObjectId)
			return ((ObjectId) o).toString();
		
		return o;
	}
	
	String _getColumnName(int columnIndex) {
		return entryList.get(columnIndex - 1).getKey();
	}
	
	Object _get(final int columnIndex) {
		if (columnIndex == 0)
			return curDBObject;
		Object o=entryList.get(columnIndex - 1).getValue();
		if (o == null)
			return null;
		
		if (o instanceof ObjectId)
			return ((ObjectId) o).toString();
		
		return o;
	}
	
	Object _get(final String columnName) {
		Object o=curDBObject.get(columnName);
		if (o == null)
			return null;
		
		if (o instanceof ObjectId)
			return ((ObjectId) o).toString();
		
		return o;
	}
	
	public Object getObject(int columnIndex) {
		return _get(columnIndex);
	}
	
	// column <-> int mapping
	public int findColumn(String columnName) {
		//因为这个不常用，所以只在需要时创建
		if (keyList==null){
			keyList =new ArrayList<>(curDBObject.keySet());
		}
	    int idx =keyList.indexOf(columnName);
	    if (idx>-1)
	    	return idx+1;
	    return idx;
	}
	
	private String _getString(Object o) {
		if (o == null)
			return null;
		return o.toString();
	}
	
	public String getString(String columnName) {
		return _getString(_get(columnName));
	}
	
	public String getString(int columnIndex) {
		return _getString(_get(columnIndex));
	}
	
	public Object getObject(String columnName) {
		return _get(columnName);
	}
	
	String _getColumnClassName(int columnIndex) {
		Object o = _get(columnIndex);
		if (o != null)
			return o.getClass().toString();
		return null;
	}
	
	public void clearWarnings() {
		// NO-OP
	}
	
	public void close() {
		closed = true;
	}
	
	public boolean isClosed() {
		return closed;
	}
	
	// meta data
	public int getConcurrency() {
		return CONCUR_READ_ONLY;
	}
	
	public int getType() {
		return TYPE_FORWARD_ONLY;
	}
	
	public void setFetchDirection(int direction) {
		if (direction == getFetchDirection())
			return;
		throw new UnsupportedOperationException();
	}
	
	public int getFetchDirection() {
		return 1;
	}
	
	public String getCursorName() {
		return "MongoResultSet: " + cursor.toString();
	}
	
	public int getRow() {
		return row;
	}
	
	public int getHoldability() {
		return ResultSet.HOLD_CURSORS_OVER_COMMIT;
	}
	
	private boolean _getBoolean(Object o) {
		if (o == null)
			return false;
		return (Boolean) o;
	}
	
	public boolean getBoolean(int columnIndex) {
		return _getBoolean(_get(columnIndex));
	}
	
	public boolean getBoolean(String columnName) {
		return _getBoolean(_get(columnName));
	}
	
	public byte[] getBytes(int columnIndex) {
		return (byte[]) (_get(columnIndex));
	}
	
	public byte[] getBytes(String columnName) {
		return (byte[]) (_get(columnName));
	}
	
	public Date getDate(int columnIndex) {
		return (Date) (_get(columnIndex));
	}
	
	public Date getDate(String columnName) {
		return (Date) _get(columnName);
	}
	
	public double getDouble(int columnIndex) {
		return _getNumber((_get(columnIndex))).doubleValue();
	}
	
	public double getDouble(String columnName) {
		return _getNumber(_get(columnName)).doubleValue();
	}
	
	public float getFloat(int columnIndex) {
		return _getNumber(_get(columnIndex)).floatValue();
	}
	
	public float getFloat(String columnName) {
		return _getNumber(_get(columnName)).floatValue();
	}
	
	public int getInt(int columnIndex) {
		return _getNumber(_get(columnIndex)).intValue();
	}
	
	public int getInt(String columnName) {
		return _getNumber(_get(columnName)).intValue();
	}
	
	public long getLong(int columnIndex) {
		return _getNumber(_get(columnIndex)).longValue();
	}
	
	public long getLong(String columnName) {
		return _getNumber(_get(columnName)).longValue();
	}
	
	public short getShort(int columnIndex) {
		return _getNumber(_get(columnIndex)).shortValue();
	}
	
	public short getShort(String columnName) {
		return _getNumber(_get(columnName)).shortValue();
	}
	
	public ResultSetMetaData getMetaData() {
		return meta;
	}
	
	private URL _getURL(Object o) throws SQLException {
		try {
			return new URL(_getString(o));
		}
		catch (MalformedURLException e) {
			throw new SQLException("bad url [" + o + "]");
		}
	}
	
	public URL getURL(int columnIndex) throws SQLException {
		return _getURL(_get(columnIndex));
	}
	
	public URL getURL(String columnName) throws SQLException {
		return _getURL(_get(columnName));
	}
	
	int curRow = 0;
	
	//只能向后定位，如果没有达到位置，则调用next向后一位
	public boolean absolute(int row) {
		boolean result = (row <= size) && (curRow <= row);
		if (result)
			while (curRow < row)
				next();
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T unwrap(Class<T> iface) throws SQLException {
		if (iface == DBObject.class) {
			return (T) curDBObject;
		}
		else if (iface == DBCursor.class)
			return (T) cursor;
		return null;
	}
	
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		if (iface == DBObject.class)
			return true;
		else if (iface == DBCursor.class)
			return true;
		return false;
	}
	
	@Deprecated
	public SQLWarning getWarnings() {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void setFetchSize(int rows) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public int getFetchSize() {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public Statement getStatement() {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void afterLast() {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void beforeFirst() {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public boolean first() {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public boolean isAfterLast() {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public boolean isBeforeFirst() {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public boolean isFirst() {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public boolean isLast() {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public boolean last() {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void moveToCurrentRow() {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void moveToInsertRow() {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public boolean previous() {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void refreshRow() {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public boolean relative(int rows) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public boolean rowDeleted() {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public boolean rowInserted() {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public boolean rowUpdated() {
		throw new UnsupportedOperationException();
	}
	
	// modifications
	@Deprecated
	public void insertRow() {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void cancelRowUpdates() {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void deleteRow() {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateRow() {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateArray(int columnIndex, Array x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateArray(String columnName, Array x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateAsciiStream(int columnIndex, InputStream x, int length) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateAsciiStream(String columnName, InputStream x, int length) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateAsciiStream(int columnIndex, InputStream x, long length) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateAsciiStream(String columnName, InputStream x, long length) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateAsciiStream(int columnIndex, InputStream x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateAsciiStream(String columnName, InputStream x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateBigDecimal(int columnIndex, BigDecimal x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateBigDecimal(String columnName, BigDecimal x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateBinaryStream(int columnIndex, InputStream x, int length) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateBinaryStream(String columnName, InputStream x, int length) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateBinaryStream(int columnIndex, InputStream x, long length) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateBinaryStream(String columnName, InputStream x, long length) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateBinaryStream(int columnIndex, InputStream x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateBinaryStream(String columnName, InputStream x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateBlob(int columnIndex, Blob x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateBlob(String columnName, Blob x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateBlob(int columnIndex, InputStream x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateBlob(String columnName, InputStream x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateBlob(int columnIndex, InputStream x, long l) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateBlob(String columnName, InputStream x, long l) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateBoolean(int columnIndex, boolean x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateBoolean(String columnName, boolean x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateByte(int columnIndex, byte x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateByte(String columnName, byte x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateBytes(int columnIndex, byte[] x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateBytes(String columnName, byte[] x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateCharacterStream(int columnIndex, Reader x, int length) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateCharacterStream(String columnName, Reader reader, int length) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateCharacterStream(int columnIndex, Reader x, long length) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateCharacterStream(String columnName, Reader reader, long length) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateCharacterStream(int columnIndex, Reader x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateCharacterStream(String columnName, Reader reader) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateClob(int columnIndex, Clob x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateClob(String columnName, Clob x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateClob(int columnIndex, Reader x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateClob(String columnName, Reader x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateClob(int columnIndex, Reader x, long l) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateClob(String columnName, Reader x, long l) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateDate(int columnIndex, Date x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateDate(String columnName, Date x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateDouble(int columnIndex, double x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateDouble(String columnName, double x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateFloat(int columnIndex, float x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateFloat(String columnName, float x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateInt(int columnIndex, int x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateInt(String columnName, int x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateLong(int columnIndex, long x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateLong(String columnName, long x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateNull(int columnIndex) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateNull(String columnName) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateObject(int columnIndex, Object x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateObject(int columnIndex, Object x, int scale) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateObject(String columnName, Object x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateObject(String columnName, Object x, int scale) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateRef(int columnIndex, Ref x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateRef(String columnName, Ref x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateRowId(int columnIndex, RowId x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateRowId(String columnName, RowId x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateShort(int columnIndex, short x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateShort(String columnName, short x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateSQLXML(int columnIndex, SQLXML xmlObject) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateSQLXML(String columnName, SQLXML xmlObject) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateString(int columnIndex, String x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateString(String columnName, String x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateTime(int columnIndex, Time x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateTime(String columnName, Time x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateTimestamp(int columnIndex, Timestamp x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateTimestamp(String columnName, Timestamp x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public Array getArray(int i) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public Array getArray(String colName) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public InputStream getAsciiStream(int columnIndex) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public InputStream getAsciiStream(String columnName) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public BigDecimal getBigDecimal(int columnIndex) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public BigDecimal getBigDecimal(int columnIndex, int scale) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public BigDecimal getBigDecimal(String columnName) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public BigDecimal getBigDecimal(String columnName, int scale) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public InputStream getBinaryStream(int columnIndex) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public InputStream getBinaryStream(String columnName) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public Blob getBlob(int i) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public Blob getBlob(String colName) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public byte getByte(int columnIndex) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public byte getByte(String columnName) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public Reader getCharacterStream(int columnIndex) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public Reader getCharacterStream(String columnName) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public Clob getClob(int i) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public Clob getClob(String colName) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public Date getDate(int columnIndex, Calendar cal) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public Date getDate(String columnName, Calendar cal) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
		if (columnIndex == 0)
			return curDBObject;
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	@Override
	public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public Ref getRef(int i) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public Ref getRef(String colName) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public RowId getRowId(int i) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public RowId getRowId(String name) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public SQLXML getSQLXML(int columnIndex) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public SQLXML getSQLXML(String columnName) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public Time getTime(int columnIndex, Calendar cal) {
		throw new UnsupportedOperationException();
	}
	
	public Time getTime(int columnIndex) {
		return (Time) _get(columnIndex);
		//throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public Time getTime(String columnName, Calendar cal) {
		throw new UnsupportedOperationException();
	}
	
	public Time getTime(String columnName) {
		return (Time) _get(columnName);
	}
	
	public Timestamp getTimestamp(int columnIndex) {
		return (Timestamp) _get(columnIndex);
	}
	
	public Timestamp getTimestamp(String columnName) {
		return (Timestamp) _get(columnName);
	}
	
	@Deprecated
	public Timestamp getTimestamp(int columnIndex, Calendar cal) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public Timestamp getTimestamp(String columnName, Calendar cal) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public InputStream getUnicodeStream(int columnIndex) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public InputStream getUnicodeStream(String columnName) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public String getNString(int columnIndex) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public String getNString(String columnName) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public NClob getNClob(int columnIndex) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public NClob getNClob(String columnName) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public Reader getNCharacterStream(int columnIndex) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public Reader getNCharacterStream(String columnName) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateNCharacterStream(int columnIndex, Reader x) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateNCharacterStream(int columnIndex, Reader x, long length) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateNCharacterStream(String columnLabel, Reader reader) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateNCharacterStream(String columnLabel, Reader reader, long length) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateNClob(int columnIndex, NClob nClob) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateNClob(int columnIndex, Reader reader) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateNClob(int columnIndex, Reader reader, long length) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateNClob(String columnLabel, NClob nClob) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateNClob(String columnLabel, Reader reader) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateNClob(String columnLabel, Reader reader, long length) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateNString(int columnIndex, String nString) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public void updateNString(String columnLabel, String nString) {
		throw new UnsupportedOperationException();
	}
	
	@Deprecated
	public boolean wasNull() {
		throw new UnsupportedOperationException();
	}
	
	public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
