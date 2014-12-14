jmspy
=====
<p align="center">
  <img src="https://github.com/dmgcodevil/jmspy/blob/master/resources/logo.png"/>
</p>
##Introduction

**Jmspy** is a java library that allows recording of java methods invocations, saving data into a file called snapshot and analyzing it using jmspy viewer. The library uses **CGLib** to create proxies and graph data structure representing method invocations. It supports any number of nested call structures, e.g.:

``` object.getCollection().iterator().next().getProperty() ```

Jmspy is aware of the most used collection types like list, set and map, including unmodifiable collections from jdk and empty collections.

##Jmspy-core

The components which form the basic API are: 

**MethodInvocationRecorder** - the main class which the user interacts with.
**ProxyFactory** - a factory to create proxies for objects
**ContextExplorer** - an interface that’s used to provide additional information about invocation context.

Here are some more details on them.

###ProxyFactory
This factory allows creating proxies for client objects. There is an ability to configure the factory before using it, which can be useful if you deal with complex objects and you need some workarounds to create proxies. ProxyFactory is a singleton, i.e. you can initialize it only once using ProxyFactory#getInstance(Configuration config) and pass an instance of Configuration. 
Example:
```java
Configuration.Builder builder = Configuration.builder()
                .ignoreType(DataLoader.class) // objects with type DataLoader for which no proxy should be created
                .ignoreType(java.util.logging.Logger.class) // ignore objects with type DataLoader
                .ignorePackage("com.mongodb");  // ignore objects with types exist in specified package
ProxyFactory proxyFactory = ProxyFactory.getInstance(builder.build());
```

###ContextExplorer
This is an interface which provides more information on the invocation context. Jmspy has some built-in implementations, for instance FreemarkerContextExplorer from jmspy-ext-freemarker. This implementation allows retrieving information on request url and FTL page. You can register only one ContextExplorer per MethodInvocationRecorder. The ContextExplorer  interface has two methods:
- **getRootContextInfo** - gets info about the root invocation context, such as application name, request url and etc. This method is invoked as soon as an invocation record is created via the MethodInvocationRecorder#record(java.lang.reflect.Method, Object)} method.
- **getCurrentContextInfo** - gets info about current invocation context such as page name and etc. This method is invoked as soon as a method of proxy object is intercepted.

###MethodInvocationRecorder
This is the main class with which the user must interact. This class has several constructors with an ability to pass the ProxyFactory and the ContextExplorer. It also has a default constructor, which yields a default configuration of ProxyFactory, but no default implementation for ContextExplorer is used. Thus, all three variants described below are correct:

- (1)  ```java new MethodInvocationRecorder();```
- (2) ```java new MethodInvocationRecorder(ProxyFactory.getInstance(Configuration.builder().build())); ```
- (3) see the code below
```java
MethodInvocationRecorder methodInvocationRecorder = new MethodInvocationRecorder(new ContextExplorer() {
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
- (4) = 2+3

##Restrictions
Jmspy uses CGLIB lib to create proxies and there are several restrictions that come from CGLIB nature.
CGLIB uses inheritance when it creates dynamic proxy. Thus, each proxy belongs to a generated instrumented class that extends original type by including interfaces. Java has several restrictions with class inheritance, namely:
- Final class cannot be extended
- Final methods cannot be overridden
- It isn’t possible to create instances of classes without default constructor.

All this issues are partially resolved in jmspy-core, for example the restriction with  final classes can be resolved using feature called Wrapper. Wrapper is an interface that provides several methods to create, set and get  target instance. It’s easy to use, lets consider simple example.
Suppose you have a final class on whom you want to record invocations

**Interface**
```java
public interface IFinalClass {
    String getId();
}

```
**Class**
```java
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

```java
public static void main(String[] args) {
        MethodInvocationRecorder invocationRecorder = new MethodInvocationRecorder();
        FinalClass finalClass = new FinalClass();
        FinalClass proxy = invocationRecorder.record(finalClass);
        System.out.println(isCglibProxy(proxy));
    }
```

Execution result is **false**

As you can seejmspy has failed to create a proxy for ‘finalClass’ variable.
To solve this issue you need to create a wrapper for the FinalClass. 
Suppose you are lucky and have an interface for FinalClass class, it's ```IFinalClass```

Then the wrapper will look like that:
```java
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
```java
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
Now execution result is **true**. As you can see, jmspy managed to create the proxy.
Issues with final methods and absence of default constructors also can be solved using the Wrapper approach, but what if you don’t have an interface for the FinalClass.class? 
You can’t extend it hence the decorator pattern wouldn’t work here. In this case you need to use **Jmspy-agent**

##Jmspy-agent

