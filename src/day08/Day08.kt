package day08

import java.io.File

val digitToSegmentMapping =
    mapOf(
        0 to listOf("a", "b", "c", "e", "f", "g"),
        1 to listOf("c", "f"),
        2 to listOf("a", "c", "d", "e", "g"),
        3 to listOf("a", "c", "d", "f", "g"),
        4 to listOf("b", "c", "d", "f"),
        5 to listOf("a", "b", "d", "f", "g"),
        6 to listOf("a", "b", "d", "e", "f", "g"),
        7 to listOf("a", "c", "f"),
        8 to listOf("a", "b", "c", "d", "e", "f", "g"),
        9 to listOf("a", "b", "c", "d", "f", "g")
    )

val segmentsToDigitMapping = mapOf(
    listOf("a", "b", "c", "e", "f", "g") to 0,
    listOf("c", "f") to 1,
    listOf("a", "c", "d", "e", "g") to 2,
    listOf("a", "c", "d", "f", "g") to 3,
    listOf("b", "c", "d", "f") to 4,
    listOf("a", "b", "d", "f", "g") to 5,
    listOf("a", "b", "d", "e", "f", "g") to 6,
    listOf("a", "c", "f") to 7,
    listOf("a", "b", "c", "d", "e", "f", "g") to 8,
    listOf("a", "b", "c", "d", "f", "g") to 9
)

val allSegments = listOf("a", "b", "c", "d", "e", "f", "g")

fun main() {
    val input = File("/home/jonas/IdeaProjects/AdventToCode21/src/day08/input").readLines()
    part1(input)
    part2(input)

}

fun part1(input: List<String>) {
    val outputValues = input.map {
        it.substringAfter("| ")
    }
    val easyDigits = outputValues.flatMap {
        it.split(" ").filter { n ->
            n.count() == 2 || n.count() == 3 || n.count() == 4 || n.count() == 7
        }
    }
    println(easyDigits.count())
}

fun part2(input: List<String>) {
    val decodedOutputs = input.map {
        val inputs = it.substringBeforeLast(" |").split(" ").map { s -> s.map { c -> c.toString() } }
        val outputs = it.substringAfter("| ").split(" ").map { s -> s.map { c -> c.toString() } }
        determineOutput(inputs, outputs)
    }
    println(decodedOutputs.sum())
}

fun determineOutput(encodedInput: List<List<String>>, encodedOutput: List<List<String>>): Int {
    val currentMapping = mutableMapOf(
        "a" to mutableListOf("a", "b", "c", "d", "e", "f", "g"),
        "b" to mutableListOf("a", "b", "c", "d", "e", "f", "g"),
        "c" to mutableListOf("a", "b", "c", "d", "e", "f", "g"),
        "d" to mutableListOf("a", "b", "c", "d", "e", "f", "g"),
        "e" to mutableListOf("a", "b", "c", "d", "e", "f", "g"),
        "f" to mutableListOf("a", "b", "c", "d", "e", "f", "g"),
        "g" to mutableListOf("a", "b", "c", "d", "e", "f", "g")
    )


    //order is important!
    //start with unique digits which are easy to identify
    decodeInputDigit(1, encodedInput, currentMapping)
    decodeInputDigit(7, encodedInput, currentMapping)
    decodeInputDigit(4, encodedInput, currentMapping)
    //then use selected digits to do the rest of the mapping
    decodeInputDigit(6, encodedInput, currentMapping)
    decodeInputDigit(2, encodedInput, currentMapping)
    decodeInputDigit(5, encodedInput, currentMapping)

    val reversedMapping = mutableMapOf<String, String>()
    currentMapping.forEach { (key, value) -> reversedMapping[value.first()] = key }

    val builder = StringBuilder()
    encodedOutput.map { decodeOutputDigit(it, reversedMapping) }.forEach { builder.append(it) }
    return Integer.parseInt(builder.toString())
}

private fun decodeInputDigit(
    digit: Int,
    encodedInput: List<List<String>>,
    currentMapping: MutableMap<String, MutableList<String>>
) {
    val encodedDigit = findDigit(digit, encodedInput, currentMapping)

    val segmentsToBeShown = digitToSegmentMapping[digit]!!
    val segmentsToBeNotShown = allSegments.minus(segmentsToBeShown.toSet())
    segmentsToBeShown.forEach {
        val oldValues = currentMapping[it]!!
        currentMapping[it] = oldValues.intersect(encodedDigit.toSet()).toMutableList()
    }
    segmentsToBeNotShown.forEach {
        val oldValues = currentMapping[it]!!
        currentMapping[it] = oldValues.minus(encodedDigit.toSet()).toMutableList()
    }
}

fun findDigit(
    toFind: Int, encodedInput: List<List<String>>, currentMapping: Map<String, List<String>>
): List<String> {

    return when (toFind) {
        1 -> encodedInput.first { it.size == 2 }
        7 -> encodedInput.first { it.size == 3 }
        4 -> encodedInput.first { it.size == 4 }
        6 -> {
            // 6 is one of 3 digits (0, 6, 9) with 6 segments
            // it is identified by not having BOTH segment c AND f on
            // at this point c and f are not uniquely mapped (e.g. c -> [a, b] and f -> [a, b]
            // so we are simply checking that not both of the possible segments are on
            encodedInput.filter { it.size == 6 }.first {
                !(it.contains(
                    currentMapping["c"]!![0]
                ) && it.contains(
                    currentMapping["c"]!![1]
                ))
            }
        }
        2 -> {
            encodedInput.filter { it.size == 5 }
                .first { it.contains(currentMapping["c"]!!.first()) && !it.contains(currentMapping["f"]!!.first()) }
        }
        5 -> {
            encodedInput.filter { it.size == 5 }
                .first { it.contains(currentMapping["f"]!!.first()) && !it.contains(currentMapping["c"]!!.first()) }
        }
        else -> {
            println("error not found: $toFind")
            listOf()
        }
    }

}

fun decodeOutputDigit(encodedOutputDigit: List<String>, mapping: Map<String, String>): Int {
    val list = encodedOutputDigit.map {
        mapping[it]!!.first().toString()
    }.sorted()
    return segmentsToDigitMapping[list]!!
}