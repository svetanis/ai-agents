package com.svetanis.agents.traveler;

import com.google.adk.web.AdkWebServer;
import com.svetanis.agents.base.AgentConfigsProvider;

// run command: mvn compile exec:java -Dexec.mainClass=com.svetanis.agents.traveler.TravelerApp

public class TravelerApp {

  public static void main(String[] args) throws Exception {
    TravelerRootAgent root = new TravelerRootAgent(new AgentConfigsProvider());
    AdkWebServer.start(root.get());
  }
}
