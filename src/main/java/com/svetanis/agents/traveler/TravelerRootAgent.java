package com.svetanis.agents.traveler;

import static com.google.api.client.util.Preconditions.checkNotNull;

import java.util.Map;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.ParallelAgent;
import com.google.adk.agents.SequentialAgent;
import com.google.adk.tools.AgentTool;
import com.svetanis.agents.base.AgentConfig;
import com.svetanis.agents.base.AgentConfigsProvider;
import com.svetanis.agents.base.AgentContext;
import com.svetanis.agents.base.LlmAgentProvider;

import jakarta.inject.Provider;

public class TravelerRootAgent implements Provider<LlmAgent> {

  private static final String TRA_KEY = "traveler.root.agent";
  private static final String TIA_KEY = "traveler.itinerary.agent";

  public TravelerRootAgent(AgentConfigsProvider provider) {
    this.provider = checkNotNull(provider);
  }

  private final AgentConfigsProvider provider;

  @Override
  public LlmAgent get() {
    Map<String, AgentConfig> configs = provider.get();
    AgentTool tool = AgentTool.create(tripAssistant(configs));
    AgentConfig rootConfig = configs.get(TRA_KEY);
    AgentContext rootCtx = AgentContext.build(rootConfig, tool);
    return new LlmAgentProvider(rootCtx).get();
  }

  private SequentialAgent tripAssistant(Map<String, AgentConfig> configs) {
    ParallelAgent team = new TripSearchTeam(configs).get();
    LlmAgent itinerary = new LlmAgentProvider(configs.get(TIA_KEY)).get();
    return SequentialAgent.builder() //
        .name("TripAssistantWorkflow") //
        //.description("Travel Planning Agent with parallel search") //
        .subAgents(team, itinerary) //
        .build();
  }
}
