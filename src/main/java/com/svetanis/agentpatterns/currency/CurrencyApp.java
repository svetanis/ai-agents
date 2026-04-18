package com.svetanis.agentpatterns.currency;

import com.google.adk.agents.LlmAgent;
import com.google.adk.web.AdkWebServer;
import com.svetanis.agentpatterns.base.AgentConfigsProvider;

import jakarta.inject.Provider;

// Inspired by 5-Day AI Agents Intensive Course with Google
// https://www.kaggle.com/code/kaggle5daysofai/day-2a-agent-tools

// mvn compile exec:java -Dexec.mainClass=com.svetanis.agents.currency.CurrencyApp

// Suggested PROMPTs: 
// I want to convert 500 US Dollars to Euros using my Platinum Credit Card. How much will I receive?
// Convert 1,250 USD to EUR using a Bank Transfer. Show me the precise calculation.

public class CurrencyApp {

  public static void main(String[] args) throws Exception {
    Provider<LlmAgent> agent = new RootAgent(new AgentConfigsProvider());
    // Run your agent with the ADK Dev UI
    AdkWebServer.start(agent.get());
  }
}
