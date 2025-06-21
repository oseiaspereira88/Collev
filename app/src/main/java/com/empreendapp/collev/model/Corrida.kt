package com.empreendapp.collev.model

import java.util.*

/**
 * Representa uma corrida solicitada no aplicativo de taxi.
 */
data class Corrida(
    var id: String? = null,
    var passageiro: String? = null,
    var motorista: String? = null,
    var origem: String? = null,
    var destino: String? = null,
    var status: String? = null,
    var valor: Double? = null,
    var dataSolicitacao: Date? = null,
    var dataInicio: Date? = null,
    var dataFim: Date? = null
)
