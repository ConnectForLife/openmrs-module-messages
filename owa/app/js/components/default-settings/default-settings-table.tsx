import React, { ReactFragment } from 'react';
import { connect } from 'react-redux';
import { IRootState } from '../../reducers';
import { getTemplates, updateTemplate, putTemplates } from '../../reducers/admin-settings.reducer';
import { TemplateUI } from '../../shared/model/template-ui';
import { TemplateForm } from './template-form';
import { Button } from 'react-bootstrap';
import * as Msg from '../../shared/utils/messages';
import { IColumn } from '../../shared/model/column.model';
import { FragmentTable } from './fragment-table';
import { IFragmentRow } from '../../shared/model/fragment-table-row.model';

interface IProps extends StateProps, DispatchProps { }

class DefaultSettingsTable extends React.Component<IProps> {

  componentDidMount = () => this.props.getTemplates();

  handleSave = () => this.props.putTemplates(this.props.templates);

  getColumns = (): ReadonlyArray<IColumn> => {
    // todo CFLM-521 get actor types and build columns
    const actorTypes = ['Patient'];
    return actorTypes.map(actorType => ({
      key: actorType,
      label: actorType + Msg.DEFAULT_SETTINGS_POSTFIX
    }));
  }

  getTemplateFragmentRows = (): ReadonlyArray<IFragmentRow> => {
    // todo CFLM-521 get actor types and build columns
    const actorTypes = ['Patient'];
    return actorTypes.map(actorType => ({
      key: actorType,
      fragments: this.buildTemplateFragments()
    }));
  }

  buildTemplateFragments = (): ReadonlyArray<ReactFragment> =>
    this.props.templates.map(this.buildTemplateForm);

  buildTemplateForm = (template: TemplateUI): ReactFragment => (
    <TemplateForm 
      key={`template-form-${template.localId}`}
      template={template}
      updateTemplate={this.props.updateTemplate}
    />
  );

  render = () => (
    <div>
      <FragmentTable 
        columns={this.getColumns()}
        fragments={this.getTemplateFragmentRows()}
      />
      <Button
        className="btn btn-success btn-md pull-right"
        onClick={this.handleSave}>
        {Msg.SAVE_BUTTON_LABEL}
      </Button>
    </div>
  );
};

const mapStateToProps = ({ adminSettings }: IRootState) => ({
  templates: adminSettings.defaultTemplates,
  loading: adminSettings.loading
});

const mapDispatchToProps = ({
  getTemplates,
  updateTemplate,
  putTemplates
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DefaultSettingsTable);