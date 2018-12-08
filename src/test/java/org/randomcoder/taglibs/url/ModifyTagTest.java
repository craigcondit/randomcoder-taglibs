package org.randomcoder.taglibs.url;

import junit.framework.TestCase;
import org.randomcoder.taglibs.test.mock.jee.JspWriterMock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.StringWriter;

@SuppressWarnings("javadoc") public class ModifyTagTest extends TestCase {
  private ModifyTag tag;
  private MockPageContext context;
  private MockHttpServletRequest request;
  private StringWriter writer;

  @Override protected void setUp() throws Exception {
    tag = new ModifyTag();
    writer = new StringWriter();
    final JspWriterMock jspWriter = new JspWriterMock(writer);
    request = new MockHttpServletRequest();
    request.setProtocol("http");
    request.setServerPort(80);
    request.setServerName("www.example.com");
    request.setContextPath("/context");
    request.setServletPath("/servlet");
    request.setPathInfo("/index.jsp");
    request.setQueryString("?param1=test1");
    context = new MockPageContext(new MockServletContext(), request) {
      @Override public JspWriter getOut() {
        return jspWriter;
      }
    };
    tag.setPageContext(context);
  }

  @Override protected void tearDown() throws Exception {
    tag.release();
    tag = null;
    request = null;
    context = null;
    writer.close();
    writer = null;
  }

  public void testScopeOnly() throws Exception {
    try {
      tag.setScope("request");
      tag.doStartTag();
      fail("JspException expected");
    } catch (JspException e) {
      // pass
    }
  }

  public void testCurrentPageNoVar() throws Exception {
    tag.doStartTag();
    tag.doEndTag();
    String result = writer.getBuffer().toString();
    assertEquals("/context/servlet/index.jsp?param1=test1", result);
  }

  public void testCurrentPageVar() throws Exception {
    tag.setVar("test");
    tag.doStartTag();
    tag.doEndTag();
    String result = (String) context.getAttribute("test");
    assertEquals("/context/servlet/index.jsp?param1=test1", result);
  }

  public void testCurrentPageScopeVar() throws Exception {
    tag.setVar("test");
    tag.setScope("request");
    tag.doStartTag();
    tag.doEndTag();
    String result =
        (String) context.getAttribute("test", PageContext.REQUEST_SCOPE);
    assertEquals("/context/servlet/index.jsp?param1=test1", result);
    assertNull(context.getAttribute("test", PageContext.PAGE_SCOPE));
  }

  public void testRelativeValue() throws Exception {
    tag.setValue("http://www.example.com/update?param1=test1");
    tag.doStartTag();
    tag.doEndTag();
    String result = writer.getBuffer().toString();
    assertEquals("/update?param1=test1", result);
  }

  public void testNonRelativeValue() throws Exception {
    tag.setValue("http://test.example.com/update?param1=test1");
    tag.doStartTag();
    tag.doEndTag();
    String result = writer.getBuffer().toString();
    assertEquals("http://test.example.com/update?param1=test1", result);
  }

  public void testModifiedCurrentPage() throws Exception {
    tag.doStartTag();
    tag.getParams().clear();
    tag.doEndTag();
    String result = writer.getBuffer().toString();
    assertEquals("/context/servlet/index.jsp", result);
  }

  public void testModifiedValue() throws Exception {
    tag.setValue("http://www.example.com/update?param1=test1");
    tag.doStartTag();
    tag.getParams().clear();
    tag.doEndTag();
    String result = writer.getBuffer().toString();
    assertEquals("/update", result);
  }

  public void testMalformedValue() throws Exception {
    try {
      tag.setValue("bogus://url");
      tag.doStartTag();
      fail("JspException expected");
    } catch (JspException e) {
      // pass
    }
  }
}
