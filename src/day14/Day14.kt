package day14

import java.io.File

fun main() {
    val input = File("/home/jonas/IdeaProjects/AdventToCode21/src/day14/input").readLines()
    val template = input[0]

    val insertionRules = input.filter { it.contains("->") }.associate {
        val line = it.split(" -> ")
        line[0] to line[1]
    }
    part1(template, insertionRules)
}

fun part1(template: String, rules: Map<String, String>) {
    var polymer = template
    for (i in 0 until 10) {
        polymer = replacePolymer(polymer, rules)
    }

    val list = rules.values.distinct().map { c -> polymer.asSequence().count { it.toString() == c } }.sortedDescending()
    println("most common - least common = ${list.first() - list.last()}")
}

fun replacePolymer(polymer: String, rules: Map<String, String>): String {
    val builder = StringBuilder()
    for (c in 0 until polymer.length - 1) {
        builder.append(polymer[c])
        builder.append(rules[polymer.substring(c, c + 2)])
    }
    builder.append(polymer[polymer.lastIndex])
    return builder.toString()
}