package com.randomcoder.taglibs.common;

import junit.framework.TestCase;

public class HtmlHelperTest extends TestCase
{
	public void testEncodePCData()
	{
		String result = HtmlHelper.encodePCData("This has embedded < & > characters");
		assertEquals("This has embedded &lt; &amp; &gt; characters", result);
		
		assertEquals("", HtmlHelper.encodePCData(null));
	}

	public void testEncodeAttribute()
	{
		String result = HtmlHelper.encodeAttribute("This has embedded \" < & > characters");
		assertEquals("This has embedded &quot; &lt; &amp; &gt; characters", result);
		
		assertEquals("", HtmlHelper.encodeAttribute(null));
	}
}
