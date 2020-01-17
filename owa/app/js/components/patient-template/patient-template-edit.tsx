import React, { ReactFragment } from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import {
  getTemplates,
  getPatientTemplates,
  putPatientTemplates
} from '../../reducers/patient-template.reducer'
import { getActorList } from '../../reducers/actor.reducer';
import { IRootState } from '../../reducers';
import { Button, SelectCallback } from 'react-bootstrap';
import * as Msg from '../../shared/utils/messages';
import FormSection from '@bit/soldevelo-omrs.cfl-components.form-entry/model/form-section';
import FormSubSection from '@bit/soldevelo-omrs.cfl-components.form-entry/model/form-subsection';
import FormEntry from '@bit/soldevelo-omrs.cfl-components.form-entry';
import './patient-template.scss';
import PatientTemplateForm from './patient-template-form';
import { TemplateUI } from '../../shared/model/template-ui';
import {
  getPatientTemplateWithTemplateId,
  getPatientTemplateWithActorId
} from '../../selectors/patient-template-selector';
import { PatientTemplateUI } from '../../shared/model/patient-template-ui';
import _ from 'lodash';
import { IActor } from '../../shared/model/actor.model';
import { getActorTypes } from '../../reducers/admin-settings.reducer';

interface IPatientTemplateEditProps extends DispatchProps, StateProps, RouteComponentProps<{
  patientId: string,
  patientUuid: string,
  activeSection: string
}> {
  isNew: boolean
  relatedActors: Array<IActor>
};

interface IPatientTemplateEditState {
};

class PatientTemplateEdit extends React.PureComponent<IPatientTemplateEditProps, IPatientTemplateEditState> {

  constructor(props: IPatientTemplateEditProps) {
    super(props);
  }

  componentDidMount() {
    this.props.getTemplates();
    this.props.getActorList(parseInt(this.props.match.params.patientId));
    this.props.getActorTypes();
    this.props.getPatientTemplates(parseInt(this.props.match.params.patientId));
  }

  handleSave = () => {
    const { patientId, patientUuid } = this.props.match.params;
    this.props.putPatientTemplates(this.props.patientTemplates, this.props.templates, parseInt(patientId), patientUuid);
  }

  handleCancel = () => this.props.history.goBack();

  handleNext = () => {
    const nextSubsection = this.getNextSubsection();
    return !!nextSubsection && this.changeLocation(this.getTemplateName(nextSubsection));
  };

  resolveSubsectionUrl = (subsectionName: string) => {
    const { patientId, patientUuid } = this.props.match.params;
    return `/messages/${patientId}&patientuuid=${patientUuid}/patient-template/edit/${subsectionName}`;
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
        <>
          <div className="page-header">
            The patient template is new
            for patient (patientId: {this.props.match.params.patientId})
          </div>
          <FormEntry sections={sections} activeSection={this.props.match.params.activeSection} />
        </>
      );
    } else {
      return (
        <>
          <div className="page-header">
            The patient template is not new
            (patientId: {this.props.match.params.patientId})
          </div>
          <FormEntry sections={sections} activeSection={this.props.match.params.activeSection} />
        </>
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

    this.props.templates.forEach((template: TemplateUI) => {
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
      const isPersisted = patientTemplates.length > 0;

      const onSelectCallback: SelectCallback = () => this.changeLocation(name);

      subsections.push(new FormSubSection(name, isValid, isPersisted, fragment, onSelectCallback));
    });

    sections.push(new FormSection('Messages', subsections));
    return sections;
  }

  render() {
    return (
      <>
        <div className="panel-body">
          <h2>Patient Template Edit page</h2>
          {!this.props.loading && this.renderTemplateState()}
        </div>
        <div className="panel-body">
          <Button
            className="btn btn-danger btn-md"
            onClick={this.handleCancel}>
            {Msg.CANCEL_BUTTON_LABEL}
          </Button>
          <div className="pull-right">
            <Button
              className="btn btn-default btn-md"
              disabled={!this.getNextSubsection()}
              onClick={this.handleNext}>
              {Msg.NEXT_BUTTON_LABEL}
            </Button>
            <Button
              className="btn btn-success btn-md"
              onClick={this.handleSave}>
              {Msg.SAVE_BUTTON_LABEL}
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
  getActorTypes
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PatientTemplateEdit);
