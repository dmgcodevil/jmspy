package com.github.dmgcodevil.jmspy;

import com.github.dmgcodevil.jmspy.proxy.BeanCopier;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

/**
 * Created by dmgcodevil on 11/14/2014.
 */
public class BeanCopierTest {

    @Test
    public void testBeanCopy() {
        DomainC domainC = new DomainC();
        domainC.setDomainAName("domainA");
        domainC.setDomainBName("domainB");
        domainC.setId("1");
        DomainC copy = new DomainC();
        BeanCopier.getInstance().copy(domainC, copy);
        assertEquals(domainC.getId(), copy.getId());
        assertEquals(domainC.getDomainAName(), copy.getDomainAName());
        assertEquals(domainC.getDomainBName(), copy.getDomainBName());
    }

    private static class DomainA {
        private String name;

        public String getDomainAName() {
            return name;
        }

        public void setDomainAName(String name) {
            this.name = name;
        }
    }

    private static class DomainB extends DomainA {
        private String name;

        public String getDomainBName() {
            return name;
        }

        public void setDomainBName(String name) {
            this.name = name;
        }
    }


    private static class DomainC extends DomainB {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

}
