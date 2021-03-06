package com.sinosoft.one.monitor.log;


import org.apache.commons.lang3.builder.ToStringBuilder;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 方法追踪日志.
 * User: carvin
 * Date: 12-11-28
 * Time: 上午6:12
 */
public class MethodTraceLog {
	private static final String FORMAT_STRING_PREFIX = "[@MethodTrace]";
	public static final String FORMAT_STRING = FORMAT_STRING_PREFIX  + "{}[@traceId={}] 在 {}" +
			" 调用类 {} 的方法 {}, 传入参数 {}, 返回 {}, 在 {} 结束, 经历时长为 {}";
	/**
	 * 主键ID
	 */
    private String id;
	/**
	 * URL追踪ID
	 */
    private String urlTraceLogId;
	/**
	 * 方法名
	 */
    private String methodName;
	/**
	 * 方法所属类名
	 */
    private String className;
	/**
	 * 方法参数
	 */
    private String inParam;
	/**
	 * 方法返回值
	 */
    private String outParam;
	/**
	 * 开始时间
	 */
    private Timestamp beginTime;
	/**
	 * 结束时间
	 */
    private Timestamp endTime;
	/**
	 * 花费时间
	 */
    private long consumeTime;
	/**
	 * 记录时间
	 */
    private Date recordTime;
	/**
	 * 方法ID
 	 */
	private String methodId;

    public MethodTraceLog() {

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrlTraceLogId() {
        return urlTraceLogId;
    }

    public void setUrlTraceLogId(String urlTraceLogId) {
        this.urlTraceLogId = urlTraceLogId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getInParam() {
        return inParam;
    }

    public void setInParam(String inParam) {
        this.inParam = inParam;
    }

    public String getOutParam() {
        return outParam;
    }

    public void setOutParam(String outParam) {
        this.outParam = outParam;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Timestamp beginTime) {
        this.beginTime = beginTime;
    }

    public Timestamp getEndTime() {
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

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public String getFullMethodName() {
        return getClassName() + "." + getMethodName();
    }

	public String getMethodId() {
		return methodId;
	}

	public void setMethodId(String methodId) {
		this.methodId = methodId;
	}

	public Object[] toObjectArray() {
		return new Object[]{
				id,
				urlTraceLogId,
				beginTime,
				className,
				methodName,
				inParam,
				outParam,
				endTime,
				consumeTime
		};
	}
	@Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", id)
            .append("urlTraceLogId", urlTraceLogId)
            .append("methodName", methodName)
            .append("className", className)
            .append("inParam", inParam)
            .append("outParam", outParam)
            .append("beginTime", beginTime)
            .append("endTime", endTime)
            .append("consumeTime", consumeTime)
            .append("recordTime", recordTime)
            .build();
    }
}
