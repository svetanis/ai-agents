package com.svetanis.agentpatterns.a2aclient;

import javax.inject.Provider;

import com.google.adk.a2a.agent.RemoteA2AAgent;
import com.google.adk.agents.BaseAgent;

import io.a2a.client.Client;
import io.a2a.client.config.ClientConfig;
import io.a2a.client.http.A2ACardResolver;
import io.a2a.client.http.JdkA2AHttpClient;
import io.a2a.client.transport.jsonrpc.JSONRPCTransport;
import io.a2a.client.transport.jsonrpc.JSONRPCTransportConfig;
import io.a2a.spec.AgentCard;

public class RemoteProductCatalogAgent implements Provider<BaseAgent> {

  private static final String BASE_URL = "http://localhost:9090";
  private static final String AGENT_CARD = "/.well-known/agent-card.json";

  @Override
  public BaseAgent get() {
    AgentCard card = agentCard();
    Client client = a2aClient(card);
    return RemoteA2AAgent.builder()//
        .name(card.name())//
        .a2aClient(client)//
        .agentCard(card)//
        .build();
  }

  private Client a2aClient(AgentCard card) {
    JSONRPCTransportConfig tc = new JSONRPCTransportConfig();
    return Client.builder(card)//
        .withTransport(JSONRPCTransport.class, tc)//
        .clientConfig(clientConfig(card)).build();
  }

  private ClientConfig clientConfig(AgentCard card) {
    boolean streaming = card.capabilities().streaming();
    return ClientConfig.builder()//
        .setStreaming(streaming)//
        .build();
  }

  private AgentCard agentCard() {
    String url = BASE_URL + AGENT_CARD;
    JdkA2AHttpClient client = new JdkA2AHttpClient();
    return new A2ACardResolver(client, BASE_URL, url).getAgentCard();
  }
}