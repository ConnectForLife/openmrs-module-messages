package org.openmrs.module.messages.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.openmrs.Person;
import org.openmrs.module.messages.util.WebConstants;
import org.openmrs.ui.framework.fragment.FragmentConfiguration;

import java.io.Serializable;

public class GraphConfigurationDTO implements Serializable {

    private static final long serialVersionUID = -2276860426040335510L;

    private Integer actorId;

    private Integer patientId;

    private String graphDataSetName;

    private String responseAlias;

    private String countResultAlias;

    private String groupByAlias;

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

    public String getGraphDataSetName() {
        return graphDataSetName;
    }

    public GraphConfigurationDTO setGraphDataSetName(String graphDataSetName) {
        this.graphDataSetName = graphDataSetName;
        return this;
    }

    public String getGroupByAlias() {
        return groupByAlias;
    }

    public GraphConfigurationDTO setGroupByAlias(String groupByAlias) {
        this.groupByAlias = groupByAlias;
        return this;
    }

    public String getResponseAlias() {
        return responseAlias;
    }

    public GraphConfigurationDTO setResponseAlias(String responseAlias) {
        this.responseAlias = responseAlias;
        return this;
    }

    public String getCountResultAlias() {
        return countResultAlias;
    }

    public GraphConfigurationDTO setCountResultAlias(String countResultAlias) {
        this.countResultAlias = countResultAlias;
        return this;
    }

    public GraphConfigurationDTO withFragmentConfiguration(FragmentConfiguration configuration, Person person) {
        this.setActorId((person != null) ? person.getPersonId() : null)
                .setPatientId(person != null && person.getIsPatient() ?
                        (Integer) configuration.getAttribute(WebConstants.PATIENT_ID) : null)
                .setGraphDataSetName((String) configuration.getAttribute(WebConstants.GRAPH_DATA_SET_NAME))
                .setResponseAlias((String) configuration.getAttribute(WebConstants.RESPONSE_ALIAS_NAME))
                .setCountResultAlias((String) configuration.getAttribute(WebConstants.COUNT_RESULT_ALIAS_NAME))
                .setGroupByAlias((String) configuration.getAttribute(WebConstants.GROUP_BY_ALIAS_NAME));
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
