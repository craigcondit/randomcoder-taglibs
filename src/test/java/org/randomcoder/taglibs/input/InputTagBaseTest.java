package org.randomcoder.taglibs.input;

import org.randomcoder.taglibs.test.base.AbstractTagTestCase;
import org.randomcoder.taglibs.test.mock.input.InputTagBaseMock;

public class InputTagBaseTest extends AbstractTagTestCase
{
	InputTagBaseMock tag = null;
	
	@Override
	protected void setUp() throws Exception
	{
		tag = new InputTagBaseMock();
		setUp(tag);
	}

	@Override
	protected void tearDown() throws Exception
	{
		tag.release();
		tag = null;
		super.tearDown();
	}

	public void testName()
	{
		tag.setName("test-name");
		assertEquals("test-name", tag.getName());
	}

	public void testValue()
	{
		tag.setValue("test-value");
		assertEquals("test-value", tag.getValue());
	}

	public void testStyleId()
	{
		tag.setStyleId("test-id");
		assertEquals("test-id", tag.getStyleId());
	}

	public void testDisabled()
	{
		tag.setDisabled("disabled");
		assertEquals("disabled", tag.getParams().get("disabled"));
		
		tag.setDisabled("true");
		assertEquals("disabled", tag.getParams().get("disabled"));
		
		tag.setDisabled("1");
		assertEquals("disabled", tag.getParams().get("disabled"));
		
		tag.setDisabled("false");
		assertNull(tag.getParams().get("disabled"));
	}

	public void testSetReadonly()
	{
		tag.setReadonly("readonly");
		assertEquals("readonly", tag.getParams().get("readonly"));
		
		tag.setReadonly("true");
		assertEquals("readonly", tag.getParams().get("readonly"));
		
		tag.setReadonly("1");
		assertEquals("readonly", tag.getParams().get("readonly"));
		
		tag.setReadonly("false");
		assertNull(tag.getParams().get("readonly"));
	}

	public void testTabindex()
	{
		tag.setTabindex("1");
		assertEquals("1", tag.getParams().get("tabindex"));
	}

	public void testAccesskey()
	{
		tag.setAccesskey("a");
		assertEquals("a", tag.getParams().get("accesskey"));
	}

	public void testStyleClass()
	{
		tag.setStyleClass("test-class");
		assertEquals("test-class", tag.getParams().get("class"));
	}

	public void testDir()
	{
		tag.setDir("ltr");
		assertEquals("ltr", tag.getParams().get("dir"));
	}

	public void testLang()
	{
		tag.setLang("en_US");
		assertEquals("en_US", tag.getParams().get("lang"));
	}

	public void testStyle()
	{
		tag.setStyle("display: block");
		assertEquals("display: block", tag.getParams().get("style"));
	}

	public void testTitle()
	{
		tag.setTitle("title");
		assertEquals("title", tag.getParams().get("title"));
	}

	public void testOnblur()
	{
		tag.setOnblur("alert('blur');");
		assertEquals("alert('blur');", tag.getParams().get("onblur"));
	}

	public void testOnchange()
	{
		tag.setOnchange("alert('change');");
		assertEquals("alert('change');", tag.getParams().get("onchange"));
	}

	public void testOnfocus()
	{
		tag.setOnfocus("alert('focus');");
		assertEquals("alert('focus');", tag.getParams().get("onfocus"));
	}

	public void testOnselect()
	{
		tag.setOnselect("alert('select');");
		assertEquals("alert('select');", tag.getParams().get("onselect"));
	}

	public void testOnclick()
	{
		tag.setOnclick("alert('click');");
		assertEquals("alert('click');", tag.getParams().get("onclick"));
	}

	public void testOndblclick()
	{
		tag.setOndblclick("alert('dblclick');");
		assertEquals("alert('dblclick');", tag.getParams().get("ondblclick"));
	}

	public void testOnkeydown()
	{
		tag.setOnkeydown("alert('keydown');");
		assertEquals("alert('keydown');", tag.getParams().get("onkeydown"));
	}

	public void testOnkeypress()
	{
		tag.setOnkeypress("alert('keypress');");
		assertEquals("alert('keypress');", tag.getParams().get("onkeypress"));
	}

	public void testOnkeyup()
	{
		tag.setOnkeyup("alert('keyup');");
		assertEquals("alert('keyup');", tag.getParams().get("onkeyup"));
	}

	public void testOnmousedown()
	{
		tag.setOnmousedown("alert('mousedown');");
		assertEquals("alert('mousedown');", tag.getParams().get("onmousedown"));
	}

	public void testOnmousemove()
	{
		tag.setOnmousemove("alert('mousemove');");
		assertEquals("alert('mousemove');", tag.getParams().get("onmousemove"));
	}

	public void testOnmouseout()
	{
		tag.setOnmouseout("alert('mouseout');");
		assertEquals("alert('mouseout');", tag.getParams().get("onmouseout"));
	}

	public void testOnmouseover()
	{
		tag.setOnmouseover("alert('mouseover');");
		assertEquals("alert('mouseover');", tag.getParams().get("onmouseover"));
	}

	public void testSetOnmouseup()
	{
		tag.setOnmouseup("alert('mouseup');");
		assertEquals("alert('mouseup');", tag.getParams().get("onmouseup"));
	}
	
	public void testDoEndTag() throws Exception
	{
		tag.setName("name");
		tag.setStyleId("id");
		tag.setValue("value");
		tag.setStyle("display: block");
		tag.setStyleClass(null);		
		tag.doEndTag();
		
		String result = writer.getBuffer().toString();
		
		assertTrue("bad tag", result.contains("<input"));
		assertTrue("bad name", result.contains(" name=\"name\""));
		assertTrue("bad id", result.contains(" id=\"id\""));
		assertTrue("bad value", result.contains(" value=\"value\""));
		assertTrue("bad style", result.contains(" style=\"display: block\""));
		assertFalse("bad class", result.contains(" class="));
	}
}
