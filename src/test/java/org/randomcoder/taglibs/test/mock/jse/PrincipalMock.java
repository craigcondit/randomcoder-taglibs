package org.randomcoder.taglibs.test.mock.jse;

import java.security.Principal;

@SuppressWarnings("javadoc") public class PrincipalMock implements Principal {
  private String name;

  public PrincipalMock(String name) {
    this.name = name;
  }

  @Override public String getName() {
    return name;
  }
}
