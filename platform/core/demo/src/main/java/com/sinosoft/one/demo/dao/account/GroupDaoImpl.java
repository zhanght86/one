package com.sinosoft.one.demo.dao.account;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import com.sinosoft.one.demo.model.account.Group;
import com.sinosoft.one.demo.model.account.User;

/**
 * GroupDao的扩展行为实现类.
 */
@Component
public class GroupDaoImpl implements GroupDaoCustom {

	private static final String QUERY_USER_BY_GROUPID = "select u from User u left join u.groupList g where g.id=?1";

	@PersistenceContext
	private EntityManager em;

	public void deleteWithReference(Long id) {
		//因為Group中沒有与User的关联，只能用笨办法，查询出拥有该权限组的用户, 并删除该用户的权限组.
		Group group = em.find(Group.class, id);
		List<User> users = em.createQuery(QUERY_USER_BY_GROUPID).setParameter(1, id).getResultList();
		for (User u : users) {
			u.getGroupList().remove(group);
		}
		em.remove(group);
	}

}
