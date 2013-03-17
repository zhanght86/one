package com.sinosoft.one.monitor.db.oracle.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.sinosoft.one.data.jade.parsers.util.StringUtil;
import com.sinosoft.one.monitor.db.oracle.utils.DBUtil4Monitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.sinosoft.one.monitor.alarm.model.Alarm;
import com.sinosoft.one.monitor.alarm.repository.AlarmRepository;
import com.sinosoft.one.monitor.db.oracle.model.Ava;
import com.sinosoft.one.monitor.db.oracle.model.AvaSta;
import com.sinosoft.one.monitor.db.oracle.model.Info;
import com.sinosoft.one.monitor.db.oracle.model.Lastevent;
import com.sinosoft.one.monitor.db.oracle.model.OracleAvaInfoModel;
import com.sinosoft.one.monitor.db.oracle.model.OracleHealthInfoModel;
import com.sinosoft.one.monitor.db.oracle.model.OracleStaBaseInfoModel;
import com.sinosoft.one.monitor.db.oracle.model.StaGraphModel;
import com.sinosoft.one.monitor.db.oracle.repository.AvaRepository;
import com.sinosoft.one.monitor.db.oracle.repository.AvaStaRepository;
import com.sinosoft.one.monitor.db.oracle.repository.InfoRepository;
import com.sinosoft.one.monitor.db.oracle.repository.LasteventRepository;
import com.sinosoft.one.monitor.threshold.model.SeverityLevel;

/**
 * User: Chunliang.Han
 * Date: 13-3-2
 * Time: 下午8:17
 */
@Component
public class OracleBatchInfoServiceImpl implements OracleBatchInfoService {
    @Autowired
    private InfoRepository infoRepository;
    @Autowired
    private AvaStaRepository avaStaRepository;
    @Autowired
    private AvaRepository avaRepository;
    @Autowired
    private LasteventRepository lasteventRepository;
    @Autowired
    private AlarmRepository alarmRepository;
    @Autowired
    private DBUtil4Monitor dbUtil4Monitor ;

