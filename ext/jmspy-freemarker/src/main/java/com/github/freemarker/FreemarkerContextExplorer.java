package com.github.freemarker;

import com.github.dmgcodevil.jmspy.context.ContextExplorer;
import com.github.dmgcodevil.jmspy.context.InvocationContextInfo;
import freemarker.core.Environment;
import freemarker.template.Template;

import java.util.HashMap;
import java.util.Map;

/**
 * Class description.
 *
 * @author Raman_Pliashkou
 */
public class FreemarkerContextExplorer implements ContextExplorer {

    @Override
    public InvocationContextInfo getRootContextInfo() {
        InvocationContextInfo invocationContextInfo = new InvocationContextInfo();
        HttpServletRequestInfo requestInfo = HttpServletRequestInfoHolder.getInstance().unhold();
        if (requestInfo != null) {
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
                Map<String, String> details = new HashMap<>();
                details.put("Root TreeNode", template.getRootTreeNode().toString());
                invocationContextInfo.setDetails(details);
            }

        }
        return invocationContextInfo;
    }
}
