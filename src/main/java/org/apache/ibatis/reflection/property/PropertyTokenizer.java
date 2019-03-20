/**
 * Copyright 2009-2017 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.ibatis.reflection.property;

import java.util.Iterator;

/**
 * @author Clinton Begin
 * 用于解析由 . [] 组成的表达式
 * orders[0].items[0].name
 */
public class PropertyTokenizer implements Iterator<PropertyTokenizer> {
    //当前表达式的名字
    private String name;
    //当前表达式的索引
    private final String indexedName;
    //索引下标
    private String index;
    //子表达式
    private final String children;

    public PropertyTokenizer(String fullname) {
        //查找.的位置
        int delim = fullname.indexOf('.');
        if (delim > -1) {
            name = fullname.substring(0, delim);
            //子表达式
            children = fullname.substring(delim + 1);
        } else {
            //如果不存在.那么就也不存在子表达式
            name = fullname;
            children = null;
        }
        indexedName = name;
        //如果包含[]
        delim = name.indexOf('[');
        if (delim > -1) {
            //获取索引位置
            index = name.substring(delim + 1, name.length() - 1);
            //设置当前表达式的名字
            name = name.substring(0, delim);
        }
    }

    public String getName() {
        return name;
    }

    public String getIndex() {
        return index;
    }

    public String getIndexedName() {
        return indexedName;
    }

    public String getChildren() {
        return children;
    }

    @Override
    public boolean hasNext() {
        return children != null;
    }

    @Override
    public PropertyTokenizer next() {
        return new PropertyTokenizer(children);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove is not supported, as it has no meaning in the context of properties.");
    }
}
