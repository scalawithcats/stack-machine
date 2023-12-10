package arithmetic

// All optimizations:
//
// - Algebraic simplification
// - Super instructions
// - Stack caching
// - Bytecode interpreter
object All {

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
      // We store literals in the data array, and program bytecodes in the
      // program array. As there is no control flow in a program, the order in
      // which literals are written in the data array is the order in which the
      // program reads them out.
      //
      // Separating program and data leads to substantial performance
      // improvements. An earlier iteration, that used a ByteBuffer and stored
      // program and data together, had performance under half of that reported
      // here.
      val program = Array.ofDim[Byte](16777216)
      val data = Array.ofDim[Double](16777216)

      // Program pointer. First free element
      var pp = 0
      // Data pointer. First free element
      var dp = 0
      def loop(expr: Expression): Unit =
        expr match {
          case Addition(Literal(0.0), expr) =>
            loop(expr)
          case Addition(expr, Literal(0.0)) =>
            loop(expr)
          case Addition(Literal(1.0), expr) =>
            loop(expr)
            program(pp) = Op.AddOne
            pp = pp + 1
          case Addition(expr, Literal(1.0)) =>
            loop(expr)
            program(pp) = Op.AddOne
            pp = pp + 1
          case Addition(Literal(v), expr) =>
            loop(expr)
            program(pp) = Op.AddLit
            data(dp) = v
            pp = pp + 1
            dp = dp + 1
          case Addition(expr, Literal(v)) =>
            loop(expr)
            program(pp) = Op.AddLit
            data(dp) = v
            pp = pp + 1
            dp = dp + 1
          case Literal(value) =>
            program(pp) = Op.Lit
            data(dp) = value
            pp = pp + 1
            dp = dp + 1
          case Addition(left, right) =>
            loop(left)
            loop(right)
            program(pp) = Op.Add
            pp = pp + 1
          case Subtraction(left, right) =>
            loop(left)
            loop(right)
            program(pp) = Op.Sub
            pp = pp + 1
          case Multiplication(left, right) =>
            loop(left)
            loop(right)
            program(pp) = Op.Mul
            pp = pp + 1
          case Division(left, right) =>
            loop(left)
            loop(right)
            program(pp) = Op.Div
            pp = pp + 1
        }

      loop(this)
      // Shrink to size
      val p = IArray.unsafeFromArray(program.slice(0, pp))
      val d = IArray.unsafeFromArray(data.slice(0, dp))
      Program(p, d)
    }

    def eval: Double = compile.eval
  }
  object Expression extends arithmetic.ExpressionConstructors[Expression] {
    def literal(value: Double): Expression = Literal(value)
  }

  object Op {
    val Lit: Byte = 0
    val Add: Byte = 1
    val Sub: Byte = 2
    val Mul: Byte = 3
    val Div: Byte = 4
    // Super instructions
    val AddLit: Byte = 5
    val AddOne: Byte = 6
  }

  final case class Program(program: IArray[Byte], data: IArray[Double]) {
    val machine = new StackMachine(program, data)

    def eval: Double = machine.eval
  }

  final case class StackMachine(program: IArray[Byte], data: IArray[Double]) {
    // The data stack
    private val stack: Array[Double] = Array.ofDim[Double](256)

    final def eval: Double = {
      // sp points to first free element on the stack
      // stack(sp - 1) is the first element
      //
      // dp points to data
      //
      // top is top of the stack
      def loop(sp: Int, dp: Int, pc: Int, top: Double): Double =
        if (pc == program.size) top
        else
          program(pc) match {
            case Op.Lit =>
              stack(sp) = top
              loop(sp + 1, dp + 1, pc + 1, data(dp))
            case Op.Add =>
              val a = stack(sp - 1)
              loop(sp - 1, dp, pc + 1, top + a)
            case Op.AddLit =>
              val a = data(dp)
              loop(sp, dp + 1, pc + 1, top + a)
            case Op.AddOne =>
              loop(sp, dp, pc + 1, top + 1)
            case Op.Sub =>
              val a = stack(sp - 1)
              loop(sp - 1, dp, pc + 1, top - a)
            case Op.Mul =>
              val a = stack(sp - 1)
              loop(sp - 1, dp, pc + 1, top * a)
            case Op.Div =>
              val a = stack(sp - 1)
              loop(sp - 1, dp, pc + 1, top / a)
          }

      loop(0, 0, 0, 0.0)
    }
  }
}
