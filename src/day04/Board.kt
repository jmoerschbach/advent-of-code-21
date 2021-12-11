package day04

import kotlin.math.max

data class Field(val value: Int, var marked: Boolean = false) {

    override fun toString(): String {
        val marker = if (marked) "x" else "o"
        return "%02d|$marker".format(value)
    }
}

class Board(input: List<List<Int>>) {
    private var fields: List<List<Field>>
    var hasBingo = false

    init {
        fields = input.map { it.map { value -> Field(value) }.toList() }.toList()
    }

    fun checkNumber(number: Int) {
        traverse(
            Unit,
            { _: Unit, _: Int -> },
            { _, i: Int, j: Int -> if (fields[i][j].value == number) fields[i][j].marked = true })
    }

//    fun checkNumber(number: Int) {
//        for (i in fields.indices) {
//            for (j in fields[i].indices) {
//                if (number == fields[i][j].value)
//                    fields[i][j].marked = true
//            }
//        }
//    }

    fun getSum(): Int {
        return traverse(
            0,
            { total: Int, _: Int -> total },
            { total: Int, i: Int, j: Int -> if (!fields[i][j].marked) total + fields[i][j].value else total })
    }

    //    fun getSum(): Int {
//        var sum = 0
//        for (i in fields.indices) {
//            for (j in fields[i].indices) {
//                if (!fields[i][j].marked)
//                    sum += fields[i][j].value
//            }
//        }
//        return sum
//    }

    override fun toString(): String {
        return traverse("", { s: String, _: Int -> s + "\n" }, { s: String, i: Int, j: Int -> s + fields[i][j] + " " })
    }

    //    override fun toString(): String {
//        val output = StringBuilder()
//        for (i in fields.indices) {
//            for (j in fields[i].indices) {
//                output.append(fields[i][j]).append(" ")
//            }
//            output.appendLine()
//        }
//        return output.toString()
//    }

    private fun <R> traverse(
        initial: R,
        applyOnRow: (acc: R, row: Int) -> R,
        applyOnRowAndColumn: (acc: R, row: Int, column: Int) -> R
    ): R {
        var accumulator: R = initial
        for (i in fields.indices) {
            accumulator = applyOnRow(accumulator, i)
            for (j in fields[i].indices) {
                accumulator = applyOnRowAndColumn(accumulator, i, j)
            }
        }
        return accumulator
    }

    fun hasBingo(neededConsecutive: Int = 5): Boolean {
        var horizontalConsecutives = 0
        var verticalConsecutives = 0
        for (i in fields.indices) {
            var horizontal = 0
            var vertical = 0
            val chunksHorizontal = mutableListOf<Int>()
            val chunksVertical = mutableListOf<Int>()
            for (j in fields[i].indices) {
                if (fields[i][j].marked) {
                    horizontal++
                    if (j == fields[i].lastIndex) {
                        chunksHorizontal.add(horizontal)
                    }
                } else {
                    chunksHorizontal.add(horizontal)
                    horizontal = 0
                }
                if (fields[j][i].marked) {
                    vertical++
                    if (j == fields[j].lastIndex) {
                        chunksVertical.add(vertical)
                    }
                } else {
                    chunksVertical.add(vertical)
                    vertical = 0
                }
            }
            horizontalConsecutives = max(horizontalConsecutives, chunksHorizontal.maxOrNull() ?: 0)
            verticalConsecutives = max(verticalConsecutives, chunksVertical.maxOrNull() ?: 0)

        }
        hasBingo = max(verticalConsecutives, horizontalConsecutives) >= neededConsecutive
        return hasBingo
    }



}