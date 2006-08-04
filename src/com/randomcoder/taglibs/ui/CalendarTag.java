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
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Tag class which implements a calendar control.
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
public class CalendarTag extends BodyTagSupport {
  
  private static final long serialVersionUID = 8927306238278116620L;
  private static final String DEFAULT_CAPTION_FORMAT = "MMM yyyy";
  private static final String DEFAULT_BUNDLE_PREFIX = CalendarTag.class.getName() + ".";
  private static final String DEFAULT_PREV_CONTENT = "&#171;";
  private static final String DEFAULT_NEXT_CONTENT = "&#187;";
  
  private static final String REQUEST_URI_ATTRIBUTE = "javax.servlet.forward.request_uri";
  private static final String QUERY_STRING_ATTRIBUTE = "javax.servlet.forward.query_string";
  
  private final DecimalFormat dfDay = new DecimalFormat("##");
  
  private PageContext pageContext;
  
  private Date showDate;
  private Date selectedDate;
  
  private String outerClass;
  private String outerId;
  private String todayClass;
  private String selectedClass;
  private String weekendClass;
  private String prevClass;
  private String nextClass;
  private String prevLink;
  private String nextLink;
  private String captionFormat = DEFAULT_CAPTION_FORMAT;
  private boolean captionVisible = true;
  private String bundlePrefix = DEFAULT_BUNDLE_PREFIX;
  private String prevContent = DEFAULT_PREV_CONTENT;
  private String nextContent = DEFAULT_NEXT_CONTENT;
  private Locale locale;
  private ResourceBundle resourceBundle;
  private TimeZone timeZone;
  private DateFormatSymbols dateFormatSymbols;
  private final CalendarDaySpec[] daySpecs = new CalendarDaySpec[32];
                          
  public void setShowDate(Date showDate) {
    this.showDate = showDate;
  }
  
  public void setOuterId(String outerId) {
    this.outerId = outerId;
  }

  public void setSelectedClass(String selectedClass) {
    this.selectedClass = selectedClass;
  }

  public void setSelectedDate(Date selectedDate) {
    this.selectedDate = selectedDate;
  }

  public void setTodayClass(String todayClass) {
    this.todayClass = todayClass;
  }

  public void setWeekendClass(String weekendClass) {
    this.weekendClass = weekendClass;
  }

  public void setOuterClass(String outerClass) {
    this.outerClass = outerClass;
  }
  
  public void setCaptionFormat(String captionFormat) {
    this.captionFormat = captionFormat;
  }
  
  public void setCaptionVisible(boolean captionVisible) {
    this.captionVisible = captionVisible;
  }
  
  public void setBundlePrefix(String bundlePrefix) {
    this.bundlePrefix = bundlePrefix;
  }
  
  public void setPrevClass(String prevClass) {
    this.prevClass = prevClass;
  }
  
  public void setNextClass(String nextClass) {
    this.nextClass = nextClass;
  }
  
  @Override
  public void setPageContext(PageContext pageContext) {
    super.setPageContext(pageContext);
    this.pageContext = pageContext;
  }

  void setDaySpec(Integer day, CalendarDaySpec spec) {
    if (day == null) day = 0;
    if (day < 0) return;
    if (day > 31) return;
    daySpecs[day] = spec;
  }
  
  @Override  
  public void release() {
    super.release();
    cleanup();
  }
  
  private void cleanup() {
    showDate = null;    
    selectedDate = null;
    outerId = null;
    outerClass = null;
    todayClass = null;
    selectedClass = null;
    weekendClass = null;
    prevClass = null;
    nextClass = null;
    locale = null;
    resourceBundle = null;
    timeZone = null;
    captionFormat = DEFAULT_CAPTION_FORMAT;
    captionVisible = true;
    bundlePrefix = DEFAULT_BUNDLE_PREFIX;
    dateFormatSymbols = null;
    prevContent = DEFAULT_PREV_CONTENT;
    nextContent = DEFAULT_NEXT_CONTENT;    
    prevLink = null;
    nextLink = null;
    
    for (int i = 0; i < 31; i++) daySpecs[i] = null;
    
    pageContext = null;
  }

