package com.zy.ming.defendplugin

import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import org.gradle.api.Project
import org.gradle.util.GFileUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.IOUtils

class DefendTransform extends Transform {

    Project project
    DefendExt defendExt

    DefendTransform(Project project,DefendExt defendExt){
        this.project = project
        this.defendExt = defendExt
    }

    @Override
    String getName() {
        return "DefendTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        String replaceTag = "${File.separator}classes${File.separator}"
        transformInvocation.inputs.each {

            it.jarInputs.each { jar ->
                injectJar(jar,transformInvocation.outputProvider)
            }

            it.directoryInputs.each { dir ->
                File dstFile = transformInvocation.outputProvider.getContentLocation(dir.name,dir.contentTypes,dir.scopes, Format.DIRECTORY)
                dir.file.eachFileRecurse {dirFile ->
                    if (!dirFile.isDirectory()){
                        int index = dirFile.absolutePath.indexOf(replaceTag)
                        byte[] fileByte = injectClass(dirFile)
                        if (dirFile.exists()){
                            dirFile.delete()
                            dirFile.createNewFile()
                        }
                        FileOutputStream fileOutputStream = new FileOutputStream(dirFile.parentFile.absolutePath + File.separator + dirFile.name)
                        fileOutputStream.write(fileByte)
                        fileOutputStream.close()
                    }
                }
                GFileUtils.copyDirectory(dir.file,dstFile)
            }

        }
    }

    /**
     * 处理jar中的class
     */
    void injectJar(JarInput jarInput, TransformOutputProvider outputProvider){
        if (jarInput.file.getAbsolutePath().endsWith(".jar") || jarInput.file.getAbsolutePath().endsWith(".aar")) {
            //重名名输出文件,因为可能同名,会覆盖
            def jarName = jarInput.name
            def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
            if (jarName.endsWith(".jar")) {
                jarName = jarName.substring(0, jarName.length() - 4)
            }
            JarFile jarFile = new JarFile(jarInput.file)
            Enumeration enumeration = jarFile.entries()
            File tmpFile = new File(jarInput.file.getParent() + File.separator + "classes_temp.jar")
            //避免上次的缓存被重复插入
            if (tmpFile.exists()) {
                tmpFile.delete()
            }
            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(tmpFile))
            //用于保存
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) enumeration.nextElement()
                String entryName = jarEntry.getName()
                ZipEntry zipEntry = new ZipEntry(entryName)
                InputStream inputStream = jarFile.getInputStream(jarEntry)
                String realName = entryName
                if (entryName.contains(File.separator)){
                    realName = entryName.substring(entryName.lastIndexOf(File.separator) + 1,entryName.length())
                }
                //插桩class
                if (realName.endsWith(".class")
                        && !realName.startsWith("R\$")
                        && !"R.class".equals(realName)
                        && !"BuildConfig.class".equals(realName)
                        && !entryName.startsWith("androidx${File.separator}")
                        && !entryName.startsWith("android${File.separator}")) {
                    //class文件处理
                    jarOutputStream.putNextEntry(zipEntry)
                    jarOutputStream.write(defendIntent(IOUtils.toByteArray(inputStream)))
//                    jarOutputStream.write(IOUtils.toByteArray(inputStream))
                } else {
                    jarOutputStream.putNextEntry(zipEntry)
                    jarOutputStream.write(IOUtils.toByteArray(inputStream))
                }
                jarOutputStream.closeEntry()
            }
            //结束
            jarOutputStream.close()
            jarFile.close()
            def dest = outputProvider.getContentLocation(jarName + md5Name,
                    jarInput.contentTypes, jarInput.scopes, Format.JAR)
            GFileUtils.copyFile(tmpFile, dest)
            tmpFile.delete()
        }
    }

    byte[] injectClass(File inputFile){
        String name = inputFile.name
        byte[] fileByte =  inputFile.readBytes()
        if (name.endsWith(".class")
                && !name.startsWith("R\$")
                && !"R.class".equals(name)
                && !"BuildConfig.class".equals(name) && !defendExt.classWhiteList.contains(name)){
            println("DefendTransform---------injectClass--------${name}")
            fileByte = defendIntent(fileByte)
        }
        return fileByte
    }

    byte[] defendIntent(byte[] classFile){
        ClassReader classReader = new ClassReader(classFile)
        ClassWriter classWriter = new ClassWriter(classReader,ClassWriter.COMPUTE_MAXS)
        ClassVisitor cv = new DefendClassVisitor(classWriter,defendExt)
        classReader.accept(cv,ClassReader.EXPAND_FRAMES)
        return classWriter.toByteArray()
    }
}