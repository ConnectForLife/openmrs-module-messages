package org.openmrs.module.messages.domain.criteria;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.openmrs.module.messages.api.dto.ActorResponseDTO;
import org.openmrs.module.messages.api.util.NumericUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ActorResponseCriteria extends ReportCriteria implements Serializable {

    private static final long serialVersionUID = 1148759293978568932L;

    /**
     * The MySQL specific statement which is used to create over label (used to aggregate by week).
     * Based on the answeredTime.
     * The label is created by String concatenation where:
     * - The first string determine the beginning of period (the '-2' in MOD function because of I used Monday
     *      as first day of the week instead of Sunday which would be -1).
     * - The second string its '-' used to join values
     * - The third value determine end day of the period (with month and year)
     */
    private static final String AGGREGATE_BY_WEEK_SQL =
            "CONCAT("
            + "DATE_FORMAT("
            + "     FROM_DAYS("
            + "        TO_DAYS({alias}.answered_time) - MOD(TO_DAYS({alias}.answered_time) -2, 7)),"
            + "        '%d'),"
            + "'-', "
            + "DATE_FORMAT("
                    + "FROM_DAYS("
                    + " TO_DAYS({alias}.answered_time + INTERVAL 1 WEEK) - "
                    + "     MOD(TO_DAYS({alias}.answered_time + INTERVAL 1 WEEK) -2, 7) -1),"
                    + "'%d %b %Y')"
            + ") AS over";

    private static final String CONCEPT_RESPONSE_MODE = "CONCEPT";

    private static final int DEFAULT_DATA_DATE_RANGE = 4;

    private static final int OVER_VALUE_INDEX = 0;

    private static final int COUNT_VALUE_INDEX = 1;

    private static final int RESPONSE_VALUE_INDEX = 2;

    private Integer actorId;

    private Integer patientId;

    private Integer questionId;

    private String textQuestion;

    private List<Integer> possibleResponses = new ArrayList<>();

    private List<String> possibleTestResponses = new ArrayList<>();

    private String responseMode;

    private Integer dataDateRange;

    public ActorResponseCriteria() {
        this.responseMode = CONCEPT_RESPONSE_MODE;
        this.dataDateRange = DEFAULT_DATA_DATE_RANGE;
    }

    public ActorResponseCriteria setActorId(Integer actorId) {
        this.actorId = actorId;
        return this;
    }

    public ActorResponseCriteria setPatientId(Integer patientId) {
        this.patientId = patientId;
        return this;
    }

    public ActorResponseCriteria setQuestionId(Integer questionId) {
        this.questionId = questionId;
        return this;
    }

    public ActorResponseCriteria setTextQuestion(String textQuestion) {
        this.textQuestion = textQuestion;
        return this;
    }

    public ActorResponseCriteria setPossibleResponses(List<Integer> possibleResponses) {
        this.possibleResponses = possibleResponses;
        return this;
    }

    public ActorResponseCriteria setPossibleTestResponses(List<String> possibleTestResponses) {
        this.possibleTestResponses = possibleTestResponses;
        return this;
    }

    public ActorResponseCriteria setResponseMode(String responseMode) {
        if (StringUtils.isNotBlank(responseMode)) {
            this.responseMode = responseMode;
        }
        return this;
    }

    public ActorResponseCriteria setDataDateRange(Integer dataDateRange) {
        if (NumericUtils.isPositive(dataDateRange)) {
            this.dataDateRange = dataDateRange;
        }
        return this;
    }

    @Override
    protected void loadWhereStatements(Criteria hibernateCriteria) {
        addNotNullCriteria(hibernateCriteria, "actor.personId", actorId);
        addNotNullCriteria(hibernateCriteria, "patient.personId", patientId);
        addNotNullCriteria(hibernateCriteria, "question.conceptId", questionId);
        addNotNotBlankCriteria(hibernateCriteria, "text_question", textQuestion);
        addNotNotEmptyCriteria(hibernateCriteria, "response.conceptId", possibleResponses);
        addNotNotEmptyCriteria(hibernateCriteria, "text_response", possibleTestResponses);
        addDateRangeCriteria(hibernateCriteria);
    }

    @Override
    protected void loadOrderBy(Criteria hibernateCriteria) {
        hibernateCriteria.addOrder(Order.asc("answeredTime"));
    }

    @Override
    /**
     * Creates the Criteria projection list which is used to determine the SELECT and the GROUP BY statements.
     * Order of this properties is IMPORTANT ! And used in {@link #createResultTransformer()}.
     */
    protected ProjectionList createProjectionList() {
        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.sqlGroupProjection(
                AGGREGATE_BY_WEEK_SQL, "over", new String[]{"over"}, new Type[]{ StandardBasicTypes.STRING }));
        projectionList.add(Projections.count("textResponse").as("responseCount"));
        createResponseProjections(projectionList);
        return projectionList;
    }

    @Override
    protected ResultTransformer createResultTransformer() {
        return new ResultTransformer() {

            @Override
            /**
             * The order of tuple values is determine by {@link #createProjectionList()} method.
             */
            public Object transformTuple(Object[] tuple, String[] aliases) {
                String response = (tuple[RESPONSE_VALUE_INDEX] instanceof String) ? (String) tuple[RESPONSE_VALUE_INDEX] :
                        ((tuple[RESPONSE_VALUE_INDEX] != null) ? tuple[RESPONSE_VALUE_INDEX].toString() : null);
                return new ActorResponseDTO()
                        .setOver((String) tuple[OVER_VALUE_INDEX])
                        .setResponseCount((Long) tuple[COUNT_VALUE_INDEX])
                        .setResponse(response);
            }

            @Override
            public List transformList(List collection) {
                return collection;
            }
        };
    }

    private void createResponseProjections(ProjectionList projectionList) {
        if (isConceptResponseMode()) {
            projectionList.add(Projections.property("response.conceptId").as("response"));
            projectionList.add(Projections.groupProperty("response.conceptId"));
        } else {
            projectionList.add(Projections.property("textResponse").as("response"));
            projectionList.add(Projections.groupProperty("textResponse"));
        }
    }

    private boolean isConceptResponseMode() {
        return StringUtils.isNotBlank(responseMode) && responseMode.equals(CONCEPT_RESPONSE_MODE);
    }

    private void addNotNullCriteria(Criteria hibernateCriteria, String property, Object value) {
        if (value != null) {
            hibernateCriteria.add(Restrictions.eq(property, value));
        }
    }

    private void addNotNotBlankCriteria(Criteria hibernateCriteria, String property, String value) {
        if (StringUtils.isNotBlank(value)) {
            hibernateCriteria.add(Restrictions.like(property, value, MatchMode.ANYWHERE));
        }
    }

    private void addNotNotEmptyCriteria(Criteria hibernateCriteria, String property, Collection value) {
        if (CollectionUtils.isNotEmpty(value)) {
            hibernateCriteria.add(Restrictions.in(property, value));
        }
    }

    private void addDateRangeCriteria(Criteria hibernateCriteria) {
        hibernateCriteria.add(Restrictions.sqlRestriction(
                "{alias}.answered_time > DATE(CURRENT_DATE() - DAYOFWEEK(CURRENT_DATE() - 1) - INTERVAL ? WEEK)",
                new Integer[]{dataDateRange}, new Type[]{ StandardBasicTypes.INTEGER }));
    }
}
