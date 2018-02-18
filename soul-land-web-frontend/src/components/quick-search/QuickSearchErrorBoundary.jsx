import React, { Component } from "react";
import PropTypes from "prop-types";

import { Alert } from "react-bootstrap";

class QuickSearchErrorBoundary extends Component {
  constructor(props) {
    super(props);

    this.state = {
      error: null,
      errorInfo: null
    };
  }

  componentDidCatch(error, errorInfo) {
    this.setState({
      error,
      errorInfo
    });
    // can also log error messages to an error reporting service here
  }

  render() {
    const { error, errorInfo } = this.state;

    if (errorInfo) {
      return (
        <Alert bsStyle="danger">
          <h2>Unexpected error ocurred</h2>
          Service is currently unavailable due to error. Please inform the
          administrator about this issue.
          <br />
          <details style={{ whiteSpace: "pre-wrap" }}>
            {error && error.toString()}
            <br />
            {errorInfo.componentStack}
          </details>
        </Alert>
      );
    }

    // just render children if no error occurred
    return this.props.children;
  }
}

QuickSearchErrorBoundary.propTypes = {
  children: PropTypes.oneOfType([
    PropTypes.arrayOf(PropTypes.node),
    PropTypes.node
  ]).isRequired
};

export default QuickSearchErrorBoundary;
