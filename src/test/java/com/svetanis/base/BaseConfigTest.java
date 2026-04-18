package com.svetanis.base;

import org.junit.jupiter.api.Test;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.ConfigAgentUtils;
import com.google.adk.agents.ConfigAgentUtils.ConfigurationException;

class BaseConfigTest {

  @Test
  void test() throws ConfigurationException {

    BaseAgent ba = ConfigAgentUtils.fromConfig("src/main/resources/agents/tool/search-agent.yaml");
    System.out.println(ba);
    
  }

}
