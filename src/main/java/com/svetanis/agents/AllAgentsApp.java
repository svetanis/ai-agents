package com.svetanis.agents;

import com.google.adk.web.AdkWebServer;
import com.svetanis.agents.base.AgentConfigsProvider;
import com.svetanis.agents.blogger.BlogRootAgent;
import com.svetanis.agents.code.CodeRootAgent;
import com.svetanis.agents.currency.CurrencyRootAgent;
import com.svetanis.agents.story.StoryRootAgent;
import com.svetanis.agents.traveler.TravelerRootAgent;
import com.svetanis.agents.tutor.TutorRootAgent;

// mvn compile exec:java -Dexec.mainClass=com.svetanis.agents.AllAgentsApp

public class AllAgentsApp {

  public static void main(String[] agrs) {
    AgentConfigsProvider configs = new AgentConfigsProvider();
    AdkWebServer.start(//
        new TutorRootAgent(configs).get(), //
        new StoryRootAgent(configs).get(), //
        new CodeRootAgent(configs).get(), //
        new BlogRootAgent(configs).get(), //
        new CurrencyRootAgent(configs).get(), //
        new TravelerRootAgent(configs).get()//
    );
  }
}
