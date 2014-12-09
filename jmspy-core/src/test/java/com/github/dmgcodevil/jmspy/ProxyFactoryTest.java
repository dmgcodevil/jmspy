package com.github.dmgcodevil.jmspy;

import com.github.dmgcodevil.jmspy.proxy.ProxyFactory;
import com.github.dmgcodevil.jmspy.test.data.User;
import org.testng.annotations.Test;

/**
 * Class description.
 *
 * @author Raman_Pliashkou
 */
public class ProxyFactoryTest {

    @Test
    public void testDelegateStrategy() {
        User user = new User();
        User proxy1 =  ProxyFactory.getInstance().create(user);
        User proxy2 =  ProxyFactory.getInstance().create(user);
    }
}
