package com.randomcoder.taglibs.ui;

import java.io.IOException;
import java.text.*;
import java.util.*;

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

  private final DecimalFormat dfDay = new DecimalFormat("##");
  
  private PageContext pageContext;
  
  private Date showDate;
  private Date selectedDate;
  
  private String outerClass;
  private String outerId;
  private String dayClass;
  private String todayClass;
  private String selectedClass;
  private String weekendClass;
  private String captionFormat = DEFAULT_CAPTION_FORMAT;
  private String bundlePrefix = DEFAULT_BUNDLE_PREFIX;
  
  private Locale locale;
  private ResourceBundle resourceBundle;
  private TimeZone timeZone;
  private DateFormatSymbols dateFormatSymbols;
  
  public void setShowDate(Date showDate) {
    this.showDate = showDate;
  }
  
  public void setDayClass(String dayClass) {
    this.dayClass = dayClass;
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
  
  public void setBundlePrefix(String bundlePrefix) {
    this.bundlePrefix = bundlePrefix;
  }
  
  @Override
  public void setPageContext(PageContext pageContext) {
    super.setPageContext(pageContext);
    this.pageContext = pageContext;
  }
  
  @Override  
  public void release() {
    showDate = null;    
    selectedDate = null;
    outerId = null;
    outerClass = null;
    dayClass = null;
    todayClass = null;
    selectedClass = null;
    weekendClass = null;
    locale = null;
    resourceBundle = null;
    timeZone = null;
    captionFormat = DEFAULT_CAPTION_FORMAT;
    bundlePrefix = DEFAULT_BUNDLE_PREFIX;
    dateFormatSymbols = null;
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
      out.print("<div");
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
      out.print("<table>");

      out.print("<caption>");
      renderCaption(out, current);
      out.print("</caption>");
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
        
        renderDay(out, i, dow, isSameDay(current, now), isSameDay(current, selected));
              
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
      out.print("</div>");
      
      return EVAL_PAGE;
      
    } catch (IOException e) {
      throw new JspException(e);
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
  
  private void renderCaption(JspWriter out, Calendar cal) throws IOException {
    // default implementation
    SimpleDateFormat sdfTitle = new SimpleDateFormat(captionFormat, locale);
    sdfTitle.setDateFormatSymbols(dateFormatSymbols);
    out.print(encodePCData(sdfTitle.format(cal.getTime())));
  }
  
  private void renderHead(JspWriter out) throws IOException {
    // default implementation
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
  
  private void renderDay(JspWriter out, int day, int dayOfWeek, boolean isToday, boolean isSelected) throws IOException {    
    out.print("<td");
    String classes = "";
    
    // add day class
    if (dayClass != null) {
      classes += " " + encodeAttribute(dayClass.trim());
    }
    
    // add weekend class
    if ((dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) && weekendClass != null) {
      classes += " " + encodeAttribute(weekendClass.trim());
    }
    
    if (isToday && todayClass != null) {
      classes += " " + encodeAttribute(todayClass.trim());
    }
    
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
    out.print(dfDay.format(day));    
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

    StringBuffer buf = new StringBuffer();

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

    StringBuffer buf = new StringBuffer();

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
