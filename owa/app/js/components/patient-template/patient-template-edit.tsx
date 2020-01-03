import React, { ReactFragment } from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import {
  getTemplates,
  getPatientTemplates,
  putPatientTemplates,
  selectTemplate
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
    this.props.getPatientTemplates(parseInt(this.props.match.params.patientId));
  }

  componentWillUpdate(nextProps: IPatientTemplateEditProps, nextState: IPatientTemplateEditState) {
  }

  handleSave = () => {
    const { patientId, patientUuid } = this.props.match.params;
    this.props.putPatientTemplates(this.props.patientTemplates, this.props.templates, parseInt(patientId), patientUuid);
  }

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
    <PatientTemplateForm key={`template-form-${actor ? actor.actorId : parseInt(this.props.match.params.patientId)}}`}
      patientTemplate={patientTemplates.length ? patientTemplates[0] : undefined}
      template={template}
      patientId={parseInt(this.props.match.params.patientId)}
      actorId={actor ? actor.actorId : parseInt(this.props.match.params.patientId)}
      actorName={actor ? actor.actorName : undefined}
      actorTypeId={actor ? actor.actorTypeId : undefined}
    />

  mapTemplatesToSections = (): Array<FormSection> => {
    const sections = [] as Array<FormSection>;
    const subsections = [] as Array<FormSubSection>;
    const patientId = parseInt(this.props.match.params.patientId);

    this.props.templates.forEach((template: TemplateUI) => {
      const name = template.name ? template.name : `Template ${template.localId}`;

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

      const onSelectCallback: SelectCallback = () => {
        this.props.selectTemplate(template);
      }

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
            className="btn btn-success btn-md pull-right"
            onClick={this.handleSave}>
            {Msg.SAVE_BUTTON_LABEL}
          </Button>
        </div>
      </>
    );
  }
}

const mapStateToProps = ({ actor, patientTemplate }: IRootState) => ({
  templates: patientTemplate.templates,
  patientTemplates: patientTemplate.patientTemplates,
  selectedTemplate: patientTemplate.selectedTemplate,
  loading: patientTemplate.patientTemplatesLoading || patientTemplate.templatesLoading,
  relatedActors: actor.actorResultList
});

const mapDispatchToProps = ({
  getTemplates,
  getPatientTemplates,
  putPatientTemplates,
  selectTemplate,
  getActorList
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PatientTemplateEdit);
