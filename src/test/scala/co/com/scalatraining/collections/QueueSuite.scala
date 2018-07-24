package co.com.scalatraining.collections

import org.scalatest.FunSuite

import scala.collection.immutable.Queue

class QueueSuite extends FunSuite {

  test("Construccion de un Queue") {
    val queue = Queue(1, 2, 3)
    assert(queue.size == 3)
  }

  test("Adicion de un elemento a un Queue") {
    val queue = Queue.empty[Int]
    val queue2 = queue.enqueue(1)
    assert(queue == Queue.empty[Int])
    assert(queue2 == Queue(1))
  }

  test("Remover elemento de un Queue") {
    val queue = Queue(1, 2, 3, 4, 5)
    val (elem, tail) = queue.dequeue

    assert(elem === 1)
    assert(tail === Queue(2, 3, 4, 5))
  }

  test("Adicion de una cola a otra") {
    val q1 = Queue(1, 2, 3)
    val q2 = Queue(4, 5, 6)
    val queue = q1.enqueue(q2)
    assert(queue == Queue(1, 2, 3, 4, 5, 6))
  }

  test("Adicionar y remover elementos de un queue en forma fifo") {
    val queue = Queue(1, 2)
    val q1 = queue.enqueue(3)

    val (el1, q2) = q1.dequeue
    assert(el1 === 1)
    assert(q2 === Queue(2, 3))

    val (el2, q3) = q2.dequeue
    assert(el2 === 2)
    assert(q3 === Queue(3))

    val (el3, q4) = q3.dequeue
    assert(el3 === 3)
    assert(q4 === Queue.empty[Int])
  }

  test("map en un queue") {
    val queue = Queue("1", "2", "3")
    val queue2 = queue.map(dato => dato + "prueba")
    assert(queue != queue2)
  }

  test("split en un queue") {
    val queue = Queue(1, 2, 3, 4)
    val (queue2, queue3) = queue.splitAt(2)
    assert(queue2 == Queue(1, 2) && queue3 == Queue(3, 4))
  }

  test("drop en queue") {
    val queue = Queue(1, 2, 3, 4)
    assertResult(Queue(3, 4)) {
      queue.drop(2)
    }
  }

  test("dropRight queue") {
    val queue = Queue(1, 2, 3, 4)
    assertResult(Queue(1, 2)) {
      queue.dropRight(2)
    }
  }


  test("filter en queue") {
    val queue = Queue(1, 2, 3, 4)
    assertResult(Queue(2, 4)) {
      queue.filter(x =>
        x % 2 == 0
      )
    }
  }

  test("foreach en queue") {
    val queue = Queue(1, 2, 3, 4)
    assertResult(10) {
      var sum = 0
      queue.foreach((x) =>
        sum += x
      )
      sum
    }
  }

}