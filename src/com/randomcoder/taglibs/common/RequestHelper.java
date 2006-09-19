package com.randomcoder.taglibs.common;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

/**
 * Helper class containing servlet request-specific code.
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
public class RequestHelper
{

	private static final String REQUEST_URI_ATTRIBUTE = "javax.servlet.forward.request_uri";
	private static final String QUERY_STRING_ATTRIBUTE = "javax.servlet.forward.query_string";

	private RequestHelper()
	{}

	/**
	 * Parses out parameters in a given query string.
	 * 
	 * @param request HTTP request
	 * @param query query string
	 * @return parameter map
	 * @throws UnsupportedEncodingException if request encoding is unknown
	 */
	public static Map<String, List<String>> parseParameters(HttpServletRequest request, String query) throws UnsupportedEncodingException
	{

		// get the request encoding
		String encoding = request.getCharacterEncoding();

		// not found, fallback to UTF-8
		if (encoding == null)
			encoding = "UTF-8";

		Map<String, List<String>> data = new HashMap<String, List<String>>();

		// query not specified
		if (query == null)
			return data;

		StringTokenizer st = new StringTokenizer(query, "?&=", true);

		String prev = null;
		while (st.hasMoreTokens())
		{
			String tk = st.nextToken();
			if ("?".equals(tk))
				continue;
			if ("&".equals(tk))
				continue;
			if ("=".equals(tk))
			{
				if (prev == null)
					continue; // no previous entry...
				if (!st.hasMoreTokens())
					continue; // no more data

				String key = URLDecoder.decode(prev, encoding);
				String value = URLDecoder.decode(st.nextToken(), encoding);

				List<String> params = data.get(key);
				if (params == null)
				{
					params = new ArrayList<String>();
					data.put(key, params);
				}
				params.add(value);

				prev = null;
			}
			else
			{
				// this is a key
				prev = tk;
			}
		}
		return data;
	}

	/**
	 * Generates a {@code URL} by appending parameters onto a base {@code URL}.
	 * 
	 * @param request HTTP request
	 * @param url base url
	 * @param additionalParams parameters to append
	 * @return URL with merged parameters
	 * @throws UnsupportedEncodingException if request encoding is unknown
	 * @throws MalformedURLException if base url is not valid
	 */
	public static URL appendParameters(HttpServletRequest request, URL url, Map<String, String> additionalParams) throws UnsupportedEncodingException,
			MalformedURLException
	{

		// get the request encoding
		String encoding = request.getCharacterEncoding();

		// not found, fallback to UTF-8
		if (encoding == null)
			encoding = "UTF-8";

		String queryString = url.getQuery();
		String path = url.getPath();

		Map<String, List<String>> params = RequestHelper.parseParameters(request, queryString);

		// merge new parameters
		for (Entry<String, String> entry : additionalParams.entrySet())
		{
			List<String> values = new ArrayList<String>(1);
			values.add(entry.getValue());
			params.put(entry.getKey(), values);
		}

		// convert to query string
		StringBuilder queryBuf = new StringBuilder();
		for (Entry<String, List<String>> entry : params.entrySet())
		{
			String key = entry.getKey();
			for (String value : entry.getValue())
			{
				if (queryBuf.length() > 0)
				{
					queryBuf.append("&");
				}
				queryBuf.append(URLEncoder.encode(key, encoding));
				queryBuf.append("=");
				queryBuf.append(URLEncoder.encode(value, encoding));
			}
		}

		queryString = queryBuf.toString();

		if (queryString.length() > 0)
			path += "?" + queryString;

		URL result = new URL(url.getProtocol(), url.getHost(), url.getPort(), path);

		return result;
	}

	/**
	 * Generates a {@code URL} by appending parameters onto a base {@code URL}.
	 * 
	 * @param request HTTP request
	 * @param url base url
	 * @param params parameters to set
	 * @return URL with merged parameters
	 * @throws UnsupportedEncodingException if request encoding is unknown
	 * @throws MalformedURLException if base url is not valid
	 */
	public static URL setParameters(HttpServletRequest request, URL url, Map<String, List<String>> params) throws UnsupportedEncodingException,
			MalformedURLException
	{

		// get the request encoding
		String encoding = request.getCharacterEncoding();

		// not found, fallback to UTF-8
		if (encoding == null)
			encoding = "UTF-8";

		String path = url.getPath();

		// convert to query string
		StringBuilder queryBuf = new StringBuilder();
		for (Entry<String, List<String>> entry : params.entrySet())
		{
			String key = entry.getKey();
			for (String value : entry.getValue())
			{
				if (queryBuf.length() > 0)
				{
					queryBuf.append("&");
				}
				queryBuf.append(URLEncoder.encode(key, encoding));
				queryBuf.append("=");
				queryBuf.append(URLEncoder.encode(value, encoding));
			}
		}

		String queryString = queryBuf.toString();

		if (queryString.length() > 0)
			path += "?" + queryString;

		URL result = new URL(url.getProtocol(), url.getHost(), url.getPort(), path);

		return result;
	}

	/**
	 * Gets the URL representing the current request.
	 * @param request HTTP request
	 * @return URL representing the current page
	 * @throws MalformedURLException if request URL is malformed
	 */
	public static URL getCurrentUrl(HttpServletRequest request) throws MalformedURLException
	{
		StringBuilder buf = new StringBuilder();

		String scheme = request.getScheme();
		int port = request.getServerPort();

		buf.append(scheme);
		buf.append("://");
		buf.append(request.getServerName());

		if ("http".equals(scheme) && port == 80)
		{
			// do nothing
		}
		else if ("https".equals(scheme) && port == 443)
		{
			// do nothing
		}
		else if (port < 0)
		{
			// do nothing
		}
		else
		{
			buf.append(":");
			DecimalFormat dfPort = new DecimalFormat("#####");
			buf.append(dfPort.format(port));
		}

		String forwardUri = (String) request.getAttribute(REQUEST_URI_ATTRIBUTE);
		if (forwardUri == null)
		{
			// use old style method
			String contextPath = request.getContextPath();
			if (contextPath != null)
				buf.append(contextPath);

			String servletPath = request.getServletPath();
			if (servletPath != null)
				buf.append(servletPath);

			String pathInfo = request.getPathInfo();
			if (pathInfo != null)
				buf.append(pathInfo);

			String queryString = request.getQueryString();
			if (queryString != null && queryString.length() > 0)
			{
				if (!('?' == queryString.charAt(0)))
					buf.append("?");
				buf.append(queryString);
			}
		}
		else
		{
			// use forwarded attributes
			buf.append(forwardUri);

			String queryString = (String) request.getAttribute(QUERY_STRING_ATTRIBUTE);
			if (queryString != null && queryString.length() > 0)
			{
				if (!('?' == queryString.charAt(0)))
					buf.append("?");
				buf.append(queryString);
			}
		}

		String name = buf.toString();
		return new URL(name);
	}

}
