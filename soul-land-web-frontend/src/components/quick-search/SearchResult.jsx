import React, { Component } from "react";
import PropTypes from "prop-types";

import { Button } from "react-bootstrap";
import Highlighter from "react-highlight-words";
import ResultTable from "./ResultTable";
import sanitize from "../../util/sanitize-filename";

const clickThrough = (type, value, data) => {
  console.log(type, value, data);
};

const givenColumns = [
  {
    Header: "Name",
    accessor: "firstName",
    // Cell: (prop) => {},
    minWidth: 150,
    maxWidth: 500
    // width: 100,
    // className: '',
    // style: {},
    // filterMethod: (filter, row || rows, column) => {return true},
    // Filter: (<select onChange={event => onFiltersChange(event.target.value)} value={filter ? filter.value : ''}></select>)
  },
  {
    Header: "Name 2",
    accessor: "lastName",
    Cell: prop => {
      const { filtered } = prop.tdProps.rest;
      const relevantFiltered = filtered
        ? filtered.filter(f => f.id === prop.column.id)
        : [];
      let filterText = "";
      if (relevantFiltered.length > 0) {
        filterText = relevantFiltered[0].value;
      }

      return (
        <React.Fragment>
          <Button
            bsStyle="link"
            onContextMenu={e => {
              e.preventDefault();
              clickThrough(prop.original.power, prop.value);
            }}
          >
            {filterText !== "" ? (
              <Highlighter
                caseSensitive={false}
                searchWords={[filterText]}
                textToHighlight={prop.value}
              />
            ) : (
              prop.value
            )}
          </Button>
        </React.Fragment>
      );
    }
  },
  {
    Header: "Power 1",
    accessor: "power"
  },
  {
    Header: "Power 2",
    accessor: "power2"
  },
  {
    Header: "Power 3",
    accessor: "power3"
  }
];

class SearchResult extends Component {
  static propTypes = {
    data: PropTypes.array.isRequired,
    queryInfo: PropTypes.object.isRequired
  };

  generateExportedResultFileName = ext => {
    const { queryInfo } = this.props;
    return `${queryInfo.termType}-${sanitize(queryInfo.searchTerm)}.${ext}`;
  };

  showResultIfHidden = () => {
    this.tableInstance.showResultIfHidden();
  };

  render() {
    const { data } = this.props;

    return (
      <React.Fragment>
        <ResultTable
          data={data}
          columns={givenColumns}
          maxHeight="300px"
          exportFileName={this.generateExportedResultFileName}
          ref={r => {
            this.tableInstance = r;
          }}
        />
      </React.Fragment>
    );
  }
}

export default SearchResult;
