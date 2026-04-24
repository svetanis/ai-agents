package com.svetanis.agents.report;

import com.google.adk.web.AdkWebServer;
import com.svetanis.agents.base.AgentConfigsProvider;

/*
 * Hierarchical decomposition pattern (aka the russian doll)
 * https://developers.googleblog.com/developers-guide-to-multi-agent-patterns-in-adk/
 * 
 * suggested prompts:
 * Write a report on artificial intelligence
 * Create a report on self-driving vehicles
 * 
 * run command:
 * mvn compile exec:java -Dexec.mainClass=com.svetanis.agents.report.ReportWriterApp
 */

public final class ReportWriterApp {

  public static void main(String[] agrs) {
    ReportRootAgent root = new ReportRootAgent(new AgentConfigsProvider());
    AdkWebServer.start(root.get());
  }
}
