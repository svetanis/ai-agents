package com.svetanis.agents.adk.blogger;

import static java.lang.Boolean.parseBoolean;
import static java.lang.System.getProperty;

import com.google.adk.agents.LlmAgent;
import com.google.adk.web.AdkWebServer;

// mvn compile exec:java -Dexec.mainClass=com.svetanis.agents.adk.blogger.BloggerApp

// PROMPT: Run the daily executive briefing on Tech, Health, and Finance

public class BloggerApp {

	private static final String KEY = "blogger.refinement.loop";
	private static final boolean REFINE = parseBoolean(getProperty(KEY, "true"));

	public static void main(String[] agrs) {
		LlmAgent root = new BloggerRootAgent(REFINE).get();
		AdkWebServer.start(root);
	}
}
