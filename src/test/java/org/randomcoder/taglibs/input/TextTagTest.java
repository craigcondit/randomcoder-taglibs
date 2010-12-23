package org.randomcoder.taglibs.input;

import org.randomcoder.taglibs.test.base.AbstractTagTestCase;

public class TextTagTest extends AbstractTagTestCase
{
	private TextTag tag;

	@Override
	protected void setUp() throws Exception
	{
		tag = new TextTag();
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
		tag.setSize("10");
		tag.setMaxlength("20");
		tag.doEndTag();
		String result = writer.getBuffer().toString();
		assertTrue("bad name", result.contains(" type=\"text\""));
		assertTrue("bad size", result.contains(" size=\"10\""));
		assertTrue("bad maxlength", result.contains(" maxlength=\"20\""));		
	}
}
