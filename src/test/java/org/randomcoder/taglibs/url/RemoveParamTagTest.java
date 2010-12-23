package org.randomcoder.taglibs.url;

import java.io.StringWriter;

import javax.servlet.jsp.*;

import junit.framework.TestCase;

import org.springframework.mock.web.*;

import org.randomcoder.taglibs.test.mock.jee.JspWriterMock;

public class RemoveParamTagTest extends TestCase
{
	private RemoveParamTag tag;
	private ModifyTag parent;
	private MockPageContext context;
	private MockHttpServletRequest request;
	private StringWriter writer;

	@Override
	protected void setUp() throws Exception
	{
		tag = new RemoveParamTag();
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
		request.setQueryString("?param1=test1&param2=test2");
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
		context = null;
		request = null;
		writer.close();
		writer = null;
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
		tag.doStartTag();
		assertEquals(2, parent.getParams().size());
		tag.doEndTag();
		assertEquals(1, parent.getParams().size());
	}
	
	public void testDoEndTagMissingParam() throws Exception
	{
		parent.doStartTag();
		tag.setName("param3");
		tag.doStartTag();
		assertEquals(2, parent.getParams().size());
		tag.doEndTag();
		assertEquals(2, parent.getParams().size());
	}
	
}
