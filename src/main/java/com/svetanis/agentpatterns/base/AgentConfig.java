package com.svetanis.agentpatterns.base;

import static com.google.api.client.util.Preconditions.checkNotNull;
import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.fromNullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Optional;

@JsonDeserialize(builder = AgentConfig.Builder.class)
public final class AgentConfig {

  private final String name;
  private final String model;
  private final String description;
  private final String instruction;
  private final Optional<String> outputKey;
  private final Optional<String> includeContents;

  private AgentConfig(Builder builder) {
    this.name = builder.name;
    this.model = builder.model;
    this.description = builder.description;
    this.instruction = builder.instruction;
    this.outputKey = builder.outputKey;
    this.includeContents = builder.includeContents;
  }

  public static class Builder {
    
    public Builder() {}

    private String name;
    private String model;
    private String description;
    private String instruction;
    private Optional<String> outputKey = absent();
    private Optional<String> includeContents = absent();

    public final Builder withName(String name) {
      this.name = name;
      return this;
    }

    public final Builder withModel(String model) {
      this.model = model;
      return this;
    }

    public final Builder withDescription(String description) {
      this.description = description;
      return this;
    }

    public final Builder withInstruction(String instruction) {
      this.instruction = instruction;
      return this;
    }

    public final Builder withOutputKey(Optional<String> outputKey) {
      this.outputKey = outputKey;
      return this;
    }

    public final Builder withIncludeContents(Optional<String> includeContents) {
      this.includeContents = includeContents;
      return this;
    }

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
      setOutputKey(fromNullable(outputKey));
    }

    public void setOutputKey(Optional<String> outputKey) {
      this.outputKey = outputKey;
    }

    public Optional<String> getIncludeContents() {
      return includeContents;
    }

    @JsonProperty
    public void setIncludeContents(String outputKey) {
      setIncludeContents(fromNullable(outputKey));
    }

    public void setIncludeContents(Optional<String> includeContents) {
      this.includeContents = includeContents;
    }

    public AgentConfig build() {
      return validate(new AgentConfig(this));
    }

    private static AgentConfig validate(AgentConfig instance) {
      checkNotNull(instance.name);
      checkNotNull(instance.model);
      checkNotNull(instance.description);
      checkNotNull(instance.instruction);
      return instance;
    }
  }

  public String getName() {
    return name;
  }

  public String getModel() {
    return model;
  }

  public String getDescription() {
    return description;
  }

  public String getInstruction() {
    return instruction;
  }

  public Optional<String> getOutputKey() {
    return outputKey;
  }

  public Optional<String> getIncludeContents() {
    return includeContents;
  }

  
  @Override
  public String toString() {
    ToStringHelper helper = toStringHelper(this);
    helper.add("name", name);
    helper.add("model", model);
    helper.add("description", description);
    helper.add("instruction", instruction);
    helper.add("outputKey", outputKey);
    helper.add("includeContents", includeContents);
    return helper.toString();
  }
}
