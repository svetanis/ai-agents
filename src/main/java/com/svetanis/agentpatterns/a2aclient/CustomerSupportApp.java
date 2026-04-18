package com.svetanis.agentpatterns.a2aclient;

import com.google.adk.agents.LlmAgent;
import com.google.adk.web.AdkWebServer;
import com.svetanis.agentpatterns.base.AgentConfigsProvider;

import jakarta.inject.Provider;

// mvn compile exec:java -Dexec.mainClass=com.svetanis.agents.a2aclient.CustomerSupportApp

// Inspired by 5-Day AI Agents Intensive Course with Google
// https://www.kaggle.com/code/kaggle5daysofai/day-5a-agent2agent-communication

public class CustomerSupportApp {

  public static void main(String[] agrs) {
    Provider<LlmAgent> root = new CustomerSupportAgent(new AgentConfigsProvider());
    AdkWebServer.start(root.get());
  }
}
