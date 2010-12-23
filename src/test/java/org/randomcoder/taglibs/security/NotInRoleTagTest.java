package org.randomcoder.taglibs.security;

import javax.servlet.jsp.tagext.Tag;

import junit.framework.TestCase;

import org.springframework.mock.web.*;

public class NotInRoleTagTest extends TestCase
{
	private NotInRoleTag tag;
	private MockHttpServletRequest request;
	
	@Override
	protected void setUp() throws Exception
	{
		tag = new NotInRoleTag();
		request = new MockHttpServletRequest();
		MockPageContext context = new MockPageContext(new MockServletContext(), request);
		tag.setPageContext(context);
	}

	@Override
	protected void tearDown() throws Exception
	{
		tag = null;
	}

	public void testDoStartTagRoleFound() throws Exception
	{
		request.addUserRole("test");
		tag.setRole("test");
		assertEquals(Tag.SKIP_BODY, tag.doStartTag());
	}

	public void testDoStartTagRoleFoundMultiple() throws Exception
	{
		request.addUserRole("test");
		tag.setRole("test,test2");
		assertEquals(Tag.SKIP_BODY, tag.doStartTag());
	}
	
	public void testDoStartTagRoleNotFound() throws Exception
	{
		request.addUserRole("test");
		tag.setRole("bogus");
		assertEquals(Tag.EVAL_BODY_INCLUDE, tag.doStartTag());
	}

	public void testDoStartTagRoleNotFoundMultiple() throws Exception
	{
		request.addUserRole("test");
		tag.setRole("bogus,bogus2");
		assertEquals(Tag.EVAL_BODY_INCLUDE, tag.doStartTag());
	}
	
}
