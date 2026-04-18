package com.svetanis.agentpatterns.blogger;

import com.google.adk.agents.LlmAgent;
import com.google.adk.web.AdkWebServer;
import com.svetanis.agentpatterns.base.AgentConfigsProvider;

import jakarta.inject.Provider;

// mvn compile exec:java -Dexec.mainClass=com.svetanis.agents.blogger.BloggerApp

// Run the daily executive briefing on Tech, Health, and Finance and write a blog post
// Write a blog post about artificial intelligence
// Create a blog post about self-driving vehicles

public final class BloggerApp {

  public static void main(String[] agrs) {
    Provider<LlmAgent> root = new RootAgent(new AgentConfigsProvider());
    AdkWebServer.start(root.get());
  }
}
