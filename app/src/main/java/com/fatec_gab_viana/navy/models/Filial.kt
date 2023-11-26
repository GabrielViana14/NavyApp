package com.fatec_gab_viana.navy.models

data class Filial(
    var nome: String?=null,
    var cep: String?=null,
    var estado: String?=null,
    var cidade: String?=null,
    var bairro: String?=null,
    var rua: String?=null,
    var numero: String?=null,
    var complemento: String?=null,
    var latitude: Double=0.0,
    var longitude: Double=0.0)
