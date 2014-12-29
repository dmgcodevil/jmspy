package com.github.dmgcodevil.jmspy.proxy.callback;

import com.github.dmgcodevil.jmspy.InvocationRecord;
import com.github.dmgcodevil.jmspy.context.ContextExplorer;
import com.github.dmgcodevil.jmspy.context.InvocationContextInfo;
import com.github.dmgcodevil.jmspy.graph.Edge;
import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import com.github.dmgcodevil.jmspy.graph.Node;
import com.github.dmgcodevil.jmspy.proxy.JMethod;
import com.github.dmgcodevil.jmspy.proxy.JType;
import com.github.dmgcodevil.jmspy.proxy.ProxyFactory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.getOriginalType;
import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isArray;
import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isCglibProxy;
import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isJdkProxy;

/**
 * Intercepts methods and adds new edges and nodes to the invocation graph.
 * <p/>
 * This interceptor has some restrictions that may be eliminated in future:
 * Arrays aren't supported, since array is specific type in java thus we can't just wrap it a wrapper and instrument it as well.
 * Unmodifiable collections and maps aren't supported, will be fixed in the near future.
 *
 * @author dmgcodevil
 */
public class BasicMethodInterceptor implements MethodInterceptor {

    public static int INDEX = 1;

    private final InvocationRecord invocationRecord;
    private final Object original;
    private final Lock lock = new ReentrantLock();

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public BasicMethodInterceptor(Object original, InvocationRecord invocationRecord) {
        this.invocationRecord = invocationRecord;
        this.original = original;
    }

    public BasicMethodInterceptor(InvocationRecord invocationRecord) {
        this.invocationRecord = invocationRecord;
        this.original = null;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        try {
            lock.lock();
            // skip method if return type is Void
            if (method.getReturnType().equals(Void.TYPE)) {
                return proxy.invokeSuper(obj, args);
            }
            Object out = invoke(obj, method, args, proxy);
            InvocationGraph invocationGraph = getInvocationGraph();
            if (invocationGraph != null && method.getDeclaringClass() != Object.class) {
                String parentId = getIdentifier(obj);
                Node node = invocationGraph.findById(parentId);
                if (node == null) {
                    return out;
                }

                if (out != null) {
                    if (!isArray(out.getClass())) {
                        out = ProxyFactory.getInstance().create(out, invocationRecord);
                    }
                }
                String outId = getIdentifier(out);
                Node toNode = invocationGraph.findById(outId);


                if (toNode == null) {
                    toNode = new Node();
                    toNode.setId(outId);
                    toNode.setType(new JType(getOriginalType(out)));
                    toNode.setId(outId);
                }
                Edge edge = new Edge();
                edge.setMethod(new JMethod(method));
                edge.setContextInfo(getCurrentContextInfo());
                edge.setFrom(node);
                edge.setTo(toNode);

                node.addOutgoingEdge(edge);
            }
            return out;
        } finally {
            lock.unlock();
        }
    }

    private String getIdentifier(Object obj) throws InvocationTargetException, IllegalAccessException {
        if (obj == null) {
            return null;
        }
        if (obj.getClass().isArray() && Array.getLength(obj) > 0) {
            obj = Array.get(obj, 0);
        }
        if (isCglibProxy(obj)) {
            try {
                Method method = obj.getClass().getMethod(ProxyIdentifierCallback.GET_PROXY_IDENTIFIER);
                return (String) method.invoke(obj);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    private InvocationContextInfo getCurrentContextInfo() {
        if (invocationRecord == null) {
            return null;
        }
        ContextExplorer contextExplorer = invocationRecord.getInvocationContext() != null ?
                invocationRecord.getInvocationContext().getContextExplorer() : null;
        return contextExplorer != null ? contextExplorer.getCurrentContextInfo() : null;
    }

    private InvocationGraph getInvocationGraph() {
        return invocationRecord != null ? invocationRecord.getInvocationGraph() : null;
    }

    private Object invoke(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Object result;
        if (original != null) {
            result = method.invoke(original, args);
            if (isJdkProxy(result)) {
                // init
                logger.debug("post processing: jdk proxy");
                JdkProxyPostMethodInvocationProcessor.getInstance().process(result);
                // load again
                result = method.invoke(original, args);
                if (isJdkProxy(result)) {
                    logger.error("failed to unwrap jdk proxy, type: '{}'" + result.getClass());
                }
            }
        } else {
            result = proxy.invokeSuper(obj, args);
        }
        return result;
    }

}
