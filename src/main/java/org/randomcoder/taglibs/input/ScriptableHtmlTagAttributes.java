package org.randomcoder.taglibs.input;

/**
 * Interface containing setters for all common scriptable HTML attributes.
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
public interface ScriptableHtmlTagAttributes extends HtmlTagAttributes
{
	/**
	 * Sets the onclick HTML attribute.
	 * @param onclick value of onclick attribute
	 */
	public void setOnclick(String onclick);

	/**
	 * Sets the ondblclick HTML attribute.
	 * @param ondblclick value of ondblclick attribute
	 */
	public void setOndblclick(String ondblclick);

	/**
	 * Sets the onkeydown HTML attribute.
	 * @param onkeydown value of onkeydown attribute
	 */
	public void setOnkeydown(String onkeydown);

	/**
	 * Sets the onkeypress HTML attribute.
	 * @param onkeypress value of onkeypress attribute
	 */
	public void setOnkeypress(String onkeypress);

	/**
	 * Sets the onkeyup HTML attribute.
	 * @param onkeyup value of onkeyup attribute
	 */
	public void setOnkeyup(String onkeyup);

	/**
	 * Sets the onmousedown HTML attribute.
	 * @param onmousedown value of onmousedown attribute
	 */
	public void setOnmousedown(String onmousedown);

	/**
	 * Sets the onmousemove HTML attribute.
	 * @param onmousemove value of onmousemove attribute
	 */
	public void setOnmousemove(String onmousemove);

	/**
	 * Sets the onmouseout HTML attribute.
	 * @param onmouseout value of onmouseout attribute
	 */
	public void setOnmouseout(String onmouseout);

	/**
	 * Sets the onmouseover HTML attribute.
	 * @param onmouseover value of onmouseover attribute
	 */
	public void setOnmouseover(String onmouseover);

	/**
	 * Sets the onmouseup HTML attribute.
	 * @param onmouseup value of onmouseup attribute
	 */
	public void setOnmouseup(String onmouseup);

}
