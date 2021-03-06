package com.sinosoft.one.rms.service.facade;


import com.sinosoft.one.rms.User;

/**
 * 服务端包装接口
 * @author Administrator
 *
 */
public interface GetUserService {
	 /**
	  * 获取用户接口
	  * @param userCode
	  * @param password
	  * @param comCode
	  * @return
	  */
	 public User getUserByUserCodeComCode(String userCode,String comCode,String sysFlag);
	 
}
