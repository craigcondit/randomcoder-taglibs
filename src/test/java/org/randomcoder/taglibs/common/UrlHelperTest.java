package com.randomcoder.taglibs.common;

import java.net.URL;

import junit.framework.TestCase;

public class UrlHelperTest extends TestCase
{
	public void testIsUrlRelativeNull() throws Exception
	{
		assertFalse(UrlHelper.isUrlRelative(null, null));
	}

	public void testIsUrlRelativeDifferentProtocols() throws Exception
	{
		URL url1 = new URL("http://www.example.com/");
		URL url2 = new URL("https://www.example.com/");
		assertFalse(UrlHelper.isUrlRelative(url1, url2));
	}

	public void testIsUrlRelativeDifferentPorts() throws Exception
	{
		URL url1 = new URL("http://www.example.com/");
		URL url2 = new URL("http://www.example.com:8080/");
		assertFalse(UrlHelper.isUrlRelative(url1, url2));
	}

	public void testIsUrlRelativeDifferentPortsHttps() throws Exception
	{
		URL url1 = new URL("https://www.example.com/");
		URL url2 = new URL("https://www.example.com:8443/");
		assertFalse(UrlHelper.isUrlRelative(url1, url2));
	}

	public void testIsUrlRelativeDifferentHosts() throws Exception
	{
		URL url1 = new URL("http://www.example.com/");
		URL url2 = new URL("http://www2.example.com/");
		assertFalse(UrlHelper.isUrlRelative(url1, url2));
	}

	public void testIsUrlRelativeSame() throws Exception
	{
		URL url1 = new URL("http://www.example.com/test.jsp?test=2");
		URL url2 = new URL("http://www.example.com/index.jsp?test=1");
		assertTrue(UrlHelper.isUrlRelative(url1, url2));
	}
	
}
