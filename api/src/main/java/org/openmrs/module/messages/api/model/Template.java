package org.openmrs.module.messages.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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
    
    @Column(name = "service_query_type", nullable = false)
    private String serviceQueryType;
    
    @Override
    public Integer getId() {
        return id;
    }
    
    @Override
    public void setId(Integer id) {
        this.id = id;
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
}
