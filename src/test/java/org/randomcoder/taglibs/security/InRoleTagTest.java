package org.randomcoder.taglibs.security;

import junit.framework.TestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;

import javax.servlet.jsp.tagext.Tag;

@SuppressWarnings("javadoc") public class InRoleTagTest extends TestCase {
  private InRoleTag tag;
  private MockHttpServletRequest request;

  @Override protected void setUp() throws Exception {
    tag = new InRoleTag();
    request = new MockHttpServletRequest();
    MockPageContext context =
        new MockPageContext(new MockServletContext(), request);
    tag.setPageContext(context);
  }

  @Override protected void tearDown() throws Exception {
    tag = null;
  }

  public void testDoStartTagRoleFound() throws Exception {
    request.addUserRole("test");
    tag.setRole("test");
    assertEquals(Tag.EVAL_BODY_INCLUDE, tag.doStartTag());
  }

  public void testDoStartTagRoleFoundMultiple() throws Exception {
    request.addUserRole("test");
    tag.setRole("test,test2");
    assertEquals(Tag.EVAL_BODY_INCLUDE, tag.doStartTag());
  }

  public void testDoStartTagRoleNotFound() throws Exception {
    request.addUserRole("test");
    tag.setRole("bogus");
    assertEquals(Tag.SKIP_BODY, tag.doStartTag());
  }

  public void testDoStartTagRoleNotFoundMultiple() throws Exception {
    request.addUserRole("test");
    tag.setRole("bogus,bogus2");
    assertEquals(Tag.SKIP_BODY, tag.doStartTag());
  }

}
