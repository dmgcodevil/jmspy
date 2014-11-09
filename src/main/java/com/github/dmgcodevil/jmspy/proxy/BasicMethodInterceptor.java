package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.graph.Edge;
import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import com.github.dmgcodevil.jmspy.graph.Node;
import com.github.dmgcodevil.jmspy.proxy.wrappers.Wrapper;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.*;

/**
 * Created by dmgcodevil on 11/7/2014.
 */
public class BasicMethodInterceptor implements MethodInterceptor {
    InvocationGraph invocationGraph;
    private final Object lock = new Object();

    public BasicMethodInterceptor(InvocationGraph invocationGraph) {
        this.invocationGraph = invocationGraph;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        // calls super method
        Object out = null;
        synchronized (lock) {
            out = proxy.invokeSuper(obj, args);

            if (invocationGraph != null &&
                    method.getDeclaringClass() != Object.class) {
                String parentId = getIdentifier(obj);
                Node node = invocationGraph.findById(parentId);
                if (node == null) {
                    return out;
                }

                if (out != null) {
                    if (isNotPrimitiveOrWrapper(out.getClass()) && !isCglibProxy(out)) {
                        out = new ProxyFactory(invocationGraph).create(out);
                    }

                    String outId = getIdentifier(out);
                    Node toNode = invocationGraph.findById(outId);


                    if (toNode == null) {
                        toNode = new Node();
                        toNode.setId(outId);
                        toNode.setType(getOriginalType(out));
                        toNode.setId(outId);
                    }
                    Edge edge = new Edge();
                    edge.setMethod(method);
                    edge.setFrom(node);
                    edge.setTo(toNode);

                    //toNode.addMethod(method);
                    node.addOutgoingEdge(edge);
                }

            }
        }
        return out;
    }

    private String getIdentifier(Object obj) throws InvocationTargetException, IllegalAccessException {
        if (obj == null) {
            return null;
        }
        if (isCglibProxy(obj)) {
            try {
                Method method = obj.getClass().getMethod(Constants.GET_PROXY_IDENTIFIER);
                return (String) method.invoke(obj);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
