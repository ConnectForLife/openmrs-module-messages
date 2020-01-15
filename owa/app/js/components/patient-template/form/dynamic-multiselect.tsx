import React from 'react';
import { FormGroup, Radio, ControlLabel } from 'react-bootstrap';
import Select from 'react-select';
import './dynamic-multiselect.scss';
import FormLabel from '@bit/soldevelo-omrs.cfl-components.form-label';
import MultiselectOption from '../../../shared/model/multiselect-option';
import { CATEGORIES_MAP } from '../../../shared/utils/messages';

interface IProps {
  options: ReadonlyArray<string>
  selectedOptions: string;
  label: string;
  key: string;
  mandatory: boolean;
  onSelectChange: (valueSelected: string) => void;
}

interface IState {
  options: Array<MultiselectOption>,
}

export default class DynamicMultiselect extends React.Component<IProps, IState> {

  public static defaultProps = {
    mandatory: false
  };

  constructor(props: IProps) {
    super(props);
    this.state = {
      options: this.props.options
        .map((optionName) => (new MultiselectOption(this.getCategoryOptionLabel(optionName), optionName))),
    }
  }

  getCategoryOptionLabel = (optionName: string) => {
    const mappedLabel = CATEGORIES_MAP[optionName];
    return !!mappedLabel ? mappedLabel : optionName;
  }

  mapOptionsToString = (options?: Array<MultiselectOption>) => {
    return !!options ? options.map(o => o.value).join(',') : '';
  }

  mapOptionsToMultiselectOptionsArray = (optionString: string) => {
    return optionString.split(',')
      .filter(optionName => !!optionName)
      .map((optionName) => (new MultiselectOption(this.getCategoryOptionLabel(optionName), optionName)));
  }

  handleChange = (selectedOptions?: Array<MultiselectOption>) => {
    const newValue = this.mapOptionsToString(selectedOptions);
    this.props.onSelectChange(newValue);
  }

  render = () => {
    const selectedOptions = this.mapOptionsToMultiselectOptionsArray(this.props.selectedOptions);
    return (
      <FormGroup
        className="multiselect"
        controlId={this.props.key}
        key={this.props.key} >
        <FormLabel label={this.props.label} mandatory={this.props.mandatory} />
        <Select
          defaultValue={this.state.options}
          isMulti
          options={this.state.options}
          className="basic-multi-select"
          classNamePrefix="select"
          value={selectedOptions}
          onChange={this.handleChange}
        />
      </FormGroup>
    )
  }
}
