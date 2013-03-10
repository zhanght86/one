package com.sinosoft.one.monitor.os.linux.domain;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.sinosoft.one.monitor.os.linux.model.viewmodel.StatiDataModel;
import com.sinosoft.one.monitor.os.linux.util.OsUtil;

/**
 * 统计报表的图形处理
 * @author Administrator
 * chenxiongxi
 */
@Component
public class OsStatiViewHandle {
	/**
	 *  最大值的曲线
	 * @param os
	 * @param currentTime
	 * @param type
	 * @param timespan
	 * @return
	 */
	public List<Map<String, Object>> creatCpuMaxStatiLine(List<StatiDataModel> osStatis,Date currentTime,Date dayPoint,int timespan){
		int timeSize =0;
		if(timespan>1){
			timespan=timespan*24;
			timeSize=24;
		}else {
			timeSize=1;
		}
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat(OsUtil.DATEFORMATE_YEAR_MON_DAY);
		List<Map<String, Object>>maps=new ArrayList<Map<String,Object>>();
		Map<String, Object> m=new HashMap<String, Object>();
		long aveTime =(currentTime.getTime()-dayPoint.getTime())/Long.parseLong(timeSize*60*60*1000+"")+1;//平均时间段
		for (int i = 0; i < osStatis.size(); i++) {
			if(osStatis.get(i).getDate().getTime()-dayPoint.getTime()>=(timeSize*60*60*1000)){
				Integer ptime=(Integer) BigDecimal.valueOf(osStatis.get(i).getDate().getTime()-dayPoint.getTime()).divide(BigDecimal.valueOf(Long.parseLong(timeSize*60*60*1000+"")),0,BigDecimal.ROUND_UP).intValue();//空了几次
				for (int j = 0; j < ptime; j++) {
					Map<String, Object> map=new HashMap<String, Object>();
					map.put("y",-1);
					map.put("x", simpleDateFormat.format(dayPoint));
					maps.add(map);
					dayPoint=new Date (dayPoint.getTime()+(Long.parseLong(timeSize*60*60*1000+"")));
				}
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("x", simpleDateFormat.format(dayPoint));
				map.put("y",BigDecimal.valueOf(Double.valueOf(osStatis.get(i).getMaxValue())).divide(BigDecimal.valueOf(1.0),2,BigDecimal.ROUND_HALF_EVEN).doubleValue());
				maps.add(map);//本次点
				dayPoint=new Date (dayPoint.getTime()+(Long.parseLong(timeSize*60*60*1000+"")));
			}
			else{
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("y",BigDecimal.valueOf(Double.valueOf(osStatis.get(i).getMaxValue())).divide(BigDecimal.valueOf(1.0),2,BigDecimal.ROUND_HALF_EVEN).doubleValue());
				map.put("x", simpleDateFormat.format(dayPoint));
				maps.add(map);//本次点
				dayPoint=new Date(dayPoint.getTime()+Long.parseLong(timeSize*60*60*1000+""));
			}
		}
		int mapsSize=maps.size();
		if(maps.size()<aveTime){//如果总的点小于平均时间段 补上空点
			for (int i = 0; i < aveTime-mapsSize; i++) {
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("y",-1);
				map.put("x", simpleDateFormat.format(dayPoint));
				maps.add(map);
				dayPoint=new Date(dayPoint.getTime()+Long.parseLong(timeSize*60*60*1000+""));
			}
		}
		return maps;
	}
	
