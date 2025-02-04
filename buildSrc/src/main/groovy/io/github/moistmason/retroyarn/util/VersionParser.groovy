package io.github.moistmason.retroyarn.util

class VersionParser {
    File file
    Map<String, Integer> versions

    VersionParser(File file) {
        this.file = file
        this.versions = getVersions()
    }

    Map<String, Integer> getVersions() {
        Map<String, Integer> versions = new HashMap<>()
        file.eachLine { line ->
            versions.put(line, file.readLines().indexOf(line))
        }
        return versions
    }

    String getNext(String version) {
        int index = versions.get(version)
        String next = versions.entrySet().find {it.value == index + 1 }.key
        return next != null ? next : ""
    }
}
