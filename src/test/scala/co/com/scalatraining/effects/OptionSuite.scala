package co.com.scalatraining.effects

import org.scalatest.FunSuite

import scala.collection.immutable.Seq

class OptionSuite extends FunSuite {

  test("Se debe poder crear un Option con valor") {
    val s = Option {
      1
    }
    assert(s == Some(1))
  }

  test("Se debe poder crear un Option con Some") {
    val s = Some {
      1
    }
    assert(s == Some(1))
  }

  test("Test some de null") {
    val s = Some {
      null
    }
    assert(s !== None)
  }

  test("Test Option de null") {
    val s = Option {
      null
    }
    assert(s == None)
  }

  test("Se debe poder crear un Option para denotar que no hay valor") {
    val s = None
    assert(s == None)
  }

  test("Es inseguro acceder al valor de un Option con get") {
    val s = None
    assertThrows[NoSuchElementException] {
      val r = s.get
    }
  }

  test("Se debe poder hacer pattern match sobre un Option") {
    val lista: Seq[Option[String]] = List(Some("Andres"), None, Some("Luis"), Some("Pedro"))
    val nombre: Option[String] = lista(1)
    var res = ""
    res = nombre match {
      case Some(nom) => nom
      case None => "NONAME"
    }
    assert(res == "NONAME")
  }

  test("Fold en Option") {
    val o = Option(1)

    val res: Int = o.fold(10)(x => x + 20)

    assert(res == 21)
  }

  test("Fold en Option None") {
    val o: Option[Int] = None

    val res: Int = o.fold(10)(x => x + 20)

    assert(res == 10)
  }


  test("Fold en Option None con funcion") {

    def foo(int: Int): Option[Int] = {
      if (int % 2 == 0) Some(int)
      else None
    }

    val impar: Option[Int] = foo(3)
    val par: Option[Int] = foo(2)

    val resImp: Int = impar.fold(10)(x => x + 20)
    val resPar: Int = par.fold(10)(x => x + 20)

    assert(resImp == 10)
    assert(resPar == 22)
  }


  test("Se debe poder saber si un Option tiene valor con isDefined") {
    val lista = List(Some("Andres"), None, Some("Luis"), Some("Pedro"))
    val nombre = lista(0)
    assert(nombre.isDefined)
  }

  test("Se debe poder acceder al valor de un Option de forma segura con getOrElse") {
    val lista = List(Some("Andres"), None, Some("Luis"), Some("Pedro"))
    val nombre = lista(1)
    val res = nombre.getOrElse("NONAME")
    assert(res == "NONAME")
  }

  test("getOrElse implementando un fold") {
    val lista = List(Some("Andres"), None, Some("Luis"), Some("Pedro"))
    val nombre = lista(1)
    val resNone = nombre.fold("NONAME")(identity)
    val resSome = lista(0).fold("NONAME")(identity)
    assert(resNone == "NONAME")
    assert(resSome == "Andres")
  }


  test("Un Option se debe poder transformar con un map") {
    val lista = List(Some("Andres"), None, Some("Luis"), Some("Pedro"))
    val nombre = lista(0)
    val nombreCompleto: Option[String] = nombre.map(s => s + " Felipe")
    assert(nombreCompleto.getOrElse("NONAME") == "Andres Felipe")
  }

  test("Un Option se debe poder transformar con flatMap en otro Option") {
    val lista = List(Some("Andres"), None, Some("Luis"), Some("Pedro"))
    val nombre = lista(0)

    val resultado: Option[String] = nombre.flatMap(s => Option(s.toUpperCase))
    resultado.map(s => assert(s == "ANDRES"))
  }

  test("Un Option se debe poder filtrar con una hof con filter") {
    val lista = List(Some(5), None, Some(40), Some(20))
    val option0 = lista(0)
    val option1 = lista(1)
    val res0 = option0.filter(_ > 10)
    val res1 = option1.filter(_ > 10)
    val res2 = option0.filter(_ < 10)

    assert(res0 == None)
    assert(res1 == None)
    assert(res2 == Some(5))
  }

  test("for comprehensions en Option") {
    val lista = List(Some(5), None, Some(40), Some(20))
    val s1 = lista(0)
    val s2 = lista(2)

    val resultado = for {
      x <- s1
      y <- s2
    } yield x + y

    assert(resultado == Some(45))
  }

  test("for comprehensions en Option mod") {
    val lista = List(Some(5), None, Some(40), Some(20))
    val s1 = lista(0)
    val s2 = lista(2)
    val s3 = lista(3)

    val resultado = for {
      x <- s1
      y <- s2
      z <- s3
    } yield x + y + z

    assert(resultado == Some(65))
  }


  test("for comprehensions con flat maps") {
    val o1 = Some(1)
    val o2 = Some(2)
    val o3 = Some(3)

    val res = o1.flatMap { x =>
      o2.flatMap { y =>
        o3.flatMap { z =>
          Option(x + y + z)
        }
      }
    }

    assert(res == Some(6))
  }

