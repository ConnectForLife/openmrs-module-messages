/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
import React from 'react';
import ReactTable from 'react-table';
import { LocalizedMessage } from '@openmrs/react-components';
import { withFiltersChangedCallback } from './with-filters-changed-callback';
import { DEFAULT_ITEMS_PER_PAGE, DEFAULT_SORT, DEFAULT_ORDER, MIN_ROWS, PAGE_SIZE_OPTIONS } from './constants';
interface IPaginationBaseState {
  itemsPerPage: number;
  sort: string;
  order: string;
  activePage: number;
  filters: {};
}
export interface ITableProps {
  data: any[];
  columns: any[];
  loading: boolean;
  pages: number;
  filtersComponent?: any;
  filterProps?: {};
  fetchDataCallback(activePage: number, itemsPerPage: number, sort: string, order: string, filters: {}): void;
};
export default class Table extends React.PureComponent<ITableProps, IPaginationBaseState> {
  filters: any;
  constructor(props) {
    super(props);
    this.state = {
      activePage: 0,
      itemsPerPage: DEFAULT_ITEMS_PER_PAGE,
      sort: DEFAULT_SORT,
      order: DEFAULT_ORDER,
      filters: {}
    };
    if (this.props.filtersComponent) {
      this.filters = withFiltersChangedCallback(this.filtersChanged)(this.props.filtersComponent);
    }
  }
  fetchData = (state, instance) => {
    this.setState(
      {
        activePage: state.page,
        itemsPerPage: state.pageSize,
        sort: state.sorted[0] ? state.sorted[0].id : DEFAULT_SORT,
        order: state.sorted[0] ? (state.sorted[0].desc ? 'desc' : 'asc') : DEFAULT_ORDER
      },
      () => this.props.fetchDataCallback(
        this.state.activePage,
        this.state.itemsPerPage,
        this.state.sort,
        this.state.order,
        this.state.filters
      )
    );
  }
  filtersChanged = (changedFilters: {}) => this.setState(((prevState, props) => ({ filters: { ...prevState.filters, ...changedFilters } })),
    () => this.props.fetchDataCallback(
      this.state.activePage,
      this.state.itemsPerPage,
      this.state.sort,
      this.state.order,
      this.state.filters
    ));
  render = () => {
    const noDataText = <LocalizedMessage id="reactcomponents.table.noDataText" defaultMessage="No results found" />;
    const previousText = <LocalizedMessage id="reactcomponents.table.previous" defaultMessage="Previous" />;
    const nextText = <LocalizedMessage id="reactcomponents.table.next" defaultMessage="Next" />;
    const loadingText = <LocalizedMessage id="reactcomponents.table.loading" defaultMessage="Loading..." />;
    const pageText = <LocalizedMessage id="reactcomponents.table.page" defaultMessage="Page" />;
    const ofText = <LocalizedMessage id="reactcomponents.table.of" defaultMessage="of" />;
    const rowsText = <LocalizedMessage id="reactcomponents.table.rows" defaultMessage="rows" />;
    return (
      <div>
        {this.props.filtersComponent && <this.filters {...this.props.filterProps} {...this.state.filters} {...this.props.filterProps} />}
        <ReactTable
          className="-striped -highlight"
          collapseOnDataChange={false}
          columns={this.props.columns}
          data={this.props.data}
          defaultPageSize={DEFAULT_ITEMS_PER_PAGE}
          manual={true}
          loading={this.props.loading}
          minRows={MIN_ROWS}
          pages={this.props.pages}
          pageSizeOptions={PAGE_SIZE_OPTIONS}
          onFetchData={this.fetchData}
          multisort={false}
          nextText={nextText}
          previousText={previousText}
          rowsText={rowsText}
          loadingText={loadingText}
          ofText={ofText}
          noDataText={<span className="sortableTable-noDataText">{noDataText}</span>}
          pageText={pageText}
        />
      </div>
    );
  }
}
