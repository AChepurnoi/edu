package com.ucu.chepurny

import org.scalatest.FlatSpec

class RationalsTest extends FlatSpec {

  "Rationals" should "support add operation" in {
    assert(Rationals(1, 2) + Rationals(1, 2) == Rationals(2, 2))
  }

  it should "support subtraction operation" in {
    assert(Rationals(1, 2) - Rationals(1, 2) == Rationals(0, 2))
  }

  it should "support multiplication operation" in {
    assert(Rationals(2, 2) * Rationals(3, 2) == Rationals(6, 4))
  }

  it should "support div operation" in {
    assert(Rationals(2, 2) / Rationals(2, 1) == Rationals(1, 2))
  }

  it should "be optimized" in {
    assert(Rationals(4, 4) == Rationals(1, 1))
  }

  it should "have toString" in {
    assert(Rationals(4, 4).toString == "1/1")
  }


}
