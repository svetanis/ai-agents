package com.svetanis.agents.traveler;

import static com.svetanis.agents.utils.Running.runAgent;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Double.parseDouble;
import static java.lang.System.getProperty;

import com.google.adk.web.AdkWebServer;
import com.svetanis.agents.base.AgentConfigsProvider;

/*
 * run command:
 * cd contrib/multi-agent-patterns
 * mvn compile exec:java -Dexec.mainClass=com.google.adk.agents.traveler.TravelerApp
 */
public class TravelerApp {

  private static final String PROMPT =
      """
            plan a long weekend (2-3 days) for a solo adult end of May with flexible dates
            departure: New York, JFK
            destination: Rome, Italy.
            travel dates: flexible dates end of May 2026 (e.g. May 29 - June 1, 2026)
            flight preferences: none
            activity preferences: history, museums, galleries, cathedrals, castles.
            dining preferences: good Italian food
            accommodation preferences: none, up to USD 200 per night
            total budget: up to USD 2K.
      """;

  public static void main(String[] args) throws Exception {
    boolean fromServer = parseBoolean(getProperty("run.adk.web.server", "true"));
    double requestPerMin = parseDouble(getProperty("adk.rate.limit.requests.per.minute", "5"));
    TravelerRootAgent root = new TravelerRootAgent(new AgentConfigsProvider());
    if (fromServer) {
      AdkWebServer.start(root.get());
    } else {
      runAgent(root.get(), PROMPT, requestPerMin);
    }
  }
}
