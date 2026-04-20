package com.svetanis.agents.tutor;

import com.google.adk.web.AdkWebServer;
import com.svetanis.agents.base.AgentConfigsProvider;

// mvn compile exec:java -Dexec.mainClass=com.svetanis.agents.tutor.TutorApp

public class TutorApp {

  public static void main(String[] agrs) {
    TutorRootAgent root = new TutorRootAgent(new AgentConfigsProvider());
    AdkWebServer.start(root.get());
  }
}
