package day01

import java.io.File

fun main() {
    val input = File("/home/jonas/IdeaProjects/AdventToCode21/src/day01/input.txt").useLines { it.toList() }.map { it.toInt() }.toList()
    part1(input)
    part2(input)


}

fun part1(input:List<Int>) {
    var numbers = 0

    for(item in 1 until input.size) {
        if(input[item] > input[item-1])
            numbers++
    }
    println(numbers)
}

fun part2(input:List<Int>) {
    val windowSize = 3
    var numbers = 0

    for(item in input.indices) {
       val firstWindowSum= windowSum(input, item, windowSize)
       val secondWindowSum = windowSum(input,item+1, windowSize)
       if(firstWindowSum < 0 || secondWindowSum < 0) {
           break;
       }else {
            if (secondWindowSum > firstWindowSum) {
                numbers++
            }
       }
    }


    println(numbers)

}

fun windowSum(input:List<Int>, index: Int, windowSize:Int) : Int {
    if(index+windowSize>input.size)
        return -1
    var sum = 0
    for(w in index until index+windowSize) {
        sum += input[w]
    }
    return sum
}
