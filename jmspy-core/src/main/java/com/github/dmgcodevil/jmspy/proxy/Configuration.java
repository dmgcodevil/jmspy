package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.exception.ConfigurationException;
import com.github.dmgcodevil.jmspy.functional.Consumer;
import com.github.dmgcodevil.jmspy.proxy.wrappers.CollectionWrapper;
import com.github.dmgcodevil.jmspy.proxy.wrappers.EntrySetWrapper;
import com.github.dmgcodevil.jmspy.proxy.wrappers.EntryWrapper;
import com.github.dmgcodevil.jmspy.proxy.wrappers.IteratorWrapper;
import com.github.dmgcodevil.jmspy.proxy.wrappers.ListIteratorWrapper;
import com.github.dmgcodevil.jmspy.proxy.wrappers.ListWrapper;
import com.github.dmgcodevil.jmspy.proxy.wrappers.MapKeySetWrapper;
import com.github.dmgcodevil.jmspy.proxy.wrappers.MapValuesWrapper;
import com.github.dmgcodevil.jmspy.proxy.wrappers.MapWrapper;
import com.github.dmgcodevil.jmspy.proxy.wrappers.SetWrapper;
import com.github.dmgcodevil.jmspy.proxy.wrappers.Wrapper;
import com.github.dmgcodevil.jmspy.proxy.wrappers.WrappersManagerFactory;
import com.google.common.base.Verify;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.forEach;

/**
 * Configuration for {@link ProxyFactory}.
 *
 * @author dmgcodevil
 */
public final class Configuration {

    private Map<Class<?>, Wrapper> wrappers = new HashMap<>();
    private Set<Class<?>> ignoreTypes = Sets.newHashSet();
    private Set<String> ignorePackages = Sets.newHashSet();

    private Configuration(Builder builder) {
        wrappers = ImmutableMap.copyOf(builder.wrappers);
        ignoreTypes = ImmutableSet.copyOf(builder.ignoreTypes);
        ignorePackages = ImmutableSet.copyOf(builder.ignorePackages);
    }

    public static Builder builder() {
        return new Builder();
    }

    public Map<Class<?>, Wrapper> getWrappers() {
        return wrappers;
    }

    public Set<Class<?>> getIgnoreTypes() {
        return ignoreTypes;
    }

    public Set<String> getIgnorePackages() {
        return ignorePackages;
    }

    public static class Builder {
        private Map<Class<?>, Wrapper> wrappers;
        private Set<Class<?>> ignoreTypes = Sets.newHashSet();
        private Set<String> ignorePackages = Sets.newHashSet();

        public Builder() {
            initDefaultWrappers();
            ignoreType(java.math.BigDecimal.class);
        }

        private void initDefaultWrappers() {
            try {
                wrappers = WrappersManagerFactory.createWrappersManager().getWrappers();
            } catch (ClassNotFoundException e) {
                throw new ConfigurationException(e);
            }
        }

        /**
         * Register an wrapper for the given type.
         *
         * @param type    the type for that the given wrapper must be registered
         * @param wrapper the wrapper
         * @return this instance
         */
        public Builder registerWrapper(Class<?> type, Wrapper wrapper) {
            Verify.verifyNotNull(type, "type cannot be null");
            Verify.verifyNotNull(wrapper, "wrapper cannot be null");
            wrappers.put(type, wrapper);
            return this;
        }

        /**
         * Register an batch of wrappers for the given types types.
         * Wrappers make possible to create proxies for classes that don't have default constructor.
         * Basically default constructor can be created by changing byte code of a class but for the initial version
         * this problem was solved by adding the Wrappers mechanism.
         *
         * @param wrappers the wrappers
         * @return this instance
         */
        public Builder wrappers(Map<Class<?>, Wrapper> wrappers) {
            forEach(wrappers, new Consumer<Map.Entry<Class<?>, Wrapper>>() {
                @Override
                public void consume(Map.Entry<Class<?>, Wrapper> input) {
                    registerWrapper(input.getKey(), input.getValue());
                }
            });
            return this;
        }

        public Builder ignoreTypes(Set<Class<?>> ignoreTypes) {
            forEach(ignoreTypes, new Consumer<Class<?>>() {
                @Override
                public void consume(Class<?> input) {
                    ignoreType(input);
                }
            });
            return this;
        }

        public Builder ignoreType(Class<?> type) {
            Verify.verifyNotNull(type, "type cannot be null");
            this.ignoreTypes.add(type);
            return this;
        }

        public Builder ignorePackages(Set<Class<?>> ignorePackages) {
            forEach(ignorePackages, new Consumer<Class<?>>() {
                @Override
                public void consume(Class<?> input) {
                    ignoreType(input);
                }
            });
            return this;
        }

        public Builder ignorePackage(String ipackage) {
            Verify.verifyNotNull(ipackage, "ignore package cannot be null");
            this.ignorePackages.add(ipackage);
            return this;
        }

        public Configuration build() {
            return new Configuration(this);
        }
    }

}
