package org.openmrs.module.messages.api.dao.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.messages.ContextSensitiveTest;
import org.openmrs.module.messages.api.dao.ExtendedSchedulerDao;
import org.openmrs.module.messages.api.service.DatasetConstants;
import org.openmrs.scheduler.TaskDefinition;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;

public class ExtendedSchedulerDaoImplITTest extends ContextSensitiveTest {
  private static final String TASKS_XML_DATA_SET_PATH =
      DatasetConstants.XML_DATA_SET_PATH + "ExtendedSchedulerDaoImplITTest/TaskDefinitions.xml";

  @Autowired
  private ExtendedSchedulerDao extendedSchedulerDao;

  @Before
  public void setup() throws Exception {
    executeDataSet(TASKS_XML_DATA_SET_PATH);
  }

  @Test
  public void shouldReturnTasksByPrefixAndAfterStartTime() {
    final List<TaskDefinition> tasks =
        extendedSchedulerDao.getTasksByPrefixAndAfterStartTime("a:Call:", Instant.parse("2023-01-03T00:00:00.00Z"));

    Assert.assertEquals("It should return only one task.", 1, tasks.size());
    Assert.assertEquals("Only the one specific task should be returned.", "fbf2440f-a34f-4c52-9693-d7cc44e25fa2",
        tasks.get(0).getUuid());
  }
}
