package com.github.dmgcodevil.jmspy.reflection;

import com.github.dmgcodevil.jmspy.exception.ObjectInstantiationException;
import com.google.common.collect.ImmutableMap;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isPrimitiveOrWrapper;
import static com.github.dmgcodevil.jmspy.proxy.ReflectionUtils.hasDefaultConstructor;

/**
 * Provides convenient methods to create new instances using different strategies, see {@link InstantiationStrategy}.
 *
 * @author dmgcodevil
 */
public final class Instantiator {

    private static final Instantiator INSTANCE = new Instantiator();

    private static final Map<Class<?>, Object> DEFAULT_PRIMITIVE_VALUES = new ImmutableMap.Builder<Class<?>, Object>()
            .put(Boolean.class, Boolean.FALSE)
            .put(Boolean.TYPE, Boolean.FALSE)
            .put(String.class, "")
            .build();

    private static final Map<InstantiationStrategy, Strategy> INSTANTIATION_STRATEGIES =
            new ImmutableMap.Builder<InstantiationStrategy, Strategy>()
                    .put(InstantiationStrategy.CREATE_DEFAULT, new CreateDefaultConstructorStrategy())
                    .put(InstantiationStrategy.USE_EXISTING, new UseExistingConstructorStrategy())
                    .build();


    private Instantiator() {
        // empty constructor
    }

    public static Instantiator getInstance() {
        return INSTANCE;
    }

    /**
     * Creates new instance.
     * {@link InstantiationStrategy#CREATE_DEFAULT} is default strategy.
     *
     * @param type the type is used to create new instance
     * @param <T>  generic type of the given class
     * @return new instance
     * @throws ObjectInstantiationException in a case of any errors
     */
    public <T> T newInstance(final Class<T> type) throws ObjectInstantiationException {
        return newInstance(type, InstantiationStrategy.CREATE_DEFAULT);
    }

    /**
     * @param type                  the type is used to create new instance
     * @param instantiationStrategy the instantiation strategy
     * @param <T>                   generic type of the given class
     * @return new instance
     * @throws ObjectInstantiationException
     */
    public <T> T newInstance(final Class<T> type, InstantiationStrategy instantiationStrategy)
            throws ObjectInstantiationException {
        try {
            if (hasDefaultConstructor(type)) {
                return type.newInstance();
            }
            if (!INSTANTIATION_STRATEGIES.containsKey(instantiationStrategy)) {
                throw new ObjectInstantiationException("Unknown or unsupported instantiation strategy");
            }
            return INSTANTIATION_STRATEGIES.get(instantiationStrategy).newInstance(type);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ObjectInstantiationException(e);
        }
    }

    /**
     * This strategy declares one method is used to implement different
     * behavior in accordance to selected {@link InstantiationStrategy}.
     */
    private static interface Strategy {
        <T> T newInstance(final Class<T> type) throws ObjectInstantiationException;
    }

    /**
     * Creates default constructor.
     */
    // todo implement it using 'objenesis' library
    private static class CreateDefaultConstructorStrategy implements Strategy {
        @SuppressWarnings("restriction")
        @Override
        public <T> T newInstance(Class<T> type) throws ObjectInstantiationException {
            try {
                final Constructor<?> constructor = ReflectionFactory.getReflectionFactory()
                        .newConstructorForSerialization(type, Object.class.getDeclaredConstructor());
                return type.cast(createNewInstance(constructor));
            } catch (NoSuchMethodException e) {
                throw new ObjectInstantiationException(e);
            }
        }
    }

    /**
     * Calls any constructor. Simply pass null values or 0 values for primitives.
     */
    private static class UseExistingConstructorStrategy implements Strategy {
        @SuppressWarnings("unchecked")
        @Override
        public <T> T newInstance(Class<T> type) throws ObjectInstantiationException {
            Constructor<?> constructor = type.getDeclaredConstructors()[0];
            Object[] initArgs = new Object[constructor.getParameterTypes().length];
            int index = 0;
            for (Class<?> pType : constructor.getParameterTypes()) {
                if (isPrimitiveOrWrapper(pType)) {
                    initArgs[index] = DEFAULT_PRIMITIVE_VALUES.containsKey(pType) ?
                            DEFAULT_PRIMITIVE_VALUES.get(pType) : 0;
                }
                initArgs[index] = null;
                index++;
            }
            return (T) createNewInstance(constructor, initArgs);
        }
    }

    /**
     * Convenient method to create new instance using the given constructor.
     *
     * @param constructor the constructor is used to create new instance
     * @param initargs    the argument that will be passed to constructor
     * @return new instance
     * @throws ObjectInstantiationException in a case of any errors
     */
    private static Object createNewInstance(Constructor<?> constructor, Object... initargs)
            throws ObjectInstantiationException {
        try {
            constructor.setAccessible(true);
            return constructor.newInstance(initargs);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ObjectInstantiationException(e);
        }
    }

    /**
     * This enums defines possible strategies to resolve the issue with absent default constructor.
     */
    public static enum InstantiationStrategy {

        /**
         * Creates default constructor.
         */
        CREATE_DEFAULT,

        /**
         * Calls any constructor. Simply pass null values or 0 values for primitives.
         */
        USE_EXISTING
    }

}
