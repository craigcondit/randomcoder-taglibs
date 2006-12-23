package com.randomcoder.taglibs.common;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.*;

public class HtmlHelperTest
{

	@BeforeTest
	public void setUp() throws Exception
	{}

	@AfterTest
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
}
