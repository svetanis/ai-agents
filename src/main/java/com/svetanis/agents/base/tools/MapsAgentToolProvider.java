package com.svetanis.agents.base.tools;

import static com.google.api.client.util.Preconditions.checkNotNull;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.AgentTool;
import com.google.adk.tools.GoogleMapsTool;
import com.svetanis.agents.base.AgentConfig;
import com.svetanis.agents.base.AgentContext;
import com.svetanis.agents.base.AppConfig;
import com.svetanis.agents.base.LlmAgentProvider;

import jakarta.inject.Provider;

public class MapsAgentToolProvider implements Provider<AgentTool> {

  private static final String KEY = "tool.maps";

  public MapsAgentToolProvider(AppConfig appConfig) {
    this.appConfig = checkNotNull(appConfig, "appConfig");
  }

  private final AppConfig appConfig;

  @Override
  public AgentTool get() {
    GoogleMapsTool tool = new GoogleMapsTool();
    AgentConfig config = agentConfig(KEY);
    AgentContext ctx = AgentContext.builder()//
        .withConfig(config)//
        .withTools(tool)//
        .withPlugins(appConfig.getPlugins())//
        .build();//
    LlmAgent agent = new LlmAgentProvider(ctx).get();
    return AgentTool.create(agent);
  }

  private AgentConfig agentConfig(String key) {
    String agentKey = appConfig.getProperties().get(key);
    return appConfig.getAgentConfigs().get(agentKey);
  }

}
