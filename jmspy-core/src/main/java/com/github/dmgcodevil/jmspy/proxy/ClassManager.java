package com.github.dmgcodevil.jmspy.proxy;

/**
 * Class description.
 *
 * @author Raman_Pliashkou
 */
public class ClassManager {

    /**
     * Loads class from array of bytes.
     *
     * @param name  full class class name
     * @param bytes
     * @return
     * @throws ClassNotFoundException
     */
    public Class<?> loadClass(String name, byte[] bytes) throws ClassNotFoundException {
        ClassLoader parentClassLoader = ByteArrayClassLoader.class.getClassLoader();
        return new ByteArrayClassLoader(parentClassLoader, bytes, name).loadClass(name);
    }

    public Class<?> loadClass(String name, ClassLoader cl, byte[] bytes) throws ClassNotFoundException {
        return new ByteArrayClassLoader(cl, bytes, name).loadClass(name);
    }

    public class ByteArrayClassLoader extends ClassLoader {
        private byte[] ba;
        private String className;

        public ByteArrayClassLoader(ClassLoader parent, byte[] ba, String className) {
            super(parent);
            this.ba = ba;
            this.className = className;
        }

        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            if (!className.equals(name)) {
                return super.loadClass(name);
            }
            return defineClass(name, ba, 0, ba.length);
        }
    }
}