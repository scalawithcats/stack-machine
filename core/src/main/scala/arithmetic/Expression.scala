package arithmetic

trait Expression[E <: Expression[E]] {
  def +(that: E): E
  def *(that: E): E
  def -(that: E): E
  def /(that: E): E

  def eval: Double
}
trait ExpressionConstructors[E <: Expression[E]] {
  def literal(value: Double): E
}
