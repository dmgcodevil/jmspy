package com.github.dmgcodevil.jmspy.example.circulardep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dmgcodevil on 11/9/2014.
 */
public class Task {

    private String name;
    private List<Task> subTasks = new ArrayList<>();

    public Task() {
    }

    public Task(String name, Task... subTasks) {
        this.name = name;
        this.subTasks.addAll(Arrays.asList(subTasks));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getSubTasks() {
        return subTasks;
    }

//    public void setSubTasks(List<Task> subTasks) {
//        this.subTasks = subTasks;
//    }
}
