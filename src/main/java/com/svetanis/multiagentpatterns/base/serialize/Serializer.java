package com.svetanis.multiagentpatterns.base.serialize;

import com.google.common.io.ByteSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public interface Serializer {

  /** Create an instance of {@code type} from a {@code java.lang.String} */
  <T> T read(String text, Class<T> type);

  /** Create an instance of {@code type} from a {@code Reader} */
  <T> T read(Reader in, Class<T> type) throws IOException;

  /** Create an instance of {@code type} from an {@code InputStream} */
  <T> T read(InputStream in, Class<T> type) throws IOException;

  /** Create an instance of {@code type} from a {@code ByteSource} */
  <T> T read(ByteSource source, Class<T> type) throws IOException;
}
