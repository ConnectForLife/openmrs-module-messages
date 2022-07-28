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
import { connect } from 'react-redux';
import { getConfigs } from "./customize.reducer";
import Helmet from 'react-helmet';

export interface ICustomizeProps extends DispatchProps, StateProps {}

export interface ICustomizeState {}

const OPENMRS_ROOT = '/openmrs/';
const CFL_UI_ROOT = `${OPENMRS_ROOT}/owa/cfl/`;
const STYLE_HREF = CFL_UI_ROOT + 'overrides.css';
const SCRIPT_HREF = CFL_UI_ROOT + 'overrides.js';

export class Customize extends React.PureComponent<ICustomizeProps, ICustomizeState> {

    componentDidMount() {
        this.props.getConfigs();
    }

    render() {
        const styles = this.props.styles.map((e) => {
            return (<link rel="stylesheet" href={e} />);
        });
        return (
            <Helmet>
                {styles}
                <link rel="stylesheet preload" href={STYLE_HREF} as="style" />
                <script src={SCRIPT_HREF} type="text/javascript" defer />
            </Helmet>
        );
    };
}

export const mapStateToProps = ({ customizeReducer }: any) => ({
    loading: customizeReducer.loading,
    styles: customizeReducer.styles
});

const mapDispatchToProps = ({
    getConfigs
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

// @ts-ignore
export default connect(mapStateToProps, mapDispatchToProps)(Customize);
