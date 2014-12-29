package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.proxy.wrapper.AbstractWrapper;
import com.github.dmgcodevil.jmspy.proxy.wrapper.Wrapper;

/**
 * Default wrapper implementation.
 *
 * @author dmgcodevil
 */
class DefaultWrapper extends AbstractWrapper<Object> {

    @Override
    public Class<? extends Wrapper<Object>> getType() {
        return DefaultWrapper.class;
    }
}
