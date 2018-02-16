import React, {Component} from 'react'
import PropTypes from 'prop-types';

import { Modal, Button } from 'react-bootstrap';

import { Button as MaterialButton } from 'material-ui';
import Dialog, {
  DialogActions,
  DialogContent,
  // DialogContentText,
  DialogTitle,
} from 'material-ui/Dialog';

import SearchButton from './SearchButton';
import SearchDialog from './SearchDialog';


class QuickSearch extends Component {

    constructor(props) {
        super(props);

        this.state = {
          dialogOpen: true,  // TODO  - false
        }
    }

  openSearchDialog = () => {
    this.setState({dialogOpen: true});
    console.log("openned", this.state.dialogOpen);
  };

  closeSearchDialog = () => {
    this.setState({dialogOpen: false})
  };


  renderMaterialDialog() {
    return (
      <Dialog open={this.state.dialogOpen} onClose={this.closeSearchDialog}>
        <DialogTitle>Quick Search</DialogTitle>
        <DialogContent>
          <SearchDialog/>
        </DialogContent>
        <DialogActions>
          <MaterialButton onClick={this.closeSearchDialog} color="primary">
            Close
          </MaterialButton>
        </DialogActions>
      </Dialog>
    )
  }

  renderDialog() {
    return (
      <Modal bsSize="large" show={this.state.dialogOpen} onHide={this.closeSearchDialog}>
        <Modal.Header closeButton>
          <Modal.Title>Quick Search</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <SearchDialog />
        </Modal.Body>
        <Modal.Footer>
          <Button onClick={this.closeSearchDialog} bsStyle="primary">
            Close
          </Button>
          <MaterialButton onClick={this.closeSearchDialog} variant="raised" style={{fontSize: 12}}>
            Close
          </MaterialButton>
        </Modal.Footer>
      </Modal>
    )
  }

  render() {

    const material = false;

    const comp = (
      <div>
        <SearchButton onClick={this.openSearchDialog} material={material} />
        { (material)? this.renderMaterialDialog() : this.renderDialog() }
      </div>
    );

    return (this.props.enabled)? comp : <div/>;
  }
}

QuickSearch.propTypes = {
  enabled: PropTypes.bool,
};

QuickSearch.defaultProps = {
  enabled: false,
};


export default QuickSearch;
