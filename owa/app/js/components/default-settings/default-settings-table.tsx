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

import {getActorTypes, getHealthTipCategories, updateTemplate} from '../../reducers/admin-settings.reducer';
import {TemplateUI} from '../../shared/model/template-ui';
import {TemplateForm} from './template-form';
import * as Default from '../../shared/utils/messages';
import {IColumn} from '../../shared/model/column.model';
import {FragmentTable} from './fragment-table';
import {IFragmentRow} from '../../shared/model/fragment-table-row.model';
import {IActorType} from '../../shared/model/actor-type.model';
import { injectIntl, FormattedMessage } from 'react-intl';
import { PropsWithIntl } from '../../components/translation/PropsWithIntl';

interface IProps extends DispatchProps {
  templates: ReadonlyArray<TemplateUI>,
  actorTypes: ReadonlyArray<IActorType>
 }

class DefaultSettingsTable extends React.Component<PropsWithIntl<IProps>> {

  componentDidMount = () => {
    this.props.getActorTypes();
    this.props.getHealthTipCategories();
  }

  getActorTypes = () => [{uuid: '', display: 'Patient'} as IActorType, ...this.props.actorTypes];

  getColumns = (): ReadonlyArray<IColumn> => this.getActorTypes().map(actorType => ({
      key: actorType.display,
      label: actorType.display + this.props.intl.formatMessage({ id: 'messages.default.settingsPostfix' })
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
      <h4><FormattedMessage id="messages.default.settingsTableTitle" /></h4>
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
  getActorTypes,
  getHealthTipCategories
});

type DispatchProps = typeof mapDispatchToProps;

export default injectIntl(connect(
  mapStateToProps,
  mapDispatchToProps
)(DefaultSettingsTable));