package com.svetanis.agents.code;

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
import com.svetanis.agents.base.tools.CodeExecutionToolProvider;

import jakarta.inject.Provider;

public class CodeRefinementLoop implements Provider<LoopAgent> {

  private static final String CAA_KEY = "code.analyse.agent";
  private static final String CFA_KEY = "code.refiner.agent";

  private static final String DESC = """
      Improves code performance based on review feedback or signals completion.
      """;

  public CodeRefinementLoop(Map<String, AgentConfig> configs) {
    this.configs = copyOf(configs);
  }

  private final ImmutableMap<String, AgentConfig> configs;

  @Override
  public LoopAgent get() {
    LlmAgent review = new LlmAgentProvider(configs.get(CAA_KEY)).get();
    LlmAgent refiner = new LlmAgentProvider(agentCtx()).get();
    return LoopAgent.builder().name("RefinementLoop") //
        .description(DESC) //
        .subAgents(review, refiner) //
        .maxIterations(3) //
        .build();
  }

  private AgentContext agentCtx() {
    AgentTool cet = new CodeExecutionToolProvider(configs).get();
    AgentConfig config = configs.get(CFA_KEY);
    return AgentContext.builder()//
        .withConfig(config)//
        .withTools(cet, ExitLoopTool.INSTANCE)//
        .build();

  }
}