  @Override
  public int doAfterBody() throws JspException {
    // this evaluates the body, but doesn't write it out
    return SKIP_BODY;
  }

  @Override
  public int doEndTag() throws JspException {
    try {      
      JspWriter out = pageContext.getOut();

      // get locale-specific stuff
      locale = getDefaultLocale();      
      if (resourceBundle == null) resourceBundle = getDefaultResourceBundle();
      if (timeZone == null) timeZone = getDefaultTimeZone();
      if (dateFormatSymbols == null) dateFormatSymbols = getDefaultDateFormatSymbols();
      
      
      Date currentTime = new Date();
      
      Calendar selected = null;
      if (selectedDate != null) {
        selected = Calendar.getInstance(timeZone, locale);
        selected.setTime(selectedDate);
      }
      
      if (showDate == null) showDate = new Date(currentTime.getTime());
                  
      Calendar now = Calendar.getInstance(timeZone, locale);      
      now.setTime(currentTime);
            
      Calendar current = Calendar.getInstance(timeZone, locale);
      current.setTime(showDate);
      out.print("<table");
      if (outerId != null) {
        out.print(" id=\"");
        out.print(encodeAttribute(outerId));
        out.print("\"");
      }
      if (outerClass != null) {
        out.print(" class=\"");
        out.print(encodeAttribute(outerClass));
        out.print("\"");
      }
      out.print(">");

      if (captionVisible) {
        if (prevLink == null) prevLink = getDefaultPrevLink(current);
        if (nextLink == null) nextLink = getDefaultNextLink(current);
        
        out.print("<caption>");
        renderPrevLink(out);
        renderCaption(out, current);
        renderNextLink(out);
        out.print("</caption>");
      }
      out.print("<thead>");
      renderHead(out);
      out.print("</thead>");
      out.print("<tbody>");
      
      // render cells
      current.set(Calendar.DAY_OF_MONTH, 1);
      
      // get last day of month
      current.add(Calendar.MONTH, 1);
      current.set(Calendar.DAY_OF_MONTH, 1);
      current.add(Calendar.DAY_OF_MONTH, -1);    
      int endDay = current.get(Calendar.DAY_OF_MONTH);
      
      // go to first day of month
      current.set(Calendar.DAY_OF_MONTH, 1);

      int col = 0;
      out.print("<tr>");
      int firstDow = current.getFirstDayOfWeek();
      
      for (int i = 1; i <= endDay; i++) {
        int dow = current.get(Calendar.DAY_OF_WEEK);
        
        if (col == 7) {
          col = 0;
          out.print("</tr>");
          out.print("<tr>");
        }
        
        if (i == 1) {       
          // populate first row with empty cells
          int skipDays = (dow + 7 - firstDow) % 7; // should always give 0..6
          for (int j = 0; j < skipDays; j++) {
            renderEmptyDay(out, (firstDow + j) % 7);
            col++;
          }
        }
        
        // add current day      
        col++;
        
        renderDay(out, current, dow, isSameDay(current, now), isSameDay(current, selected));
              
        if (i == endDay) {
          // populate last row with empty cells
          while (col < 7) {
            current.add(Calendar.DAY_OF_MONTH, 1);
            renderEmptyDay(out, current.get(Calendar.DAY_OF_WEEK));
            col++;
          }
        }
        
        // move to next day
        current.add(Calendar.DAY_OF_MONTH, 1);
      }      
      out.print("</tr>");
      
      out.print("</tbody>");
      out.print("</table>");
      
      return EVAL_PAGE;
      
    } catch (IOException e) {
      throw new JspException(e);
    } finally {
      cleanup();
    }
  }
  
  private boolean isSameDay(Calendar cal1, Calendar cal2) {
    if (cal1 == null || cal2 == null) return false;
    if (cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR)) return false;
    if (cal1.get(Calendar.MONTH) != cal2.get(Calendar.MONTH)) return false;
    if (cal1.get(Calendar.DAY_OF_MONTH) != cal2.get(Calendar.DAY_OF_MONTH)) return false;
    return true;    
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
  
