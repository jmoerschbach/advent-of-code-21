package day16


import java.io.File

fun main() {
    val input = File("/home/jonas/IdeaProjects/AdventToCode21/src/day16/input").readLines().first()

//    part1(input)
//    part1("D2FE28")
//    part1("38006F45291200")
//    part1("EE00D40C823060")
//    part1("8A004A801A8002F478")
//    part1("620080001611562C8802118E34")
//    part1("C0015000016115A2E0802F182340")
//    part1("A0016C880162017C3686B18A3D4780")
    part2("9C0141080250320F1802104A08")

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
    println(versionNumbers.sum())

}

enum class Operator(val code: Long) {
    SUM(-1),
    PRODUCT(-2),
    MIN(-3),
    MAX(-4),
    GT(-5),
    LT(-6),
    EQU(-7),
    NONE(-8)
}

class Expression(val operator: Operator) {
    var succeedingExpression: Expression? = null
    private val operands: MutableList<Long> = mutableListOf()
    fun eval(): Long {
        val value = succeedingExpression?.eval()
        return when (operator) {
            Operator.SUM -> {
                val sum = operands.sum()
                sum + (value ?: 0)
            }
            Operator.PRODUCT -> {
                val product = operands.reduce { acc, l -> acc * l }
                product * (value ?: 1)
            }
            Operator.NONE -> {
                operands[0]
            }
            else -> {
                0
            }
        }
    }

    fun addOperand(operand: Long) {
        operands.add(operand)
    }

    override fun toString(): String {
        return "(${operator} ${succeedingExpression?.toString()}, ${operands.joinToString { it.toString() }})"
    }
}

fun eval(expression: List<Long>): Long {
    return when (expression[0]) {
        Operator.SUM.code -> {
            var sum = expression[1]
            for (i in 2 until expression.size) {
                sum += expression[i]
            }
            sum
        }
        Operator.PRODUCT.code -> {
            var sum = expression[1]
            for (i in 2 until expression.size) {
                sum *= expression[i]
            }
            sum
        }

        else -> {
            0
        }
    }
}

fun parseComplete(
    input: String,
    expressions: MutableList<MutableList<Long>>,
    currentExpression: MutableList<Long> = mutableListOf()
): String {
    val version = Integer.parseInt(input.substring(0..2), 2)
    val typeId = Integer.parseInt(input.substring(3..5), 2)
    var rest = input.removeRange(0..5)
    if (typeId == 4) {
        //literal
//        println("is literal! version: $version")

        val builder = StringBuilder()
        do {
            val group = rest.substring(0..4)
            rest = rest.removeRange(0..4)
            builder.append(group.substring(1))
        } while (group[0] == '1')
        val value = builder.toString().toLong(2)
//        println("parsed literal value: $value")
        currentExpression.add(value)
        return rest
    } else {
        //operator
//        println("is operator! version: $version")

        val lengthTypeId = if (rest[0] == '0') 0 else 1
        rest = rest.removeRange(0..0)
        val newExpression = mutableListOf<Long>()
        when (typeId) {
            0 -> newExpression.add(Operator.SUM.code)
            1 -> newExpression.add(Operator.PRODUCT.code)
            2 -> newExpression.add(Operator.MIN.code)
            3 -> newExpression.add(Operator.MAX.code)
            5 -> newExpression.add(Operator.GT.code)
            6 -> newExpression.add(Operator.LT.code)
            7 -> newExpression.add(Operator.EQU.code)
        }
        if (lengthTypeId == 0) {
            val subpacketLength = Integer.parseInt(rest.substring(0..14), 2)
            rest = rest.removeRange(0..14)
            val leftover = rest.length - subpacketLength
            while (rest.length > leftover) {
                rest = parseComplete(rest, expressions, newExpression)
            }
            expressions.add(newExpression)
            println("first: ${eval(newExpression)}")
            return rest
        } else {
            val subpacketNumber = Integer.parseInt(rest.substring(0..10), 2)
            rest = rest.removeRange(0..10)
            for (packetNr in 0 until subpacketNumber) {
                rest = parseComplete(rest, expressions, newExpression)
            }
            expressions.add(newExpression)
            println("second: ${eval(newExpression)}")
            return rest
        }
    }
}


fun part2(input: String) {
    val binaryString = convertToBinaryString(input)
    val expressions = mutableListOf<MutableList<Long>>()
    parseComplete(binaryString, expressions)
    expressions.forEach { println(it) }
}