package com.randomcoder.taglibs.common;

import static org.junit.Assert.*;

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
	}

	@Test
	public void testEncodeAttribute()
	{
		String result = HtmlHelper.encodeAttribute("This has embedded \" < & > characters");
		assertEquals("This has embedded &quot; &lt; &amp; &gt; characters", result);
	}

}