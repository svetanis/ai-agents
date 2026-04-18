package com.svetanis.agentpatterns.story;

import static com.google.api.client.util.Preconditions.checkNotNull;

import java.util.Map;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.LoopAgent;
import com.google.adk.agents.SequentialAgent;
import com.google.common.collect.ImmutableMap;
import com.svetanis.agentpatterns.base.AgentConf;
import com.svetanis.agentpatterns.base.LlmAgentProvider;

import jakarta.inject.Provider;

public class RootAgent implements Provider<SequentialAgent> {

  private static final String DESC = "Story writing and refinement system";
  private static final String SWA_KEY = "story.writer.agent";

  public RootAgent(Provider<ImmutableMap<String, AgentConf>> provider) {
    this.provider = checkNotNull(provider, "provider");
  }

  private final Provider<ImmutableMap<String, AgentConf>> provider;

  @Override
  public SequentialAgent get() {
    Map<String, AgentConf> configs = provider.get();
    LlmAgent writer = new LlmAgentProvider(configs.get(SWA_KEY)).get();
    LoopAgent refinementLoop = new RefinementLoop(configs).get();
    return SequentialAgent.builder() //
        .name("StoryRefinementSystem") //
        .description(DESC) //
        .subAgents(writer, refinementLoop) //
        .build();
  }
}
