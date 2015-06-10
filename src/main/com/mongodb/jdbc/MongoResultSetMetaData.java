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

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

class MongoResultSetMetaData implements ResultSetMetaData {
	static final String NO_INTERFACE = "no object was found that matched the provided interface: %s";
	
	private MongoResultSet mrst = null;
	
	MongoResultSetMetaData(MongoResultSet mrst) {
		this.mrst = mrst;
	}
	
	//1.获得1列所在的Catalog名字
	public String getCatalogName(int column) throws SQLException {
		mrst.checkIndex(column);
		return "";
	}
	
	//2.获得1列对应数据类型的类
	public String getColumnClassName(int column) throws SQLException {
		mrst.checkIndex(column);
		return mrst._getColumnClassName(column);
	}
	
	//3.获得该ResultSet所有列的数目
	public int getColumnCount() throws SQLException {
		int result = mrst.getFieldSize();
		return result;
	}
	
	//4.1列在数据库中类型的最大字符个数
	public int getColumnDisplaySize(int column) throws SQLException {
		mrst.checkIndex(column);
		//return _fields.get(column - 1).getValueString().length();
		return 0;
	}
	
	//5.1列的默认的列的标题
	public String getColumnLabel(int column) throws SQLException {
		mrst.checkIndex(column);
		return getColumnName(column);
	}
	
	//6.1例的例名
	public String getColumnName(int column) throws SQLException {
		mrst.checkIndex(column);
		return mrst._getColumnName(column);
	}
	
	//7.1列的类型,返回SqlType中的编号
	public int getColumnType(int column) throws SQLException {
		mrst.checkIndex(column);
		//return _fields.get(column - 1).getValueType().getJdbcType();
		return 0;
	}
	
	// 8.1列在数据库中的类型，返回类型全名
	public String getColumnTypeName(int column) throws SQLException {
		mrst.checkIndex(column);
		//return _fields.get(column - 1).getValueType().getClass().getSimpleName();
		return String.class.getSimpleName();
	}
	
	//9.1列类型的精确度(类型的长度)
	public int getPrecision(int column) throws SQLException {
		mrst.checkIndex(column);
		//            TypedColumn col = _fields.get(column - 1);
		//            return col.getValueType().getPrecision(col.getValue());
		return 0;
	}
	
	//10.1列小数点后的位数
	public int getScale(int column) throws SQLException {
		mrst.checkIndex(column);
		//            TypedColumn tc = _fields.get(column - 1);
		//            return tc.getValueType().getScale(tc.getValue());
		return 0;
	}
	
	//11.1列对应的模式的名称（应该用于Oracle）
	public String getSchemaName(int column) throws SQLException {
		mrst.checkIndex(column);
		//return keyspace;
		return "";
	}
	
	//12.1列对应的表名
	public String getTableName(int column) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}
	
	//13.1列是否自动递增
	public boolean isAutoIncrement(int column) throws SQLException {
		mrst.checkIndex(column);
		//return _fields.get(column - 1).getValueType() instanceof JdbcCounterColumn; // todo: check Value is correct.
		return false;
	}
	
	//14.1列在数据库中是否为货币型
	public boolean isCurrency(int column) throws SQLException {
		mrst.checkIndex(column);
		//TypedColumn tc = _fields.get(column - 1);
		//return tc.getValueType().isCurrency();
		return false;
	}
	
	//1列是否为空
	public int isNullable(int column) throws SQLException {
		mrst.checkIndex(column);
		return ResultSetMetaData.columnNullable;
	}
	
	//16.1列是否为只读
	public boolean isReadOnly(int column) throws SQLException {
		mrst.checkIndex(column);
		return column == 0;
	}
	
	//17.1列能否出现在where
	public boolean isSearchable(int column) throws SQLException {
		mrst.checkIndex(column);
		return false;
	}
	
	public boolean isCaseSensitive(int column) throws SQLException {
		mrst.checkIndex(column);
		//TypedColumn tc = _fields.get(column - 1);
		//return tc.getValueType().isCaseSensitive();
		return true;
	}
	
	public boolean isDefinitelyWritable(int column) throws SQLException {
		mrst.checkIndex(column);
		return isWritable(column);
	}
	
	public boolean isSigned(int column) throws SQLException {
		mrst.checkIndex(column);
		//TypedColumn tc = _fields.get(column - 1);
		//return tc.getValueType().isSigned();
		return false;
	}
	
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}
	
	public boolean isWritable(int column) throws SQLException {
		mrst.checkIndex(column);
		return column > 0;
	}
	
	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new SQLFeatureNotSupportedException(String.format(NO_INTERFACE, iface.getSimpleName()));
	}
}
