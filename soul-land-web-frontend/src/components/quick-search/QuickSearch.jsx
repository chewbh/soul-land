import React, { Component } from "react";
import PropTypes from "prop-types";

import _ from "lodash";
import { Modal, Button, Tabs, Tab } from "react-bootstrap";

import { Button as MaterialButton } from "material-ui";
import Dialog, {
  DialogActions,
  DialogContent,
  // DialogContentText,
  DialogTitle
} from "material-ui/Dialog";

import "./QuickSearch.css";
import SearchButton from "./SearchButton";
import SearchDialog from "./SearchDialog";

class QuickSearch extends Component {
  constructor(props) {
    super(props);

    this.state = {
      dialogOpen: true, // TODO  - false
      activeTab: "Card Search",
      searchTabs: [
        {
          type: "card",
          title: "Card Search",
          searchTerm: "",
          termType: "any",
          closable: false
        },
        {
          type: "card",
          title: "Card Search 2",
          searchTerm: "",
          termType: "rare",
          closable: true
        }
      ]
    };
  }

  removeDialog(tab) {
    const removeTabIndex = _.findIndex(
      this.state.searchTabs,
      t => t.title === tab.title
    );
    const searchTabs = [
      ...this.state.searchTabs.slice(0, removeTabIndex),
      ...this.state.searchTabs.slice(removeTabIndex + 1)
    ];
    const activeTab =
      this.state.activeTab === tab.title
        ? this.state.searchTabs[removeTabIndex - 1].title
        : this.state.activeTab;
    this.setState({ searchTabs, activeTab });
    console.log(searchTabs, activeTab);
  }

  handleSelectTab = key => {
    this.setState({ activeTab: key });
  };

  openSearchDialog = () => {
    this.setState({ dialogOpen: true });
  };

  closeSearchDialog = () => {
    this.setState({ dialogOpen: false });
  };

  renderMaterialDialog() {
    return (
      <Dialog open={this.state.dialogOpen} onClose={this.closeSearchDialog}>
        <DialogTitle>Quick Search</DialogTitle>
        <DialogContent>
          <SearchDialog />
        </DialogContent>
        <DialogActions>
          <MaterialButton onClick={this.closeSearchDialog} color="primary">
            Close
          </MaterialButton>
        </DialogActions>
      </Dialog>
    );
  }

  renderDialog() {
    return (
      <Modal
        dialogClassName="wideModalDialog"
        show={this.state.dialogOpen}
        onHide={this.closeSearchDialog}
      >
        <Modal.Header closeButton>
          <Modal.Title>Quick Search</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Tabs
            activeKey={this.state.activeTab}
            onSelect={this.handleSelectTab}
            id="quick-search-tabs"
            animation={false}
          >
            {this.state.searchTabs.map(tab => (
              <Tab
                eventKey={tab.title}
                key={`${tab.title}`}
                title={
                  !tab.closable ? (
                    tab.title
                  ) : (
                    <React.Fragment>
                      {tab.title}{" "}
                      <Button
                        bsStyle="link"
                        bsSize="small"
                        onClick={() => this.removeDialog(tab)}
                      >
                        <i className="fa fa-window-close" />
                      </Button>
                    </React.Fragment>
                  )
                }
              >
                <div>
                  <br />
                  <br />
                  <br />
                  <SearchDialog />
                </div>
              </Tab>
            ))}
          </Tabs>
        </Modal.Body>
        <Modal.Footer>
          <Button onClick={this.closeSearchDialog} bsStyle="primary">
            Close
          </Button>
          {false && (
            <MaterialButton
              onClick={this.closeSearchDialog}
              variant="raised"
              style={{ fontSize: 12 }}
            >
              Close
            </MaterialButton>
          )}
        </Modal.Footer>
      </Modal>
    );
  }

  render() {
    const material = true;

    const comp = (
      <div>
        <SearchButton onClick={this.openSearchDialog} material={material} />
        {this.renderDialog()}
      </div>
    );

    return this.props.enabled ? comp : <div />;
  }
}

QuickSearch.propTypes = {
  enabled: PropTypes.bool
};

QuickSearch.defaultProps = {
  enabled: false
};

export default QuickSearch;
