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

    private static ThreadLocal<String> requestURL = new ThreadLocal<>();

    public static String getRequestURL() {
        return requestURL.get();
    }

    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
        requestURL.set(request.getRequestURL().toString());
        super.doService(request, response);
    }

}
