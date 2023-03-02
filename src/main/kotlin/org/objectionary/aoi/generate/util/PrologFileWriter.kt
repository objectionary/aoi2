package org.objectionary.aoi.generate.util

import org.objectionary.aoi.generate.prologFile
import java.io.File

val plFile = File(prologFile).outputStream()
/**
 * @todo #22 write them to file
 */
val nodes = mutableSetOf<String>()


/**
 * Prints a "contains_attr(Obj, Attr, fact)" fact to a prolog file
 *
 * @param nodeName name of object
 * @param childName name of attribute that object contain
 */
fun containsAttrFact(nodeName: String, childName: String) {
    plFile.write("contains_attr(${nodeName.replace('.', '-')}, ${childName.replace('.', '-')}, fact).\n".toByteArray())
    nodes.add(nodeName)
}

/**
 * Prints a "parent(Base, Obj, fact)" fact to a prolog file
 *
 * @param nodeName name of base object (parent)
 * @param childName name of derived object
 */
fun parentFact(nodeName: String, childName: String) {
    plFile.write("parent(${nodeName.replace('.', '-')}, ${childName.replace('.', '-')}, fact).\n".toByteArray())
}

fun isInstanceFact(nodeName: String, childName: String) {
    plFile.write("is_instance(${nodeName.replace('.', '-')}, ${childName.replace('.', '-')}, fact).\n".toByteArray())
}

/**
 * Prints a "dot(Obj, Fa, Attr, fact)" fact to a prolog file
 *
 * @param obj the name of object where dot notation is occurred
 * @param attr the name of the free attribute to which dot notation applies
 * @param appliedAttr the name of the attribute that is applied using dot notation
 */
fun appliedFact(obj: String, attr: String, appliedAttr: String) {
    plFile.write(
        ("dot(${obj.replace('.', '-')}, ${attr.replace('.', '-')}, " +
                "${appliedAttr.replace('.', '-').replace("QQ", "qq")}, fact).\n").toByteArray()
    )
}

/**
 * Prints a set of rules to a prolog file
 */
fun rules() {
    val rules = """
        
        % rules
        contains_attr(Obj, Attr, rule) :- parent(Base, Obj, _),
                                     contains_attr(Base, Attr, _).
        contains_attr(Inst, Attr, rule) :- is_instance(Inst, Obj, fact),
                                     contains_attr(Obj, Attr, _).
        parent(Base, Obj2, rule) :- parent(Base, Obj1, fact), parent(Obj1, Obj2, fact).
        dot(Obj, Fa, Attr, rule) :- is_instance(Inst, Obj, fact), dot(Inst, Fa, Attr, _).
        """.trimIndent()

    plFile.write(rules.toByteArray())
}
