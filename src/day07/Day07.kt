package day07


import java.io.File
import kotlin.math.abs

fun main() {
    val input = File("/home/jonas/IdeaProjects/AdventToCode21/src/day07/input").readLines()
        .flatMap { it.split(",").map { n -> n.toInt() } }.toList()
    part1(input)
    part2(input)

}

fun part1(input: List<Int>) {
    var minFuel = Int.MAX_VALUE
    var position = input[0]
    for (i in input.indices) {
        var distanceDiff = 0
        for (j in input.indices) {
            distanceDiff += abs(input[i] - input[j])
        }
        if (distanceDiff < minFuel) {
            minFuel = distanceDiff
            position = input[i]
        }
    }
    println("position $position with min fuel $minFuel")
}

fun part2(input: List<Int>) {
    val maxPosition = input.maxOrNull()!!.toInt()
    val minPosition = input.minOrNull()!!.toInt()
    var minFuel = Int.MAX_VALUE
    var position = -1
    for (x in minPosition..maxPosition) {
        var neededFuel = 0
        for (i in input.indices) {
            val distanceDiff = abs(input[i] - x)
            val additionalFuel = (distanceDiff * (distanceDiff + 1)) / 2
            neededFuel += additionalFuel
        }
        if (neededFuel < minFuel) {
            minFuel = neededFuel
            position = x
        }
    }
    println("position $position with min fuel $minFuel")
}