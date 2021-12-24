package day11

import java.io.File

fun main() {
    val input = File("/home/jonas/IdeaProjects/AdventToCode21/src/day11/input").readLines()
        .map { it.map { c -> Integer.parseInt(c.toString()) }.toIntArray() }.toTypedArray()

//    part1(input)
    part2(input)
}


fun increase(input: Array<IntArray>, row: Int, col: Int) {
    if (row > input.lastIndex || row < 0 || col > input[row].lastIndex || col < 0) {
        return
    }
    if (input[row][col] < 0) {
        return
    }
    input[row][col] += 1
}

fun part1(input: Array<IntArray>) {
    var numberOfFlashes = 0
    for (step in 0 until 100) {
        numberOfFlashes += simulate(input)
    }
    println("$numberOfFlashes flashes")
}

fun part2(input: Array<IntArray>) {
    var numOfZeros = 0
    var steps = 0

    while (numOfZeros != 10) {
        simulate(input)
        numOfZeros = input.map {
            it.count { c -> c == 0 }
        }.count { it == 10 }
        steps++
    }
    println("steps needed to flash synchronously: $steps")
}

fun simulate(input: Array<IntArray>): Int {
    traverse(input) { row, col -> input[row][col] += 1 }
    var numberOfFlashes = 0
    var hasFlashed: Boolean
    do {
        hasFlashed = false
        traverse(input) { row, col ->
            if (input[row][col] > 9) {
                hasFlashed = true
                numberOfFlashes++
                input[row][col] = -1
                //above
                increase(input, row - 1, col)
                increase(input, row - 1, col - 1)
                increase(input, row - 1, col + 1)

                increase(input, row, col - 1)
                increase(input, row, col + 1)
                //below
                increase(input, row + 1, col)
                increase(input, row + 1, col - 1)
                increase(input, row + 1, col + 1)
            }
        }


    } while (hasFlashed)
    traverse(input) { row, col ->
        if (input[row][col] < 0) {
            input[row][col] = 0
        }
    }

    return numberOfFlashes
}

fun traverse(input: Array<IntArray>, toApply: (row: Int, col: Int) -> Unit) {
    for (row in input.indices) {
        for (col in input[row].indices) {
            toApply(row, col)
        }
    }
}