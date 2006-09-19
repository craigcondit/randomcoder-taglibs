package com.randomcoder.taglibs.input;

/**
 * Interface containing setters for all scriptable &lt;input&gt; HTML
 * attributes.
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
public interface ScriptableInputTagAttributes extends InputTagAttributes, ScriptableHtmlTagAttributes
{
	/**
	 * Sets the onblur HTML attribute.
	 * @param onblur value of onblur attribute
	 */
	public void setOnblur(String onblur);

	/**
	 * Sets the onchange HTML attribute.
	 * @param onchange value of onchange attribute
	 */
	public void setOnchange(String onchange);

	/**
	 * Sets the onfocus HTML attribute.
	 * @param onfocus value of onfocus attribute
	 */
	public void setOnfocus(String onfocus);

	/**
	 * Sets the onselect HTML attribute.
	 * @param onselect value of onselect attribute
	 */
	public void setOnselect(String onselect);
}
