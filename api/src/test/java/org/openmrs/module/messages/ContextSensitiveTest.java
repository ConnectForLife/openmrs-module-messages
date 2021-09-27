package org.openmrs.module.messages;

import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.test.context.ContextConfiguration;

// The order of context configuration classes is different than in BaseModuleContextSensitiveTest,
// so that we don't have to override every bean for test, but only those we need to change
@ContextConfiguration(
        locations = {"classpath:applicationContext-service.xml", "classpath*:moduleApplicationContext.xml",
                "classpath*:TestingApplicationContext.xml"}, inheritLocations = false)
public abstract class ContextSensitiveTest extends BaseModuleContextSensitiveTest {
}
