package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.functional.Consumer;
import com.github.dmgcodevil.jmspy.proxy.wrapper.Wrapper;
import com.google.common.base.Verify;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.forEach;

/**
 * Configuration for {@link ProxyFactory}.
 *
 * @author dmgcodevil
 */
public final class Configuration {

    private Map<Class<?>, Class<? extends Wrapper>> wrappers = new HashMap<>();
    private Set<Class<?>> ignoreTypes = Sets.newHashSet();
    private Set<String> ignorePackages = Sets.newHashSet();
    private EnhancerFactory enhancerFactory;

    private Configuration(Builder builder) {
        wrappers = ImmutableMap.copyOf(builder.wrappers);
        ignoreTypes = ImmutableSet.copyOf(builder.ignoreTypes);
        ignorePackages = ImmutableSet.copyOf(builder.ignorePackages);
        enhancerFactory = builder.enhancerFactory;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Map<Class<?>, Class<? extends Wrapper>> getWrappers() {
        return wrappers;
    }

    public Set<Class<?>> getIgnoreTypes() {
        return ignoreTypes;
    }

    public Set<String> getIgnorePackages() {
        return ignorePackages;
    }

    public EnhancerFactory getEnhancerFactory() {
        return enhancerFactory;
    }

    public static class Builder {
        private Map<Class<?>, Class<? extends Wrapper>> wrappers = new HashMap<>();
        private Set<Class<?>> ignoreTypes = Sets.newHashSet();
        private Set<String> ignorePackages = Sets.newHashSet();
        private EnhancerFactory enhancerFactory = new EnhancerFactory();

        public Builder() {
            ignoreType(java.math.BigDecimal.class);
        }

        public Builder enhancerFactory(EnhancerFactory enhancerFactory) {
            this.enhancerFactory = enhancerFactory;
            return this;
        }

        /**
         * Register a wrapper for the given type.
         *
         * @param type    the type for that the given wrapper must be registered
         * @param wrapper the wrapper class
         * @return this instance
         */
        public Builder registerWrapper(Class<?> type, Class<? extends Wrapper> wrapper) {
            Verify.verifyNotNull(type, "type cannot be null");
            Verify.verifyNotNull(wrapper, "wrapper cannot be null");
            wrappers.put(type, wrapper);
            return this;
        }

        /**
         * Register a batch of wrappers for the given types.
         * Wrappers are useful if {@link ProxyFactory} fails to create proxy for some reasons:
         * final class, class loaded using bootstrap class loader and etc.
         *
         * @param wrappers the wrappers
         * @return this instance
         */
        public Builder wrappers(Map<Class<?>, Class<? extends Wrapper>> wrappers) {
            forEach(wrappers, new Consumer<Map.Entry<Class<?>, Class<? extends Wrapper>>>() {
                @Override
                public void consume(Map.Entry<Class<?>, Class<? extends Wrapper>> input) {
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
