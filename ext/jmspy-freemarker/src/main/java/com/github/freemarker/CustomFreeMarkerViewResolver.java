package com.github.freemarker;

import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * Class description.
 *
 * @author Raman_Pliashkou
 */
public class CustomFreeMarkerViewResolver extends FreeMarkerViewResolver {
    @Override
    protected Class requiredViewClass() {
        return CustomFreeMarkerView.class;
    }


}
