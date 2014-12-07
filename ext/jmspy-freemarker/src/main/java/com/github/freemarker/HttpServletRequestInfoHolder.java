package com.github.freemarker;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by dmgcodevil on 12/7/2014.
 */
public class HttpServletRequestInfoHolder {

    private static final HttpServletRequestInfoHolder INSTANCE = new HttpServletRequestInfoHolder();

    private static final ThreadLocal<Queue<HttpServletRequestInfo>> INFO_THREAD_LOCAL =
            new InheritableThreadLocal<Queue<HttpServletRequestInfo>>() {
                @Override
                protected Queue<HttpServletRequestInfo> initialValue() {
                    return new ArrayDeque<>();
                }
            };

    public static HttpServletRequestInfoHolder getInstance() {
        return INSTANCE;
    }

    private final Lock lock = new ReentrantLock();

    public void hold(HttpServletRequestInfo requestInfo) {
        try {
            lock.lock();
            INFO_THREAD_LOCAL.get().add(requestInfo);
        } finally {
            lock.unlock();
        }
    }

    public HttpServletRequestInfo unhold() {
        return INFO_THREAD_LOCAL.get().poll();
    }
}
