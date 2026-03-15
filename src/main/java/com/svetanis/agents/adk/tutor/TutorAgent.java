package com.svetanis.agents.adk.tutor;

import javax.inject.Provider;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.AgentTool;
import com.svetanis.agents.adk.AgentConfig;
import com.svetanis.agents.adk.AgentConfigProvider;
import com.svetanis.agents.adk.SearchAgentProvider;

public class TutorAgent implements Provider<LlmAgent> {

	private static final String ROOT = "tutor/root_tutor_agent";
	private static final String CODE = "tutor/code_tutor_agent";
	private static final String MATH = "tutor/math_tutor_agent";
	private static final String SCIENCE = "tutor/science_tutor_agent";

	@Override
	public LlmAgent get() {
		AgentTool cat = AgentTool.create(new SearchAgentProvider(CODE).get());
		AgentTool mat = AgentTool.create(new SearchAgentProvider(MATH).get());
		AgentTool sat = AgentTool.create(new SearchAgentProvider(SCIENCE).get());
		AgentConfig config = new AgentConfigProvider(ROOT).get();
		return LlmAgent.builder().name(config.getName())//
				.description(config.getDescription())//
				.model(config.getModel())//
				.instruction(config.getInstruction())//
				.tools(cat, mat, sat)//
				.build();
	}
}
