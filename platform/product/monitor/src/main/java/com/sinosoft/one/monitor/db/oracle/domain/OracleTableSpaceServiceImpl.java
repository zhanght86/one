package com.sinosoft.one.monitor.db.oracle.domain;

import com.sinosoft.one.monitor.db.oracle.model.Info;
import com.sinosoft.one.monitor.db.oracle.model.OracleDetailModel;
import com.sinosoft.one.monitor.db.oracle.model.OracleTableSpaceModel;
import com.sinosoft.one.monitor.db.oracle.monitorSql.OracleMonitorSql;
import com.sinosoft.one.monitor.db.oracle.repository.InfoRepository;
import com.sinosoft.one.monitor.db.oracle.utils.DBUtil4Monitor;
import com.sinosoft.one.monitor.db.oracle.utils.db.DBUtil;
import com.sinosoft.one.monitor.db.oracle.utils.db.SqlObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * User: Chunliang.Han
 * Date: 13-3-1
 * Time: 下午6:10
 */
@Component
public class OracleTableSpaceServiceImpl implements OracleTableSpaceService {
    @Autowired
    private InfoRepository infoRepository;

    @Override
    public List<OracleTableSpaceModel> listTableSpaceInfo(String monitorId) {
        DBUtil4Monitor.changeConnection(monitorId);
        String sql = OracleMonitorSql.tableSpaceInfo;
        List<OracleTableSpaceModel> rsList = DBUtil.queryBeans(SqlObj.newInstance(sql), OracleTableSpaceModel.class);
        return rsList;
    }
}
