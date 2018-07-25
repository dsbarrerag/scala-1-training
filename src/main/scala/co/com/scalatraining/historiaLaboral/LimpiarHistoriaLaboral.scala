package co.com.scalatraining.historiaLaboral

object LimpiarHistoriaLaboral {


  def limpiar(cotizaciones: List[Cotizacion]): Set[HistoriaLaboral] = {
    cotizaciones.groupBy(_.periodo)
      .map(cot => HistoriaLaboral(cot._1, limpiarPeriodo(cot._2).salario))
      .toSet
  }

  private def limpiarPeriodo(cotizaciones: List[Cotizacion]): CotizacionSalario = {
    cotizaciones
      .filterNot(cotizacion => cotizacion.IBC == 0 || cotizacion.dias == 0)
      .distinct
      .map(cot => calcularSalario(cot))
      .groupBy(_.aportante)
      .mapValues(cotSal => cotSal.maxBy(cot => (cot.salario, cot.dias)))
      .values
      .fold(CotizacionSalario())((acc, curr) => curr.copy(salario = acc.salario + curr.salario))
  }

  private def calcularSalario(cotizacion: Cotizacion): CotizacionSalario = {
    CotizacionSalario(
      cotizacion,
      Math.round((cotizacion.IBC.toFloat / cotizacion.dias) * 30)
    )
  }

}
