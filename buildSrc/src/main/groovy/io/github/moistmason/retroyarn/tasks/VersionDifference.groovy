package io.github.moistmason.retroyarn.tasks

import net.neoforged.srgutils.IMappingFile
import net.neoforged.srgutils.IMappingFile.IClass
import net.neoforged.srgutils.IMappingFile.IField
import net.neoforged.srgutils.IMappingFile.IMethod
import net.neoforged.srgutils.IMappingFile.INode
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class VersionDifference extends DefaultTask {
    @InputFile File tinyOne
    @InputFile File tinyTwo
    @OutputFile File diff

    @TaskAction
    def exec() {
        Map<String, Map<INode, INode>> statusMap = new HashMap<>()

        IMappingFile mapOne = IMappingFile.load(tinyOne)
        IMappingFile mapTwo = IMappingFile.load(tinyTwo)

        statusMap = getMapData(statusMap, mapOne, mapTwo)
    }

    static Map<String, Map<INode, INode>> getMapData(Map<String, Map<INode, INode>> statusMap, IMappingFile mapOne, IMappingFile mapTwo) {
        mapTwo.classes.each { clsTwo ->
            IClass clsOne = mapOne.getClass(clsTwo.original)
            String clsStatus = clsOne == null ? 'NEW' : getStatus(clsOne, clsTwo)
            statusMap.put(clsStatus, Collections.singletonMap(clsOne, clsTwo))
        }

        getFields(mapTwo).each { fldTwo ->
            IField fldOne = getFields(mapOne).find { it.original == fldTwo.original }
            String fldStatus = fldOne == null ? 'NEW' : getStatus(fldOne, fldTwo)
            statusMap.put(fldStatus, Collections.singletonMap(fldOne, fldTwo))
        }

        getMethods(mapTwo).each { mtdTwo ->
            IMethod mtdOne = getMethods(mapOne).find { it.original == mtdTwo.original }
            String mtdStatus = mtdOne == null ? 'NEW' : getStatus(mtdOne, mtdTwo)
            statusMap.put(mtdStatus, Collections.singletonMap(mtdOne, mtdTwo))
        }

        /* REMOVED */
        List<IClass> removedClasses = mapOne.classes.findAll { clsOne -> !mapTwo.contains(clsOne) }
        removedClasses.each { cls -> statusMap.put('REMOVED', Collections.singletonMap(cls, null)) }

        return statusMap
    }

    static Map<String, Map<INode, INode>> getRemoved(Map<String, Map<INode, INode>> statusMap, IMappingFile mapOne, IMappingFile mapTwo) {
        mapOne.classes.each { clsOne ->
            if (!mapTwo.classes.contains(clsOne)) {
                statusMap.put('REMOVED', Collections.singletonMap(clsOne, null))
            }
        }



        return statusMap
    }

    static List<IField> getFields(IMappingFile map) {
        List<IField> fields = new ArrayList<>()
        map.classes.each { cls -> fields.addAll(cls.fields) }
        return fields
    }

    static List<IMethod> getMethods(IMappingFile map) {
        List<IMethod> methods = new ArrayList<>()
        map.classes.each {  cls -> methods.addAll(cls.methods) }
        return methods
    }

    /**
     * STATUSES:
     * > NEW: node is new to map two
     * > MAPPED: node has been mapped from an intermediary name to a new name between versions
     * > CHANGED: node name has been changed in between versions
     * > REMOVED: node has been removed in map two.
     * @param nodeOne node of older version
     * @param nodeTwo node of newer version
     * @return status
     */
    static String getStatus(INode nodeOne, INode nodeTwo) {
        if (nodeOne.mapped != nodeTwo.mapped) {
            if (nodeOne.original != nodeTwo.mapped) {
                return 'MAPPED'
            } else {
                return 'CHANGED'
            }
        }
        return 'NULL'
    }
}
