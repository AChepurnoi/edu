package calculator

sealed abstract class Expr

final case class Literal(v: Double) extends Expr

final case class Ref(name: String) extends Expr

final case class Plus(a: Expr, b: Expr) extends Expr

final case class Minus(a: Expr, b: Expr) extends Expr

final case class Times(a: Expr, b: Expr) extends Expr

final case class Divide(a: Expr, b: Expr) extends Expr

object Calculator {
  def computeValues(
                     namedExpressions: Map[String, Signal[Expr]]): Map[String, Signal[Double]] =
    namedExpressions.map { case (x, y) => (x, Signal(eval(y(), namedExpressions))) }
  


  def eval(expr: Expr, references: Map[String, Signal[Expr]]): Double =
    if (isCyclic(expr, references)) Double.NaN
    else expr match {
      case Literal(num) => num
      case Ref(x) => eval(getReferenceExpr(x, references), references)
      case Plus(a, b) => eval(a, references) + eval(b, references)
      case Minus(a, b) => eval(a, references) - eval(b, references)
      case Times(a, b) => eval(a, references) * eval(b, references)
      case Divide(a, b) => eval(a, references) / eval(b, references)
    }

  def isCyclic(expr: Expr, references: Map[String, Signal[Expr]]): Boolean = {
    def inner(expr: Expr, checked: List[String]): Boolean = expr match {
      case Literal(num) => if (num == Double.NaN) true else false
      case Ref(x) => if (checked contains x) true else inner(getReferenceExpr(x, references), x :: checked)
      case Plus(a, b) => inner(a, checked) || inner(b, checked)
      case Minus(a, b) => inner(a, checked) || inner(b, checked)
      case Times(a, b) => inner(a, checked) || inner(b, checked)
      case Divide(a, b) => inner(a, checked) || inner(b, checked)
    }
    inner(expr, List())
  }


  /** Get the Expr for a referenced variables.
    * If the variable is not known, returns a literal NaN.
    */
  private def getReferenceExpr(name: String,
                               references: Map[String, Signal[Expr]]) = {
    references.get(name).fold[Expr] {
      Literal(Double.NaN)
    } { exprSignal =>
      exprSignal()
    }
  }
}
