import React from 'react';

import {Navbar /* , Nav, NavItem, NavDropdown, MenuItem, */} from 'react-bootstrap';
import QuickSearch from "../quick-search/QuickSearch";

const  Header = () => (
    <Navbar>
      <Navbar.Header>
        <Navbar.Brand>
          <a href="#home">Soul Land</a>
        </Navbar.Brand>
      </Navbar.Header>
      <Navbar.Toggle />
      <Navbar.Collapse>
        <Navbar.Text>
          The trading card market place
        </Navbar.Text>
        <Navbar.Form pullRight>
          <QuickSearch enabled />
        </Navbar.Form>
      </Navbar.Collapse>
    </Navbar>
);

export default Header;
