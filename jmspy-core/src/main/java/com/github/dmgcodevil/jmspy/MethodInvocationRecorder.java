package com.github.dmgcodevil.jmspy;

import com.github.dmgcodevil.jmspy.context.ContextExplorer;
import com.github.dmgcodevil.jmspy.context.InvocationContext;
import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import com.github.dmgcodevil.jmspy.proxy.ProxyFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isNotPrimitiveOrWrapper;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public class MethodInvocationRecorder {


    private List<InvocationRecord> invocationRecords = new ArrayList<>();
    private ContextExplorer contextExplorer;
    private ProxyFactory proxyFactory;

    public MethodInvocationRecorder() {
        proxyFactory = ProxyFactory.getInstance();
    }

    public MethodInvocationRecorder(ProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
    }

    public List<InvocationRecord> getInvocationRecords() {
        return invocationRecords;
    }

    public Object record(Object target) {
        return record(null, target);
    }

    public Object record(Method method, Object target) {
        if (isNotPrimitiveOrWrapper(target)) {
            InvocationGraph invocationGraph = InvocationGraph.create(target);
            Object proxy = proxyFactory.create(target, invocationGraph);
            InvocationRecord invocationRecord = new InvocationRecord(createInvocationContext(method), invocationGraph);
            invocationRecords.add(invocationRecord);
            return proxy;
        } else {
            return target;
        }
    }

    public Snapshot makeSnapshot() {
        return Snapshot.save(new Snapshot(invocationRecords));
    }

    private InvocationContext createInvocationContext(Method method) {
        return InvocationContext.builder().root(method)
                .contextExplorer(contextExplorer)
                .stackTrace(Thread.currentThread().getStackTrace())
                .build();
    }
}
