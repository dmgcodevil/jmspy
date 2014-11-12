package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.getOriginalType;

/**
 * Created by dmgcodevil on 11/7/2014.
 */
public class SetterInterceptor implements MethodInterceptor {

    InvocationGraph invocationGraph;

    public SetterInterceptor(InvocationGraph invocationGraph) {
        this.invocationGraph = invocationGraph;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("--SetterInterceptor--");
        Field field = getField(o, method);
        if (field!= null && field.getAnnotation(NotProxy.class) != null) {
            return methodProxy.invokeSuper(o, args);
        }
        if (isCglibSynthetic(method)) {

            if (field != null) {
                field.setAccessible(true);
                field.set(o, processArgs(field, args)[0]);
            } else {
                System.out.println("cannot find property to set value. setter method: '"
                        + method.getName() + "' in class: '" + getOriginalType(o) + "'");
            }
            return null;
        }
        return methodProxy.invokeSuper(o, processArgs(field, args));
    }

    //net.sf.cglib.empty.Object$$InterfaceMakerByCGLIB$$1775831e

    private boolean isCglibSynthetic(Method method) {
        return method.getDeclaringClass().getName().contains("$$InterfaceMakerByCGLIB$$");
    }

    private String getFieldName(String methodName) {
        return methodName.replace("set", "");
    }

    private String normalize(String fieldName) {
        return fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
    }

    private Field getField(Object object, Method method) {
        final String fieldName = normalize(getFieldName(method.getName()));
        Class<?> type = getOriginalType(object);
        return Iterables.tryFind(Arrays.asList(type.getDeclaredFields()), new Predicate<Field>() {
            @Override
            public boolean apply(Field input) {
                return input.getName().equals(fieldName);
            }
        }).orNull();
    }


    private Object[] processArgs(Field field, Object[] args) {
        Object arg = args[0];
        if (arg != null && CommonUtils.isNotPrimitiveOrWrapper(arg.getClass())) {
            args[0] = new ProxyFactory(invocationGraph).create(arg, getType(field));
        }
        return args;
    }

    private Type getType(Field field) {
        Type.Builder builder = Type.builder().target(field.getType());
        java.lang.reflect.Type rtype = field.getGenericType();
        if (rtype instanceof ParameterizedType) {
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            if (genericType.getActualTypeArguments().length > 0) {
                builder.parameterizedTypes(genericType.getActualTypeArguments());
            }
        }
        return builder.build();
    }

}
