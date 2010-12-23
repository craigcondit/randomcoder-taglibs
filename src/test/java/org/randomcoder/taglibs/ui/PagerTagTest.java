package org.randomcoder.taglibs.ui;

import org.randomcoder.taglibs.test.base.AbstractTagTestCase;

public class PagerTagTest extends AbstractTagTestCase
{
	private PagerTag tag;

	@Override
	protected void setUp() throws Exception
	{
		tag = new PagerTag();
		super.setUp(tag);
		tag.setCount(50);
		tag.setLimit(10);
		tag.setLimitParam("limit");
		tag.setLink("http://www.example.com/");
		tag.setMaxLinks(3);
		tag.setNextContent("&gt;");
		tag.setPrevContent("&lt;");
		tag.setStart(0);
		tag.setStartParam("start");
	}

	@Override
	protected void tearDown() throws Exception
	{
		tag.release();
		tag = null;
		super.tearDown();
	}

	public void testTagCurrentUrl() throws Exception
	{
		request.setQueryString(null);
		request.setServerName("www.example.com");
		request.setContextPath("");
		request.setServletPath("/page");
		request.setPathInfo(null);
		tag.setLink(null);
		tag.doStartTag();
		tag.doEndTag();
		String result = writer.toString();
		assertEquals("1 <a href=\"/page?limit=10&amp;start=10\">2</a> <a href=\"/page?limit=10&amp;start=20\">3</a> <a href=\"/page?limit=10&amp;start=10\">&gt;</a>", result);
	}
	
	public void testTagFirstPage() throws Exception
	{
		tag.doStartTag();
		tag.doEndTag();
		String result = writer.toString();
		assertEquals("1 <a href=\"http://www.example.com/?limit=10&amp;start=10\">2</a> <a href=\"http://www.example.com/?limit=10&amp;start=20\">3</a> <a href=\"http://www.example.com/?limit=10&amp;start=10\">&gt;</a>", result);
	}

	public void testTagNegativeStart() throws Exception
	{
		tag.setStart(-1);
		tag.doStartTag();
		tag.doEndTag();
		String result = writer.toString();
		assertEquals("1 <a href=\"http://www.example.com/?limit=10&amp;start=10\">2</a> <a href=\"http://www.example.com/?limit=10&amp;start=20\">3</a> <a href=\"http://www.example.com/?limit=10&amp;start=10\">&gt;</a>", result);
	}

	public void testTagSecondPage() throws Exception
	{		
		tag.setStart(10);
		tag.doStartTag();
		tag.doEndTag();
		String result = writer.toString();
		assertEquals("<a href=\"http://www.example.com/?limit=10&amp;start=0\">&lt;</a> <a href=\"http://www.example.com/?limit=10&amp;start=0\">1</a> 2 <a href=\"http://www.example.com/?limit=10&amp;start=20\">3</a> <a href=\"http://www.example.com/?limit=10&amp;start=30\">4</a> <a href=\"http://www.example.com/?limit=10&amp;start=20\">&gt;</a>", result);
	}
	
}
