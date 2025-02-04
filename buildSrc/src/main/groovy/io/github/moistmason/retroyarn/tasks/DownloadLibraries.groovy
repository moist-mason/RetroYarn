package io.github.moistmason.retroyarn.tasks

import io.github.moistmason.retroyarn.util.GradleIo
import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class DownloadLibraries extends DefaultTask {
    @InputFile File versionJson
    @OutputFile File paths
    @OutputDirectory File directory

    @TaskAction
    void exec() {
        GradleIo.init()

        List<String> list = new ArrayList<String>()

        versionJson.json.libraries.each { lib ->
            def artifacts = (lib.downloads.artifact == null ? [] : [lib.downloads.artifact])
            artifacts.each { art ->
                File path = new File(directory, art.path)
                list.add(path.absolutePath)
                URL url = new URL(art.url)

                if (!path.exists()) {
                    FileUtils.copyURLToFile(url, path)
                }
            }
        }

        writePaths(paths, list)
    }

    static void writePaths(File paths, List<String> list) {
        paths.withWriter { writer ->
            list.each { s -> writer.write(s + "\n") }
            writer.flush()
        }
    }
}
