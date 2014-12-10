package com.github.dmgcodevil.jmspy.agent;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 * Dummy implementation of {@link ClassVisitor}.
 *
 * @author dmgcodevil
 */
public class IdleClassVisitor implements ClassVisitor {
    @Override
    public void visit(int i, int i2, String s, String s2, String s3, String[] strings) {

    }

    @Override
    public void visitSource(String s, String s2) {

    }

    @Override
    public void visitOuterClass(String s, String s2, String s3) {

    }

    @Override
    public AnnotationVisitor visitAnnotation(String s, boolean b) {
        return null;
    }

    @Override
    public void visitAttribute(Attribute attribute) {

    }

    @Override
    public void visitInnerClass(String s, String s2, String s3, int i) {

    }

    @Override
    public FieldVisitor visitField(int i, String s, String s2, String s3, Object o) {
        return null;
    }

    @Override
    public MethodVisitor visitMethod(int i, String s, String s2, String s3, String[] strings) {
        return null;
    }

    @Override
    public void visitEnd() {

    }
}
