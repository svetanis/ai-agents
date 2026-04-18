package com.svetanis.agentpatterns.base.serialize;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.io.ByteSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

public abstract class AbstractSerializer implements Serializer {

  @Override
  public <T> T read(String string, Class<T> type) {
    try {
      Reader reader = new StringReader(string);
      return read(reader, type);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public <T> T read(ByteSource source, Class<T> type) throws IOException {
    checkNotNull(source, "source");
    checkNotNull(type, "type");
    try (InputStream in = source.openBufferedStream()) {
      return read(in, type);
    } catch (Throwable e) {
      throw new IllegalStateException(e);
    }
  }
}
