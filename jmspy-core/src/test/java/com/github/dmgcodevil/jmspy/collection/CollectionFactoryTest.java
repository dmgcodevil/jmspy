package com.github.dmgcodevil.jmspy.collection;

import com.github.dmgcodevil.jmspy.MethodInvocationRecorder;
import org.testng.annotations.Test;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isCglibProxy;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Created by dmgcodevil on 12/24/2014.
 */
public class CollectionFactoryTest {

    @Test
    public void testGetDataJdkList() {
        CollectionFactory collectionFactory = new CollectionFactory(new Data("test"));
        MethodInvocationRecorder invocationRecorder = new MethodInvocationRecorder();
        CollectionFactory proxy = invocationRecorder.record(collectionFactory);
        assertTrue(isCglibProxy(proxy));
        assertTrue(isCglibProxy(proxy.getDataJdkList()));
        assertTrue(isCglibProxy(proxy.getDataJdkList().iterator()));
        assertTrue(isCglibProxy(proxy.getDataJdkList().iterator().next()));
        assertEquals(proxy.getDataJdkList().iterator().next().getBody(), "test");
    }

    @Test
    public void testGetDataJdkSet() {
        CollectionFactory collectionFactory = new CollectionFactory(new Data("test"));
        MethodInvocationRecorder invocationRecorder = new MethodInvocationRecorder();
        CollectionFactory proxy = invocationRecorder.record(collectionFactory);
        assertTrue(isCglibProxy(proxy));
        assertTrue(isCglibProxy(proxy.getDataJdkSet()));
        assertTrue(isCglibProxy(proxy.getDataJdkSet().iterator()));
        assertTrue(isCglibProxy(proxy.getDataJdkSet().iterator().next()));
        assertEquals(proxy.getDataJdkSet().iterator().next().getBody(), "test");
    }

    @Test
    public void testGetDataJdkLEmptyList() {
        CollectionFactory collectionFactory = new CollectionFactory(new Data("test"));
        MethodInvocationRecorder invocationRecorder = new MethodInvocationRecorder();
        CollectionFactory proxy = invocationRecorder.record(collectionFactory);
        assertTrue(isCglibProxy(proxy));
        assertFalse(isCglibProxy(proxy.getDataJdkEmptyList()));
    }

    @Test
    public void testGetDataJdkEmptySet() {
        CollectionFactory collectionFactory = new CollectionFactory(new Data("test"));
        MethodInvocationRecorder invocationRecorder = new MethodInvocationRecorder();
        CollectionFactory proxy = invocationRecorder.record(collectionFactory);
        assertTrue(isCglibProxy(proxy));
        assertFalse(isCglibProxy(proxy.getDataJdkEmptySet()));
    }

    @Test
    public void testGetDataJdkImmutableList() {
        CollectionFactory collectionFactory = new CollectionFactory(new Data("test"));
        MethodInvocationRecorder invocationRecorder = new MethodInvocationRecorder();
        CollectionFactory proxy = invocationRecorder.record(collectionFactory);
        assertTrue(isCglibProxy(proxy));
        Class type = proxy.getDataJdkImmutableList().getClass();
        assertTrue(isCglibProxy(proxy.getDataJdkImmutableList()));
        assertTrue(isCglibProxy(proxy.getDataJdkImmutableList().iterator()));
        assertTrue(isCglibProxy(proxy.getDataJdkImmutableList().iterator().next()));
        assertEquals(proxy.getDataJdkImmutableList().iterator().next().getBody(), "test");
    }

    @Test
    public void testGetDataJdkImmutableSet() {
        CollectionFactory collectionFactory = new CollectionFactory(new Data("test"));
        MethodInvocationRecorder invocationRecorder = new MethodInvocationRecorder();
        CollectionFactory proxy = invocationRecorder.record(collectionFactory);
        assertTrue(isCglibProxy(proxy.getDataJdkImmutableSet()));
        assertTrue(isCglibProxy(proxy.getDataJdkImmutableSet().iterator()));
        assertTrue(isCglibProxy(proxy.getDataJdkImmutableSet().iterator().next()));
        assertEquals(proxy.getDataJdkImmutableSet().iterator().next().getBody(), "test");
    }

    @Test
    public void testGetDataJdkMap() {
        CollectionFactory collectionFactory = new CollectionFactory(new Data("test"));
        MethodInvocationRecorder invocationRecorder = new MethodInvocationRecorder();
        CollectionFactory proxy = invocationRecorder.record(collectionFactory);
        assertTrue(isCglibProxy(proxy));
        assertTrue(isCglibProxy(proxy.getDataJdkMap()));
        assertTrue(isCglibProxy(proxy.getDataJdkMap().entrySet()));
        assertTrue(isCglibProxy(proxy.getDataJdkMap().entrySet().iterator()));
        assertTrue(isCglibProxy(proxy.getDataJdkMap().entrySet().iterator().next()));
        assertTrue(isCglibProxy(proxy.getDataJdkMap().entrySet().iterator().next().getKey()));
        assertTrue(isCglibProxy(proxy.getDataJdkMap().entrySet().iterator().next().getValue()));
        assertTrue(isCglibProxy(proxy.getDataJdkMap().keySet()));
        assertTrue(isCglibProxy(proxy.getDataJdkMap().keySet().iterator()));
        assertTrue(isCglibProxy(proxy.getDataJdkMap().keySet().iterator().next()));
        assertTrue(isCglibProxy(proxy.getDataJdkMap().values()));
        assertTrue(isCglibProxy(proxy.getDataJdkMap().values().iterator()));
        assertTrue(isCglibProxy(proxy.getDataJdkMap().values().iterator().next()));
    }

    @Test
    public void testGetDataJdkImmutableMap() throws ClassNotFoundException {
        CollectionFactory collectionFactory = new CollectionFactory(new Data("test"));
        MethodInvocationRecorder invocationRecorder = new MethodInvocationRecorder();
        CollectionFactory proxy = invocationRecorder.record(collectionFactory);
        assertTrue(isCglibProxy(proxy));
        assertTrue(isCglibProxy(proxy.getDataJdkImmutableMap()));
        assertTrue(isCglibProxy(proxy.getDataJdkImmutableMap().entrySet()));
        assertTrue(isCglibProxy(proxy.getDataJdkImmutableMap().entrySet().iterator()));
        assertTrue(isCglibProxy(proxy.getDataJdkImmutableMap().entrySet().iterator().next()));
        assertTrue(isCglibProxy(proxy.getDataJdkImmutableMap().entrySet().iterator().next().getKey()));
        assertTrue(isCglibProxy(proxy.getDataJdkImmutableMap().entrySet().iterator().next().getValue()));
        assertTrue(isCglibProxy(proxy.getDataJdkImmutableMap().keySet()));
        assertTrue(isCglibProxy(proxy.getDataJdkImmutableMap().keySet().iterator()));
        assertTrue(isCglibProxy(proxy.getDataJdkImmutableMap().keySet().iterator().next()));
        assertTrue(isCglibProxy(proxy.getDataJdkImmutableMap().values()));
        assertTrue(isCglibProxy(proxy.getDataJdkImmutableMap().values().iterator()));
        assertTrue(isCglibProxy(proxy.getDataJdkImmutableMap().values().iterator().next()));
    }

}
