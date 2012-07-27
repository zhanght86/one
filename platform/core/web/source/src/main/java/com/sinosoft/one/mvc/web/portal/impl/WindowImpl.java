/*
 * Copyright 2007-2012 the original author or authors.
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Charsets;
import com.sinosoft.one.mvc.web.portal.Portal;
import com.sinosoft.one.mvc.web.portal.Window;

/**
 * 
 *
 * 
 */
class WindowImpl implements Window {

    private static boolean defaultMayInterruptIfRunning = false;
    static {
        String pv;
        try {
            pv = System.getProperty("mvc.portal.may_interrupt_if_running");
            if (pv == null) {
                pv = (String) System.getenv("mvc.portal.may_interrupt_if_running");
            }
        } catch (Exception e) {
            e.printStackTrace();
            pv = "false";
        }
        if (pv == null) {
            pv = "false";
        }
        defaultMayInterruptIfRunning = Boolean.valueOf(pv);
    }

    //--------------------

    private String name;

    private String path;

    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    private String charset;

    private Throwable throwable;

    private int statusCode = 200;

    private String statusMessage = "";

    private GenericWindowContainer container;

    private WindowFuture<?> future;

    private boolean mayInterruptIfRunning = defaultMayInterruptIfRunning;

    private boolean interrupted = false;

    /**
     * 窗口请求对象私有的、有别于其他窗口的属性
     */
    private Map<String, Object> privateAttributes;

    public WindowImpl(GenericWindowContainer container, String name, String windowPath) {
        this.container = container;
        this.name = name;
        this.path = windowPath;
    }

    /**
     * 请使用 {@link #getContainer()}代替
     */
    @Override
    @Deprecated
    public Portal getPortal() {
        return (Portal) container;
    }

    @Override
    public GenericWindowContainer getContainer() {
        return container;
    }

    @Override
    public Future<?> getFuture() {
        return future;
    }

    public void setFuture(WindowFuture<?> future) {
        this.future = future;
    }

    public void setInterrupted(boolean interrupted) {
        this.interrupted = interrupted;
    }

    @Override
    public boolean isCancelled() {
        return interrupted || future.isCancelRequested() || future.isCancelled();
    }

    @Override
    public void set(String key, Object value) {
        if (FUTURE_CANCEL_ENABLE_ATTR.equals(key)) {
            if (value == null || Boolean.FALSE.equals(value) || "false".equals(value)) {
                setMayInterruptIfRunning(false);
            } else {
                setMayInterruptIfRunning(true);
            }
        } else {
            if (privateAttributes == null) {
                privateAttributes = new HashMap<String, Object>();
            }
            privateAttributes.put(key, value);
        }
    }

    @Override
    public Object get(String key) {
        if (FUTURE_CANCEL_ENABLE_ATTR.equals(key)) {
            return mayInterruptIfRunning();
        }
        return privateAttributes == null ? null : privateAttributes.get(key);
    }

    @Override
    public void remove(String key) {
        if (FUTURE_CANCEL_ENABLE_ATTR.equals(key)) {
            setMayInterruptIfRunning(defaultMayInterruptIfRunning);
            return;
        }
        if (privateAttributes != null) {
            privateAttributes.remove(key);
        }
    }

    @Override
    public Map<String, Object> getAttributes() {
        if (privateAttributes == null) {
            return Collections.emptyMap();
        } else {
            return Collections.unmodifiableMap(privateAttributes);
        }
    }

    @Override
    public void setTitle(Object title) {
        set(TITLE_ATTR, title);
    }

    @Override
    public Object getTitle() {
        Object value = get(TITLE_ATTR);
        if (value == null) {
            value = name;
        }
        return value;
    }

    @Override
    public int getContentLength() {
        return byteArrayOutputStream.size() == 0 ? -1 : byteArrayOutputStream.size();
    }

    @Override
    public String getContent() {
        try {
            return new String(byteArrayOutputStream.toByteArray(), getCharacterEncoding());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void appendContent(int b) {
        byteArrayOutputStream.write(b);
    }

    public void appendContent(byte[] b) throws IOException {
        byteArrayOutputStream.write(b);
    }

    @Override
    public void clearContent() {
        byteArrayOutputStream.reset();
    }

    public boolean isDone() {
        return future.isDone();
    }

    @Override
    public boolean isSuccess() {
        return !isCancelled() && isDone() && getStatusCode() == HttpServletResponse.SC_OK
                && throwable == null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public Throwable getThrowable() {
        return throwable;
    }

    @Override
    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
        this.statusMessage = throwable.getMessage();
        if (statusCode < 500 || statusCode >= 600) {
            statusCode = 500;
        }
    }

    public void setStatus(int sc) {
        this.statusCode = sc;
        this.statusMessage = "";
    }

    public void setStatus(int sc, String msg) {
        this.statusCode = sc;
        this.statusMessage = msg;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getStatusMessage() {
        return statusMessage;
    }

    @Override
    public String toString() {
        return "window[" + path + "]";
    }

    @Override
    public void render(Writer out) throws IOException {
        getContainer().render(out, this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Window)) {
            return false;
        }
        return this.name.equals(((Window) obj).getName());
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public void setMayInterruptIfRunning(boolean mayInterruptIfRunning) {
        this.mayInterruptIfRunning = mayInterruptIfRunning;
    }

    @Override
    public boolean mayInterruptIfRunning() {
        return mayInterruptIfRunning;
    }

    public String getCharacterEncoding() {
        return charset != null ? charset : Charsets.UTF_8.displayName();
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
