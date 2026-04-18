package com.svetanis.agentpatterns.base.serialize;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public final class YamlSerializer extends AbstractSerializer {

  public YamlSerializer() {
    this(new YamlMapperProvider().get());
  }

  @Inject
  public YamlSerializer(ObjectMapper mapper) {
    this.mapper = mapper.copy();
  }

  // ObjectMapper is mutable, don't expose it via a getter
  private final ObjectMapper mapper;

  @Override
  public <T> T read(Reader reader, Class<T> type) throws IOException {
    checkNotNull(reader, "reader");
    checkNotNull(type, "type");
    return mapper.readValue(reader, type);
  }

  @Override
  public <T> T read(InputStream in, Class<T> type) throws IOException {
    checkNotNull(in, "in");
    checkNotNull(type, "type");
    return mapper.readValue(in, type);
  }

  public ObjectMapper getMapper() {
    return mapper.copy();
  }
}
