package com.sinosoft.one.ams.service.facade;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import com.sinosoft.one.ams.model.Role;
import com.sinosoft.one.ams.model.Task;
import com.sinosoft.one.ams.utils.uiutil.Gridable;


@Service
public interface RoleService {
	
	
	//查询角色信息
	public Role findRoleById(String roleId);
	
	//根据角色ID查询角色关联的功能
	public List<Task> findTaskByRole(String roleId);
	
	//根据机构查询所有可用的功能
	public List<Task> findTaskByComCode(String comCode);
	
	//查询机构下所有可见的角色
	public Gridable<Role> getGridable(Gridable<Role> gridable,String comCode,String name,Pageable pageable);
	
	//更新角色
	public void updateRole(String roleid,String comCode,String userCode,String name,String des,String roleTpe,List<String> taskids);
	
	public void addRole(String comCode,String userCode,String name,String des,String roleTpe,List<String> taskids);
	
	public void deleteRole(String roleId, String comCode);
}
