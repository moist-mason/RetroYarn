package io.github.moistmason.retroyarn

import groovy.json.JsonSlurper

class GradleIo {
    static void init() {
        File.metaClass.getJson = { return delegate.exists() ? new JsonSlurper().parse(delegate as File) : [:] }
        String.metaClass.getUrl = { return (!delegate.isBlank() || !delegate.isEmpty()) ? new URL(delegate as String) : [:] }
    }
}
