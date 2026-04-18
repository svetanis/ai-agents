package com.svetanis.agentpatterns.blogger;

import static com.google.api.client.util.Preconditions.checkNotNull;

import java.util.Map;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.AgentTool;
import com.google.common.collect.ImmutableMap;
import com.svetanis.agentpatterns.base.AgentConf;
import com.svetanis.agentpatterns.base.AgentContext;
import com.svetanis.agentpatterns.base.LlmAgentProvider;

import jakarta.inject.Provider;

public class RootAgent implements Provider<LlmAgent> {

  private static final String BRA_KEY = "blogger.root.agent";

  public RootAgent(Provider<ImmutableMap<String, AgentConf>> provider) {
    this.provider = checkNotNull(provider, "provider");
  }

  private final Provider<ImmutableMap<String, AgentConf>> provider;

  @Override
  public LlmAgent get() {
    Map<String, AgentConf> configs = provider.get();
    AgentContext ctx = agentCtx(BRA_KEY, configs);
    return new LlmAgentProvider(ctx).get();
  }

  private AgentContext agentCtx(String key, Map<String, AgentConf> configs) {
    AgentConf config = configs.get(key);
    AgentTool pipeline = AgentTool.create(new BlogCreationPipeline(configs).get());
    return AgentContext.build(config, pipeline);
  }
}
