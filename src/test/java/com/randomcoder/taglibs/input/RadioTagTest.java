package com.randomcoder.taglibs.input;

import com.randomcoder.taglibs.test.base.InputTagTestCase;

public class RadioTagTest extends InputTagTestCase
{
	private RadioTag tag;

	@Override
	protected void setUp() throws Exception
	{
		tag = new RadioTag();
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
		assertTrue("bad name", result.contains(" type=\"radio\""));
	}
}
