package com.svetanis.agents.devtools;

import com.google.adk.agents.SequentialAgent;
import com.google.adk.web.AdkWebServer;

// mvn compile exec:java -Dexec.mainClass=com.svetanis.agents.devtools.DevToolsApp

// Suggested PROMPTs: 
// Please review and improve README.md file for {repository}.

public class DevToolsApp {

	public static void main(String[] args) throws Exception {
		SequentialAgent agent = new PipelineAgent().get();
		// Run your agent with the ADK Dev UI
		AdkWebServer.start(agent);
	}
}
