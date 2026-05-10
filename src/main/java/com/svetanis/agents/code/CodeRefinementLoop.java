package com.svetanis.agents.code;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.adk.agents.Callbacks;
import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.LoopAgent;
import com.google.adk.tools.AgentTool;
import com.google.adk.tools.BaseTool;
import com.google.adk.tools.ExitLoopTool;
import com.svetanis.agents.base.AgentConfig;
import com.svetanis.agents.base.AgentContext;
import com.svetanis.agents.base.AppConfig;
import com.svetanis.agents.base.LlmAgentProvider;

import io.reactivex.rxjava3.core.Maybe;
import jakarta.inject.Provider;

public class CodeRefinementLoop implements Provider<LoopAgent> {

  private static final int MAX_ITER = 3;
  private static final String CRA_KEY = "code.review";
  private static final String CFA_KEY = "code.refiner";
  private static final String GEN_CODE = "generated_code";
  public static final String FOP_KEY = "final_output";

  private static final String DESC = """
      Improves code performance based on review feedback or signals completion.
      """;

  public CodeRefinementLoop(AppConfig appConfig, AgentTool execTool) {
    this.appConfig = checkNotNull(appConfig, "appConfig");
    this.execTool = checkNotNull(execTool, "execTool");

  }

  private final AppConfig appConfig;
  private final AgentTool execTool;

  @Override
  public LoopAgent get() {
    LlmAgent review = llmAgent(CRA_KEY, execTool);
    LlmAgent refiner = llmAgent(CFA_KEY, execTool, ExitLoopTool.INSTANCE);
    return LoopAgent.builder() //
        .name("CodeRefinementLoop") //
        .description(DESC) //
        .subAgents(review, refiner) //
        .maxIterations(MAX_ITER) //
        // .afterAgentCallback(publishFinalFromState(GEN_CODE, FOP_KEY))//
        .build();
  }

  private LlmAgent llmAgent(String key, BaseTool... tools) {
    AgentContext ctx = AgentContext.builder() //
        .withConfig(agentConfig(key)) //
        .withTools(tools) //
        .withPlugins(appConfig.getPlugins())//
        .build();
    return new LlmAgentProvider(ctx).get();
  }

  private AgentConfig agentConfig(String key) {
    String agentKey = appConfig.getProperties().get(key).trim();
    return appConfig.getAgentConfigs().get(agentKey);
  }

  private Callbacks.AfterAgentCallback publishFinalFromState(String src, String target) {
    return callbackContext -> {
      Object stateValue = callbackContext.invocationContext().session().state().get(src);
      if (stateValue instanceof String refinedCode) {
        String trimmed = refinedCode.trim();
        if (!trimmed.isBlank()) {
          callbackContext.invocationContext().session().state().put(target, trimmed);
        }
      }
      return Maybe.empty();
    };
  }
}
