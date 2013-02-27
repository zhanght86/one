package com.sinosoft.one.ams.repositories;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.sinosoft.one.ams.model.TaskAuth;
import com.sinosoft.one.data.jade.annotation.SQL;

public interface GeRmsTaskAuthRepository extends PagingAndSortingRepository<TaskAuth, String>{
	
	//根据机构ID查询出功能ID
	@SQL("select taskId from GE_RMS_TASK_AUTH where comcode in (?1 ,'*')")
	List<String> findAllTaskIdByComCode(String comCode);
	
	//根据角色id查询角色关联的授权
	@SQL("select taskid from GE_RMS_TASK_AUTH where taskAuthID in(select taskAuthID from ge_rms_roletask  where roleid in (?1) and isValidate='1')")
	List<String> findTaskAuthByRole(List<String> roleIds);
	
	//查询已授权可用功能
	@SQL("select * from GE_RMS_TASK_AUTH where comCode in ('*',?1) and taskID in (?2)")
	List<TaskAuth> findTaskAuthByComCode(String comCode,List<String> taskids);
	
	//根据机构ID和功能ID查出taskAuthID
	@SQL("select taskAuthID from GE_RMS_TASK_AUTH where comCode = ?1 and taskId = ?2")
	String findTaskAuthIdByComCodeTaskId(String comCode , String taskId);
	
	//根据taskId查询出taskAuthID
	@SQL("select taskAuthID from GE_RMS_TASK_AUTH where taskId = ?1")
	List<String> findTaskAuthIDByTaskId(String taskId);
	
	//根据taskAuthId查询出功能ID
	@SQL("select taskID from GE_RMS_TASK_AUTH where taskAuthId = ?1 and comCode = ?2")
	String findTaskIdByTaskAuthId(String taskAuthId ,String comCode);
	
	//根据taskAuthId删除多个授权
	@SQL("delete from GE_RMS_TASK_AUTH where taskAuthId in (?1)")
	void deleteTaskAuthByIds(List<String> taskAuthids);
	
	@SQL("update GE_RMS_TASK_AUTH t set t.comcode='*' where (t.comcode=?1 or t.comcode='*' )")
	void updataTaskAuth(String loginComCode);
	
	//根据删除多个不需要的授权
	@SQL("delete from GE_RMS_TASK_AUTH where comcode !=")
	void deleteTaskAuthNotEq(String comCode);
}
