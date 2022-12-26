package org.objectionary.aoi.prolog

import org.objectionary.aoi.generate.prologFile
import org.objectionary.aoi.generate.util.nodes
import org.objectionary.aoi.sources.abstracts
import org.objectionary.deog.name
import java.io.BufferedWriter
import java.io.File
import java.io.IOException
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

const val SWIPL_CONFIG = "swipl"
class PrologInteraction {
    fun execute() {
        val output: String
        try {
            val builder = ProcessBuilder(SWIPL_CONFIG)
            builder.directory(File(System.getProperty("user.dir")))
            builder.redirectError(ProcessBuilder.Redirect.INHERIT)
            val process: Process = builder.start()
            val stdin = process.outputStream
            val writer = BufferedWriter(OutputStreamWriter(stdin))
            writer.write(consult(prologFile))
            abstracts.forEach { abst -> abst.value.forEach { name(it)?.let { n -> writer.write(findall(n, "x")) } } }
            writer.flush()
            writer.close()
            output = String(process.inputStream.readAllBytes(), StandardCharsets.UTF_8)
            println(output)
            if (process.waitFor() != 0) {
                throw RuntimeException("External process finished with non zero exit value.")
            }
        } catch (e: IOException) {
            println(e.message)
        } catch (e: InterruptedException) {
            println(e.message)
        }
    }

    private fun consult(filename: String) = "consult('${filename}').\n"

//    private fun findall(node: String) = "findall(Y,contains_attr($node, Y, _),L).\n"

    private fun findall(node: String, freeVar: String) = "findall(Y, applied($node, $freeVar, Y, _),L).\n"

}