  private void renderPrevLink(JspWriter out) throws IOException {
    if (prevLink == null) return;
    
    out.write("<a href=\"");
    out.write(encodeAttribute(prevLink));
    out.write("\"");
    if (prevClass != null) {
      out.write(" class=\"");
      out.write(encodeAttribute(prevClass));
      out.write("\"");
    }
    out.write(">");
    out.write(prevContent);
    out.write("</a> ");
  }
  
  private void renderNextLink(JspWriter out) throws IOException {
    if (nextLink == null) return;
    
    out.write(" <a href=\"");
    out.write(encodeAttribute(nextLink));
    out.write("\"");
    if (nextClass != null) {
      out.write(" class=\"");
      out.write(encodeAttribute(nextClass));
      out.write("\"");
    }
    out.write(">");
    out.write(nextContent);
    out.write("</a>");
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
  
  private void renderHead(JspWriter out) throws IOException {
    out.print("<tr>");
    
    Calendar cal = Calendar.getInstance(timeZone, locale);
    cal.setTime(new Date());
    while (cal.getFirstDayOfWeek() != cal.get(Calendar.DAY_OF_WEEK)) {
      cal.add(Calendar.DAY_OF_MONTH, 1);
    }
          
    String[] weekdays = dateFormatSymbols.getWeekdays();
    String[] shortdays = dateFormatSymbols.getShortWeekdays();
    
    int[] indices = new int[7];
    for (int i = 0; i < 7; i++) {
      indices[i] = cal.get(Calendar.DAY_OF_WEEK);
      cal.add(Calendar.DAY_OF_MONTH, 1);
    }
        
    for (int j = 0; j < 7; j++) {
      String heading = weekdays[indices[j]];
      String shortHeading = shortdays[indices[j]];
      renderDayHeading(out, shortHeading, heading, indices[j]);
    }
    
    out.print("</tr>");
  }
  
  private String getDefaultPrevLink(Calendar current) throws MalformedURLException, UnsupportedEncodingException {
    Calendar prevCal = Calendar.getInstance(timeZone, locale);
    prevCal.setTime(current.getTime());
    prevCal.add(Calendar.MONTH, -1);    
    return getDefaultNavLink(prevCal);
  }
  
  private String getDefaultNextLink(Calendar current) throws MalformedURLException, UnsupportedEncodingException {
    Calendar nextCal = Calendar.getInstance(timeZone, locale);    
    nextCal.setTime(current.getTime());
    nextCal.add(Calendar.MONTH, 1);    
    return getDefaultNavLink(nextCal);
  }
  
  private String getDefaultNavLink(Calendar cal) throws MalformedURLException, UnsupportedEncodingException {
    HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
    
    DecimalFormat dfMonth = new DecimalFormat("##");
    DecimalFormat dfYear = new DecimalFormat("####");
    
    Map<String, String> params = new HashMap<String, String>();
    params.put("month", dfMonth.format(cal.get(Calendar.MONTH) + 1));
    params.put("year", dfYear.format(cal.get(Calendar.YEAR)));
    
    URL url = getCurrentUrl(request); // this will be relative
    
    HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
    
    URL completeUrl = appendParameters(url, params);
    
    // make site-relative
    String file = completeUrl.getFile();
    if (!file.startsWith("/")) file = "/" + file;
    
    return response.encodeURL(file);
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
  
  private DateFormatSymbols getDefaultDateFormatSymbols() {
    DateFormatSymbols dfs = new DateFormatSymbols(locale);
    
    // set weekdays
    String[] origWeekdays = dfs.getWeekdays();
    String[] weekdays = new String[origWeekdays.length];
    
    weekdays[Calendar.SUNDAY] = getResource(bundlePrefix + "SUNDAY", origWeekdays[Calendar.SUNDAY]);
    weekdays[Calendar.MONDAY] = getResource(bundlePrefix + "MONDAY", origWeekdays[Calendar.MONDAY]);
    weekdays[Calendar.TUESDAY] = getResource(bundlePrefix + "TUESDAY", origWeekdays[Calendar.TUESDAY]);
    weekdays[Calendar.WEDNESDAY] = getResource(bundlePrefix + "WEDNESDAY", origWeekdays[Calendar.WEDNESDAY]);
    weekdays[Calendar.THURSDAY] = getResource(bundlePrefix + "THURSDAY", origWeekdays[Calendar.THURSDAY]);
    weekdays[Calendar.FRIDAY] = getResource(bundlePrefix + "FRIDAY", origWeekdays[Calendar.FRIDAY]);
    weekdays[Calendar.SATURDAY] = getResource(bundlePrefix + "SATURDAY", origWeekdays[Calendar.SATURDAY]);
    
    dfs.setWeekdays(weekdays);
    
    // set short weekdays
    String[] origShortWeekdays = dfs.getShortWeekdays();
    String[] shortWeekdays = new String[origShortWeekdays.length];
    
    shortWeekdays[Calendar.SUNDAY] = getResource(bundlePrefix + "SUNDAY_SHORT", origShortWeekdays[Calendar.SUNDAY]);
    shortWeekdays[Calendar.MONDAY] = getResource(bundlePrefix + "MONDAY_SHORT", origShortWeekdays[Calendar.MONDAY]);
    shortWeekdays[Calendar.TUESDAY] = getResource(bundlePrefix + "TUESDAY_SHORT", origShortWeekdays[Calendar.TUESDAY]);
    shortWeekdays[Calendar.WEDNESDAY] = getResource(bundlePrefix + "WEDNESDAY_SHORT", origShortWeekdays[Calendar.WEDNESDAY]);
    shortWeekdays[Calendar.THURSDAY] = getResource(bundlePrefix + "THURSDAY_SHORT", origShortWeekdays[Calendar.THURSDAY]);
    shortWeekdays[Calendar.FRIDAY] = getResource(bundlePrefix + "FRIDAY_SHORT", origShortWeekdays[Calendar.FRIDAY]);
    shortWeekdays[Calendar.SATURDAY] = getResource(bundlePrefix + "SATURDAY_SHORT", origShortWeekdays[Calendar.SATURDAY]);
    
    dfs.setShortWeekdays(shortWeekdays);
    
    // set months
    String[] origMonths = dfs.getMonths();
    String[] months = new String[origMonths.length];
    
    months[Calendar.JANUARY] = getResource(bundlePrefix + "JANUARY", origMonths[Calendar.JANUARY]);
    months[Calendar.FEBRUARY] = getResource(bundlePrefix + "FEBRUARY", origMonths[Calendar.FEBRUARY]);
    months[Calendar.MARCH] = getResource(bundlePrefix + "MARCH", origMonths[Calendar.MARCH]);
    months[Calendar.APRIL] = getResource(bundlePrefix + "APRIL", origMonths[Calendar.APRIL]);
    months[Calendar.MAY] = getResource(bundlePrefix + "MAY", origMonths[Calendar.MAY]);
    months[Calendar.JUNE] = getResource(bundlePrefix + "JUNE", origMonths[Calendar.JUNE]);
    months[Calendar.JULY] = getResource(bundlePrefix + "JULY", origMonths[Calendar.JULY]);
    months[Calendar.AUGUST] = getResource(bundlePrefix + "AUGUST", origMonths[Calendar.AUGUST]);
    months[Calendar.SEPTEMBER] = getResource(bundlePrefix + "SEPTEMBER", origMonths[Calendar.SEPTEMBER]);
    months[Calendar.OCTOBER] = getResource(bundlePrefix + "OCTOBER", origMonths[Calendar.OCTOBER]);
    months[Calendar.NOVEMBER] = getResource(bundlePrefix + "NOVEMBER", origMonths[Calendar.NOVEMBER]);
    months[Calendar.DECEMBER] = getResource(bundlePrefix + "DECEMBER", origMonths[Calendar.DECEMBER]);
    
    if (months.length > Calendar.UNDECIMBER)
      months[Calendar.UNDECIMBER] = getResource(bundlePrefix + "UNDECIMBER", origMonths[Calendar.UNDECIMBER]);
    
    dfs.setMonths(months);

    // set short months
    String[] origShortMonths = dfs.getShortMonths();
    String[] shortMonths = new String[origShortMonths.length];
    
    shortMonths[Calendar.JANUARY] = getResource(bundlePrefix + "JANUARY_SHORT", origShortMonths[Calendar.JANUARY]);
    shortMonths[Calendar.FEBRUARY] = getResource(bundlePrefix + "FEBRUARY_SHORT", origShortMonths[Calendar.FEBRUARY]);
    shortMonths[Calendar.MARCH] = getResource(bundlePrefix + "MARCH_SHORT", origShortMonths[Calendar.MARCH]);
    shortMonths[Calendar.APRIL] = getResource(bundlePrefix + "APRIL_SHORT", origShortMonths[Calendar.APRIL]);
    shortMonths[Calendar.MAY] = getResource(bundlePrefix + "MAY_SHORT", origShortMonths[Calendar.MAY]);
    shortMonths[Calendar.JUNE] = getResource(bundlePrefix + "JUNE_SHORT", origShortMonths[Calendar.JUNE]);
    shortMonths[Calendar.JULY] = getResource(bundlePrefix + "JULY_SHORT", origShortMonths[Calendar.JULY]);
    shortMonths[Calendar.AUGUST] = getResource(bundlePrefix + "AUGUST_SHORT", origShortMonths[Calendar.AUGUST]);
    shortMonths[Calendar.SEPTEMBER] = getResource(bundlePrefix + "SEPTEMBER_SHORT", origShortMonths[Calendar.SEPTEMBER]);
    shortMonths[Calendar.OCTOBER] = getResource(bundlePrefix + "OCTOBER_SHORT", origShortMonths[Calendar.OCTOBER]);
    shortMonths[Calendar.NOVEMBER] = getResource(bundlePrefix + "NOVEMBER_SHORT", origShortMonths[Calendar.NOVEMBER]);
    shortMonths[Calendar.DECEMBER] = getResource(bundlePrefix + "DECEMBER_SHORT", origShortMonths[Calendar.DECEMBER]);
    
    if (shortMonths.length > Calendar.UNDECIMBER)
      shortMonths[Calendar.UNDECIMBER] = getResource(bundlePrefix + "UNDECIMBER_SHORT", origShortMonths[Calendar.UNDECIMBER]);
    
    dfs.setShortMonths(shortMonths);
    
    // done
    return dfs;
  }
  
  private String getResource(String key, String defaultValue) {
    if (resourceBundle == null) return defaultValue;
    try {
      return resourceBundle.getString(key);
    } catch (MissingResourceException e) {
      return defaultValue;
    }
  }
  
  private void renderEmptyDay(JspWriter out, int dayOfWeek) throws IOException {    
    out.print("<td");
    if ((dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) && weekendClass != null) {
      out.print(" class=\"");
      out.print(encodeAttribute(weekendClass));
      out.print("\"");
    }
    out.print(">");
    out.print("&#160;");
    out.print("</td>");
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
  
  private void renderDay(JspWriter out, Calendar current, int dayOfWeek, boolean isToday, boolean isSelected) throws IOException {
    
    CalendarDaySpec daySpec = daySpecs[current.get(Calendar.DAY_OF_MONTH)];
    CalendarDaySpec defSpec = daySpecs[0];
    
    // get link generation details
    boolean showLink = true;
    boolean encodeLink = true;
    String link = null;
    String monthParam = "month";
    String dayParam = "day";
    String yearParam = "year";
    String title = null;
    String content = null;
    String dayClass = null;
    String dayId = null;
    
    if (defSpec != null) {
      Boolean defShowLink = defSpec.isShowLink();
      if (defShowLink != null) showLink = defShowLink;
      
      Boolean defEncodeLink = defSpec.isEncodeLink();
      if (defEncodeLink != null) encodeLink = defEncodeLink;
      
      String defLink = defSpec.getLink();
      if (defLink != null) link = defLink;
      
      String defMonthParam = defSpec.getMonthParam();
      if (defMonthParam != null) monthParam = defMonthParam;
      
      String defDayParam = defSpec.getDayParam();
      if (defDayParam != null) dayParam = defDayParam;
      
      String defYearParam = defSpec.getYearParam();
      if (defYearParam != null) yearParam = defYearParam;
      
      String defTitle = defSpec.getTitle();
      if (defTitle != null) title = defTitle;
      
      String defDayClass = defSpec.getDayClass();
      if (defDayClass != null) dayClass = defDayClass;
    }
    
    if (daySpec != null) {
      Boolean dayShowLink = daySpec.isShowLink();
      if (dayShowLink != null) showLink = dayShowLink;
      
      Boolean dayEncodeLink = daySpec.isEncodeLink();
      if (dayEncodeLink != null) encodeLink = dayEncodeLink;
      
      String dayLink = daySpec.getLink();
      if (dayLink != null) link = dayLink;
      
      String dayMonthParam = daySpec.getMonthParam();
      if (dayMonthParam != null) monthParam = dayMonthParam;
      
      String dayDayParam = daySpec.getDayParam();
      if (dayDayParam != null) dayParam = dayDayParam;
      
      String dayYearParam = daySpec.getYearParam();
      if (dayYearParam != null) yearParam = dayYearParam;
      
      String dayTitle = daySpec.getTitle();
      if (dayTitle != null) title = dayTitle;
      
      String dayContent = daySpec.getContent();
      if (dayContent != null) content = dayContent;
      
      String dayDayClass = daySpec.getDayClass();
      if (dayDayClass != null) dayClass = dayDayClass;
      
      String dayDayId = daySpec.getDayId();
      if (dayDayId != null) dayId = dayDayId;
    }
    
    out.print("<td");
    
    if (dayId != null) {
      out.print(" id=\"");
      out.print(encodeAttribute(dayId));
      out.print("\"");
    }
    
    String classes = "";
    
    if (dayClass != null) {
      classes += " " + encodeAttribute(dayClass.trim());
    }
    
    // add weekend class
    if ((dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) && weekendClass != null) {
      classes += " " + encodeAttribute(weekendClass.trim());
    }
    
    // add today class
    if (isToday && todayClass != null) {
      classes += " " + encodeAttribute(todayClass.trim());
    }
    
    // add selected class
    if (isSelected && selectedClass != null) {
      classes += " " + encodeAttribute(selectedClass.trim());
    }
    
    classes = classes.trim();
    if (classes.length() > 0) {
      out.print(" class=\"");
      out.print(classes);
      out.print("\"");
    }
    
    out.print(">");
    
    
    if (content == null) {
      // normal processing
      if (showLink) {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
        
        if (title == null) {
          DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, locale);
          title = df.format(current.getTime());
        }
        
        URL targetURL = null;
        String output = null;
        boolean relative = false;
        
        URL currentPage = getCurrentUrl(request);
        
        if (link == null) {
          // no link specified, use current URL
          targetURL = currentPage;
          relative = true;
        } else {
          // user link specified
          targetURL = new URL(currentPage, link);
          relative = isURLRelative(targetURL, currentPage);
        }
        
        if (encodeLink) {
          // add month-day-year
          DecimalFormat df = new DecimalFormat("####");        
          Map<String, String> params = new HashMap<String, String>();
          params.put(monthParam, df.format(current.get(Calendar.MONTH) + 1));
          params.put(dayParam, df.format(current.get(Calendar.DAY_OF_MONTH)));
          params.put(yearParam, df.format(current.get(Calendar.YEAR)));
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
        if (title != null) {
          out.print(" title=\"");
          out.print(encodeAttribute(title));
          out.print("\"");
        }
        out.print(">");
      }
      
      out.print(dfDay.format(current.get(Calendar.DAY_OF_MONTH)));
      
      if (showLink) {
        out.print("</a>");
      }
    } else {
      // custom content
      out.print(content);
    }
    
    out.print("</td>");
  }
  
  private void renderDayHeading(JspWriter out, String shortHeading, String longHeading, int dayOfWeek) throws IOException {
    String escLong = encodeAttribute(longHeading);
    String escShort = encodePCData(shortHeading);
  
    out.print("<th abbr=\"");
    out.print(escLong);
    out.print("\" title=\"");
    out.print(escLong);
    out.print("\" scope=\"col\"");
    if ((dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) && weekendClass != null) {
      out.print(" class=\"");
      out.print(encodeAttribute(weekendClass));
      out.print("\"");
    }
    out.print(">");
    out.print(escShort);
    out.print("</th>");
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
}
