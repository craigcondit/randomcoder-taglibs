package org.randomcoder.taglibs.input;

import org.randomcoder.taglibs.test.base.AbstractTagTestCase;

@SuppressWarnings("javadoc")
public class ImageTagTest extends AbstractTagTestCase
{
	private ImageTag tag;

	@Override
	protected void setUp() throws Exception
	{
		tag = new ImageTag();
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
		tag.setSrc("test.jpg");
		tag.setAlt("alt");
		tag.setUsemap("usemap");
		tag.setAlign("center");
		tag.doEndTag();
		String result = writer.getBuffer().toString();
		assertTrue("bad name", result.contains(" type=\"image\""));
		assertTrue("bad src", result.contains(" src=\"test.jpg\""));
		assertTrue("bad alt", result.contains(" alt=\"alt\""));
		assertTrue("bad usemap", result.contains(" usemap=\"usemap\""));
		assertTrue("bad align", result.contains(" align=\"center\""));
	}
}
