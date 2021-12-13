package day05


import java.io.File
import kotlin.math.abs

fun main() {

    val input = File("/home/jonas/IdeaProjects/AdventToCode21/src/day05/input").readLines()
        .map {
            it.split(" -> ").map { line ->
                val x = line.split(",").map { n -> n.toInt() }
                Pair(x[0], x[1])
            }
        }
    part2(input)
}

fun part1(input: List<List<Pair<Int, Int>>>) {
    val array = Array(10) { IntArray(10) }
    val verticalHorizontalLines = input.filter {
        val start = it[0]
        val end = it[1]
        start.first == end.first || start.second == end.second
    }

    verticalHorizontalLines.forEach {

        val start = it[0]
        val end = it[1]
        if (start.first == end.first) {
            val diff = start.second - end.second
            val x = start.first
            if (diff >= 0) {
                for (i in start.second downTo start.second - diff) {
                    array[i][x] += 1
                }
            } else {
                for (i in end.second downTo end.second + diff) {
                    array[i][x] += 1
                }
            }
        } else {

            val diff = start.first - end.first
            val y = start.second
            if (diff >= 0) {
                for (i in start.first downTo start.first - diff) {
                    array[y][i] += 1
                }
            } else {
                for (i in end.first downTo end.first + diff) {
                    array[y][i] += 1
                }
            }
        }
    }
    printArray(array)
}

fun part2(input: List<List<Pair<Int, Int>>>) {
    val array = Array(1000) { IntArray(1000) }

    input.forEach {

        val start = it[0]
        val end = it[1]
        val diffX = start.first - end.first
        val diffY = start.second - end.second
//        println("$it: diffX: $diffX, diffY: $diffY")
        if (abs(diffX) == abs(diffY)) {
            //diagonal
            if (diffX > 0) {
                if (diffY > 0) {
                    //x+, y+ -> left up
                    for (row in 0..diffX) {
                        array[start.second - row][start.first - row] += 1
                    }
                } else {
                    //x+, y- -> left down
                    for (row in 0..diffX) {
                        array[start.second + row][start.first - row] += 1
                    }
                }
            } else {
                if (diffY > 0) {
                    //x-, y+ -> right up
                    for (row in 0..abs(diffX)) {
                        array[start.second - row][start.first + row] += 1
                    }
                } else {
                    //x-,y- -> right down
                    for (row in 0..abs(diffX)) {
                        array[start.second + row][start.first + row] += 1
                    }
                }

            }
        } else {
            //horizontal/vertical
            if (diffX == 0) { //-> vertical
                if (diffY < 0) {
                    for (row in 0..abs(diffY)) {
                        array[start.second + row][start.first] += 1
                    }
                } else {
                    for (row in 0..abs(diffY)) {
                        array[start.second - row][start.first] += 1
                    }
                }
            } else { //-> horizontal
                if (diffX < 0) {
                    for (row in 0..abs(diffX)) {
                        array[start.second][start.first + row] += 1
                    }
                } else {
                    for (row in 0..abs(diffX)) {
                        array[start.second][start.first - row] += 1
                    }
                }
            }
        }

    }
    printArray(array)
}

fun printArray(array: Array<IntArray>) {
    var numberOfOverlaps = 0
    for (i in array.indices) {
        for (j in array[i].indices) {
            print("${array[i][j]} ")
            if (array[i][j] > 1)
                numberOfOverlaps += 1
        }
        println()
    }
    println("#Overlaps: $numberOfOverlaps")
}