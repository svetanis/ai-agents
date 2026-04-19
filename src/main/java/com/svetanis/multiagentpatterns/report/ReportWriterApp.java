package com.svetanis.multiagentpatterns.report;

import com.google.adk.agents.LlmAgent;
import com.google.adk.web.AdkWebServer;
import com.svetanis.multiagentpatterns.base.AgentConfigsProvider;

import jakarta.inject.Provider;

// mvn compile exec:java -Dexec.mainClass=com.svetanis.multiagentpatterns.report.ReportWriterApp

// Write a report about artificial intelligence
// Create a report about self-driving vehicles

public final class ReportWriterApp {

  public static void main(String[] agrs) {
    Provider<LlmAgent> root = new ReportRootAgent(new AgentConfigsProvider());
    AdkWebServer.start(root.get());
  }
}
