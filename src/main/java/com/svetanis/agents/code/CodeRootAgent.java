package com.svetanis.agents.code;

import static com.google.api.client.util.Preconditions.checkNotNull;

import java.util.Map;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.LoopAgent;
import com.google.adk.agents.ParallelAgent;
import com.google.adk.agents.SequentialAgent;
import com.google.adk.tools.AgentTool;
import com.svetanis.agents.base.AgentConfig;
import com.svetanis.agents.base.AgentConfigsProvider;
import com.svetanis.agents.base.AgentContext;
import com.svetanis.agents.base.LlmAgentProvider;
import com.svetanis.agents.base.tools.CodeExecutionToolProvider;

import jakarta.inject.Provider;

public class CodeRootAgent implements Provider<LlmAgent> {

  private static final String CRA_KEY = "code.root.agent";
  private static final String CGA_KEY = "code.generator.agent";
  private static final String CBA_KEY = "code.bundler.agent";

  public CodeRootAgent(AgentConfigsProvider provider) {
    this.provider = checkNotNull(provider, "provider");
  }

  private final AgentConfigsProvider provider;

  @Override
  public LlmAgent get() {
    Map<String, AgentConfig> configs = provider.get();
    AgentTool generator = AgentTool.create(codeGeneration(configs));
    AgentTool converter = AgentTool.create(codeConversion(configs));
    AgentTool fullLoop = AgentTool.create(fullLoop(configs));
    AgentContext ctx = AgentContext.builder()//
        .withConfig(configs.get(CRA_KEY))//
        .withTools(generator, converter, fullLoop)//
        .build();//
    return new LlmAgentProvider(ctx).get();
  }

  private SequentialAgent fullLoop(Map<String, AgentConfig> configs) {
    SequentialAgent generator = codeGeneration(configs);
    SequentialAgent converter = codeConversion(configs);
    return SequentialAgent.builder() //
        .name("FullLoopWorkflow") //
        .description("Generates code and translates it to multiple languages") //
        .subAgents(generator, converter) //
        .build();
  }

  private SequentialAgent codeConversion(Map<String, AgentConfig> configs) {
    ParallelAgent converters = new CodeConversionTeam(configs).get();
    LlmAgent bundler = new LlmAgentProvider(AgentContext.build(configs.get(CBA_KEY))).get();
    return SequentialAgent.builder() //
        .name("ConvertBundleWorkflow") //
        .description("Parallel code converter outputs aggregated by bundler ") //
        .subAgents(converters, bundler) //
        .build();
  }

  private SequentialAgent codeGeneration(Map<String, AgentConfig> configs) {
    AgentTool tool = new CodeExecutionToolProvider(configs).get();
    AgentContext ctx = AgentContext.build(configs.get(CGA_KEY), tool);
    LlmAgent generator = new LlmAgentProvider(ctx).get();
    LoopAgent refiner = new CodeRefinementLoop(tool, configs).get();
    return SequentialAgent.builder() //
        .name("GenerateRefineWorkflow") //
        .description("Generates code with Refinement Loop") //
        .subAgents(generator, refiner) //
        .build();
  }
}
