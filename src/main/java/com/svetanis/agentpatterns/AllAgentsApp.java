package com.svetanis.agentpatterns;

import com.google.adk.agents.LlmAgent;
import com.google.adk.web.AdkWebServer;
import com.svetanis.agentpatterns.base.AgentConfigsProvider;
import com.svetanis.agentpatterns.tutor.TutorAgent;

import jakarta.inject.Provider;

// mvn compile exec:java -Dexec.mainClass=com.svetanis.agentpatterns.AllAgentsApp

public class AllAgentsApp {

  public static void main(String[] agrs) {
    AgentConfigsProvider configs = new AgentConfigsProvider();
    Provider<LlmAgent> root = new TutorAgent(new AgentConfigsProvider());
    AdkWebServer.start(root.get());
  }
}
