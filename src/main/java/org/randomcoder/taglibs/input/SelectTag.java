package com.randomcoder.taglibs.input;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

import javax.servlet.jsp.*;

import com.randomcoder.taglibs.common.HtmlHelper;

/**
 * Tag class which produces &lt;select&gt; tags.
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
public class SelectTag extends InputTagBase
{
	private static final long serialVersionUID = -4575293374979723202L;

	private final Set<String> values = new HashSet<String>();

	@Override
	public void release()
	{
		super.release();
		values.clear();
	}

	@Override
	protected String getType()
	{
		return "select";
	}

	/**
	 * Sets the size HTML attribute.
	 * @param size value of size attribute
	 */
	public void setSize(String size)
	{
		getParams().put("size", size);
	}

	/**
	 * Adds a new value
	 * @param value value of attribute 'value'
	 */
	public void addValue(Object value)
	{
		if (value == null) return;
		
		if (value instanceof String)
		{
			values.add((String) value);
		}
		else if (value instanceof Collection)
		{
			Collection col = (Collection) value;
			for (Object val : col) addValue(val);
		}
		else if (value instanceof Long)
		{
			DecimalFormat dfLong = new DecimalFormat("####################");
			values.add(dfLong.format(((Long) value).longValue()));
		}
		else if (value instanceof Short)
		{
			DecimalFormat dfShort = new DecimalFormat("#####");
			values.add(dfShort.format(((Short) value).shortValue()));
		}
		else if (value instanceof Integer)
		{
			DecimalFormat dfInt = new DecimalFormat("##########");			
			values.add(dfInt.format(((Integer) value).intValue()));
		}
		else
		{
			// use default translation
			values.add(value.toString());
		}
	}
	
	@Override
	public void setValue(String value)
	{
		setValueObject(value);
	}
	
	/**
	 * Sets the value HTML attribute.
	 * @param value value of attribute 'value'
	 */
	public void setValue(Object value)
	{
		setValueObject(value);
	}
	
	private void setValueObject(Object value)
	{
		values.clear();
		addValue(value);
	}

	/**
	 * Sets the multiple HTML attribute.
	 * @param multiple value of multiple attribute
	 */
	public void setMultiple(String multiple)
	{
		if (multiple == null) multiple = "FALSE";
		multiple = multiple.toUpperCase(Locale.US);
		if ("TRUE".equals(multiple))
			getParams().put("multiple", "multiple");
		else if ("MULTIPLE".equals(multiple))
			getParams().put("multiple", "multiple");
		else
			getParams().remove("multiple");
	}

	@Override
	public int doStartTag() throws JspException
	{
		try
		{
			JspWriter out = pageContext.getOut();

			out.write("<");
			out.write(getType());
			out.write(" name=\"" + HtmlHelper.encodeAttribute(getName()) + "\"");
			if (getStyleId() != null)
				out.write(" id=\"" + HtmlHelper.encodeAttribute(getStyleId()) + "\"");
			out.write(buildOptions());
			out.write(">");
		}
		catch (IOException ioe)
		{
			throw new JspException(ioe);
		}

		return EVAL_BODY_INCLUDE;
	}

	/**
	 * Gets the current set of values in this tag.
	 * @return set of values
	 */
	public Set<String> getCurrentValues()
	{
		return values;
	}

	/**
	 * Determines if this is a multiple select box.
	 * @return true if multiselect, false otherwise
	 */
	public boolean isMultipleSelect()
	{
		return getParams().get("multiple") != null;
	}

	@Override
	public int doEndTag() throws JspException
	{
		try
		{
			JspWriter out = pageContext.getOut();

			out.write("</");
			out.write(getType());
			out.write(">");
		}
		catch (IOException ioe)
		{
			throw new JspException(ioe);
		}
		finally
		{
			release();
		}
		
		return EVAL_PAGE;
	}
}