    /**
     * 最近24小时可用性统计
     * 最近30天可用性统计（目前尚未实现）
     *
     * @param staTimeEnum
     * @return
     */
    @Override
    public List<OracleAvaInfoModel> avaInfoList(StaTimeEnum staTimeEnum) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "sysTime"));
        //获取所有数据库监控
        List<Info> infoList = (List<Info>) infoRepository.findAll(sort);
        //数据库监控对象集合
        List<OracleAvaInfoModel> oracleAvaInfoModelList = new ArrayList<OracleAvaInfoModel>();
        Date now = new Date();
        Date timeStart =  StaTimeEnum.getTime(staTimeEnum,now);
        long during =  (now.getTime()-timeStart.getTime())/100;
        for (Info info : infoList) {
            OracleAvaInfoModel oracleAvaInfoModel = new OracleAvaInfoModel();
            Date time = StaTimeEnum.getTime(StaTimeEnum.TODAY,now);
            AvaSta avaSta = avaStaRepository.findAvaSta(info.getId(), time);
            oracleAvaInfoModel.setMonitorID(info.getId());
            oracleAvaInfoModel.setMonitorName(info.getName());
            Double avaRate = 0d;
            if(avaSta != null){
                avaRate = avaSta.getNormalRuntime() / ((avaSta.getNormalRuntime() + avaSta.getTotalPoweroffTime() + avaSta.getUnknowTime())/1.0);
            }
            avaRate = avaRate*100;
            oracleAvaInfoModel.setAvaRate(avaRate.intValue() + "");
            if(staTimeEnum==StaTimeEnum.LAST_24HOUR){
                List<Ava> avaList0 = avaRepository.find24Hour(timeStart, info.getId());
                List<Ava> avaList = caculate(avaList0,timeStart.getTime(),now.getTime());
                List<String[]> parts = new ArrayList<String[]>();
                for (int i = 0; i < avaList.size() - 1; i++) {
                    String[] part = new String[3];
                    Ava ava1 = avaList.get(i);
                    Ava ava2 = avaList.get(i + 1);
                    long time1 = ava1.getRecordTime().getTime();
                    long time2 = ava2.getRecordTime().getTime();
                    part[0] = time1+"";
                    part[1] = ava1.getState();
                    part[2] = ((time2-time1)/during)+"";
                    parts.add(part);
                }
                oracleAvaInfoModel.setGraphInfo(parts);
            }
            oracleAvaInfoModelList.add(oracleAvaInfoModel);
        }

        return oracleAvaInfoModelList;
    }
    private List<Ava> caculate(List<Ava> avaList,long startTime,long endTime){
        //定义合并后的Ava对象集合
        List<Ava> avas = new ArrayList<Ava>();
        //遍历avaList去除状态相同项
        for(int i=0;i<avaList.size();i++){
            //定义合并后的对象
            Ava caculateAva = new Ava();
            if(avas.size()>0){
                //获取目前已经合并后的Ava对象集合中的最后一条记录
                caculateAva = avas.get(avas.size()-1);
            }
            //初始化合并后的对象(caculateAva)
            else {
                caculateAva.setState("-1");
                caculateAva.setRecordTime(new Date(0));
                caculateAva.setInterval(-1);
            }
            Ava ava = avaList.get(i);
            long caculateTime = caculateAva.getRecordTime().getTime();
            long avaTime = ava.getRecordTime().getTime();
            long interval = caculateAva.getInterval()*60000;
            //如果相邻的两个元素状态不相等，则插入新的记录
            if(!StringUtil.equals(ava.getState(),caculateAva.getState())){
                if(i==0){
                    if(startTime+60000<avaTime){
                        Ava newAva = new Ava();
                        newAva.setState("2");
                        newAva.setRecordTime(new Date(startTime));
                        avas.add(newAva);
                        avas.add(ava);
                    }else{
                        avas.add(ava);
                    }
                } else {
                    if(caculateTime+interval+1000<avaTime){
                        Ava newAva = new Ava();
                        newAva.setState("2");
                        newAva.setRecordTime(new Date(caculateTime+interval));
                        avas.add(newAva);
                        avas.add(ava);
                    }else{
                        avas.add(ava);
                    }
                }
            }
        }
        if(avaList.size()>0){
            Ava ava = avaList.get(avaList.size()-1);
            avas.add(ava);
            long avaTime = ava.getRecordTime().getTime();
            long interval = ava.getInterval()*60000;
            if(avaTime+interval+60000<endTime){
                Ava newAva = new Ava();
                newAva.setState("2");
                newAva.setRecordTime(new Date(avaTime+interval));
                avas.add(newAva);
            }
        }else{
            Ava newAva = new Ava();
            newAva.setState("2");
            newAva.setRecordTime(new Date(startTime));
            avas.add(newAva);
        }
        return avas;
    }
    
    @Override
    public List<OracleHealthInfoModel> healthInfoList(StaTimeEnum staTimeEnum) {
        List<OracleHealthInfoModel> oracleHealthInfoList = new ArrayList<OracleHealthInfoModel>();
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "sysTime"));
        List<Info> infoList = (List<Info>) infoRepository.findAll(sort);
        Date endTime = new Date();
        Date startTime = StaTimeEnum.getTime(staTimeEnum, endTime);
        for (Info info : infoList) {
            String monitorId = info.getId();
            List<Alarm> alarmList = alarmRepository.findAlarmByMonitorId(monitorId, startTime, endTime);
            OracleHealthInfoModel oracleHealthInfo = new OracleHealthInfoModel();
            oracleHealthInfo.setMonitorID(monitorId);
            oracleHealthInfo.setMonitorName(info.getName());
            List<String[]> alarms = getHealthyState(alarmList, staTimeEnum, startTime, endTime);
            /* 构建健康状态集合*/
            buildHealths(alarmList, staTimeEnum, startTime, endTime, oracleHealthInfo);
            oracleHealthInfo.setGraphInfo(alarms);
            oracleHealthInfoList.add(oracleHealthInfo);
        }
        return oracleHealthInfoList;
    }
    
    private void buildHealths(List<Alarm> alarmList, StaTimeEnum staTimeEnum, Date startTime, Date endTime, OracleHealthInfoModel oracleHealthInfo) {
    	SeverityLevel health = SeverityLevel.INFO;
        List<Long[]> dateMapList = new ArrayList<Long[]>();
        //如果统计24小时的健康状况
        if (staTimeEnum.equals(StaTimeEnum.LAST_24HOUR)) {
            pullDateMapList(dateMapList, startTime, endTime, TimeGranularityEnum.HOUR);
            for (Long[] dateMap : dateMapList) {
                long start = dateMap[0];
                long end = dateMap[1];
                for (Alarm alarm : alarmList) {
                    Date recordTime = alarm.getCreateTime();
                    long recordTimeNumber = recordTime.getTime();
                    if (recordTimeNumber < start) {
                        continue;
                    } else if (recordTimeNumber >= end) {
                        break;
                    } else if (alarm.getSeverity() != null) {
                    	health = alarm.getSeverity();
                    }
                }
            }
        } else if (staTimeEnum.equals(StaTimeEnum.LAST_30DAY)) { //如果统计30天的健康状况
            pullDateMapList(dateMapList, startTime, startTime, TimeGranularityEnum.DAY);
            for (Long[] dateMap : dateMapList) {
                long start = dateMap[0];
                long end = dateMap[1];
                for (Alarm alarm : alarmList) {
                    Date recordTime = alarm.getCreateTime();
                    long recordTimeNumber = recordTime.getTime();
                    if (recordTimeNumber < start) {
                        continue;
                    } else if (recordTimeNumber >= end) {
                        break;
                    } else if (alarm.getSeverity() != null) {
                    	health = alarm.getSeverity();
                    }
                }
            }
        }
        oracleHealthInfo.addHealth(health);
    }

    /**
     * 获取时间段内各个点的健康状况
     *
     * @param alarmList
     * @param staTimeEnum
     * @param startTime
     * @param currTime
     * @return
     */
    private List<String[]> getHealthyState(List<Alarm> alarmList, StaTimeEnum staTimeEnum, Date startTime, Date currTime) {
        List<String[]> healthy = new ArrayList<String[]>();
        List<Long[]> dateMapList = new ArrayList<Long[]>();
        //如果统计24小时的健康状况
        if (staTimeEnum.equals(StaTimeEnum.LAST_24HOUR)) {
            pullDateMapList(dateMapList, startTime, currTime, TimeGranularityEnum.HOUR);
            for (Long[] dateMap : dateMapList) {
                String[] healthyPint = new String[2];
                String healthyFlag = "1";
                StringBuilder info = new StringBuilder();
                long start = dateMap[0];
                long end = dateMap[1];
                for (Alarm alarm : alarmList) {
                    Date recordTime = alarm.getCreateTime();
                    long recordTimeNumber = recordTime.getTime();
                    if (recordTimeNumber < start) {
                        continue;
                    } else if (recordTimeNumber >= end) {
                        break;
                    } else if (alarm.getSeverity() != null) {

                        info.append(alarm.getMessage()).append("\n");
                        if (alarm.getSeverity().equals(SeverityLevel.INFO)) {
                            healthyFlag = "1";
                        } else if (alarm.getSeverity().equals(SeverityLevel.WARNING)) {
                            healthyFlag = "2";
                        } else if (alarm.getSeverity().equals(SeverityLevel.CRITICAL)) {
                            healthyFlag = "3";
                        } else if (alarm.getSeverity().equals(SeverityLevel.UNKNOW)) {
                            healthyFlag = "4";
                        }
                    }
                }
                healthyPint[0] = healthyFlag;
                healthyPint[1] = info.toString();
                healthy.add(healthyPint);
            }
        } else if (staTimeEnum.equals(StaTimeEnum.LAST_30DAY)) { //如果统计30天的健康状况
            pullDateMapList(dateMapList, startTime, currTime, TimeGranularityEnum.DAY);
            for (Long[] dateMap : dateMapList) {
                String[] healthyPint = new String[2];
                String healthyFlag = "1";
                StringBuilder info = new StringBuilder();
                long start = dateMap[0];
                long end = dateMap[1];
                for (Alarm alarm : alarmList) {
                    Date recordTime = alarm.getCreateTime();
                    long recordTimeNumber = recordTime.getTime();
                    if (recordTimeNumber < start) {
                        continue;
                    } else if (recordTimeNumber >= end) {
                        break;
                    } else if (alarm.getSeverity() != null) {

                        info.append(alarm.getMessage()).append("\n");
                        if (alarm.getSeverity().equals(SeverityLevel.INFO)) {
                            healthyFlag = "1";
                        } else if (alarm.getSeverity().equals(SeverityLevel.WARNING)) {
                            healthyFlag = "2";
                        } else if (alarm.getSeverity().equals(SeverityLevel.CRITICAL)) {
                            healthyFlag = "3";
                        } else if (alarm.getSeverity().equals(SeverityLevel.UNKNOW)) {
                            healthyFlag = "4";
                        }
                    }
                }
                healthyPint[0] = healthyFlag;
                healthyPint[1] = info.toString();
                healthy.add(healthyPint);
            }
        }
        return healthy;
    }

    /**
     * @param dateMapList 要装的时间段集合
     * @param startTime   开始时间
     * @param currTime    结束时间
     * @param currTime    timeGranularityEnum 时间粒度
     */
    private void pullDateMapList(List<Long[]> dateMapList, Date startTime, Date currTime, TimeGranularityEnum timeGranularityEnum) {
        Calendar calendar = Calendar.getInstance();
        switch (timeGranularityEnum) {
            case HOUR: {
                pullDateMapListBycalendar(dateMapList, startTime, currTime, calendar, Calendar.HOUR_OF_DAY);
                break;
            }
            case DAY: {
                pullDateMapListBycalendar(dateMapList, startTime, currTime, calendar, Calendar.DATE);
                break;
            }
        }
    }

    private void pullDateMapListBycalendar(List<Long[]> dateMapList, Date startTime,
                                           Date currTime, Calendar calendar, int timeGranularity) {
        long start = startTime.getTime();
        long end = currTime.getTime();
        while (start < end) {
            calendar.setTimeInMillis(start);
            calendar.add(timeGranularity, 1);
            Long[] dateMap = new Long[2];
            dateMap[0] = start;
            start = calendar.getTime().getTime();
            dateMap[1] = start;
            dateMapList.add(dateMap);
        }
    }

    @Override
    public List<OracleStaBaseInfoModel> listStaBaseInfo() {
        List<OracleStaBaseInfoModel> oracleStaBaseInfoModelList = new ArrayList<OracleStaBaseInfoModel>();
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "sysTime"));
        List<Info> infoList = (List<Info>) infoRepository.findAll(sort);
        for (Info info : infoList) {
            OracleStaBaseInfoModel oracleStaBaseInfoModel = new OracleStaBaseInfoModel();
            oracleStaBaseInfoModel.setMonitorID(info.getId());
            oracleStaBaseInfoModel.setMonitorName(info.getName());
            //查询数据库得到当前是否可连接
            String state = "0";
            if(dbUtil4Monitor.canConnect(info)){
                state = "1";
            }
            oracleStaBaseInfoModel.setUsability(state);
            //获取5分钟前到现在报警信息
            Date endTime = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endTime);
            calendar.add(Calendar.MINUTE, (int) -info.getPullInterval());
            Date startTime = calendar.getTime();
            //根据报警信息接确定当前是否可用
            String[] healthyPint = new String[2];
            String healthyFlag = "1";
            StringBuilder msg = new StringBuilder();
            //"1"代表可用，"0"代表不可用
            if("0".equals(state)){
                healthyPint[0] = "3";
                healthyPint[1] = msg.append(info.getName()).append("为停止").toString();
            } else {
                List<Alarm> alarmList = alarmRepository.findAlarmByMonitorId(info.getId(), startTime, endTime);
                for (Alarm alarm : alarmList) {
                    if (alarm.getSeverity() != null) {
                        msg.append(alarm.getMessage()).append("\n");
                        if (alarm.getSeverity().equals(SeverityLevel.INFO)) {
                            healthyFlag = "1";
                        } else if (alarm.getSeverity().equals(SeverityLevel.WARNING)) {
                            healthyFlag = "2";
                        } else if (alarm.getSeverity().equals(SeverityLevel.CRITICAL)) {
                            healthyFlag = "3";
                        } else if (alarm.getSeverity().equals(SeverityLevel.UNKNOW)) {
                            healthyFlag = "4";
                        }
                    }
                }
                healthyPint[0] = healthyFlag;
                healthyPint[1] = msg.toString();
            }
            //插入可用性
            oracleStaBaseInfoModel.setHealthy(healthyPint);
            oracleStaBaseInfoModelList.add(oracleStaBaseInfoModel);
        }
        return oracleStaBaseInfoModelList;
    }

    @Override
    public List<StaGraphModel> listMonitorEventSta() {
        List<StaGraphModel> totalStaGraphModelList = new ArrayList<StaGraphModel>();
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "sysTime"));
        List<Info> infoList = (List<Info>) infoRepository.findAll(sort);
        Date end = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -3);
        Date start = calendar.getTime();
        for (Info info : infoList) {
            StaGraphModel staGraphModel = new StaGraphModel();
            String monitorId = info.getId();
            staGraphModel.setId(monitorId);
            staGraphModel.setName(info.getName());
            List<Lastevent> lasteventList = lasteventRepository.findLastEventList(monitorId, start, end);
            staGraphModel.setLasteventList(lasteventList);
            totalStaGraphModelList.add(staGraphModel);
        }
        return totalStaGraphModelList;
    }
}
