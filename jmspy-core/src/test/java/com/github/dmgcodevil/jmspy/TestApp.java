package com.github.dmgcodevil.jmspy;

import com.github.dmgcodevil.jmspy.example.Candidate;
import com.github.dmgcodevil.jmspy.test.data.Contact;
import com.github.dmgcodevil.jmspy.test.data.User;
import net.sf.cglib.core.TypeUtils;

import static com.github.dmgcodevil.jmspy.proxy.ReflectionUtils.hasDefaultConstructor;

/**
 * -javaagent:jmspy-core/lib/jmspy-agent.jar=com.github.dmgcodevil.jmspy.example.Candidate.class,com.github.dmgcodevil.jmspy.test.data
 */
@Deprecated
public class TestApp {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {


        User user = new User();
        Contact contact = new Contact("test");
        Candidate candidate = new Candidate("");
        System.out.println(!TypeUtils.isFinal(user.getClass().getModifiers()));
        System.out.println(hasDefaultConstructor(contact.getClass()));
        System.out.println(!TypeUtils.isFinal(candidate.getClass().getModifiers()));
        System.out.println(hasDefaultConstructor(candidate.getClass()));
    }
}
