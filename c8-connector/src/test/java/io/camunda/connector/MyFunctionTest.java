package io.camunda.connector;

import static org.assertj.core.api.Assertions.assertThat;

import io.camunda.connector.test.ConnectorContextBuilder;
import org.junit.jupiter.api.Test;

public class MyFunctionTest {

  @Test
  void shouldReturnReceivedMessageWhenExecute() throws Exception {
    // given
    var input = new HeyDueDateConnectorRequest();
    var auth = new Authentication();
    input.setMessage("Hello World!");
    input.setAuthentication(auth);
    auth.setToken("xobx-test");
    auth.setUser("testuser");
    var function = new HeyDueDateFunction();
    var context = ConnectorContextBuilder.create()
      .variables(input)
      .build();
    // when
    var result = function.execute(context);
    // then
    assertThat(result)
      .isInstanceOf(HeyDueDateConnectorResult.class)
      .extracting("myProperty")
      .isEqualTo("Message received: Hello World!");
  }
}