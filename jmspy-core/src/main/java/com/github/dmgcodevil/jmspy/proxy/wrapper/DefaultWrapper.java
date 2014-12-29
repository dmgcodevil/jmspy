package com.github.dmgcodevil.jmspy.proxy.wrapper;

/**
 * Default wrapper implementation.
 *
 * @author dmgcodevil
 */
public class DefaultWrapper extends AbstractWrapper<Object> {

    @Override
    public Class<? extends Wrapper<Object>> getType() {
        return DefaultWrapper.class;
    }
}
