package com.github.dmgcodevil.jmspy.proxy;

import java.lang.reflect.Field;

/**
 * Created by dmgcodevil on 11/14/2014.
 */
public interface SetFieldInterceptor {

    void intercept(Object from, Field fromField, Object to, Field toField) throws Throwable;
}
