package org.randomcoder.taglibs.ui;

import java.util.*;

import javax.servlet.jsp.jstl.core.Config;

import org.randomcoder.taglibs.test.base.AbstractTagTestCase;

public class CalendarDayTagTest extends AbstractTagTestCase
{
	private CalendarDayTag tag;
	private CalendarTag parent;

	@Override
	protected void setUp() throws Exception
	{
		tag = new CalendarDayTag();
		parent = new CalendarTag();		
		tag.setParent(parent);		
		super.setUp(tag, parent);
		
		context.setAttribute(Config.FMT_LOCALE, Locale.US);
		context.setAttribute(Config.FMT_TIME_ZONE, TimeZone.getTimeZone("GMT"));
		
		parent.setCapitalizeDays(true);
		parent.setMaxWeekdayLength(1);
		parent.setTableClass("table-class");
		parent.setTableId("table-id");
		parent.setSelectedClass("selected-class");
		parent.setTodayClass("today-class");
		parent.setWeekendClass("weekend-class");
		
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.YEAR, 2000);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		parent.setSelectedDate(cal.getTime());
		
		cal.set(Calendar.DATE, 15);
		
		parent.setShowDate(cal.getTime());
		
		tag.setDay(1);
		tag.setDayClass("day-class");
		tag.setDayId("day-id");
		tag.setDayParam("dayParam");
		tag.setEncodeLink(true);
		tag.setLink("index.html");
		tag.setMonthParam("monthParam");
		tag.setShowLink(true);
		tag.setTitle("day-title");
		tag.setYearParam("yearParam");
	}

	@Override
	protected void tearDown() throws Exception
	{
		tag.release();
		tag = null;
		parent.release();
		parent = null;
		super.tearDown();
	}

	public void testTag() throws Exception
	{
		parent.doStartTag();
		tag.doStartTag();
		tag.doAfterBody();
		tag.doEndTag();
		parent.doAfterBody();
		parent.doEndTag();
		
		String result = writer.toString();
		
		assertTrue("day-class", result.contains("day-class"));
		assertTrue("day-id", result.contains("\"day-id\""));
		assertTrue("dayParam", result.contains("dayParam=1"));
		assertTrue("index.html", result.contains("index.html?"));
		assertTrue("monthParam", result.contains("monthParam=1"));
		assertTrue("day-title", result.contains("title=\"day-title\""));
		assertTrue("yearParam", result.contains("yearParam=2000"));
	}
}