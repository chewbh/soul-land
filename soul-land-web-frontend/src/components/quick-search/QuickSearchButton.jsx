import React, { Component } from 'react';
import { Button } from 'react-bootstrap';

export default class QuickSearchButton extends Component {

  render() {
    return (
      <Button bsStyle="danger" pull>
        <i className="fa fa-search fa-fw"/>
      </Button>
    );
  }

}
