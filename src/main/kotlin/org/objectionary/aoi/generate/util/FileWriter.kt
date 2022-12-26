package org.objectionary.aoi.generate.util

import org.objectionary.aoi.generate.prologFile
import java.io.File

val plFile = File(prologFile).outputStream()
val nodes = mutableSetOf<String>() // todo write them to file

fun containsAttrFact(nodeName: String, childName: String) {
    plFile.write("contains_attr(${nodeName.replace('.', '-')}, ${childName.replace('.', '-')}, fact).\n".toByteArray())
    nodes.add(nodeName)
}

fun parentFact(nodeName: String, childName: String) {
    plFile.write("parent(${nodeName.replace('.', '-')}, ${childName.replace('.', '-')}, fact).\n".toByteArray())
}

fun isInstanceFact(nodeName: String, childName: String) {
    plFile.write("is_instance(${nodeName.replace('.', '-')}, ${childName.replace('.', '-')}, fact).\n".toByteArray())
}

fun appliedFact(obj: String, attr: String, appliedAttr: String) {
    plFile.write(
        ("applied(${obj.replace('.', '-')}, ${attr.replace('.', '-')}, " +
                "${appliedAttr.replace('.', '-')}, fact).\n").toByteArray()
    )
}

fun rules() {
    val rules = "\n% rules\n" +
            "contains_attr(X, Y, rule) :- parent(Z, X, _),\n" +
            "                             contains_attr(Z, Y, _).\n" +
            "contains_attr(X, Y, rule) :- is_instance(X, Z, fact),\n" +
            "                             contains_attr(Z, Y, _).\n" +
            "parent(X, Y, rule) :- parent(X, Z, fact), parent(Z, Y, fact).\n" +
            "applied(Base, Fa, Attr, rule) :- is_instance(X, Base, fact),\n" +
            "                                 applied(X, Fa, Attr, _)."

    plFile.write(rules.toByteArray())
}
