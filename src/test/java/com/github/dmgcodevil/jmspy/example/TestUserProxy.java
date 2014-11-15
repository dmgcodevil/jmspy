package com.github.dmgcodevil.jmspy.example;

import com.github.dmgcodevil.jmspy.MethodInvocationRecorder;
import com.github.dmgcodevil.jmspy.proxy.ProxyFactory;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.getAllFields;

/**
 * Class description.
 *
 * @author Raman_Pliashkou
 */
public class TestUserProxy {

    @org.testng.annotations.Test
    public void testCreateUserProxy() {
        User user = new User();
        user.setId("1");
        user.setLogin("test_login");
        user.setPassword("test_password");
        user.setProfile(new Profile("test_email"));
        user.addRole(new Role("user"));
        Account account = new Account("test");
        CloudService[] cloudServices = new CloudService[]{new CloudService("test_cs")};
        account.setCloudService(cloudServices);
        user.addAccount(account);
        user.addAccount(new Account("test2"));


        MethodInvocationRecorder invocationRecorder = new MethodInvocationRecorder(ProxyFactory.getInstance());
        User proxy = (User) invocationRecorder.record(user);


        for (Map.Entry<String, Account> entry : proxy.getAccounts().entrySet()) {
            System.out.println(entry.getValue().getName());
            if (entry.getValue().getCloudService() != null && entry.getValue().getCloudService().length > 0) {
                for (CloudService cloudService : entry.getValue().getCloudService()) {
                    System.out.println(cloudService.getName());
                }
            }
        }


        System.out.println(proxy.getId());
        System.out.println(proxy.getLogin());
        System.out.println(proxy.getPassword());
        System.out.println(proxy.getProfile().getEmail());
        System.out.println(proxy.getRoles().iterator().next().getName());
        System.out.println(proxy.getRoles().iterator().next().getName());
        //System.out.println(proxy.getAccounts());
        System.out.println(invocationRecorder.getInvocationRecords().iterator().next().getInvocationGraph());
    }

    public static void main(String[] args) throws IllegalAccessException {
        List<Field> list = new ArrayList<>();
        list = getAllFields(list, User.class);
        Field field = Iterables.tryFind(list, new Predicate<Field>() {
            @Override
            public boolean apply(Field input) {
                return input.getName().equals("id");
            }
        }).get();
        field.setAccessible(true);
        User user = new User();
        field.set(user, "1");
        System.out.println(user.getId());
    }
}
