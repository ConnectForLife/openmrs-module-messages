import React from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import {
  getTemplates,
  getPatientTemplates,
  putPatientTemplates,
  selectTemplate
} from '../../reducers/patient-template.reducer'
import { IRootState } from '../../reducers';
import { Button, SelectCallback } from 'react-bootstrap';
import * as Msg from '../../shared/utils/messages';
import FormSection from '@bit/soldevelo-omrs.cfl-components.form-entry/model/form-section';
import FormSubSection from '@bit/soldevelo-omrs.cfl-components.form-entry/model/form-subsection';
import FormEntry from '@bit/soldevelo-omrs.cfl-components.form-entry';
import './patient-template.scss';
import PatientTemplateForm from './patient-template-form';
import { TemplateUI } from '../../shared/model/template-ui';
import { getPatientTemplateWithTemplateId } from '../../selectors/patient-template-selector';
import { PatientTemplateUI } from '../../shared/model/patient-template-ui';
import _ from 'lodash';

interface IPatientTemplateEditProps extends DispatchProps, StateProps, RouteComponentProps<{ patientId: string, patientUuid: string }> {
  isNew: boolean
};

interface IPatientTemplateEditState {
};

class PatientTemplateEdit extends React.PureComponent<IPatientTemplateEditProps, IPatientTemplateEditState> {

  constructor(props: IPatientTemplateEditProps) {
    super(props);
  }

  componentDidMount() {
    this.props.getTemplates();
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
          <FormEntry sections={sections} />
        </>
      );
    } else {
      return (
        <>
          <div className="page-header">
            The patient template is not new
            (patientId: {this.props.match.params.patientId})
          </div>
          <FormEntry sections={sections} />
        </>
      );
    }
  }

  mapTemplatesToSections = (): Array<FormSection> => {
    const sections = [] as Array<FormSection>;
    const subsections = [] as Array<FormSubSection>;

    this.props.templates.forEach((template: TemplateUI) => {
      const name = template.name ? template.name : `Template ${template.localId}`;

      const patientTemplates: ReadonlyArray<PatientTemplateUI> =
        getPatientTemplateWithTemplateId(this.props.patientTemplates, template.id!);

      // TODO CFLM-447 handle multiple forms for multiple patient templates
      const fragment = <PatientTemplateForm
        patientTemplate={patientTemplates.length ? patientTemplates[0] : undefined}
        template={template}
        patientId={parseInt(this.props.match.params.patientId)}
        actorId={parseInt(this.props.match.params.patientId)}
      />

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
      <div className="body-wrapper">
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
      </div>
    );
  }
}

const mapStateToProps = ({ patientTemplate }: IRootState) => ({
  templates: patientTemplate.templates,
  patientTemplates: patientTemplate.patientTemplates,
  selectedTemplate: patientTemplate.selectedTemplate,
  loading: patientTemplate.patientTemplatesLoading || patientTemplate.templatesLoading
});

const mapDispatchToProps = ({
  getTemplates,
  getPatientTemplates,
  putPatientTemplates,
  selectTemplate
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PatientTemplateEdit);
