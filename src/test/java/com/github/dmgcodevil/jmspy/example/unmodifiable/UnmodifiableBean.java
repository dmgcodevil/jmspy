package com.github.dmgcodevil.jmspy.example.unmodifiable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class description.
 *
 * @author Raman_Pliashkou
 */
public class UnmodifiableBean {

    private List<String> stringList = new ArrayList<>();
    private Map<String, String> stringMap = new HashMap<>();
    private Set<String> stringSet = new HashSet<>();

    public List<String> getStringList() {
        return Collections.unmodifiableList(stringList);
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    public Map<String, String> getStringMap() {
        return Collections.unmodifiableMap(stringMap);
    }

    public void setStringMap(Map<String, String> stringMap) {
        this.stringMap = stringMap;
    }

    public Set<String> getStringSet() {
        return Collections.unmodifiableSet(stringSet);
    }

    public void setStringSet(Set<String> stringSet) {
        this.stringSet = stringSet;
    }
}
