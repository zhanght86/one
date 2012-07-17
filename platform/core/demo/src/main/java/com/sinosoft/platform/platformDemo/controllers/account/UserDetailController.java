package com.sinosoft.platform.platformDemo.controllers.account;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.sinosoft.platform.platformDemo.controllers.LoginRequired;
import com.sinosoft.platform.platformDemo.model.account.Group;
import com.sinosoft.platform.platformDemo.model.account.User;
import com.sinosoft.platform.platformDemo.service.account.AccountManager;

/**
 * 使用@ModelAttribute, 实现Struts2 Preparable二次绑定的效果。 
 * 因为@ModelAttribute被默认执行, 而其他的action url中并没有${id}，所以需要独立出一个Controller.
 * 
 * @author calvin
 */
@LoginRequired
@Path("user")
public class UserDetailController {

	@Autowired
	private AccountManager accountManager;

	
	@Autowired
	private GroupListEditor groupListEditor;

	@InitBinder
	public void initBinder(WebDataBinder b) {
		b.registerCustomEditor(List.class, "groupList", groupListEditor);
	}
/*
 * 
 * @Get("update/{id:[0-9]+}/check/{name:[a-z]+}/{email}")
 * 
 * 
 * 
 * 
 * 
 * 
 */
	@Get("update/{id:[0-9]+}")
	public String updateForm(@Param("id") long id, Invocation inv) {
		inv.addModel("user", accountManager.getUser(id));
		inv.addModel("userInfo", accountManager.findUserInfo(id));
		inv.addModel("allGroups", accountManager.getAllGroup());
		return "userForm";
	}

	@Post("save/{id}")
	public String save(@Param("id") long id,@Param("groupList") List<Long> gids ,User user,Invocation inv) {
		
		List<Group> groupList = new ArrayList<Group>();
		for (Long long1 : gids) {
			Group group = new Group(long1, null);
			groupList.add(group);
		}
		
		user.setGroupList(groupList);
		user.setCreateTime(new Date());
		accountManager.saveUser(user);
		
		if(user.getUserInfo().getId() == null){
			user.getUserInfo().setId(user.getId());
		}
		user.getUserInfo().setStrGeneral(user.getUserInfo().getGeneral().name());
		accountManager.saveUserInfo(user.getUserInfo());
		
		inv.addFlash("message", "修改用户" + user.getLoginName() + "成功");
		return "r:/platformDemo/account/user/list";
	}

}
