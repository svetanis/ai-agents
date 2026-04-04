package com.svetanis.base;

import static com.google.common.io.Files.createParentDirs;

import java.io.File;
import java.io.IOException;

import com.google.common.io.ByteSource;

public class Files {

	public static void write(ByteSource bytes, File to) throws IOException {
		createParentDirs(to);
		com.google.common.io.Files.write(bytes.read(), to);
	}

	public static File canonical(File file) {
		try {
			return file.getCanonicalFile();
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
