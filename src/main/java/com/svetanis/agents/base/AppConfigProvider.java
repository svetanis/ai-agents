package com.svetanis.agents.base;

import static com.google.common.collect.Maps.fromProperties;
import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;

import jakarta.inject.Provider;

public class AppConfigProvider implements Provider<AppConfig> {

  private static final String SRC = "application.properties";

  private static final Logger LOGGER = getLogger(AppConfigProvider.class);

  @Override
  public AppConfig get() {
    Properties props = loadProperties(SRC);
    AppConfig.Builder builder = AppConfig.builder();
    builder.withProperties(fromProperties(props));
    builder.withAgentConfigs(new AgentConfigsProvider().get());
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
