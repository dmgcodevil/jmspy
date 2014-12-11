jmspy
=====

##Introduction

Jmspy is a java library that allows recording of java methods invocations, saving data into a file called snapshot and analyzing it using jmspy viewer. The library uses CGLib to create proxies and graph data structure representing method invocations. It supports any number of nested call structures, e.g.:

``` object.getCollection().iterator().next().getProperty() ```

Jmspy is aware of the most used collection types like list, set and map, including unmodifiable collections from jdk and empty collections.

##Jmspy-core

The components which form the basic API are: 

*MethodInvocationRecorder* - the main class which the user interacts with.
*ProxyFactory* - a factory to create proxies for objects
*ContextExplorer* - an interface that’s used to provide additional information about invocation context.

Here are some more details on them.

###ProxyFactory
This factory allows creating proxies for client objects. There is an ability to configure the factory before using it, which can be useful if you deal with complex objects and you need some workarounds to create proxies. ProxyFactory is a singleton, i.e. you can initialize it only once using ProxyFactory#getInstance(Configuration config) and pass an instance of Configuration. 
Example:
```
Configuration.Builder builder = Configuration.builder()
                .ignoreType(DataLoader.class) // objects with type DataLoader for which no proxy should be created
                .ignoreType(java.util.logging.Logger.class) // ignore objects with type DataLoader
                .ignorePackage("com.mongodb");  // ignore objects with types exist in specified package
ProxyFactory proxyFactory = ProxyFactory.getInstance(builder.build());
```

###ContextExplorer
This is an interface which provides more information on the invocation context. Jmspy has some built-in implementations, for instance FreemarkerContextExplorer from jmspy-ext-freemarker. This implementation allows retrieving information on request url and FTL page. You can register only one ContextExplorer per MethodInvocationRecorder. The ContextExplorer  interface has two methods:
- *getRootContextInfo* - gets info about the root invocation context, such as application name, request url and etc. This method is invoked as soon as an invocation record is created via the MethodInvocationRecorder#record(java.lang.reflect.Method, Object)} method.
- *getCurrentContextInfo* - gets info about current invocation context such as page name and etc. This method is invoked as soon as a method of proxy object is intercepted.

###MethodInvocationRecorder
This is the main class with which the user must interact. This class has several constructors with an ability to pass the ProxyFactory and the ContextExplorer. It also has a default constructor, which yields a default configuration of ProxyFactory, but no default implementation for ContextExplorer is used. Thus, all three variants described below are correct:

1. ```MethodInvocationRecorder methodInvocationRecorder = new MethodInvocationRecorder();```
2. ```MethodInvocationRecorder methodInvocationRecorder = new MethodInvocationRecorder(ProxyFactory.getInstance(Configuration.builder().build()));```
3. ```MethodInvocationRecorder methodInvocationRecorder = new MethodInvocationRecorder(new ContextExplorer() {
            @Override
            public InvocationContextInfo getRootContextInfo() {
                return null;
            }

            @Override
            public InvocationContextInfo getCurrentContextInfo() {
                return null;
            }
        });
```
4. 2+3

##Restrictions
Jmspy uses CGLIB lib to create proxies and there are several restrictions that come from CGLIB nature.
CGLIB uses inheritance when it creates dynamic proxy. Thus, each proxy belongs to a generated instrumented class that extends original type by including interfaces. Java has several restrictions with class inheritance, namely:
- Final class cannot be extended
- Final methods cannot be overridden
- It isn’t possible to create instances of classes without default constructor.

All this issues are partially resolved in jmspy-core, for example the restriction with  final classes can be resolved using feature called Wrapper. Wrapper is an interface that provides several methods to create, set and get  target instance. It’s easy to use, lets consider simple example.
Suppose you have a final class on whom you want to record invocations

*Interface*
```
public interface IFinalClass {
    String getId();
}

```
*Class*
```
public final class FinalClass implements IFinalClass {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
```
App class:

```
public static void main(String[] args) {
        MethodInvocationRecorder invocationRecorder = new MethodInvocationRecorder();
        FinalClass finalClass = new FinalClass();
        FinalClass proxy = invocationRecorder.record(finalClass);
        System.out.println(isCglibProxy(proxy));
    }
```

Execution result is *false*

As you can seejmspy has failed to create a proxy for ‘finalClass’ variable.
To solve this issue you need to create a wrapper for the FinalClass. 
Suppose you are lucky and have an interface for FinalClass class, it's ```IFinalClass```

Then the wrapper will look like that:
```
public class FinalClassWrapper implements IFinalClass, Wrapper<IFinalClass> {

    private IFinalClass target;

    public FinalClassWrapper() {
    }

    public FinalClassWrapper(IFinalClass target) {
        this.target = target;
    }

    @Override
    public Wrapper create(IFinalClass target) {
        return new FinalClassWrapper(target);
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
        return target.getId();
    }
}
```
Now you need to register this wrapper:
```
    public static void main(String[] args) {
        Configuration conf = Configuration.builder()
                .registerWrapper(FinalClass.class, new FinalClassWrapper()) //register our wrapper
                .build();
        ProxyFactory proxyFactory = ProxyFactory.getInstance(conf);
        MethodInvocationRecorder invocationRecorder = new MethodInvocationRecorder(proxyFactory);
        IFinalClass finalClass = new FinalClass();  // change class FinalClass to interface IFinalClass 
        IFinalClass proxy = invocationRecorder.record(finalClass); // change class FinalClass to interface IFinalClass
        System.out.println(isCglibProxy(proxy));
    }
```
Now execution result is *true*. As you can see, jmspy managed to create the proxy.
Issues with final methods and absence of default constructors also can be solved using the Wrapper approach, but what if you don’t have an interface for the FinalClass.class? 
You can’t extend it hence the decorator pattern wouldn’t work here. In this case you need to use *Jmspy-agent*

##Jmspy-agent

It’s a java agent that uses asm library to transform classes, which solves the issues with final classes, methods and absent constructors. You just need to specify the jmspy-agent at your application startup via the appropriate command line parameter for JVM.
You can pass an argument to the agent to indicate which classes or packages should be instrumented. It considers whole string after '=' as a single parameter. Basically, you have two variants to pass the parameter:

1. using javaagent parameter
example:                     
``` -javaagent:{path_to_jar}/jmspy-agent-x.y.z.jar=com.github.dmgcodevil.jmspy.example.Candidate.class,com.github.dmgcodevil.jmspy.test.data ```

2. usning property file: jmspy_agent.properties
The property file should be placed at the top level of your project, for example:
 'src/main/resources/jmspy_agent.properties' or ‘src/test/resources/jmspy_agent.properties'
The exact property name is "instrumentedResources", for instance: instrumentedResources=com.github.dmgcodevil.jmspy.example.Candidate.class
Arguments allow specifying concrete classes that should be instrumented or whole packages.
The agent expects the argument as a string of the following format:
``` [ {canonicalClassName}.class, {package}, {canonicalClassName}.class, ... ] ```
Thus, it's possible to specify classes or packages as required.

