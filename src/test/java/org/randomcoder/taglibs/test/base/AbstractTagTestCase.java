package org.randomcoder.taglibs.test.base;

import junit.framework.TestCase;
import org.randomcoder.taglibs.test.mock.jee.JspWriterMock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import java.io.StringWriter;

@SuppressWarnings("javadoc") abstract public class AbstractTagTestCase
    extends TestCase {
  protected MockPageContext context;
  protected MockHttpServletRequest request;
  protected StringWriter writer;

  protected final void setUp(Tag... tags) {
    writer = new StringWriter();
    final JspWriterMock jspWriter = new JspWriterMock(writer);
    request = new MockHttpServletRequest();
    context = new MockPageContext(new MockServletContext(), request) {
      @Override public JspWriter getOut() {
        return jspWriter;
      }
    };
    for (Tag tag : tags)
      tag.setPageContext(context);
  }

  @Override protected void tearDown() throws Exception {
    context = null;
    request = null;
    writer.close();
    writer = null;
  }
}
