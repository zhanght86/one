package com.sinosoft.one.monitor.threshold.model;
// Generated 2013-3-1 10:29:53 by One Data Tools 1.0.0


import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.*;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Threshold.
* 阈值信息表
 */
@Entity
@Table(name="GE_MONITOR_THRESHOLD"
)
public class Threshold  implements java.io.Serializable {

    /**
    * 主键ID.
    */
    private String id;
    /**
    * 名称.
    */
    private String name;
    /**
    * 描述.
    */
    private String description;
    /**
    * 临界阈值条件.
    */
    private String criticalThresholdCondition;
    /**
    * 临界阈值值.
    */
    private BigDecimal criticalThresholdValue;
    /**
    * 临界阈值信息.
    */
    private String criticalThresholdMessage;
    /**
    * 警告阈值条件.
    */
    private String warningThresholdCondition;
    /**
    * 警告阈值值.
    */
    private BigDecimal warningThresholdValue;
    /**
    * 警告阈值信息.
    */
    private String warningThresholdMessage;
    /**
    * 正常阈值条件.
    */
    private String infoThresholdCondition;
    /**
    * 正常阈值值.
    */
    private BigDecimal infoThresholdValue;
    /**
    * 正常阈值信息.
    */
    private String infoThresholdMessage;
	/**
	 * 结果信息
	 */
	private String resultMessage;
	/**
	 * 条件符号
	 */
	private String conditionStr;

    public Threshold() {
    }

	
    public Threshold(String id) {
        this.id = id;
    }
   
    @Id 
    
    @Column(name="id", unique=true, length=32)
    public String getId() {
    return this.id;
    }

    public void setId(String id) {
    this.id = id;
    }
    
    @Column(name="name", length=100)
    public String getName() {
    return this.name;
    }

    public void setName(String name) {
    this.name = name;
    }
    
    @Column(name="description", length=250)
    public String getDescription() {
    return this.description;
    }

    public void setDescription(String description) {
    this.description = description;
    }
    
    @Column(name="critical_threshold_condition", length=2)
    public String getCriticalThresholdCondition() {
    return this.criticalThresholdCondition;
    }

    public void setCriticalThresholdCondition(String criticalThresholdCondition) {
    this.criticalThresholdCondition = criticalThresholdCondition;
    }
    
    @Column(name="critical_threshold_value", precision=22, scale=0)
    public BigDecimal getCriticalThresholdValue() {
    return this.criticalThresholdValue;
    }

    public void setCriticalThresholdValue(BigDecimal criticalThresholdValue) {
    this.criticalThresholdValue = criticalThresholdValue;
    }
    
    @Column(name="critical_threshold_message", length=250)
    public String getCriticalThresholdMessage() {
    return this.criticalThresholdMessage;
    }

    public void setCriticalThresholdMessage(String criticalThresholdMessage) {
    this.criticalThresholdMessage = criticalThresholdMessage;
    }
    
    @Column(name="warning_threshold_condition", length=2)
    public String getWarningThresholdCondition() {
    return this.warningThresholdCondition;
    }

    public void setWarningThresholdCondition(String warningThresholdCondition) {
    this.warningThresholdCondition = warningThresholdCondition;
    }
    
    @Column(name="warning_threshold_value", precision=22, scale=0)
    public BigDecimal getWarningThresholdValue() {
    return this.warningThresholdValue;
    }

    public void setWarningThresholdValue(BigDecimal warningThresholdValue) {
    this.warningThresholdValue = warningThresholdValue;
    }
    
    @Column(name="warning_threshold_message", length=250)
    public String getWarningThresholdMessage() {
    return this.warningThresholdMessage;
    }

    public void setWarningThresholdMessage(String warningThresholdMessage) {
    this.warningThresholdMessage = warningThresholdMessage;
    }
    
    @Column(name="info_threshold_condition", length=2)
    public String getInfoThresholdCondition() {
    return this.infoThresholdCondition;
    }

    public void setInfoThresholdCondition(String infoThresholdCondition) {
    this.infoThresholdCondition = infoThresholdCondition;
    }
    
    @Column(name="info_threshold_value", precision=22, scale=0)
    public BigDecimal getInfoThresholdValue() {
    return this.infoThresholdValue;
    }

    public void setInfoThresholdValue(BigDecimal infoThresholdValue) {
    this.infoThresholdValue = infoThresholdValue;
    }
    
    @Column(name="info_threshold_message", length=250)
    public String getInfoThresholdMessage() {
    return this.infoThresholdMessage;
    }

    public void setInfoThresholdMessage(String infoThresholdMessage) {
    this.infoThresholdMessage = infoThresholdMessage;
    }

	@Transient
	public String getResultMessage() {
		return resultMessage;
	}

	@Transient
	public String getConditionStr() {
		return conditionStr;
	}

	/**
	 * 计算严重级别
	 * @param attributeValue 属性值
	 * @return 严重级别
	 */
	public SeverityLevel evalSeverityLevel(BigDecimal attributeValue) {
		String condition = this.getCriticalThresholdCondition();
		BigDecimal conditionValue = this.getCriticalThresholdValue();
		if(isThisLevel(condition, conditionValue, attributeValue)) {
			resultMessage = attributeValue + " " + conditionStr + " " + conditionValue;
			return SeverityLevel.CRITICAL;
		}

		condition = this.getWarningThresholdCondition();
		conditionValue = this.getWarningThresholdValue();
		if(isThisLevel(condition, conditionValue, attributeValue)) {
			resultMessage = attributeValue + " " + conditionStr + " " + conditionValue;
			return SeverityLevel.WARNING;
		}

		condition = this.getInfoThresholdCondition();
		conditionValue = this.getInfoThresholdValue();
		if(isThisLevel(condition, conditionValue, attributeValue)) {
			resultMessage = attributeValue + " " + conditionStr + " " + conditionValue;
			return SeverityLevel.INFO;
		}
		return SeverityLevel.UNKNOW;
	}

	private boolean isThisLevel(String condition, BigDecimal conditionValue, BigDecimal attributeValue) {
		if(ThresholdConditions.EQ.name().equals(condition)) {
			condition = ThresholdConditions.EQ.symbol();
			return conditionValue.equals(attributeValue);
		}
		if(ThresholdConditions.NE.name().equals(condition)) {
			condition = ThresholdConditions.NE.symbol();
			return !conditionValue.equals(attributeValue);
		}
		if(ThresholdConditions.LT.equals(condition)) {
			condition = ThresholdConditions.LT.symbol();
			return attributeValue.compareTo(conditionValue) < 0;
		}
		if(ThresholdConditions.LE.equals(condition)) {
			condition = ThresholdConditions.LE.symbol();
			return attributeValue.compareTo(conditionValue) <= 0;
		}
		if(ThresholdConditions.GT.equals(condition)) {
			condition = ThresholdConditions.GT.symbol();
			return attributeValue.compareTo(conditionValue) > 0;
		}
		if(ThresholdConditions.GE.equals(condition)) {
			condition = ThresholdConditions.GE.symbol();
			return attributeValue.compareTo(conditionValue) >= 0;
		}
		throw new RuntimeException("invalid condition : " + condition);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}

