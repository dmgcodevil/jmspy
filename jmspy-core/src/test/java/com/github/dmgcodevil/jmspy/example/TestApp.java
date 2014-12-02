package com.github.dmgcodevil.jmspy.example;

import com.github.dmgcodevil.jmspy.InvocationRecord;
import com.github.dmgcodevil.jmspy.MethodInvocationRecorder;
import com.google.common.collect.Lists;

/**
 * Class description.
 *
 * @author Raman_Pliashkou
 */
public class TestApp {

    public static void main(String[] args) {
        Role adminRole = new Role("admin");
        Role userRole = new Role("user");
        Role guestRole = new Role("guest");
        User user = new User();
        user.setRoles(Lists.newArrayList(adminRole, userRole, guestRole));
        user.setLogin("login");
        user.setPassword("pass");
        MethodInvocationRecorder invocationRecorder = new MethodInvocationRecorder();
        User proxy = (User) invocationRecorder.record(user);
        int count = 0;
        for (Role role : proxy.getRoles()) {
            System.out.println(role.getName());
            if (count == 1) {
                System.out.println(role.isActive());
            }
            count++;
        }
        InvocationRecord invocationRecord = invocationRecorder.getInvocationRecords().iterator().next();
        System.out.println(invocationRecord);
    }
}
