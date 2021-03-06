package com.sinosoft.one.rms.service.spring;

import ins.framework.common.QueryRule;
import ins.framework.dao.GenericDaoHibernate;
import ins.framework.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.sinosoft.one.rms.DataPower;
import com.sinosoft.one.rms.Menu;
import com.sinosoft.one.rms.User;
import com.sinosoft.one.rms.model.BusPower;
import com.sinosoft.one.rms.model.Company;
import com.sinosoft.one.rms.model.Employe;
import com.sinosoft.one.rms.model.ExcPower;
import com.sinosoft.one.rms.model.Group;
import com.sinosoft.one.rms.model.GroupRole;
import com.sinosoft.one.rms.model.Role;
import com.sinosoft.one.rms.model.RoleDesignate;
import com.sinosoft.one.rms.model.Task;
import com.sinosoft.one.rms.model.UserGroup;
import com.sinosoft.one.rms.model.UserPower;
import com.sinosoft.one.rms.model.service.CompanyModelInterface;
import com.sinosoft.one.rms.model.service.EmployeModelInterface;
import com.sinosoft.one.rms.service.facade.GetUserService;
import com.sinosoft.one.rms.service.facade.CompanyService;
import com.sinosoft.one.rms.service.facade.EmployeService;

@Service
public class GetUserServiceSpringImpl extends
		GenericDaoHibernate<Employe, String> implements GetUserService {

	@Autowired
	private EmployeService employeServiceInterface;
	
	@Autowired
	private CompanyService companyServiceInterface;
	
	// private RmsService rmsService;
	// private RoleService roleService;
	// private GroupService groupService;

	/**
	 * 创建USER对象
	 */
	@SuppressWarnings("unused")
	public User getUserByUserCodeComCode(String userCode, String comCode,String sysFlag) {
		try{
			
		
		EmployeModelInterface employe = findUserByCode(userCode);
		Assert.notNull(employe);
		UserPower userPower = findUserPowerByComUser(userCode, comCode);
		Assert.notNull(userPower);
		CompanyModelInterface company = findCompanyByComCode(comCode);
		Assert.notNull(company);
		List<Group> grouplist = findGroupByUser(userCode);
		List<String> groupIdList = new ArrayList<String>();
		for (Group group : grouplist) {
			groupIdList.add(group.getGroupID());
		}
		Set<Role> roles = findRoleByGroup(groupIdList, comCode);
		List<String> roleIdList = new ArrayList<String>();
		for (Role role : roles) {
			roleIdList.add(role.getRoleID());
		}
		List<Task> tasklist = findTaskByUserCode(userCode, comCode, sysFlag);
		List<String> taskIdList = new ArrayList<String>();
		for (Task task : tasklist) {
			taskIdList.add(task.getTaskID());
		}

		List<Menu> menulist = new ArrayList<Menu>();
		List<Task> topList = new ArrayList<Task>();
//		Map<String, Task> filter = new HashMap<String, Task>();
//		List<Task> filter = new ArrayList<Task>();
		for (Task task : tasklist) {
			if (StringUtils.isNotBlank(task.getIsAsMenu())&& task.getIsAsMenu().toString().equals("1")) {
//				filter.add( task);
				if (task.getParent() == null) {
					topList.add(task);
				}
			}
		}
		craeteList(topList, menulist, tasklist);
		List<DataPower> dataPowers = new ArrayList<DataPower>();
		creatDataPowerList(dataPowers,userPower.getBusPowers(),userCode,comCode);
		String passWord="";
		if(employe.getPassword()!=null&&!employe.getPassword().equals("")){
			passWord=employe.getPassword();
		}
//		System.out.println("用户的密码："+employe.getPassword()+"        用户代码："+employe.getUserCode());
		return new User(employe.getUserCode(),employe.getPassword(),
				employe.getUserName(), company.getComCode(),
				company.getComCName(), roleIdList, taskIdList, menulist, dataPowers);
		 } catch (Exception e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		}
		 return null;
		}

	

//----------------------------------------------------------------------------------------------------//

	EmployeModelInterface findUserByCode(String userCode) {
		Assert.hasText(userCode);
		return employeServiceInterface.findUserByCode(userCode);
	}

	/**
	 * 根据用户代码机构代码查询权限
	 * 
	 * @param userCode
	 * @param comCode
	 * @return
	 */
	UserPower findUserPowerByComUser(String userCode, String comCode) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("userCode", userCode);
		queryRule.addEqual("comCode", comCode);
		queryRule.addEqual("isValidate", "1");
		return super.findUnique(UserPower.class, queryRule);
	}

	CompanyModelInterface findCompanyByComCode(String comCode) {
		Assert.hasText(comCode);
		return companyServiceInterface.findCompanyByComCode(comCode);
	}

	List<Group> findGroupByUser(String userCode) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("userCode", userCode);
		queryRule.addEqual("isValidate", "1");
		List<UserPower> userPowers = new ArrayList<UserPower>();
		userPowers = super.find(UserPower.class, queryRule);
		List<Group> groups = new ArrayList<Group>();
		if (userPowers != null) {
			for (UserPower userPower : userPowers) {
				List<UserGroup> userGroups = userPower.getUserGroups();
				if (userGroups != null) {
					for (UserGroup userGroup : userGroups) {
						groups.add(userGroup.getGroup());
					}
				}
			}
		}
		return groups;
	}

	/**
	 * 根据用户组查询关联的角色
	 */
	Set<Role> findRoleByGroup(List<String> groupIDs, String comCode) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addIn("groupID", groupIDs);
		List<Group> groups = super.find(Group.class, queryRule);
		if (groups == null) {
			// 异常
		}
		Set<GroupRole> groupRoles = new HashSet<GroupRole>();
		for (Group group : groups) {
			groupRoles.addAll(group.getGroupRoles());
		}
		// 根据机构获得指派的信息 取得roleID 过滤用户组关联的角色
		List<RoleDesignate> roleDesignates = new ArrayList<RoleDesignate>();
		if (StringUtils.isNotBlank(groupIDs.get(0))) {
			QueryRule queryRole = QueryRule.getInstance();
			List<String> comCodes = new ArrayList<String>();
			comCodes.add(comCode);
			// 查询*的（所有机构可见类型的角色）
			comCodes.add("*");
			queryRole.addIn("id.comCode", comCodes);
			roleDesignates = super.find(RoleDesignate.class, queryRole);
		}
		List<String> roleids = new ArrayList<String>();
		Set<Role> roles = new HashSet<Role>();
		for (RoleDesignate roleDesignate : roleDesignates) {
			roleids.add(roleDesignate.getId().getRoleID());
		}
