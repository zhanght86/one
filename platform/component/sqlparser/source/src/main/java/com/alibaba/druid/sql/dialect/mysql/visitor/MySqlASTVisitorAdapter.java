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
package com.alibaba.druid.sql.dialect.mysql.visitor;

import com.alibaba.druid.sql.dialect.mysql.ast.MySqlForceIndexHint;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlIgnoreIndexHint;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlKey;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlPrimaryKey;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlUseIndexHint;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlBinaryExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlBooleanExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlCharExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlExtractExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlIntervalExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlMatchAgainstExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlOutFileExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlUserName;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.CobarShowStatus;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableAddColumn;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableAddIndex;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableAddUnique;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableChangeColumn;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableCharacter;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableOption;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlBinlogStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCommitStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateIndexStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateUserStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateUserStatement.UserSpecification;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDeleteStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDescribeStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDropTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDropUser;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDropViewStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlExecuteStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlHelpStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlKillStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlLoadDataInFileStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlLoadXmlStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlLockTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlPartitionByKey;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlPrepareStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlRenameTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlReplaceStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlResetStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlRollbackStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectGroupBy;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock.Limit;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSetCharSetStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSetNamesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSetTransactionIsolationLevelStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowAuthorsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowBinLogEventsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowBinaryLogsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowCharacterSetStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowCollationStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowColumnsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowContributorsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowCreateDatabaseStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowCreateEventStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowCreateFunctionStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowCreateProcedureStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowCreateTriggerStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowCreateViewStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowDatabasesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowEngineStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowEnginesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowErrorsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowEventsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowFunctionCodeStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowFunctionStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowGrantsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowIndexesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowKeysStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowMasterLogsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowMasterStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowOpenTablesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowPluginsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowPrivilegesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowProcedureCodeStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowProcedureStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowProcessListStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowProfileStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowProfilesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowRelayLogEventsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowSlaveHostsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowSlaveStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowTableStatusStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowTablesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowTriggersStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowVariantsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowWarningsStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlStartTransactionStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlTableIndex;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUnionQuery;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUnlockTablesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.alibaba.druid.sql.visitor.SQLASTVisitorAdapter;

