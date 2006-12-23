package com.randomcoder.taglibs.common;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.*;

import org.junit.*;
import org.springframework.mock.web.MockHttpServletRequest;

public class RequestHelperTest
{
	MockHttpServletRequest request;

	@Before
	public void setUp() throws Exception
	{
		request = new MockHttpServletRequest();
		
	}

	@After
	public void tearDown() throws Exception
	{}

	@Test
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

	@Test
	public void testAppendParameters() throws Exception
	{
		URL url = new URL("http://www.example.com/test?param1=test1&param2=test2");
		Map<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("param3", "test3");
		
		URL result = RequestHelper.appendParameters(request, url, additionalParams);		
		String ext = result.toExternalForm();
		assertTrue(ext.contains("param3=test3"));
	}

	@Test
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
	
	@Test
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

	@Test
	public void testGetCurrentUrl() throws Exception
	{
		request.setScheme("http");
		request.setServerName("www.example.com");
		request.setServerPort(80);
		request.setQueryString("?param1=test1&param2=test2");
		request.setContextPath("/test");
		request.setServletPath("/index.jsp");
		
		URL result = RequestHelper.getCurrentUrl(request);
		String ext = result.toExternalForm();
		assertEquals("http://www.example.com/test/index.jsp?param1=test1&param2=test2", ext);
	}

}
