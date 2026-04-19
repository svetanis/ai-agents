package com.svetanis.multiagentpatterns.a2aclient;

import static com.google.api.client.util.Preconditions.checkNotNull;
import static java.util.Arrays.asList;

import java.util.Map;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.svetanis.multiagentpatterns.base.AgentConfig;
import com.svetanis.multiagentpatterns.base.AgentConfigsProvider;
import com.svetanis.multiagentpatterns.base.AgentContext;
import com.svetanis.multiagentpatterns.base.LlmAgentProvider;

import jakarta.inject.Provider;

public class CustomerSupportAgent implements Provider<LlmAgent> {

  private static final String CSA_KEY = "a2aclient.customer.support.agent";

  public CustomerSupportAgent(AgentConfigsProvider provider) {
    this.provider = checkNotNull(provider, "provider");
  }

  private final AgentConfigsProvider provider;

  @Override
  public LlmAgent get() {
    AgentContext ctx = agentContext(provider.get());
    return new LlmAgentProvider(ctx).get();
  }

  private AgentContext agentContext(Map<String, AgentConfig> configs) {
    BaseAgent pca = new RemoteProductCatalogAgent().get();
    AgentConfig config = configs.get(CSA_KEY);
    return AgentContext//
        .builder()//
        .withConfig(config)//
        .withSubAgents(asList(pca))//
        .build();
  }
}