package com.github.dmgcodevil.jmspy.ext.freemarker;

import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Custom implementation of {@link DispatcherServlet} to record request.
 *
 * @author Raman_Pliashkou
 */
public class JmspyDispatcherServlet extends DispatcherServlet {

    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpServletRequestInfoHolder.getInstance().hold(new HttpServletRequestInfo(request));
        super.doService(request, response);
    }

}