package org.objectionary.aoi.launch

import org.objectionary.aoi.generate.PlGenerator
import org.objectionary.aoi.prolog.PrologInteraction

fun main() {
    launchAoi("C:\\Users\\lesya\\aoi-l\\src\\test\\resources\\integration\\in\\basic\\app.xmir")
}

fun launchAoi(path: String) {
    PlGenerator().generatePrologScripts(path)
    PrologInteraction().execute()
}