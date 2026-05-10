package com.svetanis.agents.base.tools;

import static com.google.api.client.util.Preconditions.checkNotNull;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.AgentTool;
import com.google.adk.tools.GoogleSearchTool;
import com.svetanis.agents.base.AgentConfig;
import com.svetanis.agents.base.AgentContext;
import com.svetanis.agents.base.AppConfig;
import com.svetanis.agents.base.LlmAgentProvider;

import jakarta.inject.Provider;

public class SearchAgentToolProvider implements Provider<AgentTool> {

  private static final String KEY = "tool.search";

  public SearchAgentToolProvider(AppConfig appConfig) {
    this.appConfig = checkNotNull(appConfig, "appConfig");
  }

  private final AppConfig appConfig;

  @Override
  public AgentTool get() {
    GoogleSearchTool tool = new GoogleSearchTool();
    AgentConfig config = agentConfig(KEY);
    AgentContext ctx = AgentContext.builder()//
        .withConfig(config)//
        .withTools(tool)//
        .build();//
    LlmAgent agent = new LlmAgentProvider(ctx).get();
    return AgentTool.create(agent);
  }

  private AgentConfig agentConfig(String key) {
    String agentKey = appConfig.getProperties().get(key).trim();
    return appConfig.getAgentConfigs().get(agentKey);
  }
}
