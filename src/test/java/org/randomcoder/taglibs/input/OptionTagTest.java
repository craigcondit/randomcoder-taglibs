package org.randomcoder.taglibs.input;

import javax.servlet.jsp.JspException;

import org.randomcoder.taglibs.test.base.AbstractTagTestCase;

@SuppressWarnings("javadoc")
public class OptionTagTest extends AbstractTagTestCase
{
	private OptionTag tag;
	private SelectTag parent;

	@Override
	protected void setUp() throws Exception
	{
		tag = new OptionTag();
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
	
	public void testSetName() throws Exception
	{
		try
		{
			tag.setName("name");
			fail("IllegalArgumentException expected");
		}
		catch (IllegalArgumentException e)
		{
			// pass
		}
	}

	public void testSetReadonly() throws Exception
	{
		try
		{
			tag.setReadonly("name");
			fail("IllegalArgumentException expected");
		}
		catch (IllegalArgumentException e)
		{
			// pass
		}
	}

	public void testSetTabindex() throws Exception
	{
		try
		{
			tag.setTabindex("1");
			fail("IllegalArgumentException expected");
		}
		catch (IllegalArgumentException e)
		{
			// pass
		}
	}

	public void testSetAccessKey() throws Exception
	{
		try
		{
			tag.setAccesskey("a");
			fail("IllegalArgumentException expected");
		}
		catch (IllegalArgumentException e)
		{
			// pass
		}
	}

	public void testSetOnblur() throws Exception
	{
		try
		{
			tag.setOnblur("alert('blur');");
			fail("IllegalArgumentException expected");
		}
		catch (IllegalArgumentException e)
		{
			// pass
		}
	}

	public void testSetOnchange() throws Exception
	{
		try
		{
			tag.setOnchange("alert('change');");
			fail("IllegalArgumentException expected");
		}
		catch (IllegalArgumentException e)
		{
			// pass
		}
	}

	public void testSetOnfocus() throws Exception
	{
		try
		{
			tag.setOnfocus("alert('focus');");
			fail("IllegalArgumentException expected");
		}
		catch (IllegalArgumentException e)
		{
			// pass
		}
	}

	public void testSetOnselect() throws Exception
	{
		try
		{
			tag.setOnselect("alert('select');");
			fail("IllegalArgumentException expected");
		}
		catch (IllegalArgumentException e)
		{
			// pass
		}
	}

	public void testTagNoParent() throws Exception
	{
		try
		{
			tag.setParent(null);
			tag.doEndTag();
			fail("JspException expected");
		}
		catch (JspException e)
		{
			// pass
		}
	}

	public void testTag() throws Exception
	{
		parent.setValue("value");
		tag.setText("text");
		tag.setValue("value");		
		tag.setStyleId("id");
		tag.setLabel("label");
		parent.doStartTag();
		tag.doStartTag();
		tag.doEndTag();
		parent.doEndTag();
		String result = writer.getBuffer().toString();
		assertTrue("bad tag name", result.contains("<option"));
		assertTrue("bad id", result.contains(" id=\"id\""));
		assertTrue("bad label", result.contains(" label=\"label\""));
		assertTrue("bad selected", result.contains(" selected=\"selected\""));
	}

	public void testTagNoValue() throws Exception
	{
		parent.setValue("text");
		tag.setText("text");
		tag.setStyleId("id");
		tag.setLabel("label");
		parent.doStartTag();
		tag.doStartTag();
		tag.doEndTag();
		parent.doEndTag();
		String result = writer.getBuffer().toString();
		assertTrue("bad tag name", result.contains("<option"));
		assertTrue("bad id", result.contains(" id=\"id\""));
		assertTrue("bad label", result.contains(" label=\"label\""));
		assertTrue("bad selected", result.contains(" selected=\"selected\""));
	}
}
