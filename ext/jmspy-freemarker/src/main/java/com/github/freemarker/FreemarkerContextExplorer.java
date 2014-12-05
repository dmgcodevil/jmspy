package com.github.freemarker;

import com.github.dmgcodevil.jmspy.context.ContextExplorer;
import com.github.dmgcodevil.jmspy.context.InvocationContextInfo;

import java.util.Stack;

/**
 * Class description.
 *
 * @author Raman_Pliashkou
 */
public class FreemarkerContextExplorer implements ContextExplorer {

    private static ThreadLocal<Stack<ExecutionInfo>> threadLocals = new InheritableThreadLocal<>();

    public static void executionInfo(String url, String template) {
        Stack<ExecutionInfo> executionInfos = threadLocals.get();
        if (executionInfos == null) {
            executionInfos = new Stack<>();
            threadLocals.set(executionInfos);
        }
        ExecutionInfo executionInfo = new ExecutionInfo();
        executionInfo.setRequestUrl(url);
        executionInfo.setTemplate(template);
        executionInfos.push(executionInfo);
    }

    @Override
    public InvocationContextInfo getInfo() {
        InvocationContextInfo invocationContextInfo = new InvocationContextInfo();
        Stack<ExecutionInfo> executionInfos = threadLocals.get();
        if (executionInfos != null && !executionInfos.empty()) {
            ExecutionInfo executionInfo = executionInfos.pop();
            StringBuilder builder = new StringBuilder();
            builder.append("requestUrl: '").append(executionInfo.getRequestUrl()).append("';  ");
            builder.append("template: '").append(executionInfo.getTemplate()).append("'");
            invocationContextInfo.setDescription(builder.toString());
        }
        return invocationContextInfo;
    }
}
