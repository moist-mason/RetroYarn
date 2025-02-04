package io.github.moistmason.retroyarn.tasks

import io.github.moistmason.retroyarn.util.GradleIo
import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class DownloadJar extends DefaultTask {
    @Input String side
    @InputFile File versionJson
    @OutputFile File dest

    @TaskAction
    void exec() {
        URL src = getSrc(versionJson, side)
        FileUtils.copyURLToFile(src, dest)
    }

    static URL getSrc(File versionJson, String side) {
        GradleIo.init()
        return new URL(versionJson.json.downloads.get(side).url)
    }
}
