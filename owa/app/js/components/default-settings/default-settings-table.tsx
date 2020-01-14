import React, { ReactFragment } from 'react';
import { connect } from 'react-redux';

import { updateTemplate } from '../../reducers/admin-settings.reducer';
import { TemplateUI } from '../../shared/model/template-ui';
import { TemplateForm } from './template-form';
import * as Msg from '../../shared/utils/messages';
import { IColumn } from '../../shared/model/column.model';
import { FragmentTable } from './fragment-table';
import { IFragmentRow } from '../../shared/model/fragment-table-row.model';

interface IProps extends DispatchProps {
  templates: ReadonlyArray<TemplateUI>
 }

class DefaultSettingsTable extends React.Component<IProps> {

  // todo CFLM-521 get actor types and build columns
  getActorTypes = () => ['Patient', 'Caregiver', 'Father'];

  getColumns = (): ReadonlyArray<IColumn> => 
    this.getActorTypes().map(actorType => ({
      key: actorType,
      label: actorType + Msg.DEFAULT_SETTINGS_POSTFIX
    }));

  getTemplateFragmentRows = (): ReadonlyArray<IFragmentRow> => 
    this.getActorTypes().map(actorType => ({
      key: actorType,
      fragments: this.buildTemplateFragments()
    }));

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
      <h3>{Msg.DEFAULT_SETTINGS_TABLE_TITLE}</h3>
      <FragmentTable 
        columns={this.getColumns()}
        fragments={this.getTemplateFragmentRows()}
      />
    </div>
  );
};

const mapDispatchToProps = ({
  updateTemplate,
});

type DispatchProps = typeof mapDispatchToProps;

export default connect(
  undefined,
  mapDispatchToProps
)(DefaultSettingsTable);