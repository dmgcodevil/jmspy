package com.github.dmgcodevil.jmspy;

import com.github.dmgcodevil.jmspy.test.data.Contact;
import com.github.dmgcodevil.jmspy.test.data.User;
import net.sf.cglib.core.TypeUtils;

import static com.github.dmgcodevil.jmspy.proxy.ReflectionUtils.hasDefaultConstructor;

/**
 * -javaagent:jmspy-core/lib/jmspy-agent.jar
 */
public class TestApp {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {


        User user = new User();
        Contact contact = new Contact("test");
        System.out.println(TypeUtils.isFinal(user.getClass().getModifiers()));
        System.out.println(hasDefaultConstructor(contact.getClass()));
    }
}
