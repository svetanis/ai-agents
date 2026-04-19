package com.svetanis.multiagentpatterns.tutor;

import com.google.adk.agents.LlmAgent;
import com.google.adk.web.AdkWebServer;
import com.svetanis.multiagentpatterns.base.AgentConfigsProvider;

import jakarta.inject.Provider;

// mvn compile exec:java -Dexec.mainClass=com.svetanis.agents.tutor.TutorApp

public class TutorApp {

  public static void main(String[] agrs) {
    Provider<LlmAgent> root = new TutorRootAgent(new AgentConfigsProvider());
    AdkWebServer.start(root.get());
  }
}
