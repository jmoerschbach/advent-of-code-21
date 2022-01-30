package day12

import java.io.File
import kotlin.properties.Delegates

val regex = "[A-Z]+".toRegex()

data class Cave(val name: String) {

    val isBig = name.matches(regex)
    val isStart = name == "start"
    val isEnd = name == "end"

    override fun toString(): String {
        return name
    }
}

fun main() {

    val graph = mutableMapOf<Cave, MutableList<Cave>>()

    File("/home/jonas/IdeaProjects/AdventToCode21/src/day12/input").readLines().forEach {
        val srcDest = it.split("-")
        val src = Cave(srcDest[0])
        val dest = Cave(srcDest[1])
        addEdge(graph, src, dest)
        addEdge(graph, dest, src)
    }
    part1(graph)
    part2(graph)
}

fun addEdge(graph: MutableMap<Cave, MutableList<Cave>>, src: Cave, dest: Cave) {
    val edges = graph[src]
    if (edges == null)
        graph[src] = mutableListOf(dest)
    else
        edges.add(dest)
}

fun part1(graph: MutableMap<Cave, MutableList<Cave>>) {
    val currentPath = mutableListOf<Cave>()
    println(countPaths(graph, Cave("start"), currentPath))
}

fun countPaths(graph: MutableMap<Cave, MutableList<Cave>>, currentCave: Cave, currentPath: MutableList<Cave>): Int {
    if (currentPath.contains(currentCave) && currentCave.isStart) {
        return 0
    }
    if (currentPath.contains(currentCave) && !currentCave.isBig) {
        return 0
    }
    if (currentCave.isEnd) {
        return 1
    }

    var numberOfPaths = 0
    val caves = graph[currentCave]!!
    currentPath.add(currentCave)
    for (cave in caves) {
        numberOfPaths += countPaths(graph, cave, currentPath)
    }
    currentPath.remove(currentCave)
    return numberOfPaths
}

fun part2(graph: MutableMap<Cave, MutableList<Cave>>) {
    val currentPath = mutableListOf<Cave>()
    println(countPaths(graph, Cave("start"), currentPath, false))
}

fun countPaths(
    graph: MutableMap<Cave, MutableList<Cave>>,
    currentCave: Cave,
    currentPath: MutableList<Cave>,
    smallCaveAlreadyVisited: Boolean
): Int {

    if (currentPath.contains(currentCave) && currentCave.isStart) {
        return 0
    }
    var alreadyVisited = smallCaveAlreadyVisited
    if (currentPath.contains(currentCave) && !currentCave.isBig) {
        if (smallCaveAlreadyVisited) {
            return 0
        } else {
            alreadyVisited = true
        }
    }
    if (currentCave.isEnd) {
        return 1
    }

    var numberOfPaths = 0
    val caves = graph[currentCave]!!
    currentPath.add(currentCave)
    for (cave in caves) {
        numberOfPaths += countPaths(graph, cave, currentPath, alreadyVisited)
    }
    currentPath.remove(currentCave)
    return numberOfPaths
}