package com.svetanis.agents.code;

import static com.google.api.client.util.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Lists.transform;
import static java.util.Arrays.asList;

import java.util.List;
import java.util.Map;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.SequentialAgent;
import com.google.adk.tools.AgentTool;
import com.google.adk.tools.BaseTool;
import com.google.common.collect.ImmutableList;
import com.svetanis.agents.base.AgentConfig;
import com.svetanis.agents.base.AgentConfigsProvider;
import com.svetanis.agents.base.AgentContext;
import com.svetanis.agents.base.LlmAgentProvider;
import com.svetanis.agents.base.tools.CodeExecutionToolProvider;

import jakarta.inject.Provider;

public class CodeRootAgent implements Provider<SequentialAgent> {

  private static final String DESC = "Code workflow pipeline";

  private static final String CWA_KEY = "code.write.agent";
  private static final String CRA_KEY = "code.review.agent";
  private static final String CFA_KEY = "code.refactor.agent";

  public CodeRootAgent(AgentConfigsProvider configs) {
    this.configs = checkNotNull(configs, "configs");
  }

  private final AgentConfigsProvider configs;

  @Override
  public SequentialAgent get() {
    List<? extends BaseAgent> subAgents = subAgents(configs.get());
    return SequentialAgent.builder() //
        .name("CodePipeline") //
        .description(DESC) //
        .subAgents(subAgents) //
        .build();
  }

  private ImmutableList<? extends BaseAgent> subAgents(Map<String, AgentConfig> configs) {
    List<String> keys = asList(CWA_KEY, CRA_KEY, CFA_KEY);
    AgentTool tool = new CodeExecutionToolProvider(configs).get();
    return copyOf(transform(keys, k -> subAgent(configs.get(k), tool)));
  }

  private LlmAgent subAgent(AgentConfig config, AgentTool tool) {
    AgentContext ctx = agentCtx(config, asList(tool));
    return new LlmAgentProvider(ctx).get();
  }

  private AgentContext agentCtx(AgentConfig config, List<? extends BaseTool> tools) {
    return AgentContext.builder() //
        .withConfig(config) //
        .withTools(tools) //
        .build(); //
  }
}
