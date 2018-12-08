package org.randomcoder.taglibs.security;

import junit.framework.TestCase;
import org.randomcoder.taglibs.test.mock.jse.PrincipalMock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;

import javax.servlet.jsp.tagext.Tag;

@SuppressWarnings("javadoc") public class LoggedInTagTest extends TestCase {
  private LoggedInTag tag;
  private MockHttpServletRequest request;

  @Override protected void setUp() throws Exception {
    tag = new LoggedInTag();
    request = new MockHttpServletRequest();
    MockPageContext context =
        new MockPageContext(new MockServletContext(), request);
    tag.setPageContext(context);
  }

  @Override protected void tearDown() throws Exception {
    tag = null;
  }

  public void testDoStartTagLoggedIn() throws Exception {
    request.setUserPrincipal(new PrincipalMock("user"));
    assertEquals(Tag.EVAL_BODY_INCLUDE, tag.doStartTag());
  }

  public void testDoStartTagNotLoggedIn() throws Exception {
    request.setUserPrincipal(null);
    assertEquals(Tag.SKIP_BODY, tag.doStartTag());
  }
}
