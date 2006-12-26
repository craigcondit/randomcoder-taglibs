package com.randomcoder.taglibs.url;

import java.io.StringWriter;
import java.util.*;

import javax.servlet.jsp.*;

import junit.framework.TestCase;

import org.springframework.mock.web.*;

import com.randomcoder.taglibs.test.mock.jee.JspWriterMock;

public class SetParamTagTest extends TestCase
{
	private SetParamTag tag;
	private ModifyTag parent;
	private MockPageContext context;
	private MockHttpServletRequest request;
	private StringWriter writer;

	@Override
	protected void setUp() throws Exception
	{
		tag = new SetParamTag();
		parent = new ModifyTag();		
		tag.setParent(parent);
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
		parent.setPageContext(context);
	}

	@Override
	protected void tearDown() throws Exception
	{		
		tag.release();
		tag = null;
		parent = null;
	}

	public void testDoEndTagNoParent() throws Exception
	{
		try
		{
			tag.setParent(null);
			tag.doEndTag();
			fail("JspException expected");
		}
		catch (JspException e)
		{
			// pass
		}
	}

	public void testDoEndTagExistingParam() throws Exception
	{
		parent.doStartTag();
		tag.setName("param1");
		tag.setValue("test2");
		tag.doStartTag();
		assertEquals(1, parent.getParams().size());
		tag.doEndTag();
		Map<String, List<String>> params = parent.getParams();
		assertEquals(1, params.size());
		List<String> values = params.get("param1");
		assertNotNull(values);
		assertEquals(1, values.size());
		assertEquals("test2", values.get(0));
	}
	
	public void testDoEndTagNewParam() throws Exception
	{
		parent.doStartTag();
		tag.setName("param2");
		tag.setValue("test2");
		tag.doStartTag();
		assertEquals(1, parent.getParams().size());
		tag.doEndTag();
		Map<String, List<String>> params = parent.getParams();
		assertEquals(2, params.size());
		List<String> values = params.get("param2");
		assertNotNull(values);
		assertEquals(1, values.size());
		assertEquals("test2", values.get(0));
	}
	
}
