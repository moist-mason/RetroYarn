package io.github.moistmason.retroyarn

class MinecraftVersion {
    static Map<String, Integer> getVersions(File file) {
        Map<String, Integer> versions = new HashMap<>()
        file.eachLine { line ->
            versions.put(line, file.readLines().indexOf(line))
        }
        return versions
    }
}
