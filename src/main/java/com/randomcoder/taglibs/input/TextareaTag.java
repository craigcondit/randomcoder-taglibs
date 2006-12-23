package com.randomcoder.taglibs.input;

import java.io.IOException;

import javax.servlet.jsp.*;

import com.randomcoder.taglibs.common.HtmlHelper;

/**
 * Tag class which produces &lt;textarea&gt;.
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
public class TextareaTag extends InputTagBase
{

	private static final long serialVersionUID = -905446055685214582L;

	@Override
	protected String getType()
	{
		return "textarea";
	}

	/**
	 * Sets the rows HTML attribute.
	 * @param rows value of rows attribute
	 */
	public void setRows(String rows)
	{
		getParams().put("rows", rows);
	}

	/**
	 * Sets the cols HTML attribute.
	 * @param cols value of cols attribute
	 */
	public void setCols(String cols)
	{
		getParams().put("cols", cols);
	}

	@Override
	public int doEndTag() throws JspException
	{
		try
		{
			JspWriter out = pageContext.getOut();

			out.write("<textarea");
			out.write(" name=\"" + HtmlHelper.encodeAttribute(getName()) + "\"");
			if (getStyleId() != null)
				out.write(" id=\"" + HtmlHelper.encodeAttribute(getStyleId()) + "\"");
			out.write(buildOptions());
			out.write(">");
			out.write(HtmlHelper.encodePCData(getValue()));
			out.write("</textarea>");
		}
		catch (IOException ioe)
		{
			throw new JspException(ioe);
		}

		return EVAL_PAGE;
	}
}
