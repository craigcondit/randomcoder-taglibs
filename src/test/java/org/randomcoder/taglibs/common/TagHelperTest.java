package org.randomcoder.taglibs.common;

import javax.servlet.jsp.*;

import junit.framework.TestCase;

@SuppressWarnings("javadoc")
public class TagHelperTest extends TestCase
{
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	public void testGetScope() throws Exception
	{
		assertEquals(PageContext.APPLICATION_SCOPE, TagHelper.getScope("application"));
		assertEquals(PageContext.SESSION_SCOPE, TagHelper.getScope("session"));
		assertEquals(PageContext.REQUEST_SCOPE, TagHelper.getScope("request"));
		assertEquals(PageContext.PAGE_SCOPE, TagHelper.getScope("page"));
		
		try
		{
			TagHelper.getScope(null);
			fail("JspException expected");
		}
		catch (JspException e)
		{
			// pass
		}
		try
		{
			TagHelper.getScope("invalid");
			fail("JspException expected");
		}
		catch (JspException e)
		{
			// pass
		}
	}

}
