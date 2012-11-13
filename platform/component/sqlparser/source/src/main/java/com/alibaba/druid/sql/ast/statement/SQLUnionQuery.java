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
package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public class SQLUnionQuery extends SQLSelectQuery {

    private static final long serialVersionUID = 1L;

    private SQLSelectQuery    left;
    private SQLSelectQuery    right;
    private SQLUnionOperator  operator         = SQLUnionOperator.UNION;
    private SQLOrderBy        orderBy;

    public SQLUnionOperator getOperator() {
        return operator;
    }

    public void setOperator(SQLUnionOperator operator) {
        this.operator = operator;
    }

    public SQLUnionQuery(){

    }

    public SQLSelectQuery getLeft() {
        return left;
    }

    public void setLeft(SQLSelectQuery left) {
        this.left = left;
    }

    public SQLSelectQuery getRight() {
        return right;
    }

    public void setRight(SQLSelectQuery right) {
        this.right = right;
    }

    public SQLOrderBy getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(SQLOrderBy orderBy) {
        this.orderBy = orderBy;
    }


    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, left);
            acceptChild(visitor, right);
            acceptChild(visitor, orderBy);
        }
        visitor.endVisit(this);
    }

}