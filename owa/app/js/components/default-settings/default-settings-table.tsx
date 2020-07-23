import React, { ReactFragment } from 'react';
import { connect } from 'react-redux';
import { IRootState } from '../../reducers';

import { updateTemplate, getActorTypes } from '../../reducers/admin-settings.reducer';
import { TemplateUI } from '../../shared/model/template-ui';
import { TemplateForm } from './template-form';
import * as Default from '../../shared/utils/messages';
import { getIntl } from '@openmrs/react-components/lib/components/localization/withLocalization';
import { IColumn } from '../../shared/model/column.model';
import { FragmentTable } from './fragment-table';
import { IFragmentRow } from '../../shared/model/fragment-table-row.model';
import { IActorType } from '../../shared/model/actor-type.model';

interface IProps extends DispatchProps {
  templates: ReadonlyArray<TemplateUI>,
  actorTypes: ReadonlyArray<IActorType>
 }

class DefaultSettingsTable extends React.Component<IProps> {

  componentDidMount = () => {
    this.props.getActorTypes();
  }

  getActorTypes = () => [{uuid: '', display: 'Patient'} as IActorType, ...this.props.actorTypes];

  getColumns = (): ReadonlyArray<IColumn> => this.getActorTypes().map(actorType => ({
      key: actorType.display,
      label: actorType.display + getIntl().formatMessage({ id: 'MESSAGES_DEFAULT_SETTINGS_POSTFIX', defaultMessage: Default.DEFAULT_SETTINGS_POSTFIX })
    }));

  getTemplateFragmentRows = (): ReadonlyArray<IFragmentRow> => 
    this.getActorTypes().map(actorType => ({
      key: actorType.display,
      fragments: this.buildTemplateFragments(actorType)
    }));

  buildTemplateFragments = (actorType: IActorType): ReadonlyArray<ReactFragment> =>
    this.props.templates.map((t) => this.buildTemplateForm(t, actorType));

  buildTemplateForm = (template: TemplateUI, actorType: IActorType): ReactFragment => (
    <TemplateForm 
      key={`template-form-${template.localId}`}
      template={template}
      updateTemplate={this.props.updateTemplate}
      actorType={actorType}
    />
  );

  render = () => (
    <div>
      <h4>{getIntl().formatMessage({ id: 'MESSAGES_DEFAULT_SETTINGS_TABLE_TITLE', defaultMessage: Default.DEFAULT_SETTINGS_TABLE_TITLE })}</h4>
      <FragmentTable 
        columns={this.getColumns()}
        fragments={this.getTemplateFragmentRows()}
      />
    </div>
  );
};

const mapStateToProps = ({ adminSettings }: IRootState) => ({
  loading: adminSettings.loading,
  actorTypes: adminSettings.actorTypes
});

const mapDispatchToProps = ({
  updateTemplate,
  getActorTypes
});

type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DefaultSettingsTable);
