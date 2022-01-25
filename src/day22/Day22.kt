package day22

import java.io.File
import kotlin.math.max
import kotlin.math.min

data class Instruction(
    val on: Boolean,
    val minX: Int,
    val maxX: Int,
    val minY: Int,
    val maxY: Int,
    val minZ: Int,
    val maxZ: Int
)

data class Cuboid(val x: IntRange, val y: IntRange, val z: IntRange) {
    fun numberOfCubes(): Long {
        return x.count().toLong() * y.count() * z.count()
    }
}


fun main() {
    val input = File("/home/jonas/IdeaProjects/AdventToCode21/src/day22/input").readLines().map {
        val line = it.split(" ")
        val onOff = line[0] == "on"
        val coords = line[1].split(",")
        val x = coords[0].substring(2).split("..")
        val y = coords[1].substring(2).split("..")
        val z = coords[2].substring(2).split("..")
        Instruction(onOff, x[0].toInt(), x[1].toInt(), y[0].toInt(), y[1].toInt(), z[0].toInt(), z[1].toInt())
    }
    part2(input)
}

fun part1(input: List<Instruction>) {
    val ons = MutableList(102) { MutableList(102) { MutableList(102) { 0 } } }
    input.filter { it.minX >= -50 && it.maxX <= 50 }
        .filter { it.minY >= -50 && it.maxY <= 50 }
        .filter { it.minZ >= -50 && it.maxZ <= 50 }
        .forEach {
            for (x in it.minX + 50..it.maxX + 50) {
                for (y in it.minY + 50..it.maxY + 50) {
                    for (z in it.minZ + 50..it.maxZ + 50) {
                        ons[x][y][z] = if (it.on) 1 else 0
                    }
                }
            }
        }
    println(ons.sumOf { bla -> bla.sumOf { it.sum() } })
}

fun part2(input: List<Instruction>) {
    val reactorCore = mutableListOf<Pair<Int, Cuboid>>()
    input.forEach {
        val cuboidsToAdd = mutableListOf<Pair<Int, Cuboid>>()
        val currentCuboid = Cuboid(it.minX..it.maxX, it.minY..it.maxY, it.minZ..it.maxZ)

        if (it.on) {
            cuboidsToAdd.add(Pair(1, currentCuboid))
        }
        reactorCore.forEach { reactorCuboid ->
            val intersection = getIntersection(currentCuboid, reactorCuboid.second)
            if (intersection != null) {
                cuboidsToAdd.add(Pair(-reactorCuboid.first, intersection))
            }
        }
        reactorCore.addAll(cuboidsToAdd)
    }

    val numberOfOnCuboids =reactorCore.sumOf { it.first * it.second.numberOfCubes() }
println(numberOfOnCuboids)
}

fun getIntersection(a: Cuboid, b: Cuboid): Cuboid? {
    val intersection = Cuboid(
        max(a.x.first, b.x.first)..min(a.x.last, b.x.last),
        max(a.y.first, b.y.first)..min(a.y.last, b.y.last),
        max(a.z.first, b.z.first)..min(a.z.last, b.z.last)
    )
    if (intersection.x.isEmpty() || intersection.y.isEmpty() || intersection.z.isEmpty())
        return null
    return intersection
}