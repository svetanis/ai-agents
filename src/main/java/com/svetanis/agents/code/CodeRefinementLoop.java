package com.svetanis.agents.code;

import static com.google.api.client.util.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableMap.copyOf;

import java.util.Map;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.LoopAgent;
import com.google.adk.tools.AgentTool;
import com.google.adk.tools.ExitLoopTool;
import com.google.common.collect.ImmutableMap;
import com.svetanis.agents.base.AgentConfig;
import com.svetanis.agents.base.AgentContext;
import com.svetanis.agents.base.LlmAgentProvider;

import jakarta.inject.Provider;

public class CodeRefinementLoop implements Provider<LoopAgent> {

  private static final String CRA_KEY = "code.review.agent";
  private static final String CFA_KEY = "code.refiner.agent";

  private static final String DESC = """
      Improves code performance based on review feedback or signals completion.
      """;

  public CodeRefinementLoop(AgentTool executionTool, Map<String, AgentConfig> configs) {
    this.executionTool = checkNotNull(executionTool);
    this.configs = copyOf(configs);
  }

  private final AgentTool executionTool;
  private final ImmutableMap<String, AgentConfig> configs;

  @Override
  public LoopAgent get() {
    LlmAgent review = new LlmAgentProvider(configs.get(CRA_KEY)).get();
    LlmAgent refiner = new LlmAgentProvider(agentCtx()).get();
    return LoopAgent.builder()//
        .name("CodeRefinementLoop") //
        .description(DESC) //
        .subAgents(review, refiner) //
        .maxIterations(3) //
        .build();
  }

  private AgentContext agentCtx() {
    AgentConfig config = configs.get(CFA_KEY);
    return AgentContext.builder()//
        .withConfig(config)//
        .withTools(executionTool, ExitLoopTool.INSTANCE)//
        .build();

  }
}
