package com.svetanis.io;

import static com.google.common.collect.ImmutableMap.copyOf;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static com.google.common.io.Files.asByteSink;
import static com.google.common.io.Files.createParentDirs;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;
import com.google.common.io.ByteSink;
import com.google.common.io.ByteSource;

public final class DefaultFileSystemService implements FileSystemService {

	@Override
	public ImmutableMap<File, ByteSource> write(File dir, Map<String, ByteSource> map) throws IOException {
		Map<File, ByteSource> files = newLinkedHashMap();
		for (Entry<String, ByteSource> entry : map.entrySet()) {
			String path = entry.getKey();
			File file = new File(dir, path);
			createParentDirs(file);
			ByteSource source = entry.getValue();
			ByteSink sink = asByteSink(file);
			source.copyTo(sink);
			files.put(file, source);
		}
		return copyOf(files);
	}
}
