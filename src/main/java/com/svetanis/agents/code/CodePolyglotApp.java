package com.svetanis.agents.code;

import com.google.adk.web.AdkWebServer;
import com.svetanis.agents.base.AgentConfigsProvider;

// mvn compile exec:java -Dexec.mainClass=com.svetanis.agents.code.CodePolyglotApp

public final class CodePolyglotApp {

  public static void main(String[] agrs) {
    AgentConfigsProvider configs = new AgentConfigsProvider();
    CodeRootAgent root = new CodeRootAgent(configs);
    AdkWebServer.start(root.get());
  }
}
