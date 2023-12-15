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

object DirectThreaded3 {

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
      val machine = new StackMachine()
      def loop(expr: Expression): List[machine.Op] =
        expr match {
          case Literal(value) => List(machine.Lit(value))
          case Addition(left, right) =>
            loop(left) ++ loop(right) ++ List(machine.Add)
          case Subtraction(left, right) =>
            loop(left) ++ loop(right) ++ List(machine.Sub)
          case Multiplication(left, right) =>
            loop(left) ++ loop(right) ++ List(machine.Mul)
          case Division(left, right) =>
            loop(left) ++ loop(right) ++ List(machine.Div)
        }

      Program(machine)(loop(this).toArray)
    }

    def eval: Double = compile.eval
  }
  object Expression extends arithmetic.ExpressionConstructors[Expression] {
    def literal(value: Double): Expression = Literal(value)
  }

  final case class Program(machine: StackMachine)(program: Array[machine.Op]) {
    def eval: Double = machine.eval(program)
  }

  final class StackMachine() {
    // The data stack
    private val stack: Array[Double] = Array.ofDim[Double](256)
    // sp points to first free element on the stack
    // stack(sp - 1) is the first element
    private var sp: Int = 0
    // ip points to the current instruction
    private var ip: Int = 0

    sealed abstract class Op extends Function0[Unit]
    final case class Lit(value: Double) extends Op {
      def apply(): Unit = {
        stack(sp) = value
        sp = sp + 1
        ip = ip + 1
      }
    }
    case object Add extends Op {
      def apply(): Unit = {
        val a = stack(sp - 1)
        val b = stack(sp - 2)
        stack(sp - 2) = (a + b)
        sp = sp - 1
        ip = ip + 1
      }
    }
    case object Sub extends Op {
      def apply(): Unit = {
        val a = stack(sp - 1)
        val b = stack(sp - 2)
        stack(sp - 2) = (a - b)
        sp = sp - 1
        ip = ip + 1
      }

    }
    case object Mul extends Op {
      def apply(): Unit = {
        val a = stack(sp - 1)
        val b = stack(sp - 2)
        stack(sp - 2) = (a * b)
        sp = sp - 1
        ip = ip + 1
      }
    }
    case object Div extends Op {
      def apply(): Unit = {
        val a = stack(sp - 1)
        val b = stack(sp - 2)
        stack(sp - 2) = (a / b)
        sp = sp - 1
        ip = ip + 1

      }
    }

    final def eval(program: Array[Op]): Double = {
      def trampoline(): Double =
        if ip == program.size then stack(sp - 1)
        else {
          program(ip).apply()
          trampoline()
        }

      trampoline()
    }
  }
}
