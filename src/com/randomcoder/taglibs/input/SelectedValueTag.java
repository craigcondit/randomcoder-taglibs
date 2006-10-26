package com.randomcoder.taglibs.input;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Tag class which supports adding multiple discrete values to &lt;select&gt; tags.
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
public class SelectedValueTag extends TagSupport
{
	private static final long serialVersionUID = -1742624158427225310L;
	
	private Object value;
	
	/**
	 * Set the selected value.
	 * @param value value to select
	 */
	public void setValue(Object value)
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
		value = null;
	}
	
	/**
	 * Renders the option tag to the output.
	 * 
	 * @return EVAL_PAGE
	 */
	@Override
	public int doEndTag() throws JspException
	{
		try
		{
			SelectTag select = (SelectTag) findAncestorWithClass(this, SelectTag.class);
			if (select == null)
				throw new JspException("selected-value tag must be nested inside a select tag");
			select.addValue(value);
			return EVAL_PAGE;
		}
		finally
		{
			release();
		}		
	}
}
