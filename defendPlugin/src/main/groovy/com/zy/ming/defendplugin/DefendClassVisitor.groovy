package com.zy.ming.defendplugin

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class DefendClassVisitor extends ClassVisitor{

    DefendExt defendExt

    DefendClassVisitor(ClassVisitor classVisitor,DefendExt defendExt) {
        super(Opcodes.ASM5, classVisitor)
        this.defendExt = defendExt
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        mv = new DefendMethodVisitor(Opcodes.ASM5,mv,defendExt)
        return mv
    }
}