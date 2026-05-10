package com.svetanis.agents.blogger;

import static com.google.api.client.util.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Lists.transform;
import static java.util.Arrays.asList;

import java.util.List;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.ParallelAgent;
import com.google.adk.tools.AgentTool;
import com.google.adk.tools.BaseTool;
import com.google.common.collect.ImmutableList;
import com.svetanis.agents.base.AgentConfig;
import com.svetanis.agents.base.AgentContext;
import com.svetanis.agents.base.AppConfig;
import com.svetanis.agents.base.LlmAgentProvider;
import com.svetanis.agents.base.tools.SearchAgentToolProvider;

import jakarta.inject.Provider;

public class ResearchTeam implements Provider<ParallelAgent> {

  private static final String DESC = """
      The ParallelAgent runs all its sub-agents simultaneously"
      """;

  private static final String TRA_KEY = "blogger.tech";
  private static final String HRA_KEY = "blogger.health";
  private static final String FRA_KEY = "blogger.finance";

  public ResearchTeam(AppConfig appConfig) {
    this.appConfig = checkNotNull(appConfig, "appConfig");
  }

  private final AppConfig appConfig;

  @Override
  public ParallelAgent get() {
    AgentTool sat = new SearchAgentToolProvider(appConfig).get();
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
    AgentContext ctx = AgentContext.builder()//
        .withConfig(agentConfig(key))//
        .withPlugins(appConfig.getPlugins())//
        .withTools(tool)//
        .build();//
    return new LlmAgentProvider(ctx).get();
  }

  private AgentConfig agentConfig(String key) {
    String agentKey = appConfig.getProperties().get(key).trim();
    return appConfig.getAgentConfigs().get(agentKey);
  }

}
