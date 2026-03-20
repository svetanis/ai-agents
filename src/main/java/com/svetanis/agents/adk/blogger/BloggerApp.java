package com.svetanis.agents.adk.blogger;

import com.google.adk.agents.LlmAgent;
import com.google.adk.web.AdkWebServer;

// mvn compile exec:java -Dexec.mainClass=com.svetanis.agents.adk.blogger.BloggerApp

// PROMPT: Run the daily executive briefing on Tech, Health, and Finance

public class BloggerApp {

	public static void main(String[] agrs) {
		LlmAgent root = new BloggerRootAgent().get();
		AdkWebServer.start(root);
	}
}
