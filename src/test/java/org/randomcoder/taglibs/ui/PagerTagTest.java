package org.randomcoder.taglibs.ui;

import org.randomcoder.taglibs.test.base.AbstractTagTestCase;

@SuppressWarnings("javadoc") public class PagerTagTest
    extends AbstractTagTestCase {
  private PagerTag tag;

  @Override protected void setUp() throws Exception {
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
    assertTrue("limit=10: " + result, result.contains("limit=10"));
    assertTrue("start=10: " + result, result.contains("start=10"));
    assertTrue("start=20: " + result, result.contains("start=20"));
  }

  public void testTagCurrentUrlStartOffset() throws Exception {
    request.setQueryString(null);
    request.setServerName("www.example.com");
    request.setContextPath("");
    request.setServletPath("/page");
    request.setPathInfo(null);
    tag.setLink(null);
    tag.setStartOffset(1);
    tag.doStartTag();
    tag.doEndTag();
    String result = writer.toString();
    assertTrue("limit=10: " + result, result.contains("limit=10"));
    assertTrue("start=11: " + result, result.contains("start=11"));
    assertTrue("start=21: " + result, result.contains("start=21"));
  }

  public void testTagFirstPage() throws Exception {
    tag.doStartTag();
    tag.doEndTag();
    String result = writer.toString();
    assertTrue("limit=10: " + result, result.contains("limit=10"));
    assertTrue("start=10: " + result, result.contains("start=10"));
    assertTrue("start=20: " + result, result.contains("start=20"));
  }

  public void testTagNegativeStart() throws Exception {
    tag.setStart(-1);
    tag.doStartTag();
    tag.doEndTag();
    String result = writer.toString();
    assertTrue("limit=10: " + result, result.contains("limit=10"));
    assertFalse("start=-10: " + result, result.contains("start=-10"));
    assertFalse("start=0: " + result, result.contains("start=0"));
    assertTrue("start=10: " + result, result.contains("start=10"));
    assertTrue("start=20: " + result, result.contains("start=20"));
  }

  public void testTagSecondPage() throws Exception {
    tag.setStart(10);
    tag.doStartTag();
    tag.doEndTag();
    String result = writer.toString();
    assertTrue("limit=10: " + result, result.contains("limit=10"));
    assertTrue("start=0: " + result, result.contains("start=0"));
    assertFalse("start=10: " + result, result.contains("start=10"));
    assertTrue("start=20: " + result, result.contains("start=20"));
    assertTrue("start=30: " + result, result.contains("start=30"));
  }

}
