package org.objectionary.aoi.generate.util

import java.io.File

val plFile = File("proloog.pl").outputStream()

fun containsAttrFact(nodeName: String, childName: String) {
    plFile.write("contains_attr($nodeName, $childName, fact).\n".toByteArray())
}

fun parentFact(nodeName: String, childName: String) {
    plFile.write("parent($nodeName, $childName, fact).\n".toByteArray())
}

fun isInstanceFact(nodeName: String, childName: String) {
    plFile.write("is_instance($nodeName, $childName, fact).\n".toByteArray())
}

fun rules() {
    val rules = "\n% rules\n" +
            "contains_attr(X, Y, rule) :- parent(Z, X, _),\n" +
            "                             contains_attr(Z, Y, _).\n" +
            "contains_attr(X, Y, rule) :- is_instance(X, Z, fact),\n" +
            "                             contains_attr(Z, Y, _).\n" +
            "parent(X, Y, rule) :- parent(X, Z, fact), parent(Z, Y, fact)."

    plFile.write(rules.toByteArray())
}
