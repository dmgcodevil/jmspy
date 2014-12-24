package com.github.dmgcodevil.jmspy.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by dmgcodevil on 12/24/2014.
 */
public class CollectionFactory {

    /**
     * List.
     */
    private List<Data> dataJdkList = new ArrayList<>();
    private List<Data> dataJdkEmptyList = Collections.emptyList();

    /**
     * Set section.
     */
    private Set<Data> dataJdkSet = new HashSet<>();
    private Set<Data> dataJdkEmptySet = Collections.emptySet();

    private Map<Data, Data> dataJdkMap = new HashMap<>();
    private Map<Data, Data> dataJdkEmptyMap = Collections.emptyMap();

    public CollectionFactory(Data... data) {
        dataJdkList.addAll(Arrays.asList(data));
        dataJdkSet.addAll(Arrays.asList(data));
        for (Data _data : data) {
            dataJdkMap.put(_data, _data);
        }
    }

    public List<Data> getDataJdkList() {
        return dataJdkList;
    }

    public List<Data> getDataJdkImmutableList() {
        return Collections.unmodifiableList(dataJdkList);
    }

    public Set<Data> getDataJdkSet() {
        return dataJdkSet;
    }

    public Set<Data> getDataJdkImmutableSet() {
        return Collections.unmodifiableSet(dataJdkSet);
    }

    public List<Data> getDataJdkEmptyList() {
        return dataJdkEmptyList;
    }

    public Set<Data> getDataJdkEmptySet() {
        return dataJdkEmptySet;
    }

    public Map<Data, Data> getDataJdkMap() {
        return dataJdkMap;
    }

    public Map<Data, Data> getDataJdkEmptyMap() {
        return dataJdkEmptyMap;
    }

    public Map<Data, Data> getDataJdkImmutableMap() {
        return Collections.unmodifiableMap(dataJdkMap);
    }


}
