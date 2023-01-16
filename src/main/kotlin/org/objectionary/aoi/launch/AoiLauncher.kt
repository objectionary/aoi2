package org.objectionary.aoi.launch

import org.objectionary.aoi.generate.PlGenerator
import org.objectionary.aoi.prolog.PrologInteraction
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    launchAoi("C:\\Users\\lesya\\aoi-l\\src\\test\\resources\\integration\\in\\basic\\app.xmir")
}

fun launchAoi(path: String) {
    Files.walk(Paths.get(path))
        .filter(Files::isRegularFile)
        .forEach {
            PlGenerator().generatePrologScripts(it.toString())
            PrologInteraction().execute()
        }
}