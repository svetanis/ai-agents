package com.svetanis.agents.adk.blogger;

import javax.inject.Provider;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.LoopAgent;
import com.google.adk.tools.ExitLoopTool;
import com.svetanis.agents.adk.AgentContext;
import com.svetanis.agents.adk.LlmAgentProvider;

public class BlogRefinementLoop implements Provider<LoopAgent> {

	private static final String BCA = "blogger/critic-agent";
	private static final String BRA = "blogger/refiner-agent";

	private static final String DESC = """
			LoopAgent contains the agents that
			will run repeatedly:
			Critic -> Refiner
			""";

	@Override
	public LoopAgent get() {
		LlmAgent critic = new LlmAgentProvider(BCA).get();
		AgentContext ctx = AgentContext.build(BRA, ExitLoopTool.INSTANCE);
		LlmAgent refiner = new LlmAgentProvider(ctx).get();
		return LoopAgent.builder().name("BlogRefinementLoop")//
				.description(DESC)//
				.subAgents(critic, refiner) //
				.maxIterations(2).build(); // safety net to prevent infinite loops
	}
}
