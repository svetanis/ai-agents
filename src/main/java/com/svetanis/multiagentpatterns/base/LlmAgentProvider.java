package com.svetanis.multiagentpatterns.base;

import static com.google.adk.agents.LlmAgent.IncludeContents.DEFAULT;
import static com.google.adk.agents.LlmAgent.IncludeContents.valueOf;
import static com.google.api.client.util.Preconditions.checkNotNull;

import com.google.adk.agents.LlmAgent;
import com.google.common.base.CharMatcher;
import com.google.common.base.Optional;

import io.reactivex.rxjava3.core.Maybe;
import jakarta.inject.Provider;

public class LlmAgentProvider implements Provider<LlmAgent> {

  public LlmAgentProvider(AgentConfig config) {
    this(AgentContext.build(config));
  }

  public LlmAgentProvider(AgentContext ctx) {
    this.ctx = checkNotNull(ctx, "ctx");
  }

  private final AgentContext ctx;

  @Override
  public LlmAgent get() {
    AgentConfig config = ctx.getConfig();
    LlmAgent.Builder builder = LlmAgent.builder();
    builder.name(config.getName());
    builder.description(config.getDescription());
    builder.model(config.getModel());
    builder.instruction(config.getInstruction());
    builder.includeContents(includeContents(config));
    if (config.getOutputKey().isPresent()) {
      builder.outputKey(config.getOutputKey().get());
    }
    if (config.getTransferToAgent().isPresent()) {
      String name = config.getTransferToAgent().get();
      builder.afterAgentCallback(cc -> {
        cc.eventActions().setTransferToAgent(name);
        return Maybe.empty();
      });

    }
    builder.tools(ctx.getTools());
    builder.subAgents(ctx.getSubAgents());
    return builder.build();
  }

  private LlmAgent.IncludeContents includeContents(AgentConfig config) {
    Optional<String> incl = config.getIncludeContents();
    if (incl.isPresent()) {
      String trimmed = CharMatcher.whitespace().trimFrom(incl.get());
      return valueOf(trimmed.toUpperCase());
    }
    return DEFAULT;
  }
}
