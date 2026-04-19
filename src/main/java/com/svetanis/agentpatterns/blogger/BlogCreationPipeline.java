package com.svetanis.agentpatterns.blogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.SequentialAgent;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.svetanis.agentpatterns.base.AgentConfig;
import com.svetanis.agentpatterns.base.LlmAgentProvider;

import jakarta.inject.Provider;

public class BlogCreationPipeline implements Provider<SequentialAgent> {

  private static final String DESC = """
      Blog Post creation pipeline from research to publication.
      """;

  private static final String BAA_KEY = "blogger.aggregator.agent";
  private static final String BWA_KEY = "blogger.writer.agent";
  private static final String BEA_KEY = "blogger.editor.agent";
  private static final String BFA_KEY = "blogger.formatter.agent";

  public BlogCreationPipeline(Map<String, AgentConfig> configs) {
    this.configs = ImmutableMap.copyOf(configs);
  }

  private final ImmutableMap<String, AgentConfig> configs;

  @Override
  public SequentialAgent get() {
    List<BaseAgent> subAgents = subAgents();
    return SequentialAgent.builder() //
        .name("BlogPostPipeline") //
        .description(DESC) //
        .subAgents(subAgents) //
        .build();
  }

  private ImmutableList<BaseAgent> subAgents() {
    List<BaseAgent> list = new ArrayList<>();
    // 1. parallel research team
    list.add(new ResearchTeam(configs).get());
    // 2. aggregator
    list.add(new LlmAgentProvider(configs.get(BAA_KEY)).get());
    // 3. writer
    list.add(new LlmAgentProvider(configs.get(BWA_KEY)).get());
    // 4. editor
    list.add(new LlmAgentProvider(configs.get(BEA_KEY)).get());
    // 5. formatter
    list.add(new LlmAgentProvider(configs.get(BFA_KEY)).get());
    return ImmutableList.copyOf(list);
  }
}
