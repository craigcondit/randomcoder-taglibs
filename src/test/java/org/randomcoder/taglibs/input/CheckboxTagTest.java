package org.randomcoder.taglibs.input;

import org.randomcoder.taglibs.test.base.AbstractTagTestCase;

@SuppressWarnings("javadoc")
public class CheckboxTagTest extends AbstractTagTestCase
{
	private CheckboxTag tag;

	@Override
	protected void setUp() throws Exception
	{
		tag = new CheckboxTag();
		setUp(tag);
	}

	@Override
	protected void tearDown() throws Exception
	{
		tag.release();
		tag = null;
		super.tearDown();
	}

	public void testSetCheckedFalse() throws Exception
	{
		tag.setChecked("false");
		tag.doEndTag();
		String result = writer.getBuffer().toString();
		assertFalse("bad checked", result.contains(" checked="));		
	}

	public void testSetCheckedNull() throws Exception
	{
		tag.setChecked(null);
		tag.doEndTag();
		String result = writer.getBuffer().toString();
		assertFalse("bad checked", result.contains(" checked="));		
	}

	public void testSetCheckedTrue() throws Exception
	{
		tag.setChecked("true");
		tag.doEndTag();
		String result = writer.getBuffer().toString();
		assertTrue("bad checked", result.contains(" checked=\"checked\""));		
	}

	public void testSetCheckedOne() throws Exception
	{
		tag.setChecked("1");
		tag.doEndTag();
		String result = writer.getBuffer().toString();
		assertTrue("bad checked", result.contains(" checked=\"checked\""));		
	}

	public void testSetCheckedOn() throws Exception
	{
		tag.setChecked("on");
		tag.doEndTag();
		String result = writer.getBuffer().toString();
		assertTrue("bad checked", result.contains(" checked=\"checked\""));		
	}

	public void testSetCheckedYes() throws Exception
	{
		tag.setChecked("yes");
		tag.doEndTag();
		String result = writer.getBuffer().toString();
		assertTrue("bad checked", result.contains(" checked=\"checked\""));		
	}

	public void testSetCheckedChecked() throws Exception
	{
		tag.setChecked("checked");
		tag.doEndTag();
		String result = writer.getBuffer().toString();
		assertTrue("bad checked", result.contains(" checked=\"checked\""));		
	}

	public void testDoEndTag() throws Exception
	{
		tag.setStyleId("id");
		tag.setValue("value");
		tag.doEndTag();
		String result = writer.getBuffer().toString();
		assertTrue("bad name", result.contains(" type=\"checkbox\""));
		assertTrue("bad id", result.contains(" id=\"id\""));
		assertTrue("bad value", result.contains(" value=\"value\""));
	}
}
