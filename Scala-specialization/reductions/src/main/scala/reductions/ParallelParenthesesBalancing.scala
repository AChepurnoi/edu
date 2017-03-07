package reductions

import scala.annotation._
import org.scalameter._
import common._

object ParallelParenthesesBalancingRunner {

  @volatile var seqResult = false

  @volatile var parResult = false

  val standardConfig = config(
    Key.exec.minWarmupRuns -> 40,
    Key.exec.maxWarmupRuns -> 80,
    Key.exec.benchRuns -> 120,
    Key.verbose -> true
  ) withWarmer (new Warmer.Default)

  def main(args: Array[String]): Unit = {
    val length = 100000000
    val chars = new Array[Char](length)
    val threshold = 10000
    val seqtime = standardConfig measure {
      seqResult = ParallelParenthesesBalancing.balance(chars)
    }
    println(s"sequential result = $seqResult")
    println(s"sequential balancing time: $seqtime ms")

    val fjtime = standardConfig measure {
      parResult = ParallelParenthesesBalancing.parBalance(chars, threshold)
    }
    println(s"parallel result = $parResult")
    println(s"parallel balancing time: $fjtime ms")
    println(s"speedup: ${seqtime / fjtime}")
  }
}

object ParallelParenthesesBalancing {

  /** Returns `true` iff the parentheses in the input `chars` are balanced.
    */
  def balance(chars: Array[Char]): Boolean = {
    def helper(chars: Array[Char], acc: Int): Int = {
      if (chars.isEmpty || acc < 0) acc
      else if (chars.head == '(') helper(chars.tail, acc + 1)
      else if (chars.head == ')') helper(chars.tail, acc - 1)
      else helper(chars.tail, acc)
    }
    helper(chars, 0) == 0
  }

  /** Returns `true` iff the parentheses in the input `chars` are balanced.
    */
  def parBalance(chars: Array[Char], threshold: Int): Boolean = {

    def traverse(idx: Int, until: Int, arg1: Int, arg2: Int): (Int, Int) = {
      /*
       c = # of closing parenthese not matched
       o = # of opening parenthese not matched
       m = # of matching paris that has their opening parenthesis in the left subtree and their closing parenthesis in the right subtree
       */
      val m: Int = Math.min(idx, arg2)
      (idx + arg1 - m, until + arg1 - m)
    }

    def helper(in_chars: Array[Char], acc: (Int, Int)): (Int, Int) = {
      if (in_chars.isEmpty) acc
      else if (in_chars.head == '(') helper(in_chars.tail, (acc._1 + 1, acc._2))
      else if (in_chars.head == ')') {
        if (acc._1 > 0) helper(in_chars.tail, (acc._1 - 1, acc._2))
        else helper(in_chars.tail, (acc._1, acc._2 + 1))
      }
      else helper(in_chars.tail, acc)
    }

    def reduce(from: Int, until: Int): (Int, Int) = {
      if (until - from <= threshold) {
        helper(chars.slice(from, until), (0, 0))
      }
      else {
        val mid = from + (until - from) / 2
        val (l, r) = parallel(reduce(from, mid),
          reduce(mid, until))
        traverse(l._1, l._2, r._1, r._2)
      }
    }

    reduce(0, chars.length) ==(0, 0)
  }

  // For those who want more:
  // Prove that your reduction operator is associative!

}
