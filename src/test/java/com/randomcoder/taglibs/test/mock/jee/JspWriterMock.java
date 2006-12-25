package com.randomcoder.taglibs.test.mock.jee;

import java.io.*;

import javax.servlet.jsp.JspWriter;

public class JspWriterMock extends JspWriter
{
	private Writer writer;
	
	public JspWriterMock(Writer writer)
	{
		super(0, true);
		this.writer = writer;
	}

	@Override
	public void clear() throws IOException
	{
		throw new IOException();
	}

	@Override
	public void clearBuffer() throws IOException
	{
	}

	@Override
	public void close() throws IOException
	{
		writer.close();
	}

	@Override
	public void flush() throws IOException
	{
		writer.flush();
	}

	@Override
	public int getRemaining()
	{
		return 0;
	}

	@Override
	public void newLine() throws IOException
	{
		writer.write("\r\n");
	}

	@Override
	public void print(boolean b) throws IOException
	{
		writer.write(Boolean.toString(b));
	}

	@Override
	public void print(char c) throws IOException
	{
		writer.write(new char[] {c});
	}

	@Override
	public void print(int i) throws IOException
	{
		writer.write(Integer.toString(i));
	}

	@Override
	public void print(long l) throws IOException
	{
		writer.write(Long.toString(l));
	}

	@Override
	public void print(float f) throws IOException
	{
		writer.write(Float.toString(f));
	}

	@Override
	public void print(double d) throws IOException
	{
		writer.write(Double.toString(d));
	}

	@Override
	public void print(char[] s) throws IOException
	{
		writer.write(s);
	}

	@Override
	public void print(String s) throws IOException
	{
		writer.write(s);
	}

	@Override
	public void print(Object obj) throws IOException
	{
		writer.write(obj.toString());
	}

	@Override
	public void println() throws IOException
	{
		writer.write("\r\n");
	}

	@Override
	public void println(boolean x) throws IOException
	{
		print(x);
		println();
	}

	@Override
	public void println(char x) throws IOException
	{
		print(x);
		println();
	}

	@Override
	public void println(int x) throws IOException
	{
		print(x);
		println();
	}

	@Override
	public void println(long x) throws IOException
	{
		print(x);
		println();
	}

	@Override
	public void println(float x) throws IOException
	{
		print(x);
		println();
	}

	@Override
	public void println(double x) throws IOException
	{
		print(x);
		println();
	}

	@Override
	public void println(char[] x) throws IOException
	{
		print(x);
		println();
	}

	@Override
	public void println(String x) throws IOException
	{
		print(x);
		println();
	}

	@Override
	public void println(Object x) throws IOException
	{
		print(x);
		println();
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException
	{
		writer.write(cbuf, off, len);
	}
}
