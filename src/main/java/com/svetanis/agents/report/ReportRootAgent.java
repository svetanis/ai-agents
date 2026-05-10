package com.svetanis.agents.report;

import static com.google.api.client.util.Preconditions.checkNotNull;

import java.util.List;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.AgentTool;
import com.svetanis.agents.base.AgentConfig;
import com.svetanis.agents.base.AgentContext;
import com.svetanis.agents.base.AppConfig;
import com.svetanis.agents.base.LlmAgentProvider;
import com.svetanis.agents.base.tools.SearchAgentToolProvider;

import jakarta.inject.Provider;

public class ReportRootAgent implements Provider<LlmAgent> {

  private static final String RRW_KEY = "report.root";
  private static final String RTR_KEY = "report.topic";
  private static final String RCA_KEY = "report.content";
  private static final String RAA_KEY = "report.research";

  public ReportRootAgent(AppConfig appConfig) {
    this.appConfig = checkNotNull(appConfig, "appConfig");
  }

  private final AppConfig appConfig;
  
  @Override
  public LlmAgent get() {
    AgentTool assistant = AgentTool.create(researchAssistant());
    AgentContext ctx = AgentContext.builder()//
        .withConfig(agentConfig(RRW_KEY))//
        .withPlugins(appConfig.getPlugins())//
        .withTools(assistant)//
        .build();
    return new LlmAgentProvider(ctx).get();
  }

  private LlmAgent researchAssistant() {
    LlmAgent search = webSearch();
    LlmAgent analyst = summarizer();
    AgentConfig config = agentConfig(RAA_KEY);
    AgentContext ctx = AgentContext.builder() //
        .withConfig(config) //
        .withPlugins(appConfig.getPlugins())//
        .withSubAgents(List.of(search, analyst)) //
        .build();
    return new LlmAgentProvider(ctx).get();
  }

  private LlmAgent summarizer() {
    AgentContext ctx = AgentContext.builder()//
        .withConfig(agentConfig(RCA_KEY))//
        .withPlugins(appConfig.getPlugins())//
        .build();
    return new LlmAgentProvider(ctx).get();
  }

  private LlmAgent webSearch() {
    AgentTool search = new SearchAgentToolProvider(appConfig).get();
    AgentContext ctx = AgentContext.builder()//
        .withConfig(agentConfig(RTR_KEY))//
        .withTools(search)//
        .withPlugins(appConfig.getPlugins())//
        .build();
    return new LlmAgentProvider(ctx).get();
  }
  

  private AgentConfig agentConfig(String key) {
    String agentKey = appConfig.getProperties().get(key).trim();
    return appConfig.getAgentConfigs().get(agentKey);
  }

}
