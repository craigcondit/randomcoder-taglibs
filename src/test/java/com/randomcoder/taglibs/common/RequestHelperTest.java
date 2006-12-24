package com.randomcoder.taglibs.common;

import java.net.URL;
import java.util.*;

import junit.framework.TestCase;

import org.springframework.mock.web.MockHttpServletRequest;

public class RequestHelperTest extends TestCase
{
	MockHttpServletRequest request;

	@Override
	public void setUp() throws Exception
	{
		request = new MockHttpServletRequest();
		
	}

	@Override
	public void tearDown() throws Exception
	{
		request = null;
	}

	public void testParseParameters() throws Exception
	{
		String query = "?param1=test1&param2=test2&param3=test3&param3=test3again";
		
		Map<String, List<String>> params = RequestHelper.parseParameters(request, query);		
		assertNotNull(params);
		
		List<String> param1 = params.get("param1");
		assertNotNull(param1);
		assertEquals(1, param1.size());
		assertEquals("test1", param1.get(0));

		List<String> param2 = params.get("param2");
		assertNotNull(param2);
		assertEquals(1, param2.size());
		assertEquals("test2", param2.get(0));

		List<String> param3 = params.get("param3");
		assertNotNull(param3);
		assertEquals(2, param3.size());
		assertEquals("test3", param3.get(0));
		assertEquals("test3again", param3.get(1));
	}

	public void testParseParametersNullQuery() throws Exception
	{
		Map<String, List<String>> params = RequestHelper.parseParameters(request, null);		
		assertNotNull(params);
		assertEquals(0, params.keySet().size());
	}
	
	public void testAppendParameters() throws Exception
	{
		URL url = new URL("http://www.example.com/test?param1=test1&param2=test2");
		Map<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("param3", "test3");
		
		URL result = RequestHelper.appendParameters(request, url, additionalParams);		
		String ext = result.toExternalForm();
		assertTrue(ext.contains("param3=test3"));
	}

	public void testAppendParametersDuplicate() throws Exception
	{
		URL url = new URL("http://www.example.com/test?param1=test1&param2=test2orig");
		Map<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("param2", "test2updated");
		
		URL result = RequestHelper.appendParameters(request, url, additionalParams);
		String ext = result.toExternalForm();
		assertFalse(ext.contains("param2=testorig"));
		assertTrue(ext.contains("param2=test2updated"));
	}
	
	public void testSetParameters() throws Exception
	{
		Map<String, List<String>> params = new HashMap<String, List<String>>();
		
		List<String> param1 = new ArrayList<String>();
		param1.add("new1");
		params.put("param1", param1);

		List<String> param3 = new ArrayList<String>();
		param3.add("new3");
		params.put("param3", param3);
		
		URL url = new URL("http://www.example.com/test?param1=test1&param2=test2");
		
		URL result = RequestHelper.setParameters(request, url, params);
		String ext = result.toExternalForm();
		assertFalse(ext.contains("param1=test1"));
		assertFalse(ext.contains("param2"));
		assertTrue(ext.contains("param1=new1"));
		assertTrue(ext.contains("param3=new3"));
	}

	public void testGetCurrentUrl() throws Exception
	{
		request.setScheme("http");
		request.setServerName("www.example.com");
		request.setServerPort(80);
		request.setQueryString("?param1=test1&param2=test2");
		request.setContextPath("/test");
		request.setServletPath("/servlet");
		request.setPathInfo("/index.jsp");
		
		URL result = RequestHelper.getCurrentUrl(request);
		String ext = result.toExternalForm();
		assertEquals("http://www.example.com/test/servlet/index.jsp?param1=test1&param2=test2", ext);
	}

	public void testGetCurrentUrlHttps() throws Exception
	{
		request.setScheme("https");
		request.setServerName("www.example.com");
		request.setServerPort(443);
		request.setContextPath("");
		request.setServletPath("/index.jsp");
		
		URL result = RequestHelper.getCurrentUrl(request);
		String ext = result.toExternalForm();
		assertEquals("https://www.example.com/index.jsp", ext);
	}

	public void testGetCurrentUrlRandomPort() throws Exception
	{
		request.setScheme("http");
		request.setServerName("www.example.com");
		request.setServerPort(8080);
		request.setContextPath("");
		request.setServletPath("/index.jsp");
		
		URL result = RequestHelper.getCurrentUrl(request);
		String ext = result.toExternalForm();
		assertEquals("http://www.example.com:8080/index.jsp", ext);
	}

	public void testGetCurrentUrlNegativePort() throws Exception
	{
		request.setScheme("http");
		request.setServerName("www.example.com");
		request.setServerPort(-1);
		request.setContextPath("");
		request.setServletPath("/index.jsp");
		
		URL result = RequestHelper.getCurrentUrl(request);
		String ext = result.toExternalForm();
		assertEquals("http://www.example.com/index.jsp", ext);
	}

	public void testGetCurrentUrlMalformedQueryString() throws Exception
	{
		request.setScheme("http");
		request.setServerName("www.example.com");
		request.setServerPort(80);
		request.setContextPath("");
		request.setServletPath("/index.jsp");
		request.setQueryString("param1=test1");
		
		URL result = RequestHelper.getCurrentUrl(request);
		String ext = result.toExternalForm();
		assertEquals("http://www.example.com/index.jsp?param1=test1", ext);
	}

	public void testGetCurrentUrlForwardURI() throws Exception
	{
		request.setScheme("http");
		request.setServerName("www.example.com");
		request.setServerPort(80);
		request.setContextPath("");
		request.setServletPath("/index.jsp");
		request.setAttribute("javax.servlet.forward.request_uri", "/original/index.jsp");
		request.setAttribute("javax.servlet.forward.query_string", "?original1=test1");
		
		URL result = RequestHelper.getCurrentUrl(request);
		String ext = result.toExternalForm();
		assertEquals("http://www.example.com/original/index.jsp?original1=test1", ext);
	}
	
}
