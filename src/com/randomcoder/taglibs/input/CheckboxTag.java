/*
 * $Id: CheckboxTag.java 20 2005-02-09 20:13:51Z ccondit $
 */
package com.randomcoder.taglibs.input;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.jsp.*;

import com.randomcoder.taglibs.common.HtmlHelper;

/**
 * Tag class which produces &lt;input type="checkbox"&gt;.
 * 
 * <pre>
 * Copyright (c) 2006, Craig Condit. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *   * Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * </pre> 
 */
public class CheckboxTag extends InputTagBase {
  private static final long serialVersionUID = -4288046933679612906L;
  
  private static final String ONE = "1".toUpperCase(Locale.US);
  private static final String YES = "YES".toUpperCase(Locale.US);
  private static final String TRUE = "TRUE".toUpperCase(Locale.US);
  private static final String ON = "ON".toUpperCase(Locale.US);
  private static final String CHECKED = "CHECKED".toUpperCase(Locale.US);
  
  /**
   * Value of checked HTML attribute.
   */
  protected String checked;

  /**
   * Sets the checked HTML attribute.
   * @param checked value of checked attribute
   */
  public void setChecked(String checked) { this.checked = checked; }
  
  /**
   * Determines if checkbox attribute is selected
   * @return true if checkbox is checked, false otherwise
   */
  protected boolean isChecked() {
    if (checked == null) return false;
    String value = checked.toUpperCase(Locale.US);
    
    if (TRUE.equals(value)) return true;
    if (ONE.equals(value)) return true;
    if (CHECKED.equals(value)) return true;
    if (ON.equals(value)) return true;
    if (YES.equals(value)) return true;

    return false;
  }
	
  @Override
  public void release() {
    super.release();
    checked = null;
  }

  @Override
  public int doEndTag() throws JspException {
    try {
      JspWriter out = pageContext.getOut();

      boolean isChecked = isChecked();

      out.write("<input type=\"" + getType() + "\"");
      out.write(" name=\"" + HtmlHelper.encodeAttribute(getName()) + "\"");
      if (getStyleId() != null) out.write(" id=\"" + HtmlHelper.encodeAttribute(getStyleId()) + "\"");
      if (getValue() != null) out.write(" value=\"" + HtmlHelper.encodeAttribute(getValue()) + "\"");
      if (isChecked) out.write(" checked=\"checked\"");
      out.write(buildOptions());
      out.write(" />");
    } catch (IOException ioe) { throw new JspException(ioe); }

    return EVAL_PAGE;
  }

  @Override
  protected String getType() { return "checkbox"; }
}
