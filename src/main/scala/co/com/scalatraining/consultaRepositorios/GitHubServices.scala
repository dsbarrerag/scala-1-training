package co.com.scalatraining.consultaRepositorios

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

case class Repositorio(nombre: String, lineas: Int, lenguaje: String)

object GitHubServices {

  def repositoriosDeUsuario(usuario: String): Future[Option[List[String]]] = Future {
    Thread.sleep(500)
    Data.generarRepositorios
  }

  def repositorio(nombre: String): Future[Repositorio] = Future {
    Thread.sleep(100)
    Data.generarRepositorio(nombre)
  }

}
