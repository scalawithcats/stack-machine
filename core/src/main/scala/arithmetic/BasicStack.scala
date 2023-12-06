package arithmetic

object BasicStack {

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
          case Literal(value) => List(Op.Lit(value))
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
    val machine = new StackMachine(program)

    def eval: Double = machine.eval
  }

  class StackMachine(val program: List[Op]) {
    def eval: Double = {
      def pop2(data: List[Double]): (Double, Double, List[Double]) =
        data match {
          case a :: b :: next => (a, b, next)
          case other =>
            throw new IllegalStateException(
              s"The data stack does not have the required two elements. Stack: $data"
            )
        }
      def loop(data: List[Double], program: List[Op]): Double =
        program match {
          case head :: next =>
            head match {
              case Op.Lit(value) => loop(value :: data, next)
              case Op.Add =>
                val (a, b, rest) = pop2(data)
                loop((a + b) :: rest, next)
              case Op.Sub =>
                val (a, b, rest) = pop2(data)
                loop((a - b) :: rest, next)
              case Op.Mul =>
                val (a, b, rest) = pop2(data)
                loop((a * b) :: rest, next)
              case Op.Div =>
                val (a, b, rest) = pop2(data)
                loop((a / b) :: rest, next)
            }

          case Nil => data.head
        }

      loop(List.empty, program)
    }
  }
}
