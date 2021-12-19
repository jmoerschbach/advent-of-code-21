package day06

import java.io.File

fun main() {

    val input = File("/home/jonas/IdeaProjects/AdventToCode21/src/day06/example").readLines()
        .flatMap { it.split(",").map { x -> x.toInt() } }
    part2(input)

}

fun part2(input: List<Int>) {
    val DAYS = 80
    var allFish = input.toMutableList()
    val map = mutableMapOf<Int, List<Int>>()
    for (day in 0 until DAYS) {
        if (allFish.size > 1000) {
           val tmp =  allFish.chunked(allFish.size/2)
            map[day] = tmp[1]
            allFish = tmp[0].toMutableList()
        } else {
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
        println("day $day: $allFish\n${map.size}")
        println()
    }

    map.forEach{}

//    calculateForStartingValue(8)
//    val numberOfNumbers = mutableListOf<Int>()
//    var totalFish = 0L
//    for (i in 0 until 9) {
//        numberOfNumbers.add(input.count { it == i })
//    }
//    for (i in numberOfNumbers.indices) {
//        val x = numberOfNumbers[i]
//        println("calculating for number $i")
//        if (x > 0) {
//            totalFish += x * calculateForStartingValue(i)
//        }
//    }
}

fun calculateForStartingValue(start: Int): Int {
    val days = 40

    val allFish = mutableListOf(start)
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
        println("day $day: $allFish")
    }
    return allFish.count()
}

fun part1(input: List<Int>) {
    val days = 200

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
    println("after $days days there are ${allFish.size} lanternfish")
}