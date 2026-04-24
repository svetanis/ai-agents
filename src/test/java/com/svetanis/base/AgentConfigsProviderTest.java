package com.svetanis.base;

import static com.google.adk.agents.LlmAgent.IncludeContents.DEFAULT;
import static com.google.adk.agents.LlmAgent.IncludeContents.valueOf;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.google.adk.agents.LlmAgent;
import com.google.common.base.CharMatcher;
import com.google.common.base.Optional;
import com.svetanis.agents.base.AgentConfig;
import com.svetanis.agents.base.AgentConfigsProvider;
import com.svetanis.agents.base.AppConfig;
import com.svetanis.agents.base.AppConfigProvider;

// https://github.com/google/adk-java/blob/main/contrib/samples/configagent/core_generate_content_config_config/root_agent.yaml
public class AgentConfigsProviderTest {

  @Test
  public void test() throws IOException {

    AppConfig config = new AppConfigProvider().get();
    for (String key : config.getProperties().keySet()) {
      System.out.println(key + ":" + config.getProperties().get(key));
    }

    // GenerateContentConfig.builder().temperature(Double.valueOf(0.1).floatValue()).maxOutputTokens(2000).build();
    AgentConfigsProvider provider = new AgentConfigsProvider();
    Map<String, AgentConfig> map = provider.get();
    for (String key : map.keySet()) {
      AgentConfig conf = map.get(key);
      LlmAgent.IncludeContents ic = includeContents(conf);
      System.out.println(key + ":" + conf.getOutputKey().or("") + "->" + ic + "--" + conf.getTransferToAgent().or(""));
      System.out.println(conf);
    }
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
