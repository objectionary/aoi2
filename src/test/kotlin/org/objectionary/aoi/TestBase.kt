// SPDX-FileCopyrightText: Copyright (c) 2022 Roman Korostinskiy
// SPDX-License-Identifier: MIT

/*
 * SPDX-FileCopyrightText: Copyright (c) 2022 Olesia Subbotina
 * SPDX-License-Identifier: MIT
 */

package org.objectionary.aoi

import java.io.File
import kotlin.test.assertEquals

/**
 * Common interface for all test classes
 */
interface TestBase {
    /**
     * File path separator
     */
    @Suppress("CUSTOM_GETTERS_SETTERS")
    val sep: Char
        get() = File.separatorChar

    /**
     * Constructs test execution process
     */
    fun doTest()

    /**
     * Compares expected test output with the actual one
     *
     * @param expected expected output
     * @param actual actual output
     */
    @Suppress("KDOC_WITHOUT_RETURN_TAG")
    fun checkOutput(
        expected: String,
        actual: String
    ) =
        assertEquals(
            expected.replace("\n", "").replace("\r", ""),
            actual.replace("\n", "").replace("\r", "")
        )

    /**
     * @param directoryName name of the input directory
     * @return path to input location
     */
    fun constructInPath(directoryName: String): String = "src${sep}test${sep}resources${sep}unit${sep}in$sep$directoryName"

    /**
     * @param directoryName name of the output directory
     * @return path to output location
     */
    fun constructOutPath(directoryName: String): String

    /**
     * @return name of the test being executed
     */
    fun getTestName() = Thread.currentThread().stackTrace[4].methodName
        .substring(5)
        .replace(' ', '_')
}
