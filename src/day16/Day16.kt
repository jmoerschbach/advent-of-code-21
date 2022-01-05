package day16


import java.io.File
import java.math.BigDecimal
import java.math.BigInteger

fun main() {
    val input = File("/home/jonas/IdeaProjects/AdventToCode21/src/day16/input").readLines().first()

    part1(input)
//    part1("D2FE28")
//    part1("38006F45291200")
//    part1("EE00D40C823060")
//    part1("8A004A801A8002F478")
//    part1("620080001611562C8802118E34")
//    part1("C0015000016115A2E0802F182340")
//    part1("A0016C880162017C3686B18A3D4780")

}

fun convertToBinaryString(input: String): String {
    val builder = StringBuilder()
    input.map {
        val value = Integer.parseInt(it.toString(), 16)
        String.format("%4s", Integer.toBinaryString(value)).replace(" ".toRegex(), "0")
    }.forEach { builder.append(it) }
    return builder.toString()
}

fun parse(input: String, versionNumbers: MutableList<Int>): String {
    val version = Integer.parseInt(input.substring(0..2), 2)
    val typeId = Integer.parseInt(input.substring(3..5), 2)
    versionNumbers.add(version)
    var rest = input.removeRange(0..5)
    if (typeId == 4) {
        //literal
        println("is literal! version: $version")

        val builder = StringBuilder()
        do {
            val group = rest.substring(0..4)
            rest = rest.removeRange(0..4)
            builder.append(group.substring(1))
        } while (group[0] == '1')
        val value = builder.toString().toLong(2)
        println("parsed literal value: $value")
        return rest
    } else {
        //operator
        println("is operator! version: $version")

        val lengthTypeId = if (rest[0] == '0') 0 else 1
        rest = rest.removeRange(0..0)
        if (lengthTypeId == 0) {
            val subpacketLength = Integer.parseInt(rest.substring(0..14), 2)
            rest = rest.removeRange(0..14)
            val leftover = rest.length - subpacketLength
            while (rest.length > leftover) {
                rest = parse(rest, versionNumbers)
            }
            return rest
        } else {
            val subpacketNumber = Integer.parseInt(rest.substring(0..10), 2)
            rest = rest.removeRange(0..10)
            for (packetNr in 0 until subpacketNumber) {
                rest = parse(rest, versionNumbers)
            }
            return rest
        }
    }
}


fun part1(input: String) {
    val binaryString = convertToBinaryString(input)

    val versionNumbers = mutableListOf<Int>()
    parse(binaryString, versionNumbers)
//    var rest = parseLiteral(binaryString, versionNumbers)
//    println(rest)
//    rest = parseOperator(rest, versionNumbers)
    println(versionNumbers.sum())

}