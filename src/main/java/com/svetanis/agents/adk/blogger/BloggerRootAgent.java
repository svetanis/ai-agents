package com.svetanis.agents.adk.blogger;

import javax.inject.Provider;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.AgentTool;
import com.svetanis.agents.adk.AgentConfig;
import com.svetanis.agents.adk.AgentConfigProvider;
import com.svetanis.agents.adk.AgentContext;
import com.svetanis.agents.adk.LlmAgentProvider;

public class BloggerRootAgent implements Provider<LlmAgent> {

	private static final String BRA = "blogger/root-agent";

	private final boolean refine;

	public BloggerRootAgent(boolean refine) {
		this.refine = refine;
	}

	@Override
	public LlmAgent get() {
		AgentContext ctx = ctx(BRA);
		return new LlmAgentProvider(ctx).get();
	}

	private AgentContext ctx(String fragment) {
		AgentConfig config = new AgentConfigProvider(fragment).get();
		AgentTool pipeline = AgentTool.create(new BlogPipeline(refine).get());
		return AgentContext.builder()//
				.withConfig(config)//
				.withTools(pipeline)//
				.build();
	}
}
