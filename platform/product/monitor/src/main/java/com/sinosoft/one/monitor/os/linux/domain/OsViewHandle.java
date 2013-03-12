package com.sinosoft.one.monitor.os.linux.domain;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sinosoft.one.monitor.os.linux.model.Os;
import com.sinosoft.one.monitor.os.linux.model.OsCpu;
import com.sinosoft.one.monitor.os.linux.model.OsDisk;
import com.sinosoft.one.monitor.os.linux.model.OsRam;
import com.sinosoft.one.monitor.os.linux.model.OsStati;
import com.sinosoft.one.monitor.os.linux.model.viewmodel.OsGridModel;
import com.sinosoft.one.monitor.os.linux.model.viewmodel.StatiDataModel;
import com.sinosoft.one.monitor.os.linux.util.OsTransUtil;
import com.sinosoft.one.monitor.os.linux.util.OsUtil;

@Component
public class OsViewHandle {
	@Autowired
	private OsCpuViewHandle osCpuViewDataHandle;
	@Autowired
	private OsRamViewHandle osRamViewHandle;
	@Autowired
	private OsRespondViewHadle osRespondViewHadle;
	@Autowired
	private OsStatiViewHandle osStatiViewHandle;
	@Autowired
	private OsRamService osRamService;
	@Autowired
	private OsCpuService osCpuService;
	@Autowired
	private OsDiskService osDiskService;
	@Autowired
	private OsService osService;
	@Autowired
	private OsStatiService osStatiService;
	@Autowired
	private OsAvailableServcie osAvailableServcie;
	
	
	/**
	 * 构建多个操作系统的线
	 *  @param currentTime页面当前时间
	 * @param interCycle 页面轮询时间
	 * @param timespan 需要的时间段 如 1为1小时内
	 * @return
	 */
	public Map<String, Map<String,List<List<?>>>>  createlineView(Date currentTime ,int interCycle ,int timespan){
		List<Os>oss=osService.getAllOs();
		Map<String, Map<String,List<List<?>>>> viewMap=new HashMap<String, Map<String,List<List<?>>>>();
		Map<String,List<List<?>>> cpuLineViewMaps=osCpuViewDataHandle.creatCpuLineData(oss, currentTime, interCycle, timespan);
		Map<String,List<List<?>>> ramLineViewMaps=osRamViewHandle.creatRamLineData(oss, currentTime, interCycle, timespan);
		Map<String,List<List<?>>> swapLineViewMaps=osRamViewHandle.creatSwapLineData(oss, currentTime, interCycle, timespan);
		Map<String,List<List<?>>> respondLineViewMaps=osRespondViewHadle.creatRespondLineData(oss, currentTime, interCycle, timespan);
		viewMap.put("chartMem", ramLineViewMaps);
		viewMap.put("chartCpu", cpuLineViewMaps);
		viewMap.put("chartSwap", swapLineViewMaps);
		viewMap.put("chartReply", respondLineViewMaps);
		return viewMap;
	}
	
	/**
	 * 构建1个操作系统的线
	 * @param os
	 * @param currentTime页面当前时间
	 * @param interCycle 页面轮询时间
	 * @param timespan 需要的时间段 如 1为1小时内
	 * @return
	 */
	public  Map<String,List<List<?>>>   createOneOsCpuAndMemline(String osid,Date currentTime ,int interCycle ,int timespan){
		Os os =osService.getOsBasicById(osid);
		Map<String,List<List<?>>> viewMap=new   HashMap<String, List<List<?>>>();
		List<List<?>> oneCpuLineViewMaps=osCpuViewDataHandle.creatOneCpuUsedLineData(os, currentTime, interCycle, timespan);
		List<List<?>> oneRamLineViewMaps=osRamViewHandle.creatOneRamLineData(os, currentTime, interCycle, timespan);
		List<List<?>> oneSwapLineViewMaps=osRamViewHandle.creatOneSwapLineData(os, currentTime, interCycle, timespan);
		viewMap.put("CPU", oneCpuLineViewMaps);
		viewMap.put("MEM", oneRamLineViewMaps);
		viewMap.put("SWAP", oneSwapLineViewMaps);
		return viewMap;
	}
	
