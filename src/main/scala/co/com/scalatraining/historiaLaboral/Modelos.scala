package co.com.scalatraining.historiaLaboral

case class Periodo(fecha: String)

case class Cotizacion(periodo: Periodo, aportante: String, dias: Int, IBC: Int)

case class CotizacionSalario(periodo: Periodo, aportante: String, dias: Int, salario: Int)

object CotizacionSalario {
  def apply(cotizacion: Cotizacion, salario: Int): CotizacionSalario =
    new CotizacionSalario(cotizacion.periodo, cotizacion.aportante, cotizacion.dias, salario)

  def apply(): CotizacionSalario =
    new CotizacionSalario(Periodo(""), "", 0, 0)
}

case class HistoriaLaboral(periodo: Periodo, salario: Int)