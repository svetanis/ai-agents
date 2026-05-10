package com.svetanis.agents.base;

import static com.google.adk.agents.LlmAgent.IncludeContents.DEFAULT;
import static com.google.adk.agents.LlmAgent.IncludeContents.valueOf;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.time.Duration.ofSeconds;
import static java.util.Arrays.asList;

import java.util.Optional;

import com.google.adk.agents.LlmAgent;
import com.google.adk.plugins.PluginManager;
import com.google.common.base.CharMatcher;
import com.google.genai.types.GenerateContentConfig;
import com.svetanis.agents.plugins.RateLimitPlugin;
import com.svetanis.agents.plugins.RetryPlugin;

import io.reactivex.rxjava3.core.Maybe;
import jakarta.inject.Provider;

public class LlmAgentStaticProvider implements Provider<LlmAgent> {

  public LlmAgentStaticProvider(AgentConfig config) {
    this(AgentContext.build(config));
  }

  public LlmAgentStaticProvider(AgentContext ctx) {
    this.ctx = checkNotNull(ctx, "ctx");
  }

  private final AgentContext ctx;

  private static final PluginManager SHARED_PLUGINS = new PluginManager(asList(
      new RateLimitPlugin(5.0 / 60.0),
      new RetryPlugin(3, ofSeconds(2))
  ));

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
      builder.afterAgentCallback(
          cc -> {
            cc.eventActions().setTransferToAgent(name);
            return Maybe.empty();
          });
    }
    builder.tools(ctx.getTools());
    builder.subAgents(ctx.getSubAgents());
    builder.generateContentConfig(contentConfig(config.getContentConfig()));

    builder.beforeModelCallback((ctx1, req) -> SHARED_PLUGINS.beforeModelCallback(ctx1, req));
    builder.afterModelCallback((ctx1, res) -> SHARED_PLUGINS.afterModelCallback(ctx1, res));
    builder.onModelErrorCallback((ctx1, req, err) -> SHARED_PLUGINS.onModelErrorCallback(ctx1, req.toBuilder(), err));

    return builder.build();
  }

  private GenerateContentConfig contentConfig(Optional<ContentConfig> content) {
    GenerateContentConfig gcc = new DefaultContentConfigProvider().get();
    if (!content.isPresent()) {
      return gcc;
    }
    ContentConfig cc = content.get();
    return GenerateContentConfig.builder() //
        .temperature(cc.getTemperature().orElse(gcc.temperature().get())) //
        .maxOutputTokens(cc.getMaxOutputTokens().orElse(gcc.maxOutputTokens().get())) //
        .build();
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
