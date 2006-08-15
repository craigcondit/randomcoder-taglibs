package com.randomcoder.taglibs.ui;

import java.io.IOException;
import java.net.*;
import java.text.*;
import java.util.*;

import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.randomcoder.taglibs.common.*;

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
  private static final long serialVersionUID = 6415946260066806405L;
  
  private final DecimalFormat dfDay = new DecimalFormat("##");
  
  private PageContext pageContext;
  
  private Date showDate;
  private Date selectedDate;
  
  private String tableClass;
  private String tableId;
  private String todayClass;
  private String selectedClass;
  private String weekendClass;
  private boolean capitalizeDays = true;
  private Integer maxWeekdayLength;
  private Locale locale;
  private ResourceBundle resourceBundle;
  private TimeZone timeZone;
  private DateFormatSymbols dateFormatSymbols;
  
  private final CalendarDaySpec[] daySpecs = new CalendarDaySpec[32];
        
  /**
   * Determins if captions for weekdays should be capitalized (defaults to true).
   *
   * @param capitalizeDays true to capitalize, false otherwise
   */
  public void setCapitalizeDays(boolean capitalizeDays) {
    this.capitalizeDays = capitalizeDays;
  }

  /**
   * Sets the maximum length of the weekday header fields (default is no limit).
   * 
   * @param maxWeekdayLength number of characters
   */
  public void setMaxWeekdayLength(int maxWeekdayLength) {
    this.maxWeekdayLength = maxWeekdayLength;
  }

  /**
   * Sets the CSS class to apply to the calendar table.
   * 
   * @param tableClass CSS class name
   */
  public void setTableClass(String tableClass) {
    this.tableClass = tableClass;
  }

  /**
   * Sets the CSS id to apply to the calendar table.
   * 
   * @param tableId CSS id
   */
  public void setTableId(String tableId) {
    this.tableId = tableId;
  }

  /**
   * Sets the CSS class to apply to the selected date.
   * 
   * @param selectedClass CSS class
   */
  public void setSelectedClass(String selectedClass) {
    this.selectedClass = selectedClass;
  }

  /**
   * Sets the date to mark as selected in the calendar.
   * 
   * @param selectedDate selected date
   */
  public void setSelectedDate(Date selectedDate) {
    this.selectedDate = selectedDate;
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
   * Sets the CSS class to apply to the cell containing today's date.
   * 
   * @param todayClass CSS class
   */
  public void setTodayClass(String todayClass) {
    this.todayClass = todayClass;
  }

  /**
   * Sets the CSS class to apply to the cells containing weekend dates.
   * 
   * @param weekendClass CSS class
   */
  public void setWeekendClass(String weekendClass) {
    this.weekendClass = weekendClass;
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
    selectedDate = null;
    tableId = null;
    tableClass = null;
    todayClass = null;
    selectedClass = null;
    weekendClass = null;
    locale = null;
    resourceBundle = null;
    timeZone = null;
    dateFormatSymbols = null;
    capitalizeDays = true;
    maxWeekdayLength = null;
    
    for (int i = 0; i < 31; i++) daySpecs[i] = null;
    
    pageContext = null;
  }

  /**
   * After the body evaluation: do not reevaluate and continue with the page. By
   * default nothing is done with the bodyContent data (if any).
   * 
   * @return SKIP_BODY
   */
  @Override
  public int doAfterBody() throws JspException {
    return SKIP_BODY;
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
      if (tableId != null) {
        out.print(" id=\"");
        out.print(HtmlHelper.encodeAttribute(tableId));
        out.print("\"");
      }
      if (tableClass != null) {
        out.print(" class=\"");
        out.print(HtmlHelper.encodeAttribute(tableClass));
        out.print("\"");
      }
      out.print(">");
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

  private DateFormatSymbols getDateFormatSymbols() {
    DateFormatSymbols dfs = new DateFormatSymbols(locale);
  
    // capitalize days
    if (capitalizeDays) {
      String[] weekdays = dfs.getWeekdays();
      for (int i = 0; i < weekdays.length; i++) {
        if (weekdays[i].length() > 0) {
          weekdays[i] = weekdays[i].substring(0,1).toUpperCase(locale) + weekdays[i].substring(1);
        }
      }
      dfs.setWeekdays(weekdays);
      
      String[] shortWeekdays = dfs.getShortWeekdays();
      for (int i = 0; i < shortWeekdays.length; i++) {
        if (shortWeekdays[i].length() > 0) {
          shortWeekdays[i] = shortWeekdays[i].substring(0,1).toUpperCase(locale) + shortWeekdays[i].substring(1);
        }
      }
      dfs.setShortWeekdays(shortWeekdays);      
    }
    
    // shorten weekdays if needed
    if (maxWeekdayLength != null && maxWeekdayLength > 0) {
      String[] shortWeekdays = dfs.getShortWeekdays();
      for (int i = 0; i < shortWeekdays.length; i++) {
        if (shortWeekdays[i].length() > maxWeekdayLength) {
          shortWeekdays[i] = shortWeekdays[i].substring(0, maxWeekdayLength);
        }
      }
      dfs.setShortWeekdays(shortWeekdays);
    }
    
    return dfs;
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
  
  private void renderEmptyDay(JspWriter out, int dayOfWeek) throws IOException {    
    out.print("<td");
    if ((dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) && weekendClass != null) {
      out.print(" class=\"");
      out.print(HtmlHelper.encodeAttribute(weekendClass));
      out.print("\"");
    }
    out.print(">");
    out.print("&#160;");
    out.print("</td>");
  }
  
  private void renderDay(JspWriter out, Calendar current, int dayOfWeek, boolean isToday, boolean isSelected) throws IOException {
    
    CalendarDaySpec daySpec = daySpecs[current.get(Calendar.DAY_OF_MONTH)];
    CalendarDaySpec defSpec = daySpecs[0];
    
    // get link generation details
    boolean showLink = true;
    boolean encodeLink = true;
    String link = null;
    String mParam = "month";
    String dParam = "day";
    String yParam = "year";
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
      if (defMonthParam != null) mParam = defMonthParam;
      
      String defDayParam = defSpec.getDayParam();
      if (defDayParam != null) dParam = defDayParam;
      
      String defYearParam = defSpec.getYearParam();
      if (defYearParam != null) yParam = defYearParam;
      
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
      if (dayMonthParam != null) mParam = dayMonthParam;
      
      String dayDayParam = daySpec.getDayParam();
      if (dayDayParam != null) dParam = dayDayParam;
      
      String dayYearParam = daySpec.getYearParam();
      if (dayYearParam != null) yParam = dayYearParam;
      
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
      out.print(HtmlHelper.encodeAttribute(dayId));
      out.print("\"");
    }
    
    String classes = "";
    
    if (dayClass != null) {
      classes += " " + HtmlHelper.encodeAttribute(dayClass.trim());
    }
    
    // add weekend class
    if ((dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) && weekendClass != null) {
      classes += " " + HtmlHelper.encodeAttribute(weekendClass.trim());
    }
    
    // add today class
    if (isToday && todayClass != null) {
      classes += " " + HtmlHelper.encodeAttribute(todayClass.trim());
    }
    
    // add selected class
    if (isSelected && selectedClass != null) {
      classes += " " + HtmlHelper.encodeAttribute(selectedClass.trim());
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
        
        URL currentPage = RequestHelper.getCurrentUrl(request);
        
        if (link == null) {
          // no link specified, use current URL
          targetURL = currentPage;
          relative = true;
        } else if (encodeLink) {
          // user link specified
          targetURL = new URL(currentPage, link);
          relative = UrlHelper.isUrlRelative(targetURL, currentPage);
        }
        
        if (encodeLink) {
          // add month-day-year
          DecimalFormat df = new DecimalFormat("####");        
          Map<String, String> params = new HashMap<String, String>();
          params.put(mParam, df.format(current.get(Calendar.MONTH) + 1));
          params.put(dParam, df.format(current.get(Calendar.DAY_OF_MONTH)));
          params.put(yParam, df.format(current.get(Calendar.YEAR)));
          URL generated = RequestHelper.appendParameters(request, targetURL, params);
          
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
        out.print(HtmlHelper.encodeAttribute(output));
        out.print("\"");
        if (title != null) {
          out.print(" title=\"");
          out.print(HtmlHelper.encodeAttribute(title));
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
    String escLong = HtmlHelper.encodeAttribute(longHeading);
    String escShort = HtmlHelper.encodePCData(shortHeading);
  
    out.print("<th abbr=\"");
    out.print(escLong);
    out.print("\" title=\"");
    out.print(escLong);
    out.print("\" scope=\"col\"");
    if ((dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) && weekendClass != null) {
      out.print(" class=\"");
      out.print(HtmlHelper.encodeAttribute(weekendClass));
      out.print("\"");
    }
    out.print(">");
    out.print(escShort);
    out.print("</th>");
  }
}
