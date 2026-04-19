package com.svetanis.multiagentpatterns;

import com.google.adk.web.AdkWebServer;
import com.svetanis.multiagentpatterns.base.AgentConfigsProvider;
import com.svetanis.multiagentpatterns.blogger.BlogRootAgent;
import com.svetanis.multiagentpatterns.code.CodeRootAgent;
import com.svetanis.multiagentpatterns.currency.CurrencyRootAgent;
import com.svetanis.multiagentpatterns.story.StoryRootAgent;
import com.svetanis.multiagentpatterns.traveler.TravelerRootAgent;
import com.svetanis.multiagentpatterns.tutor.TutorRootAgent;

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
