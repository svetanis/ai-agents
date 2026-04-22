package com.svetanis.agents.base.serialize;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteSource;

import jakarta.inject.Inject;

public final class YamlSerializerSimple {

  public YamlSerializerSimple() {
    this(new YamlMapperProvider().get());
  }

  @Inject
  public YamlSerializerSimple(ObjectMapper mapper) {
    this.mapper = mapper.copy();
  }

  private final ObjectMapper mapper;

  public <T> T read(InputStream in, Class<T> type) throws IOException {
    checkNotNull(in, "in");
    checkNotNull(type, "type");
    return mapper.readValue(in, type);
  }

  public <T> T read(ByteSource source, Class<T> type) throws IOException {
    checkNotNull(source, "source");
    checkNotNull(type, "type");
    try (InputStream in = source.openBufferedStream()) {
      return read(in, type);
    } catch (Throwable e) {
      throw new IllegalStateException(e);
    }
  }

  public ObjectMapper getMapper() {
    return mapper.copy();
  }
}
