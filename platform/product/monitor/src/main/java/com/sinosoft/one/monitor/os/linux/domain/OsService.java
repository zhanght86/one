package com.sinosoft.one.monitor.os.linux.domain;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sinosoft.one.monitor.os.linux.model.Os;
import com.sinosoft.one.monitor.os.linux.model.OsShell;
import com.sinosoft.one.monitor.os.linux.repository.OsRepository;
import com.sinosoft.one.monitor.os.linux.repository.OsShellRepository;
/**
 * 数据库操作类
 * @author chenxiongxi
 * @version 1.0
 * @created 27-����-2013 14:42:30
 */
@Component
public class OsService {
	@Autowired
	private OsRepository osRepository;
	@Autowired
	private OsShellRepository osShellRepository;
	
	/**
	 * 读取操作系统基本信息
	 * @return
	 */
	public Os getOsBasicByIp(String ip){
		return  osRepository.findOsbyIp(ip);
	}

	/**
	 * 判断IP是否已有
	 * @param ip
	 * @return
	 */
	public boolean checkOsByIp(String ip){
		int ipcount=osRepository.checkOsByIp(ip);
		if(ipcount>0){
			return false;
		}
		return true;
	}
	/**
	 * 读取操作系统基本信息
	 * @return
	 */
	public Os getOsBasicById(String id){
		Os os=osRepository.findOne(id);
		return  os;
	}
	/**
	 * 保存操作系统基本信息
	 * @return
	 * @throws Exception 
	 */
	public void saveOsBasic(String name,String type,String ipAddr,String subnetMask,int interCycle) throws Exception{
		Os os=osRepository.findOsbyIp(ipAddr);
		if(os!=null){
			 throw new Exception();
		}
		Os newos =new Os();
		newos.setName(name);
		newos.setType(type);
		newos.setIpAddr(ipAddr);
		newos.setSubnetMask(subnetMask);
		newos.setIntercycleTime(interCycle);
		osRepository.save(newos);
	}
	
	/**
	 * 删除操作系统基本信息
	 * @return
	 */
	public void deleteOsBasic(String osId){
		try {
			osRepository.delete(osId);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	/**
	 * 读取操作系统基本脚本
	 * @return
	 */
	public List<OsShell> getOsShell(){
		return osShellRepository.findShell();
	}
	
	/**
	 * 保存操作系统基本脚本
	 */
	public void saveShell(String type,String template){
		OsShell osShell=new OsShell();
		osShell.setType(type);
		osShell.setTemplate(template);
		osShellRepository.save(osShell);
	}
	
	/**
	 * 获取所有OS监控器信息
	 * @return
	 */
	public List<Os> getAllOs(){
		return (List<Os>) osRepository.findAll();
	}
}
