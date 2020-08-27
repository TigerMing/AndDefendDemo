package com.zy.ming.defendplugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.api.ApplicationVariant
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.tasks.TaskState

class DefendPlugin implements Plugin<Project> {

    Project project
    DefendExt defendExt

    @Override
    void apply(Project project) {
        this.project = project
        this.defendExt = project.extensions.create("defendExt",DefendExt)

        if (project.plugins.hasPlugin(AppPlugin)){
            println("DefendPlugin---------begain")
            def android = project.extensions.getByType(AppExtension)

            android.applicationVariants.all{ ApplicationVariant variant ->

                this.project.gradle.taskGraph.addTaskExecutionListener(new TaskExecutionListener() {
                    @Override
                    void beforeExecute(Task task) {
                    }

                    @Override
                    void afterExecute(Task task, TaskState taskState) {

                    }
                })
            }
            android.registerTransform(new DefendTransform(project,defendExt))
        }
    }
}