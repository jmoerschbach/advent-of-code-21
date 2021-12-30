package day15

import java.io.File

data class Coords(val x: Int, val y: Int)
data class Node(var initialCost: Int, var calculatedCost: Int = Integer.MAX_VALUE, var visited: Boolean = false) {
    lateinit var coords: Coords

    override fun toString(): String {
        return "(${coords.x},${coords.y}):$initialCost"
//        return "$initialCost"
    }

    fun copyAndIncreaseBy(increment: Int): Node {
        val newNode = this.copy()
        newNode.coords = Coords(this.coords.x, this.coords.y)
        val newValue = this.initialCost + increment
        newNode.initialCost = if (newValue > 9) newValue - 9 else newValue
        return newNode
    }
}

fun main() {
    val input = File("/home/jonas/IdeaProjects/AdventToCode21/src/day15/input").readLines()
        .map {
            val line = it.map { c -> Node(Integer.parseInt(c.toString())) }
            line
        }
    for (row in input.indices) {
        for (col in input[row].indices) {
            input[row][col].coords = Coords(col, row)

        }
    }
//    part1(input)
    part2(input)
}

fun <T> printArray(array: List<List<T>>) {
    for (row in array.indices) {
        for (col in array[row].indices) {
            print(array[row][col])
        }
        println()
    }
}


fun part2(input: List<List<Node>>) {
    val tileSize = input.size
    val numOfTiles = 5
    val inflatedInput = MutableList(numOfTiles * tileSize) { MutableList<Node>(numOfTiles * tileSize) { Node(0) } }

    for (tileRow in 0 until numOfTiles) {
        for (tileCol in 0 until numOfTiles) {
            for (row in input.indices) {
                for (col in input[row].indices) {
                    val coords = Coords(tileCol * tileSize + col, tileRow * tileSize + row)
                    inflatedInput[coords.y][coords.x] =
                        input[row][col].copyAndIncreaseBy(tileRow + tileCol)
                    inflatedInput[coords.y][coords.x].coords = coords

                }
            }
        }
    }
    println("bottom right reached with total risk of ${djikstra(inflatedInput)}")


}

fun part1(input: List<List<Node>>) {
    println("bottom right reached with total risk of ${djikstra(input)}")
}

fun djikstra(input: List<List<Node>>): Int {
    var current = input[0][0]
    current.calculatedCost = 0
    current.initialCost = 0
    val unvisited = input.flatten().toMutableList()

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
        unvisited.remove(current)
        current = unvisited.minByOrNull { it.calculatedCost }!!
    }
}


fun findUnvisitedNeighbours(input: List<List<Node>>, current: Node): List<Node> {
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