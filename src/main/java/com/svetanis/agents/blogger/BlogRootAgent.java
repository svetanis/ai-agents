package com.svetanis.agents.blogger;

import static com.google.api.client.util.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.SequentialAgent;
import com.google.adk.tools.AgentTool;
import com.google.adk.tools.BaseTool;
import com.google.common.collect.ImmutableList;
import com.svetanis.agents.base.AgentConfig;
import com.svetanis.agents.base.AgentContext;
import com.svetanis.agents.base.AppConfig;
import com.svetanis.agents.base.LlmAgentProvider;

import jakarta.inject.Provider;

public class BlogRootAgent implements Provider<LlmAgent> {

  private static final String DESC = """
      Blog Post creation pipeline from research to publication.
      """;

  private static final String BRA_KEY = "blogger.root";

  private static final String BAA_KEY = "blogger.aggregator";
  private static final String BWA_KEY = "blogger.writer";
  private static final String BEA_KEY = "blogger.editor";
  private static final String BFA_KEY = "blogger.formatter";

  public BlogRootAgent(AppConfig appConfig) {
    this.appConfig = checkNotNull(appConfig, "appConfig");
  }

  private final AppConfig appConfig;

  @Override
  public LlmAgent get() {
    AgentTool pipeline = AgentTool.create(blogPipeline());
    AgentContext ctx = agentCtx(BRA_KEY, pipeline);
    return new LlmAgentProvider(ctx).get();
  }

  public SequentialAgent blogPipeline() {
    List<BaseAgent> subAgents = subAgents();
    return SequentialAgent.builder() //
        .name("BlogPostPipeline") //
        .description(DESC) //
        .subAgents(subAgents) //
        .build();
  }

  private AgentContext agentCtx(String key, BaseTool... tools) {
    return AgentContext.builder()//
        .withConfig(agentConfig(key))//
        .withTools(tools)//
        .withPlugins(appConfig.getPlugins())//
        .build();
  }

  private ImmutableList<BaseAgent> subAgents() {
    List<BaseAgent> list = new ArrayList<>();
    // 1. parallel research team
    list.add(new ResearchTeam(appConfig).get());
    // 2. aggregator
    list.add(new LlmAgentProvider(agentCtx(BAA_KEY)).get());
    // 3. writer
    list.add(new LlmAgentProvider(agentCtx(BWA_KEY)).get());
    // 4. editor
    list.add(new LlmAgentProvider(agentCtx(BEA_KEY)).get());
    // 5. formatter
    list.add(new LlmAgentProvider(agentCtx(BFA_KEY)).get());
    return ImmutableList.copyOf(list);
  }

  private AgentConfig agentConfig(String key) {
    String agentKey = appConfig.getProperties().get(key).trim();
    return appConfig.getAgentConfigs().get(agentKey);
  }

}
