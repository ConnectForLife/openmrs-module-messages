/*
 *  This Source Code Form is subject to the terms of the Mozilla Public License,
 *  v. 2.0. If a copy of the MPL was not distributed with this file, You can
 *  obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 *  the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *  <p>
 *  Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 *  graphic logo is a trademark of OpenMRS Inc.
 */

import React from 'react';
import ReactTable from 'react-table';
import { withFiltersChangedCallback } from './with-filters-changed-callback';
import {
  DEFAULT_ITEMS_PER_PAGE,
  DEFAULT_SORT,
  DEFAULT_ORDER,
  MIN_ROWS,
  PAGE_SIZE_OPTIONS,
  ROW_RESULTS_COUNT_LABEL
} from './constants';
import _ from 'lodash';
import {ReactTableCustomPagination} from "./pagination-component";
import { FormattedMessage } from 'react-intl';

interface IPaginationBaseState {
  itemsPerPage: number;
  sort: string;
  order: string;
  activePage: number;
  filters: {};
}

export interface ITableProps {
  query?: string;
  data: any[];
  columns: any[];
  loading: boolean;
  pages: number;
  filtersComponent?: any;
  filterProps?: {};
  showPagination?: boolean;
  sortable?: boolean,
  multiSort?: boolean,
  resizable?: boolean,
  fetchDataCallback(activePage: number, itemsPerPage: number, sort: string, order: string, filters: {}, query?: string): void;
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

  componentDidUpdate(prevProps) {
    if (prevProps.query != this.props.query) {
      const { activePage, itemsPerPage, sort, order, filters } = this.state;
      this.props.fetchDataCallback(activePage, itemsPerPage, sort, order, filters, this.props.query);
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
        this.state.filters,
        this.props.query
      )
    );
  }

  filtersChanged = (changedFilters: {}) => this.setState(((prevState, props) => ({ filters: { ...prevState.filters, ...changedFilters } })),
    () => this.props.fetchDataCallback(
      this.state.activePage,
      this.state.itemsPerPage,
      this.state.sort,
      this.state.order,
      this.state.filters,
      this.props.query
    ));

  render = () => {
    const NullComponent = () => null;
    const noDataText = <FormattedMessage id="reactcomponents.table.noDataText" />;
    const previousText = <FormattedMessage id="reactcomponents.table.previous" />;
    const nextText = <FormattedMessage id="reactcomponents.table.next" />;
    const loadingText = <FormattedMessage id="reactcomponents.table.loading" />;
    const pageText = <FormattedMessage id="reactcomponents.table.page" />;
    const ofText = <FormattedMessage id="reactcomponents.table.of" />;
    const rowsText = ROW_RESULTS_COUNT_LABEL;

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
          nextText={nextText}
          previousText={previousText}
          rowsText={rowsText}
          loadingText={loadingText}
          ofText={ofText}
          noDataText={<span className="sortableTable-noDataText">{noDataText}</span>}
          NoDataComponent={NullComponent}
          pageText={pageText}
          showPagination={_.get(this.props, 'showPagination', true)}
          sortable={_.get(this.props, 'sortable', true)}
          multiSort={_.get(this.props, 'multiSort', true)}
          resizable={_.get(this.props, 'resizable', true)}
          PaginationComponent={ReactTableCustomPagination}
        />
      </div>
    );
  }
}
