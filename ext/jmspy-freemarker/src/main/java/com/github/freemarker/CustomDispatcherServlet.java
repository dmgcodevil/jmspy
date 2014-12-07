package com.github.freemarker;

import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class description.
 *
 * @author Raman_Pliashkou
 */
public class CustomDispatcherServlet extends DispatcherServlet {

    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpServletRequestInfoHolder.getInstance().hold(new HttpServletRequestInfo(request));
        super.doService(request, response);
    }

}
