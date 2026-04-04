package com.svetanis.io;

import static com.svetanis.base.Files.canonical;
import static com.svetanis.base.Strings.asByteSource;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.ByteSource;
import com.svetanis.base.Files;
import com.svetanis.base.Strings;

public class FssTest {

	private static final String TARGET = "./target/readme";

	@Test
	void test() throws IOException {
		File dst = Files.canonical(new File(TARGET));
		FileSystemService fss = new DefaultFileSystemService();
		fss.write(dst, content());
		String path = Joiner.on("/").join(TARGET, "README-v1.md");
		File to = canonical(new File(path));
		ByteSource bytes = Strings.asByteSource("test", UTF_8);
		Files.write(bytes, to);
	}

	private ImmutableMap<String, ByteSource> content() {
		String key = "README.md";
		String s = "Awesome sauce";
		Map<String, ByteSource> map = new HashMap<>();
		map.put(key, asByteSource(s, UTF_8));
		return ImmutableMap.copyOf(map);
	}
}
