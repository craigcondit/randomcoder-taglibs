package org.randomcoder.taglibs.common;

import java.net.URL;

/**
 * Helper class containing URL-specific code.
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
public class UrlHelper
{

	private UrlHelper()
	{}

	/**
	 * Determines if two {@code URL} objects have the same host and protocol.
	 * 
	 * @param url1 first url
	 * @param url2 second url
	 * @return true if urls share same host and protocol, false otherwise
	 */
	public static boolean isUrlRelative(URL url1, URL url2)
	{
		if (url1 == null || url2 == null)
			return false;

		String protocol1 = url1.getProtocol();
		String protocol2 = url2.getProtocol();
		if (!protocol1.equals(protocol2))
			return false;

		int port1 = url1.getPort();
		int port2 = url2.getPort();

		if ("http".equals(protocol1))
		{
			if (port1 < 0)
				port1 = 80;
			if (port2 < 0)
				port2 = 80;
		}

		if ("https".equals(protocol1))
		{
			if (port1 < 0)
				port1 = 443;
			if (port2 < 0)
				port2 = 443;
		}

		if (port1 != port2)
			return false;

		String host1 = url1.getHost();
		String host2 = url2.getHost();
		if (!host1.equals(host2))
			return false;

		return true;
	}
}
