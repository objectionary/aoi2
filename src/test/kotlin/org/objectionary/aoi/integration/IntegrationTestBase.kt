// SPDX-FileCopyrightText: Copyright (c) 2022 Roman Korostinskiy
// SPDX-License-Identifier: MIT

/*
 * SPDX-FileCopyrightText: Copyright (c) 2022 Olesia Subbotina
 * SPDX-License-Identifier: MIT
 */

package org.objectionary.aoi.integration

import org.objectionary.aoi.TestBase
import org.apache.commons.io.FileUtils
import org.objectionary.aoi.launch.launchAoi
import org.objectionary.aoi.sources.SourcesExtractor.Companion.documents
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Base class for testing decorators resolver
 */
open class IntegrationTestBase : TestBase {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    override fun doTest() {
        val path = getTestName()
        documents.clear()
        launchAoi(constructInPath(path))
        val actualFiles: MutableList<String> = mutableListOf()
        Files.walk(Paths.get(constructOutPath(path)))
            .filter(Files::isRegularFile)
            .forEach { actualFiles.add(it.toString()) }
        Files.walk(Paths.get(constructResultPath(path)))
            .filter(Files::isRegularFile)
            .forEach { file ->
                val actualBr: BufferedReader = File(file.toString()).bufferedReader()
                val actual = actualBr.use { it.readText() }.replace(" ", "")
                val expectedFile = actualFiles.find {
                    it.replace("out$sep", "in$sep").replaceFirst(path, "${path}_aoi2") == file.toString()
                }
                val expectedBr: BufferedReader = File(expectedFile.toString()).bufferedReader()
                val expected = expectedBr.use { it.readText() }.replace(" ", "")
                checkOutput(expected, actual)
            }
        try {
            val tmpDir =
                Paths.get((constructResultPath(path))).toString()
            FileUtils.deleteDirectory(File(tmpDir))
        } catch (e: Exception) {
            logger.error(e.printStackTrace().toString())
        }
    }

    override fun constructOutPath(directoryName: String): String =
        File(System.getProperty("user.dir")).resolve(
            File("src${sep}test${sep}resources${sep}integration${sep}out$sep$directoryName")
        ).absolutePath.replace("/", File.separator)

    override fun constructInPath(directoryName: String): String =
        File(System.getProperty("user.dir")).resolve(
            File("src${sep}test${sep}resources${sep}integration${sep}in$sep$directoryName")
        ).absolutePath.replace("/", File.separator)

    private fun constructResultPath(directoryName: String): String =
        File(System.getProperty("user.dir")).resolve(
            File("src${sep}test${sep}resources${sep}integration${sep}in$sep${directoryName}_aoi2")
        ).absolutePath.replace("/", File.separator)
}
