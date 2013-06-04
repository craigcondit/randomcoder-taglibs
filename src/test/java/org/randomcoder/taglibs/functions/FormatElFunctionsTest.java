package org.randomcoder.taglibs.functions;

import junit.framework.TestCase;

@SuppressWarnings("javadoc")
public class FormatElFunctionsTest extends TestCase
{
	
	public void testShortenNull()
	{
		assertNull(FormatElFunctions.shorten(null, 10));
	}

	public void testShortenShort()
	{
		assertEquals("Short", FormatElFunctions.shorten("Short", 10));
	}

	public void testShortenTiny()
	{
		assertEquals("...", FormatElFunctions.shorten("Value", 3));
	}

	public void testShortenNormal()
	{
		assertEquals("Val...", FormatElFunctions.shorten("Values", 5));
	}
	
}
