package com.sinosoft.ebusiness.test.rule.service.spring;

import org.springframework.beans.factory.annotation.Autowired;

import com.sinosoft.ebusiness.rule.service.facade.DroolsRuleService;
import com.sinosoft.ebusiness.test.rule.service.facade.OtherRuleService;

public class OtherRuleServiceSpringImpl implements OtherRuleService {
	@Autowired
	public DroolsRuleService droolsRuleService;

	public void executeRule(Object fact) throws Exception {
		droolsRuleService.executeRules("otherChangeSet.xml", fact);
	}

}