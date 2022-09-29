/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import _ from 'lodash';

import React from 'react';
import {FormGroup} from 'react-bootstrap';
import Select from 'react-select';
import './dynamic-multiselect.scss';
import FormLabel from '../../form-label/form-label';
import MultiselectOption from '../../../shared/model/multiselect-option';

interface IProps {
    options: Array<MultiselectOption>
    selectedOptions: string;
    label: string;
    key: string;
    mandatory: boolean;
    onSelectChange: (valueSelected: string) => void;
}

interface IState {
    options: Array<MultiselectOption>,
    optionsLabelByValueMap: { [x: string]: any; }
    formDivWrapperHeight: number,
}

export default class DynamicMultiselect extends React.Component<IProps, IState> {

    public static defaultProps = {
        mandatory: false
    };

    constructor(props: IProps) {
        super(props);
        this.state = {
            options: this.props.options,
            optionsLabelByValueMap: _.chain(this.props.options)
                .keyBy('value')
                .mapValues('label')
                .value(),
            formDivWrapperHeight: 90
        };
        this.handleSelectClick = this.handleSelectClick.bind(this);
    }

    getCategoryOptionLabel = (optionValue: string) => {
        const mappedLabel = this.state.optionsLabelByValueMap[optionValue];
        return !!mappedLabel ? mappedLabel : optionValue;
    };

    mapOptionsToString = (options?: Array<MultiselectOption>) => {
        return !!options ? options.map(o => o.value).join(',') : '';
    };

    mapOptionsToMultiselectOptionsArray = (optionString: string) => {
        return optionString && optionString.split(',')
            .filter(optionValue => !!optionValue)
            .map((optionValue) => (new MultiselectOption(this.getCategoryOptionLabel(optionValue), optionValue)));
    };

    handleChange = (selectedOptions?: Array<MultiselectOption>) => {
        const newValue = this.mapOptionsToString(selectedOptions);
        this.props.onSelectChange(newValue);
    };

    handleSelectClick() {
        const elem = document.getElementById('selectWrapper')!;
        this.setState({formDivWrapperHeight: elem.clientHeight + 60});
    }

    render = () => {
        const selectedOptions = this.mapOptionsToMultiselectOptionsArray(this.props.selectedOptions);
        return (
            <div style={{height: this.state.formDivWrapperHeight}}>
                <FormGroup
                    className="multiselect"
                    controlId={this.props.key}
                    key={this.props.key}>
                    <FormLabel label={this.props.label} mandatory={this.props.mandatory}/>
                    <div id="selectWrapper" onClick={this.handleSelectClick}>
                        <Select
                            defaultValue={this.state.options}
                            isMulti
                            options={this.state.options}
                            className="basic-multi-select"
                            classNamePrefix="select"
                            value={selectedOptions}
                            onChange={this.handleChange}
                        />
                    </div>
                </FormGroup>
            </div>
        )
    }
}

