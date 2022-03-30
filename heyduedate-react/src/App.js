import React, { Component } from "react"

import "bootstrap/dist/css/bootstrap.min.css"

import Container from "react-bootstrap/Container"
import Row from "react-bootstrap/Row"
import Col from "react-bootstrap/Col"

import Form from "react-bootstrap/Form"
import Button from "react-bootstrap/Button"

import DatePicker from "react-datepicker"
import "react-datepicker/dist/react-datepicker.css"

import Table from "react-bootstrap/Table"

import "./App.css"

import axios from "axios"

class App extends Component {
  constructor() {
    super()
    this.state = {
      calendars: [],
      calendar: "",
      startDateTime: "",
      dueDateTime: "",
      sla: "",
      calculationLogBlocks: [],
    }

    this.handleChange = this.handleChange.bind(this)
    this.handleClick = this.handleClick.bind(this)
  }

  componentDidMount() {
    if (process.env.REACT_APP_API_URL) {
      axios.get(process.env.REACT_APP_API_URL + "/calendar").then((response) => {
        this.setState({
          calendars: response.data,
          calendar: response.data.length > 1 ? response.data[0] : null
        })
      })
    }
  }

  handleChange(evt) {
    if (evt instanceof Date) {
      this.setState({ startDateTime: new Date(evt) })
    } else {
      this.setState({ [evt.target.name]: evt.target.value })
    }
    this.clearForm()
  }

  handleClick() {
    axios
      .get(
        process.env.REACT_APP_API_URL +
        "/duedate?" +
        "calendar=" +
        this.state.calendar +
        "&start=" +
        this.state.startDateTime.toISOString() +
        "&sla=" +
        this.state.sla +
        "&log=true"
      )
      .then((response) => {
        this.setState({
          dueDateTime: response.data.dueDateTime,
          calculationLogBlocks: response.data.calculationLogBlocks,
        })
      })
      .catch((error) =>
        this.setState({
          dueDateTime: error.message,
          calculationLogBlocks: [],
        })
      )
  }

  clearForm() {
    this.setState({
      dueDateTime: "",
      calculationLogBlocks: [],
    })
  }

  convertTimeZoneLessDateToUTCString(date) {
    return new Date(date + "Z").toUTCString()
  }

  render() {
    return process.env.REACT_APP_API_URL ? (
      <Container className="d-flex flex-column justify-content-center align-items-center">
        <Form className="p-5 border w-50">
          <Row>
            <Col>
              <Form.Label>Calendar</Form.Label>
            </Col>
          </Row>
          <Row>
            <Col>
              <Form.Select
                name="calendar"
                value={this.state.calendar}
                onChange={this.handleChange}
                className="form-control"
              >
                {this.state.calendars.map((opt) => (
                  <option value={opt}>{opt}</option>
                ))}
              </Form.Select>
            </Col>
          </Row>

          <Row className="p-3">
            <Col></Col>
          </Row>

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
            <Col className="d-flex justify-content-center">
              <Button onClick={this.handleClick}>Hey Due Date!</Button>
            </Col>
          </Row>
        </Form>

        <Row className="p-3">
          <Col></Col>
        </Row>

        {this.state.startDateTime ? (
          <Row>
            <Col>
              <img src="start.png" height={30} alt="Start from" />
              <h3 className="start-date">
                {this.state.startDateTime
                  ? new Date(this.state.startDateTime).toUTCString()
                  : ""}
              </h3>
            </Col>
          </Row>
        ) : (
          ""
        )}

        <Row className="p-3">
          <Col></Col>
        </Row>

        {this.state.dueDateTime ? (
          <Row>
            <Col>
              <img src="due-date.png" height={30} alt="due to" />
              <h3 className="due-date">
                {this.state.dueDateTime
                  ? this.convertTimeZoneLessDateToUTCString(
                    this.state.dueDateTime
                  )
                  : ""}
              </h3>
            </Col>
          </Row>
        ) : (
          ""
        )}

        {this.state.calculationLogBlocks.length > 0 ? (
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
                      <tr
                        key={item.start}
                      >
                        <td>
                          {this.convertTimeZoneLessDateToUTCString(item.start)}
                        </td>
                        <td>
                          {this.convertTimeZoneLessDateToUTCString(item.end)}
                        </td>
                        <td>{item.slaUsedTimeInMinutes}</td>
                        <td>
                          {item.locationId +
                            (!item.on
                              ? " (OFF)"
                              : item.dstAffected
                                ? " (DST)"
                                : "")}
                        </td>
                      </tr>
                    ))}
                </tbody>
              </Table>
            </Col>
          </Row>
        ) : (
          ""
        )}
      </Container>
    )
      : (
        <Container className="d-flex flex-column justify-content-center align-items-center">
          <h5><i>No server API URL in the format http[s]://[host]:[port] defined at REACT_APP_API_URL environment variable</i></h5>
        </Container>
      )
  }
}
export default App
