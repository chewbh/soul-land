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
  Checkbox
} from "react-bootstrap";

import { withStyles } from "material-ui/styles";
import { CircularProgress } from "material-ui/Progress";

import SearchResult from "./SearchResult";

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

  // renderMaterial() {
  //   return (
  //     <TextField id="select-qs-type"
  //                select
  //                SelectProps={{
  //                  native: true,
  //                }}
  //                style={{fontSize: 120}}
  //                label="Type" margin="normal"
  //                defaultValue="Any">
  //       { types.map(option => ( <option key={option.value} value={option.value}>{option.label}</option> )) }
  //     </TextField>
  //   )
  // }

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
    e.preventDefault();

    if (this.checkInputValidationState() === "error") return;

    this.setState({
      showHelpTip: false,
      loadingSearchResult: true
    });

    const { termType, searchTerm, permutate, permutateOption } = this.state;
    const reqParam = permutateOption ? `?permutate=${permutate}` : "";
    const searchUrl = `/api/search/quick/${termType}/${searchTerm}${reqParam}`;
    console.log(`quick search submitted to ${searchUrl}`, this.state);
    fetch(searchUrl)
      .then(result => result.json())
      .then(result => this.setState({ result }));
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
      <div>
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
      </div>
    );
  }

  render() {
    const { classes } = this.props;
    return (
      <Panel style={{ borderStyle: "none" }}>
        Search all type of cards quickly
        <br />
        <br />
        {this.renderSearchInputs()}
        {this.state.loadingSearchResult && (
          <CircularProgress className={classes.progress} />
        )}
        <SearchResult data={this.state.result} />
      </Panel>
    );
  }
}

export default withStyles(loaderStyles)(SearchDialog);

SearchDialog.propTypes = {
  classes: PropTypes.object.isRequired
};