  test("Prueba for comprenhension con none") {

    def foo(int: Int): Option[Int] = {
      println(s"Ejecutando foo con ${int}")
      Some(int)
    }

    def bar(int: Int): Option[Int] = {
      println(s"Ejecutando bar con ${int}")
      None
    }

    val res = for {
      r1 <- foo(1)
      r2 <- foo(2)
      r3 <- foo(3)
      r4 <- foo(4)
      r5 <- foo(5)
      r6 <- foo(6)
      r7 <- foo(7)
      r8 <- foo(8)
      r9 <- foo(9)
      r10 <- bar(10)
    } yield r1 + r2 + r3 + r4 + r5 + r6

    assert(res == None)

  }

  test("for comprehesions None en Option") {
    val consultarNombre = Some("Andres")
    val consultarApellido = Some("Estrada")
    val consultarEdad = None
    val consultarSexo = Some("M")

    val resultado = for {
      nom <- consultarNombre
      ape <- consultarApellido
      eda <- consultarEdad
      sex <- consultarSexo
      //} yield (nom+","+ape+","+eda+","+sex)
    } yield (s"$nom $ape, $eda,$sex")

    assert(resultado == None)
  }

  test("for comprehesions None en Option 2") {

    def consultarNombre(dni: String): Option[String] = Some("Felix")

    def consultarApellido(dni: String): Option[String] = Some("Vergara")

    def consultarEdad(dni: String): Option[String] = None

    def consultarSexo(dni: String): Option[String] = Some("M")

    val dni = "8027133"
    val resultado = for {
      nom <- consultarNombre(dni)
      ape <- consultarApellido(dni)
      eda <- consultarEdad(dni)
      sex <- consultarSexo(dni)
      //} yield (nom+","+ape+","+eda+","+sex)
    } yield (s"$nom $ape, $eda,$sex")

    assert(resultado == None)
  }


  /**
    *
    * Test para alternativas a pattern match para Options
    *
    */


  val some: Option[Int] = Some(2)
  val none: Option[Int] = None

  test("flatMap") {

    def foo(int: Int): Option[Int] = {
      if (int % 2 == 0) Some(int)
      else None
    }

    def pm(option: Option[Int]): Option[Int] = option match {
      case None => None
      case Some(x) => foo(x)
    }

    assert(pm(some) === some.flatMap(foo))
    assert(pm(none) === none.flatMap(foo))
  }


  test("flatten") {

    val option: Option[Option[Int]] = Option(Option(1))
    val none: Option[Option[Int]] = None

    def pm(option: Option[Option[Int]]): Option[Int] = option match {
      case None => None
      case Some(x) => x
    }

    assert(pm(option) === option.flatten)
    assert(pm(none) === none.flatten)
  }


  test("map") {

    def foo(int: Int): Option[Int] = {
      if (int % 2 == 0) Some(int)
      else None
    }

    def pm(option: Option[Int]): Option[Option[Int]] = option match {
      case None => None
      case Some(x) => Some(foo(x))
    }

    assert(pm(some) === some.map(foo))
    assert(pm(none) === none.map(foo))
  }


  test("forEach") {

    var somePm = 0
    var someFe = 0
    var nonePm = 0
    var noneFe = 0

    some match {
      case None => {}
      case Some(x) => somePm = x + 1
    }

    some.foreach(x => someFe = x + 1)

    none match {
      case None => {}
      case Some(x) => nonePm = x + 1
    }

    none.foreach(x => noneFe = x + 1)

    assert(somePm === someFe)
    assert(nonePm === noneFe)
  }


  test("isDefined") {
    def pm(option: Option[Int]): Boolean = option match {
      case None => false
      case Some(_) => true
    }

    assert(pm(some) === some.isDefined)
    assert(pm(none) === none.isDefined)
  }

  test("isEmpty") {
    def pm(option: Option[Int]): Boolean = option match {
      case None => true
      case Some(_) => false
    }

    assert(pm(some) === some.isEmpty)
    assert(pm(none) === none.isEmpty)
  }


  test("forAll") {

    def foo(int: Int): Boolean = int % 2 == 0

    def pm(option: Option[Int]): Boolean = option match {
      case None => true
      case Some(x) => foo(x)
    }

    assert(pm(some) === some.forall(foo))
    assert(pm(none) === none.forall(foo))
  }


  test("exists") {

    def foo(int: Int): Boolean = int % 2 == 0

    def pm(option: Option[Int]): Boolean = option match {
      case None => false
      case Some(x) => foo(x)
    }

    assert(pm(some) === some.exists(foo))
    assert(pm(none) === none.exists(foo))
  }


  test("orElse") {

    val foo: Option[Int] = Some(3)

    def pm(option: Option[Int]): Option[Int] = option match {
      case None => foo
      case Some(x) => Some(x)
    }

    assert(pm(some) === some.orElse(foo))
    assert(pm(none) === none.orElse(foo))
  }


  test("getOrElse") {

    val foo: Int = 3

    def pm(option: Option[Int]): Int = option match {
      case None => foo
      case Some(x) => x
    }

    assert(pm(some) === some.getOrElse(foo))
    assert(pm(none) === none.getOrElse(foo))
  }


  test("toList") {
    def pm(option: Option[Int]): List[Int] = option match {
      case None => Nil
      case Some(x) => x :: Nil
    }

    assert(pm(some) === some.toList)
    assert(pm(none) === none.toList)
  }
}

