package day16


import java.io.File
import kotlin.math.max
import kotlin.math.min

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

enum class Operator {
    SUM,
    PRODUCT,
    MIN,
    MAX,
    GT,
    LT,
    EQU,
    NONE
}

class Expression(private val operator: Operator) {
    var succeedingExpression: Expression? = null
    private val operands: MutableList<Long> = mutableListOf()
    private val succedingExpressions: MutableList<Expression> = mutableListOf()
    fun eval(): Long {
        val value = succeedingExpression?.eval()
        return when (operator) {
            Operator.SUM -> {
                val sum = operands.sum()
//                sum + (value ?: 0)
                succedingExpressions.sumOf { it.eval() }
            }
            Operator.PRODUCT -> {
                succedingExpressions.map { it.eval() }.reduce { acc, l -> acc * l }
                val product = operands.reduce { acc, l -> acc * l }
                product * (value ?: 1)
            }
            Operator.MIN -> {
                val oneMin = operands.minOrNull()
                if (value == null)
                    oneMin!!
                else min(oneMin!!, value)
            }
            Operator.MAX -> {
                val oneMax = operands.maxOrNull()
                if (value == null)
                    oneMax!!
                else max(oneMax!!, value)
            }
            Operator.GT -> {

                return if (value == null) {
                    if (operands[0] > operands[1]) 1 else 0
                } else {
                    if (value > operands[0]) 1 else 0
                }
            }
            Operator.LT -> {
                return if (value == null) {
                    if (operands[0] > operands[1]) 0 else 1
                } else {
                    if (value > operands[0]) 0 else 1
                }
            }
            Operator.EQU -> {
                return if (value == null) {
                    if (operands[0] == operands[1]) 1 else 0
                } else {
                    if (value == operands[0]) 1 else 0
                }
            }
            else -> {
                operands[0]
            }
        }
    }

    fun addOperand(operand: Long) {
        operands.add(operand)
    }

    fun addExpression(expression: Expression) {
        succedingExpressions.add(expression)
    }

    override fun toString(): String {
        val expr = if (succeedingExpression == null) "" else succeedingExpression.toString() + ","
        return "(${operator} $expr ${operands.joinToString { it.toString() }})"
    }
}

fun parseComplete(
    input: String,
    currentExpression: Expression
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
        currentExpression.addOperand(value)
        return rest
    } else {
        //operator
//        println("is operator! version: $version")

        val lengthTypeId = if (rest[0] == '0') 0 else 1
        rest = rest.removeRange(0..0)

        val operator = when (typeId) {
            0 -> Operator.SUM
            1 -> Operator.PRODUCT
            2 -> Operator.MIN
            3 -> Operator.MAX
            5 -> Operator.GT
            6 -> Operator.LT
            7 -> Operator.EQU
            else -> Operator.NONE
        }
        val newExpression = Expression(operator)
        if (lengthTypeId == 0) {
            val subpacketLength = Integer.parseInt(rest.substring(0..14), 2)
            rest = rest.removeRange(0..14)
            val leftover = rest.length - subpacketLength
            while (rest.length > leftover) {
                rest = parseComplete(rest, newExpression)
            }
            currentExpression.succeedingExpression = newExpression
            println("first: $currentExpression")
            return rest
        } else {
            val subpacketNumber = Integer.parseInt(rest.substring(0..10), 2)
            rest = rest.removeRange(0..10)
            for (packetNr in 0 until subpacketNumber) {
                rest = parseComplete(rest, newExpression)
            }
            currentExpression.succeedingExpression = newExpression
            println("second: $currentExpression")
            return rest
        }
    }
}


fun part2(input: String) {
    val binaryString = convertToBinaryString(input)
    val startingExpression = Expression(Operator.SUM)
    startingExpression.addOperand(0)
    parseComplete(binaryString, startingExpression)
    println(startingExpression)
    println(startingExpression.eval())
}