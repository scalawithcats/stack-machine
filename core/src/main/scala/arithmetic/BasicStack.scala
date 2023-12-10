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

object BasicStack {

  enum Expression extends arithmetic.Expression[Expression] {
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

    case Literal(value: Double)
    case Addition(left: Expression, right: Expression)
    case Subtraction(left: Expression, right: Expression)
    case Multiplication(left: Expression, right: Expression)
    case Division(left: Expression, right: Expression)
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

  final case class StackMachine(program: List[Op]) {
    def eval: Double = {
      def pop(stack: List[Double]): (Double, List[Double]) =
        stack match {
          case head :: next => (head, next)
          case Nil =>
            throw new IllegalStateException(
              s"The data stack does not have any elements."
            )
        }

      def push(value: Double, stack: List[Double]): List[Double] =
        value :: stack

      def loop(stack: List[Double], program: List[Op]): Double =
        program match {
          case head :: next =>
            head match {
              case Op.Lit(value) => loop(push(value, stack), next)
              case Op.Add =>
                val (a, s1) = pop(stack)
                val (b, s2) = pop(s1)
                val s = push(a + b, s2)
                loop(s, next)
              case Op.Sub =>
                val (a, s1) = pop(stack)
                val (b, s2) = pop(s1)
                val s = push(a + b, s2)
                loop(s, next)
              case Op.Mul =>
                val (a, s1) = pop(stack)
                val (b, s2) = pop(s1)
                val s = push(a + b, s2)
                loop(s, next)
              case Op.Div =>
                val (a, s1) = pop(stack)
                val (b, s2) = pop(s1)
                val s = push(a + b, s2)
                loop(s, next)
            }

          case Nil => stack.head
        }

      loop(List.empty, program)
    }
  }
}
