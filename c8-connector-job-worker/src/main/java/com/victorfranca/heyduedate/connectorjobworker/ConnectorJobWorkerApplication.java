package com.victorfranca.heyduedate.connectorjobworker;

import java.util.concurrent.ExecutionException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.camunda.connector.HeyDueDateFunction;
import io.camunda.connector.runtime.jobworker.ConnectorJobHandler;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProvider;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProviderBuilder;

@SpringBootApplication
public class ConnectorJobWorkerApplication {

	private static final String zeebeAPI = "04322dc6-3d72-4d72-8dfc-484189a06927.bru-2.zeebe.camunda.io";
	private static final String clientId = "vGMj5t5PK1LObVszf5mswW6QtFr5-xk6";
	private static final String clientSecret = "TXNWoTDYqRRYbs7QUleD_HSEgKJ.TGNr3.6PJIrtzzpM~7LaACOeEdeViLd8AfBD";
	private static final String oAuthAPI = "https://login.cloud.camunda.io/oauth/token";

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		SpringApplication.run(ConnectorJobWorkerApplication.class, args);

		OAuthCredentialsProvider credentialsProvider = new OAuthCredentialsProviderBuilder()
				.authorizationServerUrl(oAuthAPI).audience(zeebeAPI).clientId(clientId).clientSecret(clientSecret)
				.build();

		try (ZeebeClient client = ZeebeClient.newClientBuilder().gatewayAddress(zeebeAPI)
				.credentialsProvider(credentialsProvider).build()) {
			client.newWorker().jobType("io.camunda:my-connector:1")
					.handler(new ConnectorJobHandler(new HeyDueDateFunction())).name("HeyDueDateWorker")
					.fetchVariables("foo", "bar").open();
		}
	}

}
