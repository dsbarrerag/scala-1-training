package co.com.scalatraining.consultaRepositorios

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class RepoLenguaje(lenguaje: String, numero: Int)

case class UsuarioCompleto(respositorios: List[Repositorio], resumen: List[RepoLenguaje])

object ServiciosRepositorios {

  def obtenerUsuarioCompleto(usuario: String): Future[Option[UsuarioCompleto]] = {
    GitHubServices.repositoriosDeUsuario(usuario).flatMap { opt =>
      opt.map(rep =>
        Future.sequence {
          rep.map(nombre => GitHubServices.repositorio(nombre))
        }
      ).fold[Future[Option[UsuarioCompleto]]](Future.successful(None))(f =>
        f.map(repos => Option(crearUsuarioCompleto(repos))))
      /*
      match {
        case Some(f) => f.map( repos => Option(crearUsuarioCompleto(repos)))
        case None    => Future.successful(None)
      }
      */
    }
  }

  private def crearUsuarioCompleto(repositorios: List[Repositorio]): UsuarioCompleto = {
    UsuarioCompleto(
      repositorios.sortBy(_.lineas).reverse,
      repositorioPorLenguaje(repositorios).sortBy(_.numero).reverse
    )
  }

  private def repositorioPorLenguaje(repositorios: List[Repositorio]): List[RepoLenguaje] = {
    repositorios.groupBy(_.lenguaje).mapValues(_.length).map {
      repos => RepoLenguaje(repos._1, repos._2)
    }.toList
  }

}
