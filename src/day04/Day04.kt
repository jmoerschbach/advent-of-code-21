package day04


import java.io.File

fun main() {
    val input = File("/home/jonas/IdeaProjects/AdventToCode21/src/day04/example").bufferedReader().readLines()
    val numbersToDraw = input[0].split(",").map { Integer.parseInt(it) }
    val boards = createBoards(input)
    part1(numbersToDraw, boards)
    println("### PART 2 ###")
    part2(numbersToDraw, boards)
}

private fun createBoards(input: List<String>): List<Board> {
    val boards = mutableListOf<Board>()

    val singleBoardInput = mutableListOf<List<Int>>()
    for (i in 2 until input.size) {
        val row = input[i]
        if (row.isBlank()) {
            boards.add(Board(singleBoardInput))
            singleBoardInput.clear()
        } else {
            val rowListString = row.trim().replace("  ", " ").split(" ")
            val rowList = rowListString.map { Integer.parseInt(it) }
            singleBoardInput.add(rowList)
        }
        if (i == input.lastIndex) {
            boards.add(Board(singleBoardInput))
        }
    }
    return boards
}

fun part1(numbersToDraw: List<Int>, boards: List<Board>) {
    var i = 0
    var hasWinner = false
    while (!hasWinner) {
        val currentNumber = numbersToDraw[i++]
        boards.forEach {
            it.checkNumber(currentNumber)
            if (it.hasBingo()) {
                println("Winner Board:\n$it")
                println("Sum ${it.getSum()} * $currentNumber = ${it.getSum() * currentNumber}")
                hasWinner = true
            }
        }
    }
}


fun part2(numbersToDraw: List<Int>, boards: List<Board>) {
    var unwonBoards = boards
    var i = 0
    var currentNumber = numbersToDraw[0]
    while (unwonBoards.size > 1) {
        unwonBoards.forEach {
            it.checkNumber(currentNumber)
            it.hasBingo()
        }
        currentNumber = numbersToDraw[++i]
        unwonBoards = unwonBoards.filter { !it.hasBingo }
    }

    val lastBoard = unwonBoards[0]
    while (!lastBoard.hasBingo()) {
        currentNumber = numbersToDraw[i++]
        lastBoard.checkNumber(currentNumber)
    }
    println("Last Board to win:\n$lastBoard")
    println("Sum ${lastBoard.getSum()} * $currentNumber = ${lastBoard.getSum() * currentNumber}")
}