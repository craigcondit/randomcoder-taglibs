/*
 * $Id: OptionTag.java 20 2005-02-09 20:13:51Z ccondit $
 */
package com.randomcoder.taglibs.input;

import java.io.IOException;
import java.util.Collection;
import javax.servlet.jsp.*;

/**
 * Tag class which produces &lt;option&gt;.
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
public class OptionTag extends InputTagBase {
  private static final long serialVersionUID = 6545257704852434656L;
  
  private String text;
  private String label;
	
  @Override
  protected String getType() { return "option"; }

  /**
   * Sets the label HTML attribute.
   * @param label value of label attribute
   */
  public void setLabel(String label) { this.label = label; }
  
  /**
   * Sets the textual content of the &lt;option&gt; tag.
   * @param text value of body content
   */
  public void setText(String text) { this.text = text; }
	
  /**
   * Not used.
   * 
   * @throws IllegalArgumentException always
   */
  @Override
  public void setName(String name) throws IllegalArgumentException {
    throw new IllegalArgumentException("Attribute 'name' is not allowed");		
  }
  
  /**
   * Not used.
   * 
   * @throws IllegalArgumentException always
   */
  @Override
  public void setReadonly(String readonly) throws IllegalArgumentException {
    throw new IllegalArgumentException("Attribute 'readonly' is not allowed");		
  }
  
  /**
   * Not used.
   * 
   * @throws IllegalArgumentException always
   */
  @Override
  public void setTabindex(String tabindex) throws IllegalArgumentException {
    throw new IllegalArgumentException("Attribute 'tabindex' is not allowed");		
  }
  
  /**
   * Not used.
   * 
   * @throws IllegalArgumentException always
   */
  @Override
  public void setAccesskey(String accesskey) throws IllegalArgumentException {
    throw new IllegalArgumentException("Attribute 'accesskey' is not allowed");		
  }
  
  /**
   * Not used.
   * 
   * @throws IllegalArgumentException always
   */
  @Override
  public void setOnblur(String onblur) throws IllegalArgumentException {
    throw new IllegalArgumentException("Attribute 'onblur' is not allowed");		
  }
  
  /**
   * Not used.
   * 
   * @throws IllegalArgumentException always
   */
  @Override
  public void setOnchange(String onchange) throws IllegalArgumentException {
    throw new IllegalArgumentException("Attribute 'onchange' is not allowed");		
  }
  
  /**
   * Not used.
   * 
   * @throws IllegalArgumentException always
   */
  @Override
  public void setOnfocus(String onfocus) throws IllegalArgumentException {
    throw new IllegalArgumentException("Attribute 'onfocus' is not allowed");		
  }
  
  /**
   * Not used.
   * 
   * @throws IllegalArgumentException always
   */
  @Override
  public void setOnselect(String onselect) throws IllegalArgumentException {
    throw new IllegalArgumentException("Attribute 'onselect' is not allowed");		
  }

  @Override
  public void release() {
    super.release();
    label = null;
    text = null;
  }
  
  @Override
  public int doEndTag() throws JspException {
    try {
      JspWriter out = pageContext.getOut();

      SelectTag select = (SelectTag) findAncestorWithClass(this, SelectTag.class);
      if (select == null) throw new JspException("option tag must be nested inside a select tag");

      Collection<String> values = select.getCurrentValues();

      String testValue = getValue();
      if (testValue == null) testValue = text;			
      boolean selected = values.contains(testValue);

      out.write("<option");
      if (getStyleId() != null) out.write(" id=\"" + encodeAttribute(getStyleId()) + "\"");
      if (getValue() != null) out.write(" value=\"" + encodeAttribute(getValue()) + "\"");
      if (label != null) out.write(" label=\"" + encodeAttribute(label) + "\"");
      if (selected) out.write(" selected=\"selected\"");			
      out.write(buildOptions());
      out.write(">");
      out.write(encodePCData(text));
      out.write("</option>");
    } catch (IOException ioe) { throw new JspException(ioe); }

    return EVAL_PAGE;
  }
}
