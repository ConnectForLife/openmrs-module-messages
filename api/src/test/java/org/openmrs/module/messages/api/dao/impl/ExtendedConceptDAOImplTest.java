package org.openmrs.module.messages.api.dao.impl;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.ConceptAttribute;
import org.openmrs.module.messages.ContextSensitiveTest;
import org.openmrs.module.messages.api.dao.ExtendedConceptDAO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ExtendedConceptDAOImplTest extends ContextSensitiveTest {

    @Autowired
    private ExtendedConceptDAO dao;

    @Before
    public void setUp() throws Exception {
        executeDataSet("datasets/ConceptDataSet.xml");
        executeDataSet("datasets/extendedConceptDAOTestData/ConceptAttributesDataSet.xml");
    }

    @Test
    public void shouldGetAllNonVoidedConceptAttributesByTypeUuid() {
        List<ConceptAttribute> actual = dao.getConceptAttributesByTypeUuid("236aeedd-dc22-11ec-81c5-0242ac140002");

        assertNotNull(actual);
        assertEquals(3, actual.size());
    }
}
