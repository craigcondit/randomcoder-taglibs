package org.randomcoder.taglibs.ui;

import org.randomcoder.taglibs.test.base.AbstractTagTestCase;

@SuppressWarnings("javadoc") public class PagerTagPageModeTest
    extends AbstractTagTestCase {
  private PagerTag tag;

  @Override protected void setUp() throws Exception {
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

  @Override protected void tearDown() throws Exception {
    tag.release();
    tag = null;
    super.tearDown();
  }

  public void testTagCurrentUrl() throws Exception {
    request.setQueryString(null);
    request.setServerName("www.example.com");
    request.setContextPath("");
    request.setServletPath("/page");
    request.setPathInfo(null);
    tag.setLink(null);
    tag.doStartTag();
    tag.doEndTag();
    String result = writer.toString();
    assertTrue("page.size=10: " + result, result.contains("page.size=10"));
    assertTrue("page.page=1: " + result, result.contains("page.page=1"));
    assertTrue("page.page=2: " + result, result.contains("page.page=2"));
  }

  public void testTagCurrentUrlPageOffset() throws Exception {
    request.setQueryString(null);
    request.setServerName("www.example.com");
    request.setContextPath("");
    request.setServletPath("/page");
    request.setPathInfo(null);
    tag.setPageOffset(1);
    tag.setLink(null);
    tag.doStartTag();
    tag.doEndTag();
    String result = writer.toString();
    assertTrue("page.size=10: " + result, result.contains("page.size=10"));
    assertFalse("page.page=1: " + result, result.contains("page.page=1"));
    assertTrue("page.page=2: " + result, result.contains("page.page=2"));
    assertTrue("page.page=3: " + result, result.contains("page.page=3"));
  }

  public void testTagFirstPage() throws Exception {
    tag.doStartTag();
    tag.doEndTag();
    String result = writer.toString();
    assertTrue("page.size=10: " + result, result.contains("page.size=10"));
    assertTrue("page.page=1: " + result, result.contains("page.page=1"));
    assertTrue("page.page=2: " + result, result.contains("page.page=2"));
  }

  public void testTagNegativePage() throws Exception {
    tag.setPage(-1);
    tag.doStartTag();
    tag.doEndTag();
    String result = writer.toString();
    assertTrue("page.size=10: " + result, result.contains("page.size=10"));
    assertFalse("page.page=-1: " + result, result.contains("page.page=-1"));
    assertFalse("page.page=0: " + result, result.contains("page.page=0"));
    assertTrue("page.page=1: " + result, result.contains("page.page=1"));
    assertTrue("page.page=2: " + result, result.contains("page.page=2"));
  }

  public void testTagSecondPage() throws Exception {
    tag.setPage(1);
    tag.doStartTag();
    tag.doEndTag();
    String result = writer.toString();
    assertTrue("page.size=10: " + result, result.contains("page.size=10"));
    assertTrue("page.page=0: " + result, result.contains("page.page=0"));
    assertFalse("page.page=1: " + result, result.contains("page.page=1"));
    assertTrue("page.page=2: " + result, result.contains("page.page=2"));
    assertTrue("page.page=3: " + result, result.contains("page.page=3"));
  }

}
