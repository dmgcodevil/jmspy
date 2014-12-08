package com.github.dmgcodevil.jmspy;

import com.github.dmgcodevil.jmspy.proxy.EnhancerFactory;
import com.github.dmgcodevil.jmspy.proxy.ProxyFactory;
import com.github.dmgcodevil.jmspy.proxy.ReflectionUtils;
import org.testng.annotations.Test;

/**
 * Created by dmgcodevil on 12/8/2014.
 */
public class EnhancerFactoryTest {

    @Test
    public void createProxy() {
        EnhancerFactory enhancerFactory = new EnhancerFactory();
        TestClass testClass = new TestClass("test");

        TestInterface proxy = ProxyFactory.getInstance().create(testClass);
        System.out.println(proxy.getText());
    }

    public static interface TestInterface {
        String getText();
    }

    public static class TestClass implements TestInterface{
        private String text;

        public TestClass(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}
