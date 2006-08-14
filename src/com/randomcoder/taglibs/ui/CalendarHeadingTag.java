package com.randomcoder.taglibs.ui;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.util.Map.Entry;

import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Tag class which implements a calendar heading.
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
public class CalendarHeadingTag extends TagSupport {
  
  private static final long serialVersionUID = 8927306238278116620L;
  private static final String DEFAULT_CAPTION_FORMAT = "MMM yyyy";
  private static final String DEFAULT_PREV_CONTENT = "&#171;";
  private static final String DEFAULT_NEXT_CONTENT = "&#187;";
  
  private static final String REQUEST_URI_ATTRIBUTE = "javax.servlet.forward.request_uri";
  private static final String QUERY_STRING_ATTRIBUTE = "javax.servlet.forward.query_string";
  
  private PageContext pageContext;
  
  private Date showDate;
  
  private String prevClass;
  private String nextClass;
  private String prevLink;
  private String nextLink;
  private String prevId;
  private String nextId;
  private String prevTitle;
  private String nextTitle;
  private boolean encodePrevLink = true;
  private boolean encodeNextLink = true;
  private boolean capitalizeMonths = true;
  private String yearParam = "year";
  private String monthParam = "month";
  private boolean showPrevLink = true;
  private boolean showNextLink = true;
  private String captionFormat = DEFAULT_CAPTION_FORMAT;
  private String prevContent = DEFAULT_PREV_CONTENT;
  private String nextContent = DEFAULT_NEXT_CONTENT;
  private Locale locale;
  private ResourceBundle resourceBundle;
  private TimeZone timeZone;
  private DateFormatSymbols dateFormatSymbols;
  
  private final CalendarDaySpec[] daySpecs = new CalendarDaySpec[32];
        
  /**
   * Determins if month names should be capitalized (defaults to true).
   * 
   * @param capitalizeMonths true to capitalize, false otherwise
   */
  public void setCapitalizeMonths(boolean capitalizeMonths) {
    this.capitalizeMonths = capitalizeMonths;
  }

  /**
   * Sets the date format to use for table caption (defaults to 'MMM yyyy'). 
   * 
   * @param captionFormat date format to use
   */
  public void setCaptionFormat(String captionFormat) {
    this.captionFormat = captionFormat;
  }
  
  /**
   * Determines if year and month should be encoded into next link (defaults to
   * true).
   * 
   * @param encodeNextLink
   *          true if date should be encoded into next link, false otherwise
   */
  public void setEncodeNextLink(boolean encodeNextLink) {
    this.encodeNextLink = encodeNextLink;
  }

  /**
   * Determines if year and month should be encoded into previous link (defaults
   * to true).
   * 
   * @param encodePrevLink
   *          true if date should be encoded into previous link, false otherwise
   */
  public void setEncodePrevLink(boolean encodePrevLink) {
    this.encodePrevLink = encodePrevLink;
  }

  /**
   * Sets the name of the parameter to encode month information into for
   * navigational links (defaults to 'month').
   * 
   * @param monthParam parameter name
   */
  public void setMonthParam(String monthParam) {
    this.monthParam = monthParam;    
  }

  /**
   * Sets the CSS class to apply to the generated next link.
   * 
   * @param nextClass css class
   */
  public void setNextClass(String nextClass) {
    this.nextClass = nextClass;
  }

  /**
   * Sets custom content to use for the next link (defaults to a right
   * angle quote).
   * 
   * @param nextContent HTML content
   */
  public void setNextContent(String nextContent) {
    this.nextContent = nextContent;
  }

  /**
   * Sets the CSS id to apply to the generated next link.
   * 
   * @param nextId css id
   */
  public void setNextId(String nextId) {
    this.nextId = nextId;
  }
  
  /**
   * Sets the URL to use for the next link (defaults to the current page).
   * @param nextLink URL
   */
  public void setNextLink(String nextLink) {
    this.nextLink = nextLink;
  }

  /**
   * Sets the value of the title attribute to apply to the next link.
   * 
   * @param nextTitle title text
   */
  public void setNextTitle(String nextTitle) {
    this.nextTitle = nextTitle;     
  }
  
  /**
   * Sets the CSS class to apply to the generated previous link.
   * 
   * @param prevClass css class
   */
  public void setPrevClass(String prevClass) {
    this.prevClass = prevClass;
  }

  /**
   * Sets custom content to use for the previous link (defaults to a left
   * angle quote).
   * 
   * @param prevContent HTML content
   */
  public void setPrevContent(String prevContent) {
    this.prevContent = prevContent;
  }

  /**
   * Sets the CSS id to apply to the generated previous link.
   * 
   * @param prevId css id
   */
  public void setPrevId(String prevId) {
    this.prevId = prevId;
  }
  
