package com.randomcoder.taglibs.common;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;

import org.junit.*;

public class HtmlHelperTest
{

	@Before
	public void setUp() throws Exception
	{}

	@After
	public void tearDown() throws Exception
	{}

	@Test
	public void testEncodePCData()
	{
		String result = HtmlHelper.encodePCData("This has embedded < & > characters");
		assertEquals("This has embedded &lt; &amp; &gt; characters", result);
		
		assertEquals("", HtmlHelper.encodePCData(null));
	}

	@Test
	public void testEncodeAttribute()
	{
		String result = HtmlHelper.encodeAttribute("This has embedded \" < & > characters");
		assertEquals("This has embedded &quot; &lt; &amp; &gt; characters", result);
		
		assertEquals("", HtmlHelper.encodeAttribute(null));
	}

	@Test
	public void testDefaultConstructor() throws Exception
	{
		Constructor con = HtmlHelper.class.getDeclaredConstructors()[0];
		con.setAccessible(true);
		con.newInstance();
	}
}
