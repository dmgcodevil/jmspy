package com.github.freemarker;

import com.google.common.base.Verify;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dmgcodevil on 12/7/2014.
 */
public class HttpServletRequestInfo {
    private String requestUrl;

    public HttpServletRequestInfo(HttpServletRequest request) {
        Verify.verifyNotNull(request, "request cannot be null");
        requestUrl = request.getRequestURL().toString();
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }
}
