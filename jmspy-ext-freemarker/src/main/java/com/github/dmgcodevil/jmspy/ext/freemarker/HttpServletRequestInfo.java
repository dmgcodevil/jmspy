package com.github.dmgcodevil.jmspy.ext.freemarker;

import com.google.common.base.Verify;

import javax.servlet.http.HttpServletRequest;

/**
 * Information about {@link HttpServletRequest}.
 *
 * @author dmgcodevil
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
