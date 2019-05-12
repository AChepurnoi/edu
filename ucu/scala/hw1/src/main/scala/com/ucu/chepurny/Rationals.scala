package com.ucu.chepurny


class Rational(n: Int, d: Int) {

  val num: Int = n / gcd(n, d)
  val den: Int = validate(d / gcd(n, d))

  def isNull: Boolean = num == 0

  def add(r: Rational): Rational = new Rational(num * r.den + r.num * den, r.den * den)

  override def toString: String = s"${num}/${den}"

  private def validate(den: Int): Int = if (den == 0) throw new RuntimeException("Den should not be 0") else den

  override def equals(obj: Any): Boolean = {
    obj match {
      case r: Rational => r.num * den == num * r.den
      case _ => false
    }
  }

  private def gcd(a: Int, b: Int): Int = {
    if (b == 0) a else gcd(b, a % b)
  }

  def sub(r: Rational): Rational = new Rational(num * r.den - r.num * den, r.den * den)

  def mul(r: Rational): Rational = new Rational(num * r.num, den * r.den)

  def div(r: Rational): Rational = new Rational(num * r.den, den * r.num)


  def toDouble: Double = num / den

  def +(that: Rational): Rational = add(that)

  def -(that: Rational): Rational = sub(that)

  def ==(that: Rational): Boolean = equals(that)

  def /(that: Rational): Rational = div(that)

  def *(that: Rational): Rational = mul(that)

  def <(that: Rational): Boolean = num * that.den < that.num * den

  def >(that: Rational): Boolean = num * that.den > that.num * den

  def unary_- : Rational = new Rational(-num, den)
}


//  Ex: Complete the Rational class with an add(r:Rational):Rational
//  function.

//  Ex: Redefine the toString method of the Rational class.

//  Ex: Redefine equals method of the Rational class.

//  Ex: Add sub, div, mul methods

//  Ex: Make sure that rational numbers that are created are optimized e.g. 6/12 is optimized into 1/2.
//      Hint: use gcd function we've seen in lectures.

//  Ex: Define toDouble method to return real approximate value of the number.

//  Ex: Operators: <, >, ==, /, *, +, -. Glance through https://docs.scala-lang.org/tour/operators.html.
//      Remember you can operate with functions as values to define an 'alias' operators for already defined methods.
//      To get a function value from a method, you can use '_' syntax, e.g. `isNull _`

//  Ex (Optional): Define companion object Rational with apply as alternative constructor for rationals.

//  Ex: Write a few tests using ScalaTest framework covering basic use cases.

object Rationals extends App {
  def apply(num: Int, den: Int) = new Rational(num, den)
}