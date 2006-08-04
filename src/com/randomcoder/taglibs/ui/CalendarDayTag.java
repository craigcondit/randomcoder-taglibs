package com.randomcoder.taglibs.ui;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.*;

import org.apache.commons.logging.*;

public class CalendarDayTag extends BodyTagSupport {    
  private static final long serialVersionUID = 2379893374923533986L;
  private static final Log logger = LogFactory.getLog(CalendarDayTag.class);
  
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
        logger.debug("Body content: " + content);
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
