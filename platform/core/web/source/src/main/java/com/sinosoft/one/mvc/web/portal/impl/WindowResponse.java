/*
 * Copyright 2007-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sinosoft.one.mvc.web.portal.impl;

import java.io.*;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponseWrapper;

import com.sinosoft.one.mvc.web.portal.Window;

/**
 * 
 *
 * 
 */
class WindowResponse extends HttpServletResponseWrapper {

    private WindowImpl window;

    private PrintWriter writer;

    private ServletOutputStream out;

    private Locale locale;

    private String charset;

    public WindowResponse(WindowImpl window) {
        super(window.getContainer().getResponse());
        this.window = window;
    }

    public Window getWindow() {
        return window;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (out == null) {
            window.setCharset(getCharacterEncoding());
            this.out = new ResponseServletOutputStream();
        }
        return out;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        window.setCharset(getCharacterEncoding());
        if (this.writer == null) {
//            this.writer = new PrintWriter(new Writer() {
//
//                @Override
//                public void close() throws IOException {
//                }
//
//                @Override
//                public void flush() throws IOException {
//                }
//
//                @Override
//                public void write(char[] cbuf, int offset, int len) throws IOException {
//                    String tempString = new String(cbuf, offset, len);
//                    window.appendContent(tempString.getBytes(getCharacterEncoding()));
//                }
//            });
            this.writer = new ResponsePrintWriter(getCharacterEncoding());

        }
        return writer;
    }

    @Override
    public void flushBuffer() throws IOException {
    }

    @Override
    public void setHeader(String name, String value) {
        synchronized (window.getContainer()) {
            super.setHeader(name, value);
        }
    }

    @Override
    public void addHeader(String name, String value) {
        synchronized (window.getContainer()) {
            super.addHeader(name, value);
        }
    }

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public Locale getLocale() {
        return locale != null ? locale : super.getLocale();
    }

    @Override
    public void sendError(int sc) throws IOException {
        window.setStatus(sc);
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        window.setStatus(sc, msg);
    }

    @Override
    public void setStatus(int sc) {
        window.setStatus(sc);
    }

    @Override
    public void setStatus(int sc, String sm) {
        window.setStatus(sc, sm);
    }

    @Override
    public void sendRedirect(String location) throws IOException {
        throw new UnsupportedOperationException("don't call sendRedirect in window request:"
                + location);
    }

    @Override
    public void setBufferSize(int size) {

    }

    @Override
    public void resetBuffer() {
    }

    @Override
    public void setCharacterEncoding(String charset) {
        this.charset = charset;
    }

    @Override
    public String getCharacterEncoding() {
        return charset != null ? charset : super.getCharacterEncoding();
    }

    @Override
    public void setContentType(String type) {
    }

    @Override
    public void setContentLength(int len) {
    }

    private class ResponseServletOutputStream extends ServletOutputStream {


        @Override
        public void write(int b) throws IOException {
            window.appendContent(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            window.appendContent(b, off, len);
        }

    }

    private class ResponsePrintWriter extends PrintWriter {

        private ResponsePrintWriter(String characterEncoding) throws UnsupportedEncodingException {
            super(new OutputStreamWriter(window.getByteArrayOutputStream(), characterEncoding));
        }

        @Override
        public void write(char buf[], int off, int len) {
            super.write(buf, off, len);
        }

        @Override
        public void write(String s, int off, int len) {
            super.write(s, off, len);
        }

        @Override
        public void write(int c) {
            super.write(c);
        }
    }

}