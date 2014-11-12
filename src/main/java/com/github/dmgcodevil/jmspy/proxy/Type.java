package com.github.dmgcodevil.jmspy.proxy;

import com.google.common.base.Optional;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public
final class Type {

    private static final int KEY_TYPE = 0;
    private static final int VALUE_TYPE = 1;
    private static final int COMPONENT_TYPE = 0;

    private final Class<?> target;
    private final java.lang.reflect.Type[] parameterizedTypes;

    public static final Type EMPTY = builder().build();

    /**
     * Creates type with the given target type. see also {@link com.github.dmgcodevil.jmspy.proxy.Type.Builder}.
     *
     * @param target target type
     */
    public Type(Class<?> target) {
        this.target = target;
        this.parameterizedTypes = null;
    }

    public Type(Builder builder) {
        target = builder.target;
        parameterizedTypes = builder.parameterizedTypes;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Class<?> getTarget() {
        return target;
    }

    public java.lang.reflect.Type[] getParameterizedTypes() {
        return parameterizedTypes;
    }

    /**
     * Gets types of a map components (key, value). This method can be applied for Map.
     *
     * @return the types of a components (key, value)
     */
    public MapType getMapComponentsTypes() {
        if (parameterizedTypes != null &&
                parameterizedTypes.length == 2) // this check is required because it's possible that target type is
        // subtype of Map and has more than two parametrized types or property defined without generic types, in this case we cannot be sure that
        // first and second elements from array is correct types of Map key-value pair thus we need to get components types from actual map entries.
        {
            Class<?> keyType = (Class<?>) getParameterizedTypes()[KEY_TYPE];
            Class<?> valueType = (Class<?>) getParameterizedTypes()[VALUE_TYPE];
            return MapType.builder().keyType(keyType).valueType(valueType).build();
        }
        return MapType.empty();
    }

    /**
     * Gets type of a component. This method can be applied for Collections and Arrays.
     *
     * @return the component type
     */
    public Optional<Class<?>> getComponentType() {
        if (parameterizedTypes != null &&
                parameterizedTypes.length == 1) // reason of this check is same to  getMapComponentsTypes() method
        {
            return Optional.<Class<?>>of((Class<?>) parameterizedTypes[COMPONENT_TYPE]);
        } else {
            return Optional.absent();
        }
    }

    public static class Builder {
        private Class<?> target;
        private java.lang.reflect.Type[] parameterizedTypes;

        public Builder target(Class<?> target) {
            this.target = target;
            return this;
        }

        public Builder parameterizedTypes(java.lang.reflect.Type[] parameterizedTypes) {
            this.parameterizedTypes = parameterizedTypes;
            return this;
        }

        public Type build() {
            return new Type(this);
        }
    }

}
