package com.svetanis.agentpatterns.story;

import com.google.adk.agents.SequentialAgent;
import com.google.adk.web.AdkWebServer;
import com.svetanis.agentpatterns.base.AgentConfigsProvider;

import jakarta.inject.Provider;

// mvn compile exec:java -Dexec.mainClass=com.svetanis.agents.story.StoryWriterApp

// Write a story about the importance of knowledge-seeking
// Write a story about the impact of choices on out future
// Write a story about finding beauty in ordinary moments
// Write a story about seeking new experiences and challenges
// Write a story about the role of imagination in shaping the future
// Write a story about recovering and staying strong through hardship

public class StoryWriterApp {

  public static void main(String[] agrs) {
    Provider<SequentialAgent> root = new RootAgent(new AgentConfigsProvider());
    AdkWebServer.start(root.get());
  }
}
