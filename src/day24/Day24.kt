package day24

import java.io.File

enum class Operator { INP, ADD, MUL, DIV, MOD, EQL }
enum class Operand { LITERAL, X, W, Y, Z, NONE }
data class Instruction(val op: Operator, val operandA: Operand, val operandB: Operand, val literal: Int) {
    constructor(op: Operator, operandA: Operand, operandB: Operand) :
            this(op, operandA, operandB, 0)

}

class Alu {
    var w = 0
    var x = 0
    var y = 0
    var z = 0
    var inputFeeder = InputFeeder(0)

    private fun reset() {
        w = 0
        w = 0
        y = 0
        z = 0
    }

    fun evaluate(instructions: List<Instruction>) {
        reset()
        instructions.forEach {

            var result = 0
            when (it.op) {
                Operator.INP -> {
                    result = inputFeeder.getInput()
                }
                Operator.MUL -> {
                    result = getValue(it.operandA) * getValue(it)
                }
                Operator.DIV -> {
                    result = getValue(it.operandA) / getValue(it)
                }
                Operator.ADD -> {
                    result = getValue(it.operandA) + getValue(it)
                }
                Operator.MOD -> {
                    result = getValue(it.operandA) % getValue(it)
                }
                Operator.EQL -> {
                    result = if (getValue(it.operandA) == getValue(it)) 1 else 0
                }
            }
            storeValue(it.operandA, result)
        }
    }

    private fun storeValue(destinationRegister: Operand, value: Int) {
        when (destinationRegister) {
            Operand.W -> w = value
            Operand.X -> x = value
            Operand.Y -> y = value
            Operand.Z -> z = value
            else -> throw IllegalArgumentException("cannot store value in invalid register: $destinationRegister")
        }
    }

    private fun getValue(sourceRegister: Operand): Int {
        return when (sourceRegister) {
            Operand.W -> w
            Operand.X -> x
            Operand.Y -> y
            Operand.Z -> z
            else -> 0
        }
    }

    private fun getValue(input: Instruction): Int {
        return if (input.operandB == Operand.LITERAL)
            input.literal
        else getValue(input.operandB)

    }
}

fun main() {
    val input = File("/home/jonas/IdeaProjects/AdventToCode21/src/day24/input").readLines().map {
        val line = it.split(" ")
        val op = parseOperator(line[0])
        val operandA = parseOperand(line[1])
        val operandB = if (line.size > 2) parseOperand(line[2]) else Operand.NONE
        if (operandB == Operand.LITERAL)
            Instruction(op, operandA, operandB, line[2].toInt())
        else
            Instruction(op, operandA, operandB)

    }

    part1(input)
}

fun parseOperand(s: String): Operand {
    val potentialNumber = s.toIntOrNull()
    return if (potentialNumber != null)
        Operand.LITERAL
    else
        when (s) {
            "x" -> Operand.X
            "y" -> Operand.Y
            "z" -> Operand.Z
            "w" -> Operand.W
            "" -> Operand.NONE
            else -> throw IllegalArgumentException("unknown operand: $s")
        }
}

fun parseOperator(s: String): Operator {
    return when (s) {
        "inp" -> Operator.INP
        "add" -> Operator.ADD
        "mul" -> Operator.MUL
        "div" -> Operator.DIV
        "mod" -> Operator.MOD
        "eql" -> Operator.EQL
        else -> throw IllegalArgumentException("unknown operator")
    }
}

class InputFeeder(var startingValue: Long) {
    private var index = 0
    private var digits = splitIntoDigits(startingValue)

    fun calculateNextInput() {
        index = 0
        if (startingValue<0)
            throw IllegalArgumentException()
        do {
            digits = splitIntoDigits(startingValue--)
        } while (digits.contains(0))
//        println("will feed $digits")
    }

    fun getInput(): Int {
//        println("$index: ${digits[index]}")
        return digits[index++]
    }
}

fun splitIntoDigits(input: Long): List<Int> {
    var value = input
    val digits = mutableListOf<Int>()
    while (value > 0) {
        digits.add(0, (value % 10).toInt())
        value /= 10
    }
    return digits
}


fun part1(instructions: List<Instruction>) {
    val alu = Alu()
    val inputFeeder = InputFeeder(99999999999999)
    alu.inputFeeder = inputFeeder
    do {
        inputFeeder.calculateNextInput()
        alu.evaluate(instructions)
    } while (!isValid(alu))

    println(inputFeeder.startingValue)
}

fun isValid(alu: Alu): Boolean {
    return alu.z == 0
}
