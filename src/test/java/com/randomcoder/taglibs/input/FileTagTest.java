package com.randomcoder.taglibs.input;

import com.randomcoder.taglibs.test.base.InputTagTestCase;

public class FileTagTest extends InputTagTestCase
{
	private FileTag tag;

	@Override
	protected void setUp() throws Exception
	{
		tag = new FileTag();
		setUp(tag);
	}

	@Override
	protected void tearDown() throws Exception
	{
		tag.release();
		tag = null;
		super.tearDown();
	}

	public void testSetValue() throws Exception
	{
		try
		{
			tag.setValue("value");
			fail("IllegalArgumentException expected");
		}
		catch (IllegalArgumentException e)
		{
			// pass
		}
	}
	public void testDoEndTag() throws Exception
	{
		tag.setAccept("all");
		tag.doEndTag();
		String result = writer.getBuffer().toString();
		assertTrue("bad name", result.contains(" type=\"file\""));
		assertTrue("bad accept", result.contains(" accept=\"all\""));
	}
}
