package com.svetanis.agents.adk.blogger;

import static com.google.common.collect.ImmutableList.copyOf;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Provider;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.ParallelAgent;
import com.google.adk.tools.GoogleSearchTool;
import com.google.common.collect.ImmutableList;
import com.svetanis.agents.adk.LlmAgentProvider;

public class ResearchTeam implements Provider<ParallelAgent> {

	private static final String DESC = "The ParallelAgent runs all its sub-agents simultaneously";

	private static final String TRA = "blogger/tech-researcher";
	private static final String HRA = "blogger/health-researcher";
	private static final String FRA = "blogger/finance-researcher";

	@Override
	public ParallelAgent get() {
		GoogleSearchTool gst = new GoogleSearchTool();
		List<LlmAgent> subAgents = subAgents(gst);
		return ParallelAgent.builder()//
				.name("ResearchTeam")//
				.description(DESC)//
				.subAgents(subAgents)//
				.build();
	}

	private ImmutableList<LlmAgent> subAgents(GoogleSearchTool gst) {
		List<LlmAgent> list = new ArrayList<>();
		list.add(new LlmAgentProvider(TRA, gst).get());
		list.add(new LlmAgentProvider(HRA, gst).get());
		list.add(new LlmAgentProvider(FRA, gst).get());
		return copyOf(list);
	}
}
