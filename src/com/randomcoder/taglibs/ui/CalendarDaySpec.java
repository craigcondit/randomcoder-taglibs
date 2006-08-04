package com.randomcoder.taglibs.ui;

import java.io.Serializable;

import org.apache.commons.lang.builder.*;

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

  public String getDayParam() {
    return dayParam;
  }

  public Boolean isEncodeLink() {
    return encodeLink;
  }

  public String getLink() {
    return link;
  }

  public String getMonthParam() {
    return monthParam;
  }

  public Boolean isShowLink() {
    return showLink;
  }

  public String getYearParam() {
    return yearParam;
  }
  
  public String getTitle() {
    return title;
  }
  
  public String getDayClass() {
    return dayClass;
  }

  public String getDayId() {
    return dayId;
  }
  
  public String getContent() {
    return content;
  }
  
  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
  
}
