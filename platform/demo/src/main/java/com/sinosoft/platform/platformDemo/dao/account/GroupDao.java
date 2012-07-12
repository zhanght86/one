package com.sinosoft.platform.platformDemo.dao.account;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.sinosoft.platform.platformDemo.model.account.Group;

/**
 * 权限组对象的Dao interface.
 * 
 * @author calvin
 */

public interface GroupDao extends PagingAndSortingRepository<Group, Long>, GroupDaoCustom {
}
