package org.objectionary.aoi.sources

import com.jcabi.xml.XML
import com.jcabi.xml.XMLDocument
import com.yegor256.xsline.TrClasspath
import com.yegor256.xsline.Xsline
import org.eolang.parser.ParsingTrain
import org.objectionary.deog.abstract
import org.objectionary.deog.name
import org.objectionary.deog.packageName
import org.objectionary.deog.repr.DGraphNode
import org.slf4j.LoggerFactory
import org.w3c.dom.Document
import org.w3c.dom.Node
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Paths
import javax.xml.parsers.DocumentBuilderFactory

typealias GraphAbstracts = MutableMap<String, MutableSet<Node>>

val abstracts: GraphAbstracts = mutableMapOf()


class SourcesExtractor {
    private val logger = LoggerFactory.getLogger("org.objectionary.deog.launch.DeogLauncher")
    private val sep = File.separatorChar
    companion object {
        val documents: MutableMap<Document, String> = mutableMapOf()
    }

    fun collectDocuments(path: String): MutableMap<Document, String> {
        Files.walk(Paths.get(path))
            .filter(Files::isRegularFile)
            .forEach {
                val tmpPath = createTempDirectories(path)
                transformXml(it.toString(), tmpPath)
                documents[getDocument(tmpPath)!!] = tmpPath
            }
        documents.forEach {
            val objects: MutableList<Node> = mutableListOf()
            val docObjects = it.key.getElementsByTagName("o")
            for (i in 0 until docObjects.length) {
                objects.add(docObjects.item(i))
            }
            abstracts(objects)
        }
        return documents
    }

    /**
     * Creates a new xml by applying several xsl transformations to it
     *
     * @param inFilename to the input file
     * @param outFilename path to the output file
     */
    private fun transformXml(
        inFilename: String,
        outFilename: String
    ) {
        val xmir: XML = XMLDocument(File(inFilename))
        val after = Xsline(
            TrClasspath(
                ParsingTrain().empty(),
                "/org/eolang/parser/add-refs.xsl",
                "/org/eolang/parser/expand-aliases.xsl",
                "/org/eolang/parser/resolve-aliases.xsl",
                "/org/eolang/parser/add-default-package.xsl"
                // "/org/eolang/parser/wrap-method-calls.xsl"
            ).back()
        ).pass(xmir)
        File(outFilename).outputStream().write(after.toString().toByteArray())
    }

    /**
     * @param filename source xml filename
     * @return generated Document
     */
    private fun getDocument(filename: String): Document? {
        try {
            val factory = DocumentBuilderFactory.newInstance()
            FileInputStream(filename).use { return factory.newDocumentBuilder().parse(it) }
        } catch (e: Exception) {
            logger.error(e.printStackTrace().toString())
        }
        return null
    }

    private fun createTempDirectories(path: String): String {
        val tmpPath =
            "${path.substringBeforeLast(sep)}_aoi2$sep${path.substringAfterLast(sep)}"
        val forDirs = File(tmpPath.substringBeforeLast(sep)).toPath()
        Files.createDirectories(forDirs)
        val newFilePath = Paths.get(tmpPath)
        try {
            Files.createFile(newFilePath)
        } catch (e: Exception) {
            logger.error(e.message)
        }
        return tmpPath
    }

    @Suppress("PARAMETER_NAME_IN_OUTER_LAMBDA")
    private fun abstracts(objects: MutableList<Node>) =
        objects.forEach {
            val name = name(it)
            if (abstract(it) != null && name != null) {
                abstracts.getOrPut(name) { mutableSetOf() }.add(it)
            }
        }
}