package com.svetanis.agents.traveler;

import static com.google.common.collect.ImmutableMap.copyOf;
import static java.util.Arrays.asList;

import java.util.List;
import java.util.Map;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.ParallelAgent;
import com.google.adk.agents.SequentialAgent;
import com.google.adk.tools.AgentTool;
import com.google.adk.tools.BaseTool;
import com.google.common.collect.ImmutableMap;
import com.svetanis.agents.AgentConf;
import com.svetanis.agents.AgentContext;
import com.svetanis.agents.LlmAgentProvider;
import com.svetanis.agents.tools.MapsAgentToolProvider;
import com.svetanis.agents.tools.SearchAgentToolProvider;

import jakarta.inject.Provider;

public class SearchTeam implements Provider<ParallelAgent> {

  private static final String DESC = """
      The ParallelSearchTeam agent searches flights,
      accomodations, dining and activity options concurrently.
      """;

  private static final String TFA_KEY = "traveler.flight.agent";
  private static final String THA_KEY = "traveler.accomodation.agent";
  private static final String TAA_KEY = "traveler.activity.agent";
  private static final String TDA_KEY = "traveler.dining.agent";

  public SearchTeam(Map<String, AgentConf> configs) {
    this.configs = copyOf(configs);
  }

  private final ImmutableMap<String, AgentConf> configs;

  @Override
  public ParallelAgent get() {
    AgentTool maps = new MapsAgentToolProvider(configs).get();
    AgentTool search = new SearchAgentToolProvider(configs).get();
    LlmAgent flights = llmAgent(TFA_KEY, asList(search));
    SequentialAgent experiences = experiences(asList(search, maps));
    LlmAgent accomodation = llmAgent(THA_KEY, asList(maps));
    return ParallelAgent.builder() //
        .name("ParallelSearchTeam") //
        .description(DESC) //
        .subAgents(flights, experiences, accomodation) //
        .build();
  }

  private SequentialAgent experiences(List<BaseTool> tools) {
    LlmAgent activities = llmAgent(TAA_KEY, tools);
    LlmAgent dining = llmAgent(TDA_KEY, tools);
    return SequentialAgent.builder()//
        .name("Activities and Dining")//
        .subAgents(activities, dining)//
        .build();
  }

  private LlmAgent llmAgent(String key, List<BaseTool> tools) {
    AgentConf config = configs.get(key);
    AgentContext ctx = AgentContext.builder()//
        .withConfig(config)//
        .withTools(tools)//
        .build();//
    return new LlmAgentProvider(ctx).get();
  }
}
