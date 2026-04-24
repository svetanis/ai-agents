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
import com.svetanis.agents.base.AgentConfig;
import com.svetanis.agents.base.AgentContext;
import com.svetanis.agents.base.LlmAgentProvider;
import com.svetanis.agents.base.tools.MapsAgentToolProvider;
import com.svetanis.agents.base.tools.SearchAgentToolProvider;

import jakarta.inject.Provider;

public class TripSearchTeam implements Provider<ParallelAgent> {

  private static final String DESC = """
      The ParallelSearchTeam agent searches flights,
      accommodations, dining and activity options concurrently.
      """;

  private static final String TFA_KEY = "traveler.flight.agent";
  private static final String TAA_KEY = "traveler.accommodation.agent";
  private static final String TEA_KEY = "traveler.activity.agent";
  private static final String TDA_KEY = "traveler.dining.agent";

  public TripSearchTeam(Map<String, AgentConfig> configs) {
    this.configs = copyOf(configs);
  }

  private final ImmutableMap<String, AgentConfig> configs;

  @Override
  public ParallelAgent get() {
    AgentTool maps = new MapsAgentToolProvider(configs).get();
    AgentTool search = new SearchAgentToolProvider(configs).get();
    LlmAgent flights = llmAgent(TFA_KEY, asList(search));
    SequentialAgent experiences = experiences(asList(search, maps));
    LlmAgent accommodation = llmAgent(TAA_KEY, asList(maps));
    return ParallelAgent.builder() //
        .name("ParallelSearchTeam") //
        .description(DESC) //
        .subAgents(flights, experiences, accommodation) //
        .build();
  }

  private SequentialAgent experiences(List<BaseTool> tools) {
    LlmAgent activities = llmAgent(TEA_KEY, tools);
    LlmAgent dining = llmAgent(TDA_KEY, tools);
    return SequentialAgent.builder() //
        .name("Activities and Dining") //
        .subAgents(activities, dining) //
        .build();
  }

  private LlmAgent llmAgent(String key, List<BaseTool> tools) {
    AgentConfig config = configs.get(key);
    AgentContext ctx = AgentContext.builder() //
        .withConfig(config) //
        .withTools(tools) //
        .build(); //
    return new LlmAgentProvider(ctx).get();
  }
}
