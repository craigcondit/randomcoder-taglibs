package com.randomcoder.taglibs.input;

import com.randomcoder.taglibs.test.base.AbstractTagTestCase;

public class HiddenTagTest extends AbstractTagTestCase
{
	private HiddenTag tag;

	@Override
	protected void setUp() throws Exception
	{
		tag = new HiddenTag();
		setUp(tag);
	}

	@Override
	protected void tearDown() throws Exception
	{
		tag.release();
		tag = null;
		super.tearDown();
	}

	public void testDoEndTag() throws Exception
	{
		tag.doEndTag();
		String result = writer.getBuffer().toString();
		assertTrue("bad name", result.contains(" type=\"hidden\""));
	}
}
