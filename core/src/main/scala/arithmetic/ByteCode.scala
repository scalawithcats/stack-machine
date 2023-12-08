package arithmetic

import java.nio.ByteBuffer

object ByteCode {

  enum Expression extends arithmetic.Expression[Expression] {
    case Literal(value: Double)
    case Addition(left: Expression, right: Expression)
    case Subtraction(left: Expression, right: Expression)
    case Multiplication(left: Expression, right: Expression)
    case Division(left: Expression, right: Expression)

    def +(that: Expression): Expression = Addition(this, that)
    def *(that: Expression): Expression = Multiplication(this, that)
    def -(that: Expression): Expression = Subtraction(this, that)
    def /(that: Expression): Expression = Division(this, that)

    def compile: Program = {
      val p = Array.ofDim[Byte](16777216)
      val buffer = ByteBuffer.wrap(p)
      var limit = 0
      def loop(expr: Expression): Unit =
        expr match {
          case Literal(value) =>
            buffer.put(Op.Lit.ordinal.toByte)
            buffer.putDouble(value)
            limit = limit + 8 + 1
          case Addition(left, right) =>
            loop(left)
            loop(right)
            buffer.put(Op.Add.ordinal.toByte)
            limit = limit + 1
          case Subtraction(left, right) =>
            loop(left)
            loop(right)
            buffer.put(Op.Sub.ordinal.toByte)
            limit = limit + 1
          case Multiplication(left, right) =>
            loop(left)
            loop(right)
            buffer.put(Op.Mul.ordinal.toByte)
            limit = limit + 1
          case Division(left, right) =>
            loop(left)
            loop(right)
            buffer.put(Op.Div.ordinal.toByte)
            limit = limit + 1
        }

      loop(this)
      Program(buffer, limit)
    }

    def eval: Double = compile.eval
  }
  object Expression extends arithmetic.ExpressionConstructors[Expression] {
    def literal(value: Double): Expression = Literal(value)
  }

  enum Op {
    case Lit
    case Add
    case Sub
    case Mul
    case Div
  }

  final case class Program(program: ByteBuffer, limit: Int) {
    val machine = new StackMachine(program, limit)

    def eval: Double = machine.eval
  }

  final case class StackMachine(program: ByteBuffer, limit: Int) {
    // The data stack
    private val stack: Array[Double] = Array.ofDim[Double](256)

    object code {
      val lit = Op.Lit.ordinal.toByte
      val add = Op.Add.ordinal.toByte
      val sub = Op.Sub.ordinal.toByte
      val mul = Op.Mul.ordinal.toByte
      val div = Op.Div.ordinal.toByte
    }

    final def eval: Double = {
      val p = program.array()
      // sp points to first free element on the stack
      // stack(sp - 1) is the first element
      def loop(sp: Int, pc: Int): Double =
        if (pc == limit) stack(sp - 1)
        else
          p(pc) match {
            case code.lit =>
              stack(sp) = program.getDouble(pc + 1)
              loop(sp + 1, pc + 1 + 8)
            case code.add =>
              val a = stack(sp - 1)
              val b = stack(sp - 2)
              stack(sp - 2) = (a + b)
              loop(sp - 1, pc + 1)
            case code.sub =>
              val a = stack(sp - 1)
              val b = stack(sp - 2)
              stack(sp - 2) = (a - b)
              loop(sp - 1, pc + 1)
            case code.mul =>
              val a = stack(sp - 1)
              val b = stack(sp - 2)
              stack(sp - 2) = (a * b)
              loop(sp - 1, pc + 1)
            case code.div =>
              val a = stack(sp - 1)
              val b = stack(sp - 2)
              stack(sp - 2) = (a / b)
              loop(sp - 1, pc + 1)
          }

      loop(0, 0)
    }
  }
}
