package com.github.dmgcodevil.jmspy;


import com.github.dmgcodevil.jmspy.example.Account;
import com.github.dmgcodevil.jmspy.example.Role;
import com.github.dmgcodevil.jmspy.example.User;
import com.github.dmgcodevil.jmspy.proxy.ProxyFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.testng.annotations.Test;

/**
 * Class description.
 *
 * @author Raman_Pliashkou
 */
@Deprecated
public class ProxyFactoryTest {

    @Test
    public void testDelegateStrategy() {
        User user = new User();
        user.setAccounts(Sets.newHashSet(new Account("acc")));
        user.setRoles(Lists.newArrayList(new Role()));
        User proxy = ProxyFactory.getInstance().create(user);
        proxy.getAccounts();
    }


}
