package com.randomcoder.taglibs.url;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Tag class which adds a parameter to a URL.
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
public class AddParamTag extends TagSupport
{
	private static final long serialVersionUID = -1726710757304026189L;

	private String name;
	private String value;

	/**
	 * Sets the parameter name.
	 * 
	 * @param name parameter name
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Sets the parameter value.
	 * 
	 * @param value parameter value
	 */
	public void setValue(String value)
	{
		this.value = value;
	}

	/**
	 * Release state.
	 */
	@Override
	public void release()
	{
		super.release();
		cleanup();
	}

	/**
	 * Adds the given parameter to the URL.
	 * 
	 * @return EVAL_PAGE
	 */
	@Override
	public int doEndTag() throws JspException
	{
		try
		{
			ModifyTag mtag = (ModifyTag) findAncestorWithClass(this, ModifyTag.class);
			if (mtag == null)
				throw new JspException("No modify tag parent found");

			mtag.addParameter(name, value);

			return EVAL_PAGE;

		}
		finally
		{
			cleanup();
		}
	}

	private void cleanup()
	{
		name = null;
		value = null;
	}

}
