package com.randomcoder.taglibs.ui;

import java.util.*;

import javax.servlet.jsp.jstl.core.Config;

import com.randomcoder.taglibs.test.base.AbstractTagTestCase;

public class CalendarTagTest extends AbstractTagTestCase
{
	private CalendarTag tag;

	@Override
	protected void setUp() throws Exception
	{
		tag = new CalendarTag();
		super.setUp(tag);
		context.setAttribute(Config.FMT_LOCALE, Locale.US);
		context.setAttribute(Config.FMT_TIME_ZONE, TimeZone.getTimeZone("GMT"));
		tag.setCapitalizeDays(true);
		tag.setMaxWeekdayLength(1);
		tag.setTableClass("table-class");
		tag.setTableId("table-id");
		tag.setSelectedClass("selected-class");
		tag.setTodayClass("today-class");
		tag.setWeekendClass("weekend-class");
		
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.YEAR, 2000);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		tag.setSelectedDate(cal.getTime());
		
		cal.set(Calendar.DATE, 15);
		
		tag.setShowDate(cal.getTime());		
	}

	@Override
	protected void tearDown() throws Exception
	{
		tag.release();
		tag = null;
		super.tearDown();
	}

	public void testTag() throws Exception
	{		
		tag.doStartTag();
		tag.doAfterBody();
		tag.doEndTag();
		String result = writer.toString();
		
		// sanity checks
		assertTrue("start", result.startsWith("<table"));
		assertTrue("end", result.endsWith("</table>"));
		
		// 31 days in january
		for (int i = 1; i <= 31; i++)
		{
			assertTrue(Integer.toString(i), result.contains("January " + i + ", 2000"));
		}
		assertFalse("32", result.contains("January 32, 2000"));
		
		// look for classes and ids
		assertTrue("table-class", result.contains("table-class"));
		assertTrue("table-id", result.contains("table-id"));
		assertTrue("selected-class", result.contains("selected-class"));
		assertFalse("today-class", result.contains("today-class"));
		assertTrue("weekend-class", result.contains("weekend-class"));
		
		// look for weekdays
		assertTrue("Sunday", result.contains("Sunday"));
		assertTrue("Monday", result.contains("Monday"));
		assertTrue("Tuesday", result.contains("Tuesday"));
		assertTrue("Wednesday", result.contains("Wednesday"));
		assertTrue("Thursday", result.contains("Thursday"));
		assertTrue("Friday", result.contains("Friday"));
		assertTrue("Saturday", result.contains("Saturday"));
	}
	
	public void testTagDefaultDay() throws Exception
	{
		tag.doStartTag();
		tag.setDaySpec(null, new CalendarDaySpec(true, "test.html", true, "testMonth", "testDay", "testYear", "testTitle", "testDayClass", null, null));		
		tag.doAfterBody();
		tag.doEndTag();
		String result = writer.toString();
		
		assertTrue("testMonth", result.contains("testMonth="));
		assertTrue("testDay", result.contains("testDay="));
		assertTrue("testYear", result.contains("testYear="));
		assertTrue("testDayClass", result.contains("testDayClass"));
		assertTrue("test.html", result.contains("test.html?"));
		assertTrue("testTitle", result.contains("\"testTitle\""));
	}
	
	public void testTagDaySpecified() throws Exception
	{
		tag.doStartTag();
		tag.setDaySpec(1, new CalendarDaySpec(false, null, null, null, null, null, null, "dayOneClass", "dayOneId", "one"));		
		tag.doAfterBody();
		tag.doEndTag();
		String result = writer.toString();
		
		assertTrue("dayOneClass", result.contains("dayOneClass"));
		assertTrue("dayOneId", result.contains("\"dayOneId\""));
		assertTrue("one", result.contains(">one</td>"));			
	}
	
	public void testTagInvalidSpecs() throws Exception
	{
		tag.doStartTag();
		tag.setDaySpec(-1, new CalendarDaySpec(null, null, null, null, null, null, null, "smallClass", null, null));		
		tag.setDaySpec(32, new CalendarDaySpec(null, null, null, null, null, null, null, "bigClass", null, null));		
		tag.doAfterBody();
		tag.doEndTag();
		String result = writer.toString();
		
		assertFalse("smallClass", result.contains("smallClass"));
		assertFalse("bigClass", result.contains("bigClass"));
	}
	
	public void testTagEncodedDay() throws Exception
	{
		tag.doStartTag();
		tag.setDaySpec(1, new CalendarDaySpec(true, "index.html", true, "testMonth", "testDay", "testYear", "testTitle", "testClass", "testId", null));		
		tag.doAfterBody();
		tag.doEndTag();
		String result = writer.toString();
		
		assertTrue("index.html", result.contains("index.html?"));
		assertTrue("testMonth", result.contains("testMonth=1"));
		assertTrue("testDay", result.contains("testDay=1"));
		assertTrue("testYear", result.contains("testYear=2000"));
		assertTrue("testTitle", result.contains("\"testTitle\""));
		assertTrue("testClass", result.contains("testClass"));
		assertTrue("testId", result.contains("\"testId\""));
	}
	
	public void testTagConstantLink() throws Exception
	{
		tag.doStartTag();
		tag.setDaySpec(1, new CalendarDaySpec(true, "test.html?link=1/1/2000", false, null, null, null, null, null, null, null));		
		tag.doAfterBody();
		tag.doEndTag();
		String result = writer.toString();
		
		assertTrue("link", result.contains("href=\"test.html?link=1/1/2000\""));
	}
}