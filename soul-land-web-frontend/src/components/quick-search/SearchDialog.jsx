import React, { Component } from "react";
import PropTypes from "prop-types";
import {
  Panel,
  Form,
  FormGroup,
  FormControl,
  ControlLabel,
  HelpBlock,
  Button,
  Checkbox,
  Alert
} from "react-bootstrap";

import { withStyles } from "material-ui/styles";
import { CircularProgress } from "material-ui/Progress";

import SearchResult from "./SearchResult";
import QuickSearchErrorBoundary from "./QuickSearchErrorBoundary";

const loaderStyles = theme => ({
  progress: {
    margin: `0 ${theme.spacing.unit * 2}px`
  }
});

const types = [
  {
    value: "rare",
    label: "Rare",
    helpTip: "Input can be in any of these form: xxxxxx, xxxx, xxxxx"
  },
  { value: "love", label: "Love", helpTip: "" },
  { value: "hero", label: "Hero", helpTip: "Same as main search" },
  {
    value: "any",
    label: "Any",
    helpTip: "Search with type 'Any' is case insensitive"
  }
];

// should eventually move to util
const delay = (func, timeInMs = 1000) => {
  const immediateAction = new Promise(resolve => {
    setTimeout(() => {
      resolve();
    }, timeInMs);
  });
  immediateAction.then(func);
};

class SearchDialog extends Component {
  constructor(props) {
    super(props);

    const defaultTermType = "any";

    this.state = {
      searchTerm: "",
      termType: defaultTermType,
      permutate: false,
      permutateOption: false,
      additionalHelpTip: types.filter(t => t.value === defaultTermType)[0]
        .helpTip,
      showHelpTip: true,
      loadingSearchResult: false
    };
  }

  componentDidMount() {
    delay(() => {
      this.setState({ searchTerm: "test" });
      this.performSearch();
    });
  }

  getSearchTermInfo = () => {
    const { termType, searchTerm, permutate, permutateOption } = this.state;

    return {
      termType,
      searchTerm,
      permutate: permutateOption ? permutate : false
    };
  };

  handleTermTypeChanged = e => {
    let permutateOption = false;
    let permutate = false;
    if (e.target.value === "rare") {
      permutateOption = true;
      permutate = true;
    }

    this.setState({
      termType: e.target.value,
      permutate,
      permutateOption,
      additionalHelpTip: types.filter(t => t.value === e.target.value)[0]
        .helpTip
    });

    this.searchTermInput.focus();
  };

  performSearch = e => {
    if (e) e.preventDefault();

    if (this.checkInputValidationState() === "error") return;

    this.setState({
      showHelpTip: false,
      loadingSearchResult: true
      // showError: false  // remove previous error message during a search
    });

    const { termType, searchTerm, permutate, permutateOption } = this.state;
    const reqParam = permutateOption ? `?permutate=${permutate}` : "";
    const searchUrl = `/api/search/quick/${termType}/${searchTerm}${reqParam}`;
    console.log(`quick search submitted to ${searchUrl}`, this.state);
    fetch(searchUrl)
      .then(resp => {
        if (!resp.ok) throw Error(resp.statusText);
        return resp;
      })
      .then(result => result.json())
      .then(result => {
        this.setState({ loadingSearchResult: false, result });
        this.searchResult.showResultIfHidden();
      })
      .catch(err => this.handleSearchError(err));
  };

  handleSearchError = error => {
    console.error("hit error with search", error);
    this.setState({
      showError: true,
      loadingSearchResult: false
    });
  };

  handleDismiss = () => {
    this.setState({ showError: false });
  };

  moveCaretAtEndAndShowHelpTips = e => {
    const tempValue = e.target.value;
    e.target.value = "";
    e.target.value = tempValue;

    this.setState({ showHelpTip: true });
  };

  checkInputValidationState = () => {
    const len = this.state.searchTerm.length;
    if (len === 0) return "error";
    return null;
  };

  renderSearchInputs() {
    return (
      <React.Fragment>
        <Form inline>
          <FormGroup controlId="frmQSType">
            <ControlLabel>Type</ControlLabel>{" "}
            <FormControl
              componentClass="select"
              placeholder="Card Type"
              value={this.state.termType}
              onChange={this.handleTermTypeChanged}
            >
              {types.map(option => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </FormControl>
          </FormGroup>
          {"   "}
          <FormGroup
            controlId="frmQSTermAndOption"
            style={{ width: "50%" }}
            validationState={this.checkInputValidationState()}
          >
            <FormControl
              style={{ width: this.state.permutateOption ? "75%" : "100%" }}
              type="text"
              placeholder="Enter trading card"
              inputRef={searchTermInput => {
                this.searchTermInput = searchTermInput;
              }}
              autoFocus
              autoComplete="off"
              onFocus={this.moveCaretAtEndAndShowHelpTips}
              value={this.state.searchTerm}
              onChange={e => this.setState({ searchTerm: e.target.value })}
            />
            {this.state.permutateOption && (
              <span>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <Checkbox
                  checked={this.state.permutate}
                  onChange={e => this.setState({ permutate: e.target.checked })}
                >
                  {" "}
                  Permutate
                </Checkbox>
              </span>
            )}
          </FormGroup>
          {"   "}
          <FormGroup controlId="frmQSGoBtn">
            <Button
              type="submit"
              bsStyle="success"
              disabled={this.checkInputValidationState() === "error"}
              onClick={this.performSearch}
            >
              Go!
            </Button>
          </FormGroup>
        </Form>
        {this.state.showHelpTip && (
          <HelpBlock>
            <ul>
              {this.state.additionalHelpTip &&
                this.state.additionalHelpTip !== "" && (
                  <li>{this.state.additionalHelpTip}</li>
                )}
              <li>
                Note that wild cards and multiple search terms are not supported
              </li>
              <li>Search will be performed on Bandai and Nintento DB</li>
            </ul>
          </HelpBlock>
        )}
        <br />
      </React.Fragment>
    );
  }

  renderSearchErrorAlert() {
    return (
      <Alert bsStyle="danger" onDismiss={this.handleDismiss}>
        <strong>Search failed or took longer than expected.</strong>
        <br />
        Please try again after 30 seconds or inform the administrator if issue
        persists after 3 tries.
      </Alert>
    );
  }

  render() {
    const { classes } = this.props;
    return (
      <QuickSearchErrorBoundary>
        <Panel style={{ borderStyle: "none", boxShadow: "none" }}>
          <br />
          {this.state.showError && this.renderSearchErrorAlert()}
          {this.renderSearchInputs()}
          {this.state.loadingSearchResult && (
            <CircularProgress className={classes.progress} />
          )}
          {this.state.result && (
            <SearchResult
              data={this.state.result}
              queryInfo={this.getSearchTermInfo()}
              ref={r => {
                this.searchResult = r;
              }}
            />
          )}
        </Panel>
      </QuickSearchErrorBoundary>
    );
  }
}

export default withStyles(loaderStyles)(SearchDialog);

SearchDialog.propTypes = {
  classes: PropTypes.object.isRequired
};