It’s a java agent that uses **asm** library to transform classes, which solves the issues with final classes, methods and absent constructors. You just need to specify the jmspy-agent at your application startup via the appropriate command line parameter for JVM.
You can pass an argument to the agent to indicate which classes or packages should be instrumented. It considers whole string after '=' as a single parameter. Basically, you have two variants to pass the parameter:

1. using javaagent parameter
example:                     
``` 
-javaagent:{path_to_jar}/jmspy-agent-x.y.z.jar=com.github.dmgcodevil.jmspy.example.Candidate.class,com.github.dmgcodevil.jmspy.test.data
```

2. usning property file: jmspy_agent.properties
The property file should be placed at the top level of your project, for example:
``` 'src/main/resources/jmspy_agent.properties' or ‘src/test/resources/jmspy_agent.properties' ```
The exact property name is "instrumentedResources", for instance: instrumentedResources=com.github.dmgcodevil.jmspy.example.Candidate.class
Arguments allow specifying concrete classes that should be instrumented or whole packages.
The agent expects the argument as a string of the following format:
``` [ {canonicalClassName}.class, {package}, {canonicalClassName}.class, ... ] ```
Thus, it's possible to specify classes or packages as required.

There are several significant points to mention:
1. If agentArgs/property file weren't specified (nonexistent property file or empty string),  then **all classes will be transformed**.
2. Classes and packages must be separated by the *','* symbol.
3. A class name must end with *'.class'* suffix, otherwise it will be considered as a package name.

In some cases the preferable option is to use package names in the agentArgs instead of specifying concrete class names, unless  you know the exact class name at runtime. This is because of the possibility that nested or anonymous classes with names like ``` com/site/project/URLClassPath$FileLoader$1 ```
will not be transformed, if you specify an incorrect name. In this case it’s better to specify a package name like ```com.site.project ```

##How to integrate JMSpy with an application

It’s pretty easy to integrate jmspy in your project. One of the possible approaches is described below, but you can experiment with it in order to find the best approach for your case.
Suppose you have a plain web application with the traditional three-tier architecture and you use spring as an ioc container. You want to monitor what is happening with your domains after a repository retrieves it from a storage. Below described the main steps for implementing it.

1. **Create necessary spring beans**

You need to create a **methodInvocationRecorder** bean, (the preferable scope is singleton). Also, if you need to configure a ProxyFactory for it, then you need to create a class that will be responsible for configuring and initializing the ProxyFactory:
```java
public class JmspyProxyFactoryConstructor {
    public ProxyFactory construct() {
        Configuration.Builder builder = Configuration.builder()
            // … configuration code
        return ProxyFactory.getInstance(builder.build());
    }
}
```

Spring xml context:

```xml
<bean id="methodInvocationRecorder" class="com.github.dmgcodevil.jmspy.MethodInvocationRecorder">
        <constructor-arg>
            <bean class="com.github.dmgcodevil.jmspy.proxy.ProxyFactory" factory-bean="jmspyProxyFactoryConstructor" factory-method="construct" />
        </constructor-arg>
    </bean>

<bean id="jmspyProxyFactoryConstructor" class="com.edmunds.vehiclelanding.jmspy.JmspyProxyFactoryConstructor" />
```

2. **Create AOP aspect to intercept repository methods**
```java
@Around("repoMethodPointcut()) // your pointcut
    public Object intercept (final ProceedingJoinPoint pjp, Spyable spyable) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        return methodInvocationRecorder.record(signature.getMethod(), pjp.proceed()); // start recording
    }
```
Note that you need to return the result of the MethodInvocationRecorder#record method invocation from ```intercept``` method, not the result of ```pjp.proceed()```. The result of ```pjp.proceed()``` must be passed to the record method.

3. **Add jmx bean to save invocation snapshot**.

JMSpy creates snapshots on demand which means that you need to manually invoke  the ```MethodInvocationRecorder#makeSnapshot()``` method when you are sure that you have collected all necessary data.
```java
@ManagedResource(objectName = "Edmunds:type=JMSpy,concern=Concern,name=JmspyJmxOperations")
public class JmspyJmxOperations {

    @Autowired
    private MethodInvocationRecorder methodInvocationRecorder;

    @ManagedOperation
    public void takeSnapshot() {
        Snapshot.save(methodInvocationRecorder.makeSnapshot());
    }
}
```
Also you can add RESTful service to invoke invoke the MethodInvocationRecorder#makeSnapshot().


##Dependencies

All artifacts uploaded to maven central

```xml
<dependency>
    <groupId>com.github.dmgcodevil</groupId>
    <artifactId>jmspy-core</artifactId>
    <version>1.1.1</version>
</dependency>
```

**Released modules**:
- jmspy-core
- jmspy-agent
- jmspy-ext-freemarker
