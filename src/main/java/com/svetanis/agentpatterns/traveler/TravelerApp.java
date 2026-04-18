package com.svetanis.agentpatterns.traveler;

import com.google.adk.agents.SequentialAgent;
import com.google.adk.web.AdkWebServer;
import com.svetanis.agentpatterns.base.AgentConfigsProvider;

import jakarta.inject.Provider;

// mvn compile exec:java -Dexec.mainClass=com.svetanis.agents.traveler.TravelerApp

public class TravelerApp {

  public static void main(String[] args) throws Exception {
    Provider<SequentialAgent> root = new RootAgent(new AgentConfigsProvider());
    AdkWebServer.start(root.get());
  }
}
