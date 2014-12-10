package com.github.dmgcodevil.jmspy.proxy.callback;

import net.sf.cglib.proxy.FixedValue;

/**
 * Helper callback is used to intercept synthetic method to get proxy identifier.
 *
 * @author dmgcodevil
 */
public class ProxyIdentifierCallback implements FixedValue {

    public static int INDEX = 0;

    public static final String GET_PROXY_IDENTIFIER = "get$Proxy$Identifier";

    private final String id;

    public ProxyIdentifierCallback(String id) {
        this.id = id;
    }

    @Override
    public Object loadObject() throws Exception {
        return id;
    }
}