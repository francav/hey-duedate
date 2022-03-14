import React, { Component } from "react";

import "bootstrap/dist/css/bootstrap.min.css";

import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";

import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";

import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";

import Table from "react-bootstrap/Table";

import "./App.css";

import axios from "axios";

class App extends Component {
  constructor() {
    super();
    this.state = {
      startDateTime: "",
      dueDateTime: "",
      sla: "",
      calculationLogBlocks: [],
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
          this.state.sla +
          "/log"
      )
      .then((response) => {
        this.setState({
          dueDateTime: new Date(response.data.dueDateTime).toLocaleString(),
          calculationLogBlocks: response.data.calculationLogBlocks,
        });
      })
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

        <Row className="p-5">
          <Col>
            <Table responsive>
              <thead>
                <tr>
                  <th>Start</th>
                  <th>End</th>
                  <th>Time to Work</th>
                  <th>Note</th>
                </tr>
              </thead>
              <tbody>
                {this.state.calculationLogBlocks &&
                  this.state.calculationLogBlocks.map((item) => (
                    <tr key={item.start}>
                      <td>{item.start}</td>
                      <td>{item.end}</td>
                      <td>{item.slaUsedTimeInMinutes}</td>
                      <td>{!item.on ? 'OFF' : (item.dstAffected ? 'DST' : '')}</td>
                    </tr>
                  ))}
              </tbody>
            </Table>
          </Col>
        </Row>
      </Container>
    );
  }
}
export default App;
