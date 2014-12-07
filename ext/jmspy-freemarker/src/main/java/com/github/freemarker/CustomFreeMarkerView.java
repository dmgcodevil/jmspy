package com.github.freemarker;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import java.io.IOException;
import java.util.Locale;

/**
 * Class description.
 *
 * @author Raman_Pliashkou
 */
public class CustomFreeMarkerView extends FreeMarkerView {

    @Override
    protected Template getTemplate(String name, Locale locale) throws IOException {
        return super.getTemplate(name, locale);
    }

}
