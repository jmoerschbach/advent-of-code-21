package day03


import java.io.File

const val DIGITS = 12
const val HIGHEST_DIGIT_INDEX = DIGITS - 1
fun main() {
    val input =
        File("/home/jonas/IdeaProjects/AdventToCode21/src/day03/input").useLines { it.toList() }.map { it.toInt(2) }
            .toList()
   part1(input)
    part2(input)

}

fun countBits(input: List<Int>): IntArray {
    val numOfOnes = IntArray(DIGITS)
    input.forEach {
        for (i in numOfOnes.indices) {
            numOfOnes[numOfOnes.size - 1 - i] += (it and (1 shl i)) shr i
        }
    }
    return numOfOnes
}

fun mostCommonBit(numOfOnes: IntArray, position: Int, totalNumber: Int): Int {
    val numOfZeroes = totalNumber - numOfOnes[position]
    return if (numOfOnes[position] >= numOfZeroes) 1 else 0
}

fun leastCommonBit(numOfOnes: IntArray, position: Int, totalNumber: Int): Int {
    val numOfZeroes = totalNumber - numOfOnes[position]
    return if (numOfOnes[position] < numOfZeroes) 1 else 0
}

fun part1(input: List<Int>) {
    val numOfOnes = countBits(input)

    val halfsize = input.size / 2;
    var gamma = 0
    for (i in numOfOnes.indices) {
        gamma += (mostCommonBit(numOfOnes, i, input.size)) shl numOfOnes.size - 1 - i
    }

    var epsilon = 0
    for (i in numOfOnes.indices) {
        epsilon += (if (numOfOnes[i] < halfsize) 1 else 0) shl numOfOnes.size - 1 - i
    }
    println("gamma: $gamma")
    println("epsilon: $epsilon")
    println("power: ${epsilon * gamma}")
}

fun part2(input: List<Int>) {
    val oxygen = oxygenGeneratorRating(input)
    val co2 = co2ScrubberRating(input)

    println("oxygen $oxygen, co2 $co2, life support ${oxygen * co2}")

}

fun oxygenGeneratorRating(input: List<Int>): Int {
    var work = input
    var currentDigitIndex = HIGHEST_DIGIT_INDEX

    while (work.size > 1 && currentDigitIndex >= 0) {
        val numOfOnes = countBits(work)
        val mcb = mostCommonBit(numOfOnes, HIGHEST_DIGIT_INDEX - currentDigitIndex, work.size)
        work = work.filter {
            val bla = (it and (1 shl currentDigitIndex)) shr currentDigitIndex
            (mcb == 1 && bla == 1) || (mcb == 0 && bla == 0)
        }.toList()
        currentDigitIndex--
    }
    return work.first()
}

fun co2ScrubberRating(input: List<Int>): Int {
    var work = input
    var currentDigitIndex = HIGHEST_DIGIT_INDEX

    while (work.size > 1 && currentDigitIndex >= 0) {
        val numOfOnes = countBits(work)
        val lcb = leastCommonBit(numOfOnes, HIGHEST_DIGIT_INDEX - currentDigitIndex, work.size)
        work = work.filter {
            val maskedAndShifted = (it and (1 shl currentDigitIndex)) shr currentDigitIndex
            (lcb == 1 && maskedAndShifted == 1) || (lcb == 0 && maskedAndShifted == 0)
        }.toList()

        currentDigitIndex--
    }

    return work.first()
}
