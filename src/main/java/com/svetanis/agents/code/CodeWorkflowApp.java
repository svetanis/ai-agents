package com.svetanis.agents.code;

import com.google.adk.agents.SequentialAgent;
import com.google.adk.web.AdkWebServer;
import com.svetanis.agents.base.AgentConfigsProvider;

import jakarta.inject.Provider;

// mvn compile exec:java -Dexec.mainClass=com.svetanis.agents.code.CodeWorkflowApp

public final class CodeWorkflowApp {

  public static void main(String[] agrs) {
    AgentConfigsProvider configs = new AgentConfigsProvider();
    Provider<SequentialAgent> single = new CodeRootAgent(configs);
    Provider<SequentialAgent> refinement = new CodeRooWithRefinementAgent(configs);
    AdkWebServer.start(single.get(), refinement.get());
  }
}
