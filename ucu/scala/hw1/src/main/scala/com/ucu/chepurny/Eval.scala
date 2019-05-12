package com.ucu.chepurny

import scala.collection.immutable.{Queue, Stack}
import scala.util.parsing.combinator._

trait Expr

case class BinExpr(o: String, l: Expr, r: Expr) extends Expr

case class Constant(r: Rational) extends Expr

case class Inv(e: Expr) extends Expr

object Eval {
  //  Define an eval(e:Expr):Rational function computing the value of any
  //  expression.
  def eval(e: Expr): Rational = {
    e match {
      case BinExpr(o, l, r) => o match {
        case "+" => eval(l) + eval(r)
        case "-" => eval(l) - eval(r)
        case "/" => eval(l) / eval(r)
        case "*" => eval(l) * eval(r)
      }
      case Constant(e) => e
      case Inv(e) => -eval(e)
    }
  }

  def main(args: Array[String]): Unit = {
    val result: Rational = eval(BinExpr("+", Constant(new Rational(18, 27)), Inv(Constant(new Rational(1, 2)))))
    println(result)

  }
}