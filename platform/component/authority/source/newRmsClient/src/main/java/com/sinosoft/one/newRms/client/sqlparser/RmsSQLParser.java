package com.sinosoft.one.newRms.client.sqlparser;

import java.sql.Connection;

import org.springframework.stereotype.Service;

@Service
public interface RmsSQLParser {
	 public  String parser(Connection connection, String sql)throws Exception;
}
