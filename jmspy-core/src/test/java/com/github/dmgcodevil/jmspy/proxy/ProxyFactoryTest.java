package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.proxy.Configuration;
import com.github.dmgcodevil.jmspy.proxy.EnhancerFactory;
import com.github.dmgcodevil.jmspy.proxy.ProxyFactory;
import com.github.dmgcodevil.jmspy.proxy.wrapper.AbstractWrapper;
import com.github.dmgcodevil.jmspy.proxy.DefaultWrapper;
import com.github.dmgcodevil.jmspy.proxy.wrapper.Wrapper;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isCglibProxy;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertTrue;

/**
 * Unit test for {@link ProxyFactory}.
 * <p/>
 * Created by dmgcodevil on 12/29/2014.
 */
public class ProxyFactoryTest {

    private ProxyFactory proxyFactory = ProxyFactory.getInstance();

    @BeforeMethod
    public void prepareProxyFactory() throws NoSuchFieldException, IllegalAccessException {
        Field instance = proxyFactory.getClass().getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(proxyFactory, null);
    }

    @Test
    public void create_givenSimpleObject_shouldCreateProxy() {
        // given
        EnhancerFactory enhancerFactory = spy(new EnhancerFactory());
        Configuration configuration = Configuration.builder().enhancerFactory(enhancerFactory).build();
        ProxyFactory proxyFactory = ProxyFactory.getInstance(configuration);
        Simple simple = new Simple();
        // when
        Simple proxy = proxyFactory.create(simple);
        // then
        assertTrue(isCglibProxy(proxy));
        verify(enhancerFactory).createEnhancer(simple.getClass());
        verify(enhancerFactory, never()).createEnhancerWithWrapper(simple.getClass(), DefaultWrapper.class);
    }

    @Test
    public void create_givenUnmodifiableArrayList_shouldCreateProxyUsingWrapper() {
        // given
        EnhancerFactory enhancerFactory = spy(new EnhancerFactory());
        Configuration configuration = Configuration.builder().enhancerFactory(enhancerFactory).build();
        ProxyFactory proxyFactory = ProxyFactory.getInstance(configuration);
        List<String> list = Collections.unmodifiableList(new ArrayList<String>());
        // when
        List<String> proxy = proxyFactory.create(list);
        // then
        assertTrue(isCglibProxy(proxy));
        verify(enhancerFactory, atLeastOnce()).createEnhancer(list.getClass());
        verify(enhancerFactory).createEnhancerWithWrapper(list.getClass(), DefaultWrapper.class);
        assertTrue(Wrapper.class.isAssignableFrom(proxy.getClass()));

    }

    @Test
    public void create_givenFinalClass_shouldCreateProxyUsingWrapper() {
        // given
        EnhancerFactory enhancerFactory = spy(new EnhancerFactory());
        Configuration configuration = Configuration.builder().enhancerFactory(enhancerFactory).build();
        ProxyFactory proxyFactory = ProxyFactory.getInstance(configuration);
        Final aFinal = new FinalClass();
        // when
        Final proxy = proxyFactory.create(aFinal);
        // then
        assertTrue(isCglibProxy(proxy));
        verify(enhancerFactory, atLeastOnce()).createEnhancer(aFinal.getClass());
        verify(enhancerFactory).createEnhancerWithWrapper(aFinal.getClass(), DefaultWrapper.class);
        assertTrue(Wrapper.class.isAssignableFrom(proxy.getClass()));
    }

    @Test
    public void create_givenFinalClassAndInterfaceBasedWrapper_shouldCreateProxyUsingWrapper() {
        // given
        EnhancerFactory enhancerFactory = spy(new EnhancerFactory());
        Configuration configuration = Configuration.builder().enhancerFactory(enhancerFactory)
                .registerWrapper(Final.class, FinalClassWrapper.class)
                .build();
        ProxyFactory proxyFactory = ProxyFactory.getInstance(configuration);
        Final aFinal = new FinalClass();
        // when
        Final proxy = proxyFactory.create(aFinal);
        // then
        assertTrue(isCglibProxy(proxy));
        verify(enhancerFactory, atLeastOnce()).createEnhancer(aFinal.getClass());
        verify(enhancerFactory).createEnhancerWithWrapper(aFinal.getClass(), FinalClassWrapper.class);
        assertTrue(Wrapper.class.isAssignableFrom(proxy.getClass()));
    }


    @Test
    public void create_givenFinalClassAndClassBasedWrapper_shouldCreateProxyUsingWrapper() {
        // given
        EnhancerFactory enhancerFactory = spy(new EnhancerFactory());
        Configuration configuration = Configuration.builder().enhancerFactory(enhancerFactory)
                .registerWrapper(FinalClass.class, FinalClassWrapper.class)
                .build();
        ProxyFactory proxyFactory = ProxyFactory.getInstance(configuration);
        Final aFinal = new FinalClass();
        // when
        Final proxy = proxyFactory.create(aFinal);
        // then
        assertTrue(isCglibProxy(proxy));
        verify(enhancerFactory, atLeastOnce()).createEnhancer(aFinal.getClass());
        verify(enhancerFactory).createEnhancerWithWrapper(aFinal.getClass(), FinalClassWrapper.class);
        assertTrue(Wrapper.class.isAssignableFrom(proxy.getClass()));
    }

    public static class Simple {

    }

    public static interface Final {
    }

    public static final class FinalClass implements Final {

    }

    public static class FinalClassWrapper extends AbstractWrapper<Final> {

        @Override
        public Class<? extends Wrapper<Final>> getType() {
            return FinalClassWrapper.class;
        }
    }

}
