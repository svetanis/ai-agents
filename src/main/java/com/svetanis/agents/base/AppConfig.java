package com.svetanis.agents.base;

import static com.google.api.client.util.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableMap.copyOf;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.adk.plugins.PluginManager;
import com.google.common.collect.ImmutableMap;

public final class AppConfig {

  private final Optional<PluginManager> plugins;
  private final ImmutableMap<String, String> properties;
  private final ImmutableMap<String, AgentConfig> agentConfigs;

  private AppConfig(Builder builder) {
    this.plugins = builder.plugins;
    this.properties = copyOf(builder.properties);
    this.agentConfigs = copyOf(builder.agentConfigs);
  }

  public static final Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private Optional<PluginManager> plugins = empty();
    private Map<String, String> properties = new HashMap<>();
    private Map<String, AgentConfig> agentConfigs = new HashMap<>();

    public final Builder withPlugins(PluginManager plugins) {
      return withPlugins(ofNullable(plugins));
    }

    public final Builder withPlugins(Optional<PluginManager> plugins) {
      this.plugins = plugins;
      return this;
    }

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
      checkNotNull(instance.plugins);
      checkNotNull(instance.properties);
      checkNotNull(instance.agentConfigs);
      return instance;
    }
  }

  public Optional<PluginManager> getPlugins() {
    return plugins;
  }

  public ImmutableMap<String, String> getProperties() {
    return properties;
  }

  public ImmutableMap<String, AgentConfig> getAgentConfigs() {
    return agentConfigs;
  }
}
