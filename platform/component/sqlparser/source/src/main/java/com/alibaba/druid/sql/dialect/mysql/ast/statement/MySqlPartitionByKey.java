/*
 * Copyright 1999-2011 Alibaba Group Holding Ltd.
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
package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLPartitioningClause;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlObjectImpl;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlPartitionByKey extends MySqlObjectImpl implements SQLPartitioningClause {

    private static final long serialVersionUID = 1L;

    private List<SQLName>     columns          = new ArrayList<SQLName>();

    private SQLExpr           partitionCount;


    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, columns);
            acceptChild(visitor, partitionCount);
        }
        visitor.endVisit(this);
    }

    public SQLExpr getPartitionCount() {
        return partitionCount;
    }

    public void setPartitionCount(SQLExpr partitionCount) {
        this.partitionCount = partitionCount;
    }

    public List<SQLName> getColumns() {
        return columns;
    }

    public void setColumns(List<SQLName> columns) {
        this.columns = columns;
    }

}