	/**
	 *  平均值的曲线
	 * @param os
	 * @param currentTime
	 * @param type
	 * @param timespan
	 * @return
	 */
	public List<Map<String, Object>> creatCpuAvaStatiLine(List<StatiDataModel> osStatis,Date currentTime,Date dayPoint,int timespan){
		int timeSize =0;
		if(timespan>1){
			timespan=timespan*24;
			timeSize=24;
		}else {
			timeSize=1;
		}
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat(OsUtil.DATEFORMATE_YEAR_MON_DAY);
		List<Map<String, Object>>maps=new ArrayList<Map<String,Object>>();
		Map<String, Object> m=new HashMap<String, Object>();
		long aveTime =(currentTime.getTime()-dayPoint.getTime())/Long.parseLong(timeSize*60*60*1000+"")+1;//平均时间段
		for (int i = 0; i < osStatis.size(); i++) {
			if(osStatis.get(i).getDate().getTime()-dayPoint.getTime()>=(timeSize*60*60*1000)){
				Integer ptime = (Integer) BigDecimal
				.valueOf(
						osStatis.get(i).getDate().getTime()
								- dayPoint.getTime())
				.divide(BigDecimal.valueOf(Long.parseLong(timeSize * 60 * 60
						* 1000 + "")), 0, BigDecimal.ROUND_UP)
				.intValue();// 空了几次
				for (int j = 0; j < ptime; j++) {
					Map<String, Object> map=new HashMap<String, Object>();
					map.put("y",-1);
					map.put("x", simpleDateFormat.format(dayPoint));
					maps.add(map);
					dayPoint=new Date (dayPoint.getTime()+(Long.parseLong(timeSize*60*60*1000+"")));
				}
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("x", simpleDateFormat.format(dayPoint));
				map.put("y",BigDecimal.valueOf(Double.valueOf(osStatis.get(i).getAvgValue())).divide(BigDecimal.valueOf(1.0),2,BigDecimal.ROUND_HALF_EVEN).doubleValue());
				maps.add(map);//本次点
				dayPoint=new Date (dayPoint.getTime()+(Long.parseLong(timeSize*60*60*1000+"")));
			}
			else{
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("y",BigDecimal.valueOf(Double.valueOf(osStatis.get(i).getAvgValue())).divide(BigDecimal.valueOf(1.0),2,BigDecimal.ROUND_HALF_EVEN).doubleValue());
				map.put("x", simpleDateFormat.format(dayPoint));
				maps.add(map);//本次点
				dayPoint=new Date (dayPoint.getTime()+(Long.parseLong(timeSize*60*60*1000+"")));
			}
		}
		int mapsSize=maps.size();
		if(maps.size()<aveTime){//如果总的点小于平均时间段 补上空点
			for (int i = 0; i < aveTime-mapsSize; i++) {
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("y",-1);
				map.put("x", simpleDateFormat.format(dayPoint));
				maps.add(map);
				dayPoint=new Date (dayPoint.getTime()+(Long.parseLong(timeSize*60*60*1000+"")));
			}
		}
		return maps;
	}
	
	/**
	 * 最小值的曲线
	 * @param os
	 * @param currentTime
	 * @param type
	 * @param timespan
	 * @return
	 */
	public List<Map<String, Object>> creatCpuMinStatiLine(List<StatiDataModel> osStatis,Date currentTime,Date dayPoint,int timespan){
		int timeSize =0;
		if(timespan>1){
			timespan=timespan*24;
			timeSize=24;
		}else {
			timeSize=1;
		}
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat(OsUtil.DATEFORMATE_YEAR_MON_DAY);
		List<Map<String, Object>>maps=new ArrayList<Map<String,Object>>();
		Map<String, Object> m=new HashMap<String, Object>();
		long aveTime =(currentTime.getTime()-dayPoint.getTime())/Long.parseLong(timeSize*60*60*1000+"")+1;//平均时间段
		for (int i = 0; i < osStatis.size(); i++) {
			if(osStatis.get(i).getDate().getTime()-dayPoint.getTime()>=(timeSize*60*60*1000)){
				Integer ptime = (Integer) BigDecimal
						.valueOf(
								osStatis.get(i).getDate().getTime()
										- dayPoint.getTime())
						.divide(BigDecimal.valueOf(Long.parseLong(timeSize * 60 * 60
								* 1000 + "")), 0, BigDecimal.ROUND_UP)
						.intValue();// 空了几次
				for (int j = 0; j < ptime; j++) {
					Map<String, Object> map=new HashMap<String, Object>();
					map.put("y",-1);
					map.put("x", simpleDateFormat.format(dayPoint));
					maps.add(map);
					dayPoint=new Date (dayPoint.getTime()+(Long.parseLong(timeSize*60*60*1000+"")));
				}
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("x", simpleDateFormat.format(dayPoint));
				map.put("y",BigDecimal.valueOf(Double.valueOf(osStatis.get(i).getMinValue())).divide(BigDecimal.valueOf(1.0),2,BigDecimal.ROUND_HALF_EVEN).doubleValue());
				maps.add(map);//本次点
				dayPoint=new Date (dayPoint.getTime()+(Long.parseLong(timeSize*60*60*1000+"")));
			}
			else{
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("y",BigDecimal.valueOf(Double.valueOf(osStatis.get(i).getMinValue())).divide(BigDecimal.valueOf(1.0),2,BigDecimal.ROUND_HALF_EVEN).doubleValue());
				map.put("x", simpleDateFormat.format(dayPoint));
				maps.add(map);//本次点
				dayPoint=new Date (dayPoint.getTime()+(Long.parseLong(timeSize*60*60*1000+"")));
			}
		}
		int mapsSize=maps.size();
		if(maps.size()<aveTime){//如果总的点小于平均时间段 补上空点
			for (int i = 0; i < aveTime-mapsSize; i++) {
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("y",-1);
				map.put("x", simpleDateFormat.format(dayPoint));
				maps.add(map);
				dayPoint=new Date (dayPoint.getTime()+(Long.parseLong(timeSize*60*60*1000+"")));
			}
		}
		return maps;
	}
	
}

