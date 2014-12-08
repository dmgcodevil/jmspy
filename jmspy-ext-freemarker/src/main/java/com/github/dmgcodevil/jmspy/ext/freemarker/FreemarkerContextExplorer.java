package com.github.dmgcodevil.jmspy.ext.freemarker;

import com.github.dmgcodevil.jmspy.context.ContextExplorer;
import com.github.dmgcodevil.jmspy.context.InvocationContextInfo;
import com.github.dmgcodevil.jmspy.ext.freemarker.config.ContextExplorerConfiguration;
import freemarker.core.Environment;
import freemarker.template.Template;

import java.util.HashMap;
import java.util.Map;

import static com.github.dmgcodevil.jmspy.ext.freemarker.config.ContextExplorerConfiguration.defaultConfig;

/**
 * Implementation of {@link ContextExplorer} for freemarker.
 *
 * @author dmgcodevil
 */
public class FreemarkerContextExplorer implements ContextExplorer {

    private ContextExplorerConfiguration configuration = defaultConfig();
    private static final InvocationContextInfo UNKNOWN = new InvocationContextInfo();

    static {
        UNKNOWN.setInfo("unknown");
    }

    public FreemarkerContextExplorer() {
    }

    public FreemarkerContextExplorer(ContextExplorerConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public InvocationContextInfo getRootContextInfo() {
        if (!configuration.isRecordRequestUrl()) {
            return UNKNOWN;
        }

        HttpServletRequestInfo requestInfo = HttpServletRequestInfoHolder.getInstance().unhold();
        InvocationContextInfo invocationContextInfo = UNKNOWN;
        if (requestInfo != null) {
            invocationContextInfo = new InvocationContextInfo();
            invocationContextInfo.setInfo("requestUrl: '" + requestInfo.getRequestUrl() + "'");
        }
        return invocationContextInfo;
    }

    @Override
    public InvocationContextInfo getCurrentContextInfo() {
        InvocationContextInfo invocationContextInfo = new InvocationContextInfo();
        Environment environment = Environment.getCurrentEnvironment();
        if (environment != null) {
            Template template = environment.getMainNamespace().getTemplate();
            if (template != null) {
                StringBuilder builder = new StringBuilder();
                builder.append("template name: '").append(template.getName()).append("'; ");
                invocationContextInfo.setInfo(builder.toString());
                if (configuration.isRecordDetails()) {
                    Map<String, String> details = new HashMap<>();
                    details.put("Root TreeNode", template.getRootTreeNode().toString());
                    invocationContextInfo.setDetails(details);
                }
            }
        }
        return invocationContextInfo;
    }
}