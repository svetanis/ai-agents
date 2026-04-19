package com.svetanis.agentpatterns.traveler;

import static com.google.api.client.util.Preconditions.checkNotNull;

import java.util.Map;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.ParallelAgent;
import com.google.adk.agents.SequentialAgent;
import com.google.common.collect.ImmutableMap;
import com.svetanis.agentpatterns.base.AgentConfig;
import com.svetanis.agentpatterns.base.LlmAgentProvider;

import jakarta.inject.Provider;

public class RootAgent implements Provider<SequentialAgent> {

  private static final String DESC = """
      Travel planning system
      with parallel search
      and itinerary builder.
      """;
  private static final String KEY = "traveler.itinerary.agent";

  public RootAgent(Provider<ImmutableMap<String, AgentConfig>> provider) {
    this.provider = checkNotNull(provider);
  }

  private final Provider<ImmutableMap<String, AgentConfig>> provider;

  @Override
  public SequentialAgent get() {
    Map<String, AgentConfig> configs = provider.get();
    ParallelAgent team = new SearchTeam(configs).get();
    LlmAgent itinerary = new LlmAgentProvider(configs.get(KEY)).get();
    return SequentialAgent.builder() //
        .name("TravelPlanner") //
        .description(DESC) //
        .subAgents(team, itinerary) //
        .build();
  }
}
