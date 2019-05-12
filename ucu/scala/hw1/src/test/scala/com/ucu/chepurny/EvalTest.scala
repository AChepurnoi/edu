package com.ucu.chepurny

import org.scalatest.FlatSpec

import scala.util.parsing.combinator._

class EvalTest extends FlatSpec {


  "Eval" should "accept expr1" in {
    Eval.eval(BinExpr("-", Constant(Rationals(1, 2)), Constant(Rationals(1, 2)))) == Rationals(0, 2)
  }


  "Eval" should "accept 1/2 / 1/2" in {
    Eval.eval(BinExpr("/", Constant(Rationals(1, 2)), Constant(Rationals(1, 2)))) == Rationals(1, 1)
  }

  "Eval" should "accept (1/2 - 1/2) + 1/2" in {
    Eval.eval(BinExpr("+", BinExpr("-", Constant(Rationals(1, 2)), Constant(Rationals(1, 2))),
      Constant(Rationals(1, 2)))) == Rationals(1, 2)
  }

  "Eval" should "accept 1/2 * 1/2" in {
    Eval.eval(BinExpr("*", Constant(Rationals(1, 2)), Constant(Rationals(1, 2)))) == Rationals(1, 4)
  }


}
