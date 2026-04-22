package com.svetanis.agents.code;

import static com.google.api.client.util.Preconditions.checkNotNull;

import java.util.Map;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.LoopAgent;
import com.google.adk.agents.ParallelAgent;
import com.google.adk.agents.SequentialAgent;
import com.svetanis.agents.base.AgentConfig;
import com.svetanis.agents.base.AgentConfigsProvider;
import com.svetanis.agents.base.AgentContext;
import com.svetanis.agents.base.LlmAgentProvider;

import jakarta.inject.Provider;

public class CodeRootAgent implements Provider<SequentialAgent> {

  private static final String DESC = "Code Polyglot System with refinement loop";

  private static final String CPA_KEY = "code.python.agent";
  private static final String CBA_KEY = "code.bundler.agent";

  public CodeRootAgent(AgentConfigsProvider provider) {
    this.provider = checkNotNull(provider, "provider");
  }

  private final AgentConfigsProvider provider;

  @Override
  public SequentialAgent get() {
    Map<String, AgentConfig> configs = provider.get();
    LlmAgent generator = new LlmAgentProvider(configs.get(CPA_KEY)).get();
    LoopAgent refiner = new CodeRefinementLoop(configs).get();
    ParallelAgent translators = new CodeTranslationTeam(configs).get();
    LlmAgent bundler = new LlmAgentProvider(AgentContext.build(configs.get(CBA_KEY))).get();
    return SequentialAgent.builder() //
        .name("CodePolyglotSystem") //
        .description(DESC) //
        .subAgents(generator, refiner, translators, bundler) //
        .build();
  }
}
