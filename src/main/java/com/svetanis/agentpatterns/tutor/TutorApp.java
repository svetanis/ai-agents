package com.svetanis.agentpatterns.tutor;

import com.google.adk.agents.LlmAgent;
import com.google.adk.web.AdkWebServer;
import com.svetanis.agentpatterns.base.AgentConfigsProvider;

import jakarta.inject.Provider;

// mvn compile exec:java -Dexec.mainClass=com.svetanis.agents.tutor.TutorApp

public class TutorApp {

  public static void main(String[] agrs) {
    Provider<LlmAgent> root = new TutorAgent(new AgentConfigsProvider());
    AdkWebServer.start(root.get());
  }
}
