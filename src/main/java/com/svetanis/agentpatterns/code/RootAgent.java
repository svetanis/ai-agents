package com.svetanis.agentpatterns.code;

import static com.google.api.client.util.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Lists.transform;
import static java.util.Arrays.asList;

import java.util.List;
import java.util.Map;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.SequentialAgent;
import com.google.adk.tools.AgentTool;
import com.google.adk.tools.BaseTool;
import com.google.adk.tools.ExitLoopTool;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.svetanis.agentpatterns.base.AgentConfig;
import com.svetanis.agentpatterns.base.AgentContext;
import com.svetanis.agentpatterns.base.LlmAgentProvider;
import com.svetanis.agentpatterns.base.tools.CodeExecutionToolProvider;

import jakarta.inject.Provider;

public class RootAgent implements Provider<SequentialAgent> {

  private static final String DESC = "Code workflow pipeline";
  
  private static final String CWA_KEY = "code.write.agent";
  private static final String CRA_KEY = "code.review.agent";
  private static final String CFA_KEY = "code.refactor.agent";

  public RootAgent(Provider<ImmutableMap<String, AgentConfig>> provider) {
    this.provider = checkNotNull(provider, "provider");
  }

  private final Provider<ImmutableMap<String, AgentConfig>> provider;

  @Override
  public SequentialAgent get() {
    Map<String, AgentConfig> configs = provider.get();
    List<BaseAgent> subAgents = subAgents(configs);
    return SequentialAgent.builder() //
        .name("CodeWorkflow") //
        .description(DESC) //
        .subAgents(subAgents) //
        .build();
  }

  private ImmutableList<BaseAgent> subAgents(Map<String, AgentConfig> configs) {
    List<String> keys = asList(CWA_KEY, CRA_KEY, CFA_KEY);
    AgentTool tool = new CodeExecutionToolProvider(configs).get();
    return copyOf(transform(keys, k -> subAgent(configs.get(k), tool)));
  }

  private BaseAgent subAgent(AgentConfig config, AgentTool tool) {
    List<BaseTool> tools = asList(tool, ExitLoopTool.INSTANCE);
    AgentContext ctx = agentCtx(config, tools);
    return new LlmAgentProvider(ctx).get();
  }

  private AgentContext agentCtx(AgentConfig config, List<BaseTool> tools) {
    return AgentContext.builder() //
        .withConfig(config) //
        .withTools(tools) //
        .build(); //
  }
}
