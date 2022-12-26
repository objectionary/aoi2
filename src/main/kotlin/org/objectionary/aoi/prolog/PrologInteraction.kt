package org.objectionary.aoi.prolog

import org.objectionary.aoi.generate.prologFile
import java.io.BufferedWriter
import java.io.File
import java.io.IOException
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets


class PrologInteraction {
    fun execute() {
        val output: String
        try {
            val builder = ProcessBuilder("swipl")
            builder.directory(File(System.getProperty("user.dir")))
            builder.redirectError(ProcessBuilder.Redirect.INHERIT)
            val process: Process = builder.start()
            val stdin = process.outputStream
            val writer = BufferedWriter(OutputStreamWriter(stdin))
            writer.write("consult(${prologFile}).\n")
            writer.write("findall(Y,contains_attr(cat, Y, _),L).\n")
            writer.write("findall(Y,contains_attr(dog, Y, _),L).\n")
            writer.write("findall(Y,contains_attr(some_object, Y, _),L).\n")
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
}
