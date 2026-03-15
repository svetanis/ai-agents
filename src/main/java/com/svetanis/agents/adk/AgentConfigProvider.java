package com.svetanis.agents.adk;

import java.io.File;

import javax.inject.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class AgentConfigProvider implements Provider<AgentConfig> {

	private static final String SRC = "src/main/resources/%s.yaml";

	private final String fragment;

	public AgentConfigProvider(String fragment) {
		this.fragment = fragment;
	}

	@Override
	public AgentConfig get() {
		try {
			String path = String.format(SRC, fragment);
			File file = new File(path);
			return readYaml(file);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public static AgentConfig readYaml(final File file) throws Exception {
		final ObjectMapper mapper = new ObjectMapper(new YAMLFactory()); // jackson databind
		return mapper.readValue(file, AgentConfig.class);
	}

}
