package co.com.scalatraining.effects

import java.util.concurrent.Executors

import org.scalatest.FunSuite

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.language.postfixOps
import scala.util.{Failure, Success}

class FutureSuite extends FunSuite {

  test("Un futuro se puede crear") {

    val hiloPpal = Thread.currentThread().getName

    var hiloFuture = ""

    println(s"Test 1 - El hilo ppal es ${hiloPpal}")

    val saludo: Future[String] = Future {
      hiloFuture = Thread.currentThread().getName
      println(s"Test 1 - El hilo del future es ${hiloFuture}")

      Thread.sleep(500)
      "Hola"
    }
    val resultado: String = Await.result(saludo, 10 seconds)
    assert(resultado == "Hola")
    assert(hiloPpal != hiloFuture)
  }

  test("map en Future") {

    val t1 = Thread.currentThread().getName
    println(s"Test 2 - El hilo del ppal es ${t1}")

    val saludo = Future {
      val t2 = Thread.currentThread().getName
      println(s"Test 2 - El hilo del future (saludo) es ${t2}")

      Thread.sleep(500)
      "Hola"
    }

    val saludo2 = Future {
      println(s"Test 2 - Hilo normal (saludo2) ${Thread.currentThread().getName}")
    }

    val saludoCompleto = saludo.map(mensaje => {
      val t3 = Thread.currentThread().getName
      println(s"Test 2 - El hilo del map (saludoCompleto) es ${t3}")

      mensaje + " muchachos"
    })

    val resultado = Await.result(saludoCompleto, 10 seconds)
    assert(resultado == "Hola muchachos")
  }

  test("Se debe poder encadenar Future con for-comp") {

    val f1 = Future {
      println(s"f1, el hilo es ${Thread.currentThread().getName}")
      Thread.sleep(200)
      1
    }

    val f2 = Future {
      println(s"f2, el hilo es ${Thread.currentThread().getName}")
      Thread.sleep(200)
      2
    }

    val f3: Future[Int] = {
      println(s"for-comp, el hilo es ${Thread.currentThread().getName}")
      for {
        res1 <- f1
        res2 <- f2
      } yield res1 + res2
    }

    val res = Await.result(f3, 10 seconds)

    assert(res == 3)

  }

  test("Se debe poder encadenar Future con for-comp Fallido") {

    val f1 = Future {
      Thread.sleep(200)
      1
    }

    val f2 = Future {
      Thread.sleep(200)
      2 / 0
    }

    val f3: Future[Int] = for {
      res1 <- f1
      res2 <- f2
    } yield res1 + res2

    assertThrows[ArithmeticException] {
      Await.result(f3, 10 seconds)
    }
  }

  test("Se debe poder encadenar Future con for-comp Fallido y recuperado") {

    val f1 = Future {
      Thread.sleep(200)
      1
    }

    val f2 = Future {
      Thread.sleep(200)
      2 / 0
    }

    val f3: Future[Int] = for {
      res1 <- f1
      res2 <- f2.recover { case _: Exception => 2 }
    } yield res1 + res2


    val res = Await.result(f3, 10 seconds)

    assert(res === 3)
  }

  test("Se debe poder manejar el error de un Future de forma imperativa") {
    val divisionCero = Future {
      Thread.sleep(100)
      10 / 0
    }
    var error = false

    val r: Unit = divisionCero.onFailure {
      case e: Exception => error = true
    }


    Thread.sleep(1000)

    assert(error == true)
  }

  test("Se debe poder manejar el exito de un Future de forma imperativa") {

    val division = Future(5)

    var r = 0

    val f: Unit = division.onComplete {
      case Success(res) => r = res
      case Failure(e) => r = 1
    }

    Thread.sleep(150)

    val res = Await.result(division, 10 seconds)

    assert(r == 5)
  }

  test("Se debe poder manejar el error de un Future de forma funcional sincronicamente") {

    var threadName1 = ""
    var threadName2 = ""

    val divisionPorCero = Future {
      threadName1 = Thread.currentThread().getName
      Thread.sleep(100)
      10 / 0
    }.recover {
      case e: ArithmeticException => {
        threadName2 = Thread.currentThread().getName
        "No es posible dividir por cero"
      }
    }

    val res = Await.result(divisionPorCero, 10 seconds)

    assert(threadName1 == threadName2)
    assert(res == "No es posible dividir por cero")

  }

