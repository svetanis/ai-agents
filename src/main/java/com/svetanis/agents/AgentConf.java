package com.svetanis.agents;

import static com.google.common.base.Optional.absent;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;

public final class AgentConf {

  @JsonProperty
  private String name;
  @JsonProperty
  private String model;
  @JsonProperty
  private String description;
  @JsonProperty
  private String instruction;
  private Optional<String> outputKey = absent();
  private Optional<String> includeContents = absent();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getInstruction() {
    return instruction;
  }

  public void setInstruction(String instruction) {
    this.instruction = instruction;
  }

  public Optional<String> getOutputKey() {
    return outputKey;
  }

  @JsonProperty
  public void setOutputKey(String outputKey) {
    setOutputKey(Optional.fromNullable(outputKey));
  }

  public void setOutputKey(Optional<String> outputKey) {
    this.outputKey = outputKey;
  }

  public Optional<String> getIncludeContents() {
    return includeContents;
  }

  @JsonProperty
  public void setIncludeContents(String outputKey) {
    setIncludeContents(Optional.fromNullable(outputKey));
  }

  public void setIncludeContents(Optional<String> includeContents) {
    this.includeContents = includeContents;
  }

}
