package org.randomcoder.taglibs.common;

import java.util.*;

import junit.framework.TestCase;

public class DateHelperTest extends TestCase
{
	public void testIsSameDay()
	{		
		assertFalse("null", DateHelper.isSameDay(null, null));
		
		Calendar cal1 = new GregorianCalendar();
		cal1.set(Calendar.YEAR, 2000);
		cal1.set(Calendar.MONTH, Calendar.JANUARY);
		cal1.set(Calendar.DATE, 1);

		Calendar cal2 = new GregorianCalendar();
		cal2.set(Calendar.YEAR, 2000);
		cal2.set(Calendar.MONTH, Calendar.JANUARY);
		cal2.set(Calendar.DATE, 1);
		
		assertTrue("same", DateHelper.isSameDay(cal1, cal2));
		
		cal1.set(Calendar.DATE, 2);
		assertFalse("day", DateHelper.isSameDay(cal1, cal2));
		
		cal1.set(Calendar.DATE, 1);
		cal1.set(Calendar.MONTH, Calendar.FEBRUARY);
		assertFalse("month", DateHelper.isSameDay(cal1, cal2));

		cal1.set(Calendar.MONTH, Calendar.JANUARY);
		cal1.set(Calendar.YEAR, 2001);
		assertFalse("month", DateHelper.isSameDay(cal1, cal2));
	}

}
