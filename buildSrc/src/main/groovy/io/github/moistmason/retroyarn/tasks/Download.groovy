package io.github.moistmason.retroyarn.tasks

import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.plugins.ide.eclipse.model.Output

class Download extends DefaultTask {
    @Input URL src
    @OutputFile File dest

    @TaskAction
    void exec() {
        if (!dest.exists()) {
            FileUtils.copyURLToFile(src, dest)
        }
    }
}