//		// 根据指派获得的ID过滤用户组角色 获得角色集合
		for (GroupRole groupRole : groupRoles) {
			for (String roleid : roleids) {
				if (groupRole.getRole().getRoleID().toString()
						.equals(roleid.toString())) {
					roles.add(groupRole.getRole());
				}
			}
		}
		return roles;
	}

	/**
	 * 获得员工在机构下有效权限集合
	 */
	 List<Task> findTaskByUserCode(String userCode, String comCode,String sysFlag) {
		List<Task> userTasksResult = new ArrayList<Task>();
		UserPower userPower = new UserPower();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("userCode", userCode);
		queryRule.addEqual("comCode", comCode);
		queryRule.addEqual("isValidate", "1");
		userPower = super.findUnique(UserPower.class, queryRule);
		List<UserGroup> userGroups = new ArrayList<UserGroup>();
		if (userPower != null) {
			userGroups = userPower.getUserGroups();
			List<Group> groups = new ArrayList<Group>();
			for (UserGroup usergroup : userGroups) {
				// 用户组的过滤
				if (usergroup.getIsValidate().toString().equals("1".toString())) {
					groups.add(usergroup.getGroup());
				}
			}
			List<GroupRole> groupRoles = new ArrayList<GroupRole>();
			for (Group group : groups) {
				groupRoles.addAll(group.getGroupRoles());
			}
			// 根据机构获得指派的信息 取得roleID 过滤用户组关联的角色
			List<RoleDesignate> roleDesignates = new ArrayList<RoleDesignate>();
			QueryRule queryRole = QueryRule.getInstance();
			queryRole.addIn("id.comCode", comCode,"*");
			roleDesignates = super.find(RoleDesignate.class, queryRole);
			// 根据获得的指派角色信息获得角色ID
			List<String> roleids = new ArrayList<String>();
			for (RoleDesignate roleDesignate : roleDesignates) {
				for (GroupRole groupRole : groupRoles) {
					if (roleDesignate.getId().getRoleID().toString()
							.equals(groupRole.getRole().getRoleID().toString())) {
						roleids.add(roleDesignate.getId().getRoleID());
					}
				}
			}
			StringBuffer taskIDSQL = new StringBuffer();
			List<String> taskIDs = new ArrayList<String>();
			if (roleids.size() > 0) {
				// 根据角色ID获得 角色关联的功能id集合
				taskIDSQL
						.append(" select taskid from ge_rms_task_auth where taskauthid in (");
				taskIDSQL
						.append(" select taskauthid from ge_rms_roletask g where g.isvalidate='1' ");
				taskIDSQL.append("and g.roleid in (");
				for (String string : roleids) {
					taskIDSQL.append(" '" + string + "',");
				}
				taskIDSQL.delete(taskIDSQL.length() - 1, taskIDSQL.length());
				taskIDSQL.append(")");
				taskIDSQL.append(")");
				taskIDs = (List<String>) getSession().createSQLQuery(
						taskIDSQL.toString()).list();
			}
			taskIDSQL.setLength(0);
			// 获取该机构的功能ID
			taskIDSQL.append("select taskid from ge_rms_task_auth where comcode='"+ comCode + "' or comcode='*'");
			List<String> comtaskIDs = new ArrayList<String>();
			comtaskIDs = (List<String>) getSession().createSQLQuery(
					taskIDSQL.toString()).list();
			List<String> resultTaskIDs = new ArrayList<String>();
			// 两功能ID集合去重
			for (String com_TaskID : comtaskIDs) {
				for (String role_TaskID : taskIDs) {
					if (com_TaskID.toString().equals(role_TaskID.toString())) {
						resultTaskIDs.add(com_TaskID);
						break;
					}
				}
			}
			// 获得功能对象
			Set<Task> tasks = new HashSet<Task>();
			if (resultTaskIDs.size() > 0) {
				StringBuffer hql=new StringBuffer();
				hql.append("from Task where taskID in(");
				for (String string : resultTaskIDs) {
					hql.append(" '" + string + "',");
				}
				hql.delete(hql.length() - 1, hql.length());
				hql.append(")");
				hql.append(" and isValidate='1'");
				if(StringUtils.isNotBlank(sysFlag)){
					hql.append(" and (sysFlag='"+sysFlag+"' or sysFlag='RMS')");
				}else{
					hql.append(" and sysFlag='RMS'");
				}
				tasks.addAll(super.findByHql(hql.toString()));
			}
			// 获得除外权限
			Set<Task> excTasks = new HashSet<Task>();
			if (userPower != null) {
				for (ExcPower excpower : userPower.getExcPowers()) {
					excTasks.add(excpower.getTask());
				}
			}
			Set<Task> result = new HashSet<Task>();
			// 除外权限和获得的权限过滤
			for (Task task : tasks) {
				int i = 0;
				for (Task etask : excTasks) {
					if (task.getTaskID().toString()
							.equals(etask.getTaskID().toString()))
						i = i + 1;
				}
				if (i == 0) {
					result.add(task);
				}
			}
			Set<Task> ts = new HashSet<Task>();
			iterateTask(ts, result);
			userTasksResult = taskArrange(ts);
			return userTasksResult;
		} else {
			return userTasksResult;
		}
	}

	/**
	 * 循环获得父节点功能
	 */
	void iterateTask(Set<Task> tsks, Set<Task> tasks) {
		for (Task task : tasks) {
			this.getSuppTask(tsks, task);
		}
	}

	void getSuppTask(Set<Task> result, Task task) {
		if (task.getIsValidate().toString().equals("1")) {
			result.add(task);
		}
		if (task.getParent() != null) {
			getSuppTask(result, task.getParent());
		}
	}

	// 对功能集合排序
	List<Task> taskArrange(Set<Task> ts) {
		List<String> ids = new ArrayList<String>();
		for (Task task : ts) {
			ids.add(task.getTaskID());
		}
		List<Task> tasks = new ArrayList<Task>();
		if (ids.size() > 0) {
			QueryRule queryRule = QueryRule.getInstance();
			queryRule.addIn("taskID", ids);
			queryRule.addAscOrder("taskID");
			tasks = super.find(Task.class, queryRule);
		}
		return tasks;
	}

	/**
	 * 根据用户代码 获取机构列表(引入机构,登陆时已引入机构)
	 */
	List<Company> findComByUserCode(String userCode) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("userCode", userCode);
		List<UserPower> userPower = super.find(UserPower.class, queryRule);
		if (userPower == null || userPower.isEmpty()) {
			// 异常
		}
		List<String> comCodes = new ArrayList<String>();
		for (Iterator<UserPower> iter = userPower.iterator(); iter.hasNext();) {
			comCodes.add(iter.next().getComCode());
		}
		List<Company> companies = new ArrayList<Company>();
		if (comCodes.size() > 0) {
			QueryRule queryRuleComcode = QueryRule.getInstance();
			queryRuleComcode.addIn("comCode", comCodes);
			companies = super.find(Company.class, queryRuleComcode);
		}
		// QueryRule queryComCode = QueryRule.getInstance();
		// queryComCode.addIn("comCode", comCodes);
		return companies;
	}

	
	/**
	 * 组织树状数据
	 * @param list
	 * @param dest
	 * @param filter
	 */
	 void craeteList(List<Task> list, List<Menu> dest,
			List<Task> filter) {
		for (Iterator<Task> iter = list.iterator(); iter.hasNext();) {
			Task task = iter.next();
			if (!filter.contains(task))
				continue;
			Menu menu = new Menu(task.getTaskID(), task.getMenuURL(),task.getName());
			if (!task.getChildren().isEmpty()) {
				List<Task> newTopTask=new ArrayList<Task>();
				for (Task filterTask : filter) {
					for (Task childrenTask : task.getChildren()){
						if(filterTask.getTaskID().toString().equals(childrenTask.getTaskID().toString())&&"1".toString().equals(childrenTask.getIsAsMenu()))
						newTopTask.add(filterTask);
					}
				}
				List<Menu> ls = new ArrayList<Menu>();
				menu.setChildren(ls);
				craeteList(newTopTask, ls, filter);
			}
			dest.add(menu);
		}

	}
	
	 void creatDataPowerList(List<DataPower> dataPowers,List<BusPower>busPowers,String userCode,String comCode){
		for (BusPower busPower : busPowers) {
			if(StringUtils.isNotBlank(busPower.getDataRuleParam())&&busPower.getDataRule()!=null){
				DataPower dataPower=new DataPower(userCode,comCode,busPower.getTask().getTaskID(), busPower.getDataRule().getDataRuleID(), busPower.getDataRuleParam(), busPower.getDataRule().getRule(),busPower.getDataRule().getBusDataInfos());
				dataPowers.add(dataPower);
			}else if(busPower.getDataRule()!=null){
				DataPower dataPower=new DataPower(userCode,comCode,busPower.getTask().getTaskID(),busPower.getDataRule().getDataRuleID(), null, busPower.getDataRule().getRule(),busPower.getDataRule().getBusDataInfos());
				dataPowers.add(dataPower);
			}
		}
	}

	
}
