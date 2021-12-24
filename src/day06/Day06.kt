package day06

import java.io.File

fun main() {

    val input = File("/home/jonas/IdeaProjects/AdventToCode21/src/day06/input").readLines()
        .flatMap { it.split(",").map { x -> x.toInt() } }
    part1(input)
    part2(input)

}

fun part2(input: List<Int>) {
    val days = 256
    val array =
        arrayOf(
            input.count { it == 0 }.toLong(),
            input.count { it == 1 }.toLong(),
            input.count { it == 2 }.toLong(),
            input.count { it == 3 }.toLong(),
            input.count { it == 4 }.toLong(),
            input.count { it == 5 }.toLong(),
            input.count { it == 6 }.toLong(),
            input.count { it == 7 }.toLong(),
            input.count { it == 8 }.toLong()
        )



    for (day in 0 until days) {
        val newFish = array[0]
        array[0] = array[1]
        array[1] = array[2]
        array[2] = array[3]
        array[3] = array[4]
        array[4] = array[5]
        array[5] = array[6]
        array[6] = array[7]
        array[7] = array[8]
        array[8] = newFish
        array[6] += newFish
    }

    val lanternfish = array.sum()
    println("After $days days there are $lanternfish lanternfish")


}

fun part1(input: List<Int>) {
    val days = 80

    val allFish = input.toMutableList()
    for (day in 0 until days) {
        var laternfishToAdd = 0
        val iterator = allFish.listIterator()
        while (iterator.hasNext()) {
            val laternfish = iterator.next();
            if (laternfish == 0) {
                iterator.set(6)
                laternfishToAdd++
            } else {
                iterator.set(laternfish - 1)
            }
        }
        for (i in 0 until laternfishToAdd) {
            allFish.add(8)
        }

    }
    println("After $days days there are ${allFish.size} lanternfish")
}