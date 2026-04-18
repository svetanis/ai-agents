package com.svetanis.agentpatterns.base;

import static com.google.api.client.util.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.copyOf;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import com.google.adk.agents.BaseAgent;
import com.google.adk.tools.BaseTool;
import com.google.common.collect.ImmutableList;

public final class AgentContext {

  private final AgentConf config;
  private final ImmutableList<BaseTool> tools;
  private final ImmutableList<BaseAgent> subAgents;

  public static AgentContext build(AgentConf config) {
    return builder().withConfig(config).build();
  }

  public static AgentContext build(AgentConf config, BaseTool base) {
    return builder().withConfig(config).withTools(base).build();
  }

  public static final Builder builder() {
    return new Builder();
  }

  private AgentContext(Builder builder) {
    this.config = builder.config;
    this.tools = copyOf(builder.tools);
    this.subAgents = copyOf(builder.subAgents);
  }

  public static class Builder {

    private AgentConf config;
    private List<BaseTool> tools = new ArrayList<>();
    private List<BaseAgent> subAgents = new ArrayList<>();

    public final Builder withConfig(AgentConf config) {
      this.config = config;
      return this;
    }

    public final Builder withTools(BaseTool... tools) {
      return withTools(asList(tools));
    }

    public final Builder withTools(List<BaseTool> tools) {
      this.tools = tools;
      return this;
    }

    public final Builder withSubAgents(List<BaseAgent> subAgents) {
      this.subAgents = subAgents;
      return this;
    }

    public AgentContext build() {
      return validate(new AgentContext(this));
    }

    private static AgentContext validate(AgentContext instance) {
      checkNotNull(instance.getConfig());
      return instance;
    }
  }

  public AgentConf getConfig() {
    return config;
  }

  public ImmutableList<BaseTool> getTools() {
    return tools;
  }

  public ImmutableList<BaseAgent> getSubAgents() {
    return subAgents;
  }
}
