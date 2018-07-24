package co.com.scalatraining.collections

import org.scalatest.FunSuite

import scala.collection.immutable.Stack

class StackSuite extends FunSuite {


  test("Creacion vacia") {
    val stack1: Stack[Int] = Stack()
    val stack2 = Stack.empty[Int]
    assert(stack1.isEmpty)
    assert(stack2.isEmpty)
    assert(stack1 === stack2)
  }

  test("foreach en un Stack") {
    val stack = Stack(1, 2, 3, 4, 5)
    assertResult(15) {
      var sum = 0
      stack.foreach(x =>
        sum += x
      )
      sum
    }
  }

  test("Adicionar un elemento a un stack") {
    val s1 = Stack(1, 2, 3, 4, 5)
    val s2 = Stack(2, 3, 4, 5, 6)

    val stack1 = s1 :+ 6
    val stack2 = 1 +: s2

    assert(stack1 === stack2)

  }

  test("Adicionar un elemento al comienzo de un stack") {
    val s1 = Stack(1, 2, 3, 4)
    val s2 = Stack(2, 3, 4)

    assert(s1 === s2.push(1))
  }

  test("primer elemento en un Stack") {
    val stack = Stack(1, 2, 3, 4, 5)
    assert(1 === stack.head)
  }

  test("head en un Stack vacio") {
    val stack = Stack.empty[Int]

    assertThrows[NoSuchElementException] {
      val x = stack.head
    }
  }

  test("split en un Stack") {
    val s1 = Stack(1, 2, 3, 4)
    val (s2, s3) = s1.splitAt(2)
    assert(s2 === Stack(1, 2) && s3 === Stack(3, 4))
  }


  test("drop en un Stack") {
    val stack = Stack(1, 2, 3, 4, 5)
    assertResult(Stack(3, 4, 5)) {
      stack.drop(2)
    }
  }

  test("dropRight en un Stack") {
    val stack = Stack(1, 2, 3, 4, 5)
    assertResult(Stack(1, 2, 3)) {
      stack.dropRight(2)
    }
  }

  test("Pop en un stack") {
    val stack = Stack(1, 2, 3, 4, 5)
    val (head, tail) = stack.pop2

    assert(head === 1)
    assert(tail === Stack(2, 3, 4, 5))
  }

  test("Map en un stack") {
    val stack = Stack(1, 2, 3, 4, 5)
    val sqrStack = stack.map(element => element * element)

    assert(sqrStack === Stack(1, 4, 9, 16, 25))
  }

  test("Reversar un Stack") {
    val stack = Stack(1, 2, 3, 4, 5)
    val revStack = stack.reverse

    assert(revStack === Stack(5, 4, 3, 2, 1))
  }

  test("filter en un Stack") {
    val stack = Stack(1, 2, 3, 4, 5, 6)
    assertResult(Stack(2, 4, 6)) {
      stack.filter( element => element % 2 == 0)
    }
  }
}
