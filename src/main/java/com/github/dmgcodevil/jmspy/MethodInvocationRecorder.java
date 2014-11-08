package com.github.dmgcodevil.jmspy;

import com.github.dmgcodevil.jmspy.proxy.ProxyFactory;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public class MethodInvocationRecorder {


    private List<InvocationRecord> invocationRecords;

    private InvocationContext invocationContext;
    private ProxyFactory proxyFactory;

    public Object record(Object target) {
        return target;
    }

    public Object record(Method method, Object target) {
        return target;
    }
}