	/**
	 * 构建1个操作系统Cpu分解的线
	 * @param os
	 * @param currentTime页面当前时间
	 * @param interCycle 页面轮询时间
	 * @param timespan 需要的时间段 如 1为1小时内
	 * @return
	 */
	public Map<String,List<List<?>>>  createOneCpuResolveView(String osid,Date currentTime ,int interCycle ,int timespan){
		Os os =osService.getOsBasicById(osid);
		Map<String,List<List<?>>> viewMap=new   HashMap<String, List<List<?>>>();
		Calendar c  = Calendar.getInstance();
		c.setTime(currentTime);
		c.set(Calendar.HOUR_OF_DAY,currentTime.getHours()-timespan);
		List<OsCpu> osCpus=osCpuService.getCpuByDate(os.getOsInfoId(), c.getTime(), currentTime);
		List<List<?>> oneCpuUserTimeLineViewMaps=osCpuViewDataHandle.creatOneCpuUserTimeLine(os, currentTime, interCycle, timespan,osCpus);
		List<List<?>> oneCpuSysTimeLineViewMaps=osCpuViewDataHandle.creatOneCpuSysTimeLine(os, currentTime, interCycle, timespan,osCpus);
		List<List<?>> oneCpuIOLineViewMaps=osCpuViewDataHandle.creatOneCpuIOLine(os, currentTime, interCycle, timespan,osCpus);
		List<List<?>> OneCpuIDLELineViewMaps=osCpuViewDataHandle.creatOneCpuIDLELine(os, currentTime, interCycle, timespan,osCpus);
		viewMap.put("userTime", oneCpuUserTimeLineViewMaps);
		viewMap.put("sysTime", oneCpuSysTimeLineViewMaps);
		viewMap.put("io", oneCpuIOLineViewMaps);
		viewMap.put("idle", OneCpuIDLELineViewMaps);
		return viewMap;
	}
	
	/**
	 * 获取最新的cpu、内存 、磁盘利用率
	 * @param osid
	 * @param currentTime
	 * @return
	 */
	public Map<String, Double> creatUtilZationView(String osid, Date currentTime){
		Map<String, Double> map=new HashMap<String, Double>();
		Os os =osService.getOsBasicById(osid);
		OsCpu osCpu=osCpuService.findNealyCpu(os.getOsInfoId(), currentTime, os.getIntercycleTime());
		List<OsDisk> osDisks=osDiskService.findNealyDisk(os.getOsInfoId(), currentTime, os.getIntercycleTime());
		OsRam osRam=osRamService.findNealyRam(os.getOsInfoId(), currentTime, os.getIntercycleTime());
		if(osCpu==null){
			map.put("cpuUtilzation", 0.00);
		}else{
			map.put("cpuUtilzation", Double.valueOf(osCpu.getUtiliZation()));
		}
		if(osDisks.size()<0){
			map.put("diskUtilzation", 0.00);
		}else{
			long diskTotal=0;
			long diskUsed=0;
			for (OsDisk osDisk : osDisks) {
				diskTotal+=Long.parseLong(osDisk.getTotal());
				diskUsed+=Long.parseLong(osDisk.getUsed());
			}
			String result= OsTransUtil.countUtilZation(diskTotal+"", diskUsed+"");
			map.put("diskUtilzation", Double.parseDouble(result));
		}
		if(osRam==null){
			map.put("ramUtilzation", 0.00);
		}else{
			long memTotal=Long.parseLong(osRam.getMemTotal())+Long.parseLong(osRam.getSwapTotal());
			long memUsed=Long.parseLong(osRam.getMemUsed())+Long.parseLong(osRam.getSwapUsed());
			String result= OsTransUtil.countUtilZation(memTotal+"", memUsed+"");
			map.put("ramUtilzation", Double.parseDouble(result));
		}
		return map;
	}
	
