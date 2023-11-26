package com.fatec_gab_viana.navy.models

data class CarroNew(
    var imagem: String,
    var grupo: String,
    var modeloCarro:String,
    var marcaCarro: String,
    var placaCarro: String,
    var ano: Int,
    var km : Double,
    var potencia: Double,
    var combustivelCarro: String,
    var preco: Double,
    var valorHora: Double,
    var valorKmRodado: Double,
    var cambioCarro: String,
    var assentos: Int,
    var filial: String,
    var disponivel: Boolean,
    var ipvaPago: Boolean){
    // Construtor sem argumentos
    constructor() : this("", "", "", "", "", 0, 0.0, 0.0, "", 0.0, 0.0, 0.0, "", 0,"", false, false)
}
