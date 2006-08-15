package com.randomcoder.taglibs.url;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.TagSupport;

import com.randomcoder.taglibs.common.*;

/**
 * Tag class which modifies a URL.
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
public class ModifyTag extends TagSupport {
  private static final long serialVersionUID = 5868343381730564644L;
  
  private String var;
  private String scope;
  private String value;
  
  private PageContext pageContext;
  
  private URL targetURL;
  private boolean relative = false;
  private Map<String, List<String>> params = null; 
  
  /**
   * Sets the scope for the exported variable.
   * 
   * @param scope JSP scope (page, request, session, or application)
   */
  public void setScope(String scope) {
    this.scope = scope;
  }
  
  /**
   * Sets the name of the exported variable.
   *  
   * @param var variable name
   */
  public void setVar(String var) {
    this.var = var;
  }

  /**
   * Sets the value representing the URL to modify.
   * 
   * @param value URL
   */
  public void setValue(String value) {
    this.value = value;
  }
  
  /**
   * Sets the JSP PageContext variable.
   */
  @Override
  public void setPageContext(PageContext pageContext) {
    super.setPageContext(pageContext);
    this.pageContext = pageContext;
  }
  
  /**
   * Processes the start tag.
   */
  @Override
  public int doStartTag() throws JspException {
    try {
      if (scope != null && var == null)
        throw new JspException("var is required when scope is specified");
      
      HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
      
      URL currentPage = RequestHelper.getCurrentUrl(request);
      
      System.out.println("Current page: " + currentPage);
      
      if (value == null) {
        // no link specified, use current URL
        targetURL = currentPage;
        relative = true;
      } else {
        targetURL = new URL(currentPage, value);
        relative = UrlHelper.isUrlRelative(targetURL, currentPage);
      }
      
      // get current parameters
      params = RequestHelper.parseParameters(request, targetURL.getQuery());
      
      return EVAL_BODY_INCLUDE;
    } catch (MalformedURLException e) {
      throw new JspException(e);
    } catch (UnsupportedEncodingException e) {
      throw new JspException(e);
    }
  }

  /**
   * Release state.
   */
  @Override  
  public void release() {
    super.release();
    cleanup();
  }
  
  /**
   * Renders the modified URL to a variable or to the current writer.
   * @return EVAL_PAGE
   */
  @Override
  public int doEndTag() throws JspException {
    try {      
      HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
      HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
            
      URL generated = RequestHelper.setParameters(request, targetURL, params);
      
      String output = null;
      
      if (relative) {
        output = response.encodeURL(generated.getFile());
      } else {
        output = response.encodeURL(generated.toExternalForm());
      }
      
      if (var == null) {
        JspWriter out = pageContext.getOut();        
        out.write(output);
      } else {
        if (scope == null) {
          pageContext.setAttribute(var, output);
        } else {
          pageContext.setAttribute(var, output, getScope(scope));
        }
      }

      return EVAL_PAGE;
      
    } catch (IOException e) {
      throw new JspException(e);
    } finally {
      cleanup();
    }
  }
  
  /**
   * Removes parameter from the current URL.
   * 
   * @param paramName name of parameter to remove
   */
  void removeParameter(String paramName) {
    params.remove(paramName);
  }
  
  /**
   * Adds a parameter to the current URL.
   * 
   * @param paramName parameter name
   * @param paramValue parameter value
   */
  void addParameter(String paramName, String paramValue) {
    List<String> values = params.get(paramName);
    if (values == null) {
      values = new ArrayList<String>();
      params.put(paramName, values);
    }
    values.add(paramValue);
  }

  /**
   * Sets a parameter in the current URL.
   * 
   * @param paramName parameter name
   * @param paramValue parameter value
   */
  void setParameter(String paramName, String paramValue) {
    List<String> values = params.get(paramName);
    if (values == null) {
      values = new ArrayList<String>();
      params.put(paramName, values);
    }
    values.clear();
    values.add(paramValue);
  }  
  
  /**
   * Clears all parameters in the current URL.
   */
  void clearParameters() {
    params.clear();
  }
  
  private void cleanup() {
    var = null;
    scope = null;
    value = null;
    targetURL = null;
    relative = false;
    params = null;
    pageContext = null;
  }
    
  private int getScope(String scopeName) throws JspException {
    if (scopeName == null) throw new JspException("scope is null");
    
    String test = scopeName.toUpperCase(Locale.US);
    
    if ("PAGE".equals(test)) return PageContext.PAGE_SCOPE;
    if ("REQUEST".equals(test)) return PageContext.REQUEST_SCOPE;
    if ("SESSION".equals(test)) return PageContext.SESSION_SCOPE;
    if ("APPLICATION".equals(test)) return PageContext.APPLICATION_SCOPE;
    throw new JspException("Invalid scope: " + scopeName);
  }

}
