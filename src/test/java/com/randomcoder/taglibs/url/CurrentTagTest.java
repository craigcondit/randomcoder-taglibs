package com.randomcoder.taglibs.url;

import java.io.StringWriter;

import javax.servlet.jsp.*;

import junit.framework.TestCase;

import org.springframework.mock.web.*;

import com.randomcoder.taglibs.test.mock.jee.JspWriterMock;

public class CurrentTagTest extends TestCase
{
	private static final String EXPECTED_RESULT = "http://www.example.com/context/servlet/index.jsp?param1=test1";
	private CurrentTag tag;
	private MockPageContext context;
	private MockHttpServletRequest request;
	private StringWriter writer;

	@Override
	protected void setUp() throws Exception
	{
		tag = new CurrentTag();
		writer = new StringWriter();
		final JspWriterMock jspWriter = new JspWriterMock(writer);
		request = new MockHttpServletRequest();		
		request.setProtocol("http");
		request.setServerPort(80);
		request.setServerName("www.example.com");
		request.setContextPath("/context");
		request.setServletPath("/servlet");
		request.setPathInfo("/index.jsp");
		request.setQueryString("?param1=test1");
		context = new MockPageContext(new MockServletContext(), request) 
		{
			@Override
			public JspWriter getOut() { return jspWriter; }
		};
		tag.setPageContext(context);
	}

	@Override
	protected void tearDown() throws Exception
	{
		tag.release();
		tag = null;
		request = null;
		context = null;
		writer.close();
	}

	public void testDoEndTagNullVar() throws Exception
	{
		tag.doEndTag();
		assertEquals(EXPECTED_RESULT, writer.getBuffer().toString());
	}
	
	public void testDoEndTagScopeOnly() throws Exception
	{
		try
		{
			tag.setScope("page");
			tag.setVar(null);
			tag.doEndTag();
			fail("Expected JspException");
		}
		catch (JspException e)
		{
			// pass
		}
	}

	public void testDoEndTagVarOnly() throws Exception
	{
		tag.setVar("test");
		tag.doEndTag();
		assertEquals(EXPECTED_RESULT, context.getAttribute("test"));
	}

	public void testDoEndTagPageScope() throws Exception
	{
		tag.setVar("testPage");
		tag.setScope("page");
		tag.doEndTag();
		assertEquals(EXPECTED_RESULT, context.getAttribute("testPage", PageContext.PAGE_SCOPE));
	}

	public void testDoEndTagApplicationScope() throws Exception
	{
		tag.setVar("testApp");
		tag.setScope("application");
		tag.doEndTag();
		assertEquals(EXPECTED_RESULT, context.getAttribute("testApp", PageContext.APPLICATION_SCOPE));
	}

	public void testDoEndTagRequestScope() throws Exception
	{
		tag.setVar("testReq");
		tag.setScope("request");
		tag.doEndTag();
		assertEquals(EXPECTED_RESULT, context.getAttribute("testReq", PageContext.REQUEST_SCOPE));
	}

	public void testDoEndTagSessionScope() throws Exception
	{
		tag.setVar("testSession");
		tag.setScope("session");
		tag.doEndTag();
		assertEquals(EXPECTED_RESULT, context.getAttribute("testSession", PageContext.SESSION_SCOPE));
	}

	public void testDoEndTagInvalidScope() throws Exception
	{
		tag.setVar("testSession");
		tag.setScope("invalid");
		try
		{
			tag.doEndTag();
			fail("Expected JspException");
		}
		catch (JspException e)
		{
			// pass
		}
	}

}
