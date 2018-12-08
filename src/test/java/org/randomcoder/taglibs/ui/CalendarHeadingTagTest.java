package org.randomcoder.taglibs.ui;

import org.randomcoder.taglibs.test.base.AbstractTagTestCase;

import javax.servlet.jsp.jstl.core.Config;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@SuppressWarnings("javadoc") public class CalendarHeadingTagTest
    extends AbstractTagTestCase {
  private CalendarHeadingTag tag;

  @Override protected void setUp() throws Exception {
    tag = new CalendarHeadingTag();
    super.setUp(tag);
    context.setAttribute(Config.FMT_LOCALE, Locale.US);
    context.setAttribute(Config.FMT_TIME_ZONE, TimeZone.getTimeZone("GMT"));

    tag.setCapitalizeMonths(true);
    tag.setCaptionFormat("MMM yyyy");
    tag.setEncodeNextLink(true);
    tag.setEncodePrevLink(true);
    tag.setMonthParam("monthParam");
    tag.setNextClass("nextClass");
    tag.setNextContent("&gt;");
    tag.setNextId("nextId");
    tag.setNextLink("next.html");
    tag.setNextTitle("nextTitle");
    tag.setPrevClass("prevClass");
    tag.setPrevContent("&lt;");
    tag.setPrevId("prevId");
    tag.setPrevLink("prev.html");
    tag.setPrevTitle("prevTitle");
    tag.setShowDate(new Date());
    tag.setShowNextLink(true);
    tag.setShowPrevLink(true);
    tag.setYearParam("yearParam");
  }

  @Override protected void tearDown() throws Exception {
    tag.release();
    tag = null;
    super.tearDown();
  }

  public void testTag() throws Exception {
    tag.doStartTag();
    tag.doAfterBody();
    tag.doEndTag();
    String result = writer.toString();

    assertTrue("prev.html", result.contains("prev.html?"));
    assertTrue("next.html", result.contains("next.html?"));
    assertTrue("yearParam", result.contains("yearParam="));
    assertTrue("monthParam", result.contains("monthParam="));
    assertTrue("prevClass", result.contains("class=\"prevClass\""));
    assertTrue("nextClass", result.contains("class=\"nextClass\""));
    assertTrue("prevId", result.contains("id=\"prevId\""));
    assertTrue("nextId", result.contains("id=\"nextId\""));
    assertTrue("prevTitle", result.contains("title=\"prevTitle\""));
    assertTrue("nextTitle", result.contains("title=\"nextTitle\""));
    assertTrue("prevContent", result.contains(">&lt;<"));
    assertTrue("nextContent", result.contains(">&gt;<"));
  }

  public void testAbsoluteUrl() throws Exception {
    tag.setPrevLink("http://test.com/prev.html");
    tag.setNextLink("http://test.com/next.html");
    tag.doStartTag();
    tag.doAfterBody();
    tag.doEndTag();
    String result = writer.toString();

    assertTrue("prevLink",
        result.contains("href=\"http://test.com/prev.html?"));
    assertTrue("nextLink",
        result.contains("href=\"http://test.com/next.html?"));
  }

  public void testNonEncodedLink() throws Exception {
    tag.setPrevLink("http://test.com/prev.html");
    tag.setNextLink("http://test.com/next.html");
    tag.setEncodePrevLink(false);
    tag.setEncodeNextLink(false);
    tag.doStartTag();
    tag.doAfterBody();
    tag.doEndTag();
    String result = writer.toString();

    assertTrue("prevLink",
        result.contains("href=\"http://test.com/prev.html\""));
    assertTrue("nextLink",
        result.contains("href=\"http://test.com/next.html\""));
  }

  public void testHideLink() throws Exception {
    tag.setShowNextLink(false);
    tag.setShowPrevLink(false);

    tag.doStartTag();
    tag.doAfterBody();
    tag.doEndTag();
    String result = writer.toString();

    assertTrue("prevLink", result.contains("&lt;</span>"));
    assertTrue("nextLink", result.contains("&gt;</span>"));
  }

}