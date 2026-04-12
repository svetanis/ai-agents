package com.svetanis.agents.a2aclient;

import com.google.adk.agents.LlmAgent;
import com.google.adk.web.AdkWebServer;

// mvn compile exec:java -Dexec.mainClass=com.svetanis.agents.a2aclient.CustomerSupportApp

// Inspired by 5-Day AI Agents Intensive Course with Google
// https://www.kaggle.com/code/kaggle5daysofai/day-5a-agent2agent-communication

public class CustomerSupportApp {

  public static void main(String[] agrs) {
    LlmAgent root = new CustomerSupportAgent().get();
    AdkWebServer.start(root);
  }
}
