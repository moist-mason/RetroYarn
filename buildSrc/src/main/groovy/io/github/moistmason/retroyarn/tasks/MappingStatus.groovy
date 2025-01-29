package io.github.moistmason.retroyarn.tasks

import net.neoforged.srgutils.IMappingFile
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

import java.text.DecimalFormat
import java.text.NumberFormat

class MappingStatus extends DefaultTask {
    @Input String mcVersion
    @InputFile File tiny
    @OutputFile File log

    @TaskAction
    void exec() {
        IMappingFile mapping = IMappingFile.load(tiny)

        // mapped classes
        int c = mapping.classes.findAll { clazz -> clazz.original != clazz.mapped }.size()
        Map<Integer, Integer> classes = Collections.singletonMap(c, mapping.classes.size())

        // mapped fields
        int f = 0
        int fTotal = 0
        mapping.classes.each { clazz ->
            def matches = clazz.fields.findAll { fld -> fld.original != fld.mapped }
            f = f + matches.size()
            fTotal = fTotal + clazz.fields.size()
        }
        Map<Integer, Integer> fields = Collections.singletonMap(f, fTotal)

        // mapped methods
        int m = 0
        int mTotal = 0
        mapping.classes.each { clazz ->
            def matches = clazz.methods.findAll { mtd -> mtd.original != mtd.mapped }
            m = m + matches.size()
            mTotal = mTotal + clazz.methods.size()
        }
        Map<Integer, Integer> methods = Collections.singletonMap(m, mTotal)

        write(mcVersion, log, classes, methods, fields)
    }

    static def write(String version, File log, Map<Integer, Integer> classes, Map<Integer, Integer> methods, Map<Integer, Integer> fields) {
        log.withWriter { writer ->
            writer.write("VERSION: " + version + "\n")
            classes.each { e -> writer.write("CLASSES: " + writeLine(e.key, e.value) + "\n") }
            fields.each { e -> writer.write("FIELDS: " + writeLine(e.key, e.value) + "\n") }
            methods.each { e -> writer.write("METHODS: " + writeLine(e.key, e.value) + "\n") }
        }

    }

    static String writeLine(int numer, int denom) {
        return numer + " / " + denom + "\t(" + getPercent(numer, denom) + "%)"
    }

    /**
     * Simple percentage getter
     * @param numer
     * @param denom
     * @return the percent
     */
    static String getPercent(int numer, int denom) {
        NumberFormat form = new DecimalFormat("00.00")
        return form.format((numer / denom) * 100)
    }
}
