package com.svetanis.multiagentpatterns.traveler;

import static com.google.api.client.util.Preconditions.checkNotNull;

import java.util.Map;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.ParallelAgent;
import com.google.adk.agents.SequentialAgent;
import com.svetanis.multiagentpatterns.base.AgentConfig;
import com.svetanis.multiagentpatterns.base.AgentConfigsProvider;
import com.svetanis.multiagentpatterns.base.LlmAgentProvider;

import jakarta.inject.Provider;

public class TravelerRootAgent implements Provider<SequentialAgent> {

  private static final String DESC = """
      Travel planning system
      with parallel search
      and itinerary builder.
      """;
  private static final String KEY = "traveler.itinerary.agent";

  public TravelerRootAgent(AgentConfigsProvider provider) {
    this.provider = checkNotNull(provider);
  }

  private final AgentConfigsProvider provider;

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
