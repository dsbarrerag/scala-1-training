package co.com.scalatraining.consultaRepositorios

import scala.util.Random

object Data {
  val random = new Random

  def generarRepositorios: Option[List[String]] =
    Option {
      Range(1, random.nextInt(10))
        .map(_ => s"${randomNombre} ${randomNombre}")
        .toList
    }

  def generarRepositorio(nombre: String): Repositorio = {
    Repositorio(
      nombre,
      100 + random.nextInt(10000),
      randomLenguaje
    )
  }

  def randomNombre: String = nombres(random.nextInt(nombres.length))

  def randomLenguaje: String = lenguajes(random.nextInt(lenguajes.length))

  val lenguajes: List[String] = List(
    "Java",
    "Scala",
    "C++",
    "C",
    "Ruby",
    "Javascript",
    "Python",
    "PhP",
    "Go",
    "Perl"
  )
  val nombres: List[String] = List(
    "toe ring",
    "stop sign",
    "cork",
    "charger",
    "deodorant",
    "perfume",
    "blouse",
    "plate",
    "watch",
    "shoe lace",
    "bottle",
    "pool stick",
    "tire swing",
    "CD",
    "candle",
    "washing machine",
    "bed",
    "fridge",
    "rubber duck",
    "piano",
    "photo album",
    "puddle",
    "doll",
    "pants",
    "rug",
    "speakers",
    "ring",
    "fork",
    "bread",
    "television",
    "phone",
    "pen",
    "mop",
    "sponge",
    "video games",
    "slipper",
    "cell phone",
    "chair",
    "fake flowers",
    "cookie jar",
    "candy wrapper",
    "model car",
    "computer",
    "grid paper",
    "ipod",
    "desk",
    "radio",
    "bow",
    "scotch tape",
    "bookmark"
  )
}
