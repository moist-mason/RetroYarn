package io.github.moistmason.retroyarn.tasks

import net.neoforged.srgutils.IMappingFile
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class VersionDifferences extends DefaultTask {
    @Input String versionOne
    @Input String versionTwo
    @InputFile File tinyOne
    @InputFile File tinyTwo
    @OutputFile File diff

    @TaskAction
    void exec() {
        IMappingFile mapOne = IMappingFile.load(tinyOne)
        IMappingFile mapTwo = IMappingFile.load(tinyTwo)


    }

    /**
     * STATUSES
     * > MAPPED_NEW -> original and mapped are different
     * > MAPPED_OLD -> original
     */

    static def getClassData(IMappingFile mapOne, IMappingFile mapTwo) {
        def dataMap = new HashMap<>()
        return dataMap
    }
}
