package com.svetanis.agents.adk.blogger;

import static com.google.common.collect.ImmutableList.copyOf;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Provider;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.SequentialAgent;
import com.google.common.collect.ImmutableList;
import com.svetanis.agents.adk.LlmAgentProvider;

public class BlogPipeline implements Provider<SequentialAgent> {

	private static final String DESC = "Sequential agent runs the sub-agents in the order that they are listed";

	private static final String BAA = "blogger/aggregator-agent";
	private static final String BWA = "blogger/writer-agent";
	private static final String BEA = "blogger/editor-agent";
	private static final String BPA = "blogger/presenter-agent";

	private final boolean refine;

	public BlogPipeline(boolean refine) {
		this.refine = refine;
	}

	@Override
	public SequentialAgent get() {
		List<BaseAgent> subAgents = subAgents(refine);
		return SequentialAgent.builder()//
				.name("BlogPipeline")//
				.description(DESC)//
				.subAgents(subAgents)//
				.build();
	}

	private ImmutableList<BaseAgent> subAgents(boolean refine) {
		List<BaseAgent> list = new ArrayList<>();
		// 1. parallel research team
		list.add(new ResearchTeam().get());
		// 2. aggregator
		list.add(new LlmAgentProvider(BAA).get());
		// 3. writer
		list.add(new LlmAgentProvider(BWA).get());
		// 4. refiner
		list.add(refiner(refine));
		// 5. presenter
		list.add(new LlmAgentProvider(BPA).get());
		return copyOf(list);
	}

	private BaseAgent refiner(boolean refine) {
		if (refine) {
			return new BlogRefinementLoop().get();
		} else {
			return new LlmAgentProvider(BEA).get();
		}
	}
}
