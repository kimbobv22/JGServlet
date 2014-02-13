package com.jg.util;

public abstract class JGBase64CoderDef{
	abstract public char[] encode(byte[] value_);
	abstract public byte[] decode(String value_);
}