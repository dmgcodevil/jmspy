package com.github.dmgcodevil.jmspy;

import com.github.dmgcodevil.jmspy.proxy.Configuration;
import com.github.dmgcodevil.jmspy.proxy.ProxyFactory;
import com.github.dmgcodevil.jmspy.proxy.wrapper.Wrapper;
import org.testng.annotations.Test;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isCglibProxy;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by dmgcodevil on 12/29/2014.
 */
public class FinalClassWrapperTest {

    @Test
    public void testGetId() {

        Configuration conf = Configuration.builder()
                .registerWrapper(FinalClass.class, FinalClassWrapper.class) //register our wrapper
                .build();
        ProxyFactory proxyFactory = ProxyFactory.getInstance(conf);
        MethodInvocationRecorder invocationRecorder = new MethodInvocationRecorder(proxyFactory);
        FinalClass finalClass = new FinalClass();
        finalClass.setId("1");
        IFinalClass proxy = invocationRecorder.record(finalClass);
        assertTrue(isCglibProxy(proxy));
        assertEquals("wrapper: 1", proxy.getId());
    }

    public static interface IFinalClass {
        String getId();
    }

    public static final class FinalClass implements IFinalClass {

        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public class FinalClassWrapper implements IFinalClass, Wrapper<IFinalClass> {

        private IFinalClass target;

        public FinalClassWrapper() {
            // default constructor is required !
        }

        @Override
        public void setTarget(IFinalClass target) {
            this.target = target;
        }

        @Override
        public IFinalClass getTarget() {
            return target;
        }

        @Override
        public Class<? extends Wrapper<IFinalClass>> getType() {
            return FinalClassWrapper.class;
        }

        @Override
        public String getId() {
            return "wrapper: " + target.getId();
        }
    }

}


