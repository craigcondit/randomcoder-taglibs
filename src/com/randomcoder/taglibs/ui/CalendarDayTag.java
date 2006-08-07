package com.randomcoder.taglibs.ui;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.*;

/**
 * Implementation of the calendar-day tag.
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
public class CalendarDayTag extends BodyTagSupport {    
  private static final long serialVersionUID = 2379893374923533986L;
  
  private Integer day;
  private Boolean showLink;
  private String link;
  private Boolean encodeLink;
  private String monthParam;
  private String dayParam;
  private String yearParam;
  private String title;
  private String dayClass;
  private String dayId;
  
  public void setDay(int day) {
    this.day = day;
  }
  
  public void setShowLink(boolean showLink) {
    this.showLink = showLink;
  }
  
  public void setLink(String link) {
    this.link = link;
  }
  
  public void setEncodeLink(Boolean encodeLink) {
    this.encodeLink = encodeLink;
  }
  
  public void setMonthParam(String monthParam) {
    this.monthParam = monthParam;
  }
  
  public void setDayParam(String dayParam) {
    this.dayParam = dayParam;
  }
  
  public void setYearParam(String yearParam) {
    this.yearParam = yearParam;
  }
  
  public void setTitle(String title) {
    this.title = title;
  }
  
  public void setDayClass(String dayClass) {
    this.dayClass = dayClass;    
  }
  
  public void setDayId(String dayId) {
    this.dayId = dayId;
  }
  
  @Override
  public int doAfterBody() throws JspException {
    // this evaluates the body, but doesn't write it out
    return SKIP_BODY;
  }

  @Override
  public int doEndTag() throws JspException {
    try {
      CalendarTag cal = (CalendarTag) findAncestorWithClass(this, CalendarTag.class);
      if (cal == null) return Tag.EVAL_PAGE; // can't use this
      
      String content = null;
      if (day != null && day >= 1 && day <= 31) {
        BodyContent body = getBodyContent();
        if (body != null) content = body.getString();
        if (content == null || content.trim().length() == 0)
          content = null;
      }
      
      CalendarDaySpec spec = new CalendarDaySpec(
        showLink, link, encodeLink, monthParam, dayParam, yearParam, title, dayClass, dayId, content
      );
      cal.setDaySpec(day, spec);
      
      return Tag.EVAL_PAGE;
    } finally {
      cleanup();
    }
  }

  @Override
  public void release() {
    super.release();
    cleanup();
  }
  
  private void cleanup() {
    day = null;
    showLink = null;
    link = null;
    encodeLink = null;
    monthParam = null;
    dayParam = null;
    yearParam = null;
    dayClass = null;
    dayId = null;
  }
}
