package com.github.dmgcodevil.jmspy;

import com.github.dmgcodevil.jmspy.example.Account;
import com.github.dmgcodevil.jmspy.example.Contact;
import com.github.dmgcodevil.jmspy.example.User;
import com.github.dmgcodevil.jmspy.graph.Edge;
import com.github.dmgcodevil.jmspy.graph.Node;
import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isCglibProxy;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

/**
 * JMSpyTest unit test.
 *
 * @author dmgcodevil
 */
public class JMSpyTest {

    private MethodInvocationRecorder invocationRecorder = new MethodInvocationRecorder();
    private ExecutorService executor = Executors.newFixedThreadPool(100);

    @Test
    public void testUnmodifiableSet() throws ExecutionException, InterruptedException {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                User user = new User();
                Account account = new Account("test_acc");
                user.setAccounts(Sets.newHashSet(account));
                User proxy;
                try {
                    proxy = invocationRecorder.record(JMSpyTest.class.getMethod("testUnmodifiableSet"), user);
                } catch (NoSuchMethodException e) {
                    throw Throwables.propagate(e);
                }
                assertTrue(isCglibProxy(proxy));
                assertTrue(isCglibProxy(proxy.getAccounts()), "getAccounts() must return proxy");
                assertTrue(isCglibProxy(proxy.getAccounts().iterator()), "iterator must be proxy");
                assertTrue(isCglibProxy(proxy.getAccounts().iterator().next()), "each Account must be proxy");
                assertEquals(account.getName(), proxy.getAccounts().iterator().next().getName());

                for (Account acc : proxy.getAccounts()) {
                    assertTrue(isCglibProxy(acc));
                    System.out.println(acc.getName());
                }
            }
        }).get();

    }

    @Test
    public void testUnmodifiableList() throws ExecutionException, InterruptedException {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                User user = new User();
                Contact contact = new Contact("test_contact");
                user.setContacts(Lists.newArrayList(contact));
                User proxy;
                try {
                    proxy = invocationRecorder.record(JMSpyTest.class.getMethod("testUnmodifiableList"), user);
                } catch (NoSuchMethodException e) {
                    throw Throwables.propagate(e);
                }
                assertTrue(isCglibProxy(proxy));

                assertTrue(isCglibProxy(proxy.getAccounts()));
                assertTrue(isCglibProxy(proxy.getAccounts().iterator()));
                assertFalse(proxy.getAccounts().iterator().hasNext());

                assertTrue(isCglibProxy(proxy.getContacts()));
                assertTrue(isCglibProxy(proxy.getContacts().iterator()));
                assertTrue(isCglibProxy(proxy.getContacts().iterator().next()));

                assertEquals(contact.getName(), proxy.getContacts().iterator().next().getName());

                for (Contact con : proxy.getContacts()) {
                    assertTrue(isCglibProxy(con));
                    System.out.println(con.getName());
                }
            }
        }).get();
    }

    @AfterClass
    public void assertMethodInvocationRecorder() throws NoSuchMethodException {
        assertEquals(2, invocationRecorder.getInvocationRecords().size());
        InvocationRecord invocationRecord = getInvocationRecord(JMSpyTest.class.getMethod("testUnmodifiableSet"));
        assertNotNull(invocationRecord);
        Node rootNode = invocationRecord.getInvocationGraph().getRoot();
        assertEquals(5, rootNode.getOutgoingEdges().size());
        assertTrue(exists(rootNode, "getAccounts"));
        assertTrue(exists(rootNode, "getAccounts.iterator"));
        assertTrue(exists(rootNode, "getAccounts.iterator.next"));
        assertTrue(exists(rootNode, "getAccounts.iterator.next.getName"));

        invocationRecord = getInvocationRecord(JMSpyTest.class.getMethod("testUnmodifiableList"));
        assertNotNull(invocationRecord);
        Node rootNodeTestUnmodifiableList = invocationRecord.getInvocationGraph().getRoot();
        assertEquals(8, rootNodeTestUnmodifiableList.getOutgoingEdges().size());

        assertTrue(exists(rootNodeTestUnmodifiableList, "getAccounts"));
        assertTrue(exists(rootNodeTestUnmodifiableList, "getAccounts.iterator"));
        assertTrue(exists(rootNodeTestUnmodifiableList, "getAccounts.iterator.hasNext"));

        assertTrue(exists(rootNodeTestUnmodifiableList, "getContacts"));
        assertTrue(exists(rootNodeTestUnmodifiableList, "getContacts.iterator"));
        assertTrue(exists(rootNodeTestUnmodifiableList, "getContacts.iterator.next"));
        assertTrue(exists(rootNodeTestUnmodifiableList, "getContacts.iterator.next.getName"));

    }


    private boolean exists(Node root, String path) {
        List<String> methods = Lists.newArrayList(StringUtils.split(path, "."));
        return exists(root, methods.get(0), methods, 0);
    }

    private boolean exists(Node root, String method, List<String> methods, int level) {
        boolean exists = false;
        if (level <= methods.size() - 1) {
            for (Edge edge : root.getOutgoingEdges()) {
                if (edge.getMethod().getName().equals(method)) {
                    if (CollectionUtils.isNotEmpty(edge.getTo().getOutgoingEdges())) {
                        int nextLevel = level + 1;
                        if (nextLevel != methods.size() && exists(edge.getTo(),
                                methods.get(nextLevel), methods, nextLevel)) {
                            exists = true;
                            break;
                        }
                    } else {
                        exists = true;
                    }

                }
            }
        }
        return exists;
    }

    // todo improve this method and add to MethodInvocationRecorder class
    public InvocationRecord getInvocationRecord(final Method method) {
        return Iterables.tryFind(invocationRecorder.getInvocationRecords(), new Predicate<InvocationRecord>() {
            @Override
            public boolean apply(InvocationRecord input) {
                return input.getInvocationContext().getRoot() != null &&
                        input.getInvocationContext().getRoot().getName().equals(method.getName());
            }
        }).orNull();
    }

}
