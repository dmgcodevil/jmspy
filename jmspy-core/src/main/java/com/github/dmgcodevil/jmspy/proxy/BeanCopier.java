package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.exception.BeanCopierException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.getOriginalType;

/**
 * Created by dmgcodevil on 11/14/2014.
 */
public class BeanCopier {

    private SetFieldInterceptor setFieldInterceptor = DEFAULT_SET_FIELD_INTERCEPTOR;
    private SetFieldErrorHandler setFieldErrorHandler = DEFAULT_ERROR_HANDLER;

    private static final SetFieldInterceptor DEFAULT_SET_FIELD_INTERCEPTOR = new SetFieldInterceptor() {
        @Override
        public void intercept(Object from, Field fromField, Object to, Field toField) throws Throwable {
            toField.set(to, fromField.get(from));
        }
    };

    private static final SetFieldErrorHandler DEFAULT_ERROR_HANDLER = new SetFieldErrorHandler() {
        @Override
        public void handle(Object target, Field field, Throwable error) {
            error.printStackTrace();
            System.out.println("failed to set a value to field: '" + field + "', error: " + error);
        }
    };

    private static final BeanCopier BEAN_COPIER = new BeanCopier();

    public BeanCopier() {
    }

    public BeanCopier(SetFieldInterceptor setFieldInterceptor) {
        this.setFieldInterceptor = setFieldInterceptor;
    }

    public void setSetFieldInterceptor(SetFieldInterceptor setFieldInterceptor) {
        this.setFieldInterceptor = setFieldInterceptor;
    }

    @Deprecated
    public static BeanCopier getInstance() {
        return BEAN_COPIER;
    }

    public void copy(Object from, Object to) {
        Class<?> fromType = getOriginalType(from);
        Class<?> toType = getOriginalType(from);
        if (!fromType.equals(toType)) {
            throw new BeanCopierException("objects have different classes");
        }
        List<Field> fromFields = getFields(from);
        List<Field> toFields = getFields(to);
        for (Field fromField : fromFields) {
            if (toFields.contains(fromField)) {
                int index = toFields.indexOf(fromField);
                Field toField = null;
                try {
                    toField = toFields.get(index);
                    if (!Modifier.isFinal(toField.getModifiers())) {
                        makeAccessible(fromField);
                        makeAccessible(toField);
                        setFieldInterceptor.intercept(from, fromField, to, toFields.get(index));
                    }
                } catch (Throwable throwable) {
                    setFieldErrorHandler.handle(to, toField, throwable);
                }
            }
        }
    }

    private List<Field> getFields(Object object) {
        return ReflectionUtils.getAllFields(object.getClass());
    }

    private void makeAccessible(Field field) {
        field.setAccessible(true);
    }

}
