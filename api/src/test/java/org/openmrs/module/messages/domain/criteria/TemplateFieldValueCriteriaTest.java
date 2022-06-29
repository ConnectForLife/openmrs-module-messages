package org.openmrs.module.messages.domain.criteria;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class TemplateFieldValueCriteriaTest {

    @Mock
    private Criteria criteria;


    @Test
    public void shouldAddHibernateCriteria() {
        when(criteria.createAlias(anyString(), anyString())).thenReturn(criteria);

        TemplateFieldValueCriteria templateFieldValueCriteria =
                new TemplateFieldValueCriteria().setPatientTemplate(new PatientTemplate())
                        .setFieldTypeName("Frequency");

        ArgumentCaptor<Criterion> captor = ArgumentCaptor.forClass(Criterion.class);

        templateFieldValueCriteria.loadHibernateCriteria(criteria);

        verify(criteria, times(2)).add(captor.capture());
    }
}
