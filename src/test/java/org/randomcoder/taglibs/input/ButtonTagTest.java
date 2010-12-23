package org.randomcoder.taglibs.input;

import org.randomcoder.taglibs.test.base.AbstractTagTestCase;

public class ButtonTagTest extends AbstractTagTestCase
{
	private ButtonTag tag;

	@Override
	protected void setUp() throws Exception
	{
		tag = new ButtonTag();
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
		assertTrue("bad name", result.contains(" type=\"button\""));
	}
}
