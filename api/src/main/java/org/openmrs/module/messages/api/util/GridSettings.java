package org.openmrs.module.messages.api.util;

import org.openmrs.Patient;
import org.openmrs.module.messages.domain.PagingInfo;
import org.openmrs.module.messages.domain.criteria.PatientTemplateCriteria;

/**
 * Models the audit log filter settings UI
 */
public class GridSettings {

    /**
     * The default page to display.
     */
    private static final int DEFAULT_PAGE_INDEX = 1;

    /**
     * The default value of number of rows to display per page.
     */
    private static final int DEFAULT_PAGE_SIZE = 100;

    /**
     * The number of rows to display per page.
     */
    private Integer rows;

    /**
     * The page to display, starting from 1.
     */
    private Integer page;

    /**
     * The id of patient to search for
     */
    private Integer patientId;

    public Integer getRows() {
        return rows;
    }

    public GridSettings setRows(Integer rows) {
        this.rows = rows;
        return this;
    }

    public Integer getPage() {
        return page;
    }

    public GridSettings setPage(Integer page) {
        this.page = page;
        return this;
    }

    public GridSettings setPatientId(Integer patientId) {
        this.patientId = patientId;
        return this;
    }

    public Integer getPatientId() {
        return patientId;
    }

    /**
     * Gets {@link PagingInfo} containing the paging configuration form grid settings
     * @return the newly created paging information
     */
    public PagingInfo getPagingInfo() {
        Integer pageIndex = getPage();
        Integer pageSize = getRows();
        return new PagingInfo(pageIndex != null ? pageIndex : DEFAULT_PAGE_INDEX,
                pageSize != null ? pageSize : DEFAULT_PAGE_SIZE);
    }

    public PatientTemplateCriteria getCriteria() {
        return new PatientTemplateCriteria(wrap(patientId));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("GridSettings{ ");
        if (rows != null) {
            builder.append("rows=").append(rows).append(", ");
        }
        if (page != null) {
            builder.append("page=").append(page).append(", ");
        }
        if (patientId != null) {
            builder.append("patientId=").append(patientId);
        }
        builder.append(" }");
        return builder.toString();
    }

    private Patient wrap(Integer patientId) {
        Patient patient = new Patient();
        patient.setId(patientId);
        return patient;
    }
}
