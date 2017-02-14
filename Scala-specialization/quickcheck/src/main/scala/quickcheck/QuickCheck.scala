package quickcheck


import common._
import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._
import Math._

abstract class QuickCheckHeap extends Properties("Heap") with IntHeap {

  property("gen1") = forAll { (h: H) =>
    val m = if (isEmpty(h)) 0 else findMin(h)
    findMin(insert(m, h)) == m
  }


  property("min1") = forAll { a: Int =>
    val h = insert(a, empty)
    findMin(h) == a
  }

  property("minimum of 2") = forAll { (a: Int, b: Int) =>
    val h = insert(a, insert(b, empty))
    findMin(h) == a.min(b)
  }

  property("minimum of 3") = forAll { (a: Int, b: Int, c: Int) =>
    val h = insert(a, insert(b, insert(c, empty)))
    findMin(h) == a.min(b.min(c))
  }

  property("is empty for empty") = forAll { a: Int =>
    isEmpty(empty)
  }

  property("is not empty for inserted") = forAll { a: Int =>
    !isEmpty(insert(a, empty))
  }

  property("deleteMin of single element heap") = forAll { a: Int =>
    val h = insert(a, empty)
    deleteMin(h) == empty
  }

  property("deleteMin of two element heap") = forAll { (a: Int, b: Int) =>
    val h = insert(a, insert(b, empty))
    deleteMin(h) == insert(a.max(b), empty)
  }

  property("gen insert minimum") = forAll { h: H =>
    val m = if (isEmpty(h)) 0 else findMin(h)
    findMin(insert(m, h)) == m
  }

  property("meld 2 heaps minimum") = forAll { (h1: H, h2: H) =>
    val min = findMin(h1) min findMin(h2)
    findMin(meld(h1, h2)) == min
  }

  property("find min of 2 arbitrary heaps") = forAll { (h1: H, h2: H) =>
    val min = findMin(meld(h1, h2))
    isEmpty(h1) || isEmpty(h2) || min == findMin(h1) || min == findMin(h2)
  }

  property("finding and deleting minimums of arbitrary heap") = forAll { h: H =>
    def reduce(h: H): List[A] =
      if (!isEmpty(h)) findMin(h) :: reduce(deleteMin(h))
      else Nil
    reduce(h) == reduce(h).sorted
  }

  property("delete min and insert it again") = forAll { h: H =>
    val reinserted = insert(findMin(h), deleteMin(h))
    findMin(reinserted) == findMin(h)
  }

  property("transferring the minimum to another heap") = forAll { (h1: H, h2: H) =>
    def reduce(h: H): List[A] =
      if (!isEmpty(h)) findMin(h) :: reduce(deleteMin(h))
      else Nil
    val melded = meld(h1, h2)
    val transferred = meld(deleteMin(h1), insert(findMin(h1), h2))
    reduce(melded) == reduce(transferred)
  }


  lazy val genHeap: Gen[H] = for {
    x <- arbitrary[A]
    h <- oneOf(Gen.const(empty), genHeap)
  } yield insert(x, h)

  implicit lazy val arbHeap: Arbitrary[H] = Arbitrary(genHeap)


}
