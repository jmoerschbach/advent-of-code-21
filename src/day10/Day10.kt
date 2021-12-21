package day10


import java.io.File
import java.util.ArrayDeque

val beginningChars = listOf('(', '[', '{', '<')
val closeMap = mapOf('>' to '<', ']' to '[', ')' to '(', '}' to '{')
val openMap = mapOf('<' to '>', '[' to ']', '(' to ')', '{' to '}')
val points = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
val completionPoints = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)
fun main() {
    val input = File("/home/jonas/IdeaProjects/AdventToCode21/src/day10/input").readLines()
    part1(input)
    part2(input)

}

fun part1(input: List<String>) {

    println("Points: ${input.sumOf { isCorrupt(it) }}")
}

fun isCorrupt(s: String): Int {
    val stack = ArrayDeque<Char>()
    for (i in s.indices) {
        val c = s[i]
        if (beginningChars.contains(c)) {
            stack.push(c)
        } else {
            val openingChar = closeMap[c]
            if (stack.pop() != openingChar)
                return points.getOrDefault(c, 0)
        }
    }
    return 0
}

fun part2(input: List<String>) {
    val incompleteLines = input.filter { isCorrupt(it) == 0 }.map { completeLine(it) }.sorted()
    println("Middle ${incompleteLines[incompleteLines.size / 2]}")
}

fun completeLine(s: String): Long {
    val stack = ArrayDeque<Char>()
    val completionList = mutableListOf<Char>()

    for (i in s.indices) {
        val c = s[i]
        if (beginningChars.contains(c)) {
            stack.push(c)
        } else {
            val openingChar = closeMap[c]
            if (stack.peek() == openingChar) {
                stack.pop()
            } else {
                stack.push(c)
            }
        }
    }
    while (stack.isNotEmpty()) {
        completionList.add(openMap[stack.pop()]!!)
    }
    return completionList.map { completionPoints[it]!!.toLong() }.reduce { acc, p -> (acc * 5) + p }
}