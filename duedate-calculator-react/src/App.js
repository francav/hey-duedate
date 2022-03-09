import React, { Component } from "react";

import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";

import "bootstrap/dist/css/bootstrap.min.css";
import Container from "react-bootstrap/Container";
import Button from "react-bootstrap/Button";
import Col from "react-bootstrap/Col";

import "./App.css";

import axios from "axios";
import { Row } from "reactstrap";

class App extends Component {
  constructor() {
    super();
    this.state = {
      startDateTime: "",
      dueDateTime: "",
      sla: "",
    };

    this.handleChange = this.handleChange.bind(this);
    this.handleClick = this.handleClick.bind(this);
  }

  handleChange(evt) {
    if (evt instanceof Date) {
      this.setState({ startDateTime: evt });
    } else {
      this.setState({ [evt.target.name]: evt.target.value });
    }
  }

  handleClick() {
    axios
      .get(
        "http://localhost:8080/duedate/" +
          this.state.startDateTime.toISOString() +
          "/" +
          this.state.sla
      )
      .then((response) => this.setState({ dueDateTime: response.data }))
      .catch((error) => this.setState({ dueDateTime: error.message }));
  }

  render() {
    return (
      <Container className="p-3">
        <Row>
          <Col>
            <label>
              Start Date/Time:
              <DatePicker
                name="startDateTime"
                selected={this.state.startDateTime}
                onChange={this.handleChange}
                showTimeSelect
                dateFormat="MM/dd/yyyy EE hh:mm a"
              />
            </label>
            <label>
              SLA(minutes)
              <input
                name="sla"
                value={this.state.sla}
                onChange={this.handleChange}
              />
            </label>
          </Col>
        </Row>

        <button
          type="submit"
          className="btn btn-primary"
          onClick={this.handleClick}
        >
          Submit
        </button>

        <Row>
          <p>{this.state.dueDateTime}</p>
        </Row>

        <Row>
          <p>{this.state.errorMessage}</p>
        </Row>
      </Container>
    );
  }
}
export default App;
