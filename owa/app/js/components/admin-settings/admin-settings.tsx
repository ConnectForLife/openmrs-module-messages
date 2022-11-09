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
import {IRootState} from '../../reducers';
import {Button, SelectCallback} from 'react-bootstrap';
import * as Default from '../../shared/utils/messages';
import FormSection from '../form-entry/model/form-section';
import FormSubSection from '../form-entry/model/form-subsection';
import FormEntry from '../form-entry/form-entry';
import './admin-settings.scss';
import {TemplateForm} from '../default-settings/template-form';
import {TemplateUI} from '../../shared/model/template-ui';
import {getActorTypes, getHealthTipCategories, updateTemplate} from '../../reducers/admin-settings.reducer';
import {IActorType} from '../../shared/model/actor-type.model';
import {history} from '../../config/redux-store';
import {IHealthTipCategory} from "../../shared/model/health-tip-category.model";
import { LocalizedMessage } from '@openmrs/react-components';

interface IAdminSettingsProps extends DispatchProps, StateProps {
  templates: Array<TemplateUI>,
  actorTypes: Array<IActorType>,
  activeSection: string,
  onSaveCallback?: Function,
  healthTipCategories: IHealthTipCategory[]
}

interface IAdminSettingsState {
};

class AdminSettings extends React.PureComponent<IAdminSettingsProps, IAdminSettingsState> {

  constructor(props: IAdminSettingsProps) {
    super(props);
  }

  componentDidMount() {
    this.props.getActorTypes();
    this.props.getHealthTipCategories();
  }

  componentDidUpdate(prevProps: Readonly<IAdminSettingsProps>, prevState: Readonly<IAdminSettingsState>, snapshot?: any): void {
    if (!this.props.activeSection && this.props.templates.length) {
      this.changeLocation(this.props.templates[0].name!);
    }
  }

  handleSave = () => {
    if (!!this.props.onSaveCallback) {
      this.props.onSaveCallback();
    }
  };

  handleCancel = () => history.goBack();

  handleNext = () => {
    const nextSubsection = this.getNextSubsection();
    return !!nextSubsection && this.changeLocation(this.getTemplateName(nextSubsection));
  };

  resolveSubsectionUrl = (subsectionName: string) => {
    return `/messages/manage/${subsectionName}`;
  };

  getNextSubsection = () => {
    const { templates } = this.props;
    const currentTemplateName = this.props.activeSection;
    const nextTemplateIndex = templates.findIndex(template => template.name === currentTemplateName) + 1;
    return nextTemplateIndex < templates.length ? templates[nextTemplateIndex] : null;
  };

  getTemplateName = (template: TemplateUI) => template.name ? template.name : `Template ${template.localId}`;

  changeLocation = (activeSection: string) => history.replace(this.resolveSubsectionUrl(activeSection));

  renderTemplateState = () => {
    const sections = this.mapTemplatesToSections();
    return (
      <FormEntry sections={sections} activeSection={this.props.activeSection} />
    );
  };

  buildTemplateForm = (template: TemplateUI, actorType: IActorType): ReactFragment => (
    <TemplateForm
      key={`template-form-${template.localId}-${actorType.display}`}
      template={template}
      updateTemplate={this.props.updateTemplate}
      healthTipCategories={this.props.healthTipCategories}
      actorType={actorType}
    />
  );

  mapTemplatesToSections = (): Array<FormSection> => {
    const sections = [] as Array<FormSection>;
    const subsections = [] as Array<FormSubSection>;
    const actorTypesWithPatient = [
      {
        uuid: '',
        display: 'Patient'
      } as IActorType,
      ...this.props.actorTypes
    ];
    this.props.templates.forEach((template: TemplateUI) => {
      const name = this.getTemplateName(template);
      const fragment = (
        <>
          {actorTypesWithPatient.map(actorType => this.buildTemplateForm(template, actorType))}
        </>
      );

      const onSelectCallback: SelectCallback = () => this.changeLocation(name);
      subsections.push(new FormSubSection(name, true, false, fragment, onSelectCallback));
    });

    sections.push(new FormSection('Messages', subsections));
    return sections;
  };

  render() {
    return (
      <div className="admin-settings">
        <div className="panel-body">
          <h2><LocalizedMessage id="MESSAGES_MESSAGES_SETTINGS_LABEL" defaultMessage={Default.MESSAGES_SETTINGS_LABEL} /></h2>
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
      </div>
    );
  }
}

const mapStateToProps = ({ adminSettings }: IRootState) => ({
  loading: adminSettings.loading,
  actorTypes: adminSettings.actorTypes,
  healthTipCategories: adminSettings.healthTipCategories
});

const mapDispatchToProps = ({
  updateTemplate,
  getActorTypes,
  getHealthTipCategories
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AdminSettings);
