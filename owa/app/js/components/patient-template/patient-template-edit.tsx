/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import React, {ReactFragment} from 'react';
import {connect} from 'react-redux';
import {RouteComponentProps} from 'react-router-dom';
import {getPatientTemplates, getTemplates, putPatientTemplates} from '../../reducers/patient-template.reducer'
import {getActorList} from '../../reducers/actor.reducer';
import {IRootState} from '../../reducers';
import {Button, SelectCallback} from 'react-bootstrap';
import * as Default from '../../shared/utils/messages';
import FormSection from '../form-entry/model/form-section';
import FormSubSection from '../form-entry/model/form-subsection';
import FormEntry from '../form-entry/form-entry';
import './patient-template.scss';
import PatientTemplateForm from './patient-template-form';
import {TemplateUI} from '../../shared/model/template-ui';
import {getPatientTemplateWithActorId, getPatientTemplateWithTemplateId} from '../../selectors/patient-template-selector';
import {PatientTemplateUI} from '../../shared/model/patient-template-ui';
import _ from 'lodash';
import {IActor} from '../../shared/model/actor.model';
import {getActorTypes, getHealthTipCategories} from '../../reducers/admin-settings.reducer';
import Timezone from '../timezone/timezone';
import {DashboardType} from '../../shared/model/dashboard-type';
import { LocalizedMessage } from '@openmrs/react-components';

interface IPatientTemplateEditProps extends DispatchProps, StateProps, RouteComponentProps<{
  patientId: string,
  patientUuid: string,
  activeSection: string,
  dashboardType: DashboardType
}> {
  isNew: boolean
  relatedActors: Array<IActor>
  locale?: string
};

interface IPatientTemplateEditState {
};

class PatientTemplateEdit extends React.PureComponent<IPatientTemplateEditProps, IPatientTemplateEditState> {

  constructor(props: IPatientTemplateEditProps) {
    super(props);
  }

  componentDidMount() {
    this.props.getHealthTipCategories();
    this.props.getTemplates();
    this.props.getActorList(parseInt(this.props.match.params.patientId));
    this.props.getActorTypes();
    this.props.getPatientTemplates(parseInt(this.props.match.params.patientId));
  }

  private isPatient() {
    const dashboardType = this.props.match.params.dashboardType;
    return !!dashboardType ? dashboardType.toUpperCase() === DashboardType.PATIENT : true;
  }

  handleSave = () => {
    const { patientId, patientUuid, dashboardType } = this.props.match.params;
    this.props.putPatientTemplates(
      this.props.patientTemplates,
      this.props.templates,
      parseInt(patientId),
      patientUuid,
      dashboardType,
      this.props.locale
    );
  }

  handleCancel = () => this.props.history.goBack();

  handleNext = () => {
    const nextSubsection = this.getNextSubsection();
    return !!nextSubsection && this.changeLocation(this.getTemplateName(nextSubsection));
  };

  resolveSubsectionUrl = (subsectionName: string) => {
    const { patientId, patientUuid, dashboardType } = this.props.match.params;
    return `/messages/${dashboardType}/${patientId}&patientuuid=${patientUuid}/patient-template/edit/${subsectionName}`;
  };

  getNextSubsection = () => {
    const { templates } = this.props;
    const currentTemplateName = this.props.match.params.activeSection;
    const nextTemplateIndex = templates.findIndex(template => template.name === currentTemplateName) + 1;
    return nextTemplateIndex < templates.length ? templates[nextTemplateIndex] : null;
  };

  getTemplateName = (template: TemplateUI) => template.name ? template.name : `Template ${template.localId}`;

  changeLocation = (activeSection: string) => this.props.history.replace(this.resolveSubsectionUrl(activeSection));

  renderTemplateState = () => {
    const sections = this.mapTemplatesToSections();
    if (this.props.isNew) {
      return (
        <FormEntry sections={sections} activeSection={this.props.match.params.activeSection} />
      );
    } else {
      return (
        <FormEntry sections={sections} activeSection={this.props.match.params.activeSection} />
      );
    }
  }

  buildActorTemplate = (patientTemplates: ReadonlyArray<PatientTemplateUI>, template: TemplateUI, actor?: IActor): ReactFragment =>
    <PatientTemplateForm key={`template-form-${actor ? actor.actorId : parseInt(this.props.match.params.patientId)}`}
      patientTemplate={patientTemplates.length ? patientTemplates[0] : undefined}
      template={template}
      patientId={parseInt(this.props.match.params.patientId)}
      actor={actor}
    />

  mapTemplatesToSections = (): Array<FormSection> => {
    const sections = [] as Array<FormSection>;
    const subsections = [] as Array<FormSubSection>;
    const patientId = parseInt(this.props.match.params.patientId);

    let templates = this.props.templates;
    templates = _.orderBy(templates, ['createdAt'], ['asc']);
    templates.forEach((template: TemplateUI) => {
      const name = this.getTemplateName(template);

      const patientTemplates: ReadonlyArray<PatientTemplateUI> =
        getPatientTemplateWithTemplateId(this.props.patientTemplates, template.id!);

      const fragment =
        <>
          {
            this.buildActorTemplate(
              getPatientTemplateWithActorId(patientTemplates, patientId), template)
          }
          {
            this.props.relatedActors.map((actor) =>
              this.buildActorTemplate(
                getPatientTemplateWithActorId(patientTemplates, actor.actorId), template, actor))
          }
        </>

      const isValid = !_.some(patientTemplates, pt => pt.hasErrors());
      const isPersisted = _.some(patientTemplates, pt => pt.isPersisted);

      const onSelectCallback: SelectCallback = () => this.changeLocation(name);

      subsections.push(new FormSubSection(name, isValid, isPersisted, fragment, onSelectCallback));
    });

    sections.push(new FormSection('Messages', subsections));
    return sections;
  }

  render() {
    return (
      <>
        <Timezone />
        <div className="panel-body">
          <h2><LocalizedMessage id="MESSAGES_EDIT_MESSAGES_TITLE" defaultMessage={Default.EDIT_MESSAGES_TITLE} /></h2>
          {!this.props.loading && this.renderTemplateState()}
        </div>
        <div className="panel-body">
          <Button
            className="btn btn-danger btn-md"
            onClick={this.handleCancel}>
            <LocalizedMessage id="MESSAGES_CANCEL_BUTTON_LABEL" defaultMessage={Default.CANCEL_BUTTON_LABEL} />
          </Button>
          <div className="pull-right">
            <Button
              className="btn btn-default btn-md sec-btn"
              disabled={!this.getNextSubsection()}
              onClick={this.handleNext}>
              <LocalizedMessage id="MESSAGES_NEXT_BUTTON_LABEL" defaultMessage={Default.NEXT_BUTTON_LABEL} />
            </Button>
            <Button
              className="btn btn-success btn-md confirm"
              onClick={this.handleSave}>
              <LocalizedMessage id="MESSAGES_SAVE_BUTTON_LABEL" defaultMessage={Default.SAVE_BUTTON_LABEL} />
            </Button>
          </div>
        </div>
      </>
    );
  }
}

const mapStateToProps = ({ actor, patientTemplate, adminSettings }: IRootState) => ({
  templates: patientTemplate.templates,
  patientTemplates: patientTemplate.patientTemplates,
  loading: patientTemplate.patientTemplatesLoading || patientTemplate.templatesLoading || adminSettings.loading,
  relatedActors: actor.actorResultList
});

const mapDispatchToProps = ({
  getTemplates,
  getPatientTemplates,
  putPatientTemplates,
  getActorList,
  getActorTypes,
  getHealthTipCategories
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PatientTemplateEdit);
