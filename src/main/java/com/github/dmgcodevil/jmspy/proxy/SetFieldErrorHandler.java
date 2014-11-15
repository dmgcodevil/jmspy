package com.github.dmgcodevil.jmspy.proxy;

import java.lang.reflect.Field;

/**
 * Created by dmgcodevil on 11/14/2014.
 */
public interface SetFieldErrorHandler {

    public void handle(Object to, Field field, Throwable error);
}
