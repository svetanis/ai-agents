package com.svetanis.agents.code;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.LoopAgent;
import com.google.adk.agents.SequentialAgent;
import com.google.adk.tools.AgentTool;
import com.google.adk.tools.BaseTool;
import com.google.common.collect.ImmutableList;
import com.svetanis.agents.base.AgentConfig;
import com.svetanis.agents.base.AgentContext;
import com.svetanis.agents.base.AppConfig;
import com.svetanis.agents.base.LlmAgentProvider;
import com.svetanis.agents.base.tools.CodeExecutionToolProvider;

import jakarta.inject.Provider;

public class CodeRootAgent implements Provider<LlmAgent> {

  private static final String CRA_KEY = "code.root";
  private static final String CCA_KEY = "code.critic";
  private static final String CBA_KEY = "code.bundler";
  private static final String CFA_KEY = "code.refactor";
  private static final String CGA_KEY = "code.generator";
  private static final String CTA_KEY = "code.converter";

  public CodeRootAgent(AppConfig appConfig) {
    this.appConfig = checkNotNull(appConfig, "appConfig");
  }

  private final AppConfig appConfig;

  @Override
  public LlmAgent get() {
    AgentContext ctx = AgentContext.builder() //
        .withConfig(agentConfig(CRA_KEY)) //
        .withTools(agentTools()) //
        .withPlugins(appConfig.getPlugins())//
        .build(); //
    return new LlmAgentProvider(ctx).get();
  }

  private ImmutableList<AgentTool> agentTools() {
    List<AgentTool> tools = new ArrayList<>();
    tools.add(AgentTool.create(generationWorkflow()));
    tools.add(AgentTool.create(conversionWorkflow()));
    tools.add(AgentTool.create(fullLoop()));
    return ImmutableList.copyOf(tools);
  }

  private SequentialAgent fullLoop() {
    SequentialAgent generator = generationWorkflow();
    SequentialAgent converter = conversionWorkflow();
    LlmAgent bundler = llmAgent(agentConfig(CBA_KEY));
    return SequentialAgent.builder() //
        .name("FullLoopWorkflow") //
        .description("Generates and converts code") //
        .subAgents(generator, converter, bundler) //
        .build();
  }

  private SequentialAgent conversionWorkflow() {
    AgentTool tool = new CodeExecutionToolProvider(appConfig).get();
    LlmAgent convert = llmAgent(agentConfig(CTA_KEY), tool);
    LlmAgent review = llmAgent(agentConfig(CCA_KEY), tool);
    LlmAgent refactor = llmAgent(agentConfig(CFA_KEY), tool);
    return SequentialAgent.builder() //
        .name("CodeConversionWorkflow") //
        .description("Converts code with review-refactor") //
        .subAgents(convert, review, refactor) //
        .build();
  }

  private SequentialAgent generationWorkflow() {
    AgentTool tool = new CodeExecutionToolProvider(appConfig).get();
    LlmAgent generator = llmAgent(agentConfig(CGA_KEY), tool);
    LoopAgent refiner = new CodeRefinementLoop(appConfig, tool).get();
    return SequentialAgent.builder() //
        .name("CodeGenerationWorkflow") //
        .description("Generates code with Refinement Loop") //
        .subAgents(generator, refiner) //
        .build();
  }

  private LlmAgent llmAgent(AgentConfig config, BaseTool... tools) {
    AgentContext ctx = AgentContext.builder()//
        .withConfig(config)//
        .withTools(tools)//
        .withPlugins(appConfig.getPlugins())//
        .build();
    return new LlmAgentProvider(ctx).get();
  }

  private AgentConfig agentConfig(String key) {
    String agentKey = appConfig.getProperties().get(key).trim();
    return appConfig.getAgentConfigs().get(agentKey);
  }
}