public class MySqlASTVisitorAdapter extends SQLASTVisitorAdapter implements
		MySqlASTVisitor {


	public boolean visit(MySqlBooleanExpr x) {
		return true;
	}


	public void endVisit(MySqlBooleanExpr x) {

	}


	public boolean visit(Limit x) {
		return true;
	}


	public void endVisit(Limit x) {

	}


	public boolean visit(MySqlTableIndex x) {
		return true;
	}


	public void endVisit(MySqlTableIndex x) {

	}


	public boolean visit(MySqlKey x) {
		return true;
	}


	public void endVisit(MySqlKey x) {

	}


	public boolean visit(MySqlPrimaryKey x) {

		return true;
	}


	public void endVisit(MySqlPrimaryKey x) {

	}


	public void endVisit(MySqlIntervalExpr x) {

	}


	public boolean visit(MySqlIntervalExpr x) {

		return true;
	}


	public void endVisit(MySqlExtractExpr x) {

	}


	public boolean visit(MySqlExtractExpr x) {

		return true;
	}


	public void endVisit(MySqlMatchAgainstExpr x) {

	}


	public boolean visit(MySqlMatchAgainstExpr x) {

		return true;
	}


	public void endVisit(MySqlBinaryExpr x) {

	}


	public boolean visit(MySqlBinaryExpr x) {

		return true;
	}


	public void endVisit(MySqlPrepareStatement x) {

	}


	public boolean visit(MySqlPrepareStatement x) {

		return true;
	}


	public void endVisit(MySqlExecuteStatement x) {

	}


	public boolean visit(MySqlExecuteStatement x) {

		return true;
	}


	public void endVisit(MySqlDeleteStatement x) {

	}


	public boolean visit(MySqlDeleteStatement x) {

		return true;
	}


	public void endVisit(MySqlInsertStatement x) {

	}


	public boolean visit(MySqlInsertStatement x) {

		return true;
	}


	public void endVisit(MySqlLoadDataInFileStatement x) {

	}


	public boolean visit(MySqlLoadDataInFileStatement x) {

		return true;
	}


	public void endVisit(MySqlLoadXmlStatement x) {

	}


	public boolean visit(MySqlLoadXmlStatement x) {

		return true;
	}


	public void endVisit(MySqlReplaceStatement x) {

	}


	public boolean visit(MySqlReplaceStatement x) {

		return true;
	}


	public void endVisit(MySqlSelectGroupBy x) {

	}


	public boolean visit(MySqlSelectGroupBy x) {

		return true;
	}


	public void endVisit(MySqlStartTransactionStatement x) {

	}


	public boolean visit(MySqlStartTransactionStatement x) {

		return true;
	}


	public void endVisit(MySqlCommitStatement x) {

	}


	public boolean visit(MySqlCommitStatement x) {

		return true;
	}


	public void endVisit(MySqlRollbackStatement x) {

	}


	public boolean visit(MySqlRollbackStatement x) {

		return true;
	}


	public void endVisit(MySqlShowColumnsStatement x) {

	}


	public boolean visit(MySqlShowColumnsStatement x) {

		return true;
	}


	public void endVisit(MySqlShowTablesStatement x) {

	}


	public boolean visit(MySqlShowTablesStatement x) {

		return true;
	}


	public void endVisit(MySqlShowDatabasesStatement x) {

	}


	public boolean visit(MySqlShowDatabasesStatement x) {

		return true;
	}


	public void endVisit(MySqlShowWarningsStatement x) {

	}


	public boolean visit(MySqlShowWarningsStatement x) {

		return true;
	}


	public void endVisit(MySqlShowStatusStatement x) {

	}


	public boolean visit(MySqlShowStatusStatement x) {

		return true;
	}


	public void endVisit(CobarShowStatus x) {

	}


	public boolean visit(CobarShowStatus x) {
		return true;
	}


	public void endVisit(MySqlKillStatement x) {

	}


	public boolean visit(MySqlKillStatement x) {
		return true;
	}


	public void endVisit(MySqlBinlogStatement x) {

	}


	public boolean visit(MySqlBinlogStatement x) {
		return true;
	}
	

	public void endVisit(MySqlResetStatement x) {
		
	}
	

	public boolean visit(MySqlResetStatement x) {
		return true;
	}


    public void endVisit(MySqlCreateUserStatement x) {
        
    }


    public boolean visit(MySqlCreateUserStatement x) {
        return true;
    }


    public void endVisit(UserSpecification x) {
        
    }


    public boolean visit(UserSpecification x) {
        return true;
    }
    

    public void endVisit(MySqlDropUser x) {
        
    }
    

    public boolean visit(MySqlDropUser x) {
        return true;
    }
    

    public void endVisit(MySqlDropTableStatement x) {
        
    }
    

    public boolean visit(MySqlDropTableStatement x) {
        return true;
    }


    public void endVisit(MySqlPartitionByKey x) {
        
    }


    public boolean visit(MySqlPartitionByKey x) {
        return true;
    }


    public boolean visit(MySqlSelectQueryBlock x) {
        return true;
    }


    public void endVisit(MySqlSelectQueryBlock x) {
        
    }


    public boolean visit(MySqlOutFileExpr x) {
        return true;
    }


    public void endVisit(MySqlOutFileExpr x) {
        
    }


    public boolean visit(MySqlDescribeStatement x) {
        return true;
    }


    public void endVisit(MySqlDescribeStatement x) {
        
    }


	public boolean visit(MySqlUpdateStatement x) {
		return true;
	}


	public void endVisit(MySqlUpdateStatement x) {
		
	}


    public boolean visit(MySqlSetTransactionIsolationLevelStatement x) {
        return true;
    }


    public void endVisit(MySqlSetTransactionIsolationLevelStatement x) {
        
    }
    

    public boolean visit(MySqlSetNamesStatement x) {
        return true;
    }
    

    public void endVisit(MySqlSetNamesStatement x) {
        
    }
    

    public boolean visit(MySqlSetCharSetStatement x) {
        return true;
    }
    

    public void endVisit(MySqlSetCharSetStatement x) {
        
    }
    

    public boolean visit(MySqlShowAuthorsStatement x) {
        return true;
    }
    

    public void endVisit(MySqlShowAuthorsStatement x) {
        
    }
    

    public boolean visit(MySqlShowBinaryLogsStatement x) {
        return true;
    }
    

    public void endVisit(MySqlShowBinaryLogsStatement x) {
        
    }
    

    public boolean visit(MySqlShowMasterLogsStatement x) {
        return true;
    }
    

    public void endVisit(MySqlShowMasterLogsStatement x) {
        
    }
    

    public boolean visit(MySqlShowCollationStatement x) {
        return true;
    }
    

    public void endVisit(MySqlShowCollationStatement x) {
        
    }
    

    public boolean visit(MySqlShowBinLogEventsStatement x) {
        return true;
    }
    

    public void endVisit(MySqlShowBinLogEventsStatement x) {
        
    }


    public boolean visit(MySqlShowCharacterSetStatement x) {
        return true;
    }


    public void endVisit(MySqlShowCharacterSetStatement x) {
        
    }
    

    public boolean visit(MySqlShowContributorsStatement x) {
        return true;
    }
    

    public void endVisit(MySqlShowContributorsStatement x) {
        
    }
    

    public boolean visit(MySqlShowCreateDatabaseStatement x) {
        return true;
    }
    

    public void endVisit(MySqlShowCreateDatabaseStatement x) {
        
    }
    

    public boolean visit(MySqlShowCreateEventStatement x) {
        return true;
    }
    

    public void endVisit(MySqlShowCreateEventStatement x) {
        
    }
    

    public boolean visit(MySqlShowCreateFunctionStatement x) {
        return true;
    }
    

    public void endVisit(MySqlShowCreateFunctionStatement x) {
        
    }


    public boolean visit(MySqlShowCreateProcedureStatement x) {
        return true;
    }


    public void endVisit(MySqlShowCreateProcedureStatement x) {
        
    }
    

    public boolean visit(MySqlShowCreateTableStatement x) {
        return true;
    }
    

    public void endVisit(MySqlShowCreateTableStatement x) {
        
    }
    

    public boolean visit(MySqlShowCreateTriggerStatement x) {
        return true;
    }
    

    public void endVisit(MySqlShowCreateTriggerStatement x) {
        
    }


    public boolean visit(MySqlShowCreateViewStatement x) {
        return true;
    }


    public void endVisit(MySqlShowCreateViewStatement x) {
        
    }


    public boolean visit(MySqlShowEngineStatement x) {
        return true;
    }


    public void endVisit(MySqlShowEngineStatement x) {
        
    }
    

    public boolean visit(MySqlShowEnginesStatement x) {
        return true;
    }
    

    public void endVisit(MySqlShowEnginesStatement x) {
        
    }


    public boolean visit(MySqlShowErrorsStatement x) {
        return true;
    }


    public void endVisit(MySqlShowErrorsStatement x) {
        
    }
    

    public boolean visit(MySqlShowEventsStatement x) {
        return true;
    }
    

    public void endVisit(MySqlShowEventsStatement x) {
        
    }


    public boolean visit(MySqlShowFunctionCodeStatement x) {
        return true;
    }


    public void endVisit(MySqlShowFunctionCodeStatement x) {
        
    }


    public boolean visit(MySqlShowFunctionStatusStatement x) {
        return true;
    }


    public void endVisit(MySqlShowFunctionStatusStatement x) {
        
    }


    public boolean visit(MySqlShowGrantsStatement x) {
        return true;
    }


    public void endVisit(MySqlShowGrantsStatement x) {
    }


    public boolean visit(MySqlUserName x) {
        return true;
    }


    public void endVisit(MySqlUserName x) {
        
    }


    public boolean visit(MySqlShowIndexesStatement x) {
        return true;
    }


    public void endVisit(MySqlShowIndexesStatement x) {
        
    }


    public boolean visit(MySqlShowKeysStatement x) {
        return true;
    }


    public void endVisit(MySqlShowKeysStatement x) {
        
    }
    

    public boolean visit(MySqlShowMasterStatusStatement x) {
        return true;
    }
    

    public void endVisit(MySqlShowMasterStatusStatement x) {
        
    }


    public boolean visit(MySqlShowOpenTablesStatement x) {
        return true;
    }


    public void endVisit(MySqlShowOpenTablesStatement x) {
        
    }


    public boolean visit(MySqlShowPluginsStatement x) {
        return true;
    }


    public void endVisit(MySqlShowPluginsStatement x) {
        
    }
    

    public boolean visit(MySqlShowPrivilegesStatement x) {
        return true;
    }
    

    public void endVisit(MySqlShowPrivilegesStatement x) {
        
    }


    public boolean visit(MySqlShowProcedureCodeStatement x) {
        return true;
    }


    public void endVisit(MySqlShowProcedureCodeStatement x) {
        
    }


    public boolean visit(MySqlShowProcedureStatusStatement x) {
        return true;
    }


    public void endVisit(MySqlShowProcedureStatusStatement x) {
        
    }


    public boolean visit(MySqlShowProcessListStatement x) {
        return true;
    }


    public void endVisit(MySqlShowProcessListStatement x) {
        
    }


    public boolean visit(MySqlShowProfileStatement x) {
        return true;
    }


    public void endVisit(MySqlShowProfileStatement x) {
        
    }


    public boolean visit(MySqlShowProfilesStatement x) {
        return true;
    }


    public void endVisit(MySqlShowProfilesStatement x) {
        
    }


    public boolean visit(MySqlShowRelayLogEventsStatement x) {
        return true;
    }


    public void endVisit(MySqlShowRelayLogEventsStatement x) {
        
    }


    public boolean visit(MySqlShowSlaveHostsStatement x) {
        return true;
    }


    public void endVisit(MySqlShowSlaveHostsStatement x) {
        
    }
    

    public boolean visit(MySqlShowSlaveStatusStatement x) {
        return true;
    }
    

    public void endVisit(MySqlShowSlaveStatusStatement x) {
        
    }


    public boolean visit(MySqlShowTableStatusStatement x) {
        return true;
    }


    public void endVisit(MySqlShowTableStatusStatement x) {
        
    }


    public boolean visit(MySqlShowTriggersStatement x) {
        return true;
    }


    public void endVisit(MySqlShowTriggersStatement x) {
        
    }


    public boolean visit(MySqlShowVariantsStatement x) {
        return true;
    }


    public void endVisit(MySqlShowVariantsStatement x) {
        
    }


    public boolean visit(MySqlAlterTableStatement x) {
        return true;
    }


    public void endVisit(MySqlAlterTableStatement x) {
        
    }


    public boolean visit(MySqlAlterTableAddColumn x) {
        return true;
    }


    public void endVisit(MySqlAlterTableAddColumn x) {
        
    }
    

    public boolean visit(MySqlCreateIndexStatement x) {
        return true;
    }
    

    public void endVisit(MySqlCreateIndexStatement x) {
        
    }


    public boolean visit(MySqlRenameTableStatement.Item x) {
        return true;
    }


    public void endVisit(MySqlRenameTableStatement.Item x) {
        
    }


    public boolean visit(MySqlRenameTableStatement x) {
        return true;
    }


    public void endVisit(MySqlRenameTableStatement x) {
        
    }


    public boolean visit(MySqlDropViewStatement x) {
        return true;
    }


    public void endVisit(MySqlDropViewStatement x) {
        
    }


    public boolean visit(MySqlUnionQuery x) {
        return true;
    }


    public void endVisit(MySqlUnionQuery x) {
        
    }


    public boolean visit(MySqlUseIndexHint x) {
        return true;
    }


    public void endVisit(MySqlUseIndexHint x) {
        
    }


    public boolean visit(MySqlIgnoreIndexHint x) {
        return true;
    }


    public void endVisit(MySqlIgnoreIndexHint x) {
        
    }


    public boolean visit(MySqlLockTableStatement x) {
        return true;
    }


    public void endVisit(MySqlLockTableStatement x) {
        
    }


    public boolean visit(MySqlUnlockTablesStatement x) {
        return true;
    }


    public void endVisit(MySqlUnlockTablesStatement x) {
        
    }


    public boolean visit(MySqlForceIndexHint x) {
        return true;
    }


    public void endVisit(MySqlForceIndexHint x) {
        
    }


    public boolean visit(MySqlAlterTableChangeColumn x) {
        return true;
    }


    public void endVisit(MySqlAlterTableChangeColumn x) {
        
    }


    public boolean visit(MySqlAlterTableCharacter x) {
        return true;
    }


    public void endVisit(MySqlAlterTableCharacter x) {
        
    }


    public boolean visit(MySqlAlterTableAddIndex x) {
        return true;
    }


    public void endVisit(MySqlAlterTableAddIndex x) {
        
    }


    public boolean visit(MySqlAlterTableOption x) {
        return true;
    }


    public void endVisit(MySqlAlterTableOption x) {
        
    }


    public boolean visit(MySqlCreateTableStatement x) {
        return true;
    }


    public void endVisit(MySqlCreateTableStatement x) {
        
    }


    public boolean visit(MySqlHelpStatement x) {
        return true;
    }


    public void endVisit(MySqlHelpStatement x) {
        
    }


    public boolean visit(MySqlCharExpr x) {
        return true;
    }


    public void endVisit(MySqlCharExpr x) {
        
    }


    public boolean visit(MySqlAlterTableAddUnique x) {
        return true;
    }


    public void endVisit(MySqlAlterTableAddUnique x) {
        
    }
}
