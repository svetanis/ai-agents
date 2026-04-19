package com.svetanis.multiagentpatterns.code;

import com.google.adk.agents.SequentialAgent;
import com.google.adk.web.AdkWebServer;
import com.svetanis.multiagentpatterns.base.AgentConfigsProvider;

import jakarta.inject.Provider;

// mvn compile exec:java -Dexec.mainClass=com.svetanis.agents.code.CodeWorkflowApp

public final class CodeWorkflowApp {

  public static void main(String[] agrs) {
    Provider<SequentialAgent> root = new CodeRootAgent(new AgentConfigsProvider());
    AdkWebServer.start(root.get());
  }
}
