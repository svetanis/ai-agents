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
import com.svetanis.agents.base.LlmAgentProvider;
import com.svetanis.agents.base.tools.CodeExecutionToolProvider;

import jakarta.inject.Provider;

public class CodeTranslationTeam implements Provider<ParallelAgent> {

  private static final String SEQ_DESC = "Runs code translation pipeline with sanity check";
  private static final String PAR_DESC = "Runs code translation to multiple languages simultaneously";

  private static final String CPJ_KEY = "code.python.java";
  private static final String CPC_KEY = "code.python.cplus";
  private static final String CPG_KEY = "code.python.go";
  private static final String CPT_KEY = "code.python.typescript";

  private static final String CRA_KEY = "code.review.agent";
  private static final String CFA_KEY = "code.refactor.agent";

  public CodeTranslationTeam(Map<String, AgentConfig> configs) {
    this.configs = ImmutableMap.copyOf(configs);
  }

  private final ImmutableMap<String, AgentConfig> configs;

  @Override
  public ParallelAgent get() {
    AgentTool cet = new CodeExecutionToolProvider(configs).get();
    List<SequentialAgent> subAgents = subAgents(cet);
    return ParallelAgent.builder() //
        .name("CodeTranslationTeam") //
        .description(PAR_DESC) //
        .subAgents(subAgents) //
        .build();
  }

  private ImmutableList<SequentialAgent> subAgents(AgentTool tool) {
    List<String> keys = asList(CPJ_KEY); // , CPG_KEY, CPC_KEY, CPT_KEY);
    return copyOf(transform(keys, k -> flow(k, tool)));
  }

  // single review/refactor sanity check for translated code
  private SequentialAgent flow(String key, AgentTool tool) {
    LlmAgent translate = new LlmAgentProvider(AgentContext.build(configs.get(key), tool)).get();
    LlmAgent review = llmAgent(CRA_KEY, tool);
    LlmAgent refactor = llmAgent(CFA_KEY, tool);
    return SequentialAgent.builder()//
        .name("TranslationPipeline")//
        .description(SEQ_DESC)//
        .subAgents(translate, review, refactor)//
        .build();
  }

  public LlmAgent llmAgent(String key, AgentTool tool) {
    AgentConfig config = configs.get(key);
    String lang = substringAfterLast(key, ".").trim();
    String outputKey = Joiner.on("_").join(lang, "code");
    LlmAgent.Builder builder = LlmAgent.builder();
    builder.name(config.getName());
    builder.description(config.getDescription());
    builder.model(config.getModel());
    builder.instruction(config.getInstruction().replace("lang_code", outputKey));
    builder.outputKey(outputKey);
    builder.tools(tool);
    return builder.build();
  }
}
