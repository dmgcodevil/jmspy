package com.github.dmgcodevil.jmspy.example.circulardep;

import com.github.dmgcodevil.jmspy.MethodInvocationRecorder;

/**
 * Created by dmgcodevil on 11/9/2014.
 */
public class Test {
    public static void main(String[] args) {
        Task task1= new Task("task_1", new Task("task_2", new Task("task_3")));
        MethodInvocationRecorder invocationRecorder = new MethodInvocationRecorder();
        Task proxy = (Task) invocationRecorder.record(task1);

       // proxy.getName();
        proxy.getSubTasks().iterator().next().getSubTasks().iterator().next().getName();
        System.out.println(invocationRecorder.getInvocationRecords().iterator().next().getInvocationGraph());
    }
}
