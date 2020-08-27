package com.zy.ming.defendplugin

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class DefendMethodVisitor extends MethodVisitor{

    DefendExt defendExt

    DefendMethodVisitor(int api, MethodVisitor methodVisitor,DefendExt defendExt) {
        super(api, methodVisitor)
        this.defendExt = defendExt
    }

    @Override
    void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        if (defendExt != null
                && opcode == Opcodes.INVOKEVIRTUAL
                && owner == "android/content/Intent"
                && defendExt.replaceArray != null
                && defendExt.replaceArray.size() > 0){
            ReplaceBean replaceBean = getReplaceDescriptor(descriptor)
            if (replaceBean != null){
                super.visitMethodInsn(Opcodes.INVOKESTATIC,defendExt.classPath,replaceBean.getMethod(),replaceBean.getDescriptor(),false)
                return
            }
        }
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
    }

    ReplaceBean getReplaceDescriptor(String descriptor){
        ReplaceBean replaceBean = null
        if (defendExt.replaceArray != null && defendExt.replaceArray.size() > 0) {
            defendExt.replaceArray.each {
                if (it.startsWith(descriptor)) {
                    replaceBean = new ReplaceBean(it)
                    return replaceBean
                }
            }
        }
        return replaceBean
    }

    @Override
    void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack, maxLocals)
    }
}

class ReplaceBean{

    String[] replaceArray

    public ReplaceBean(String replaceStr){
        replaceArray = replaceStr.split("=")
        if (replaceArray == null || replaceArray.length != 3){
            throw IllegalStateException("build.gradle ext replaceArray is wrong!")
        }
    }

    public String getDescriptor(){
        return replaceArray[2]
    }

    public String getMethod(){
        return replaceArray[1]
    }
}