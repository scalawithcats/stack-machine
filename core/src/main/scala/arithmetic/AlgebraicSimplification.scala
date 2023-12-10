package arithmetic

object AlgebraicSimplification {

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
      def loop(expr: Expression): List[Op] =
        expr match {
          case Addition(Literal(0.0), expr) => loop(expr)
          case Addition(expr, Literal(0.0)) => loop(expr)
          case Literal(value)               => List(Op.Lit(value))
          case Addition(left, right) =>
            loop(left) ++ loop(right) ++ List(Op.Add)
          case Subtraction(left, right) =>
            loop(left) ++ loop(right) ++ List(Op.Sub)
          case Multiplication(left, right) =>
            loop(left) ++ loop(right) ++ List(Op.Mul)
          case Division(left, right) =>
            loop(left) ++ loop(right) ++ List(Op.Div)
        }

      Program(loop(this))
    }

    def eval: Double = compile.eval
  }
  object Expression extends arithmetic.ExpressionConstructors[Expression] {
    def literal(value: Double): Expression = Literal(value)
  }

  enum Op {
    case Lit(value: Double)
    case Add
    case Sub
    case Mul
    case Div
  }

  final case class Program(program: List[Op]) {
    val machine = new StackMachine(program.toArray)

    def eval: Double = machine.eval
  }

  class StackMachine(program: Array[Op]) {
    // The data stack
    private val stack: Array[Double] = Array.ofDim[Double](256)

    final def eval: Double = {
      // sp points to first free element on the stack
      // stack(sp - 1) is the first element
      def loop(sp: Int, pc: Int): Double =
        if (pc == program.size) stack(sp - 1)
        else
          program(pc) match {
            case Op.Lit(value) =>
              stack(sp) = value
              loop(sp + 1, pc + 1)
            case Op.Add =>
              val a = stack(sp - 1)
              val b = stack(sp - 2)
              stack(sp - 2) = (a + b)
              loop(sp - 1, pc + 1)
            case Op.Sub =>
              val a = stack(sp - 1)
              val b = stack(sp - 2)
              stack(sp - 2) = (a - b)
              loop(sp - 1, pc + 1)
            case Op.Mul =>
              val a = stack(sp - 1)
              val b = stack(sp - 2)
              stack(sp - 2) = (a * b)
              loop(sp - 1, pc + 1)
            case Op.Div =>
              val a = stack(sp - 1)
              val b = stack(sp - 2)
              stack(sp - 2) = (a / b)
              loop(sp - 1, pc + 1)
          }

      loop(0, 0)
    }
  }
}
