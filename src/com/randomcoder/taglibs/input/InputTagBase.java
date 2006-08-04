/*
 * $Id: InputTagBase.java 20 2005-02-09 20:13:51Z ccondit $
 */
package com.randomcoder.taglibs.input;

import java.io.IOException;
import java.util.*;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Base class of all tags which produce &lt;input&gt; tags.
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
abstract public class InputTagBase extends TagSupport implements ScriptableInputTagAttributes {
  
  private final Map<String, String> params = new HashMap<String, String>();

  private String styleId;
  private String name;
  private String value;

  /**
   * Sets the name HTML attribute.
   * @param name value of name attribute
   */
  public void setName(String name) { this.name = name; }
  
  /**
   * Sets the value HTML attribute.
   * @param value value of value attribute
   */
  public void setValue(String value) { this.value = value; }
  
  /**
   * Sets the styleId (HTML style) attribute.
   * @param styleId value of styleId attribute
   */
  public void setStyleId(String styleId) { this.styleId = styleId; }

  /**
   * Gets the value HTML attribute.
   * @return value attribute value
   */
  protected String getValue() { return value; }
  
  /**
   * Gets the name HTML attribute.
   * @return name attribute value
   */
  protected String getName() { return name; }
  
  /**
   * Gets the styleId (HTML style) attribute.
   * @return styleId attribute value
   */
  protected String getStyleId() { return styleId; }

  /**
   * Gets additional parameters associated with this tag.
   * @return parameter map
   */
  protected Map<String, String> getParams() { return params; }
    
  // attributes common to all INPUT tags
    
  public void setDisabled(String disabled) {
    boolean dis = false;
    if ("true".equalsIgnoreCase(disabled)) dis = true;
    if ("1".equalsIgnoreCase(disabled)) dis = true;
    if ("disabled".equalsIgnoreCase(disabled)) dis = true;

    if (dis)
      params.put("disabled", "disabled");
    else
      params.remove("disabled");
  }
  
  public void setReadonly(String readonly) {
    boolean ro = false;
    if ("true".equalsIgnoreCase(readonly)) ro = true;
    if ("1".equalsIgnoreCase(readonly)) ro = true;
    if ("readonly".equalsIgnoreCase(readonly)) ro = true;

    if (ro)
      params.put("readonly", "readonly");
    else
      params.remove("readonly");
  }
  
  public void setTabindex(String tabindex) { params.put("tabindex", tabindex); }  
  public void setAccesskey(String accesskey) { params.put("accesskey", accesskey); }
	
  // attributes common to all HTML tags
  
  public void setStyleClass(String styleClass) { params.put("class", styleClass); }
  public void setDir(String dir) { params.put("dir", dir); }
  public void setLang(String lang) { params.put("lang", lang); }
  public void setStyle(String style) { params.put("style", style); }
  public void setTitle(String title) { params.put("title", title); }

  // scripting attributes common to all INPUT tags

  public void setOnblur(String onblur) { params.put("onblur", onblur); }
  public void setOnchange(String onchange) { params.put("onchange", onchange); }
  public void setOnfocus(String onfocus) { params.put("onfocus", onfocus); }
  public void setOnselect(String onselect) { params.put("onselect", onselect); }

  // scripting attributes common to all HTML tags

  public void setOnclick(String onclick) { params.put("onclick", onclick); }
  public void setOndblclick(String ondblclick) { params.put("ondblclick", ondblclick); }
  public void setOnkeydown(String onkeydown) { params.put("onkeydown", onkeydown); }
  public void setOnkeypress(String onkeypress) { params.put("onkeypress", onkeypress); }
  public void setOnkeyup(String onkeyup) { params.put("onkeyup", onkeyup); }  
  public void setOnmousedown(String onmousedown) { params.put("onmousedown", onmousedown); }
  public void setOnmousemove(String onmousemove) { params.put("onmousemove", onmousemove); }
  public void setOnmouseout(String onmouseout) { params.put("onmouseout", onmouseout); }
  public void setOnmouseover(String onmouseover) { params.put("onmouseover", onmouseover); }
  public void setOnmouseup(String onmouseup) { params.put("onmouseup", onmouseup); }
	
  @Override
  public void release() {
    params.clear();
    name = null;
    value = null;
    styleId = null;
  }
		
  @Override
  public int doEndTag() throws JspException {
    try {
      JspWriter out = pageContext.getOut();

      out.write("<input type=\"" + getType() + "\"");
      if (name != null) out.write(" name=\"" + encodeAttribute(name) + "\"");
      if (styleId != null) out.write(" id=\"" + encodeAttribute(styleId) + "\"");
      if (value != null) out.write(" value=\"" + encodeAttribute(value) + "\"");
      out.write(buildOptions());
      out.write(" />");
    } catch (IOException ioe) { throw new JspException(ioe); }

    return EVAL_PAGE;
  }
	
  /**
   * Renders attribute values.
   * @return String holding attributes
   */
  protected String buildOptions() {
    StringBuilder buf = new StringBuilder();

    for (String key : params.keySet()) {
      String val = params.get(key);
      if (val != null) {
        buf.append(" ");
        buf.append(key);
        buf.append("=\"");
        buf.append(encodeAttribute(val));
        buf.append("\"");
      }
    }

    return buf.toString();
  }
	
  /**
   * Encodes PCDATA attributes.
   * @param pcData PCDATA value
   * @return encoded PCDATA value
   */
  protected String encodePCData(String pcData) {
    if (pcData == null) return "";

    StringBuilder buf = new StringBuilder();

    for (int i = 0; i < pcData.length(); i++) {
      char c = pcData.charAt(i);
      switch (c) {
      case '>': buf.append("&gt;"); break;
      case '<': buf.append("&lt;"); break;
      case '&': buf.append("&amp;"); break;
      default: buf.append(c);
      }
    }		
    return buf.toString();
  }

  /**
   * Encodes attribute values.
   * @param attributeValue value of attribute to encode
   * @return encoded value
   */
  protected String encodeAttribute(String attributeValue) {
    if (attributeValue == null) return "";

    StringBuilder buf = new StringBuilder();

    for (int i = 0; i < attributeValue.length(); i++) {
      char c = attributeValue.charAt(i);
      switch (c) {
      case '"': buf.append("&quot;"); break;
      case '>': buf.append("&gt;"); break;
      case '<': buf.append("&lt;"); break;
      case '&': buf.append("&amp;"); break;
      default: buf.append(c);
      }
    }		
    return buf.toString();
  }

  /**
   * Gets the value of the 'type' attribute used in final tag.
   * @return type attribute value
   */
  abstract protected String getType();	
}
