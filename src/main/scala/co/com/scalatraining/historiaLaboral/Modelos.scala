package co.com.scalatraining.historiaLaboral

case class Periodo(fecha: String)

case class Cotizacion(periodo: Periodo, aportante: String, dias: Int, IBC: Int)

case class CotizacionSalario(periodo: Periodo, aportante: String, salario: Int)

object Cotizacion {
  def empty: Cotizacion = Cotizacion(Periodo(""), "", 0, 0)
}

case class HistoriaLaboral(periodo: Periodo, salario: Int)