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

import './react-table-pagination.scss';
import { FormattedMessage } from 'react-intl';

const paginationButton = props => (
    <button type="button" {...props} className="-btn">
        {props.children}
    </button>
)

interface ReactTableCustomPaginationProps {
    pages: number;
    page: number;
    showPageSizeOptions: boolean;
    pageSizeOptions: number[];
    pageSize: number;
    showPageJump: boolean;
    canPrevious: boolean;
    canNext: boolean;
    onPageSizeChange: any;
    previousText: string;
    nextText: string;
    onPageChange: any;
    ofText: string;
    pageText: string;
    rowsText: string;
    firstText: string;
    lastText: string;
    style: Record<string, any>;
    FirstComponent: any;
    PreviousComponent: any;
    NextComponent: any;
    LastComponent: any;
}

interface ReactTableCustomPaginationState {
    page: string | number;
}

export class ReactTableCustomPagination extends React.PureComponent<ReactTableCustomPaginationProps,
    ReactTableCustomPaginationState> {
    constructor(props: ReactTableCustomPaginationProps) {
        super(props);

        this.state = {
            page: props.page,
        };
    }

    componentWillReceiveProps(nextProps: ReactTableCustomPaginationProps) {
        this.setState({ page: nextProps.page });
    }

    getSafePage = (page: any) => {
        if (Number.isNaN(page)) {
            page = this.props.page;
        }
        return Math.min(Math.max(page, 0), this.props.pages - 1);
    };

    changePage = (page: any) => {
        page = this.getSafePage(page);
        this.setState({ page });
        if (this.props.page !== page) {
            this.props.onPageChange(page);
        }
    };

    applyPage = (e: any) => {
        if (e) {
            e.preventDefault();
        }
        const page = this.state.page;
        this.changePage(page === '' ? this.props.page : page);
    };

    render(): JSX.Element {
        const {
            pages,
            page,
            showPageSizeOptions,
            pageSizeOptions,
            pageSize,
            showPageJump,
            canPrevious,
            canNext,
            onPageSizeChange,
            style,
            previousText,
            nextText,
            ofText,
            pageText,
            rowsText,
            FirstComponent = paginationButton,
            PreviousComponent = paginationButton,
            NextComponent = paginationButton,
            LastComponent = paginationButton,
            firstText = <FormattedMessage id="reactcomponents.table.first" />,
            lastText = <FormattedMessage id="reactcomponents.table.last" />
        } = this.props;

        return (
            <div className="-pagination" style={style}>
                <div className="-first">
                    <FirstComponent
                        onClick={() => {
                            if (!canPrevious) return
                            this.changePage(0)
                        }}
                        disabled={!canPrevious}>
                        {firstText}
                    </FirstComponent>
                </div>
                <div className="-previous">
                    <PreviousComponent
                        onClick={() => {
                            if (!canPrevious) return
                            this.changePage(page - 1)
                        }}
                        disabled={!canPrevious}>
                        {previousText}
                    </PreviousComponent>
                </div>
                <div className="-center -center-pagination">
                    <span className="-pageInfo">
                        {pageText}{' '}
                        {showPageJump ? (
                            <div className="-pageJump">
                                <input
                                    type={this.state.page === '' ? 'text' : 'number'}
                                    onChange={e => {
                                        const val: string = e.target.value;
                                        const page: number = parseInt(val, 10) - 1;
                                        if (val === '') {
                                            return this.setState({ page: val });
                                        }
                                        this.setState({ page: this.getSafePage(page) });
                                    }}
                                    value={this.state.page === '' ? '' : (this.state.page as number) + 1}
                                    onBlur={this.applyPage}
                                    onKeyPress={e => {
                                        if (e.which === 13 || e.keyCode === 13) {
                                            this.applyPage(e);
                                        }
                                    }}
                                />
                            </div>
                        ) : (
                                <span className="-currentPage">{page + 1}</span>
                            )}{' '}
                        {ofText} <span className="-totalPages">{pages || 1}</span>
                    </span>
                    {showPageSizeOptions && (
                        <span className="select-wrap -pageSizeOptions">
                            <select onChange={e => onPageSizeChange(Number(e.target.value))} value={pageSize}>
                                {pageSizeOptions.map((option: any, i: number) => (
                                    <option key={i} value={option}>
                                        {`${option} ${rowsText}`}
                                    </option>
                                ))}
                            </select>
                        </span>
                    )}
                </div>
                <div className="-next">
                    <NextComponent
                        onClick={() => {
                            if (!canNext) return;
                            this.changePage(page + 1);
                        }}
                        disabled={!canNext}>
                        {nextText}
                    </NextComponent>
                </div>
                <div className="-last">
                    <LastComponent
                        onClick={() => {
                            if (!canNext) return
                            this.changePage(pages)
                        }}
                        disabled={!canNext}>
                        {lastText}
                    </LastComponent>
                </div>
            </div>
        );
    }
}