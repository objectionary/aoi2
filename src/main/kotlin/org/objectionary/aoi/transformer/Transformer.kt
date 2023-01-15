package org.objectionary.aoi.transformer

import org.objectionary.aoi.sources.SourcesExtractor.Companion.documents
import org.objectionary.deog.name
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.FileOutputStream
import java.io.OutputStream
import java.io.UnsupportedEncodingException
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

class FileTransformer {

    /**
     * Aggregates the process of adding an aoi section to xmir documents
     */
    fun addAoiSection(absMap: MutableMap<Node, MutableSet<String>>) {
        documents.forEach { doc ->
            val program = doc.key.getElementsByTagName("program").item(0)
            val aoiChild: Element = doc.key.createElement("aoi")
            addAoiChildren(aoiChild, absMap)
            program.appendChild(aoiChild)
        }
        transformDocuments()
    }

    @Suppress("CUSTOM_LABEL")
    private fun addAoiChildren(parent: Element, absMap: MutableMap<Node, MutableSet<String>>) {
        absMap
            .filter { it.key.ownerDocument == parent.ownerDocument }
            .forEach { el ->
                if (el.value.size > 0) {
                    val obj = parent.ownerDocument.createElement("obj")
                    val inferred: Element = parent.ownerDocument.createElement("inferred")
                    val name = name(el.key)
                    obj.setAttribute("name", name!!)
                    el.value.forEach {
                        val element = parent.ownerDocument.createElement("obj")
                        element.setAttribute("name", it)
                        inferred.appendChild(element)
                    }
                    obj.appendChild(inferred)
                    parent.appendChild(obj)
                }
            }
    }

    private fun getFqn(name: String, par: Node): String {
        var fqn = name
        var parent = par
        while (name(parent) != null) {
            fqn = "${name(parent)}.$fqn"
            parent = parent.parentNode
        }
        return fqn
    }

    private fun transformDocuments() {
        documents.forEach { doc ->
            val outputStream = FileOutputStream(doc.value)
            outputStream.use { writeXml(it, doc.key) }
        }
    }

    /**
     * Writes transformed [document] to [output]
     *
     * @param document transformed document
     * @param output where to write the result
     */
    @Throws(TransformerException::class, UnsupportedEncodingException::class)
    private fun writeXml(output: OutputStream, document: Document) {
        val prettyPrintXlst = this.javaClass.getResourceAsStream("pretty_print.xslt")
        val transformer = TransformerFactory.newInstance().newTransformer(StreamSource(prettyPrintXlst))
        transformer.setOutputProperty(OutputKeys.INDENT, "yes")
        transformer.setOutputProperty(OutputKeys.STANDALONE, "no")
        val source = DOMSource(document)
        val result = StreamResult(output)
        transformer.transform(source, result)
    }
}