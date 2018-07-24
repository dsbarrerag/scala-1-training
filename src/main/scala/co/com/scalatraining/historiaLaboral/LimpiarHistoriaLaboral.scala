package co.com.scalatraining.historiaLaboral

object LimpiarHistoriaLaboral {


  def limpiar(cotizaciones: List[Cotizacion]): Set[HistoriaLaboral] = {
    cotizaciones.groupBy(_.periodo)
      .map(cot => HistoriaLaboral(cot._1, limpiarPeriodo(cot._2).IBC))
      .toSet
  }

  private def limpiarPeriodo(cotizaciones: List[Cotizacion]): Cotizacion = {
    cotizaciones
      .filterNot(cotizacion => cotizacion.IBC == 0 || cotizacion.dias == 0)
      .distinct
      .map(cot => cot.copy(IBC = calcularSalario(cot)))
      .groupBy(_.aportante)
      .mapValues(cotizaciones =>
        cotizaciones.fold(Cotizacion.empty)((acc, curr) =>
          curr.copy(dias = acc.dias.max(curr.dias), IBC = acc.IBC.max(curr.IBC))))
      .values
      .fold(Cotizacion.empty)((acc, curr) => curr.copy(IBC = acc.IBC + curr.IBC))
  }

  private def calcularSalario(cotizacion: Cotizacion): Int = {
    Math.round((cotizacion.IBC.toFloat / cotizacion.dias) * 30)
  }

}