	/**
	 * 统计报表曲线
	 * @param osid
	 * @param currentTime
	 * @param timespan 时间段 1 7 30
	 * @return
	 */
	public Map<String,List<List<?>>> creatStatiLine(String osid,String statitype, Date currentTime,int timespan ){
		long span;
		if(timespan>1){
			span=(timespan-1)*24;
		}else{
			span=(timespan*24);
		}
		Map<String,List<List<?>>> viewMap=new  HashMap<String,List<List<?>>>();
		Date dayPoint= new Date(currentTime.getTime()-Long.valueOf(span*60*60*1000));
		dayPoint=OsTransUtil.getDayPointByDate(dayPoint);//天整点
		List<StatiDataModel> osStatis=osStatiService.findStatiByTimeSpan(osid, statitype, dayPoint, currentTime);
		currentTime=OsTransUtil.getDayPointByDate(currentTime);//获取当天整点
		List<List<?>> cpuMaxmaps=osStatiViewHandle.creatCpuMaxStatiLine( osStatis, currentTime, dayPoint, timespan);
		List<List<?>> cpuMinmaps=osStatiViewHandle.creatCpuMinStatiLine( osStatis, currentTime, dayPoint, timespan);
		List<List<?>> cpuAvemaps=osStatiViewHandle.creatCpuAvaStatiLine( osStatis, currentTime, dayPoint, timespan);
		viewMap.put("max", cpuMaxmaps);
		viewMap.put("min", cpuMinmaps);
		viewMap.put("ave", cpuAvemaps);
		return viewMap;
		
	}
	
	
	/**
	 * 统计报表表格
	 * @param osid
	 * @param currentTime
	 * @param timespan 时间段 1 7 30
	 * @return
	 */
	public List<OsGridModel> creatStatiGrid(String osid,String statitype, Date currentTime,int timespan ){
		long span=0;
		SimpleDateFormat simpleDateFormat;
		if(timespan>1){
			span=24;	
			simpleDateFormat=new SimpleDateFormat(OsUtil.DATEFORMATE_YEAR_MON_DAY);
		}else{
			
			simpleDateFormat=new SimpleDateFormat(OsUtil.DATEFORMATE_HOURS);
		}
		List<OsGridModel>osGridModels= new ArrayList<OsGridModel>();
		Date dayPoint=OsTransUtil.getBeforeDate(currentTime, timespan+"");
		List<StatiDataModel> osStatis=osStatiService.findStatiByTimeSpan(osid, statitype, dayPoint, currentTime);
		for (StatiDataModel statiDataModel : osStatis) {
			OsGridModel osGridModel=new OsGridModel();
			osGridModel.setMaxValue(statiDataModel.getMaxValue());
			osGridModel.setMinValue(statiDataModel.getMinValue());
			osGridModel.setTime(simpleDateFormat.format(statiDataModel.getDate()));
			osGridModel.setAverageValue(statiDataModel.getAvgValue());
			osGridModels.add(osGridModel);
		}
		return osGridModels;
		
	}
	/**
	 * 可用性统计报表表格
	 * @param osid
	 * @param currentTime
	 * @param timespan 时间段 1 7 30
	 * @return
	 */
	public List<OsGridModel> creatAvailableHistoryGrid(String osid,String statitype, Date currentTime,int timespan ){
		SimpleDateFormat simpleDateFormat;
		if(timespan>1){
			//按天统计时使用的格式化时间
			simpleDateFormat=new SimpleDateFormat(OsUtil.DATEFORMATE_YEAR_MON_DAY);
		}else{
			//按小时统计时使用的格式化时间
			simpleDateFormat=new SimpleDateFormat(OsUtil.DATEFORMATE_HOURS);
		}
		List<OsGridModel>osGridModels= new ArrayList<OsGridModel>();
		List<StatiDataModel> osStatis=osAvailableServcie.getOsAvailablesHistoryByDate(osid, currentTime, timespan);
		for (StatiDataModel statiDataModel : osStatis) {
			OsGridModel osGridModel=new OsGridModel();
			osGridModel.setNormalRun(statiDataModel.getNormalRun());
			osGridModel.setCrashTime(statiDataModel.getCrashTime());
			osGridModel.setAveRepairTime(statiDataModel.getAveRepairTime());
			osGridModel.setAveFaultTime(statiDataModel.getAveFaultTime());
			osGridModel.setTime(simpleDateFormat.format(statiDataModel.getDate()));
			osGridModels.add(osGridModel);
		}
		return osGridModels;
		
	}
}