  test("Se debe poder manejar el error de un Future de forma funcional asincronamente") {

    var threadName1 = ""
    var threadName2 = ""

    implicit val ecParaPrimerHilo = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))

    val f1 = Future {
      threadName1 = Thread.currentThread().getName
      2 / 0
    }(ecParaPrimerHilo)
      .recoverWith {
        case e: ArithmeticException => {
          //implicit val ecParaRecuperacion = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))
          Future {
            threadName2 = Thread.currentThread().getName
            1
          } //(ecParaRecuperacion)
        }
      }

    val res = Await.result(f1, 10 seconds)

    println(s"Test en recoverWith thread del fallo: $threadName1")
    println(s"Test en recoverWith thread de recuperacion: $threadName2")

    assert(threadName1 != threadName2)
    assert(res == 1)
  }

  test("Los future **iniciados** fuera de un for-comp deben iniciar al mismo tiempo") {

    val timeForf1 = 100
    val timeForf2 = 200
    val timeForf3 = 100

    val additionalTime = 50D

    val estimatedElapsed = (Math.max(Math.max(timeForf1, timeForf2), timeForf3) + additionalTime) / 1000

    val f1 = Future {
      Thread.sleep(timeForf1)
      1
    }
    val f2 = Future {
      Thread.sleep(timeForf2)
      2
    }
    val f3 = Future {
      Thread.sleep(timeForf3)
      3
    }

    val t1: Long = System.nanoTime()

    val resultado = for {
      a <- f1
      b <- f2
      c <- f3
    } yield (a + b + c)

    val res = Await.result(resultado, 10 seconds)
    val elapsed = (System.nanoTime() - t1) / 1.0E09

    println(s"Future **iniciados** fuera del for-comp estimado: $estimatedElapsed real: $elapsed")
    assert(elapsed <= estimatedElapsed)
    assert(res == 6)

  }

  test("Los future **definidos** fuera de un for-comp deben iniciar secuencialmente") {

    val timeForf1 = 100
    val timeForf2 = 300
    val timeForf3 = 500

    val estimatedElapsed: Double = (timeForf1 + timeForf2 + timeForf3) / 1000

    def f1 = Future {
      Thread.sleep(timeForf1)
      1
    }

    def f2 = Future {
      Thread.sleep(timeForf2)
      2
    }

    def f3 = Future {
      Thread.sleep(timeForf3)
      3
    }

    val t1 = System.nanoTime()

    val resultado = for {
      a <- f1
      b <- f2
      c <- f3
    } yield (a + b + c)

    val res = Await.result(resultado, 10 seconds)
    val elapsed = (System.nanoTime() - t1) / 1.0E09

    println(s"Future **definidos** fuera del for-comp estimado: $estimatedElapsed real: $elapsed")

    assert(elapsed >= estimatedElapsed)
    assert(res == 6)

  }

  test("Los future declarados dentro de un for-comp deben iniciar secuencialmente") {

    val t1 = System.nanoTime()

    val timeForf1 = 100
    val timeForf2 = 100
    val timeForf3 = 100

    val estimatedElapsed = (timeForf1 + timeForf2 + timeForf3) / 1000

    val resultado = for {
      a <- Future {
        Thread.sleep(timeForf1)
        1
      }
      b <- Future {
        Thread.sleep(timeForf2)
        2
      }
      c <- Future {
        Thread.sleep(timeForf3)
        3
      }
    } yield (a + b + c)

    val res = Await.result(resultado, 10 seconds)
    val elapsed = (System.nanoTime() - t1) / 1.0E09

    assert(elapsed >= estimatedElapsed)
    assert(res == 6)
  }

  test("Future.sequence") {

    val listOfFutures: List[Future[Int]] = Range(1, 11).map(Future.successful).toList

    val resSequence: Future[List[Int]] = Future.sequence(listOfFutures)

    val resFuture = resSequence.map(l => l.sum / l.size)

    val res = Await.result(resFuture, 10 seconds)

    assert(res == Range(1, 11).sum / Range(1, 11).size)

  }

  test("Future.traverse") {
    def foo(i: Seq[Int]): Future[Int] = Future.successful(i.sum / i.size)

    val seq: Seq[Future[Int]] = Range(1, 11).map(Future.successful)

    val resFutureTrav: Future[Int] = Future.traverse(seq)(x => x.map(_ + 1)).flatMap(foo)


    val res: Int = Await.result(resFutureTrav, 10 seconds)


    assert(res === Range(2, 12).sum / Range(2, 12).size)
  }

  test("Servicio de clima") {
    val ecServClima = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(5))
    val ecGuardarBD = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(10))

    def servClima(int: Int): Future[String] = Future {
      println(s"Servicio del clima # ${int} con el hilo ${Thread.currentThread().getName}")
      Thread.sleep(500)
      s"Soleado en Medellin: ${int}"
    }(ecServClima)


    def serviceDB(clima: String): Future[Boolean] = Future {
      println(s"El hilo: ${Thread.currentThread().getName} está guardando en BD: ${clima}")
      Thread.sleep(100)
      true
    }(ecGuardarBD)

    val a: Seq[Future[Boolean]] = Range(1, 20).map { int => servClima(int).flatMap(str => serviceDB(str)) }

    val res = Await.result(Future.sequence(a), 10 seconds)

    assert(res.size === 19)
  }

}