package day13


import java.io.File

fun main() {
    val input = File("/home/jonas/IdeaProjects/AdventToCode21/src/day13/example").readLines()
    val coordinates = input
        .filter { !it.contains("fold along") }.filter { it.isNotBlank() }
        .map { it.split(",").map { x -> x.toInt() } }

    val commands = input.filter { it.contains("fold along") }.map { it.substringAfter("fold along ") }.map {
        val c = it.split("=")
        Pair(c[0], Integer.parseInt(c[1]))
    }

    part1(coordinates, commands)
    part2(coordinates, commands)


}

fun findHighestXY(coordinates: List<List<Int>>): Pair<Int, Int> {
    return Pair(coordinates.maxOf { it[0] }, coordinates.maxOf { it[1] })
}

fun part1(coordinates: List<List<Int>>, commands: List<Pair<String, Int>>) {
    var array = createArray(coordinates)

    commands.take(1).forEach {
        val com = it.first
        val value = it.second
        array = if (com == "y") {
            foldUp(array, value)
        } else {
            foldLeft(array, value)
        }
    }
    printArray(array)
    println(countDots(array))

}

fun part2(coordinates: List<List<Int>>, commands: List<Pair<String, Int>>) {
    var array = createArray(coordinates)

    commands.forEach {
        val com = it.first
        val value = it.second
        array = if (com == "y") {
            foldUp(array, value)
        } else {
            foldLeft(array, value)
        }
    }
    printArray(array)
    println(countDots(array))

}

private fun createArray(coordinates: List<List<Int>>): Array<CharArray> {
    val (x, y) = findHighestXY(coordinates)
    var array = Array(y + 1) { CharArray(x + 1) }
    for (row in array.indices) {
        for (col in array[row].indices) {
            array[row][col] = '.'
        }
    }
    coordinates.forEach { array[it[1]][it[0]] = '#' }
    return array
}

fun countDots(array: Array<CharArray>): Int {
    return array.sumOf { it.count { c -> c == '#' } }
}

fun foldLeft(array: Array<CharArray>, i: Int): Array<CharArray> {
    return (transpone(fold(transpone(array), i)))
}

fun foldUp(array: Array<CharArray>, i: Int): Array<CharArray> {
    return fold(array, i)
}

fun fold(array: Array<CharArray>, i: Int): Array<CharArray> {
    val firstHalf = array.sliceArray(0 until i)
    val secondHalf = array.sliceArray(i + 1 until array.size)

    for (row in secondHalf.indices) {
        for (col in secondHalf[row].indices) {
            val newRow = firstHalf.lastIndex - row
            if (newRow in 0..firstHalf.lastIndex) {
                firstHalf[newRow][col] = if (array[newRow][col] == '#' || secondHalf[row][col] == '#') '#' else '.'
            }
        }
    }
    return firstHalf
}

fun transpone(array: Array<CharArray>): Array<CharArray> {
    val transponed = Array(array[0].size) { CharArray(array.size) }
    for (row in array.indices) {
        for (col in array[row].indices) {
            transponed[col][row] = array[row][col]
        }
    }
    return transponed
}

fun printArray(array: Array<CharArray>) {
    for (row in array.indices) {
        for (col in array[row].indices) {
            print(array[row][col])
        }
        println()
    }
}