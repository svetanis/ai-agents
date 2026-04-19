package com.svetanis.agentpatterns.tutor;

import static com.google.api.client.util.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Lists.transform;
import static java.util.Arrays.asList;

import java.util.List;
import java.util.Map;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.AgentTool;
import com.google.adk.tools.BaseTool;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.svetanis.agentpatterns.base.AgentConfig;
import com.svetanis.agentpatterns.base.AgentContext;
import com.svetanis.agentpatterns.base.LlmAgentProvider;
import com.svetanis.agentpatterns.base.tools.SearchAgentToolProvider;

import jakarta.inject.Provider;

public class TutorAgent implements Provider<LlmAgent> {

  private static final String ROOT_KEY = "tutor.root.agent";
  private static final String CODE_KEY = "tutor.code.agent";
  private static final String MATH_KEY = "tutor.math.agent";
  private static final String SCNC_KEY = "tutor.science.agent";

  public TutorAgent(Provider<ImmutableMap<String, AgentConfig>> provider) {
    this.provider = checkNotNull(provider, "provider");
  }

  private final Provider<ImmutableMap<String, AgentConfig>> provider;

  @Override
  public LlmAgent get() {
    Map<String, AgentConfig> configs = provider.get();
    List<BaseTool> tools = tools(configs);
    AgentContext ctx = agentCtx(configs.get(ROOT_KEY), tools);
    return new LlmAgentProvider(ctx).get();
  }

  private ImmutableList<BaseTool> tools(Map<String, AgentConfig> configs) {
    List<String> keys = asList(CODE_KEY, MATH_KEY, SCNC_KEY);
    AgentTool sat = new SearchAgentToolProvider(configs).get();
    return copyOf(transform(keys, k -> agentTool(configs.get(k), sat)));
  }

  private AgentTool agentTool(AgentConfig config, BaseTool tool) {
    AgentContext ctx = agentCtx(config, asList(tool));
    LlmAgent agent = new LlmAgentProvider(ctx).get();
    return AgentTool.create(agent);
  }

  private AgentContext agentCtx(AgentConfig config, List<BaseTool> tools) {
    return AgentContext.builder() //
        .withConfig(config) //
        .withTools(tools) //
        .build(); //
  }
}
