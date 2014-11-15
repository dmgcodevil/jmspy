package com.github.dmgcodevil.jmspy.example.empty;

import com.github.dmgcodevil.jmspy.MethodInvocationRecorder;

/**
 * Created by dmgcodevil on 11/14/2014.
 */
public class Test {

    public static void main(String[] args) {
        MethodInvocationRecorder invocationRecorder = new MethodInvocationRecorder();
        EmptyHolder emptyHolder = (EmptyHolder)invocationRecorder.record(new EmptyHolder());
        emptyHolder.getList();
        emptyHolder.getMap();
        emptyHolder.getSet();

    }
}
