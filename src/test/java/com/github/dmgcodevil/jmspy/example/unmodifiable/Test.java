package com.github.dmgcodevil.jmspy.example.unmodifiable;

import com.github.dmgcodevil.jmspy.MethodInvocationRecorder;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.HashMap;
import java.util.Map;

/**
 * Class description.
 *
 * @author Raman_Pliashkou
 */
public class Test {


    public static void main(String[] args) {
        MethodInvocationRecorder invocationRecorder = new MethodInvocationRecorder();
        UnmodifiableBean unmodifiableBean = new UnmodifiableBean();
        unmodifiableBean.setStringList(Lists.newArrayList("1"));
        unmodifiableBean.setStringSet(Sets.newHashSet("2"));
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("3", "3");
        unmodifiableBean.setStringMap(stringMap);
        UnmodifiableBean proxy = (UnmodifiableBean) invocationRecorder.record(unmodifiableBean);
        System.out.println(proxy.getStringList());
        System.out.println(proxy.getStringSet());
        System.out.println(proxy.getStringMap());

        // proxy.getName();

        System.out.println(invocationRecorder.getInvocationRecords().iterator().next().getInvocationGraph());
    }

}
