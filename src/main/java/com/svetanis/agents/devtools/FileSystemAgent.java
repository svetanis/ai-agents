package com.svetanis.agents.devtools;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.logging.Level.INFO;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.Annotations.Schema;
import com.google.adk.tools.FunctionTool;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.ByteSource;
import com.svetanis.agents.base.AgentConfig;
import com.svetanis.agents.base.AgentContext;
import com.svetanis.agents.base.LlmAgentProvider;
import com.svetanis.agents.utils.Files;
import com.svetanis.agents.utils.Strings;

import jakarta.inject.Provider;

public class FileSystemAgent implements Provider<LlmAgent> {

  private static final String TARGET = "./target/temp";
  private static final String DFA_KEY = "devtools.filesystem.agent";

  private static final Logger LOG = Logger.getLogger(FileSystemAgent.class.getName());

  public FileSystemAgent(Map<String, AgentConfig> configs) {
    this.configs = ImmutableMap.copyOf(configs);
  }

  private final ImmutableMap<String, AgentConfig> configs;

  @Override
  public LlmAgent get() {
    FunctionTool fst = FunctionTool.create(FileSystemAgent.class, "writeFile");
    AgentContext ctx = AgentContext//
        .builder()//
        .withConfig(configs.get(DFA_KEY))//
        .withTools(fst)//
        .build();
    return new LlmAgentProvider(ctx).get();
  }

  // TODO: build FileSystem MCP server
  @Schema(name = "write", description = "Writes a content to the destination file ")
  public static Map<String, String> writeFile(//
      @Schema(name = "destinationPath", description = "the destination path", optional = true) String destinationPath, //
      @Schema(name = "content", description = "content to be written to destination file") String content) {
    String target = Joiner.on("/").join(TARGET, "README.md");
    String path = Optional.fromNullable(destinationPath).or(target);
    try {
      File to = Files.canonical(new File(path));
      ByteSource bytes = Strings.asByteSource(content, UTF_8);
      Files.write(bytes, to);
      String msg = String.format("File saved to %s", path);
      LOG.log(INFO, () -> msg);
      return Map.of("status", "OK", "msg", msg);
    } catch (IOException e) {
      return Map.of("exception", e.getMessage());
    }
  }
}
