package com.github.dmgcodevil.jmspy.agent;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Jmspy Agent transforms classes to make it possible to create CGLIB proxies
 * for final classes, classes without default constructor and etc.
 *
 * @author dmgcodevil
 */
public class Agent {

    /**
     * Premain method is required to apply agent.
     *
     * @param agentArgs the arguments for the agent. Whole string follows after '=' is considered as single parameter.
     *                  <p/>
     *                  Example:
     *                  -javaagent:jmspy-core/lib/jmspy-agent.jar=com.github.dmgcodevil.jmspy.example.Candidate.class,com.github.dmgcodevil.jmspy.test.data
     *                  <p/>
     *                  Arguments allows specify concrete classes that should be instrumented or whole packages.
     *                  The agent expects argument string in the next format:
     *                  [ {canonicalClassName}.class, {package}, {canonicalClassName}.class, ... ]
     *                  thus it's possible to specify classes or packages as much as needed
     *                  There are several significant notes:
     *                  1. If agentArgs wasn't specified (null or empty string) then all classes will be transformed if need be.
     *                  2. Classes and packages must be separated using ',' sign
     *                  3. Class name must end with '.class' suffix, otherwise it will be considered as a package name
     *                  <p/>
     *                  Preferred to use packages names in a agentArgs instead of specifying concrete classes names
     *                  instead of cases when you know exactly a class name in runtime,
     *                  because it's possible that nested or anonymous classes that have class name like 'com/site/project/URLClassPath$FileLoader$1'
     *                  will not be transformed, in this case better specify package name: 'com.site.project'
     * @param inst      the instrumentation instance
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new JmspyClassFileTransformer(agentArgs));
    }

    /**
     * Creates default constructor.
     *
     * @param classWriter the class writer
     */
    private static void createDefaultConstructor(ClassWriter classWriter) {
        // constructor
        MethodVisitor mv = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitMaxs(2, 1);
        mv.visitVarInsn(Opcodes.ALOAD, 0); // push `this` to the operand stack
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, Type.getInternalName(Object.class), "<init>", "()V"); // call the constructor of super class
        mv.visitInsn(Opcodes.RETURN);
        mv.visitEnd();
    }

    private static class ClassInfoReader implements ClassVisitor {
        private static final String DEFAULT_CONSTRUCTOR_SIGNATURE = "<init>()V";
        private boolean defaultConstructor;


        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {

        }

        @Override
        public void visitSource(String source, String debug) {

        }

        @Override
        public void visitOuterClass(String owner, String name, String desc) {

        }

        @Override
        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            return null;
        }

        @Override
        public void visitAttribute(Attribute attr) {

        }

        @Override
        public void visitInnerClass(String name, String outerName, String innerName, int access) {

        }

        @Override
        public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
            return null;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            if (DEFAULT_CONSTRUCTOR_SIGNATURE.equals(name + desc)) {
                defaultConstructor = true;
            }
            return null;
            //return super.visitMethod(access, name, desc, signature, exceptions);
        }

        @Override
        public void visitEnd() {

        }

        public boolean isDefaultConstructor() {
            return defaultConstructor;
        }
    }

    private static class JmspyClassFileTransformer implements ClassFileTransformer {

        //no transform is performed
        private static final byte[] NO_TRANSFORM = null;

        private Set<String> instrumentedPackages = Collections.emptySet();
        private Set<String> instrumentedClasses = Collections.emptySet();

        private JmspyClassFileTransformer(String agentArgs) {
            if (agentArgs != null && agentArgs.trim().length() > 0) {
                agentArgs = agentArgs.replace(".", "/");
                String[] elements = agentArgs.split(",");
                if (elements.length > 0) {
                    instrumentedPackages = new HashSet<>();
                    instrumentedClasses = new HashSet<>();
                    for (String el : elements) {
                        el = el.trim();
                        if (el.endsWith("/class")) {
                            instrumentedClasses.add(el.replace("/class", ""));
                        } else {
                            instrumentedPackages.add(el);
                        }
                    }
                }
            }
        }

        private boolean isInstrumented(String className) {
            // if no arrgs were passed to agent then all classes must be instrumented
            if (instrumentedClasses.isEmpty() && instrumentedPackages.isEmpty()) {
                return true;
            }
            if (instrumentedClasses.contains(className)) {
                return true;
            }
            for (String instrumentedPkg : instrumentedPackages) {
                if (className.startsWith(instrumentedPkg)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public byte[] transform(ClassLoader classLoader, String className, Class<?> aClass,
                                ProtectionDomain protectionDomain, byte[] bytes)
                throws IllegalClassFormatException {

            if (isInstrumented(className)) {
                try {
                    ClassReader classReader = new ClassReader(bytes);
                    ClassWriter classWriter = new ClassWriter(0);

                    // read class info
                    ClassInfoReader classInfoReader = new ClassInfoReader();
                    classReader.accept(classInfoReader, 0);

                    if (!classInfoReader.isDefaultConstructor()) {
                        createDefaultConstructor(classWriter);
                    }

                    ClassVisitor classVisitor = new ClassAdapter(classWriter) {
                        @Override
                        public void visit(int version, int access, String name, String signature, String superName,
                                          String[] interfaces) {
                            int removeFinal = access & (~Opcodes.ACC_FINAL);
                            super.visit(version, removeFinal, name, signature, superName, interfaces);
                        }

                        @Override
                        public MethodVisitor visitMethod(int access, String name, String desc, String signature,
                                                         String[] exceptions) {
                            int removeFinal = access & (~Opcodes.ACC_FINAL);
                            return super.visitMethod(removeFinal, name, desc, signature, exceptions);
                        }
                    };


                    classReader.accept(classVisitor, 0);

                    return classWriter.toByteArray();
                } catch (Throwable e) {
                    // add logger
                    System.out.println(e.getMessage());
                    //throw new IllegalClassFormatException(e.getMessage());
                }
            }

            return NO_TRANSFORM;
        }
    }

}
