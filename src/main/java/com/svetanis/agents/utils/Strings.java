package com.svetanis.agents.utils;

import java.nio.charset.Charset;

import com.google.common.io.ByteSource;

public class Strings {

	public static ByteSource asByteSource(String s, Charset charset) {
		return ByteSource.wrap(s.getBytes(charset));
	}

}
