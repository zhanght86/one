package com.sinosoft.one.log.urltrace;

import com.alibaba.fastjson.JSON;
import com.sinosoft.one.log.*;
import com.sinosoft.one.log.queue.LoggableQueueAppender;
import com.sinosoft.one.util.encode.JsonBinder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * URL 追踪类
 * User: carvin
 * Date: 12-11-28
 * Time: 上午1:57
 * 用于记录URL追踪信息.
 */
public class URLTraceLog extends AbstractLoggable implements Loggable {

    private String id;
    private String url;
    private Timestamp beginTime;
    private Timestamp endTime;
    private long consumeTime;
    private String sessionId;
    private String userIp;
    private byte[] requestInfo;

    private LoggableQueueAppender loggableQueueAppender;

    public URLTraceLog() {
        logHandler = Loggables.getUrlTraceLogHandler();
        loggableQueueAppender = Loggables.getLoggableQueueAppender();
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Timestamp beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public long getConsumeTime() {
        return consumeTime;
    }

    public void setConsumeTime(long consumeTime) {
        this.consumeTime = consumeTime;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public HttpServletRequest getRequestInfo() {
        return requestInfo == null ? null : (HttpServletRequest) JSON.parse(requestInfo);
    }

    public void setRequestInfo(byte[] requestInfo) {
        this.requestInfo = requestInfo;
    }

    public LoggableQueueAppender getLoggableQueueAppender() {
        return loggableQueueAppender;
    }

    public Map<String, Object> toMap() throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("id", id);
        paramMap.put("url", url);
        paramMap.put("beginTime", beginTime);
        paramMap.put("endTime", endTime);
        paramMap.put("consumeTime", consumeTime);
        paramMap.put("sessionId", sessionId);
        paramMap.put("userId", userId);
        paramMap.put("userIp", userIp);
        paramMap.put("appName", appName);
        paramMap.put("requestInfo", requestInfo);
        return paramMap;
    }

    public LogType getType() {
        return LogType.urlTraceLog;
    }

    public static URLTraceLog beginTrace() {
        URLTraceLog urlTraceLog = new URLTraceLog();
        urlTraceLog.setId(TraceUtils.getTraceId());
        urlTraceLog.setBeginTime(new Timestamp(System.currentTimeMillis()));
        return urlTraceLog;
    }

    public static void endTrace(HttpServletRequest request, URLTraceLog targetURLTraceLog) {
        targetURLTraceLog.setUrl(request.getRequestURI());
        Date endDate = new Date();
        targetURLTraceLog.setEndTime(new Timestamp(System.currentTimeMillis()));
        targetURLTraceLog.setConsumeTime(endDate.getTime() - targetURLTraceLog.getBeginTime().getTime());
        targetURLTraceLog.setSessionId(request.getSession(false).getId());
        targetURLTraceLog.setUserIp(request.getRemoteAddr());
        targetURLTraceLog.setRequestInfo(JSON.toJSONBytes(request));
        targetURLTraceLog.getLoggableQueueAppender().append(targetURLTraceLog);
    }
}
