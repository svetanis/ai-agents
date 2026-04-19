package com.svetanis.multiagentpatterns.report;

import com.google.adk.agents.LlmAgent;
import com.google.adk.web.AdkWebServer;
import com.svetanis.multiagentpatterns.base.AgentConfigsProvider;

import jakarta.inject.Provider;

/*
 * Hierarchical decomposition pattern (aka the russian doll)
 * https://developers.googleblog.com/developers-guide-to-multi-agent-patterns-in-adk/
 * 
 * suggested prompts:
 * Write a report on artificial intelligence
 * Create a report on self-driving vehicles
 * 
 * run command:
 * mvn compile exec:java -Dexec.mainClass=com.svetanis.multiagentpatterns.report.ReportWriterApp
 */

public final class ReportWriterApp {

  public static void main(String[] agrs) {
    Provider<LlmAgent> root = new ReportRootAgent(new AgentConfigsProvider());
    AdkWebServer.start(root.get());
  }
}
