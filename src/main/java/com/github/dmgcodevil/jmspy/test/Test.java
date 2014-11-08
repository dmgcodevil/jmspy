package com.github.dmgcodevil.jmspy.test;

import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import com.github.dmgcodevil.jmspy.proxy.ProxyFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created by dmgcodevil on 11/7/2014.
 */
public class Test {

    public static void main(final String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        User user = new User();
        user.setId("1");
        user.setLogin("test_login");
        user.setPassword("test_password");
        user.setProfile(new Profile("test_email"));
        user.addRole(new Role("user"));
        user.addAccount(new Account("test"));

        InvocationGraph invocationGraph = InvocationGraph.create(user);
        User proxy = (User) new ProxyFactory(invocationGraph).create(user);
        for(Map.Entry<String, Account> entry: proxy.getAccounts().entrySet()){
            System.out.println(entry.getValue().getName());
        }
       System.out.println(proxy.getAccounts().entrySet().iterator().next().getValue().getName());

        System.out.println(proxy.getId());
        System.out.println(proxy.getLogin());
        System.out.println(proxy.getPassword());
        System.out.println(proxy.getProfile().getEmail());
        System.out.println(proxy.getRoles().iterator().next().getName());
        System.out.println(invocationGraph);
    }


}
