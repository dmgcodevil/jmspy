package com.github.dmgcodevil.jmspy.example.empty;

import java.util.*;

/**
 * Created by dmgcodevil on 11/14/2014.
 */
public class EmptyHolder {

    List<String> list = Collections.emptyList();
    Set<String> set = Collections.emptySet();
    Map<String, String> map = Collections.emptyMap();
    Date date = null;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public Set<String> getSet() {
        return set;
    }

    public void setSet(Set<String> set) {
        this.set = set;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
