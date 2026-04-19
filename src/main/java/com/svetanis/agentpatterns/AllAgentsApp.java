package com.svetanis.agentpatterns;

import com.google.adk.web.AdkWebServer;
import com.svetanis.agentpatterns.base.AgentConfigsProvider;
import com.svetanis.agentpatterns.blogger.BlogRootAgent;
import com.svetanis.agentpatterns.code.CodeRootAgent;
import com.svetanis.agentpatterns.currency.CurrencyRootAgent;
import com.svetanis.agentpatterns.story.StoryRootAgent;
import com.svetanis.agentpatterns.traveler.TravelerRootAgent;
import com.svetanis.agentpatterns.tutor.TutorRootAgent;

// mvn compile exec:java -Dexec.mainClass=com.svetanis.agentpatterns.AllAgentsApp

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
