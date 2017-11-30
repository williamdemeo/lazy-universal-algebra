package random

import scala.collection.immutable.RedBlackTree.Tree


trait Generator[+T] {
  //self =>  // an alias for "this".

  def generate: T

  def map[S](f: T => S): Generator[S] = new Generator[S] {
    override def generate: S = f(Generator.this.generate)
  }

  def flatMap[S](f: T => Generator[S]): Generator[S] = new Generator[S] {
    override def generate: S = f(Generator.this.generate).generate
  }

}

object Generator {

  val intGen = new Generator[Int] {
    val rand = new java.util.Random
    override def generate = rand.nextInt()
  }
  val boolGen = for (x <- intGen) yield x > 0
  //  expands to: new Generator[Boolean] { def generate = integers.generate > 0 }

  //  Here are three (beta-)equivalent ways to build pair generators.
  def pairs_first_try[T, U](t: Generator[T], u: Generator[U]) =
    t flatMap(x => u map (y => (x, y)))

  def pairs_second_try[T, U](t: Generator[T], u: Generator[U]) =
    for { ti <- t; ui <- u } yield (ti, ui)

  /*   Given two generators tgen: Generator[T] and ugen: Generator[U],
   *   generate a pair (t, u): (T, U).
   */
  def pairs[T,U](tgen: Generator[T], ugen: Generator[U]) = new Generator[(T,U)] {
    override def generate: (T, U) = (tgen.generate, ugen.generate)
  }

  //   Always generate x
  def singleGen[T](x: T) = new Generator[T]{ override def generate: T = x  }

  // Generate a random Ints in the range [lo, hi]
  def chooseInt(lo: Int, hi: Int): Generator[Int] =
    for (x <- intGen) yield lo + x % (hi - lo)

  // Randomly choose one from a given list of values of type T.
  def oneOf[T](ts: T*): Generator[T] = for { i <- chooseInt(0, ts.length) } yield ts(i)

  // For example, to pick a random color from among red, green, blue,
  oneOf("red", "green", "blue")

  //---- List of random length with random Int entries.
  def intListGen: Generator[List[Int]] =
    for {
      isEmpty <- boolGen
      list <- if (isEmpty) emptyListGen else nonEmptyIntListGen
    } yield list

  def emptyListGen = singleGen(Nil)

  def nonEmptyIntListGen =
    for {
      head <- intGen
      tail <- intListGen
    } yield head :: tail

  //---- List of random length with random entries of type T.
  def listGen[T](t: Generator[T]): Generator[List[T]] =
    for {
      isEmpty <- boolGen
      list <- if (isEmpty) emptyListGen else nonEmptyListGen(t)
    } yield list

  def nonEmptyListGen[T](t: Generator[T]) : Generator[List[T]] =
    for {
      head <- t
      tail <- listGen[T](t)
    } yield head :: tail


  //---- Random Int Trees ----------------------
  trait IntTree

  case class IntNode(left: IntTree, t: Int, right: IntTree) extends IntTree
  case class IntLeaf(t: Int) extends IntTree

  def intTreeGen: Generator[IntTree] = for {
    isLeaf <- boolGen
    intTree <- if(isLeaf) intLeafGen else intNodeGen
  } yield intTree

  def intLeafGen : Generator[IntLeaf] = for { i <- intGen } yield IntLeaf(i)

  def intNodeGen: Generator[IntNode] = for {
    l <- intTreeGen
    v <- intGen
    r <- intTreeGen
  } yield IntNode(l, v, r)

  //---- Random Generic Trees ----------------------
  trait Tree[T]

  case class Node[T](left: Tree[T], t: T, right: Tree[T]) extends Tree[T]
  case class Leaf[T](t: T) extends Tree[T]

  def treeGen[T]: Generator[Tree[T]] = for {
    isLeaf <- boolGen
    tree <- if(isLeaf) leafGen[T] else nodeGen[T]
  } yield tree

  def leafGen[T]: Generator[Leaf[T]] = ???

  def nodeGen[T]: Generator[Node[T]] = ???




  //  val pairs = new Generator[(Int, Int)] {
//    override def generate: (Int, Int) = (integers.generate, integers.generate)
//  }

}
