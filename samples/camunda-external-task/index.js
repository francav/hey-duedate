const {
  Client,
  logger,
  Variables
} = require("camunda-external-task-client-js")

const axios = require("axios")

// configuration for the Client:
//  - 'baseUrl': url to the Process Engine
//  - 'logger': utility to automatically log important events
const config = {
  baseUrl: "http://localhost:8080/engine-rest",
  use: logger
}

// create a Client instance with custom configuration
const client = new Client(config)

// create a handler for the task
const handler = async ({ task, taskService }) => {
  const startDateTime = task.variables.get("startDateTime");
  const sla = task.variables.get("slaInMinutes");
  const logEnabled = false;

  axios
    .get(
      "http://localhost:8081" +
      "/duedate?" +
      "calendar=" +
      "calendar-sample" +
      "&start=" +
      startDateTime.toISOString() +
      "&sla=" +
      sla +
      "&log="+
      logEnabled
    )
    .then((response) => {
      var processVariables = new Variables()
      processVariables.set("dueDate", new Date(response.data.dueDateTime + "Z"))
      processVariables.set("startDateTimeUTC", startDateTime.toISOString())
      processVariables.set("dueDateUTC", new Date(response.data.dueDateTime + "Z").toISOString())
      taskService.complete(task, processVariables)
    })
    .catch((error) =>
        console.error(`Failed completing my task,` + error)
    )
  
}

client.subscribe("dueDateCalculator", handler)