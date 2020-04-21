package org.openmrs.module.messages.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.openmrs.Person;
import org.openmrs.module.messages.util.WebConstants;
import org.openmrs.ui.framework.fragment.FragmentConfiguration;

import java.io.Serializable;
import java.util.List;

public class GraphConfigurationDTO implements Serializable {

    private static final long serialVersionUID = -2276860426040335510L;

    private Integer actorId;
    private Integer patientId;
    private Integer questionId;
    private Integer dataDateRange;
    private String responseMode;
    private String aggregateMode;
    private String textQuestion;
    private List<Integer> possibleResponses;
    private List<String> possibleTextResponses;

    public Integer getActorId() {
        return this.actorId;
    }

    public GraphConfigurationDTO setActorId(Integer actorId) {
        this.actorId = actorId;
        return this;
    }

    public Integer getPatientId() {
        return this.patientId;
    }

    public GraphConfigurationDTO setPatientId(Integer patientId) {
        this.patientId = patientId;
        return this;
    }

    public Integer getQuestionId() {
        return this.questionId;
    }

    public GraphConfigurationDTO setQuestionId(Integer questionId) {
        this.questionId = questionId;
        return this;
    }

    public Integer getDataDateRange() {
        return this.dataDateRange;
    }

    public GraphConfigurationDTO setDataDateRange(Integer dataDateRange) {
        this.dataDateRange = dataDateRange;
        return this;
    }

    public String getResponseMode() {
        return this.responseMode;
    }

    public GraphConfigurationDTO setResponseMode(String responseMode) {
        this.responseMode = responseMode;
        return this;
    }

    public String getAggregateMode() {
        return this.aggregateMode;
    }

    public GraphConfigurationDTO setAggregateMode(String aggregateMode) {
        this.aggregateMode = aggregateMode;
        return this;
    }

    public String getTextQuestion() {
        return this.textQuestion;
    }

    public GraphConfigurationDTO setTextQuestion(String textQuestion) {
        this.textQuestion = textQuestion;
        return this;
    }

    public List<Integer> getPossibleResponses() {
        return this.possibleResponses;
    }

    public GraphConfigurationDTO setPossibleResponses(List<Integer> possibleResponses) {
        this.possibleResponses = possibleResponses;
        return this;
    }

    public List<String> getPossibleTextResponses() {
        return this.possibleTextResponses;
    }

    public GraphConfigurationDTO setPossibleTextResponses(List<String> possibleTextResponses) {
        this.possibleTextResponses = possibleTextResponses;
        return this;
    }

    public GraphConfigurationDTO withFragmentConfiguration(FragmentConfiguration configuration, Person person) {
        this.setActorId((person == null) ? null : person.getPersonId())
                .setPatientId(person.isPatient() ? (Integer) configuration.getAttribute(WebConstants.PATIENT_ID) : null)
                .setQuestionId((Integer) configuration.getAttribute(WebConstants.QUESTION_ID))
                .setDataDateRange((Integer) configuration.getAttribute(WebConstants.DATA_DATE_RANGE))
                .setResponseMode((String) configuration.getAttribute(WebConstants.RESPONSE_MODE))
                .setAggregateMode((String) configuration.getAttribute(WebConstants.AGGREGATE_MODE))
                .setTextQuestion((String) configuration.getAttribute(WebConstants.TEXT_QUESTION))
                .setPossibleResponses((List<Integer>) configuration.getAttribute(WebConstants.POSSIBLE_RESPONSES))
                .setPossibleTextResponses((List<String>) configuration.getAttribute(WebConstants.POSSIBLE_TEXT_RESPONSES));
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