  /**
   * Sets the URL to use for the previous link (defaults to the current page).
   * @param prevLink URL
   */
  public void setPrevLink(String prevLink) {
    this.prevLink = prevLink;
  }

  /**
   * Sets the value of the title attribute to apply to the previous link.
   * 
   * @param prevTitle title text
   */
  public void setPrevTitle(String prevTitle) {
    this.prevTitle = prevTitle;
  }

  /**
   * Sets the date to display a calendar for.
   * 
   * @param showDate date to display calendar for, or null for today's date
   */
  public void setShowDate(Date showDate) {
    this.showDate = showDate;
  }

  /**
   * Determines if next navigational link should be displayed (defaults to 
   * true).
   * 
   * @param showNextLink true to show next link, false otherwise
   */
  public void setShowNextLink(boolean showNextLink) {
    this.showNextLink = showNextLink;
  }

  /**
   * Determines if previous navigational link should be displayed (defaults to 
   * true).
   * 
   * @param showPrevLink true to show previous link, false otherwise
   */
  public void setShowPrevLink(boolean showPrevLink) {
    this.showPrevLink = showPrevLink;
  }

  /**
   * Sets the name of the parameter to encode year information into for
   * navigational links (defaults to 'year').
   * 
   * @param yearParam parameter name
   */
  public void setYearParam(String yearParam) {
    this.yearParam = yearParam;
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
   * Used by CalendarDayTag to apply customization to day rendering.
   * 
   * @param day day to apply the spec to, or null for default
   * @param spec day-specific parameters to apply
   */
  void setDaySpec(Integer day, CalendarDaySpec spec) {
    if (day == null) day = 0;
    if (day < 0) return;
    if (day > 31) return;
    daySpecs[day] = spec;
  }
  
  /**
   * Release state.
   */
  @Override  
  public void release() {
    super.release();
    cleanup();
  }
  
  private void cleanup() {
    showDate = null;    
    prevClass = null;
    nextClass = null;
    locale = null;
    resourceBundle = null;
    timeZone = null;
    captionFormat = DEFAULT_CAPTION_FORMAT;
    dateFormatSymbols = null;
    prevContent = DEFAULT_PREV_CONTENT;
    nextContent = DEFAULT_NEXT_CONTENT;    
    prevLink = null;
    nextLink = null;
    showPrevLink = true;
    showNextLink = true;
    monthParam = "month";
    yearParam = "year";
    prevTitle = null;
    nextTitle = null;
    encodePrevLink = true;
    encodeNextLink = true;
    capitalizeMonths = true;
    prevId = null;
    nextId = null;
    
    for (int i = 0; i < 31; i++) daySpecs[i] = null;
    
    pageContext = null;
  }

  /**
   * Renders the calendar to the current page context's JSPWriter.
   * @return EVAL_PAGE
   */
  @Override
  public int doEndTag() throws JspException {
    try {      
      JspWriter out = pageContext.getOut();

      // get locale-specific stuff
      locale = getDefaultLocale();      
      if (resourceBundle == null) resourceBundle = getDefaultResourceBundle();
      if (timeZone == null) timeZone = getDefaultTimeZone();
      if (dateFormatSymbols == null) dateFormatSymbols = getDateFormatSymbols();
            
      Date currentTime = new Date();
      
      if (showDate == null) showDate = new Date(currentTime.getTime());
                  
      Calendar current = Calendar.getInstance(timeZone, locale);
      current.setTime(showDate);
      
      Calendar prevCal = Calendar.getInstance(timeZone, locale);
      prevCal.setTime(current.getTime());
      prevCal.add(Calendar.MONTH, -1);    
      
      Calendar nextCal = Calendar.getInstance(timeZone, locale);
      nextCal.setTime(current.getTime());
      nextCal.add(Calendar.MONTH, 1);    
      
      renderPrevLink(out, prevCal.get(Calendar.YEAR), prevCal.get(Calendar.MONTH) + 1);
      renderCaption(out, current);
      renderNextLink(out, nextCal.get(Calendar.YEAR), nextCal.get(Calendar.MONTH) + 1);
      
      return EVAL_PAGE;
      
    } catch (IOException e) {
      throw new JspException(e);
    } finally {
      cleanup();
    }
  }
  
  private Locale getDefaultLocale() {
    Locale result = (Locale) Config.find(pageContext, Config.FMT_LOCALE);
    if (result == null) result = Locale.getDefault();
    return result;
  }
  
  private ResourceBundle getDefaultResourceBundle() {
    LocalizationContext result = (LocalizationContext) Config.find(pageContext, Config.FMT_LOCALIZATION_CONTEXT);
    if (result == null) return null; // this is bad :(
    return result.getResourceBundle();
  }
  
  private TimeZone getDefaultTimeZone() {
    TimeZone result = (TimeZone) Config.find(pageContext, Config.FMT_TIME_ZONE);
    if (result == null) result = TimeZone.getDefault();
    return result;
  }
  
  private void renderPrevLink(JspWriter out, int year, int month) throws IOException {
    renderNavLink(out, year, month, showPrevLink, encodePrevLink, prevLink, prevTitle, prevClass, prevId, prevContent);
    out.print(" ");
  }

  private void renderNextLink(JspWriter out, int year, int month) throws IOException {
    out.print(" ");
    renderNavLink(out, year, month, showNextLink, encodeNextLink, nextLink, nextTitle, nextClass, nextId, nextContent);
  }

  private void renderNavLink(JspWriter out, int year, int month, boolean showLink, boolean encodeLink, String link, String title, String navClass, String navId, String text) throws IOException {
    
    if (showLink) {
      HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
      HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

      URL targetURL = null;
      String output = null;
      boolean relative = false;
      
      URL currentPage = getCurrentUrl(request);
      
      if (link == null) {
        // no link specified, use current URL
        targetURL = currentPage;
        relative = true;
      } else if (encodeLink) {
        targetURL = new URL(currentPage, link);
        relative = isURLRelative(targetURL, currentPage);
      }
      
      if (encodeLink) {
        // add month-day-year
        DecimalFormat df = new DecimalFormat("####");        
        Map<String, String> params = new HashMap<String, String>();
        params.put(monthParam, df.format(month));
        params.put(yearParam, df.format(year));
        URL generated = appendParameters(targetURL, params);
        
        if (relative) {
          output = response.encodeURL(generated.getFile());
        } else {
          output = response.encodeURL(generated.toExternalForm());
        }
      } else {
        // using URL as-is (maybe javascript, etc.)
        output = link;
      }
      
      // write it out
      out.print("<a href=\"");
      out.print(encodeAttribute(output));
      out.print("\"");
      if (navId != null) {
        out.print(" id=\"");
        out.print(encodeAttribute(navId));
        out.print("\"");
      }
      if (navClass != null) {
        out.print(" class=\"");
        out.print(encodeAttribute(navClass));
        out.print("\"");
      }
      if (title != null) {
        out.print(" title=\"");
        out.print(encodeAttribute(title));
        out.print("\"");
      }
      out.print(">");
      
    } else {
      out.print("<span");
      if (navId != null) {
        out.print(" id=\"");
        out.print(encodeAttribute(navId));
        out.print("\"");
      }
      if (navClass != null) {
        out.print(" class=\"");
        out.print(encodeAttribute(navClass));
        out.print("\"");
      }      
      out.print(">");
    }
    
    out.print(text);
    
    if (showLink) {
      out.print("</a>");
    } else {
      out.print("</span>");
    }
  }
  
  private Map<String, List<String>> parseParameters(String query)
  throws UnsupportedEncodingException {
    
    // get the request encoding
    HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
    String encoding = request.getCharacterEncoding();
    
    // not found, fallback to UTF-8
    if (encoding == null) encoding = "UTF-8";
    
    Map<String, List<String>> data = new HashMap<String, List<String>>();
    
    // query not specified
    if (query == null) return data;
    
    StringTokenizer st = new StringTokenizer(query, "?&=", true);
    
    String prev = null;
    while (st.hasMoreTokens())
    {
       String tk = st.nextToken();
       if ("?".equals(tk)) continue;
       if ("&".equals(tk)) continue;
       if ("=".equals(tk)) {
         if (prev == null) continue; // no previous entry...
         if (!st.hasMoreTokens()) continue; // no more data
         
         String key = URLDecoder.decode(prev, encoding);
         String value = URLDecoder.decode(st.nextToken(), encoding);
         
         List<String> params = data.get(key);
         if (params == null) {
           params = new ArrayList<String>();
           data.put(key, params);
         }
         params.add(value);
         
         prev = null;
       } else {
         // this is a key
         prev = tk;
       }
    }
    return data;
  }
  
  private void renderCaption(JspWriter out, Calendar cal) throws IOException {
    SimpleDateFormat sdfTitle = new SimpleDateFormat(captionFormat, locale);
    sdfTitle.setDateFormatSymbols(dateFormatSymbols);
    out.print(encodePCData(sdfTitle.format(cal.getTime())));
  }
  
  private URL appendParameters(URL url, Map<String, String> additionalParams)
  throws UnsupportedEncodingException, MalformedURLException {
    
    // get the request encoding
    HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
    String encoding = request.getCharacterEncoding();
    
    // not found, fallback to UTF-8
    if (encoding == null) encoding = "UTF-8";
    
    String queryString = url.getQuery();
    String path = url.getPath();

    Map<String, List<String>> params = parseParameters(queryString);

    // merge new parameters 
    for (Entry<String, String> entry : additionalParams.entrySet()) {
      List<String> values = new ArrayList<String>(1);
      values.add(entry.getValue());
      params.put(entry.getKey(), values);
    }
      
    // convert to query string
    StringBuilder queryBuf = new StringBuilder();
    for (Entry<String, List<String>> entry : params.entrySet()) {
      String key = entry.getKey();
      for (String value : entry.getValue()) {
        if (queryBuf.length() > 0) {
          queryBuf.append("&");
        }
        queryBuf.append(URLEncoder.encode(key, encoding));
        queryBuf.append("=");
        queryBuf.append(URLEncoder.encode(value, encoding));
      }
    }
    
    queryString = queryBuf.toString();
    
    if (queryString.length() > 0) path += "?" + queryString;
    
    URL result = new URL(url.getProtocol(), url.getHost(), url.getPort(), path);
    
    return result;
  }
  
  private URL getCurrentUrl(HttpServletRequest request)
  throws MalformedURLException {
    StringBuilder buf = new StringBuilder();
    
    String scheme = request.getScheme();
    int port = request.getServerPort();
    
    buf.append(scheme);
    buf.append("://");
    buf.append(request.getServerName());
    
    if ("http".equals(scheme) && port == 80) {
      // do nothing
    } else if ("https".equals(scheme) && port == 443) {
      // do nothing
    } else if (port < 0) {
      // do nothing
    } else {
      buf.append(":");
      DecimalFormat dfPort = new DecimalFormat("#####");
      buf.append(dfPort.format(port));
    }
    
    String forwardUri = (String) request.getAttribute(REQUEST_URI_ATTRIBUTE);
    if (forwardUri == null) {
      // use old style method
      String contextPath = request.getContextPath();
      if (contextPath != null)
        buf.append(contextPath);
      
      String servletPath = request.getServletPath();
      if (servletPath != null)
        buf.append(servletPath);
      
      String pathInfo = request.getPathInfo();
      if (pathInfo != null)
        buf.append(pathInfo);
      
      String queryString = request.getQueryString();
      if (queryString != null && queryString.length() > 0) {
        if (!('?' == queryString.charAt(0))) buf.append("?");
        buf.append(queryString);
      }      
    } else {
      // use forwarded attributes
      buf.append(forwardUri);
      
      String queryString = (String) request.getAttribute(QUERY_STRING_ATTRIBUTE);
      if (queryString != null && queryString.length() > 0) {
        if (!('?' == queryString.charAt(0))) buf.append("?");
        buf.append(queryString);
      }      
    }
    
    String name = buf.toString();
    return new URL(name);
  }
  
  private DateFormatSymbols getDateFormatSymbols() {
    DateFormatSymbols dfs = new DateFormatSymbols(locale);

    // capitalize months
    if (capitalizeMonths) {
      String[] months = dfs.getMonths();
      for (int i = 0; i < months.length; i++) {
        if (months[i].length() > 0) {
          months[i] = months[i].substring(0,1).toUpperCase(locale) + months[i].substring(1);
        }
      }
      dfs.setMonths(months);
      
      String[] shortMonths = dfs.getShortMonths();
      for (int i = 0; i < shortMonths.length; i++) {
        if (shortMonths[i].length() > 0) {
          shortMonths[i] = shortMonths[i].substring(0,1).toUpperCase(locale) + shortMonths[i].substring(1);
        }
      }
      dfs.setShortMonths(shortMonths);
    }
    
    return dfs;
  }
  
  private boolean isURLRelative(URL url1, URL url2) {
    if (url1 == null || url2 == null) return false;
        
    String protocol1 = url1.getProtocol();
    String protocol2 = url2.getProtocol();
    if (!protocol1.equals(protocol2)) return false;
    
    int port1 = url1.getPort();
    int port2 = url2.getPort();
    
    if ("http".equals(protocol1)) {
      if (port1 < 0) port1 = 80;
      if (port2 < 0) port2 = 80;
    }

    if ("https".equals(protocol1)) {
      if (port1 < 0) port1 = 443;
      if (port2 < 0) port2 = 443;
    }
    
    if (port1 != port2) return false;
    
    String host1 = url1.getHost();
    String host2 = url2.getHost();
    if (!host1.equals(host2)) return false;
    
    return true;
  }
  
  /**
   * Encodes PCDATA attributes.
   * @param pcData PCDATA value
   * @return encoded PCDATA value
   */
  private String encodePCData(String pcData) {
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
  private String encodeAttribute(String attributeValue) {
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
}
