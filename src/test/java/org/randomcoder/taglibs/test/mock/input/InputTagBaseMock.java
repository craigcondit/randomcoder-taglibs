package org.randomcoder.taglibs.test.mock.input;

import org.randomcoder.taglibs.input.InputTagBase;

import java.util.Map;

@SuppressWarnings("javadoc") public class InputTagBaseMock
    extends InputTagBase {
  private static final long serialVersionUID = 6434610266875827823L;

  @Override public String getType() {
    return "test";
  }

  @Override public String buildOptions() {
    return super.buildOptions();
  }

  @Override public String getName() {
    return super.getName();
  }

  @Override public Map<String, String> getParams() {
    return super.getParams();
  }

  @Override public String getStyleId() {
    return super.getStyleId();
  }

  @Override public String getValue() {
    return super.getValue();
  }
}
