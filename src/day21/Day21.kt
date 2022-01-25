package day21

import java.io.File
import kotlin.math.min

class Die {
    var numberOfRolls = 0
        private set

    private
    var currentValue = 0
    fun roll(): Int {
        numberOfRolls++
        return ++currentValue
    }
}

fun main() {
//    val input = File("/home/jonas/IdeaProjects/AdventToCode21/src/day21/example").readLines()
    part1(7,3)
}

fun part1(pos1:Int, pos2:Int) {
    val die = Die()
    var player1Position = pos1
    var player2Position = pos2
    var player1Score = 0
    var player2Score = 0

    var playerTurn = 0
    while (player1Score < 1000 && player2Score < 1000) {
        val steps = die.roll() + die.roll() + die.roll()
        if (playerTurn == 0) {
            playerTurn = 1
            player1Position += steps
            player1Position = ((player1Position - 1) % 10) + 1
            player1Score += player1Position
        } else {
            playerTurn = 0
            player2Position += steps
            player2Position = ((player2Position - 1) % 10) + 1
            player2Score += player2Position

        }
    }
    val looserScore = min(player1Score, player2Score)
    val result = looserScore*die.numberOfRolls
    println("result is $result")
}