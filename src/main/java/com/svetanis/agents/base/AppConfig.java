package com.svetanis.agents.base;

import static com.google.api.client.util.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableMap.copyOf;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

public final class AppConfig {

  private final ImmutableMap<String, String> properties;
  private final ImmutableMap<String, AgentConfig> agentConfigs;

  private AppConfig(Builder builder) {
    this.properties = copyOf(builder.properties);
    this.agentConfigs = copyOf(builder.agentConfigs);
  }

  public static final Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private Map<String, String> properties = new HashMap<>();
    private Map<String, AgentConfig> agentConfigs = new HashMap<>();

    public final Builder withProperties(Map<String, String> properties) {
      this.properties = properties;
      return this;
    }

    public final Builder withAgentConfigs(Map<String, AgentConfig> agentConfigs) {
      this.agentConfigs = agentConfigs;
      return this;
    }

    public AppConfig build() {
      return validate(new AppConfig(this));
    }

    private static AppConfig validate(AppConfig instance) {
      checkNotNull(instance.properties);
      checkNotNull(instance.agentConfigs);
      return instance;
    }
  }

  public ImmutableMap<String, String> getProperties() {
    return properties;
  }

  public ImmutableMap<String, AgentConfig> getAgentConfigs() {
    return agentConfigs;
  }
}
