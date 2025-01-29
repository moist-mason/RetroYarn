package io.github.moistmason.retroyarn.tasks

import net.neoforged.srgutils.IMappingFile
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class WriteCsv extends DefaultTask {
    @InputFile File tiny
    @OutputFile File classCsv
    @OutputFile File fieldCsv
    @OutputFile File methodCsv

    @TaskAction
    void exec() {
        IMappingFile mapping = IMappingFile.load(tiny)
        Map<String, String> classMap = getClassMap(mapping)
        Map<String, String> fieldMap = getFieldMap(mapping)
        Map<String, String> methodMap = getMethodMap(mapping)
        write(classMap, classCsv)
        write(fieldMap, fieldCsv)
        write(methodMap, methodCsv)
    }

    static void write(Map<String, String> map, File csv) {
        csv.withWriter { writer ->
            writer.write("interm,yarn\n")
            map.each { e -> writer.write(String.join(',', e.key, e.value) + "\n") }
            writer.flush()
        }
    }

    static Map<String, String> getClassMap(IMappingFile file) {
        Map<String, String> map = new HashMap<>()
        file.classes.each { cls -> map.put(cls.original, cls.mapped) }

        return map
    }

    static Map<String, String> getFieldMap(IMappingFile file) {
        Map<String, String> map = new HashMap<>()
        file.classes.each { clazz ->
            clazz.fields.each { fld -> map.put(fld.original, fld.mapped) }
        }

        return map
    }

    static Map<String, String> getMethodMap(IMappingFile file) {
        Map<String, String> map = new HashMap<>()
        file.classes.each { clazz ->
            clazz.methods.each { mtd -> map.put(mtd.original, mtd.mapped) }
        }

        return map
    }
}
