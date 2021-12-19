package day09

import java.io.File

data class Coords(val row: Int, val col: Int)

data class LowPoint(val row: Int, val col: Int, val height: Int)

fun main() {
    val input = File("/home/jonas/IdeaProjects/AdventToCode21/src/day09/input").readLines().map {
        it.map { c -> Integer.parseInt(c.toString()) }
    }
    part1(input)
    part2(input)
}

fun findLowPoints(input: List<List<Int>>): List<LowPoint> {
    val lowPoints = mutableListOf<LowPoint>()
    for (row in input.indices) {
        for (col in input[row].indices) {
            var isLow = true
            val current = input[row][col]
            if (row > 0) {
                //check above
                if (current >= input[row - 1][col]) {
                    isLow = false
                }
            }
            if (col > 0) {
                //check left
                if (current >= input[row][col - 1]) {
                    isLow = false
                }
            }
            if (row < input.lastIndex) {
                //check below
                if (current >= input[row + 1][col]) {
                    isLow = false
                }
            }
            if (col < input[row].lastIndex) {
                //check right
                if (current >= input[row][col + 1]) {
                    isLow = false
                }
            }
            if (isLow) {
                lowPoints.add(LowPoint(row, col, current))
            }
        }
    }
    return lowPoints
}

fun part1(input: List<List<Int>>) {
    val riskLevel = findLowPoints(input).sumOf { it.height + 1 }
    println("Risk level is $riskLevel")
}

fun part2(input: List<List<Int>>) {
    val allLowPoints = findLowPoints(input)
    val allBasins = mutableListOf<List<Coords>>()
    allLowPoints.forEach {
        val basin = mutableListOf<Coords>()
        searchForBasin(input, basin, mutableListOf(), Coords(it.row, it.col))
        allBasins.add(basin)
    }
    allBasins.sortByDescending { it.size }
    val multiplied = allBasins.take(3).map { it.size }.reduce { acc, i -> acc * i }
    println("Multiple of largest basins: $multiplied")

}

fun searchForBasin(
    input: List<List<Int>>,
    basin: MutableList<Coords>,
    alreadyLookedAt: MutableList<Coords>,
    current: Coords
) {
    if (current.row < 0 || current.row > input.lastIndex || current.col < 0 || current.col > input[current.row].lastIndex)
        return
    if (alreadyLookedAt.contains(current)) {
        return
    }
    alreadyLookedAt.add(current)

    if (input[current.row][current.col] == 9) {
        return
    }

    if (current.row == input.lastIndex && current.col == input[current.row].lastIndex) {
        if (input[current.row][current.col] != 9) {
            basin.add(Coords(current.row, current.col))
        }
        return
    }
    basin.add(Coords(current.row, current.col))

    //vertical
    searchForBasin(input, basin, alreadyLookedAt, Coords(current.row + 1, current.col))
    searchForBasin(input, basin, alreadyLookedAt, Coords(current.row - 1, current.col))

    //horizontal
    searchForBasin(input, basin, alreadyLookedAt, Coords(current.row, current.col + 1))
    searchForBasin(input, basin, alreadyLookedAt, Coords(current.row, current.col - 1))


}