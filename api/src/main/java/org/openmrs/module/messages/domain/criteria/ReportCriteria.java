package org.openmrs.module.messages.domain.criteria;

import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.transform.ResultTransformer;

public abstract class ReportCriteria extends BaseCriteria {

    private static final long serialVersionUID = -3041867659416133582L;

    protected abstract void loadWhereStatements(Criteria hibernateCriteria);

    @Override
    public void loadHibernateCriteria(Criteria hibernateCriteria) {
        loadProjections(hibernateCriteria);
        loadResultTransformer(hibernateCriteria);
        loadWhereStatements(hibernateCriteria);
        loadOrderBy(hibernateCriteria);
    }

    protected ProjectionList createProjectionList() {
        return null;
    }

    protected ResultTransformer createResultTransformer() {
        return null;
    }

    protected void loadOrderBy(Criteria hibernateCriteria) {
    }

    private void loadProjections(Criteria hibernateCriteria) {
        ProjectionList projectionList = createProjectionList();
        if (projectionList != null) {
            hibernateCriteria.setProjection(projectionList);
        }
    }

    private void loadResultTransformer(Criteria hibernateCriteria) {
        ResultTransformer resultTransformer = createResultTransformer();
        if (resultTransformer != null) {
            hibernateCriteria.setResultTransformer(resultTransformer);
        }
    }
}
