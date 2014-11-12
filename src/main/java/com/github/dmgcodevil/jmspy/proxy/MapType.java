package com.github.dmgcodevil.jmspy.proxy;

/**
 * Class description.
 *
 * @author Raman_Pliashkou
 */
public class MapType {

    private Class<?> keyType;
    private Class<?> valueType;

    public static final MapType EMPTY = builder().build();

    public MapType(Class<?> keyType, Class<?> valueType) {
        this.keyType = keyType;
        this.valueType = valueType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static MapType empty() {
        return EMPTY;
    }

    public Class<?> getKeyType() {
        return keyType;
    }

    public String getKeyTypeName() {
        return keyType != null ? keyType.getCanonicalName() : "";
    }


    public Class<?> getValueType() {
        return valueType;
    }

    public String getValueTypeName() {
        return valueType != null ? valueType.getCanonicalName() : "";
    }

    public boolean isEmpty() {
        return this == EMPTY || (keyType == null && valueType == null);
    }

    public static class Builder {
        private Class<?> keyType;
        private Class<?> valueType;

        public Builder keyType(Class<?> keyType) {
            this.keyType = keyType;
            return this;
        }

        public Builder valueType(Class<?> valueType) {
            this.valueType = valueType;
            return this;
        }

        MapType build() {
            return new MapType(keyType, valueType);
        }
    }

}
