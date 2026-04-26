package com.svetanis.agents.utils;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.RunConfig;
import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.sessions.Session;
import com.google.adk.sessions.SessionKey;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import com.svetanis.agents.plugins.RateLimitPlugin;

import io.reactivex.rxjava3.core.Flowable;

public class Running {

  public static final String APP_NAME = "MyApp";
  private static final String USER_ID = "12345";
  private static final String SESSION_ID = "98765";
  public static final String MODEL_NAME = "gemini-2.5-flash";

  private static final Logger log = LoggerFactory.getLogger(Running.class);

  public static RateLimitPlugin rateLimitPlugin(double requestsPerMinute) {
    double requestsPerSecond = requestsPerMinute / 60.0;
    log.info(format("rate limit %s requests per second", requestsPerSecond));
    return new RateLimitPlugin(requestsPerSecond);
  }

  public static void runAgent(BaseAgent agent, String prompt, double requestsPerMinute) {
    RateLimitPlugin rlp = rateLimitPlugin(requestsPerMinute);
    InMemoryRunner runner = new InMemoryRunner(agent, APP_NAME, asList(rlp));
    // Explicitly create the session first
    SessionKey sessionKey =
        runner
            .sessionService()
            .createSession(runner.appName(), USER_ID, null, SESSION_ID)
            .blockingGet()
            .sessionKey();
    System.out.println("Session created: " + SESSION_ID + " for user: " + USER_ID);
    Content promptContent = Content.fromParts(Part.fromText(prompt));
    System.out.println("\nSending prompt: \"" + prompt + "\" to agent...\n");
    runner
        .runAsync(sessionKey, promptContent)
        .blockingForEach(
            event -> {
              System.out.println("Event received: " + event.toJson());
            });
  }

  // Run your agent from the command-line with your own run event loop
  public static void runAgent(BaseAgent root, double requestsPerMinute) {
    RunConfig runConfig = RunConfig.builder().build();
    RateLimitPlugin rlp = rateLimitPlugin(requestsPerMinute);
    InMemoryRunner runner = new InMemoryRunner(root, "trip-advisor", asList(rlp));
    Session session =
        runner.sessionService().createSession(runner.appName(), USER_ID).blockingGet();
    try (Scanner scanner = new Scanner(System.in, UTF_8)) {
      while (true) {
        System.out.print("\nYou > ");
        String userInput = scanner.nextLine();
        if ("quit".equalsIgnoreCase(userInput)) {
          break;
        }
        Content userMsg = Content.fromParts(Part.fromText(userInput));
        Flowable<Event> events =
            runner.runAsync(session.userId(), session.id(), userMsg, runConfig);
        System.out.print("\nAgent > ");
        events.blockingForEach(
            event -> {
              if (event.finalResponse()) {
                System.out.println(event.stringifyContent());
              }
            });
      }
    }
  }
}
