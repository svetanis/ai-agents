package com.svetanis.agents.tutor;

import static com.google.api.client.util.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Lists.transform;
import static java.util.Arrays.asList;

import java.util.List;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.AgentTool;
import com.google.adk.tools.BaseTool;
import com.google.common.collect.ImmutableList;
import com.svetanis.agents.base.AgentConfig;
import com.svetanis.agents.base.AgentContext;
import com.svetanis.agents.base.AppConfig;
import com.svetanis.agents.base.LlmAgentProvider;
import com.svetanis.agents.base.tools.CodeExecutionToolProvider;
import com.svetanis.agents.base.tools.SearchAgentToolProvider;

import jakarta.inject.Provider;

public class TutorRootAgent implements Provider<LlmAgent> {

  private static final String ROOT_KEY = "tutor.root";
  private static final String CODE_KEY = "tutor.code";
  private static final String MATH_KEY = "tutor.math";
  private static final String SCNC_KEY = "tutor.science";

  public TutorRootAgent(AppConfig appConfig) {
    this.appConfig = checkNotNull(appConfig, "appConfig");
  }

  private final AppConfig appConfig;

  @Override
  public LlmAgent get() {
    List<BaseTool> tools = tools();
    AgentContext ctx = agentCtx(ROOT_KEY, tools);
    return new LlmAgentProvider(ctx).get();
  }

  private ImmutableList<BaseTool> tools() {
    List<String> keys = asList(CODE_KEY, MATH_KEY, SCNC_KEY);
    AgentTool sat = new SearchAgentToolProvider(appConfig).get();
    AgentTool cet = new CodeExecutionToolProvider(appConfig).get();
    return copyOf(transform(keys, k -> agentTool(k, asList(sat, cet))));
  }

  private AgentTool agentTool(String key, List<? extends BaseTool> tools) {
    AgentContext ctx = agentCtx(key, tools);
    LlmAgent agent = new LlmAgentProvider(ctx).get();
    return AgentTool.create(agent);
  }

  private AgentContext agentCtx(String key, List<? extends BaseTool> tools) {
    return AgentContext.builder() //
        .withConfig(agentConfig(key)) //
        .withTools(tools) //
        .withPlugins(appConfig.getPlugins())//
        .build(); //
  }

  private AgentConfig agentConfig(String key) {
    String agentKey = appConfig.getProperties().get(key).trim();
    return appConfig.getAgentConfigs().get(agentKey);
  }

}
