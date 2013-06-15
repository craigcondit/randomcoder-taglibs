package org.randomcoder.taglibs.ui;

import org.randomcoder.taglibs.test.base.AbstractTagTestCase;

@SuppressWarnings("javadoc")
public class PagerTagPageModeTest extends AbstractTagTestCase
{
	private PagerTag tag;

	@Override
	protected void setUp() throws Exception
	{
		tag = new PagerTag();
		super.setUp(tag);
		tag.setCount(50);
		tag.setLimit(10);
		tag.setLimitParam("page.size");
		tag.setLink("http://www.example.com/");
		tag.setMaxLinks(3);
		tag.setNextContent("&gt;");
		tag.setPrevContent("&lt;");
		tag.setPage(0);
		tag.setPageParam("page.page");
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
		assertEquals("1 <a href=\"/page?page.size=10&amp;page.page=1\">2</a> <a href=\"/page?page.size=10&amp;page.page=2\">3</a> <a href=\"/page?page.size=10&amp;page.page=1\">&gt;</a>", result);
	}
	
	public void testTagFirstPage() throws Exception
	{
		tag.doStartTag();
		tag.doEndTag();
		String result = writer.toString();
		assertEquals("1 <a href=\"http://www.example.com/?page.size=10&amp;page.page=1\">2</a> <a href=\"http://www.example.com/?page.size=10&amp;page.page=2\">3</a> <a href=\"http://www.example.com/?page.size=10&amp;page.page=1\">&gt;</a>", result);
	}

	public void testTagNegativePage() throws Exception
	{
		tag.setPage(-1);
		tag.doStartTag();
		tag.doEndTag();
		String result = writer.toString();
		assertEquals("1 <a href=\"http://www.example.com/?page.size=10&amp;page.page=1\">2</a> <a href=\"http://www.example.com/?page.size=10&amp;page.page=2\">3</a> <a href=\"http://www.example.com/?page.size=10&amp;page.page=1\">&gt;</a>", result);
	}

	public void testTagSecondPage() throws Exception
	{		
		tag.setPage(1);
		tag.doStartTag();
		tag.doEndTag();
		String result = writer.toString();
		assertEquals("<a href=\"http://www.example.com/?page.size=10&amp;page.page=0\">&lt;</a> <a href=\"http://www.example.com/?page.size=10&amp;page.page=0\">1</a> 2 <a href=\"http://www.example.com/?page.size=10&amp;page.page=2\">3</a> <a href=\"http://www.example.com/?page.size=10&amp;page.page=3\">4</a> <a href=\"http://www.example.com/?page.size=10&amp;page.page=2\">&gt;</a>", result);
	}
	
}
