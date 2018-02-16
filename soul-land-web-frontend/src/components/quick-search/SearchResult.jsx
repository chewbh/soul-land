import React, { Component } from "react";
import PropTypes from "prop-types";

import ReactTable from "react-table";
import "react-table/react-table.css";

const columns = [
  {
    Header: "name",
    accessor: "firstname"
  },
  {
    Header: "name 2",
    accessor: "lastname"
  }
];

class SearchResult extends Component {
  render() {
    const { data } = this.props;

    return (
      <div>
        <ReactTable data={data} columns={columns} />
      </div>
    );
  }
}

SearchResult.propTypes = {
  data: PropTypes.array.isRequired
};

export default SearchResult;
