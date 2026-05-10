package com.svetanis.agents.base;

import static com.google.api.client.util.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableMap.copyOf;
import static com.google.common.collect.Maps.fromProperties;
import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;

import com.google.adk.plugins.PluginManager;
import com.google.common.collect.ImmutableMap;

import jakarta.inject.Provider;

public class AppConfigProvider implements Provider<AppConfig> {

  private static final String SRC = "application.properties";

  private static final Logger LOGGER = getLogger(AppConfigProvider.class);

  public AppConfigProvider(PluginManager plugins, Map<String, AgentConfig> agentConfigs) {
    this.plugins = checkNotNull(plugins, "plugins");
    this.agentConfigs = copyOf(agentConfigs);
  }

  private final PluginManager plugins;
  private final ImmutableMap<String, AgentConfig> agentConfigs;

  @Override
  public AppConfig get() {
    Properties props = loadProperties(SRC);
    AppConfig.Builder builder = AppConfig.builder();
    builder.withPlugins(plugins);
    builder.withAgentConfigs(agentConfigs);
    builder.withProperties(fromProperties(props));
    return builder.build();
  }

  private Properties loadProperties(String resource) {
    Properties props = new Properties();
    try (InputStream in = AppConfigProvider.class.getClassLoader().getResourceAsStream(resource)) {
      if (in == null) {
        LOGGER.info(format("%s not found on classpath", resource));
        return new Properties();
      }
      props.load(in);
      return props;
    } catch (Throwable e) {
      e.printStackTrace();
      throw new IllegalStateException(e);
    }
  }
}
