package com.svetanis.agentpatterns.story;

import static com.google.api.client.util.Preconditions.checkNotNull;

import java.util.Map;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.LoopAgent;
import com.google.adk.agents.SequentialAgent;
import com.svetanis.agentpatterns.base.AgentConfig;
import com.svetanis.agentpatterns.base.AgentConfigsProvider;
import com.svetanis.agentpatterns.base.LlmAgentProvider;

import jakarta.inject.Provider;

public class StoryRootAgent implements Provider<SequentialAgent> {

  private static final String DESC = "Story writing and refinement system";
  private static final String SWA_KEY = "story.writer.agent";

  public StoryRootAgent(AgentConfigsProvider provider) {
    this.provider = checkNotNull(provider, "provider");
  }

  private final AgentConfigsProvider provider;

  @Override
  public SequentialAgent get() {
    Map<String, AgentConfig> configs = provider.get();
    LlmAgent writer = new LlmAgentProvider(configs.get(SWA_KEY)).get();
    LoopAgent refinementLoop = new RefinementLoop(configs).get();
    return SequentialAgent.builder() //
        .name("StoryRefinementSystem") //
        .description(DESC) //
        .subAgents(writer, refinementLoop) //
        .build();
  }
}
