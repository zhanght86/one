package com.sinosoft.one.service.spring;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.sinosoft.one.rms.model.Task;
import com.sinosoft.one.rms.service.facade.TaskService;
//
@DirtiesContext
@ContextConfiguration(locations = { "/spring/applicationContext-rmstest.xml",
        "/spring/applicationContext-rms.xml" })
@TransactionConfiguration(transactionManager = "defaultTransactionManager",defaultRollback=false)
public class TaskServiceSpringImplTest extends AbstractJUnit4SpringContextTests{
	@Autowired
	private TaskService rmstaskService;
	@Test
	public void test(){
		Set<Task>tasks=  rmstaskService.findTaskAuthByComCode("00");
		Assert.assertNotNull(tasks);
		tasks=rmstaskService.findTaskAuthByComCodeAndsysFlag("00", "RMS");
		Assert.assertNotNull(tasks);
	}
}
