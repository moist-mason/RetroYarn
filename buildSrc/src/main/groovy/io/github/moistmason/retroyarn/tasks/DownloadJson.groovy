package io.github.moistmason.retroyarn.tasks

import io.github.moistmason.retroyarn.util.GradleIo
import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class DownloadJson extends DefaultTask {
    @Input String version
    @InputFile File manifest
    @OutputFile File dest

    @TaskAction
    void exec() {
        if (!dest.exists()) {
            URL src = getSrc(manifest, version)
            FileUtils.copyURLToFile(src, dest)
        }
    }

    static URL getSrc(File manifest, String version) {
        GradleIo.init()
        return new URL(manifest.json.versions.find { ver -> ver.id == version }.url)
    }
}
