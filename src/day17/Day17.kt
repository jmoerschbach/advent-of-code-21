package day17

import java.io.File
import kotlin.math.max

data class Coords(val x: Int, val y: Int) {
    fun add(xVelocity: Int, yVelocity: Int): Coords {
        return Coords(x + xVelocity, y + yVelocity)
    }

    fun isInTarget(lower: Coords, upper: Coords): Boolean {
        return x >= lower.x && y >= lower.y && x <= upper.x && y <= upper.y
    }

    fun isDeflected(lower: Coords, upper: Coords): Boolean {
        return x > upper.x || y < lower.y
    }
}

fun main() {
    val input = File("/home/jonas/IdeaProjects/AdventToCode21/src/day17/input").readLines().first()
        .substringAfter("target area: ").split(", ").flatMap { it.substring(2).split("..") }.map { it.toInt() }

    part1(Coords(input[0], input[2]), Coords(input[1], input[3]))
    part2(Coords(input[0], input[2]), Coords(input[1], input[3]))
}

fun part1(lower: Coords, upper: Coords) {

    var overallHighestY = lower.y


    for (startingXVelocity in -1000 until 1000) {
        for (startingYVelocity in -1000 until 1000) {

            var currentPosition = Coords(0, 0)
            var highestY = lower.y
            var currentXVelocity = startingXVelocity
            var currentYVelocity = startingYVelocity

            while (!currentPosition.isDeflected(lower, upper)) {
                if (currentPosition.isInTarget(lower, upper)) {
                    overallHighestY = max(overallHighestY, highestY)
                    break
                }
                currentPosition = currentPosition.add(currentXVelocity, currentYVelocity)
                highestY = max(highestY, currentPosition.y)
                currentYVelocity--
                if (currentXVelocity > 0) {
                    currentXVelocity--
                } else if (currentXVelocity < 0) {
                    currentXVelocity++
                }
            }
        }
    }
    println("highest y=$overallHighestY")
}

fun part2(lower: Coords, upper: Coords) {

    var overallHighestY = lower.y
    val hittingInitialVelocities = mutableSetOf<Pair<Int, Int>>()

    for (startingXVelocity in -1000 until 1000) {
        for (startingYVelocity in -1000 until 1000) {

            var currentPosition = Coords(0, 0)
            var highestY = lower.y
            var currentXVelocity = startingXVelocity
            var currentYVelocity = startingYVelocity

            while (!currentPosition.isDeflected(lower, upper)) {
                if (currentPosition.isInTarget(lower, upper)) {
                    overallHighestY = max(overallHighestY, highestY)
                    hittingInitialVelocities.add(Pair(startingXVelocity, startingYVelocity))
                    break
                }
                currentPosition = currentPosition.add(currentXVelocity, currentYVelocity)
                highestY = max(highestY, currentPosition.y)
                currentYVelocity--
                if (currentXVelocity > 0) {
                    currentXVelocity--
                } else if (currentXVelocity < 0) {
                    currentXVelocity++
                }
            }
        }
    }
    println("# of distinct hitting initial velocities: ${hittingInitialVelocities.count()}")
}