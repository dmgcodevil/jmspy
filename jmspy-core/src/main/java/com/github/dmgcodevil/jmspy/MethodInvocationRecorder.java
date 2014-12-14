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
 * Class provides several methods to start invocation recording for some objects.
 * <p/>
 * Basically overall process comprises several steps, namely:
 * 1. Create proxy for the target object.
 * 2. Create invocation record
 * <p/>
 * There is ability to register {@link ContextExplorer} to get more information about execution context.
 * Basically this information will be common for all invocation records
 * thus it doesn't make sense to register multiple explorers.
 * <p/>
 * Also there is ability to save invocation snapshot.
 *
 * @author dmgcodevil
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

    public MethodInvocationRecorder(ContextExplorer contextExplorer) {
        this();
        this.contextExplorer = contextExplorer;
    }

    public MethodInvocationRecorder(ContextExplorer contextExplorer, ProxyFactory proxyFactory) {
        this.contextExplorer = contextExplorer;
        this.proxyFactory = proxyFactory;
    }

    public List<InvocationRecord> getInvocationRecords() {
        return invocationRecords;
    }

    public <T> T record(T target) {
        return record(null, target);
    }

    public <T> T record(Method method, T target) {
        if (isNotPrimitiveOrWrapper(target)) {
            InvocationRecord invocationRecord = new InvocationRecord(createInvocationContext(method),
                    InvocationGraph.create(target));
            T proxy = (T) proxyFactory.create(target, invocationRecord);
            invocationRecords.add(invocationRecord);
            return proxy;
        } else {
            return target;
        }
    }

    public Snapshot makeSnapshot() {
        return Snapshot.save(new Snapshot(invocationRecords));
    }

    public Snapshot saveSnapshot(Snapshot snapshot) {
        return Snapshot.save(snapshot);
    }

    private InvocationContext createInvocationContext(Method method) {
        return InvocationContext.builder()
                .root(method)
                .contextExplorer(contextExplorer)
                .stackTrace(Thread.currentThread().getStackTrace())
                .build();
    }
}
