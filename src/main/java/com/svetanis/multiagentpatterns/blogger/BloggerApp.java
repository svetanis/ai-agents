package com.svetanis.multiagentpatterns.blogger;

import com.google.adk.agents.LlmAgent;
import com.google.adk.web.AdkWebServer;
import com.svetanis.multiagentpatterns.base.AgentConfigsProvider;

import jakarta.inject.Provider;

// mvn compile exec:java -Dexec.mainClass=com.svetanis.multiagentpatterns.blogger.BloggerApp

// Run the daily executive briefing on Tech, Health, and Finance 
// and write a blog post about most recent trends 

public final class BloggerApp {

  public static void main(String[] agrs) {
    Provider<LlmAgent> root = new BlogRootAgent(new AgentConfigsProvider());
    AdkWebServer.start(root.get());
  }
}
