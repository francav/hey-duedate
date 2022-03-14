import React, { Component } from "react";

import "bootstrap/dist/css/bootstrap.min.css";

import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";

import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";

import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";

import "./App.css";

import axios from "axios";

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
      .then((response) =>
        this.setState({ dueDateTime: new Date(response.data).toString() })
      )
      .catch((error) => this.setState({ dueDateTime: error.message }));
  }

  render() {
    return (
      <Container className="d-flex flex-column justify-content-center align-items-center">
        <Form className="p-5 border w-50">
          <Row>
            <Col>
              <Form.Label>Start Date/Time</Form.Label>
            </Col>
          </Row>
          <Row>
            <Col>
              <DatePicker
                name="startDateTime"
                selected={this.state.startDateTime}
                onChange={this.handleChange}
                showTimeSelect
                dateFormat="MM/dd/yyyy EE hh:mm a"
                className="form-control"
              />
            </Col>
          </Row>

          <Row className="p-3">
            <Col></Col>
          </Row>

          <Row>
            <Col>
              <Form.Label>SLA(minutes)</Form.Label>
            </Col>
          </Row>
          <Row>
            <Col>
              <Form.Control
                name="sla"
                type="input"
                value={this.state.sla}
                onChange={this.handleChange}
              />
            </Col>
          </Row>

          <Row className="p-3">
            <Col></Col>
          </Row>

          <Row>
            <Col className="d-flex flex-column justify-content-center">
              <Button onClick={this.handleClick}>Hey Due Date!</Button>
            </Col>
          </Row>
        </Form>

        <Row className="p-5">
          <Col>
            {this.state.dueDateTime}
            {this.state.errorMessage}
          </Col>
        </Row>
      </Container>
    );
  }
}
export default App;
