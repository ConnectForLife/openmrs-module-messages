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
import { Button } from 'react-bootstrap';
import * as Msg from '../../shared/utils/messages';
import FormSection from '@bit/soldevelo-omrs.cfl-components.form-entry/model/form-section';
import FormSubSection from '@bit/soldevelo-omrs.cfl-components.form-entry/model/form-subsection';
import FormEntry from '@bit/soldevelo-omrs.cfl-components.form-entry';
import './patient-template.scss';

interface IPatientTemplateEditProps extends DispatchProps, StateProps, RouteComponentProps<{ patientId: string }> {
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
    this.props.putPatientTemplates(this.props.patientTemplates, this.props.templates);
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

    this.props.templates.forEach((template) => {
      const name = template.name ? template.name : `Template ${template.localId}`;

      //TODO in CFLM-305: replace fragment with real template component
      const fragment = <div>{`Template ${template.localId} body (Work in progress...)`}</div>;

      //TODO in CFLM-306: decide if the 'completed-icon' should be displayed next to section name
      //(if the section was completed)
      const completed = false;

      const onSelectCallback = () => {
        this.props.selectTemplate(template);
      }

      subsections.push(new FormSubSection(name, completed, fragment, onSelectCallback));
    });

    sections.push(new FormSection('Messages', subsections));
    return sections;
  }

  render() {
    const { isNew } = this.props;
    return (
      <div className="body-wrapper">
        <div className="panel-body">
          <h2>Patient Template Edit page</h2>
          {this.renderTemplateState()}
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
  selectedTemplate: patientTemplate.selectedTemplate
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
