package com.svetanis.agentpatterns.base;

import static com.google.adk.agents.LlmAgent.IncludeContents.DEFAULT;
import static com.google.adk.agents.LlmAgent.IncludeContents.valueOf;
import static com.google.api.client.util.Preconditions.checkNotNull;

import com.google.adk.agents.LlmAgent;
import com.google.common.base.CharMatcher;
import com.google.common.base.Optional;

import jakarta.inject.Provider;

public class LlmAgentProvider implements Provider<LlmAgent> {

  public LlmAgentProvider(AgentConf config) {
    this(AgentContext.build(config));
  }

  public LlmAgentProvider(AgentContext ctx) {
    this.ctx = checkNotNull(ctx, "ctx");
  }

  private final AgentContext ctx;

  @Override
  public LlmAgent get() {
    AgentConf config = ctx.getConfig();
    LlmAgent.Builder builder = LlmAgent.builder();
    builder.name(config.getName());
    builder.description(config.getDescription());
    builder.model(config.getModel());
    builder.instruction(config.getInstruction());
    builder.includeContents(includeContents(config));
    if (config.getOutputKey().isPresent()) {
      builder.outputKey(config.getOutputKey().get());
    }
    builder.tools(ctx.getTools());
    builder.subAgents(ctx.getSubAgents());
    return builder.build();
  }

  private LlmAgent.IncludeContents includeContents(AgentConf config) {
    Optional<String> incl = config.getIncludeContents();
    if (incl.isPresent()) {
      String trimmed = CharMatcher.whitespace().trimFrom(incl.get());
      return valueOf(trimmed.toUpperCase());
    }
    return DEFAULT;
  }
}
