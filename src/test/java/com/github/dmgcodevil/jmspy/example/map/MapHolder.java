package com.github.dmgcodevil.jmspy.example.map;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class description.
 *
 * @author Raman_Pliashkou
 */
public class MapHolder {
    Map<String, Object> stringObjectMap = new LinkedHashMap<>();

    public Map<String, Object> getStringObjectMap() {
        return stringObjectMap;
    }

    public void setStringObjectMap(Map<String, Object> stringObjectMap) {
        this.stringObjectMap = stringObjectMap;
    }
}
