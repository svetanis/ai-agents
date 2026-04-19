package com.svetanis.multiagentpatterns.currency;

import static com.google.api.client.util.Preconditions.checkNotNull;

import java.util.Map;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.AgentTool;
import com.google.adk.tools.BuiltInCodeExecutionTool;
import com.google.adk.tools.FunctionTool;
import com.google.common.collect.ImmutableList;
import com.svetanis.multiagentpatterns.base.AgentConfig;
import com.svetanis.multiagentpatterns.base.AgentConfigsProvider;
import com.svetanis.multiagentpatterns.base.AgentContext;
import com.svetanis.multiagentpatterns.base.LlmAgentProvider;

import jakarta.inject.Provider;

public class CurrencyRootAgent implements Provider<LlmAgent> {

  private static final String CRA_KEY = "currency.root.agent";
  private static final String CCA_KEY = "currency.calculator.agent";

  public CurrencyRootAgent(AgentConfigsProvider configs) {
    this.configs = checkNotNull(configs, "configs");
  }

  private final AgentConfigsProvider configs;

  @Override
  public LlmAgent get() {
    AgentContext ctx = agentContext(configs.get());
    return new LlmAgentProvider(ctx).get();
  }

  private AgentContext agentContext(Map<String, AgentConfig> configs) {
    FunctionTool pmf = FunctionTool.create(CurrencyTools.class, "paymentMethodFee");
    FunctionTool exr = FunctionTool.create(CurrencyTools.class, "exchangeRate");
    AgentTool cat = AgentTool.create(calculatorAgent(configs));
    AgentConfig config = configs.get(CRA_KEY);
    return AgentContext//
        .builder()//
        .withConfig(config)//
        .withTools(ImmutableList.of(pmf, exr, cat))//
        .build();
  }

  private LlmAgent calculatorAgent(Map<String, AgentConfig> configs) {
    AgentConfig config = configs.get(CCA_KEY);
    AgentContext ctx = AgentContext//
        .builder()//
        .withConfig(config)//
        .withTools(ImmutableList.of(new BuiltInCodeExecutionTool()))//
        .build();
    return new LlmAgentProvider(ctx).get();
  }
}