import React, { Component } from "react";
import PropTypes from "prop-types";

import { Button, Panel, Col, Row } from "react-bootstrap";
import Highlighter from "react-highlight-words";
import ReactTable from "react-table";
import "react-table/react-table.css";

// import { ContextMenu, MenuItem, ContextMenuTrigger } from "react-contextmenu";
// import _ from "lodash";

import XLSX from "xlsx";
import json2csv from "json2csv";
import { saveAs } from "file-saver";

const Tips = () => (
  <div style={{ textAlign: "center" }}>
    <em>Tip: Hold shift when sorting to multi-sort!</em>
  </div>
);

// to be set by wrapper components
const enhanceReactTableColumns = givenColumns =>
  givenColumns.map(col =>
    Object.assign(
      {},
      {
        show: true,
        Cell: prop => {
          const { filtered } = prop.tdProps.rest;
          const relevantFiltered = filtered
            ? filtered.filter(f => f.id === prop.column.id)
            : [];
          let filterText = "";
          if (relevantFiltered.length > 0) {
            filterText = relevantFiltered[0].value;
          }
          return filterText !== "" ? (
            <Highlighter
              caseSensitive={false}
              searchWords={[filterText]}
              textToHighlight={prop.value}
            />
          ) : (
            prop.value
          );
        }
      },
      col
    )
  );

// pass in the filename generation func

function toAoa(data, tableColumns) {
  // transform data into parsable XLSX worksheet form (array of array - aoa)
  const transformedData = data.map(d =>
    tableColumns
      .filter(col => col.show)
      .map(
        col =>
          Object.prototype.hasOwnProperty.call(d, col.accessor)
            ? d[col.accessor]
            : null
      )
  );
  transformedData.unshift(
    tableColumns.filter(col => col.show).map(c => c.Header)
  );
  return transformedData;
}

function toCsv(data, tableColumns) {
  // return JSON.stringify(data);
  // return XLSX.utils.sheet_to_csv(XLSX.utils.aoa_to_sheet(toAoa(data)));

  return json2csv({
    data,
    fields: tableColumns.filter(col => col.show).map(col => ({
      label: col.Header,
      value: col.accessor
    }))
  });
}

const resultFilterMethod = (filter, row /* , column */) => {
  const id = filter.pivotId || filter.id;
  return row[id] !== undefined
    ? String(row[id])
        .toLowerCase()
        .includes(filter.value.toLowerCase())
    : true;
};

class ResultTable extends Component {
  static propTypes = {
    data: PropTypes.array.isRequired,
    columns: PropTypes.array.isRequired,
    exportFileName: PropTypes.func,
    showExportTop: PropTypes.bool,
    showExportBottom: PropTypes.bool,
    maxHeight: PropTypes.string
  };

  static defaultProps = {
    exportFileName: ext => `exported_data.${ext}`,
    showExportTop: true,
    showExportBottom: false,
    maxHeight: ""
  };

  constructor(props) {
    super(props);

    this.state = {
      open: true
    };
  }

  showResultIfHidden = () => {
    this.setState({ open: true });
  };

  exportCSV = () => {
    try {
      const { exportFileName } = this.props;
      const visibleData = this.tableInstance.getResolvedState().sortedData;
      const transformedData = toCsv(
        visibleData,
        enhanceReactTableColumns(this.props.columns)
      );
      const filename = exportFileName("csv");

      console.log(
        `exporting data to ${filename}`,
        visibleData,
        transformedData
      );

      const blob = new Blob([transformedData], {
        type: "text/csv;charset=utf-8"
      });
      saveAs(blob, filename);
    } catch (e) {
      console.error("encountering issue during export of csv", e);
    }
  };

  exportExcel = () => {
    try {
      const { exportFileName } = this.props;
      const visibleData = this.tableInstance.getResolvedState().sortedData;
      const transformedData = toAoa(
        visibleData,
        enhanceReactTableColumns(this.props.columns)
      );
      const filename = exportFileName("xlsx");

      console.log(
        `exporting data to ${filename}`,
        visibleData,
        transformedData
      );

      const worksheet = XLSX.utils.aoa_to_sheet(transformedData);
      const workbook = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(workbook, worksheet, "exported_data");
      XLSX.writeFile(workbook, filename);
    } catch (e) {
      console.error("encountering issue during export of excel", e);
    }
  };

  renderExportButtons() {
    return (
      <React.Fragment>
        <Button bsStyle="default" onClick={this.exportCSV}>
          CSV
        </Button>
        &nbsp;&nbsp;&nbsp;
        <Button bsStyle="default" onClick={this.exportExcel}>
          Excel
        </Button>
        <br />
      </React.Fragment>
    );
  }

  render() {
    const {
      data,
      showExportTop,
      showExportBottom,
      maxHeight,
      columns
    } = this.props;

    const tableStyle = maxHeight !== "" ? { maxHeight } : {};

    return (
      <React.Fragment>
        <Row>
          <Col sm={8}>
            {showExportTop && data.length > 0 && this.renderExportButtons()}
          </Col>
          <Col sm={4}>
            <Button
              onClick={() => this.setState({ open: !this.state.open })}
              className="pull-right"
            >
              {this.state.open ? "Hide" : "Show"} Result
            </Button>
          </Col>
        </Row>
        <br />
        {this.state.open && (
          <Panel style={{ borderStyle: "none", boxShadow: "none" }}>
            <ReactTable
              data={data}
              columns={enhanceReactTableColumns(columns)}
              showPaginationTop={false}
              pageSizeOptions={[5, 20, 50, 100, 500, 1000]}
              defaultPageSize={50}
              minRows={3}
              filterable
              defaultFilterMethod={resultFilterMethod}
              getTdProps={(state, rowInfo, column, instance) => ({
                filtered: instance.getResolvedState().filtered
              })}
              ref={r => {
                this.tableInstance = r;
              }}
              noDataText="No record found !"
              style={tableStyle}
              className="-striped -highlight"
            />
          </Panel>
        )}
        {this.state.open && <Tips />}
        {showExportBottom && data.length > 0 && this.renderExportButtons()}
      </React.Fragment>
    );
  }
}

export default ResultTable;

// TODO: search result
// TODO: 2nd tier search
// TODO: moment
// TODO: fix vertial-align

// column show and hide (add or not?)
// make it easy to customize filter?
