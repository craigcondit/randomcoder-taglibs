package com.randomcoder.taglibs.functions;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * EL functions for escaping.
 * 
 * <pre>
 * Copyright (c) 2006, Craig Condit. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *   * Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * </pre> 
 */
public final class EscapeElFunctions
{
	/**
	 * URL-encode a value.
	 * @param value value to encode
	 * @return URL-encoded value
	 */
	public static String urlencode(String value)
	{
		if (value == null)
			return value;
		try
		{
			return URLEncoder.encode(value, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			return null;
		}
	}

	/**
	 * Escape a value for use in an HTML attribute.
	 * @param value value to escape
	 * @return attribute-safe escaped value
	 */
	public static String attribute(String value)
	{
		if (value == null)
			return null;
		
		StringBuilder builder = new StringBuilder();
		for (char c : value.toCharArray())
		{
			switch (c)
			{
			case '&':
				builder.append("&amp;");
				break;
			case '<':
				builder.append("&lt;");
				break;
			case '>':
				builder.append("&gt;");
				break;
			case '"':
				builder.append("&#34;");
				break;
			case '\'':
				builder.append("&#39;");
				break;
			default:
				builder.append(c);
			}
		}
		return builder.toString();
	}

	/**
	 * Escapes a value for use in a javascript double-quoted string.
	 * @param value value to escape
	 * @return escaped value
	 */
	public static String jsDouble(String value)
	{
		if (value == null)
			return null;
		StringBuilder builder = new StringBuilder();
		for (char c : value.toCharArray())
		{
			switch (c)
			{
			case '\\':
				builder.append("\\\\");
				break;
			case '"':
				builder.append("\\\"");
				break;
			default:
				builder.append(c);
			}
		}
		return builder.toString();
	}

	/**
	 * Escapes a value for use in a javascript single-quoted string.
	 * @param value value to escape
	 * @return escaped value
	 */
	public static String jsSingle(String value)
	{
		if (value == null)
			return null;
		StringBuilder builder = new StringBuilder();
		for (char c : value.toCharArray())
		{
			switch (c)
			{
			case '\\':
				builder.append("\\\\");
				break;
			case '\'':
				builder.append("\\'");
				break;
			default:
				builder.append(c);
			}
		}
		return builder.toString();
	}

}
