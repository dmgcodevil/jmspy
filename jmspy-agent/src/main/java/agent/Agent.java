package agent;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * Class description.
 *
 * @author Raman_Pliashkou
 */
public class Agent {

    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader classLoader, String className, Class<?> aClass, ProtectionDomain protectionDomain, byte[] bytes)
                    throws IllegalClassFormatException {

                if (className.startsWith("com/github/dmgcodevil/jmspy/test/data")) {
                    try {
                        //if (isFinal(aClass.getModifiers())) { // todo
                        ClassReader classReader = new ClassReader(bytes);
                        ClassWriter classWriter = new ClassWriter(0);

                        ClassInfoReader classInfoReader = new ClassInfoReader();
                        classReader.accept(classInfoReader, 0);
                        if (classInfoReader.isDefaultConstructor()) {
                            System.out.println("has default constructor");
                        } else {
                            System.out.println("create default constructor");
                            createDefaultConstructor(classWriter);
                        }


                        ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM4, classWriter) {
                            @Override
                            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                                super.visit(version, access & (~Opcodes.ACC_FINAL), name, signature, superName, interfaces);
                            }

                            @Override
                            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                                return super.visitMethod(
                                        access & (~Opcodes.ACC_FINAL),
                                        name, desc, signature, exceptions);
                            }
                        };


                        classReader.accept(classVisitor, 0);

                        return classWriter.toByteArray();
                        // }
                    } catch (Throwable e) {
                        throw new IllegalClassFormatException(e.getMessage());
                    }
                }

                return null;
            }
        });
    }

    private static class ClassInfoReader extends ClassVisitor {
        private boolean defaultConstructor;

        public ClassInfoReader() {
            super(Opcodes.ASM4);
        }


        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            if ("<init>()V".equals(name + desc)) {
                defaultConstructor = true;
            }
            return super.visitMethod(access, name, desc, signature, exceptions);
        }

        public boolean isDefaultConstructor() {
            return defaultConstructor;
        }
    }

    private static void createDefaultConstructor(ClassWriter classWriter) {
        // constructor
        MethodVisitor mv = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitMaxs(2, 1);
        mv.visitVarInsn(Opcodes.ALOAD, 0); // push `this` to the operand stack
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, Type.getInternalName(Object.class), "<init>", "()V"); // call the constructor of super class
        mv.visitInsn(Opcodes.RETURN);
        mv.visitEnd();
    }

}
