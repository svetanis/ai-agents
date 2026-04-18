package com.svetanis.agents.currency;

import static com.google.api.client.util.Preconditions.checkNotNull;

import java.util.Map;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.AgentTool;
import com.google.adk.tools.BuiltInCodeExecutionTool;
import com.google.adk.tools.FunctionTool;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.svetanis.agents.AgentConf;
import com.svetanis.agents.AgentContext;
import com.svetanis.agents.LlmAgentProvider;

import jakarta.inject.Provider;

public class RootAgent implements Provider<LlmAgent> {

  private static final String CRA_KEY = "currency.root.agent";
  private static final String CCA_KEY = "currency.calculator.agent";

  public RootAgent(Provider<ImmutableMap<String, AgentConf>> provider) {
    this.provider = checkNotNull(provider, "provider");
  }

  private final Provider<ImmutableMap<String, AgentConf>> provider;

  @Override
  public LlmAgent get() {
    AgentContext ctx = agentContext(provider.get());
    return new LlmAgentProvider(ctx).get();
  }

  private AgentContext agentContext(Map<String, AgentConf> configs) {
    FunctionTool pmf = FunctionTool.create(CurrencyTools.class, "paymentMethodFee");
    FunctionTool exr = FunctionTool.create(CurrencyTools.class, "exchangeRate");
    AgentTool cat = AgentTool.create(calculatorAgent(configs));
    AgentConf config = configs.get(CRA_KEY);
    return AgentContext//
        .builder()//
        .withConfig(config)//
        .withTools(ImmutableList.of(pmf, exr, cat))//
        .build();
  }

  private LlmAgent calculatorAgent(Map<String, AgentConf> configs) {
    AgentConf config = configs.get(CCA_KEY);
    AgentContext ctx = AgentContext//
        .builder()//
        .withConfig(config)//
        .withTools(ImmutableList.of(new BuiltInCodeExecutionTool()))//
        .build();
    return new LlmAgentProvider(ctx).get();
  }
}