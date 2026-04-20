package com.svetanis.agents.code;

import static com.google.api.client.util.Preconditions.checkNotNull;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.LoopAgent;
import com.google.adk.agents.SequentialAgent;
import com.svetanis.agents.base.AgentConfigsProvider;
import com.svetanis.agents.base.LlmAgentProvider;

import jakarta.inject.Provider;

public class CodeRooWithRefinementAgent implements Provider<SequentialAgent> {

  private static final String DESC = "Code workflow pipeline with refinement loop";

  private static final String CWA_KEY = "code.write.agent";

  public CodeRooWithRefinementAgent(AgentConfigsProvider configs) {
    this.configs = checkNotNull(configs, "configs");
  }

  private final AgentConfigsProvider configs;

  @Override
  public SequentialAgent get() {
    LlmAgent writer = new LlmAgentProvider(configs.get().get(CWA_KEY)).get();
    LoopAgent refiner = new RefinementLoop(configs.get()).get();
    return SequentialAgent.builder() //
        .name("CodeWorkflow") //
        .description(DESC) //
        .subAgents(writer, refiner) //
        .build();
  }
}
