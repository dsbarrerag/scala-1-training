package co.com.scalatraining.consultarRepositorios

import co.com.scalatraining.consultaRepositorios.{ServiciosRepositorios, UsuarioCompleto}
import org.scalatest.{FunSuite}

import scala.concurrent.duration._

import scala.concurrent.{Await, Future}

class ServiciosRepositoriosSpecs extends FunSuite {

  test("Pedir un repositorio") {
    val usuarioCompleto: Future[Option[UsuarioCompleto]] = ServiciosRepositorios.obtenerUsuarioCompleto("David")

    val opt = Await.result(usuarioCompleto, 5 seconds)
    opt.map {
      res =>

        println(res.respositorios)
        println(res.resumen)

        assert(res.respositorios === res.respositorios.sortWith(_.lineas > _.lineas))

        assert(res.respositorios.groupBy(_.lenguaje)
          .mapValues(_.size).toSet === res.resumen.map(x => (x.lenguaje, x.numero)).toSet)

        assert(true)
    }
  }

}
