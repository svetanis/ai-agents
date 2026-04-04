package com.svetanis.io;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.io.ByteSource;

public interface FileSystemService {

	ImmutableMap<File, ByteSource> write(File dir, Map<String, ByteSource> map) throws IOException;

}
