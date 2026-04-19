package com.svetanis.multiagentpatterns.story;

import static com.google.common.collect.ImmutableMap.copyOf;

import java.util.Map;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.LoopAgent;
import com.google.adk.tools.ExitLoopTool;
import com.google.common.collect.ImmutableMap;
import com.svetanis.multiagentpatterns.base.AgentConfig;
import com.svetanis.multiagentpatterns.base.AgentContext;
import com.svetanis.multiagentpatterns.base.LlmAgentProvider;

import jakarta.inject.Provider;

public class RefinementLoop implements Provider<LoopAgent> {

  private static final String SCA_KEY = "story.critic.agent";
  private static final String SRA_KEY = "story.refiner.agent";

  private static final String DESC = """
      Improves essay based on critique or signals completion.
      """;

  public RefinementLoop(Map<String, AgentConfig> configs) {
    this.configs = copyOf(configs);
  }

  private final ImmutableMap<String, AgentConfig> configs;

  @Override
  public LoopAgent get() {
    LlmAgent critic = new LlmAgentProvider(configs.get(SCA_KEY)).get();
    AgentConfig config = configs.get(SRA_KEY);
    AgentContext ctx = AgentContext.build(config, ExitLoopTool.INSTANCE);
    LlmAgent refiner = new LlmAgentProvider(ctx).get();
    return LoopAgent.builder().name("RefinementLoop") //
        .description(DESC) //
        .subAgents(critic, refiner) //
        .maxIterations(3) //
        .build();
  }
}
