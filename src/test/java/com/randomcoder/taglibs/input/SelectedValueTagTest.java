package com.randomcoder.taglibs.input;

import java.util.Set;

import javax.servlet.jsp.JspException;

import com.randomcoder.taglibs.test.base.InputTagTestCase;

public class SelectedValueTagTest extends InputTagTestCase
{
	private SelectedValueTag tag;
	private SelectTag parent;

	@Override
	protected void setUp() throws Exception
	{
		tag = new SelectedValueTag();
		parent = new SelectTag();
		tag.setParent(parent);
		setUp(tag, parent);
	}

	@Override
	protected void tearDown() throws Exception
	{
		tag.release();
		tag = null;
		parent.release();
		parent = null;
		super.tearDown();
	}
	
	public void testTag() throws Exception
	{
		tag.setValue("value");
		parent.doStartTag();
		tag.doStartTag();
		tag.doEndTag();		
		Set<String> values = parent.getCurrentValues();
		assertNotNull(values);
		assertEquals(1, values.size());
		assertTrue(values.contains("value"));
		parent.doEndTag();
	}

	public void testTagNoValue() throws Exception
	{
		parent.doStartTag();
		tag.doStartTag();
		tag.doEndTag();
		Set<String> values = parent.getCurrentValues();
		assertNotNull(values);
		assertEquals(0, values.size());
		parent.doEndTag();
	}

	public void testTagNoParent() throws Exception
	{
		try
		{
			tag.setParent(null);
			tag.doStartTag();
			tag.doEndTag();
			fail("JspException expected");
		}
		catch (JspException e)
		{
			// pass
		}
	}
}
