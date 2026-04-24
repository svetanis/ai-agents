package com.svetanis.agents.code;

import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Lists.transform;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.substringAfterLast;

import java.util.List;
import java.util.Map;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.ParallelAgent;
import com.google.adk.agents.SequentialAgent;
import com.google.adk.tools.AgentTool;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.svetanis.agents.base.AgentConfig;
import com.svetanis.agents.base.AgentContext;
import com.svetanis.agents.base.DefaultContentConfigProvider;
import com.svetanis.agents.base.LlmAgentProvider;
import com.svetanis.agents.base.tools.CodeExecutionToolProvider;

import jakarta.inject.Provider;

public class CodeConversionTeam implements Provider<ParallelAgent> {

  private static final String SEQ_DESC = "Runs code translation pipeline with sanity check";
  private static final String PAR_DESC = "Runs code translation to multiple languages simultaneously";

  private static final String CTA_KEY = "code.converter.agent";
  private static final String CCA_KEY = "code.critic.agent";
  private static final String CFA_KEY = "code.refactor.agent";

  public CodeConversionTeam(Map<String, AgentConfig> configs) {
    this.configs = ImmutableMap.copyOf(configs);
  }

  private final ImmutableMap<String, AgentConfig> configs;

  @Override
  public ParallelAgent get() {
    List<SequentialAgent> subAgents = subAgents();
    return ParallelAgent.builder() //
        .name("CodeTranslationTeam") //
        .description(PAR_DESC) //
        .subAgents(subAgents) //
        .build();
  }

  private ImmutableList<SequentialAgent> subAgents() {
    List<String> keys = asList(CTA_KEY); // , CPG_KEY, CPT_KEY);
    return copyOf(transform(keys, k -> translationPipeline(k)));
  }

  // single review/refactor sanity check for translated code
  private SequentialAgent translationPipeline(String key) {
    // each pipeline has its own execution environment
    AgentTool tool = new CodeExecutionToolProvider(configs).get();
    LlmAgent convert = new LlmAgentProvider(AgentContext.build(configs.get(key), tool)).get();
    LlmAgent review = llmAgent(CCA_KEY, tool);
    LlmAgent refactor = llmAgent(CFA_KEY, tool);
    return SequentialAgent.builder()//
        .name("TranslationPipeline")//
        .description(SEQ_DESC)//
        .subAgents(convert, review, refactor)//
        .build();
  }

  public LlmAgent llmAgent(String key, AgentTool tool) {
    AgentConfig config = configs.get(key);
    String lang = substringAfterLast(key, ".").trim();
    String outputKey = Joiner.on("_").join(lang, "code");
    return LlmAgent.builder()//
        .name(config.getName())//
        .model(config.getModel())//
        .description(config.getDescription())//
        .instruction(config.getInstruction().replace("${target_language}", outputKey))//
        .outputKey(outputKey)//
        .tools(tool)//
        .generateContentConfig(new DefaultContentConfigProvider().get())//
        .build();
  }
}
