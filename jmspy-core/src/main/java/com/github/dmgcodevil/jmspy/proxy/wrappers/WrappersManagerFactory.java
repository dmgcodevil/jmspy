package com.github.dmgcodevil.jmspy.proxy.wrappers;

/**
 * Created by dmgcodevil on 12/24/2014.
 */
public class WrappersManagerFactory {

    public static double JAVA_VERSION = getVersion();

    static double getVersion() {
        String version = System.getProperty("java.version");
        int pos = version.indexOf('.');
        pos = version.indexOf('.', pos + 1);
        return Double.parseDouble(version.substring(0, pos));
    }

    public static WrappersManager createWrappersManager() throws ClassNotFoundException {
        WrappersManager wrappersManager;
        if (JAVA_VERSION > 1.7) {
            wrappersManager = new Jdk8WrappersManager();
        } else {
            wrappersManager = new Jdk7WrappersManager();
        }
        wrappersManager.initWrappers();
        return wrappersManager;
    }
}
