package org.objectionary.aoi.launch

import org.objectionary.aoi.generate.PlGenerator
import org.objectionary.aoi.prolog.PrologInteraction
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Aggregates the whole pipeline.
 *
 * @param path to input directory
 */
fun launchAoi(path: String) {
    Files.walk(Paths.get(path))
        .filter(Files::isRegularFile)
        .forEach {
            PlGenerator().generatePrologScripts(it.toString())
            PrologInteraction().execute()
        }
}
