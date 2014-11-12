package com.github.dmgcodevil.jmspy.example;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public class Account {

    private String name;
    private CloudService[] cloudService;

    public Account() {
    }


    public Account(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CloudService[] getCloudService() {
        return cloudService;
    }

    public void setCloudService(CloudService[] cloudService) {
        this.cloudService = cloudService;
    }
}
