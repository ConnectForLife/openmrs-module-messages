import React from 'react';

export const withFiltersChangedCallback = (callback) => (Component) => 
  class WithFiltersChangedCallback extends React.Component {

  render = () => <Component {...this.props} filtersChangedCallback={callback} />;
}
