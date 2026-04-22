package com.svetanis.agents.code;

import static com.google.api.client.util.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.ImmutableList.of;
import static com.google.common.collect.Lists.transform;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.ParallelAgent;
import com.google.adk.agents.SequentialAgent;
import com.google.adk.tools.AgentTool;
import com.google.common.collect.ImmutableList;
import com.svetanis.agents.base.AgentConfig;
import com.svetanis.agents.base.AgentConfigsProvider;
import com.svetanis.agents.base.AgentContext;
import com.svetanis.agents.base.LlmAgentProvider;
import com.svetanis.agents.base.tools.CodeExecutionToolProvider;

import jakarta.inject.Provider;

public class LegacyCodeRootAgent implements Provider<SequentialAgent> {

  private static final String DESC = "Code workflow pipeline";

  private static final String CPA_KEY = "code.python.agent";
  private static final String CRA_KEY = "code.review.agent";
  private static final String CFA_KEY = "code.refactor.agent";
  private static final String CBA_KEY = "code.bundler.agent";

  public LegacyCodeRootAgent(AgentConfigsProvider configs) {
    this.configs = checkNotNull(configs, "configs");
  }

  private final AgentConfigsProvider configs;

  @Override
  public SequentialAgent get() {
    List<BaseAgent> subAgents = subAgents(configs.get());
    return SequentialAgent.builder() //
        .name("CodePolyglot") //
        .description(DESC) //
        .subAgents(subAgents) //
        .build();
  }

  private ImmutableList<BaseAgent> subAgents(Map<String, AgentConfig> configs) {
    AgentTool tool = new CodeExecutionToolProvider(configs).get();
    ParallelAgent translationTeam = new CodeTranslationTeam(configs).get();
    LlmAgent bundler = new LlmAgentProvider(agentCtx(configs.get(CBA_KEY), of())).get();
    List<String> keys = asList(CPA_KEY, CRA_KEY, CFA_KEY);
    List<LlmAgent> llmAgents = transform(keys, k -> subAgent(configs.get(k), tool));
    List<BaseAgent> list = new ArrayList<>(llmAgents);
    list.add(translationTeam);
    list.add(bundler);
    return copyOf(list);
  }

  private LlmAgent subAgent(AgentConfig config, AgentTool tool) {
    AgentContext ctx = agentCtx(config, asList(tool));
    return new LlmAgentProvider(ctx).get();
  }

  private AgentContext agentCtx(AgentConfig config, List<AgentTool> tools) {
    return AgentContext.builder() //
        .withConfig(config) //
        .withTools(tools) //
        .build(); //
  }
}
