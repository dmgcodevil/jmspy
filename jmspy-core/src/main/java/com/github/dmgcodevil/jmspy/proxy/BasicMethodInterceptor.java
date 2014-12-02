package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.graph.Edge;
import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import com.github.dmgcodevil.jmspy.graph.Node;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.getOriginalType;
import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isArray;
import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isCglibProxy;
import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isUnmodifiable;

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
                    if (!isCglibProxy(out) &&
                            !isArray(out.getClass()) &&
                            !isUnmodifiable(out)) {
                        out =  ProxyFactory.getInstance().create(out, invocationGraph);
                        //out =  ProxyFactory.getInstance().create(out);
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
        if (obj.getClass().isArray() && Array.getLength(obj) > 0) {
            obj = Array.get(obj, 0);
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
