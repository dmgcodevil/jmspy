package com.github.dmgcodevil.jmspy.example.map;

import com.github.dmgcodevil.jmspy.MethodInvocationRecorder;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class description.
 *
 * @author Raman_Pliashkou
 */
public class Test {

    public static void main(String[] args) {
        MethodInvocationRecorder invocationRecorder = new MethodInvocationRecorder();
        MapHolder mapHolder = new MapHolder();
        Map<String, Object> stringObjectMap = new HashMap<>();
        stringObjectMap.put("1", "test");
        mapHolder.setStringObjectMap(stringObjectMap);
        MapHolder s = (MapHolder)invocationRecorder.record(mapHolder);
        s.getStringObjectMap().values();
        s.getStringObjectMap().keySet();
        System.out.println(s);

    }
}
