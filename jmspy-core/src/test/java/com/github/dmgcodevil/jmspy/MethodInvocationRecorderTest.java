package com.github.dmgcodevil.jmspy;

import com.github.dmgcodevil.jmspy.example.Account;
import com.github.dmgcodevil.jmspy.example.Role;
import com.github.dmgcodevil.jmspy.example.User;
import com.github.dmgcodevil.jmspy.proxy.ProxyFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.testng.annotations.Test;

import java.util.Iterator;
import java.util.Set;

/**
 * Class description.
 *
 * @author Raman_Pliashkou
 */
@Deprecated
public class MethodInvocationRecorderTest {
    @Test
    public void testRecord() throws NoSuchMethodException {
        User user = new User();
        user.setAccounts(Sets.newHashSet(new Account("acc")));
        user.setRoles(Lists.newArrayList(new Role()));
        MethodInvocationRecorder methodInvocationRecorder = new MethodInvocationRecorder();
        User proxy = methodInvocationRecorder.record(MethodInvocationRecorderTest.class.getMethod("testRecord"), user);
        Set<Account> accountSet = proxy.getAccounts();
        Iterator<Account> iterator = accountSet.iterator();
        Account account = iterator.next();
        System.out.println(account.getName());
        InvocationRecord invocationRecord = methodInvocationRecorder.getInvocationRecords().iterator().next();
        methodInvocationRecorder.makeSnapshot();

    }
}
