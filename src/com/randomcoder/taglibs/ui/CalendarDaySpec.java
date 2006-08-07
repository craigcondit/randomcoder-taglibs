package com.randomcoder.taglibs.ui;

import java.io.Serializable;

import org.apache.commons.lang.builder.*;

/**
 * JavaBean which contains configuration parameters for a calendar day.
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
public class CalendarDaySpec implements Serializable {      
  private static final long serialVersionUID = 6291293105058624437L;
  
  private Boolean showLink;  
  private String link;
  private Boolean encodeLink;
  private String monthParam;
  private String dayParam;
  private String yearParam;
  private String title;
  private String dayClass;
  private String dayId;
  private String content;
  
  /**
   * Creates a new CalendarDaySpec instance.
   *  
   * @param showLink true if link should be rendered for this day, false otherwise
   * @param link href to use, or null for current page
   * @param encodeLink true if month/day/year information should be added to the url
   * @param monthParam name of parameter to add to url for month, or null for 'month'
   * @param dayParam name of parameter to add to url for day, or null for 'day'
   * @param yearParam name of parameter to add to url for year, or null for 'year'
   * @param title title attribute to add to rendered &lt;A&gt; tag or null for none
   * @param dayClass CSS class to apply to the enclosing table cell, or null for none
   * @param dayId CSS id to apply to the enclosing table cell, or null for none
   * @param content custom content to render for cell, or null for default
   */
  public CalendarDaySpec(Boolean showLink, String link, Boolean encodeLink, String monthParam, String dayParam, String yearParam, String title, String dayClass, String dayId, String content) {
    this.showLink = showLink;
    this.link = link;
    this.encodeLink = encodeLink;
    this.monthParam = monthParam;
    this.dayParam = dayParam;
    this.yearParam = yearParam;
    this.title = title;
    this.dayClass = dayClass;
    this.dayId = dayId;    
    this.content = content;
  }

  /**
   * Determines if a link should be rendered for this day.
   * 
   * @return true if link should be shown, false if not, and null to use default
   *         value.
   */
  public Boolean isShowLink() {
    return showLink;
  }

  /**
   * Gets the URL to use for this link.
   * 
   * @return URL to use, or null for the current page's URL.
   */
  public String getLink() {
    return link;
  }

  /**
   * Determins if the current date should be encoded into this day's URL.
   * 
   * @return true if date should be encoded, false if not, or null to use default.
   */
  public Boolean isEncodeLink() {
    return encodeLink;
  }

  /**
   * Gets the parameter name to use when encoding the month component of
   * the current date into the resulting URL.
   * 
   * @return parameter name, or null to use default
   */
  public String getMonthParam() {
    return monthParam;
  }  
  
  /**
   * Gets the parameter name to use when encoding the day component of the
   * current date into the resulting URL.
   * 
   * @return parameter name, or null to use default
   */
  public String getDayParam() {
    return dayParam;
  }

  /**
   * Gets the parameter name to use when encoding the year component of
   * the current date into the resulting URL.
   * 
   * @return parameter name, or null to use default
   */
  public String getYearParam() {
    return yearParam;
  }
  
  /**
   * Sets the value of the title attribute to set on the resulting link.
   * 
   * @return title text, or null to use default
   */
  public String getTitle() {
    return title;
  }
  
  /**
   * Gets the CSS class attribute to add to the enclosing table cell.
   * 
   * @return CSS class name, or null to use default
   */
  public String getDayClass() {
    return dayClass;
  }

  /**
   * Gets the CSS ID to add to the enclosing table cell.
   * 
   * @return CSS ID, or null to use default
   */
  public String getDayId() {
    return dayId;
  }
  
  /**
   * Gets custom content to render instead of the day number and link.
   * 
   * @return custom content to render for table cell, or null to use default
   */
  public String getContent() {
    return content;
  }
  
  /**
   * Gets a string representation of this class, suitable for debugging.
   */
  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
  
}
