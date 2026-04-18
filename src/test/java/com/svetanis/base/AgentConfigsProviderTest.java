package com.svetanis.base;

import static com.google.adk.agents.LlmAgent.IncludeContents.DEFAULT;
import static com.google.adk.agents.LlmAgent.IncludeContents.valueOf;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.google.adk.agents.LlmAgent;
import com.google.common.base.CharMatcher;
import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.svetanis.agents.AgentConf;
import com.svetanis.agents.AgentConfigsProvider;

public class AgentConfigsProviderTest {

  @Test
  public void test() throws IOException {
    AgentConfigsProvider provider = new AgentConfigsProvider();
    Map<String, AgentConf> map = Maps.filterKeys(provider.get(), k -> k.startsWith("traveler"));
    for (String key : map.keySet()) {
      AgentConf config = map.get(key);
      LlmAgent.IncludeContents ic = includeContents(config);
      // System.out.println(key + ":" + config.getOutputKey().or("") + "->" + ic);
    }
  }

  private LlmAgent.IncludeContents includeContents(AgentConf config) {
    Optional<String> incl = config.getIncludeContents();
    if (incl.isPresent()) {
      String trimmed = CharMatcher.whitespace().trimFrom(incl.get()).toUpperCase();
      return valueOf(trimmed);
    }
    return DEFAULT;
  }

}
