/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import React, { ReactFragment } from 'react';
import './fragment-table.scss';
import { Col } from 'react-bootstrap';

import { IColumn } from '../../shared/model/column.model';
import { IFragmentRow } from '../../shared/model/fragment-table-row.model';

interface IProps {
  columns: ReadonlyArray<IColumn>;
  fragments: ReadonlyArray<IFragmentRow>;
}

const FULL_WIDTH_COLUMN = 12;
const HALF_WIDTH_COLUMN = 6;

export class FragmentTable extends React.Component<IProps> {

  getColumnWidth = () => this.props.columns.length === 1 ?
    FULL_WIDTH_COLUMN : HALF_WIDTH_COLUMN;

  renderColumnWithRows = (column: IColumn) => (
    <Col md={this.getColumnWidth()} className="fragment-column">
      <div className="fragment-row header u-ta-center">
        <strong><h4>{column.label}</h4></strong>
      </div>
      {
        this.props.fragments
          .filter((fragment: IFragmentRow) => fragment.key === column.key)
          .map((fragment: IFragmentRow) => fragment.fragments
            .map((fragment: ReactFragment, index: number) => (
              <div key={index} className={`fragment-row ${index % 2 === 0 ? 'even' : 'odd'}`}>
                {fragment}
              </div>
            )))
      }
    </Col>
  );

  render = () => (
    <div className="fragment-table">
      {this.props.columns.map(this.renderColumnWithRows)}
    </div>
  );
};
