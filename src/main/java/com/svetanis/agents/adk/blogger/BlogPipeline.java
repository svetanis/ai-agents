package com.svetanis.agents.adk.blogger;

import javax.inject.Provider;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.ParallelAgent;
import com.google.adk.agents.SequentialAgent;
import com.svetanis.agents.adk.LlmAgentProvider;

public class BlogPipeline implements Provider<SequentialAgent> {

	private static final String BAA = "blogger/aggregator-agent";
	private static final String BWA = "blogger/writer-agent";
	private static final String BEA = "blogger/editor-agent";

	@Override
	public SequentialAgent get() {
		ParallelAgent research = new ResearchTeam().get();
		LlmAgent aggregator = new LlmAgentProvider(BAA).get();
		LlmAgent writer = new LlmAgentProvider(BWA).get();
		LlmAgent editor = new LlmAgentProvider(BEA).get();
		return SequentialAgent.builder()//
				.name("BlogPipeline")//
				.subAgents(research, aggregator, writer, editor)//
				.build();
	}
}
