package co.com.scalatraining.collections

import co.com.scalatraining.historiaLaboral.{Cotizacion, HistoriaLaboral, LimpiarHistoriaLaboral, Periodo}
import org.scalatest.FunSuite

class HistoriaLaboralSpecs extends FunSuite {

  test("Historia Laboral debe limpiar cotizaciones") {

    val periodos: List[Cotizacion] = List(

      // Dias en 0, IBC en 0
      Cotizacion(Periodo("2018/07"), "s4n", 0, 1000000),
      Cotizacion(Periodo("2018/07"), "s4n", 30, 0),

      //Registro Repetido
      Cotizacion(Periodo("2018/07"), "s4n", 10, 1000000),
      Cotizacion(Periodo("2018/07"), "s4n", 10, 1000000),

      Cotizacion(Periodo("2018/07"), "s4n", 20, 2000000),
      Cotizacion(Periodo("2018/08"), "s4n", 30, 2000000),

      //Cotizantes diferentes
      Cotizacion(Periodo("2018/09"), "s4n", 15, 1000000),
      Cotizacion(Periodo("2018/09"), "universidad", 15, 2000000)
    )

    val periodosLimpios = LimpiarHistoriaLaboral.limpiar(periodos)

    println(periodosLimpios)

    val res = Set(
      HistoriaLaboral(Periodo("2018/07"), 3000000),
      HistoriaLaboral(Periodo("2018/08"), 2000000),
        HistoriaLaboral(Periodo("2018/09"), 6000000)
    )

    assert(periodosLimpios === res)
  }

}
