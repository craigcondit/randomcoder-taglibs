package org.randomcoder.taglibs.functions;

import junit.framework.TestCase;

@SuppressWarnings("javadoc")
public class EscapeElFunctionsTest extends TestCase
{
	public void testUrlencode()
	{
		assertNull(EscapeElFunctions.urlencode(null));
	}
	
	public void testUrlencodeNull()
	{
		assertEquals("Testing+%26+%3C+%3E", EscapeElFunctions.urlencode("Testing & < >"));
	}
	
	public void testAttribute()
	{
		assertEquals("Testing &amp; &lt; &gt; &#34; &#39;", EscapeElFunctions.attribute("Testing & < > \" '"));
	}

	public void testAttributeNull()
	{
		assertNull(EscapeElFunctions.attribute(null));
	}
	
	public void testJsDouble()
	{
		assertEquals("Testing \\\\ \\\"", EscapeElFunctions.jsDouble("Testing \\ \""));
	}

	public void testJsDoubleNull()
	{
		assertNull(EscapeElFunctions.jsDouble(null));
	}
	
	public void testJsSingle()
	{
		assertEquals("Testing \\\\ \\'", EscapeElFunctions.jsSingle("Testing \\ '"));
	}

	public void testJsSingleNull()
	{
		assertNull(EscapeElFunctions.jsSingle(null));
	}
	
}
