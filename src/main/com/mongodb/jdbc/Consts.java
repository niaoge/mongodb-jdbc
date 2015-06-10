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

import com.helpinput.utils.Utils;


public class Consts {
	public static final String MONGODB_ID = "_id";
	
	public static final String MONGODB_SET = "$set";
	
	public static boolean isJdbcName(String sqlName) {
		if (Utils.hasLength(sqlName)) {
			char left = sqlName.charAt(0);
			if (left == '[') {
				char right = sqlName.charAt(sqlName.length() - 1);
				return (right == ']');
			}
			else if (left == '`') {
				char right = sqlName.charAt(sqlName.length() - 1);
				return (right == '`');
			}
		}
		return false;
	}
	
}
