package day02

import java.io.File

fun main() {
    val input = File("/home/jonas/IdeaProjects/AdventToCode21/src/day02/input.txt").useLines { it.toList() }

    //part1(input)
    horizontalPosition = 0
    depth = 0
    aim = 0
    part2(input)
}

var horizontalPosition = 0
var depth = 0
var aim = 0

fun part1(input: List<String>) {
    input.forEach { parseCommandForPart1(it) }
    println("Endposition: forward ist $horizontalPosition und depth ist $depth und das Produkt ist ${horizontalPosition * depth}")

}

fun parseCommandForPart1(input: String) {
    val splitInput = input.split(" ")
    val command = splitInput[0]
    val value = splitInput[1].toInt()

    when (command) {
        "forward" -> horizontalPosition += value
        "down" -> depth += value
        "up" -> depth -= value
    }
}

fun part2(input: List<String>) {
    input.forEach { parseCommandForPart2(it) }
    println("Endposition: forward ist $horizontalPosition und depth ist $depth und das Produkt ist ${horizontalPosition * depth}")

}

fun parseCommandForPart2(input: String) {
    val splitInput = input.split(" ")
    val command = splitInput[0]
    val value = splitInput[1].toInt()

    when (command) {
        "forward" -> {
            horizontalPosition += value
            depth += value* aim
        }
        "down" -> aim += value
        "up" -> aim -= value
    }
}
