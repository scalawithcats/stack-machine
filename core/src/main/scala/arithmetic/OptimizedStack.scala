/*
 * Copyright 2023 Scala with Cats
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package arithmetic

object OptimizedStack {

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
    val machine = new StackMachine(program.toArray)

    def eval: Double = machine.eval
  }

  final class StackMachine(program: Array[Op]) {
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
