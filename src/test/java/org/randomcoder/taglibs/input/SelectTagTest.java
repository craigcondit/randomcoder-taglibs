package org.randomcoder.taglibs.input;

import java.util.*;

import org.randomcoder.taglibs.test.base.AbstractTagTestCase;

public class SelectTagTest extends AbstractTagTestCase
{
	private SelectTag tag;

	@Override
	protected void setUp() throws Exception
	{
		tag = new SelectTag();
		setUp(tag);
	}

	@Override
	protected void tearDown() throws Exception
	{
		tag.release();
		tag = null;
		super.tearDown();
	}
	
	public void testAddValueNull() throws Exception
	{
		tag.addValue(null);
		Set<String> values = tag.getCurrentValues();
		assertNotNull(values);
		assertEquals(0, values.size());
	}

	public void testAddValueString() throws Exception
	{
		tag.addValue("string");
		Set<String> values = tag.getCurrentValues();
		assertNotNull(values);
		assertEquals(1, values.size());
		assertTrue(values.contains("string"));
	}

	public void testAddValueLong() throws Exception
	{
		tag.addValue(new Long(12345));
		Set<String> values = tag.getCurrentValues();
		assertNotNull(values);
		assertEquals(1, values.size());
		assertTrue(values.contains("12345"));
	}

	public void testAddValueShort() throws Exception
	{
		tag.addValue(new Short((short)12345));
		Set<String> values = tag.getCurrentValues();
		assertNotNull(values);
		assertEquals(1, values.size());
		assertTrue(values.contains("12345"));
	}

	public void testAddValueInt() throws Exception
	{
		tag.addValue(new Integer(12345));
		Set<String> values = tag.getCurrentValues();
		assertNotNull(values);
		assertEquals(1, values.size());
		assertTrue(values.contains("12345"));
	}

	public void testAddValueObject() throws Exception
	{
		Object obj = new Object();
		tag.addValue(obj);
		Set<String> values = tag.getCurrentValues();
		assertNotNull(values);
		assertEquals(1, values.size());
		assertTrue(values.contains(obj.toString()));
	}

	public void testAddValueCollection() throws Exception
	{
		List<String> list = new ArrayList<String>();
		list.add("item-1");
		list.add("item-2");
		tag.addValue(list);
		Set<String> values = tag.getCurrentValues();
		assertNotNull(values);
		assertEquals(2, values.size());
		assertTrue(values.contains("item-1"));
		assertTrue(values.contains("item-2"));
	}

	public void testSetValue() throws Exception
	{
		tag.setValue("test-value");
		Set<String> values = tag.getCurrentValues();
		assertNotNull(values);
		assertEquals(1, values.size());
		assertTrue(values.contains("test-value"));
	}
		
	public void testSetValueObject() throws Exception
	{
		Object obj = new Object();
		tag.setValue(obj);
		Set<String> values = tag.getCurrentValues();
		assertNotNull(values);
		assertEquals(1, values.size());
		assertTrue(values.contains(obj.toString()));
	}
	
	public void testSetMultiple() throws Exception
	{
		tag.setMultiple("multiple");
		assertTrue(tag.isMultipleSelect());
		
		tag.setMultiple(null);
		assertFalse(tag.isMultipleSelect());
		
		tag.setMultiple("true");
		assertTrue(tag.isMultipleSelect());
	}

	public void testDoEndTag() throws Exception
	{
		tag.setMultiple("multiple");
		tag.setName("name");
		tag.setSize("10");
		tag.setStyleId("id");
		tag.doStartTag();
		tag.doEndTag();
		String result = writer.getBuffer().toString();
		assertTrue("bad tag name", result.contains("<select"));
		assertTrue("bad name", result.contains(" name=\"name\""));
		assertTrue("bad multiple", result.contains(" multiple=\"multiple\""));
		assertTrue("bad size", result.contains(" size=\"10\""));
		assertTrue("bad id", result.contains(" id=\"id\""));
	}
}
