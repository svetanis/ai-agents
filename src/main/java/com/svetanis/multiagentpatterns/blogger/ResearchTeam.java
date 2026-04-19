package com.svetanis.multiagentpatterns.blogger;

import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Lists.transform;
import static java.util.Arrays.asList;

import java.util.List;
import java.util.Map;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.ParallelAgent;
import com.google.adk.tools.AgentTool;
import com.google.adk.tools.BaseTool;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.svetanis.multiagentpatterns.base.AgentConfig;
import com.svetanis.multiagentpatterns.base.AgentContext;
import com.svetanis.multiagentpatterns.base.LlmAgentProvider;
import com.svetanis.multiagentpatterns.base.tools.SearchAgentToolProvider;

import jakarta.inject.Provider;

public class ResearchTeam implements Provider<ParallelAgent> {

  private static final String DESC = "The ParallelAgent runs all its sub-agents simultaneously";

  private static final String TRA_KEY = "blogger.tech.researcher";
  private static final String HRA_KEY = "blogger.health.researcher";
  private static final String FRA_KEY = "blogger.finance.researcher";

  public ResearchTeam(Map<String, AgentConfig> configs) {
    this.configs = ImmutableMap.copyOf(configs);
  }

  private final ImmutableMap<String, AgentConfig> configs;

  @Override
  public ParallelAgent get() {
    AgentTool sat = new SearchAgentToolProvider(configs).get();
    List<LlmAgent> subAgents = subAgents(sat);
    return ParallelAgent.builder() //
        .name("ResearchTeam") //
        .description(DESC) //
        .subAgents(subAgents) //
        .build();
  }

  private ImmutableList<LlmAgent> subAgents(AgentTool sat) {
    List<String> keys = asList(TRA_KEY, HRA_KEY, FRA_KEY);
    return copyOf(transform(keys, k -> llmAgent(k, sat)));
  }

  private LlmAgent llmAgent(String key, BaseTool tool) {
    AgentContext ctx = AgentContext.build(configs.get(key), tool);
    return new LlmAgentProvider(ctx).get();
  }
}
