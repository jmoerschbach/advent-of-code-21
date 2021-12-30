package day15

import java.io.File

data class Coords(val x: Int, val y: Int)
data class Node(var initialCost: Int, var calculatedCost: Int = Integer.MAX_VALUE, var visited: Boolean = false) {
    lateinit var coords: Coords

    override fun toString(): String {
        return "(${coords.x},${coords.y}):$initialCost"
//        return "$initialCost"
    }
}

fun main() {
    val input = File("/home/jonas/IdeaProjects/AdventToCode21/src/day15/example").readLines()
        .map {
            val line = it.map { c -> Node(Integer.parseInt(c.toString())) }.toTypedArray()
            line
        }.toTypedArray()
    for (row in input.indices) {
        for (col in input[row].indices) {
            input[row][col].coords = Coords(col, row)

        }
    }
    part1(input)
//    part2(input)
}

fun <T> printArray(array: Array<Array<T>>) {
    for (row in array.indices) {
        for (col in array[row].indices) {
            print(array[row][col])
        }
        println()
    }
}

fun part2(input: Array<Array<Node>>) {

    printArray(input)
    println()
    printArray(copyArray(input))
}

fun copyArray(input: Array<Array<Node>>): Array<Array<Node>> {
    return input.map {
        it.map { oldNode ->
            val newNode = oldNode.copy()
            newNode.coords = Coords(oldNode.coords.x, oldNode.coords.y)
            val newValue = oldNode.initialCost + 1
            newNode.initialCost = if (newValue > 9) 1 else newValue
            newNode
        }.toTypedArray()
    }.toTypedArray()
}

fun part1(input: Array<Array<Node>>) {
    println("bottom right reached with total risk of ${djikstra(input)}")
}

fun djikstra(input: Array<Array<Node>>): Int {
    var current = input[0][0]
    current.calculatedCost = 0
    current.initialCost = 0

    //crude djikstra
    while (true) {
        val list = findUnvisitedNeighbours(input, current)
        for (v in list) {
            val tmpDistance = v.initialCost + current.calculatedCost
            if (tmpDistance < v.calculatedCost) {
                v.calculatedCost = tmpDistance
            }
        }
        current.visited = true

        if (current.coords.y == input.lastIndex && current.coords.x == input[current.coords.y].lastIndex) {
            return current.calculatedCost
        }
        current = input.flatten().filter { !it.visited }.minByOrNull { it.calculatedCost }!!
    }
}


fun findUnvisitedNeighbours(input: Array<Array<Node>>, current: Node): List<Node> {
    val list = mutableListOf<Node>()
    if (current.coords.y - 1 >= 0) {
        list.add(input[current.coords.y - 1][current.coords.x])
    }
    if (current.coords.y + 1 <= input.lastIndex) {
        list.add(input[current.coords.y + 1][current.coords.x])
    }
    if (current.coords.x - 1 >= 0) {
        list.add(input[current.coords.y][current.coords.x - 1])
    }
    if (current.coords.x + 1 <= input[current.coords.y].lastIndex) {
        list.add(input[current.coords.y][current.coords.x + 1])
    }
    return list.filter { !it.visited }
}