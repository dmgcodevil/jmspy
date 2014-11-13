package com.github.dmgcodevil.jmspy.example.jdkproxy;

import com.github.dmgcodevil.jmspy.MethodInvocationRecorder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Class description.
 *
 * @author Raman_Pliashkou
 */
public class Test {
    public static void main(String[] args) {
        ISomeObject someObject = (ISomeObject) Proxy.newProxyInstance(ISomeObject.class.getClassLoader(),
                new Class<?>[]{ISomeObject.class}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("catch someObject.process()");
                        return null;
                    }
                });
        SomeObjectHolder someObjectHolder = new SomeObjectHolder();
        someObjectHolder.setSomeObject(someObject);
        MethodInvocationRecorder invocationRecorder = new MethodInvocationRecorder();
        SomeObjectHolder proxy = (SomeObjectHolder)invocationRecorder.record(someObjectHolder);
        proxy.getSomeObject().process();
        System.out.println(invocationRecorder.getInvocationRecords());
    }
}
