package org.objectionary.aoi.launch

import org.objectionary.aoi.generate.PlGenerator
import org.objectionary.aoi.prolog.PrologInteraction

fun main(args: Array<String>) {
    PlGenerator().generatePrologScripts("C:\\Users\\lesya\\aoi-l\\src\\test\\resources\\unit\\in\\instance_usages\\basic\\app.xmir")
    PrologInteraction().execute()
}