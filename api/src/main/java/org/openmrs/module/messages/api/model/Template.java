package org.openmrs.module.messages.api.model;

import org.apache.commons.lang.StringUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "messages.Template")
@Table(name = "messages_template")
public class Template extends AbstractBaseOpenmrsData {
    
    private static final long serialVersionUID = -5344732333848745166L;
    
    @Id
    @GeneratedValue
    @Column(name = "messages_template_id")
    private Integer id;
    
    @Column(name = "service_query", columnDefinition = "text", nullable = false)
    private String serviceQuery;

    @Column(name = "calendar_service_query", columnDefinition = "text")
    private String calendarServiceQuery;
    
    @Column(name = "service_query_type", nullable = false)
    private String serviceQueryType;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "template", orphanRemoval = true)
    private List<TemplateField> templateFields = new ArrayList<>();

    @OneToMany(mappedBy = "template")
    private List<PatientTemplate> patientTemplates = new ArrayList<>();

    @Column(name = "name", nullable = false)
    private String name;

    public Template() {
        super();
    }

    public Template(Integer id) {
        super();
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Template#" + id;
    }

    public String getServiceQuery() {
        return serviceQuery;
    }

    public void setServiceQuery(String serviceQuery) {
        this.serviceQuery = serviceQuery;
    }

    public String getServiceQueryType() {
        return serviceQueryType;
    }

    public void setServiceQueryType(String serviceQueryType) {
        this.serviceQueryType = serviceQueryType;
    }

    public List<TemplateField> getTemplateFields() {
        return templateFields;
    }

    public void setTemplateFields(List<TemplateField> templateFields) {
        this.templateFields = templateFields;
    }

    public List<PatientTemplate> getPatientTemplates() {
        return patientTemplates;
    }

    public void setPatientTemplates(List<PatientTemplate> patientTemplates) {
        this.patientTemplates = patientTemplates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns query which is used in a calendar view. If this field is empty it returns service query which is used
     * in scheduler job. This calendar query is not mandatory and is typically used to improve performance of
     * calendar displaying
     *
     * @return calendar service query
     */
    public String getCalendarServiceQuery() {
        return StringUtils.isBlank(calendarServiceQuery) ? getServiceQuery() : calendarServiceQuery;
    }

    public void setCalendarServiceQuery(String calendarServiceQuery) {
        this.calendarServiceQuery = calendarServiceQuery;
    }
}
