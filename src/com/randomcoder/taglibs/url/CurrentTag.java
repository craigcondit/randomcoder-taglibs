package com.randomcoder.taglibs.url;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.TagSupport;

import com.randomcoder.taglibs.common.RequestHelper;

/**
 * Tag class which retrieves the current page's URL.
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
public class CurrentTag extends TagSupport
{
	private static final long serialVersionUID = 5868343381730564644L;

	private String var;
	private String scope;

	private PageContext pageContext;

	/**
	 * Sets the scope for the exported variable.
	 * 
	 * @param scope JSP scope (page, request, session, or application)
	 */
	public void setScope(String scope)
	{
		this.scope = scope;
	}

	/**
	 * Sets the name of the exported variable.
	 * 
	 * @param var variable name
	 */
	public void setVar(String var)
	{
		this.var = var;
	}

	/**
	 * Sets the JSP PageContext variable.
	 */
	@Override
	public void setPageContext(PageContext pageContext)
	{
		super.setPageContext(pageContext);
		this.pageContext = pageContext;
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

	private void cleanup()
	{
		var = null;
		scope = null;
		pageContext = null;
	}

	/**
	 * Renders the current page's URL to a variable or to the current writer.
	 * @return EVAL_PAGE
	 */
	@Override
	public int doEndTag() throws JspException
	{
		try
		{
			if (scope != null && var == null)
				throw new JspException("var is required when scope is specified");

			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			URL currentUrl = RequestHelper.getCurrentUrl(request);
			String text = currentUrl.toExternalForm();

			if (var == null)
			{
				JspWriter out = pageContext.getOut();
				out.write(text);
			}
			else
			{
				if (scope == null)
				{
					pageContext.setAttribute(var, text);
				}
				else
				{
					pageContext.setAttribute(var, text, getScope(scope));
				}
			}

			return EVAL_PAGE;

		}
		catch (IOException e)
		{
			throw new JspException(e);
		}
		finally
		{
			cleanup();
		}
	}

	private int getScope(String scopeName) throws JspException
	{
		if (scopeName == null)
			throw new JspException("scope is null");

		String test = scopeName.toUpperCase(Locale.US);

		if ("PAGE".equals(test))
			return PageContext.PAGE_SCOPE;
		if ("REQUEST".equals(test))
			return PageContext.REQUEST_SCOPE;
		if ("SESSION".equals(test))
			return PageContext.SESSION_SCOPE;
		if ("APPLICATION".equals(test))
			return PageContext.APPLICATION_SCOPE;
		throw new JspException("Invalid scope: " + scopeName);
	}

}
