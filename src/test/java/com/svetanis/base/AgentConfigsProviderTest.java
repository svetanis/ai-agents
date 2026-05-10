package com.svetanis.base;

import static com.google.adk.agents.LlmAgent.IncludeContents.DEFAULT;
import static com.google.adk.agents.LlmAgent.IncludeContents.valueOf;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.google.adk.agents.LlmAgent;
import com.google.common.base.CharMatcher;
import com.svetanis.agents.base.AgentConfig;
import com.svetanis.agents.base.AgentConfigsProvider;

// https://github.com/google/adk-java/blob/main/contrib/samples/configagent/core_generate_content_config_config/root_agent.yaml
public class AgentConfigsProviderTest {

  @Test
  public void test() throws IOException {
    AgentConfigsProvider provider = new AgentConfigsProvider();
    Map<String, AgentConfig> map = provider.get();
    for (String key : map.keySet()) {
      AgentConfig conf = map.get(key);
      // System.out.println(conf);
    }
    // GenerateContentConfig.builder().temperature(Double.valueOf(0.1).floatValue()).maxOutputTokens(2000).build();
    // AppConfig config = new AppConfigProvider().get();
    // for (String key : config.getProperties().keySet()) {
    // System.out.println(key + ":" + config.getProperties().get(key));
    // }
  }

  private LlmAgent.IncludeContents includeContents(AgentConfig config) {
    Optional<String> incl = config.getIncludeContents();
    if (incl.isPresent()) {
      String trimmed = CharMatcher.whitespace().trimFrom(incl.get()).toUpperCase();
      return valueOf(trimmed);
    }
    return DEFAULT;
  }

}
