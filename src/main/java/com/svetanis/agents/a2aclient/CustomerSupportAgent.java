package com.svetanis.agents.a2aclient;

import static java.util.Arrays.asList;

import javax.inject.Provider;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.svetanis.agents.adk.AgentConfig;
import com.svetanis.agents.adk.AgentConfigProvider;
import com.svetanis.agents.adk.AgentContext;
import com.svetanis.agents.adk.LlmAgentProvider;

public class CustomerSupportAgent implements Provider<LlmAgent> {

  private static final String CUSTOMER_SUPPORT = "a2aclient/customer-support-agent";

  @Override
  public LlmAgent get() {
    AgentContext ctx = ctx();
    return new LlmAgentProvider(ctx).get();
  }

  private AgentContext ctx() {
    BaseAgent pca = new RemoteProductCatalogAgent().get();
    AgentConfig config = new AgentConfigProvider(CUSTOMER_SUPPORT).get();
    return AgentContext//
        .builder()//
        .withConfig(config)//
        .withSubAgents(asList(pca))//
        .build();
  }
}