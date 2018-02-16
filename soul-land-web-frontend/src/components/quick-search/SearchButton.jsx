/* eslint-disable */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Button } from 'react-bootstrap';
import { Button as MaterialButton, IconButton, Icon } from 'material-ui';
import { PhotoCamera, Search as SearchIcon } from 'material-ui-icons';
import { withStyles } from 'material-ui/styles';

const styles = theme => ({
  button: {
    margin: theme.spacing.unit,
  },
  input: {
    display: 'none',
  },
});

class SearchButton extends Component {

  renderFab() {
    return (
      <MaterialButton variant="fab" color="secondary" style={{position: 'absolute'}} onClick={this.props.onClick}>
        <SearchIcon/>
      </MaterialButton>
    );
  }

  renderBootstrap() {
    return (
      <Button bsStyle="danger" onClick={this.props.onClick} className="btn-circle btn-xl">
        <i className="fa fa-search fa-fw"/>
      </Button>
    );
  }

  render() {

    if(this.props.material) return this.renderFab();
    else return this.renderBootstrap();
  }
}

SearchButton.propTypes = {
  material: PropTypes.bool,
};

SearchButton.defaultProps = {
  material: true
};

export default withStyles(styles)(SearchButton);
