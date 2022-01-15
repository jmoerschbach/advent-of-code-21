package day20

import java.io.File

fun main() {
    val input = File("/home/jonas/IdeaProjects/AdventToCode21/src/day20/input").readLines()
    val algorithm = input.first()
    val inputImage = input.subList(2, input.size).map { it.toList() }

    part1(algorithm, inputImage)
    part2(algorithm,inputImage)
}

fun extendImage(inputImage: List<List<Char>>, extension: Char): List<List<Char>> {
    val extendedImage = inputImage.map {
        val row = mutableListOf(extension)
        row.addAll(it.toList())
        row.add(extension)
        row
    }.toMutableList()

    val row = mutableListOf<Char>()
    for (i in extendedImage[0].indices) {
        row.add(extension)
    }
    extendedImage.add(0, row)
    extendedImage.add(row)
    return extendedImage
}

fun <T> printArray(array: List<List<T>>) {
    for (row in array.indices) {
        for (col in array[row].indices) {
            print(array[row][col])
        }
        println()
    }
}

fun part1(algorithm: String, inputImage: List<List<Char>>) {
    val outputImage = enhance(algorithm, enhance(algorithm, inputImage, '.'), '#')
    val pixelsLit = outputImage.sumOf { it.sumOf { c -> if (c == '#') 1.toInt() else 0 } }
    println("pixelsLit: $pixelsLit")
}

fun part2(algorithm: String, inputImage: List<List<Char>>) {
    var outputImage= inputImage
    for (i in 0 until 50) {
        outputImage = enhance(algorithm, outputImage, if (i % 2 == 0) '.' else '#')
    }
    val pixelsLit = outputImage.sumOf { it.sumOf { c -> if (c == '#') 1.toInt() else 0 } }
    println("pixelsLit: $pixelsLit")


}

fun enhance(algorithm: String, inputImage: List<List<Char>>, extension: Char): List<List<Char>> {
    val extendedImage = extendImage(inputImage, extension)
    val outputImage = MutableList(extendedImage.size) { MutableList(extendedImage[0].size) { '.' } }
    for (row in extendedImage.indices) {
        for (col in extendedImage[row].indices) {
            val index = getNumberForPixel(row, col, extendedImage, extension)
            outputImage[row][col] = algorithm[index]
        }
    }
    return outputImage
}

fun getNumberForPixel(row: Int, col: Int, inputImage: List<List<Char>>, extension: Char): Int {
    val builder = StringBuilder()
    if (row <= 0) {
        builder.append(extension)
        builder.append(extension)
        builder.append(extension)
    } else {
        for (colIndex in col - 1..col + 1) {
            val c = if (colIndex < 0 || colIndex > inputImage[row - 1].lastIndex) extension else inputImage[row - 1][colIndex]
            builder.append(c)
        }
    }
    for (colIndex in col - 1..col + 1) {
        val c = if (colIndex < 0 || colIndex > inputImage[row].lastIndex) extension else inputImage[row][colIndex]
        builder.append(c)
    }
    if (row >= inputImage.lastIndex) {
        builder.append(extension)
        builder.append(extension)
        builder.append(extension)
    } else {
        for (colIndex in col - 1..col + 1) {
            val c = if (colIndex < 0 || colIndex > inputImage[row + 1].lastIndex) extension else inputImage[row + 1][colIndex]
            builder.append(c)
        }
    }
    val binaryString = builder.toString().map { if (it == '.') "0" else "1" }.joinToString("")
    return binaryString.toInt(2)
}
